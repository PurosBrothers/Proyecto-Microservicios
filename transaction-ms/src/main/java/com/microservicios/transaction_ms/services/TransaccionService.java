package com.microservicios.transaction_ms.services;

import com.microservicios.transaction_ms.dtos.ItemPaymentDTO;
import com.microservicios.transaction_ms.dtos.ProcessPaymentMessageDTO;
import com.microservicios.transaction_ms.messagingrabbitmq.components.RabbitMQSender;
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

    @Autowired
    private RabbitMQSender rabbitMQSender;

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

    public List<Transaccion> getTransacciones(String uid) {
        return repository.findByUID(uid);
    }

    // Crear una transacción a partir de un carrito de compra
    public Transaccion createTransactionFromCarrito(String uid, List<Long> itemIds) {
        List<ItemCarrito> allCartItems = carritoCompraService.getCarritoItems(uid);
        List<ItemCarrito> cartItems;
        if (itemIds == null || itemIds.isEmpty()) {
            cartItems = allCartItems; // procesar todos si no se especifica
        } else {
            cartItems = allCartItems.stream()
                    .filter(item -> itemIds.contains(item.getIdItem()))
                    .collect(Collectors.toList());
        }
        if (cartItems.isEmpty()) {
            return null;
        }

        // Convertir items de carrito (por pagar) a items de transacción (pagados)
        List<ItemTransaccion> itemsTransaccion = cartItems.stream().map(item -> {
            ItemTransaccion it = new ItemTransaccion();
            it.setReservaId(null); // se seteará después
            it.setOfertaId(item.getIdItem());
            it.setCantidad(item.getCantidad());
            it.setPrecioUnitario(item.getPrecioUnitario());
            it.setFechaIncioServicio(LocalDate.now()); // assuming
            it.setFechaFinServicio(LocalDate.now().plusDays(1)); // assuming
            return it;
        }).collect(Collectors.toList());

        // calcular total
        BigDecimal total = itemsTransaccion.stream()
                .map(it -> it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // crear transacción
        Transaccion trans = new Transaccion();
        trans.setUID(uid);
        trans.setEstado("PENDING");
        trans.setFechaTransaccion(LocalDate.now());
        trans.setMontoTotal(total);
        trans.setItemsPagados(List.of()); // inicialmente vacío
        trans.setItemsPorPagar(itemsTransaccion); // items por pagar inicialmente

        Transaccion savedTrans = repository.save(trans);

        // setear reservaId a id de transacción
        for (ItemTransaccion it : savedTrans.getItemsPorPagar()) {
            it.setReservaId(savedTrans.getId());
        }
        repository.save(savedTrans);

        // remover items del carrito
        for (ItemCarrito item : cartItems) {
            carritoCompraService.removeItemFromCarrito(uid, item.getIdItem());
        }

        return savedTrans;
    }

    // Procesar pago enviando mensaje a payment-ms
    public void processPayment(Long transactionId) {
        try {
            Optional<Transaccion> transOpt = repository.findById(transactionId);
            if (transOpt.isPresent()) {
                Transaccion trans = transOpt.get();
                List<ItemPaymentDTO> items = trans.getItemsPorPagar().stream()
                        .map(it -> new ItemPaymentDTO(it.getOfertaId(), it.getCantidad(), it.getPrecioUnitario()))
                        .collect(Collectors.toList());

                ProcessPaymentMessageDTO messageDTO = new ProcessPaymentMessageDTO(
                        trans.getUID(),
                        trans.getId(),
                        trans.getMontoTotal(),
                        items);

                rabbitMQSender.sendProcessPaymentMessage(messageDTO);
                System.out.println("Procesando pago para transacción: " + transactionId);
            } else {
                System.out.println("Transacción no encontrada: " + transactionId);
            }
        } catch (Exception e) {
            System.out.println("Error procesando pago: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Actualizar estado de transacción
    public void updateTransactionStatus(Long transactionId, String status) {
        Optional<Transaccion> transOpt = repository.findById(transactionId);
        if (transOpt.isPresent()) {
            Transaccion trans = transOpt.get();
            trans.setEstado(status);
            repository.save(trans);
        }
    }

    // Completar transacción: mover items de por pagar a pagados y cambiar estado
    public void completeTransaction(Long id) {
        System.out.println("Completando transacción: " + id);
        Optional<Transaccion> opt = repository.findById(id);
        if (opt.isPresent()) {
            Transaccion trans = opt.get();
            System.out.println("Items por pagar antes: " + trans.getItemsPorPagar().size());
            trans.setItemsPagados(trans.getItemsPorPagar());
            trans.setItemsPorPagar(List.of());
            trans.setEstado("COMPLETED");
            repository.save(trans);
            System.out.println("Transacción completada: " + id);
        } else {
            System.out.println("Transacción no encontrada para completar: " + id);
        }
    }
}