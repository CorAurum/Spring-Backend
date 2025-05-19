package com.crud.alpha.controller;
import com.crud.alpha.clase.Usuarios.Cliente;
import com.crud.alpha.clase.Usuarios.dto.*;
import com.crud.alpha.clase.Usuarios.exceptions.ServiceException;
import com.crud.alpha.clase.Usuarios.exceptions.UsuarioNotFoundException;
import com.crud.alpha.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    // ** PRIVATE
    @Autowired
    private ClienteService clienteService;

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setClerkId(cliente.getClerkId());
        dto.setEmail(cliente.getEmail());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setActivo(cliente.isActivo());
        dto.setTipoBenef(cliente.getTipoBenef());
        dto.setFechaNacimiento(cliente.getFechaNacimiento());
        return dto;
    }

    // ** PUBLIC
    // Devolver una lista de todos los clientes del sistema.
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listEntities() {
        try {
            List<Cliente> clientes = clienteService.listEntities();
            List<ClienteDTO> clientesDTO = clientes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(clientesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Obtener un cliente por su id de Clerk.
    @GetMapping("/{clerkId}")
    public ResponseEntity<ClienteDTO> findEntity(@PathVariable String clerkId) {
        try {
            Cliente entity = clienteService.findEntity(clerkId);
            ClienteDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Obtener un cliente por su email.
    @GetMapping("/buscar")
    public ResponseEntity<ClienteDTO> findEntityByEmail(@RequestParam String email) {
        try {
            Cliente entity = clienteService.findEntityByEmail(email);
            ClienteDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear un nuevo cliente.
    @PostMapping
    public ResponseEntity<NewClienteDTO> createEntity(@RequestBody NewClienteDTO entityDTO) {
        try {
            NewClienteDTO savedEntityDTO = clienteService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntityDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Actualizar los datos de un cliente existente.
    @PatchMapping("/{clerkId}")
    public ResponseEntity<ClienteDTO> updateEntity(
            @PathVariable String clerkId,
            @RequestBody ClienteUpdateDTO updateDTO) {
        try {
            ClienteDTO updatedEntityDTO = clienteService.updateEntity(clerkId, updateDTO);
            return ResponseEntity.ok(updatedEntityDTO);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar un cliente por la id de Clerk.
    @DeleteMapping("/{clerkId}")
    public ResponseEntity<String> deleteEntity(@PathVariable String clerkId) {
        try {
            clienteService.deleteEntity(clerkId);
            return ResponseEntity.ok("Cliente eliminado con éxito");
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar cliente");
        }
    }

}
