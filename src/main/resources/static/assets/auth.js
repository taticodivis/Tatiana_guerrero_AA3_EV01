"use strict";

// Cada ojo controla solo su propio campo y nunca envia el formulario.
document.querySelectorAll("[data-password-toggle]").forEach(button => {
  const input = document.getElementById(button.dataset.passwordToggle);
  const originalLabel = button.getAttribute("aria-label");
  button.addEventListener("click", () => {
    const mostrar = input.type === "password";
    input.type = mostrar ? "text" : "password";
    button.setAttribute("aria-pressed", String(mostrar));
    button.setAttribute("aria-label", mostrar ? originalLabel.replace("Mostrar", "Ocultar") : originalLabel);
  });
});

const clave = document.getElementById("clave");
const confirmacion = document.getElementById("confirmacion");
const mensaje = document.getElementById("coincidencia-claves");

if (confirmacion) {
  const validarCoincidencia = () => {
    const escrito = confirmacion.value.length > 0;
    const coinciden = clave.value === confirmacion.value;
    confirmacion.setCustomValidity(escrito && !coinciden ? "Las contraseñas no coinciden." : "");
    mensaje.hidden = !escrito;
    mensaje.textContent = !escrito ? "" : coinciden ? "Las contraseñas coinciden." : "Las contraseñas no coinciden.";
    mensaje.classList.toggle("match-ok", escrito && coinciden);
    mensaje.classList.toggle("match-error", escrito && !coinciden);
    if (escrito) confirmacion.setAttribute("aria-invalid", String(!coinciden));
    else confirmacion.removeAttribute("aria-invalid");
    return coinciden;
  };
  clave.addEventListener("input", validarCoincidencia);
  confirmacion.addEventListener("input", validarCoincidencia);
  confirmacion.form.addEventListener("submit", event => {
    if (!validarCoincidencia()) {
      event.preventDefault();
      confirmacion.reportValidity();
    }
  });
}
