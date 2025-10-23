package biblioteca.ui.formularios;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.services.ControlPrestamos;
import biblioteca.services.ControlValidaciones;
import biblioteca.services.ControlComprobantes;
import biblioteca.entities.reportes.Comprobante;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * FormularioPrestamos
 * Capa de frontera: Interfaz con el usuario para registrar un préstamo.
 * Caso de uso CU03 – Registrar Préstamo.
 */
public class FormularioPrestamos {

    private final ControlPrestamos controlPrestamos;
    private final ControlValidaciones controlValidaciones;
    private final ControlComprobantes controlComprobantes;
    private final Scanner scanner;

    public FormularioPrestamos(ControlPrestamos controlPrestamos,
                               ControlValidaciones controlValidaciones,
                               ControlComprobantes controlComprobantes) {
        this.controlPrestamos = controlPrestamos;
        this.controlValidaciones = controlValidaciones;
        this.controlComprobantes = controlComprobantes;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarFormulario() {
        System.out.println("\n=== FORMULARIO DE PRÉSTAMOS ===");
        ingresarDatosPrestamo();
    }

    private void ingresarDatosPrestamo() {
        try {
            System.out.print("Ingrese DNI del socio: ");
            String dni = scanner.nextLine();

            Socio socio = buscarSocio(dni);

            System.out.print("Ingrese código del ejemplar: ");
            String codigo = scanner.nextLine();

            Ejemplar ejemplar = buscarEjemplar(codigo);

            // Delegar registro al control y capturar excepciones
            try {
                controlPrestamos.registrarPrestamo(socio, ejemplar);

                mostrarFechaCalculada(controlPrestamos.calcularFechaVencimiento(LocalDate.now()));
                emitirComprobante(socio, ejemplar);
                mostrarConfirmacion();

            } catch (IllegalArgumentException e) {
                System.out.println("Error al registrar el préstamo: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error inesperado en el formulario de préstamo: " + e.getMessage());
        }
    }

    private void mostrarFechaCalculada(LocalDate fechaVencimiento) {
        System.out.println("Fecha de devolución calculada: " + fechaVencimiento);
    }

    private void mostrarConfirmacion() {
        System.out.println("Préstamo registrado y comprobante emitido correctamente.");
    }

    private void emitirComprobante(Socio socio, Ejemplar ejemplar) {
        Prestamo ultimo = controlPrestamos.getPrestamos()
                .get(controlPrestamos.getPrestamos().size() - 1);

        Comprobante comprobante = controlComprobantes.generarComprobante("Préstamo", ultimo);
        controlComprobantes.emitirComprobante(comprobante);
    }

    // Métodos simulados: en versión final, deben conectarse con ControlUsuarios y ControlInventario
    private Socio buscarSocio(String dni) {
        return new Socio(
                1,
                "Juan",
                "Pérez",
                dni,
                "juanperez@mail.com",
                "123456789",
                LocalDate.now().minusYears(1),
                null, // TipoUsuario simulado
                "juanp",
                "1234",
                101,
                LocalDate.now().plusYears(1),
                "Activo",
                false,
                false
        );
    }

    private Ejemplar buscarEjemplar(String codigo) {
        return new Ejemplar(1, codigo, true);
    }
}

