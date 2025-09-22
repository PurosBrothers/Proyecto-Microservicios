package com.microservicios.transaction_ms.services;

import com.microservicios.transaction_ms.models.ItemCarrito;
import com.microservicios.transaction_ms.models.ItemTransaccion;
import com.microservicios.transaction_ms.models.Transaccion;
import com.microservicios.transaction_ms.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository repository;

    @Autowired
    private CarritoCompraService carritoCompraService;

    @Autowired
    private ItemTransaccionService itemTransaccionService;

    public Transaccion create(Transaccion transaccion) {
        return repository.save(transaccion);
    }

    public Transaccion update(Long id, Transaccion transaccion) {
        transaccion.setId(id);
        return repository.save(transaccion);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Transaccion> findAll() {
        return repository.findAll();
    }

    public Optional<Transaccion> findById(Long id) {
        return repository.findById(id);
    }

    public List<Transaccion> findByUID(String uid) {
        return repository.findByUID(uid);
    }

    public List<ItemTransaccion> getItemsTransaccion(String uid) {
        List<Transaccion> transacciones = repository.findByUID(uid);
        return transacciones.stream()
                .flatMap(t -> t.getItemsPagados().stream())
                .collect(Collectors.toList());
    }

    // Crear una transacción a partir de un carrito de compra
    public Transaccion createTransactionFromCarrito(String uid) {
        List<ItemCarrito> cartItems = carritoCompraService.getCarritoItems(uid);
        if (cartItems.isEmpty()) {
            return null;
        }

        // Convertir items de carrito (por pagar) a items de transacción (pagados)
        List<ItemTransaccion> itemsTransaccion = cartItems.stream().map(item -> {
            ItemTransaccion it = new ItemTransaccion();
            it.setReservaId(item.getIdItem());
            it.setCantidad(item.getCantidad());
            it.setPrecioUnitario(item.getPrecioUnitario());
            it.setFechaIncioServicio(LocalDate.now()); // assuming
            it.setFechaFinServicio(LocalDate.now().plusDays(1)); // assuming
            return it;
        }).collect(Collectors.toList());

        // Guardar items de transacción
        List<ItemTransaccion> savedItems = itemsTransaccion.stream()
                .map(itemTransaccionService::create)
                .collect(Collectors.toList());

        // calcular total
        BigDecimal total = savedItems.stream()
                .map(it -> it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // crear transacción
        Transaccion trans = new Transaccion();
        trans.setUID(uid);
        trans.setEstado("PENDING");
        trans.setFechaTransaccion(LocalDate.now());
        trans.setMontoTotal(total);
        trans.setItemsPagados(savedItems);
        trans.setItemsPorPagar(List.of());

        Transaccion savedTrans = repository.save(trans);

        // remover items del carrito
        for (ItemCarrito item : cartItems) {
            carritoCompraService.removeItemFromCarrito(uid, item.getIdItem());
        }

        return savedTrans;
    }
}