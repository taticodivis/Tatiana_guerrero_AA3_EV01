package com.codivis.crm.web;

import java.security.Principal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Expone el usuario de la sesion a las vistas compartidas. */
@ControllerAdvice
public class UsuarioActual {
    @ModelAttribute("usuarioActual")
    public String nombre(Principal usuario) {
        return usuario == null ? "" : usuario.getName();
    }
}
