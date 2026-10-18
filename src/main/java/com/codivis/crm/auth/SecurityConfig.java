package com.codivis.crm.auth;

import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

/** Acceso con sesion y proteccion CSRF administrados por Spring Security. */
@Configuration
public class SecurityConfig {
    @Bean
    AuthenticationProvider autenticacion(UsuarioRepository usuarios) {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication solicitud) {
                String nombre = solicitud.getName().strip().toLowerCase(Locale.ROOT);
                String clave = solicitud.getCredentials() == null ? null : solicitud.getCredentials().toString();
                if (!usuarios.autenticar(nombre, clave)) {
                    throw new BadCredentialsException("Usuario o contraseña incorrectos.");
                }
                return UsernamePasswordAuthenticationToken.authenticated(nombre, null,
                        List.of(new SimpleGrantedAuthority("ROLE_USUARIO")));
            }

            @Override
            public boolean supports(Class<?> tipo) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(tipo);
            }
        };
    }

    @Bean
    SecurityFilterChain seguridad(HttpSecurity http, AuthenticationProvider autenticacion) throws Exception {
        http.authenticationProvider(autenticacion)
            .authorizeHttpRequests(reglas -> reglas
                .requestMatchers("/", "/login", "/registro", "/assets/**", "/error").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                .usernameParameter("usuario").passwordParameter("clave")
                .defaultSuccessUrl("/clientes", true).failureUrl("/login?error=1").permitAll())
            .logout(salida -> salida.logoutUrl("/salir").logoutSuccessUrl("/login?salida=1")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        // CSRF permanece activo: Thymeleaf agrega el token a los formularios POST.
        return http.build();
    }
}
