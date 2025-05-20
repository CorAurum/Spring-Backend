package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.Usuarios.Vendedor.NewVendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorUpdateDTO;
import com.crud.alpha.clase.Usuarios.exceptions.ServiceException;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.repository.UsuarioRepository;
import com.crud.alpha.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener un usuario por su id de Clerk.
    public Usuario findEntity(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Usuario> entityOptional = usuarioRepository.findByClerkId(clerkId);
            if (entityOptional.isEmpty()) {
                throw new UsuarioNotFoundException("Usuario no encontrado para el clerkId: " + clerkId);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar usuario por clerkId: " + clerkId, e);
        }
    }

}
