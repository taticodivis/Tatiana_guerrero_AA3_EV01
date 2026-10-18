package com.codivis.crm.web;

import com.codivis.crm.model.Cliente;
import com.codivis.crm.repository.ClienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Controlador MVC: consulta y alta; las reglas de datos viven en el modelo/repositorio. */
@Controller
public class ClientesController {
    private final ClienteRepository clientes;

    public ClientesController(ClienteRepository clientes) { this.clientes = clientes; }

    @GetMapping("/clientes")
    public String listar(@RequestParam(defaultValue = "") String buscar, Model model) {
        String criterio = buscar.strip().toLowerCase(Locale.ROOT);
        model.addAttribute("buscar", buscar);
        model.addAttribute("clientes", clientes.listar().stream().filter(cliente ->
                cliente.nombre().toLowerCase(Locale.ROOT).contains(criterio)
                || cliente.correo().contains(criterio)).toList());
        return "clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String formulario() { return "formulario"; }

    @PostMapping("/clientes")
    public String registrar(@RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String correo,
            @RequestParam(defaultValue = "") String telefono,
            Model model, HttpServletResponse response) throws IOException {
        try {
            clientes.registrar(new Cliente(0, nombre, correo, telefono));
            // Redireccion para evitar duplicar el alta al recargar el navegador.
            return "redirect:/clientes?guardado=1";
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("nombre", nombre);
            model.addAttribute("correo", correo);
            model.addAttribute("telefono", telefono);
            return "formulario";
        }
    }
}
