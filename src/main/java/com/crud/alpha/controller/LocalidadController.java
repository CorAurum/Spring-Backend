package com.crud.alpha.controller;

import com.crud.alpha.clase.Localidad.Localidad;
import com.crud.alpha.clase.Localidad.dto.LocalidadDTO;
import com.crud.alpha.clase.Localidad.dto.LocalidadUpdateDTO;
import com.crud.alpha.clase.Localidad.dto.NewLocalidadDTO;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.service.LocalidadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/localidades")
public class LocalidadController {

    @Autowired
    private LocalidadService localidadService;

    // Convertir entidad a DTO.
    private LocalidadDTO convertToDTO(Localidad localidad) {
        LocalidadDTO dto = new LocalidadDTO();
        dto.setId(localidad.getId());
        dto.setNombre(localidad.getNombre());
        dto.setRegisteredByFullName(localidad.getRegisteredBy().getNombre() + " " + localidad.getRegisteredBy().getApellido());
        dto.setDescripcion(localidad.getDescripcion());
        dto.setCreatedAt(localidad.getCreatedAt());
        dto.setUpdatedAt(localidad.getUpdatedAt());
        return dto;
    }

    // Obtener todas las localidades.
    @GetMapping
    public ResponseEntity<List<LocalidadDTO>> listarLocalidades() {
        try {
            List<Localidad> localidades = localidadService.listEntities();
            List<LocalidadDTO> localidadDTOs = localidades.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(localidadDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Obtener una localidad por su id.
    @GetMapping("/{id}")
    public ResponseEntity<LocalidadDTO> findEntity(@PathVariable Long id) {
        try {
            Localidad entity = localidadService.findEntityById(id);
            LocalidadDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Obtener una localidad por su nombre.
    @GetMapping("/buscar")
    public ResponseEntity<LocalidadDTO> findEntityById(@RequestParam String nombre) {
        try {
            Localidad entity = localidadService.findEntity(nombre);
            LocalidadDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear una nueva localidad.
    @PostMapping
    public ResponseEntity<String> createEntity(@RequestBody NewLocalidadDTO entityDTO) {
        try {
            localidadService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Localidad creada con exito");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("entidad-duplicada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Actualizar los datos de una localidad existente.
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEntity(
            @PathVariable Long id,
            @RequestBody LocalidadUpdateDTO updateDTO) {
        try {
            localidadService.updateEntity(id, updateDTO);
            return ResponseEntity.ok("Localidad actualizada con exito");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("entidad-duplicada")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Eliminar una localidad por id.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntity(@PathVariable Long id) {
        try {
            localidadService.eliminarLocalidadPorId(id);
            return ResponseEntity.ok("Localidad eliminada con éxito");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar localidad: " + e.getMessage());
        }
    }

    //Metodo para la carga de localidades masivas
    @PostMapping("/upload")
    public ResponseEntity<String> uploadLocalidadesExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío.");
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;
            List<String> errores = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Saltear encabezado

                try {
                    NewLocalidadDTO dto = new NewLocalidadDTO();

                    // Leer nombre (col 0)
                    Cell cell0 = row.getCell(0);
                    dto.setNombre(cell0.getStringCellValue().trim());

                    // Leer descripcion (col 1)
                    Cell cell1 = row.getCell(1);
                    dto.setDescripcion(cell1 != null ? cell1.getStringCellValue().trim() : "");

                    // Leer registeredBy (col 2)
                    Cell cell2 = row.getCell(2);
                    String registeredBy = null;
                    if (cell2 != null) {
                        if (cell2.getCellType() == CellType.STRING) {
                            registeredBy = cell2.getStringCellValue().trim();
                        } else if (cell2.getCellType() == CellType.NUMERIC) {
                            registeredBy = String.valueOf((long) cell2.getNumericCellValue());
                        }
                    }
                    dto.setRegisteredBy(registeredBy);

                    // Llamamos al metodo que ya tienes en el service
                    localidadService.createEntity(dto);
                    rowCount++;

                } catch (Exception e) {
                    errores.add("Fila " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }

            if (errores.isEmpty()) {
                return ResponseEntity.ok("Se cargaron " + rowCount++ + " localidades correctamente.");
            } else {
                String errorMsg = "Se cargaron " + rowCount++ + " localidades correctamente.\n"
                        + "Errores:\n" + String.join("\n", errores);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorMsg);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo: " + e.getMessage());
        }
    }

}
