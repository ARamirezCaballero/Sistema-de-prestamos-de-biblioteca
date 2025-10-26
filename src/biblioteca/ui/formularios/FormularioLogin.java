package biblioteca.ui.formularios;

import biblioteca.entities.usuarios.Socio;
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

    /**
     * Muestra el formulario principal de login
     *
     * @return
     */
    public boolean mostrarFormulario() {
        System.out.println("\n=== LOGIN DE USUARIO ===");

        try {
            ingresarCredenciales();
            return confirmarLogin();
        } catch (Exception e) {
            mostrarError("Error durante el login: " + e.getMessage());
            return false;
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
    public boolean confirmarLogin() {
        if (usuarioTemporal == null || contraseniaTemporal == null) {
            mostrarError("Usuario o contraseña no ingresados.");
            return false;
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
        return accesoPermitido;
    }

    public Socio mostrarFormularioYRetornarSocio() {
        System.out.println("\n=== LOGIN DE USUARIO ===");
        System.out.print("Nombre de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Socio socio = controlUsuarios.loginSocio(usuario, contrasena);
        if (socio != null) {
            System.out.println("Acceso permitido para: " + socio.getNombreCompleto());
            return socio;
        } else {
            System.out.println("Error: credenciales incorrectas.");
            return null;
        }
    }

    /** Muestra mensaje de error estandarizado */
    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }
}
