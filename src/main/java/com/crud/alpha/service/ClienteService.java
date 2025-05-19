package com.crud.alpha.service;

import com.crud.alpha.clase.Usuarios.Cliente;
import com.crud.alpha.clase.Usuarios.dto.UsuarioUpdateDTO;
import com.crud.alpha.enums.Beneficiario;
import com.crud.alpha.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> obtenerPorClerkID(String clerkId) {
        return clienteRepository.findByClerkId(clerkId);
    }

    public Optional<Cliente> obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(String clerkId, UsuarioUpdateDTO clienteActualizado) {
        Optional<Cliente> clienteOpt = clienteRepository.findByClerkId(clerkId);

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            if (clienteActualizado.getNombre() != null) {
                cliente.setNombre(clienteActualizado.getNombre());
            }

            if (clienteActualizado.getApellido() != null) {
                cliente.setApellido(clienteActualizado.getApellido());
            }

            if (clienteActualizado.getFechaNacimiento() != null) {
                cliente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
            }

            return clienteRepository.save(cliente);
        } else {
            return null;
        }
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> obtenerPorTipo(Beneficiario tipoBenef) {
        return clienteRepository.findBytipoBenef(tipoBenef);
    }

}
