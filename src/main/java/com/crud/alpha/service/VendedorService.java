package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Vendedor.NewVendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.VendedorUpdateDTO;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository vendedorRepository;

    // Obtener todos los vendedores.
    public List<Vendedor> listEntities() {
        return vendedorRepository.findAll();
    }

    // Obtener un vendedor por su id del sistema.
    public Optional<Vendedor> obtenerPorId(Long id) {
        return vendedorRepository.findById(id);
    }

    // Obtener un vendedor por su id de Clerk.
    public Vendedor findEntity(String clerkId) {
        // Input validation
        if (clerkId == null || clerkId.trim().isEmpty()) {
            throw new IllegalArgumentException("El clerkId no puede ser nulo o vacío");
        }

        try {
            Optional<Vendedor> entityOptional = vendedorRepository.findByClerkId(clerkId);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Vendedor no encontrado para el clerkId: " + clerkId);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar vendedor por clerkId: " + clerkId, e);
        }
    }

    // Obtener un vendedor por su email.
    public Vendedor findEntityByEmail(String email) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        try {
            Optional<Vendedor> entityOptional = vendedorRepository.findByEmail(email);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Vendedor no encontrado para el email: " + email);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar vendedor por email: " + email, e);
        }
    }


    // Crear un nuevo vendedor.
    @Transactional
    public NewVendedorDTO createEntity(NewVendedorDTO entityDTO) {
        try {
            // Validate uniqueness of clerkId and email if provided
            if (entityDTO.getClerkId() != null && vendedorRepository.existsByClerkId(entityDTO.getClerkId())) {
                throw new IllegalArgumentException("El clerkId ya está en uso: " + entityDTO.getClerkId());
            }
            if (entityDTO.getEmail() != null && vendedorRepository.existsByEmail(entityDTO.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso: " + entityDTO.getEmail());
            }

            // Convert DTO into an entity so that we can save it.
            Vendedor entity = new Vendedor();
            entity.setClerkId(entityDTO.getClerkId());
            entity.setEmail(entityDTO.getEmail());
            entity.setNombre(entityDTO.getNombre());
            entity.setApellido(entityDTO.getApellido());
            entity.setActivo(entityDTO.isActivo());
            entity.setFechaNacimiento(entityDTO.getFechaNacimiento());
            entity.setFechaIngreso(entityDTO.getFechaIngreso());

            // Save entity
            Vendedor savedVendedor = vendedorRepository.save(entity);

            // Convert saved entity to DTO
            NewVendedorDTO savedVendedorDTO = new NewVendedorDTO();
            savedVendedorDTO.setClerkId(savedVendedor.getClerkId());
            savedVendedorDTO.setEmail(savedVendedor.getEmail());
            savedVendedorDTO.setNombre(savedVendedor.getNombre());
            savedVendedorDTO.setApellido(savedVendedor.getApellido());
            savedVendedorDTO.setActivo(savedVendedor.isActivo());
            savedVendedorDTO.setFechaNacimiento(savedVendedor.getFechaNacimiento());
            savedVendedorDTO.setFechaIngreso(savedVendedor.getFechaIngreso());
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Seguramente hacemos otra funcion aparte en el service para updatearle/crearle los omnibus creados, registro viaje y registro localidad.
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            return savedVendedorDTO;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al guardar vendedor", e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al guardar vendedor", e);
        }
    }


    // Actualizar los datos de un vendedor.
    @Transactional
    public VendedorDTO updateEntity(String clerkId, VendedorUpdateDTO entityUpdateDTO) {
        try {
            // Fetch the existing seller by ClerkId.
            Vendedor entity = findEntity(clerkId); // Throws UserNotFoundException if not found

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
            Vendedor updatedEntity = vendedorRepository.save(entity);

            // Convert to DTO.
            VendedorDTO updatedEntityDTO = new VendedorDTO();
            updatedEntityDTO.setEmail(updatedEntity.getEmail());
            updatedEntityDTO.setNombre(updatedEntity.getNombre());
            updatedEntityDTO.setApellido(updatedEntity.getApellido());
            updatedEntityDTO.setFechaNacimiento(updatedEntity.getFechaNacimiento());
            updatedEntityDTO.setFechaIngreso(updatedEntity.getFechaIngreso());
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Seguramente hacemos otra funcion aparte en el service para updatearle/crearle los omnibus creados, registro viaje y registro localidad.
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            // return the updated object.
            return updatedEntityDTO;
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al actualizar vendedor con clerkId: " + clerkId, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al actualizar vendedor con clerkId: " + clerkId, e);
        }
    }

    // Eliminar un vendedor por su id de Clerk.
    @Transactional
    public void deleteEntity(String clerkId) {
        try {
            // Fetch the seller (obtenerPorClerkID handles validation and exceptions)
            Vendedor vendedor = findEntity(clerkId);
            // Delete the seller.
            eliminarVendedorPorId(vendedor.getId());
        } catch (IllegalArgumentException | EntityNotFoundException | ServiceException e) {
            throw e; // Rethrow to let controller handle
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar vendedor por clerkId: " + clerkId, e);
        }
    }

    // Eliminar un vendedor por su id del sistema.
    @Transactional
    public void eliminarVendedorPorId(Long id) {
        try {
            // Input validation.
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser nulo");
            }
            // Verify existence (optional, for consistent not-found handling).
            if (!vendedorRepository.existsById(id)) {
                throw new EntityNotFoundException("Vendedor no encontrado para el id: " + id);
            }
            // Delete the seller.
            vendedorRepository.deleteById(id);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al eliminar vendedor por id: " + id, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar vendedor por id: " + id, e);
        }
    }

}
