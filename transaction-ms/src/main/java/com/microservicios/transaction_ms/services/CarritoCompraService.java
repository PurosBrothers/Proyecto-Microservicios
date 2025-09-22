package com.microservicios.transaction_ms.services;

import com.microservicios.transaction_ms.models.CarritoCompra;
import com.microservicios.transaction_ms.models.ItemCarrito;
import com.microservicios.transaction_ms.repository.CarritoCompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoCompraService {

    @Autowired
    private CarritoCompraRepository repository;

    public CarritoCompra create(CarritoCompra carritoCompra) {
        return repository.save(carritoCompra);
    }

    public CarritoCompra update(Long id, CarritoCompra carritoCompra) {
        carritoCompra.setId(id);
        return repository.save(carritoCompra);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<CarritoCompra> findAll() {
        return repository.findAll();
    }

    public Optional<CarritoCompra> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<CarritoCompra> findByUID(String uid) {
        return repository.findByUID(uid);
    }

    // agregar un item al carrito
    public CarritoCompra addItemToCarrito(String uid, ItemCarrito item) {
        Optional<CarritoCompra> cartOpt = repository.findByUID(uid);
        CarritoCompra cart;
        if (cartOpt.isPresent()) {
            cart = cartOpt.get();
        } else {
            cart = new CarritoCompra();
            cart.setUID(uid);
            cart.setFechaCreacion(LocalDate.now());
            cart.setActivo(true);
        }
        cart.getItems().add(item);
        cart.setFechaUltimaModificacion(LocalDate.now());
        return repository.save(cart);
    }

    // quitar un item del carrito
    public CarritoCompra removeItemFromCarrito(String uid, Long itemId) {
        Optional<CarritoCompra> cartOpt = repository.findByUID(uid);
        if (cartOpt.isPresent()) {
            CarritoCompra cart = cartOpt.get();
            cart.getItems().removeIf(item -> item.getIdItem().equals(itemId));
            cart.setFechaUltimaModificacion(LocalDate.now());
            return repository.save(cart);
        }
        return null;
    }

    // obtener los items del carrito por UID
    public List<ItemCarrito> getCarritoItems(String uid) {
        Optional<CarritoCompra> cartOpt = repository.findByUID(uid);
        return cartOpt.map(CarritoCompra::getItems).orElse(List.of());
    }

}