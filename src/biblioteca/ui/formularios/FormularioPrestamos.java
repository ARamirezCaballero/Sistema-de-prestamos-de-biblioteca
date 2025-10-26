package biblioteca.ui.formularios;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Usuario;
import biblioteca.services.*;
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
    private final ControlComprobantes controlComprobantes;
    private final ControlUsuarios controlUsuarios;
    private final Scanner scanner;

    public FormularioPrestamos(ControlPrestamos controlPrestamos,
                               ControlValidaciones controlValidaciones,
                               ControlComprobantes controlComprobantes, ControlUsuarios controlUsuarios, ControlPoliticas controlPoliticas) {
        this.controlPrestamos = controlPrestamos;
        this.controlComprobantes = controlComprobantes;
        this.controlUsuarios = controlUsuarios;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarFormulario() {
        System.out.println("\n=== FORMULARIO DE PRÉSTAMOS ===");
        iniciarRegistroPrestamo();
    }

    public void iniciarRegistroPrestamo() {
        System.out.println("\n=== REGISTRO DE PRÉSTAMO ===");
        try {
            String[] datos = ingresarDatos(); // Paso 2
            String dniSocio = datos[0];
            String codigoEjemplar = datos[1];

            Prestamo prestamo = controlPrestamos.registrarPrestamo(dniSocio, codigoEjemplar); // Paso 3
            confirmarPrestamo(prestamo); // Paso 26

        } catch (Exception e) {
            System.out.println("Error durante el registro del préstamo: " + e.getMessage());
        }
    }

    // Paso 2
    private String[] ingresarDatos() {
        System.out.print("Ingrese DNI del socio: ");
        String dni = scanner.nextLine();

        System.out.print("Ingrese código del ejemplar: ");
        String codigo = scanner.nextLine();

        return new String[]{dni, codigo};
    }


    private void mostrarFechaCalculada(LocalDate fechaVencimiento) {
        System.out.println("Fecha de devolución calculada: " + fechaVencimiento);
    }

    private void confirmarPrestamo(Prestamo p) {
        System.out.println("\n=== Préstamo registrado exitosamente ===");
        System.out.println("Socio: " + p.getSocio().getNombre());
        System.out.println("Ejemplar: " + p.getEjemplar().getCodigo());
        System.out.println("Fecha de vencimiento: " + p.getFechaVencimiento());
        emitirComprobante(p.getSocio(), p.getEjemplar());
    }

    private void emitirComprobante(Socio socio, Ejemplar ejemplar) {
        Prestamo ultimo = controlPrestamos.getPrestamos()
                .get(controlPrestamos.getPrestamos().size() - 1);

        Comprobante comprobante = controlComprobantes.generarComprobante("Préstamo", ultimo);
        controlComprobantes.emitirComprobante(comprobante);
    }

    private Socio buscarSocio(String dni) {
        Usuario usuario = controlUsuarios.buscarPorDni(dni);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró un socio con DNI: " + dni);
        }
        if (!(usuario instanceof Socio)) {
            throw new IllegalArgumentException("El usuario con DNI " + dni + " no es un socio.");
        }
        return (Socio) usuario;
    }

    private Ejemplar buscarEjemplar(String codigo) {
        Ejemplar ejemplar = BaseDatosSimulada.buscarEjemplarPorCodigo(codigo);
        if (ejemplar == null) {
            throw new IllegalArgumentException("No se encontró un ejemplar con código: " + codigo);
        }
        return ejemplar;
    }
}

