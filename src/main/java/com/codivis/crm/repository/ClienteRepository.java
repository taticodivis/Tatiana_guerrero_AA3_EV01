package com.codivis.crm.repository;

import com.codivis.crm.model.Cliente;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

/** Registro y consulta de clientes, con almacenamiento local para la demostración. */
public final class ClienteRepository {
    private final Path archivo;
    private List<Cliente> clientes;

    public ClienteRepository(Path archivo) throws IOException {
        this.archivo = archivo.toAbsolutePath();
        clientes = cargar();
    }

    public synchronized List<Cliente> listar() {
        return clientes.stream().sorted(Comparator.comparing(Cliente::nombre, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    public synchronized Cliente registrar(Cliente datos) throws IOException {
        if (datos.id() != 0) throw new IllegalArgumentException("Un cliente nuevo no debe tener identificador.");
        if (clientes.stream().anyMatch(c -> c.correo().equalsIgnoreCase(datos.correo()))) {
            throw new IllegalArgumentException("Ya hay un cliente con ese correo electrónico.");
        }
        long id = clientes.stream().mapToLong(Cliente::id).max().orElse(0) + 1;
        Cliente cliente = new Cliente(id, datos.nombre(), datos.correo(), datos.telefono());
        List<Cliente> nuevos = new ArrayList<>(clientes);
        nuevos.add(cliente);
        persistir(nuevos);
        clientes = nuevos;
        return cliente;
    }

    private List<Cliente> cargar() throws IOException {
        if (!Files.exists(archivo)) return new ArrayList<>();
        List<Cliente> resultado = new ArrayList<>();
        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                if (linea.isBlank()) continue;
                String[] campos = linea.split("\t", -1);
                // Permite leer también los contactos de la primera versión del módulo.
                if (campos.length != 4 && campos.length != 7) throw new IllegalArgumentException("Registro incompleto");
                int correo = campos.length == 4 ? 2 : 3;
                int telefono = campos.length == 4 ? 3 : 4;
                Cliente cliente = new Cliente(Long.parseLong(campos[0]), decodificar(campos[1]),
                        decodificar(campos[correo]), decodificar(campos[telefono]));
                if (cliente.id() <= 0 || resultado.stream().anyMatch(c -> c.id() == cliente.id() || c.correo().equals(cliente.correo()))) {
                    throw new IllegalArgumentException("Registro duplicado o identificador inválido");
                }
                resultado.add(cliente);
            }
        } catch (IllegalArgumentException ex) {
            throw new IOException("No se pudo leer el archivo de clientes. Revisa su integridad sin borrar los datos.", ex);
        }
        return resultado;
    }

    private void persistir(List<Cliente> nuevos) throws IOException {
        Files.createDirectories(archivo.getParent());
        Path temporal = Files.createTempFile(archivo.getParent(), "clientes-", ".tmp");
        try {
            List<String> lineas = nuevos.stream().map(c -> c.id() + "\t" + codificar(c.nombre()) + "\t"
                    + codificar(c.correo()) + "\t" + codificar(c.telefono())).toList();
            Files.write(temporal, lineas, StandardCharsets.UTF_8);
            try {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    private static String codificar(String texto) {
        return Base64.getEncoder().encodeToString(texto.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodificar(String texto) {
        return new String(Base64.getDecoder().decode(texto), StandardCharsets.UTF_8);
    }
}
