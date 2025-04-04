package com.crud.alpha.service;

import com.crud.alpha.clase.Usuario;
import com.crud.alpha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Obtener usuario por su 'clerkId'
    public Optional<Usuario> obtenerPorClerkID(String clerkId) {
        return usuarioRepository.findByClerkId(clerkId);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuarioActualizado) {
        // Fetch the existing user by clerkId
        Optional<Usuario> usuarioOpt = usuarioRepository.findByClerkId(usuarioActualizado.getClerkId());

        if (usuarioOpt.isEmpty()) {
            // If no user exists, create a new one (optional, depending on your use case)
            return usuarioRepository.save(usuarioActualizado);
        }

        // Get the existing user
        Usuario usuario = usuarioOpt.get();

        if (usuarioActualizado.getNombre() != null) {
            usuario.setNombre(usuarioActualizado.getNombre());
        }

        if (usuarioActualizado.getApellido() != null) {
            usuario.setApellido(usuarioActualizado.getApellido());
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
