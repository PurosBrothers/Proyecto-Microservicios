package com.microservicios.transaction_ms.mappers;

import com.microservicios.transaction_ms.models.ItemTransaccion;
import com.microservicios.transaction_ms.dtos.ItemTransaccionDTO;

public class ItemTransaccionMapper {

    public static ItemTransaccion toModel(ItemTransaccionDTO dto) {
        if (dto == null)
            return null;
        ItemTransaccion model = new ItemTransaccion();
        model.setId(dto.getId());
        model.setReservaId(dto.getReservaId());
        model.setOfertaId(dto.getOfertaId());
        model.setCantidad(dto.getCantidad());
        model.setPrecioUnitario(dto.getPrecioUnitario());
        model.setFechaIncioServicio(dto.getFechaIncioServicio());
        model.setFechaFinServicio(dto.getFechaFinServicio());
        return model;
    }

    public static ItemTransaccionDTO toDTO(ItemTransaccion model) {
        if (model == null)
            return null;
        ItemTransaccionDTO dto = new ItemTransaccionDTO();
        dto.setId(model.getId());
        dto.setReservaId(model.getReservaId());
        dto.setOfertaId(model.getOfertaId());
        dto.setCantidad(model.getCantidad());
        dto.setPrecioUnitario(model.getPrecioUnitario());
        dto.setFechaIncioServicio(model.getFechaIncioServicio());
        dto.setFechaFinServicio(model.getFechaFinServicio());
        return dto;
    }

}