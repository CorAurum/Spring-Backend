package com.crud.alpha.controller;

import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Pasaje.dto.VentaPasajeDTO;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.clase.exceptions.ServiceException;
import com.crud.alpha.service.VentaPasajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/public/venta")
public class VentaPasajeController {

    @Autowired
    private VentaPasajeService ventaPasajeService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody VentaPasajeDTO request) {
        System.out.println("💬 VentaPasajeDTO recibido: " + request);


        if (request == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede ser nulo.");
        }
            // fix?
        try {
            // 1. Crear la venta con estado inicial
            VentaPasaje venta = ventaPasajeService.crearVentaPasaje(request);
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo crear la venta.");
            }

            BigDecimal precio = null;
            if (venta.getPrecioViaje() != null) {
                precio = BigDecimal.valueOf(venta.getPrecioViaje().getPrecio());
            }

            if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Precio inválido para la venta.");
            }

            // 2. Crear el ítem de la preferencia
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Compra de pasajes - ID Venta " + venta.getId())
                    .quantity(1)
                    .unitPrice(precio)
                    .currencyId("UYU")
                    .build();

            // 3. Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://httpbin.org/status/200")
                    .failure("https://httpbin.org/status/400")
                    .pending("https://httpbin.org/status/202")
                    .build();

            // 4. Crear la preferencia de pago
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .notificationUrl("https://tudominio.com/api/pagos/webhook")
                    .build();

            // 5. Llamar al cliente MercadoPago para crear la preferencia
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            if (preference == null || preference.getInitPoint() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo generar la preferencia de pago.");
            }

            //  6. Guardar el ID del pago (opcional, pero recomendable)
            try {
                venta.setPaymentId(venta.getId());
               ventaPasajeService.actualizar(venta);
            } catch (Exception ex) {
                System.err.println("No se pudo guardar el paymentId: " + ex.getMessage());
                // No detiene el proceso, solo informa el error
            }

            // 7. Devolver la URL al frontend para que el usuario pague
            return ResponseEntity.ok(preference.getInitPoint());

        } catch (MPApiException apiException) {
            System.err.println("MPApiException occurred:");
            System.err.println("Status Code: " + apiException.getStatusCode());
            System.err.println("Cause: " + apiException.getCause());
            System.err.println("Message: " + apiException.getMessage());
            if (apiException.getApiResponse() != null) {
                System.err.println("Response Content: " + apiException.getApiResponse().getContent());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de API MercadoPago: " + apiException.getMessage());

        } catch (MPException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando el pago: " + e.getMessage());

        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }


    // POST: Crear nueva venta de pasaje
    @PostMapping("vp")
    public ResponseEntity<?> crearVentaPasaje(@RequestBody VentaPasajeDTO dto) {
        try {
            VentaPasaje venta = ventaPasajeService.crearVentaPasaje(dto);return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidad no encontrada: " + e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

    // GET: Listar todas las ventas de pasajes
    @GetMapping
    public ResponseEntity<?> listarVentas() {
        try {
            List<VentaPasaje> ventas = ventaPasajeService.listarVentas();
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener ventas: " + e.getMessage());
        }
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            VentaPasaje venta = ventaPasajeService.obtenerVentaPorId(id);
            return ResponseEntity.ok(venta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venta no encontrada: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}

