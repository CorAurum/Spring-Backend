package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Administrador.Administrador;
import com.crud.alpha.clase.Usuarios.Administrador.AdministradorDTO;
import com.crud.alpha.clase.Usuarios.Administrador.AdministradorUpdateDTO;
import com.crud.alpha.clase.Usuarios.Administrador.NewAdministradorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.AdministradorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    // Obtener todos los administradores.
    public List<Administrador> listEntities() {
        return administradorRepository.findAll();
    }

    // Obtener un administrador por su id del sistema.
    public Optional<Administrador> obtenerPorId(Long id) {
        return administradorRepository.findById(id);
    }

    // Obtener un administrador por su id de Clerk.
    public Administrador findEntity(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Administrador> adminOptional = administradorRepository.findByClerkId(clerkId);
            if (adminOptional.isEmpty()) {
                throw new EntityNotFoundException("Administrador no encontrado para el clerkId: " + clerkId);
            }
            return adminOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar administrador por clerkId: " + clerkId, e);
        }
    }


    // Obtener un administrador por su email.
    public Administrador findEntityByEmail(String email) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        try {
            Optional<Administrador> entityOptional = administradorRepository.findByEmail(email);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Administrador no encontrado para el email: " + email);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar administrador por email: " + email, e);
        }
    }

    // Crear un nuevo administrador.
    @Transactional
    public NewAdministradorDTO createEntity(NewAdministradorDTO entityDTO) {
        try {
            // Validate uniqueness of clerkId and email if provided.
            if (entityDTO.getClerkId() != null && administradorRepository.existsByClerkId(entityDTO.getClerkId())) {
                throw new IllegalArgumentException("El clerkId ya está en uso: " + entityDTO.getClerkId());
            }
            if (entityDTO.getEmail() != null && administradorRepository.existsByEmail(entityDTO.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso: " + entityDTO.getEmail());
            }

            // Find who created the user
            Administrador administrador = findEntity(entityDTO.getRegisteredBy());
            if (entityDTO.getRegisteredBy() != null && administrador == null) {
                throw new IllegalArgumentException("No existe un administrador para el clerkId: " + entityDTO.getRegisteredBy());
            }

            // Convert DTO into an entity so that we can save it.
            Administrador entity = new Administrador();
            entity.setClerkId(entityDTO.getClerkId());
            entity.setEmail(entityDTO.getEmail());
            entity.setNombre(entityDTO.getNombre());
            entity.setApellido(entityDTO.getApellido());
            entity.setActivo(entityDTO.isActivo());
            entity.setFechaNacimiento(entityDTO.getFechaNacimiento());
            entity.setFechaIngreso(entityDTO.getFechaIngreso());
            entity.setRegistroUsuario(new ArrayList<>());
            entity.setRegisteredBy(administrador);

            // Save entity.
            Administrador savedEntity = administradorRepository.save(entity);

            // Convert saved entity to DTO.
            NewAdministradorDTO savedEntityDTO = new NewAdministradorDTO();
            savedEntityDTO.setClerkId(savedEntity.getClerkId());
            savedEntityDTO.setEmail(savedEntity.getEmail());
            savedEntityDTO.setNombre(savedEntity.getNombre());
            savedEntityDTO.setApellido(savedEntity.getApellido());
            savedEntityDTO.setActivo(savedEntity.isActivo());
            savedEntityDTO.setFechaNacimiento(savedEntity.getFechaNacimiento());
            savedEntityDTO.setFechaIngreso(savedEntity.getFechaIngreso());

            return savedEntityDTO;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al guardar administrador", e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al guardar administrador", e);
        }
    }


    // Actualizar los datos de un administrador.
    @Transactional
    public AdministradorDTO updateEntity(String clerkId, AdministradorUpdateDTO entityUpdateDTO) {
        try {
            // Fetch the existing administrator by ClerkId.
            Administrador entity = findEntity(clerkId); // Throws UserNotFoundException if not found

            // Update the provided fields.
            if (entityUpdateDTO.getNombre() != null) {
                entity.setNombre(entityUpdateDTO.getNombre());
            }
            if (entityUpdateDTO.getApellido() != null) {
                entity.setApellido(entityUpdateDTO.getApellido());
            }
            if (entityUpdateDTO.getFechaNacimiento() != null) {
                entity.setFechaNacimiento(entityUpdateDTO.getFechaNacimiento());
            }
            if (entityUpdateDTO.getFechaIngreso() != null) {
                entity.setFechaIngreso(entityUpdateDTO.getFechaIngreso());
            }

            // Save the updated entity.
            Administrador updatedEntity = administradorRepository.save(entity);

            // Convert to DTO.
            AdministradorDTO updatedEntityDTO = new AdministradorDTO();
            updatedEntityDTO.setEmail(updatedEntity.getEmail());
            updatedEntityDTO.setNombre(updatedEntity.getNombre());
            updatedEntityDTO.setApellido(updatedEntity.getApellido());
            updatedEntityDTO.setFechaNacimiento(updatedEntity.getFechaNacimiento());
            updatedEntityDTO.setFechaIngreso(updatedEntity.getFechaIngreso());


            // return the updated object.
            return updatedEntityDTO;
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al actualizar administrador con clerkId: " + clerkId, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al actualizar administrador con clerkId: " + clerkId, e);
        }
    }

    // Eliminar un administrador por su id de Clerk.
    @Transactional
    public void deleteEntity(String clerkId) {
        try {
            // Fetch admin (obtenerPorClerkID handles validation and exceptions)
            Administrador admin = findEntity(clerkId);
            // Delete admin
            eliminarAdministradorPorId(admin.getId());
        } catch (IllegalArgumentException | EntityNotFoundException | ServiceException e) {
            throw e; // Rethrow to let controller handle
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar administrador por clerkId: " + clerkId, e);
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
                throw new EntityNotFoundException("Administrador no encontrado para el id: " + id);
            }
            // Delete admin
            administradorRepository.deleteById(id);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al eliminar administrador por id: " + id, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar administrador por id: " + id, e);
        }
    }

}
