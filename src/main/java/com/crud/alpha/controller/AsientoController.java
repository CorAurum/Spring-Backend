package com.crud.alpha.controller;

import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.service.AsientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/asiento")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    // Listar asientos por nroCoche
    @GetMapping("/{nroCoche}")
    public ResponseEntity<List<Asiento>> listarPorNroCoche(@PathVariable int nroCoche) {
        List<Asiento> asientos = asientoService.listarPorNroCoche(nroCoche);
        return ResponseEntity.ok(asientos);
    }

    // Crear varios asientos para un omnibus (ej: POST /api/public/asiento/nrocoche/10/cantidad/40 )
    @PostMapping("/{nroCoche}/{cantidad}")
    public ResponseEntity<String> crearAsientos(@PathVariable int nroCoche, @PathVariable int cantidad) {
        try {
            asientoService.crearAsientosParaOmnibus(nroCoche, cantidad);
            return ResponseEntity.ok("Se crearon " + cantidad + " asientos para el omnibus " + nroCoche);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creando asientos: " + e.getMessage());
        }
    }
}
