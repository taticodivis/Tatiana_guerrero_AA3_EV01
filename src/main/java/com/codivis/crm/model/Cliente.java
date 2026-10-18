package com.codivis.crm.model;

import java.util.Locale;
import java.util.Objects;

/** Datos básicos del cliente y validaciones del formulario. */
public record Cliente(long id, String nombre, String correo, String telefono) {
    public Cliente {
        if (id < 0) throw new IllegalArgumentException("El identificador no es válido.");
        nombre = Objects.requireNonNullElse(nombre, "").strip();
        correo = Objects.requireNonNullElse(correo, "").strip().toLowerCase(Locale.ROOT);
        telefono = Objects.requireNonNullElse(telefono, "").strip();
        if (nombre.length() < 2 || nombre.length() > 80) {
            throw new IllegalArgumentException("El nombre debe tener entre 2 y 80 caracteres.");
        }
        if (correo.length() > 120 || !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("Escribe un correo electrónico válido.");
        }
        if (!telefono.isEmpty() && !telefono.matches("[+()0-9 .-]{7,25}")) {
            throw new IllegalArgumentException("Escribe un teléfono válido, por ejemplo: +57 300 123 4567.");
        }
    }
}
