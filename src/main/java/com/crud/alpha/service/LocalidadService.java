package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Localidad.dto.LocalidadDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.ClienteRepository;
import com.crud.alpha.repository.LocalidadRepository;
import com.crud.alpha.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService {

    @Autowired
    private LocalidadRepository LocalidadRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private VendedorService vendedorService;
    @Autowired
    private LocalidadRepository localidadRepository;

    // Obtener todas las localidades
    public List<Localidad> listEntities() {
        return LocalidadRepository.findAll();
    }

    // Obtener Localidad por nombre
    public Localidad findEntity(String nombre) {
        // Input validation
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }

        try {
            Optional<Localidad> entityOptional = localidadRepository.findByNombre(nombre);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Localidad no encontrada para el nombre: " + nombre);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar la localidad de nombre: " + nombre, e);
        }
    }

    // Obtener Localidad por id
    public Localidad findEntityById(Long id) {
        // Input validation
        if (id == null) {
            throw new IllegalArgumentException("La id no puede ser nula o vacía");
        }

        try {
            Optional<Localidad> entityOptional = localidadRepository.findById(id);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Localidad no encontrada con la id: " + id);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar la localidad de id: " + id, e);
        }
    }

    // Crear una nueva localidad.
    @Transactional
    public LocalidadDTO createEntity(LocalidadDTO entityDTO) {
        try {
            if (entityDTO.getRegisteredBy() != null && vendedorService.findEntity(entityDTO.getRegisteredBy()) == null)  {
                throw new IllegalArgumentException("No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
            }

            Vendedor vendedor = vendedorService.findEntity(entityDTO.getRegisteredBy());
            // Convert DTO into an entity so that we can save it.
            Localidad entity = new Localidad();
            entity.setNombre(entityDTO.getNombre());
            entity.setVendedor(vendedor);
            // Save entity.
            Localidad savedLocalidad = localidadRepository.save(entity);

            // Convert saved entity to DTO.
            LocalidadDTO savedEntityDTO = new LocalidadDTO();
            savedEntityDTO.setNombre(savedLocalidad.getNombre());
            savedEntityDTO.setRegisteredBy(entityDTO.getRegisteredBy());

            return savedEntityDTO;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al guardar localidad", e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al guardar localidad", e);
        }
    }

    // Actualizar los datos de una localidad.
    @Transactional
    public LocalidadDTO updateEntity(String nombre, LocalidadDTO entityUpdateDTO) {
        try {
            // Fetch the existing client by name.
            Localidad entity = findEntity(nombre); // Throws UserNotFoundException if not found

            // Update the provided fields.
            if (entityUpdateDTO.getNombre() != null) {
                entity.setNombre(entityUpdateDTO.getNombre());
            }

            // Save the updated entity.
            Localidad updatedEntity = localidadRepository.save(entity);

            // Convert to DTO.
            LocalidadDTO updatedEntityDTO = new LocalidadDTO();
            updatedEntityDTO.setNombre(updatedEntity.getNombre());

            // return the updated object.
            return updatedEntityDTO;
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al actualizar la localidad con nombre: " + nombre, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al actualizar la localidad con nombre: " + nombre, e);
        }
    }

    // Eliminar una localidad por su nombre.
    @Transactional
    public void deleteEntity(String nombre) {
        try {
            // Fetch the localidad.
            Localidad localidad = findEntity(nombre);
            // Delete the localidad.
            eliminarLocalidadPorId(localidad.getId());
        } catch (IllegalArgumentException | EntityNotFoundException | ServiceException e) {
            throw e; // Rethrow to let controller handle
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar la localidad de nombre: " + nombre, e);
        }
    }

    // Eliminar una localidad por su id del sistema.
    @Transactional
    public void eliminarLocalidadPorId(Long id) {
        try {
            // Input validation.
            if (id == null) {
                throw new IllegalArgumentException("El id no puede ser nulo");
            }
            // Verify existence (optional, for consistent not-found handling).
            if (!localidadRepository.existsById(id)) {
                throw new EntityNotFoundException("Localidad no encontrada para el id: " + id);
            }
            // Delete the localidad.
            localidadRepository.deleteById(id);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            throw new ServiceException("Error al eliminar localidad por id: " + id, e);
        } catch (Exception e) {
            throw new ServiceException("Error inesperado al eliminar localidad por id: " + id, e);
        }
    }


}