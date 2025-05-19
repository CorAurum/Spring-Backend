package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Cliente;
import com.crud.alpha.clase.Usuarios.dto.ClienteDTO;
import com.crud.alpha.clase.Usuarios.dto.ClienteUpdateDTO;
import com.crud.alpha.clase.Usuarios.dto.NewClienteDTO;
import com.crud.alpha.clase.Usuarios.exceptions.ServiceException;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los clientes.
    public List<Cliente> listEntities() {
        return clienteRepository.findAll();
    }

    // Obtener un cliente por su id del sistema.
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Obtener un cliente por su id de Clerk.
    public Cliente findEntity(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Cliente> entityOptional = clienteRepository.findByClerkId(clerkId);
            if (entityOptional.isEmpty()) {
                throw new UsuarioNotFoundException("Cliente no encontrado para el clerkId: " + clerkId);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar cliente por clerkId: " + clerkId, e);
        }
    }

    // Obtener un cliente por su email.
    public Cliente findEntityByEmail(String email) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        try {
            Optional<Cliente> entityOptional = clienteRepository.findByEmail(email);
            if (entityOptional.isEmpty()) {
                throw new UsuarioNotFoundException("Cliente no encontrado para el email: " + email);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar cliente por email: " + email, e);
        }
    }


    // Crear un nuevo cliente.
    @Transactional
    public NewClienteDTO createEntity(NewClienteDTO entityDTO) {
        try {
            // Validate uniqueness of clerkId and email if provided
            if (entityDTO.getClerkId() != null && clienteRepository.existsByClerkId(entityDTO.getClerkId())) {
                throw new IllegalArgumentException("El clerkId ya está en uso: " + entityDTO.getClerkId());
            }
            if (entityDTO.getEmail() != null && clienteRepository.existsByEmail(entityDTO.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso: " + entityDTO.getEmail());
            }

            // Convert DTO into an entity so that we can save it.
            Cliente entity = new Cliente();
            entity.setClerkId(entityDTO.getClerkId());
            entity.setEmail(entityDTO.getEmail());
            entity.setNombre(entityDTO.getNombre());
            entity.setApellido(entityDTO.getApellido());
            entity.setTipoBenef(entityDTO.getTipoBenef());
            entity.setActivo(entityDTO.isActivo());
            entity.setFechaNacimiento(entityDTO.getFechaNacimiento());

            // Save entity
            Cliente savedClient = clienteRepository.save(entity);

            // Convert saved entity to DTO
            NewClienteDTO savedClientDTO = new NewClienteDTO();
            savedClientDTO.setClerkId(savedClient.getClerkId());
            savedClientDTO.setEmail(savedClient.getEmail());
            savedClientDTO.setNombre(savedClient.getNombre());
            savedClientDTO.setApellido(savedClient.getApellido());
            savedClientDTO.setTipoBenef(savedClient.getTipoBenef());
            savedClientDTO.setActivo(savedClient.isActivo());
            savedClientDTO.setFechaNacimiento(savedClient.getFechaNacimiento());

            return savedClientDTO;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al guardar cliente", e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al guardar cliente", e);
        }
    }


    // Actualizar los datos de un cliente.
    @Transactional
    public ClienteDTO updateEntity(String clerkId, ClienteUpdateDTO entityUpdateDTO) {
        try {
            // Fetch the existing client by ClerkId.
            Cliente entity = findEntity(clerkId); // Throws UserNotFoundException if not found

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

            // Save the updated entity.
            Cliente updatedEntity = clienteRepository.save(entity);

            // Convert to DTO.
            ClienteDTO updatedEntityDTO = new ClienteDTO();
            updatedEntityDTO.setEmail(updatedEntity.getEmail());
            updatedEntityDTO.setNombre(updatedEntity.getNombre());
            updatedEntityDTO.setApellido(updatedEntity.getApellido());
            updatedEntityDTO.setFechaNacimiento(updatedEntity.getFechaNacimiento());

            // return the updated object.
            return updatedEntityDTO;
        } catch (UsuarioNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al actualizar cliente con clerkId: " + clerkId, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al actualizar cliente con clerkId: " + clerkId, e);
        }
    }

    // Eliminar un cliente por su id de Clerk.
    @Transactional
    public void deleteEntity(String clerkId) {
        try {
            // Fetch the client (obtenerPorClerkID handles validation and exceptions)
            Cliente cliente = findEntity(clerkId);
            // Delete the client.
            eliminarClientePorId(cliente.getId());
        } catch (IllegalArgumentException | UsuarioNotFoundException | ServiceException e) {
            throw e; // Rethrow to let controller handle
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar cliente por clerkId: " + clerkId, e);
        }
    }

    // Eliminar un cliente por su id del sistema.
    @Transactional
    public void eliminarClientePorId(Long id) {
        try {
            // Input validation.
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser nulo");
            }
            // Verify existence (optional, for consistent not-found handling).
            if (!clienteRepository.existsById(id)) {
                throw new UsuarioNotFoundException("Cliente no encontrado para el id: " + id);
            }
            // Delete the client.
            clienteRepository.deleteById(id);
        } catch (IllegalArgumentException | UsuarioNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al eliminar cliente por id: " + id, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar cliente por id: " + id, e);
        }
    }

}
