package com.taskmanager.gamified.mapper;

import com.taskmanager.gamified.dto.TareaDTO;
import com.taskmanager.gamified.entity.Tarea;
import com.taskmanager.gamified.entity.TareaEspecial;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TareaMapper {
    
    public TareaDTO toDTO(Tarea tarea) {
        if (tarea == null) return null;
        
        TareaDTO dto = new TareaDTO();
        dto.setId(tarea.getId());
        dto.setNombre(tarea.getNombre());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setDificultad(tarea.getDificultad());
        dto.setXpBase(tarea.getXpBase());
        dto.setRepetible(tarea.getRepetible());
        dto.setActiva(tarea.getActiva());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        
        if (tarea instanceof TareaEspecial) {
            TareaEspecial tareaEspecial = (TareaEspecial) tarea;
            dto.setTipoTarea("ESPECIAL");
            dto.setFechaLimite(tareaEspecial.getFechaLimite());
            dto.setUnaVezPorUsuario(tareaEspecial.getUnaVezPorUsuario());
        } else {
            dto.setTipoTarea("NORMAL");
        }
        
        return dto;
    }
    
    public List<TareaDTO> toDTO(List<Tarea> tareas) {
        return tareas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}