package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Omnibus.dto.NewOmnibusDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            logger.info("sexo sexo");
            logger.info(entityDTO.toString());

            // Buscamos el vendedor.
            Vendedor vendedor = vendedorService.findEntity(entityDTO.getRegisteredBy());
            if (entityDTO.getRegisteredBy() != null && vendedor == null) {
                logger.error("No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
                throw new IllegalArgumentException("No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
            }

            // Check if an omnibus with the same nroCoche exists.
            Optional<Omnibus> entityOptional = omnibusRepository.findByNroCoche(entityDTO.getNroCoche());
            if (!entityOptional.isEmpty()) {
                logger.error("Ya existe un ómnibus para el numero de coche: " + entityDTO.getNroCoche());
                throw new IllegalArgumentException("omnibus-duplicado " + entityDTO.getNroCoche());
            }

            // Convert DTO into an entity so that we can save it.
            Omnibus entity = new Omnibus();
            entity.setNroCoche(entityDTO.getNroCoche());
            entity.setEstado(entityDTO.getEstado());
            entity.setAccesibilidad(entityDTO.isAccesibilidad());
            entity.setDescripcion(entityDTO.getDescripcion());
            entity.setRegisteredBy(vendedor);
            // Initialize the ultimasLocalidades list with the last location that comes in the DTO.
            List<UltimaLocalidad> ultimasLocalidades = new ArrayList<>();
            Optional<Localidad> localidad = localidadRepository.findById(entityDTO.getUltimaLocalidadId());
            if (localidad.isEmpty()) {
                logger.error("No se encontró la localidad con id: " + entityDTO.getUltimaLocalidadId());
                throw new IllegalArgumentException("localidad-not-found " + entityDTO.getUltimaLocalidadId());
            }
//            UltimaLocalidad ultimaLocalidad = new UltimaLocalidad();
//            ultimaLocalidad.setLocalidad(localidad.get());
//            ultimaLocalidadService.guardar(ultimaLocalidad);
//            ultimasLocalidades.add(ultimaLocalidad);
//            entity.setUltimasLocalidades(ultimasLocalidades);

            // Save entity.
            omnibusRepository.save(entity);
            logger.info("Ómnibus guardado");
            // Crear los asientos para el ómnibus.
            for (int i = 0; i < entityDTO.getAsientos(); i++) {
                Asiento asiento = new Asiento();
                asiento.setNumero(i+1);
                asiento.setOmnibus(entity);
                asientoRepository.save(asiento);
            }
            logger.info(entityDTO.getAsientos() + " Asientos creados");

        } catch (IllegalArgumentException e) {
            logger.warn("IllegalArgumentException: ");
            throw e;
        } catch (DataAccessException e) {
            logger.warn("DataAccessException: Error al guardar el ómnibus ");
            throw new ServiceException("Error al guardar el ómnibus " + e.getMessage(), e);
        } catch (Exception e) {
            logger.warn("Exception: Error inesperado al guardar el ómnibus ");
            throw new ServiceException("Error inesperado al guardar el ómnibus", e);
        }
    }

    // eliminar bus por nroCoche
    @Transactional
    public void eliminarOmnibus(int nroCoche) {
        Optional<Omnibus> omnibus = omnibusRepository.findByNroCoche(nroCoche);
        if (omnibus.isPresent()) {
            omnibusRepository.deleteOmnibusByNroCoche(nroCoche);
        } else {
            throw new EntityNotFoundException("Omnibus con nroCoche " + nroCoche + " no encontrado.");
        }
    }

}