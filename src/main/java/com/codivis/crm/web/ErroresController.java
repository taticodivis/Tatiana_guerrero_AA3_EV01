package com.codivis.crm.web;

import java.io.IOException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/** Muestra errores de almacenamiento sin revelar rutas ni detalles internos. */
@ControllerAdvice
public class ErroresController {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ErroresController.class);

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String almacenamiento(IOException error, Model model) {
        LOG.error("No se pudo acceder al almacenamiento del modulo", error);
        model.addAttribute("mensaje", "No se pudieron guardar los datos. Inténtalo nuevamente.");
        return "error";
    }
}
