package com.example.backend_nutripoint.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.UsuarioDTO;
import com.example.backend_nutripoint.models.Usuario;
import com.example.backend_nutripoint.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll(){
        return usuarioRepository.findAll().stream()
            .map(this::convertToDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Integer id){
        Usuario user = usuarioRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: "+ id));
        return convertToDTO(user);
        }

    @Transactional
    public UsuarioDTO updateUser(Integer id, UsuarioDTO userDTO){
        Usuario user = usuarioRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado por ID: "+id));

            user.setNombres(userDTO.getNombres());
            user.setApellidos(userDTO.getApellidos());
            user.setDni(userDTO.getDni());
            user.setEmail(userDTO.getEmail());
            user.setTelefono(userDTO.getTelefono());

            usuarioRepository.save(user);
            return convertToDTO(user);
    }

    private UsuarioDTO convertToDTO(Usuario user) {
        return UsuarioDTO.builder()
            .nombres(user.getNombres())
            .apellidos(user.getApellidos())
            .email(user.getEmail())
            .dni(user.getDni())
            .telefono(user.getTelefono())
            .build();
    }
}
