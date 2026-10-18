package com.codivis.crm;

import com.codivis.crm.auth.UsuarioRepository;
import com.codivis.crm.repository.ClienteRepository;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Punto de entrada: Spring Boot configura MVC, vistas y Tomcat integrado. */
@SpringBootApplication
public class CodivisApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodivisApplication.class, args);
    }

    // Una instancia por repositorio evita caches diferentes dentro de la aplicacion.
    @Bean
    UsuarioRepository usuarios(@Value("${crm.data.dir}") String carpeta) throws IOException {
        return new UsuarioRepository(Path.of(carpeta, "usuarios.properties"));
    }

    @Bean
    ClienteRepository clientes(@Value("${crm.data.dir}") String carpeta) throws IOException {
        return new ClienteRepository(Path.of(carpeta, "clientes.tsv"));
    }
}
