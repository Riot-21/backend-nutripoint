package com.example.backend_nutripoint.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend_nutripoint.DTO.requests.UsuarioUpdateDTO;
import com.example.backend_nutripoint.DTO.responses.UsuarioResponseDTO;
import com.example.backend_nutripoint.exceptions.NotFoundException;
import com.example.backend_nutripoint.mappers.UsuarioMapper;
import com.example.backend_nutripoint.models.Usuario;
import com.example.backend_nutripoint.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> UsuarioMapper.userToDTO(u))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Integer id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + id));
        return UsuarioMapper.userToDTO(user);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findByEmail(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email));
        return UsuarioMapper.userToDTO(user);
    }

    @Transactional
    public UsuarioResponseDTO updateUser(Integer id, UsuarioUpdateDTO userDTO) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado por ID: " + id));

        user.setNombres(userDTO.getNombres());
        user.setApellidos(userDTO.getApellidos());
        user.setDni(userDTO.getDni());
        user.setEmail(userDTO.getEmail());
        user.setTelefono(userDTO.getTelefono());

        usuarioRepository.save(user);
        return UsuarioMapper.userToDTO(user);
    }

}
