# Codivis Clientes — AA3-EV01

Aprendiz: Tatiana Guerrero.

Evidencia GA7-220501096-AA3-EV01: Codificación de módulos del software stand-alone, web y móvil. Se eligió desarrollar un módulo web del proyecto formativo Codivis utilizando Spring Boot, Spring MVC, Spring Security y Thymeleaf.

## Funciones

- Registro de usuarios e inicio y cierre de sesión.
- Registro y listado de clientes, con búsqueda por nombre o correo.
- Validaciones de formularios y acceso restringido a usuarios autenticados.
- Persistencia local en archivos de la carpeta `data/`.

Las contraseñas se almacenan mediante PBKDF2-HMAC-SHA256 con sal aleatoria y 600000 iteraciones. No hay cuentas predeterminadas: utiliza Registrarse para crear la tuya. Las cuentas de otros proyectos no se comparten automáticamente.

## Requisitos e inicio

Java JDK 17 o superior y Maven. `Iniciar.ps1` utiliza Maven de la carpeta hermana `herramientas/apache-maven-3.10.0` si existe; de lo contrario, requiere `mvn.cmd` en PATH. La primera compilación necesita Internet para descargar dependencias.

En Windows, ejecuta `Iniciar.cmd`. Compila el proyecto y arranca el servidor. Para detenerlo, pulsa Ctrl+C en esa terminal.

En VS Code, abre esta carpeta como proyecto Java con soporte Maven, espera a que se carguen sus dependencias y ejecuta `src/main/java/Run.java` mediante Run Java. Detener esa ejecución cierra el servidor.

Acceso: http://localhost:8083/crm/login

Alternativa desde esta carpeta:

```powershell
mvn package
java -jar target/codivis-clientes.jar
```

Los datos se conservan en `data/`; no se incluyen en el repositorio ni en la entrega. `target/` contiene archivos generados. El puerto 8083 debe estar libre.

## Organización y estándares

- `auth/`: autenticación y almacenamiento de cuentas.
- `model/` y `repository/`: modelo y persistencia de clientes.
- `web/`: controladores MVC y datos compartidos de las vistas.
- `src/main/resources/templates/`: vistas Thymeleaf.
- `src/main/resources/static/assets/`: CSS y JavaScript.
- `src/test/java/`: prueba de integración de registro y acceso.

El código utiliza paquetes por responsabilidad, clases en PascalCase, métodos y variables en camelCase, constantes en MAYUSCULAS y comentarios para explicar responsabilidades y decisiones.

## Verificación

```powershell
mvn test
```

La prueba de integración comprueba el registro, la lectura de la cuenta persistida, el inicio de sesión, la carga de clientes y el rechazo de una contraseña incorrecta. Usa una carpeta temporal independiente de las cuentas reales.

Además, se verificó manualmente el funcionamiento del módulo y se ajustó la distribución del buscador tras la revisión de la aprendiz.

## Entrega y pendientes

La guía 7, apartado 3.1.3, pide para AA3-EV01 los archivos del proyecto y un archivo con el enlace del repositorio, dentro de `Tatiana_Guerrero_AA3_EV01.zip` o RAR. El documento con capturas y el video corresponden a AA3-EV02.

Antes de entregar:

- Repositorio del proyecto: https://github.com/taticodivis/Tatiana_guerrero_AA3_EV01. Su enlace está en `enlace-repositorio.txt`.
- Revisar la correspondencia del módulo con los diagramas, historias de usuario y diseños previos del proyecto formativo. Estos artefactos no están incorporados aquí y su correspondencia no se da por verificada.
- Empaquetar el proyecto y el enlace, excluyendo `.git/`, `target/`, `data/` y las herramientas externas.

El README facilita la ejecución y revisión; la guía no lo enumera como archivo obligatorio independiente.
