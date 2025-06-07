package com.crud.alpha.service;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Viaje.Viaje;
import com.crud.alpha.clase.Viaje.dto.NewViajeDTO;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.repository.LocalidadRepository;
import com.crud.alpha.repository.OmnibusRepository;
import com.crud.alpha.repository.PasajeRepository;
import com.crud.alpha.repository.ViajeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {
    private static final Logger logger = LoggerFactory.getLogger(ViajeService.class);

    @Autowired
    private ViajeRepository viajeRepository;
    @Autowired
    public OmnibusRepository omnibusRepository;
    @Autowired
    public LocalidadRepository localidadRepository;
    @Autowired
    private VendedorService vendedorService;
    @Autowired
    private OmnibusService omnibusService;
    @Autowired
    private LocalidadService localidadService;
    @Autowired
    private PasajeRepository pasajeRepository;


    // Obtener todos los Viajes.
    public List<Viaje> ListarViajes() {
        return viajeRepository.findAll();
    }

    // Obtener viaje por Id
    public Optional<Viaje> buscarViajeporId(long id) {
        return viajeRepository.findById(id);
    }

    // Obtener un Viaje por su LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadDestino(long localidadDestinoId) {
        return viajeRepository.findByLocalidadDestino_Id(localidadDestinoId);
    }

    // Obtener un Viaje por su LocalidadInicial y LocalidadFinal
    public List<Viaje> ObtenerPorLocalidadOrigenyDestino(Long localidadOrigenId, Long localidadDestinoId) {
        return viajeRepository.findByLocalidadOrigen_IdAndLocalidadDestino_Id(localidadOrigenId, localidadDestinoId);
    }

    // Crear una nueva localidad.
    @Transactional
    public void createEntity(NewViajeDTO entityDTO) {
        try {
            Vendedor vendedor = vendedorService.findEntity(entityDTO.getRegisteredBy());
            if (entityDTO.getRegisteredBy() != null && vendedor == null) {
                logger.error("[VIAJE createEntity] No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
                throw new IllegalArgumentException("No existe un vendedor para el clerkId: " + entityDTO.getRegisteredBy());
            }

            Omnibus omnibus = omnibusService.findEntity(entityDTO.getNroCoche());
            if (omnibus == null) {
                logger.error("[VIAJE createEntity] No existe un omnibus para el nroCoche: " + entityDTO.getNroCoche());
                throw new IllegalArgumentException("No existe un omnibus para el nroCoche: " + entityDTO.getNroCoche());
            }

            Localidad origen = localidadService.findEntityById(entityDTO.getLocalidadOrigenId());
            if (origen == null) {
                logger.error("[VIAJE createEntity] No existe una localidad para la id: " + entityDTO.getLocalidadOrigenId());
                throw new IllegalArgumentException("No existe una localidad para la id: " + entityDTO.getLocalidadOrigenId());
            }

            Localidad destino = localidadService.findEntityById(entityDTO.getLocalidadDestinoId());
            if (origen == null) {
                logger.error("[VIAJE createEntity] No existe una localidad para la id: " + entityDTO.getLocalidadDestinoId());
                throw new IllegalArgumentException("No existe una localidad para la id: " + entityDTO.getLocalidadDestinoId());
            }

            // Convert DTO into an entity so that we can save it.
            Viaje entity = new Viaje();
            entity.setFechaPartida(entityDTO.getFechaPartida());
            entity.setFechaLlegada(entityDTO.getFechaLlegada());
            entity.setLocalidadOrigen(origen);
            entity.setLocalidadDestino(destino);
            entity.setCerrado(entityDTO.isCerrado());
            entity.setPrecio(entityDTO.getPrecio());
            entity.setOmnibusAsignado(omnibus);
            entity.setRegisteredBy(vendedor);
            // Save entity.
            viajeRepository.save(entity);
            logger.info("[VIAJE createEntity] Viaje guardado");
            // Crear los pasajes para el viaje.
            for (int i = 0; i < omnibus.getAsientos().size(); i++) {
                Pasaje pasaje = new Pasaje();
                pasaje.setAsiento(omnibus.getAsientos().get(i));
                pasaje.setViaje(entity);
                pasajeRepository.save(pasaje);
            }
            logger.info("[VIAJE createEntity] " + omnibus.getAsientos().size() + " Pasajes creados");

        } catch (IllegalArgumentException e) {
            logger.warn("[VIAJE createEntity] IllegalArgumentException: ");
            throw e;
        } catch (DataAccessException e) {
            logger.warn("[VIAJE createEntity] : Error al guardar viaje ");
            throw new ServiceException("Error al guardar viaje", e);
        } catch (Exception e) {
            logger.warn("[VIAJE createEntity] Exception: Error inesperado al guardar viaje " + e);
            throw new ServiceException("Error inesperado al guardar viaje", e);
        }
    }

}