package biblioteca.ui.formularios;

import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.services.ControlDevoluciones;

import java.util.Scanner;

public class FormularioDevolucion {

    private ControlDevoluciones controlDevoluciones;
    private Scanner scanner;

    public FormularioDevolucion(ControlDevoluciones controlDevoluciones) {
        this.controlDevoluciones = controlDevoluciones;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarFormulario() {
        System.out.println("=== REGISTRO DE DEVOLUCIÓN ===");
        try {
            Prestamo prestamo = buscarPrestamo();
            if (prestamo == null) {
                mostrarError("No se encontró el préstamo o no es válido para devolución.");
                return;
            }

            String estadoEjemplar = ingresarEstadoEjemplar();
            String observaciones = ingresarObservaciones();

            Devolucion devolucion = controlDevoluciones.registrarDevolucion(
                    prestamo.getId(),
                    estadoEjemplar,
                    observaciones
            );

            mostrarConfirmacion(devolucion);

        } catch (NumberFormatException e) {
            mostrarError("ID de préstamo inválido. Debe ser un número entero.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private Prestamo buscarPrestamo() {
        System.out.print("Ingrese ID del préstamo: ");
        int id = Integer.parseInt(scanner.nextLine());
        return controlDevoluciones.getPrestamos().stream()
                .filter(p -> p.getId() == id && controlDevoluciones.validarPrestamo(id))
                .findFirst()
                .orElse(null);
    }

    private String ingresarEstadoEjemplar() {
        System.out.println("Ingrese estado final del ejemplar (Disponible, Dañado, Extraviado): ");
        String estado;
        while (true) {
            estado = scanner.nextLine().trim();
            if (estado.isBlank()) estado = "Disponible";
            if (estado.equalsIgnoreCase("Disponible") || estado.equalsIgnoreCase("Dañado") || estado.equalsIgnoreCase("Extraviado"))
                break;
            System.out.println("Estado inválido. Debe ser Disponible, Dañado o Extraviado.");
        }
        return estado;
    }

    private String ingresarObservaciones() {
        System.out.print("Ingrese observaciones (opcional): ");
        return scanner.nextLine().trim();
    }

    private void mostrarConfirmacion(Devolucion devolucion) {
        System.out.println("Devolución registrada correctamente:");
        System.out.println(devolucion.formatearParaUI());
    }

    private void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }
}

