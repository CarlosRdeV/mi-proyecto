package com.taskmanager.gamified.mapper;

import com.taskmanager.gamified.dto.UsuarioDTO;
import com.taskmanager.gamified.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {
    
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getNivel(),
            usuario.getExperiencia(),
            usuario.getStreakActual(),
            usuario.getFechaUltimoLogin(),
            usuario.getFechaCreacion()
        );
    }
    
    public List<UsuarioDTO> toDTO(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario(dto.getNombre());
        usuario.setId(dto.getId());
        usuario.setNivel(dto.getNivel());
        usuario.setExperiencia(dto.getExperiencia());
        usuario.setStreakActual(dto.getStreakActual());
        usuario.setFechaUltimoLogin(dto.getFechaUltimoLogin());
        usuario.setFechaCreacion(dto.getFechaCreacion());
        
        return usuario;
    }
}