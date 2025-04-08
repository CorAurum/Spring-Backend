package com.crud.alpha.service;

import com.crud.alpha.clase.Usuario;
import com.crud.alpha.dto.UsuarioUpdateDTO;
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

    // Actualizar datos de un usuario
    public Usuario actualizarUsuario(String clerkId, String nombre, String apellido) {
        // Fetch the existing user by clerkId
        Optional<Usuario> usuarioOpt = usuarioRepository.findByClerkId(clerkId);

        // Get the existing user.
        Usuario usuario = usuarioOpt.get();

        // Update only the user's name and last name.
        if (nombre != null) {
            usuario.setNombre(nombre);
        }

        if (apellido != null) {
            usuario.setApellido(apellido);
        }

        return usuarioRepository.save(usuario);
    }


    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
