package biblioteca.ui.formularios;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.usuarios.TipoUsuario;
import biblioteca.services.ControlUsuarios;
import biblioteca.services.ControlValidaciones;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Formulario para el caso de uso CU01 – Registrar Socio.
 * Permite al usuario registrarse en el sistema con sus datos personales
 * y credenciales de acceso, validando los datos ingresados.
 */
public class FormularioRegistroSocio {
    private final ControlUsuarios controlUsuarios;
    private final ControlValidaciones controlValidaciones;
    private final Scanner scanner;

    private Socio socioTemporal;

    public FormularioRegistroSocio(ControlUsuarios controlUsuarios, ControlValidaciones controlValidaciones) {
        this.controlUsuarios = controlUsuarios;
        this.controlValidaciones = controlValidaciones;
        this.scanner = new Scanner(System.in);
    }

    /** Flujo principal del formulario */
    public void mostrarFormulario() {
        System.out.println("\n=== REGISTRO DE NUEVO SOCIO ===");

        try {
            ingresarDatos();
            if (validarDatos()) {
                confirmarRegistro();
            } else {
                mostrarError("Los datos del socio no son válidos. Registro cancelado.");
            }
        } catch (Exception e) {
            mostrarError("Error durante el registro: " + e.getMessage());
        }
    }

    /** Solicita los datos personales y credenciales del nuevo socio */
    public void ingresarDatos() {
        try {
            // Generar automáticamente ID y número de socio
            int id = BaseDatosSimulada.generarNuevoIdSocio();
            int numeroSocio = BaseDatosSimulada.generarNuevoNumeroSocio();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("DNI: ");
            String dni = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();

            System.out.print("Nombre de usuario: ");
            String usuario = scanner.nextLine();

            System.out.print("Contraseña: ");
            String contrasenia = scanner.nextLine();

            LocalDate fechaAlta = LocalDate.now();
            LocalDate vencimientoCarnet = fechaAlta.plusYears(1);
            String estado = "Activo";

            socioTemporal = new Socio(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email,
                    telefono,
                    fechaAlta,
                    TipoUsuario.SOCIO,
                    usuario,
                    contrasenia,
                    numeroSocio,
                    vencimientoCarnet,
                    estado,
                    false,
                    false
            );

            System.out.println("\nID asignado automáticamente: " + id);
            System.out.println("Número de socio asignado: " + numeroSocio);

        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Entrada inválida. Verifique los campos numéricos.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error en los datos: " + e.getMessage());
        }
    }

    /** Valida los datos del socio usando ControlValidaciones */
    public boolean validarDatos() {
        if (socioTemporal == null) {
            mostrarError("No se han ingresado datos del socio.");
            return false;
        }
        return controlValidaciones.validarDatosUsuario(socioTemporal);
    }

    /** Confirma y ejecuta el registro */
    public void confirmarRegistro() {
        if (socioTemporal == null) {
            mostrarError("No hay datos para registrar.");
            return;
        }

        System.out.print("¿Desea confirmar el registro de este socio? (S/N): ");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("S")) {
            controlUsuarios.registrarSocio(socioTemporal);
        } else {
            System.out.println("Registro cancelado por el usuario.");
        }

        socioTemporal = null;
    }

    /** Muestra un mensaje de error estandarizado */
    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }
}
