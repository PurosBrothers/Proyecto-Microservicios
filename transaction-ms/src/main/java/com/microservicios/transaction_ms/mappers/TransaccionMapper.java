package com.microservicios.transaction_ms.mappers;

import java.util.stream.Collectors;

import com.microservicios.transaction_ms.models.Transaccion;
import com.microservicios.transaction_ms.dtos.TransaccionDTO;

public class TransaccionMapper {

    public static Transaccion toModel(TransaccionDTO dto) {
        if (dto == null)
            return null;
        Transaccion model = new Transaccion();
        model.setId(dto.getId());
        model.setUID(dto.getUID());
        model.setEstado(dto.getEstado());
        model.setFechaTransaccion(dto.getFechaTransaccion());
        model.setMontoTotal(dto.getMontoTotal());
        model.setCodigoConfirmacion(dto.getCodigoConfirmacion());
        model.setObservaciones(dto.getObservaciones());
        if (dto.getItemsPagados() != null) {
            model.setItemsPagados(
                    dto.getItemsPagados().stream().map(ItemTransaccionMapper::toModel).collect(Collectors.toList()));
        } else {
            model.setItemsPagados(null);
        }
        if (dto.getItemsPorPagar() != null) {
            model.setItemsPorPagar(
                    dto.getItemsPorPagar().stream().map(ItemTransaccionMapper::toModel).collect(Collectors.toList()));
        } else {
            model.setItemsPorPagar(null);
        }
        return model;
    }

    public static TransaccionDTO toDTO(Transaccion model) {
        if (model == null)
            return null;
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(model.getId());
        dto.setUID(model.getUID());
        dto.setEstado(model.getEstado());
        dto.setFechaTransaccion(model.getFechaTransaccion());
        dto.setMontoTotal(model.getMontoTotal());
        dto.setCodigoConfirmacion(model.getCodigoConfirmacion());
        dto.setObservaciones(model.getObservaciones());
        if (model.getItemsPagados() != null) {
            dto.setItemsPagados(
                    model.getItemsPagados().stream().map(ItemTransaccionMapper::toDTO).collect(Collectors.toList()));
        } else {
            dto.setItemsPagados(null);
        }
        if (model.getItemsPorPagar() != null) {
            dto.setItemsPorPagar(
                    model.getItemsPorPagar().stream().map(ItemTransaccionMapper::toDTO).collect(Collectors.toList()));
        } else {
            dto.setItemsPorPagar(null);
        }
        return dto;
    }

}