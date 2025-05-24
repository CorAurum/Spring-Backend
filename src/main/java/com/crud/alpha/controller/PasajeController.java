package com.crud.alpha.controller;

import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.service.PasajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/pasajes")
public class PasajeController {

    @Autowired
    private PasajeService pasajeService;

    @PostMapping("/crearParaViaje/{idViaje}")
    public String crearPasajesParaViaje(@PathVariable Long idViaje) {
        pasajeService.crearPasajesParaViaje(idViaje);
        return "Pasajes creados correctamente para el viaje " + idViaje;
    }

    @GetMapping
    public List<Pasaje> listarTodos() {
        return pasajeService.listarPasajes();
    }

    @GetMapping("/porViaje/{idViaje}")
    public List<Pasaje> listarPorViaje(@PathVariable Long idViaje) {
        return pasajeService.listarPasajesPorViaje(idViaje);
    }

    @GetMapping("/{id}")
    public Pasaje obtenerPorId(@PathVariable Long id) {
        return pasajeService.buscarPasajePorId(id);
    }

    @PutMapping("/{id}")
    public Pasaje actualizar(@PathVariable Long id, @RequestBody Pasaje pasaje) {
        return pasajeService.actualizarPasaje(id, pasaje);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        pasajeService.eliminarPasaje(id);
        return "Pasaje eliminado correctamente";
    }
}
