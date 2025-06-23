package com.taskmanager.gamified.mapper;

import com.taskmanager.gamified.dto.EventoDTO;
import com.taskmanager.gamified.entity.Evento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventoMapper {
    
    public EventoDTO toDTO(Evento evento) {
        if (evento == null) return null;
        
        return new EventoDTO(
            evento.getId(),
            evento.getNombre(),
            evento.getDescripcion(),
            evento.getMultiplicador(),
            evento.getFechaInicio(),
            evento.getFechaFin(),
            evento.getActivo(),
            evento.getFechaCreacion()
        );
    }
    
    public List<EventoDTO> toDTO(List<Evento> eventos) {
        return eventos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}