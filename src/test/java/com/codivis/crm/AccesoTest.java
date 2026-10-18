package com.codivis.crm;

import com.codivis.crm.auth.UsuarioRepository;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccesoTest {
    static final java.nio.file.Path DATOS;
    static {
        try { DATOS = Files.createTempDirectory("codivis-acceso-"); }
        catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry r) {
        r.add("crm.data.dir", DATOS::toString);
    }
    @Autowired MockMvc mvc;

    @Test void registrarIniciarSesionYRecargarCuenta() throws Exception {
        mvc.perform(post("/registro").with(csrf()).param("usuario", "Prueba")
                .param("clave", "UnaClave12345").param("confirmacion", "UnaClave12345"))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?registro=1"));
        assertTrue(new UsuarioRepository(DATOS.resolve("usuarios.properties")).autenticar("prueba", "UnaClave12345"));
        var resultado = mvc.perform(post("/login").with(csrf()).param("usuario", "prueba")
                .param("clave", "UnaClave12345"))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/clientes")).andReturn();
        mvc.perform(get("/clientes").session((MockHttpSession) resultado.getRequest().getSession(false)))
            .andExpect(status().isOk());
        mvc.perform(post("/login").with(csrf()).param("usuario", "prueba").param("clave", "incorrecta"))
            .andExpect(redirectedUrl("/login?error=1"));
    }
}
