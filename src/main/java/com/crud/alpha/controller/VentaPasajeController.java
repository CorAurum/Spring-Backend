package com.crud.alpha.controller;

import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Pasaje.dto.VentaPasajeDTO;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.service.PasajeService;
import com.crud.alpha.service.VentaPasajeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
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
import com.mercadopago.resources.preference.Preference;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/public/venta")
public class VentaPasajeController {

    @Autowired
    private VentaPasajeService ventaPasajeService;

    @Autowired
    private PasajeService pasajeService;


//    //-------------------------------------------------------------------------------------------------------------------------------------
////-------------------------------------------------------------------------------------------------------------------------------------
//    // Metodo Post para generar la preference y la URL de pago mediante mercadoPago
//    @PostMapping("/create")
//    public ResponseEntity<?> createPayment(@RequestBody VentaPasajeDTO request) {
//        System.out.println("💬 VentaPasajeDTO recibido: " + request);
//
//
//        if (request == null) {
//            return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede ser nulo.");
//        }
//            // fix?
//        try {
//            // 1. Crear la venta con estado inicial
//            VentaPasaje venta = ventaPasajeService.crearVentaPasaje(request);
//            if (venta == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo crear la venta.");
//            }
//
//            // VENTA PASAJE AHORA TIENE SU PROPIO PRECIO FINAL, DEBE SER CALCULADO (EN BASE A LOS BENEFICIOS DEL CLIENTE) ANTES DE ESTE PASO.
//            BigDecimal precio = null;
//            if (venta.getViaje() != null) {
//                precio = BigDecimal.valueOf(venta.getViaje().getPrecio());
//            }
//
//            if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
//                return ResponseEntity.badRequest().body("Precio inválido para la venta.");
//            }
//
//            // 2. Crear el ítem de la preferencia
//            PreferenceItemRequest item = PreferenceItemRequest.builder()
//                    .title("Compra de pasajes - ID Venta " + venta.getId())
//                    .quantity(1)
//                    .unitPrice(precio)
//                    .currencyId("UYU")
//                    .build();
//
//            // 3. Configurar URLs de retorno
//            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
//                    .success("https://httpbin.org/status/200")
//                    .failure("https://httpbin.org/status/400")
//                    .pending("https://httpbin.org/status/202")
//                    .build();
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // Esto genera una variable, y luego en externalReference se envia
//            // los pasajesId y la VentaId al pago para que MP lo devuelva en el webhook
//            // y podamos confirmar la venta con estos datos.
//            String externalRef = objectMapper.writeValueAsString(Map.of(
//                    "ventaPasajeId", venta.getId(),
//                    "pasajeIds", request.getPasajesIds()
//            ));
//
//            // 4. Crear la preferencia de pago
//            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
//                    .items(Collections.singletonList(item))
//                    .backUrls(backUrls)
//                    .autoReturn("approved")
//                    .notificationUrl("https://webhook.site/53266780-77bb-48a0-adcc-21b1d7ca9eea")
//                    .externalReference(externalRef)
//                    .build();
//
//            // 5. Llamar al cliente MercadoPago para crear la preferencia
//            PreferenceClient client = new PreferenceClient();
//            Preference preference = client.create(preferenceRequest);
//
//            if (preference == null || preference.getInitPoint() == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo generar la preferencia de pago.");
//            }
//
//            //  6. Guardar el ID del pago (opcional, pero recomendable)
//            try {
//                venta.setPaymentId(venta.getId());
//               ventaPasajeService.actualizar(venta);
//            } catch (Exception ex) {
//                System.err.println("No se pudo guardar el paymentId: " + ex.getMessage());
//                // No detiene el proceso, solo informa el error
//            }
//
//            // 7. Devolver la URL al frontend para que el usuario pague
//            return ResponseEntity.ok(preference.getInitPoint());
//
//        } catch (MPApiException apiException) {
//            System.err.println("MPApiException occurred:");
//            System.err.println("Status Code: " + apiException.getStatusCode());
//            System.err.println("Cause: " + apiException.getCause());
//            System.err.println("Message: " + apiException.getMessage());
//            if (apiException.getApiResponse() != null) {
//                System.err.println("Response Content: " + apiException.getApiResponse().getContent());
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de API MercadoPago: " + apiException.getMessage());
//
//        } catch (MPException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando el pago: " + e.getMessage());
//
//        } catch (Exception e) {
//            // Captura cualquier otro error inesperado
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
//        }
//    }
    //-------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------
//    //POST Webhook que nos notifica si el pago fue exitoso (hay que limpiar el codigo, lo dejo asi para testeo)
@PostMapping("/webhook")
public ResponseEntity<?> recibirWebhook(
        @RequestBody Map<String, Object> payload,
        @RequestHeader(value = "X-Topic", required = false) String topic,
        @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
    try {
        System.out.println("📩 Webhook recibido de MercadoPago:");
        System.out.println("Topic: " + topic);
        System.out.println("Request ID: " + requestId);
        System.out.println("Payload raw: " + payload);

        // 1. Extraer el id (el id autogenerado de la VentaPasaje) (o preference_id) enviado en payload.data.id
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        Long mpPaymentId = null;
        if (data != null && data.get("id") != null) {
            mpPaymentId = Long.valueOf(data.get("id").toString());
        } else {
            return ResponseEntity.badRequest().body("No vino data.id en el payload");
        }

        // 2. Usar el SDK de MercadoPago para traer el objeto Payment (o Preference).
        // Por ejemplo, si es notificación de pago, usamos PaymentClient:
        PaymentClient paymentClient = new PaymentClient();
        Payment mpPayment = paymentClient.get(mpPaymentId);

        // 3. Leer el external_reference que asignaste al crear la preferencia
        String externalRefJson = mpPayment.getExternalReference();
        System.out.println("external_reference recibido: " + externalRefJson);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> externalData = mapper.readValue(externalRefJson, Map.class);

        Long ventaPasajeId = Long.parseLong(externalData.get("ventaPasajeId").toString());
        System.out.println("ventaPasajeId " + ventaPasajeId);
        List<Integer> pasajeIds = (List<Integer>) externalData.get("pasajeIds");
        System.out.println("pasajeIds " + pasajeIds);

        // 4. Con ese externalRef (que es el ID de tu VentaPasaje), buscas la entidad en tu BD
        Long ventaId = Long.valueOf(ventaPasajeId);
        VentaPasaje venta = ventaPasajeService.obtenerVentaPorId(ventaId);

        if (venta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe venta con ID " + ventaId);
        }

        // 5. Verificar el estado de pago (mpPayment.getStatus()), y actualizar tu entidad
        String statusMP = mpPayment.getStatus(); // ej. "approved", "pending", etc.
        venta.setPaymentStatus(statusMP);
        if(Objects.equals(statusMP, "approved")) {
            ventaPasajeService.asignarVentaAPasajes(pasajeIds, venta);
            ventaPasajeService.actualizar(venta);
        }else{
            System.out.println("Pago no aprovado");
        }
        return ResponseEntity.ok("Webhook procesado correctamente para venta ID " + ventaId);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar el webhook: " + e.getMessage());
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------

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

