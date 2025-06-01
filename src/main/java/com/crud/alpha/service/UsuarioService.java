package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todas las localidades
    public List<Usuario> listEntities() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por su id de Clerk.
    public Usuario findEntity(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Usuario> entityOptional = usuarioRepository.findByClerkId(clerkId);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Usuario no encontrado para el clerkId: " + clerkId);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar usuario por clerkId: " + clerkId, e);
        }
    }

    // Desactivar un usuario.
    public void desactivarUsuario(String clerkId) {
        try {
            Usuario usuario = findEntity(clerkId);
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        } catch (ServiceException e) {
            throw e;
        }
    }

    // Activar un usuario.
    public void activarUsuario(String clerkId) {
        try {
            Usuario usuario = findEntity(clerkId);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);
        } catch (ServiceException e) {
            throw e;
        }
    }

}