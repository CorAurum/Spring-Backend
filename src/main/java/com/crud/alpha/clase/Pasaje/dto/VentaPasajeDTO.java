package com.crud.alpha.clase.Pasaje.dto;


import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Pasaje.Pasaje;
import com.crud.alpha.clase.Pasaje.VentaPasaje;
import com.crud.alpha.clase.Usuarios.Usuario;
import com.crud.alpha.clase.Usuarios.Vendedor.Vendedor;
import com.crud.alpha.clase.Viaje.Viaje;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class VentaPasajeDTO {
    private Long paymentId;
    private String paymentStatus;
    private LocalDateTime fechaVenta;
    private String sellerId;        // ID del vendedor
    private String buyerId;         // ID del cliente
    private List<Long> pasajesIds; // Lista de IDs de pasajes asociados
    private Long viajeId;         // ID del viaje
}
