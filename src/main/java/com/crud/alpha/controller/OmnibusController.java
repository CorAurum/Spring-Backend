package com.crud.alpha.controller;

import com.crud.alpha.clase.Localidad.UltimaLocalidad;
import com.crud.alpha.clase.Localidad.dto.LocalidadDTO;
import com.crud.alpha.clase.Localidad.dto.LocalidadUpdateDTO;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.Omnibus.dto.NewOmnibusDTO;
import com.crud.alpha.clase.Omnibus.dto.OmnibusDTO;
import com.crud.alpha.clase.Omnibus.dto.OmnibusUpdateDTO;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.enums.EstadoOmnibus;
import com.crud.alpha.service.OmnibusService;
import com.crud.alpha.service.UltimaLocalidadService;
import com.crud.alpha.service.VendedorService;
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
@RequestMapping("/api/public/flota")
public class OmnibusController {

    @Autowired
    private OmnibusService omnibusService;

    private UltimaLocalidadService ultimaLocalidadService;

    @Autowired
    private VendedorService vendedorService;

    private OmnibusDTO convertToDTO(Omnibus omnibus) {
        OmnibusDTO dto = new OmnibusDTO();
        dto.setNroCoche(omnibus.getNroCoche());
        dto.setDescripcion(omnibus.getDescripcion());
        dto.setEstado(omnibus.getEstado());
        dto.setAccesibilidad(omnibus.isAccesibilidad());

        if (omnibus.getAsientos() != null && !omnibus.getAsientos().isEmpty()) {
            dto.setAsientos(omnibus.getAsientos().size());
        } else {
            dto.setAsientos(0);
        }
        // Experimentales
        if (omnibus.getUltimasLocalidades() != null && !omnibus.getUltimasLocalidades().isEmpty()) {
            UltimaLocalidad ultima = omnibus.getUltimasLocalidades().getFirst();

            dto.setUltimaLocalidadid(ultima.getLocalidad().getId());

            if (ultima.getLocalidad() != null) {
                dto.setUltimaLocalidadNombre(ultima.getLocalidad().getNombre());
            }
        }
        // ----------------------------------------------------------------------------
        dto.setRegisteredByFullName(omnibus.getRegisteredBy().getNombre() + " " + omnibus.getRegisteredBy().getApellido());
        dto.setCreatedAt(omnibus.getCreatedAt());
        dto.setUpdatedAt(omnibus.getUpdatedAt());

        return dto;
    }


    // Listar todos los ómnibus
    @GetMapping
    public ResponseEntity<List<OmnibusDTO>> listarFlota() {
        try {
            List<Omnibus> flota = omnibusService.listEntities();
            List<OmnibusDTO> omnibusDTOs = flota.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(omnibusDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Buscar un ómnibus por número de coche
    @GetMapping("/{nroCoche}")
    public ResponseEntity<OmnibusDTO> findEntity(@PathVariable int nroCoche) {
        try {
            Omnibus entity = omnibusService.findEntity(nroCoche);
            OmnibusDTO dto = convertToDTO(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Crear Omnibus
    @PostMapping
    public ResponseEntity<String> createEntity(@RequestBody NewOmnibusDTO entityDTO) {
        try {
           omnibusService.createEntity(entityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ómnibus creado con éxito");
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

// Modificar un omnibus por nroCoche
@PatchMapping("/{nroCoche}")
public ResponseEntity<String> updateEntity(
        @PathVariable int nroCoche,
        @RequestBody OmnibusUpdateDTO updateDTO) {
    try {
        omnibusService.updateEntity(nroCoche, updateDTO);
        return ResponseEntity.ok("Ómnibus actualizado con exito");
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



    // Eliminar un omnibus por nroCoche.
    @DeleteMapping("/{nroCoche}")
    public ResponseEntity<String> borrarOmnibus(@PathVariable int nroCoche) {
        try {
            omnibusService.eliminarOmnibus(nroCoche);
            return ResponseEntity.ok("Omnibus eliminado con éxito");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Omnibus: " + e.getMessage());
        }
    }


    // Altas masivas de omnibus

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Archivo vacío.");
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;
            List<String> errores = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Salteamos encabezado

                try {
                    NewOmnibusDTO dto = new NewOmnibusDTO();

                    // Leer nroCoche (col 0)
                    Cell cell0 = row.getCell(0);
                    int nroCoche = cell0.getCellType() == CellType.NUMERIC
                            ? (int) cell0.getNumericCellValue()
                            : Integer.parseInt(cell0.getStringCellValue().trim());
                    dto.setNroCoche(nroCoche);

                    // Leer descripcion (col 1)
                    dto.setDescripcion(row.getCell(1).getStringCellValue());

                    // Leer estado (col 2)
                    dto.setEstado(EstadoOmnibus.valueOf(row.getCell(2).getStringCellValue().trim().toUpperCase()));

                    // Leer accesibilidad (col 3)
                    dto.setAccesibilidad(
                            row.getCell(3).getCellType() == CellType.BOOLEAN
                                    ? row.getCell(3).getBooleanCellValue()
                                    : Boolean.parseBoolean(row.getCell(3).getStringCellValue().trim())
                    );

                    // Leer asientos (col 4)  <-- Aquí añadimos el campo asientos
                    Cell cell4 = row.getCell(4);
                    int asientos = cell4.getCellType() == CellType.NUMERIC
                            ? (int) cell4.getNumericCellValue()
                            : Integer.parseInt(cell4.getStringCellValue().trim());
                    dto.setAsientos(asientos);

                    // Leer ultimaLocalidadId (col 5)
                    Cell cell5 = row.getCell(5);
                    long locId = cell5.getCellType() == CellType.NUMERIC
                            ? (long) cell5.getNumericCellValue()
                            : Long.parseLong(cell5.getStringCellValue().trim());
                    dto.setUltimaLocalidadId(locId);

                    // Leer registeredBy (col 6)
                    Cell cell6 = row.getCell(6);
                    String registeredBy = cell6.getCellType() == CellType.STRING
                            ? cell6.getStringCellValue().trim()
                            : String.valueOf((long) cell6.getNumericCellValue());
                    dto.setRegisteredBy(registeredBy);

                    // Llamamos a createEntity que ya maneja la creación completa
                    omnibusService.createEntity(dto);
                    rowCount++;

                } catch (Exception e) {
                    errores.add("Fila " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }

            if (errores.isEmpty()) {
                return ResponseEntity.ok("Se cargaron " + rowCount++ + " ómnibus correctamente.");
            } else {
                String errorMsg = "Se cargaron " + rowCount++ + " ómnibus correctamente.\n"
                        + "Errores:\n" + String.join("\n", errores);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(errorMsg);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el archivo: " + e.getMessage());
        }
    }

}


