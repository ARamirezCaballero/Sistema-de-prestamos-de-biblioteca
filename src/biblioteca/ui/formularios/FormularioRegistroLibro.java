package biblioteca.ui.formularios;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.services.ControlLibros;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Formulario para el caso de uso CU02 – Registrar Libro.
 * Permite al bibliotecario ingresar los datos de un nuevo libro,
 * confirmar su registro y generar ejemplares asociados.
 */
public class FormularioRegistroLibro {

    private final ControlLibros controlLibros;
    private final Scanner scanner;

    private Libro libroTemporal;

    public FormularioRegistroLibro(ControlLibros controlLibros) {
        this.controlLibros = controlLibros;
        this.scanner = new Scanner(System.in);
    }

    /** Muestra el formulario principal de registro */
    public void mostrarFormulario() {
        System.out.println("\n=== REGISTRO DE NUEVO LIBRO ===");

        try {
            ingresarDatos();
            ingresarCantidadEjemplares();
            confirmarRegistro();
        } catch (Exception e) {
            mostrarError("Error durante el registro: " + e.getMessage());
        }
    }

    /** Solicita los datos básicos del libro al bibliotecario */
    public void ingresarDatos() {
        try {
            System.out.print("ID del libro: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Título: ");
            String titulo = scanner.nextLine();

            System.out.print("Autor: ");
            String autor = scanner.nextLine();

            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            System.out.print("Editorial: ");
            String editorial = scanner.nextLine();

            System.out.print("Año de publicación: ");
            int anio = scanner.nextInt();
            scanner.nextLine();

            // Crear objeto temporal
            libroTemporal = new Libro(id, titulo, autor, isbn, categoria, editorial, anio);

        } catch (InputMismatchException e) {
            scanner.nextLine();
            throw new IllegalArgumentException("Entrada inválida. Verifique los campos numéricos.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al ingresar datos: " + e.getMessage());
        }
    }

    /** Pide la cantidad de ejemplares que se deben crear */
    public void ingresarCantidadEjemplares() {
        if (libroTemporal == null) {
            throw new IllegalStateException("Debe ingresar primero los datos del libro.");
        }

        System.out.print("Cantidad de ejemplares: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares debe ser mayor a 0.");
        }

        controlLibros.crearEjemplares(libroTemporal, cantidad);
    }

    /** Confirma el registro y lo guarda en el sistema */
    public void confirmarRegistro() {
        if (libroTemporal == null) throw new IllegalStateException("No hay datos cargados para registrar.");

        System.out.print("¿Desea confirmar el registro de este libro? (S/N): ");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("S")) {
            controlLibros.registrarLibro(libroTemporal);

            // ---> Integración con BaseDatosSimulada
            BaseDatosSimulada.guardarLibro(libroTemporal);
            for (Ejemplar e : libroTemporal.obtenerEjemplares()) {
                BaseDatosSimulada.guardarEjemplar(e);
            }

            System.out.println("Libro registrado exitosamente y sincronizado con la BD simulada.");
        } else {
            System.out.println("Registro cancelado por el usuario.");
        }

        libroTemporal = null;
    }


    /** Muestra mensajes de error por consola */
    public void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }
}
