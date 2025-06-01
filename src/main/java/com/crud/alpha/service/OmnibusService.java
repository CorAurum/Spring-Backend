package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Omnibus.dto.NewOmnibusDTO;
import com.crud.alpha.clase.Omnibus.dto.OmnibusUpdateDTO;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.repository.AsientoRepository;
import com.crud.alpha.repository.LocalidadRepository;
import com.crud.alpha.repository.OmnibusRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
public class OmnibusService {
    private static final Logger logger = LoggerFactory.getLogger(OmnibusService.class);

    @Autowired
    private OmnibusRepository omnibusRepository;
    @Autowired
    private VendedorService vendedorService;
    @Autowired
    private LocalidadRepository localidadRepository;
    @Autowired
    private UltimaLocalidadService ultimaLocalidadService;
    @Autowired
    private AsientoRepository asientoRepository;

    // Obtener todos los ómnibus.
    public List<Omnibus> listEntities() {
        return omnibusRepository.findAll();
    }

    // Obtener ómnibus por numero de coche.
    public Omnibus findEntity(int nroCoche) {
        try {
            Optional<Omnibus> entityOptional = omnibusRepository.findByNroCoche(nroCoche);
            if (entityOptional.isEmpty()) {
                throw new EntityNotFoundException("Ómnibus no encontrado para el nombre: " + nroCoche);
            }
            return entityOptional.get();
        } catch (DataAccessException e) {
            throw new ServiceException("Error al consultar el ómnibus de nombre: " + nroCoche, e);
        }
    }

    // Crear un nuevo ómnibus.
    @Transactional
    public void createEntity(NewOmnibusDTO entityDTO) {
        try {
            // Buscamos el vendedor.
            Vendedor vendedor = vendedorService.findEntity(entityDTO.getRegisteredBy());
            if (entityDTO.getRegisteredBy() != null && vendedor == null) {
                logger.error("[OMNIBUS createEntity] No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
                throw new IllegalArgumentException("No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
            }

            // Check if an omnibus with the same nroCoche exists.
            Optional<Omnibus> entityOptional = omnibusRepository.findByNroCoche(entityDTO.getNroCoche());
            if (!entityOptional.isEmpty()) {
                logger.error("[OMNIBUS createEntity] Ya existe un ómnibus para el numero de coche: " + entityDTO.getNroCoche());
                throw new IllegalArgumentException("omnibus-duplicado " + entityDTO.getNroCoche());
            }



            // Create the new bus.
            Omnibus entity = new Omnibus(entityDTO);
            entity.setRegisteredBy(vendedor);

           //  Initialize the ultimasLocalidades list with the last location that comes in the DTO.
            List<UltimaLocalidad> ultimasLocalidades = new ArrayList<>();
            Optional<Localidad> localidad = localidadRepository.findById(entityDTO.getUltimaLocalidadId());
            if (localidad.isEmpty()) {
                logger.error("No se encontró la localidad con id: " + entityDTO.getUltimaLocalidadId());
                throw new IllegalArgumentException("localidad-not-found " + entityDTO.getUltimaLocalidadId());
            }

            UltimaLocalidad ultimaLocalidad = new UltimaLocalidad();
            ultimaLocalidad.setLocalidad(localidad.get());
            ultimaLocalidadService.guardar(ultimaLocalidad);
            ultimasLocalidades.add(ultimaLocalidad);
            entity.setUltimasLocalidades(ultimasLocalidades);
            ultimaLocalidad.setOmnibus(entity);
            ultimaLocalidad.setFecha(LocalDate.now());
            ultimaLocalidad.setHora(LocalTime.now());

            // Save entity.
            omnibusRepository.save(entity);
            logger.info("[OMNIBUS createEntity] Ómnibus guardado");

            // Crear los asientos para el ómnibus.
            for (int i = 0; i < entityDTO.getAsientos(); i++) {
                Asiento asiento = new Asiento();
                asiento.setNumero(i + 1);
                asiento.setOmnibus(entity);
                asientoRepository.save(asiento);
            }

            logger.info("[OMNIBUS createEntity] " + entityDTO.getAsientos() + " Asientos creados");

        } catch (IllegalArgumentException e) {
            logger.warn("[OMNIBUS createEntity] IllegalArgumentException: ");
            throw e;
        } catch (DataAccessException e) {
            logger.warn("[OMNIBUS createEntity] DataAccessException: Error al guardar el ómnibus ");
            throw new ServiceException("Error al guardar el ómnibus " + e.getMessage(), e);
        } catch (Exception e) {
            logger.warn("[OMNIBUS createEntity] Exception: Error inesperado al guardar el ómnibus ");
            throw new ServiceException("Error inesperado al guardar el ómnibus", e);
        }
    }

    // Editar un ómnibus.
    @Transactional
    public void updateEntity(int nroCoche, OmnibusUpdateDTO entityUpdateDTO) {
        try {
            // Fetch the existing Ómnibus by nroCoche.
            Omnibus entity = findEntity(nroCoche); // Throws an Exception if not found
            // Check if an omnibus with the new nroCoche exists. It is not necessary to do so if nroCoche was not modified.
            if (entity.getNroCoche() != entityUpdateDTO.getNroCoche()) {
                Optional<Omnibus> entityOptional = omnibusRepository.findByNroCoche(entityUpdateDTO.getNroCoche());
                if (entityOptional.isPresent()) {
                    logger.error("[OMNIBUS updateEntity] Ya existe un ómnibus para el numero de coche: " + entityUpdateDTO.getNroCoche());
                    throw new IllegalArgumentException("omnibus-duplicado " + entityUpdateDTO.getNroCoche());
                }
            }

            // Update the provided fields.
            // Numero de coche.
            entity.setNroCoche(entityUpdateDTO.getNroCoche());
            // Accesibilidad.
            entity.setAccesibilidad(entityUpdateDTO.isAccesibilidad());
            // Descripción.
            if (entityUpdateDTO.getDescripcion() != null) {
                entity.setDescripcion(entityUpdateDTO.getDescripcion());
            }
            // Estado.
            if (entityUpdateDTO.getEstado() != null) {
                entity.setEstado(entityUpdateDTO.getEstado());
            }
            // Asientos.
            // Faltaría handlear que si el omnibus tiene un viaje y ya vendió pasajes, seguramente tire un error de foreign key entre pasaje y asiento.
            if (entityUpdateDTO.getAsientos() != entity.getAsientos().size()) {
                // Delete all associated seats.
                asientoRepository.deleteByOmnibus_nroCoche(nroCoche);
                logger.info("[OMNIBUS updateEntity] " + entity.getAsientos().size() + " Asientos eliminados");

                // Crear los asientos para el ómnibus.
                for (int i = 0; i < entityUpdateDTO.getAsientos(); i++) {
                    Asiento asiento = new Asiento();
                    asiento.setNumero(i + 1);
                    asiento.setOmnibus(entity);
                    asientoRepository.save(asiento);
                }
                logger.info("[OMNIBUS updateEntity] " + entityUpdateDTO.getAsientos() + " Asientos creados");
            }

            // Save the updated entity.
            omnibusRepository.save(entity);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            logger.warn("[OMNIBUS updateEntity] EntityNotFoundException | IllegalArgumentException ");
            throw e;
        } catch (DataAccessException e) {
            logger.warn("[OMNIBUS updateEntity] DataAcessException: Error al actualizar la localidad ");
            throw new ServiceException("Error al actualizar la localidad", e);
        } catch (Exception e) {
            logger.warn("[OMNIBUS updateEntity] Exception: Error inesperado al actualizar la localidad ");
            throw new ServiceException("Error inesperado al actualizar la localidad", e);
        }
    }

    // Eliminar ómnibus por nroCoche.
    // También elimina los asientos asociados.
    @Transactional
    public void eliminarOmnibus(int nroCoche) {
        try {
            Optional<Omnibus> omnibus = omnibusRepository.findByNroCoche(nroCoche);
            if (!omnibus.isPresent()) {
                throw new EntityNotFoundException("Omnibus con nroCoche " + nroCoche + " no encontrado.");
            }

            // Delete all associated seats.
            asientoRepository.deleteByOmnibus_nroCoche(nroCoche);
            // Delete the bus.
            omnibusRepository.deleteOmnibusByNroCoche(nroCoche);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            logger.warn("EntityNotFoundException | IllegalArgumentException ");
            throw e;
        } catch (DataAccessException e) {
            logger.warn("DataAccessException: Error al eliminar el ómnibus ");
            throw new ServiceException("Error al eliminar el ómnibus", e);
        } catch (Exception e) {
            logger.warn("Exception: Error inesperado eliminar el ómnibus ");
            throw new ServiceException("Error inesperado al eliminar el ómnibus", e);
        }
    }

}