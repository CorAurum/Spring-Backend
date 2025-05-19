package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Administrador;
import com.crud.alpha.clase.Usuarios.dto.NewAdministradorDTO;
import com.crud.alpha.clase.Usuarios.dto.UsuarioUpdateDTO;
import com.crud.alpha.clase.Usuarios.exceptions.AdministradorNotFoundException;
import com.crud.alpha.clase.Usuarios.exceptions.AdministradorServiceException;
import com.crud.alpha.repository.AdministradorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    // Obtener todos los administradores.
    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }

    // Obtener un administrador por su id del sistema.
    public Optional<Administrador> obtenerPorId(Long id) {
        return administradorRepository.findById(id);
    }

    // Obtener un administrador por su id de Clerk.
    public Administrador obtenerPorClerkID(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Administrador> adminOptional = administradorRepository.findByClerkId(clerkId);
            if (adminOptional.isEmpty()) {
                throw new AdministradorNotFoundException("Administrador no encontrado para el clerkId: " + clerkId);
            }
            return adminOptional.get();
        } catch (DataAccessException e) {
            throw new AdministradorServiceException("Error al consultar administrador por clerkId: " + clerkId, e);
        }
    }


    // Obtener un administrador por su Email.
    public Optional<Administrador> obtenerPorEmail(String email) {
        return administradorRepository.findByEmail(email);
    }


    // Crear un nuevo administrador.
    @Transactional
    public NewAdministradorDTO guardarAdministrador(NewAdministradorDTO adminDTO) {
        try {
            // Validate uniqueness of clerkId and email if provided
            if (adminDTO.getClerkId() != null && administradorRepository.existsByClerkId(adminDTO.getClerkId())) {
                throw new IllegalArgumentException("El clerkId ya está en uso: " + adminDTO.getClerkId());
            }
            if (adminDTO.getEmail() != null && administradorRepository.existsByEmail(adminDTO.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso: " + adminDTO.getEmail());
            }

            // Convert DTO to entity
            Administrador admin = new Administrador();
            admin.setClerkId(adminDTO.getClerkId());
            admin.setEmail(adminDTO.getEmail());
            admin.setNombre(adminDTO.getNombre());
            admin.setApellido(adminDTO.getApellido());
            admin.setActivo(adminDTO.isActivo());
            admin.setFechaNacimiento(adminDTO.getFechaNacimiento());

            // Save entity
            Administrador savedAdmin = administradorRepository.save(admin);

            // Convert saved entity to DTO
            NewAdministradorDTO savedAdminDTO = new NewAdministradorDTO();
            savedAdminDTO.setClerkId(savedAdmin.getClerkId());
            savedAdminDTO.setEmail(savedAdmin.getEmail());
            savedAdminDTO.setNombre(savedAdmin.getNombre());
            savedAdminDTO.setApellido(savedAdmin.getApellido());
            savedAdminDTO.setActivo(savedAdmin.isActivo());
            savedAdminDTO.setFechaNacimiento(savedAdmin.getFechaNacimiento());

            return savedAdminDTO;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new AdministradorServiceException("Error al guardar administrador", e);
        } catch (Exception e) {
            throw new AdministradorServiceException("Error inesperado al guardar administrador", e);
        }
    }


    // ACtualizar los datos de un administrador.
    public Administrador actualizarAdministrador(String clerkId, UsuarioUpdateDTO adminActualizado) {
        Optional<Administrador> adminOpt = administradorRepository.findByClerkId(clerkId);

        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();

            if (adminActualizado.getNombre() != null) {
                admin.setNombre(adminActualizado.getNombre());
            }

            if (adminActualizado.getApellido() != null) {
                admin.setApellido(adminActualizado.getApellido());
            }

            if (adminActualizado.getFechaNacimiento() != null) {
                admin.setFechaNacimiento(adminActualizado.getFechaNacimiento());
            }

            return administradorRepository.save(admin);
        } else {
            return null;
        }
    }

    // Eliminar un administrador por su id de Clerk.
    @Transactional
    public void eliminarAdministradorPorClerkId(String clerkId) {
        try {
            // Fetch admin (obtenerPorClerkID handles validation and exceptions)
            Administrador admin = obtenerPorClerkID(clerkId);
            // Delete admin
            eliminarAdministradorPorId(admin.getId());
        } catch (IllegalArgumentException | AdministradorNotFoundException | AdministradorServiceException e) {
            throw e; // Rethrow to let controller handle
        } catch (Exception e) {
            throw new AdministradorServiceException("Error inesperado al eliminar administrador por clerkId: " + clerkId, e);
        }
    }

    // Eliminar un administrador por su id del sistema.
    @Transactional
    public void eliminarAdministradorPorId(Long id) {
        try {
            // Input validation
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser nulo");
            }
            // Verify existence (optional, for consistent not-found handling)
            if (!administradorRepository.existsById(id)) {
                throw new AdministradorNotFoundException("Administrador no encontrado para el id: " + id);
            }
            // Delete admin
            administradorRepository.deleteById(id);
        } catch (IllegalArgumentException | AdministradorNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new AdministradorServiceException("Error al eliminar administrador por id: " + id, e);
        } catch (Exception e) {
            throw new AdministradorServiceException("Error inesperado al eliminar administrador por id: " + id, e);
        }
    }
}
