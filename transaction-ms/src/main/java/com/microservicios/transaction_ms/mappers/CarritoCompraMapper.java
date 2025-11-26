package com.microservicios.transaction_ms.mappers;

import java.util.stream.Collectors;

import com.microservicios.transaction_ms.models.CarritoCompra;
import com.microservicios.transaction_ms.dtos.CarritoCompraDTO;

public class CarritoCompraMapper {

    public static CarritoCompra toModel(CarritoCompraDTO dto) {
        if (dto == null)
            return null;
        CarritoCompra model = new CarritoCompra();
        model.setId(dto.getId());
        model.setUID(dto.getUID());
        model.setFechaCreacion(dto.getFechaCreacion());
        model.setFechaUltimaModificacion(dto.getFechaUltimaModificacion());
        model.setActivo(dto.getActivo());
        if (dto.getItems() != null) {
            model.setItems(dto.getItems().stream().map(ItemCarritoMapper::toModel).collect(Collectors.toList()));
        } else {
            model.setItems(null);
        }
        return model;
    }

    public static CarritoCompraDTO toDTO(CarritoCompra model) {
        if (model == null)
            return null;
        CarritoCompraDTO dto = new CarritoCompraDTO();
        dto.setId(model.getId());
        dto.setUID(model.getUID());
        dto.setFechaCreacion(model.getFechaCreacion());
        dto.setFechaUltimaModificacion(model.getFechaUltimaModificacion());
        dto.setActivo(model.getActivo());
        if (model.getItems() != null) {
            dto.setItems(model.getItems().stream().map(ItemCarritoMapper::toDTO).collect(Collectors.toList()));
        } else {
            dto.setItems(null);
        }
        return dto;
    }

}