package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Administrador;
import com.crud.alpha.dto.UsuarioUpdateDTO;
import com.crud.alpha.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }

    public Optional<Administrador> obtenerPorId(Long id) {
        return administradorRepository.findById(id);
    }

    public Optional<Administrador> obtenerPorClerkID(String clerkId) {
        return administradorRepository.findByClerkId(clerkId);
    }

    public Optional<Administrador> obtenerPorEmail(String email) {
        return administradorRepository.findByEmail(email);
    }

    public Administrador guardarAdministrador(Administrador admin) {
        return administradorRepository.save(admin);
    }

    public Administrador actualizarAdministrador(String clerkId, UsuarioUpdateDTO adminActualizado) {
        Optional<Administrador> adminOpt = administradorRepository.findByClerkId(clerkId);

        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();

            if (adminActualizado.getNombre() != null) {
                admin.setNombre(adminActualizado.getNombre());
            }

            if (adminActualizado.getApellido() != null) {
                admin.setApellido(adminActualizado.getApellido());
            }

            if (adminActualizado.getFechaNacimiento() != null) {
                admin.setFechaNacimiento(adminActualizado.getFechaNacimiento());
            }

            return administradorRepository.save(admin);
        } else {
            return null;
        }
    }

    public void eliminarAdministrador(Long id) {
        administradorRepository.deleteById(id);
    }
}
