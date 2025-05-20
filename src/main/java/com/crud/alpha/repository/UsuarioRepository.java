package com.crud.alpha.repository;

import com.crud.alpha.clase.Usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByClerkId(String clerkId); //Metodo generado usando la funcion de JPA findBy

    boolean existsByClerkId(String clerkId); //Metodo generado usando la funcion de JPA existsBy
}

