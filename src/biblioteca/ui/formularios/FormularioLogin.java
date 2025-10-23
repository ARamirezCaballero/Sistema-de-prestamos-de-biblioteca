package biblioteca.ui.formularios;

import biblioteca.services.ControlUsuarios;

import java.util.Scanner;

/**
 * Formulario de login para usuarios del sistema (principalmente bibliotecarios).
 * Permite ingresar credenciales y validar acceso mediante ControlUsuarios.
 */
public class FormularioLogin {
    private final ControlUsuarios controlUsuarios;
    private final Scanner scanner;

    private String usuarioTemporal;
    private String contraseniaTemporal;

    public FormularioLogin(ControlUsuarios controlUsuarios) {
        this.controlUsuarios = controlUsuarios;
        this.scanner = new Scanner(System.in);
    }

    /** Muestra el formulario principal de login */
    public void mostrarFormulario() {
        System.out.println("\n=== LOGIN DE USUARIO ===");

        try {
            ingresarCredenciales();
            confirmarLogin();
        } catch (Exception e) {
            mostrarError("Error durante el login: " + e.getMessage());
        }
    }

    /** Solicita usuario y contraseña */
    public void ingresarCredenciales() {
        System.out.print("Nombre de usuario: ");
        usuarioTemporal = scanner.nextLine();

        System.out.print("Contraseña: ");
        contraseniaTemporal = scanner.nextLine();
    }

    /** Confirma las credenciales mediante ControlUsuarios */
    public void confirmarLogin() {
        if (usuarioTemporal == null || contraseniaTemporal == null) {
            mostrarError("Usuario o contraseña no ingresados.");
            return;
        }

        boolean accesoPermitido = controlUsuarios.validarCredenciales(usuarioTemporal, contraseniaTemporal);

        if (accesoPermitido) {
            System.out.println("Login exitoso. Bienvenido al sistema.");
        } else {
            mostrarError("Credenciales incorrectas. Intente nuevamente.");
        }

        // Limpiar variables temporales
        usuarioTemporal = null;
        contraseniaTemporal = null;
    }

    /** Muestra mensaje de error estandarizado */
    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }
}
