package com.crud.alpha.repository;

import com.crud.alpha.clase.Usuarios.Cliente;
import com.crud.alpha.enums.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email); //Metodo generado usando la funcion de JPA findBy

    Optional<Cliente> findByClerkId(String clerkId); //Metodo generado usando la funcion de JPA findBy

    List<Cliente> findBytipoBenef(Beneficiario tipoBenef); //Metodo generado usando la funcion de JPA findBy
}

