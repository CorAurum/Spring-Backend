package com.crud.alpha.repository;

import com.crud.alpha.clase.Omnibus.Omnibus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OmnibusRepository extends JpaRepository<Omnibus, Long> {

    Optional<Omnibus> findByNroCoche(int nroCoche); // Metodo generado usando la funcion de JPA findBy

    void deleteOmnibusByNroCoche(int nroCoche);

    boolean existsByNroCoche(int nroCoche);

    // Despues habria que hacer una query parecida para encontrar omnibus disponibles para una reasignacion.
    // Tienen que tener una cantidad de asientos igual o mayor a la cantidad de asientos del omnibus anterior.

    // Un ómnibus se considera disponible si se encuentra en la localidad de origen a la fecha de partida
    // del viaje que se está creando. Y no se encuentra en mantenimiento u otros estados que presenten impedimento.
    // A notar:
    // - A un omnibus se le cambia la ultima localidad cuando se le asigna un viaje.
    // (se le pone la ultima localidad como la localidad del destino inmediatamente con la fecha de llegada)
    // - A un omnibus se le cambia la ultima localidad cuando se le reasigna un viaje,
    // sea que se le desasigna (se borra la última correspondiente al viaje desasignado) o se le asigna uno (el caso de arriba).
    // Y asi se va construyendo su historial de ultimas localidades con las fechas futuras de arribo correspondientes.
    // *** Por lo que es factible ponerle una nueva ultima localidad con una fecha futura, indicando que recien a partir de esa fecha estará libre para un viaje.
    @Query(value =
            "SELECT o.* " +
                    " FROM omnibus o INNER JOIN ultima_localidad ul ON o.nro_coche = ul.nro_coche " +
                    "WHERE ul.fecha = ( " + // la mas ultima de las ultimas localidades.
                    "    SELECT MAX(ul2.fecha) " +
                    "    FROM ultima_localidad ul2 " +
                    "    WHERE ul2.nro_coche = ul.nro_coche) " +
                    "AND ul.localidad_id = :localidadOrigenId " + // que la ultima localidad sea igual a la de origen del nuevo viaje.
                    "AND ul.fecha <= :fechaPartida " +
                    "AND o.estado NOT IN ('MANTENIMIENTO', 'FUERA_DE_SERVICIO')", // puede estar EN_VIAJE y DISPONIBLE
            nativeQuery = true)
    List<Omnibus> findOmnibusDisponibles(
            @Param("localidadOrigenId") Long localidadOrigenId,
            @Param("fechaPartida") String fechaPartida
    );

}