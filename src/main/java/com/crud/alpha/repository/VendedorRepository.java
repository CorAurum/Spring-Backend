package com.crud.alpha.repository;

import com.crud.alpha.clase.Usuarios.Cliente.Cliente;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.enums.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByEmail(String email); //Metodo generado usando la funcion de JPA findBy

    Optional<Vendedor> findByClerkId(String clerkId); //Metodo generado usando la funcion de JPA findBy

    List<Vendedor> findBytipoBenef(Beneficiario tipoBenef); //Metodo generado usando la funcion de JPA findBy

    boolean existsByEmail(String email); //Metodo generado usando la funcion de JPA existsBy

    boolean existsByClerkId(String clerkId); //Metodo generado usando la funcion de JPA existsBy
}

