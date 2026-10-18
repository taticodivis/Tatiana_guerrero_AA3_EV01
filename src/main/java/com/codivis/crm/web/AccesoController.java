package com.codivis.crm.web;

import com.codivis.crm.auth.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Presenta el acceso y registra cuentas; el login lo procesa Spring Security. */
@Controller
public class AccesoController {
    private final UsuarioRepository usuarios;

    public AccesoController(UsuarioRepository usuarios) { this.usuarios = usuarios; }

    @GetMapping("/")
    public String inicio() { return "redirect:/login"; }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("registro", false);
        return "acceso";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registro", true);
        return "acceso";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam(defaultValue = "") String usuario,
            @RequestParam(defaultValue = "") String clave,
            @RequestParam(defaultValue = "") String confirmacion,
            Model model, HttpServletResponse response) throws IOException {
        try {
            usuarios.crear(usuario, clave, confirmacion);
            return "redirect:/login?registro=1";
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            model.addAttribute("registro", true);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", ex.getMessage());
            return "acceso";
        }
    }
}
