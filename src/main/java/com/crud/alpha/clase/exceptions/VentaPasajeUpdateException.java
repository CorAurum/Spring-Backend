package com.crud.alpha.clase.exceptions;

public class VentaPasajeUpdateException extends RuntimeException {
    public VentaPasajeUpdateException(String message) {
        super(message);
    }

    public VentaPasajeUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
