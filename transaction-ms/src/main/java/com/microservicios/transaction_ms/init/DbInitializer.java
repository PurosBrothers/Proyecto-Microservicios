package com.microservicios.transaction_ms.init;

import com.microservicios.transaction_ms.models.ItemCarrito;
import com.microservicios.transaction_ms.services.CarritoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private CarritoCompraService carritoCompraService;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar datos de ejemplo para el carrito de compra con UID "1"

        // Crear algunos items para el carrito
        ItemCarrito item1 = new ItemCarrito();
        item1.setIdItem(101L); // ID de la reserva o item
        item1.setCantidad(2);
        item1.setPrecioUnitario(new BigDecimal("50.00"));
        item1.setFechaAgregado(LocalDate.now());

        ItemCarrito item2 = new ItemCarrito();
        item2.setIdItem(102L);
        item2.setCantidad(1);
        item2.setPrecioUnitario(new BigDecimal("75.50"));
        item2.setFechaAgregado(LocalDate.now());

        // Agregar items al carrito del usuario con UID "1"
        carritoCompraService.addItemToCarrito("1", item1);
        carritoCompraService.addItemToCarrito("1", item2);

        System.out.println("Datos iniciales del carrito de compra cargados para UID '1'");
    }
}