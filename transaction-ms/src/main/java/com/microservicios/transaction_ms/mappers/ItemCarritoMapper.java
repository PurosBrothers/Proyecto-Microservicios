package com.microservicios.transaction_ms.mappers;

import com.microservicios.transaction_ms.models.ItemCarrito;
import com.microservicios.transaction_ms.dtos.ItemCarritoDTO;

public class ItemCarritoMapper {

    public static ItemCarrito toModel(ItemCarritoDTO dto) {
        if (dto == null)
            return null;
        ItemCarrito model = new ItemCarrito();
        model.setId(dto.getId());
        model.setIdItem(dto.getIdItem());
        model.setCantidad(dto.getCantidad());
        model.setPrecioUnitario(dto.getPrecioUnitario());
        model.setFechaAgregado(dto.getFechaAgregado());
        return model;
    }

    public static ItemCarritoDTO toDTO(ItemCarrito model) {
        if (model == null)
            return null;
        ItemCarritoDTO dto = new ItemCarritoDTO();
        dto.setId(model.getId());
        dto.setIdItem(model.getIdItem());
        dto.setCantidad(model.getCantidad());
        dto.setPrecioUnitario(model.getPrecioUnitario());
        dto.setFechaAgregado(model.getFechaAgregado());
        return dto;
    }

}