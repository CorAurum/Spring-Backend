package com.crud.alpha.repository;

import com.crud.alpha.clase.Usuarios.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email); //Metodo generado usando la funcion de JPA findBy

    Optional<Administrador> findByClerkId(String clerkId); //Metodo generado usando la funcion de JPA findBy

    boolean existsByEmail(String email); //Metodo generado usando la funcion de JPA existsBy

    boolean existsByClerkId(String clerkId); //Metodo generado usando la funcion de JPA existsBy
}

