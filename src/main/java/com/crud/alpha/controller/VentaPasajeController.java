package com.crud.alpha.controller;

import com.crud.alpha.clase.MP_Temporal.Compra;
import com.crud.alpha.service.VentaPasajeService;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/payment")
public class VentaPasajeController {

    @Autowired
    private VentaPasajeService ventaPasajeService;

    @PostConstruct // Establecemos el Token
    public void initMercadoPago() {
        MercadoPagoConfig.setAccessToken("APP_USR-4660902402181456-051421-a0dc72f950d732f8c86b2ac9f49a8c2c-2442411888");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody Compra request) {
        try {
            // Crear item para el pago
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title(request.getTitulo())
                    .quantity(request.getCantidad())
                    .unitPrice(BigDecimal.valueOf(request.getPrecio()))
                    .currencyId("UYU")
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://httpbin.org/status/200")
                    .failure("https://httpbin.org/status/400")
                    .pending("https://httpbin.org/status/202")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();


            // Guardar preferencia
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            System.out.println("Preference created: " + preference.getId());
            System.out.println("Payment URL: " + preference.getInitPoint());

            return ResponseEntity.ok(preference.getInitPoint());

        } catch (MPApiException apiException) {
            // Manejo detallado de errores de la API de MercadoPago
            System.err.println("MPApiException occurred:");
            System.err.println("Status Code: " + apiException.getStatusCode());
            System.err.println("Cause: " + apiException.getCause());
            System.err.println("Message: " + apiException.getMessage());
            if (apiException.getApiResponse() != null) {
                System.err.println("Response Content: " + apiException.getApiResponse().getContent());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de API MercadoPago: " + apiException.getMessage());

        } catch (MPException e) {
            // Otros errores relacionados con MercadoPago (config, etc.)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando el pago: " + e.getMessage());
        }
    }

}