package biblioteca.ui.pantallas;

import biblioteca.entities.reportes.Historial;
import biblioteca.services.ControlConsultas;
import biblioteca.services.ControlHistorial;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario para el caso de uso CU11 – Consultar Historial.
 * Permite visualizar historiales de socios o libros, aplicar filtros
 * y generar reportes de los movimientos registrados en el sistema.
 */
public class PantallaHistorial {

    private final ControlConsultas controlConsultas;
    private final ControlHistorial controlHistorial;
    private final Scanner scanner;

    public PantallaHistorial(ControlConsultas controlConsultas, ControlHistorial controlHistorial) {
        this.controlConsultas = controlConsultas;
        this.controlHistorial = controlHistorial;
        this.scanner = new Scanner(System.in);
    }

    /** Muestra el menú principal de la pantalla */
    public void mostrarPantalla() {
        int opcion = -1;
        do {
            System.out.println("\n===== CONSULTA DE HISTORIAL =====");
            System.out.println("1. Consultar historial por socio");
            System.out.println("2. Consultar historial por libro");
            System.out.println("3. Ver historial completo del sistema");
            System.out.println("4. Exportar reporte de préstamos");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // limpia buffer
                seleccionarCriterio(opcion);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Ingrese un número válido.");
                scanner.nextLine();
            }
        } while (opcion != 0);
    }

    /** Gestiona la selección del criterio de consulta */
    private void seleccionarCriterio(int opcion) {
        switch (opcion) {
            case 1 -> consultarHistorialSocio();
            case 2 -> consultarHistorialLibro();
            case 3 -> mostrarHistorialCompleto();
            case 4 -> exportarReporte();
            case 0 -> System.out.println("Regresando al menú principal...");
            default -> System.out.println("Opción no válida.");
        }
    }

    /** Consulta y muestra el historial de un socio por su ID */
    private void consultarHistorialSocio() {
        System.out.print("Ingrese el ID del socio: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        List<Historial> historiales = controlHistorial.consultarHistorialUsuario(id);
        mostrarHistorial(historiales);
    }

    /** Consulta y muestra el historial de un libro por su ID */
    private void consultarHistorialLibro() {
        System.out.print("Ingrese el ID del libro: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        List<Historial> historiales = controlHistorial.consultarHistorialLibro(id);
        mostrarHistorial(historiales);
    }

    /** Muestra por consola los historiales obtenidos */
    private void mostrarHistorial(List<Historial> historiales) {
        if (historiales == null || historiales.isEmpty()) {
            System.out.println("No se encontraron registros para el criterio seleccionado.");
            return;
        }

        System.out.println("\n=== RESULTADOS DE LA CONSULTA ===");
        for (Historial h : historiales) {
            System.out.println(h.generarResumen());
        }
    }

    //Muestra todo el historial del sistema (desde ControlHistorial)
    private void mostrarHistorialCompleto() {
        controlHistorial.mostrarHistorialCompleto();
    }

    /** Exporta un reporte textual (simulado en consola) */
    private void exportarReporte() {
        System.out.println("\nGenerando reporte de préstamos...");
        String reporte = controlConsultas.generarReporte();
        System.out.println(reporte);
        System.out.println("Reporte exportado exitosamente (simulado).");
    }
}
