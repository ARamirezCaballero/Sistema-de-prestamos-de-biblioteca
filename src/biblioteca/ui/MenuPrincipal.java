package biblioteca.ui;

import biblioteca.services.*;
import biblioteca.ui.formularios.*;
import biblioteca.ui.pantallas.*;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final ControlUsuarios controlUsuarios;
    private final ControlPrestamos controlPrestamos;
    private final ControlDevoluciones controlDevoluciones;
    private final ControlPoliticas controlPoliticas;
    private final ControlValidaciones controlValidaciones;
    private final ControlComprobantes controlComprobantes;
    private final ControlConsultas controlConsultas;
    private final ControlHistorial controlHistorial;

    private final FormularioLogin formularioLogin;

    public MenuPrincipal() {
        scanner = new Scanner(System.in);

        // Inicialización de controles (capa de control)
        controlUsuarios = new ControlUsuarios();
        controlPoliticas = new ControlPoliticas();
        controlPrestamos = new ControlPrestamos(controlPoliticas.obtenerPoliticaPrestamo());
        controlDevoluciones = new ControlDevoluciones();
        controlValidaciones = new ControlValidaciones();
        controlComprobantes = new ControlComprobantes();
        controlHistorial = new ControlHistorial();
        controlConsultas = new ControlConsultas();

        // Frontera de login (para bibliotecarios)
        formularioLogin = new FormularioLogin(controlUsuarios);
    }

    public void iniciar() {
        System.out.println("===== SISTEMA DE GESTIÓN BIBLIOTECARIA =====");

        boolean salirSistema = false;

        while (!salirSistema) {
            System.out.println("""
                    
                    ===== SELECCIÓN DE ROL =====
                    1. Bibliotecario
                    2. Socio (Portal Web Simulado)
                    0. Salir
                    """);
            System.out.print("Seleccione una opción: ");
            int rol = leerOpcion();

            switch (rol) {
                case 1 -> iniciarBibliotecario();
                case 2 -> iniciarSocio();
                case 0 -> {
                    salirSistema = true;
                    System.out.println("Saliendo del sistema...");
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // ======== FLUJO DEL BIBLIOTECARIO ========
    private void iniciarBibliotecario() {
        System.out.println("\n--- Ingreso Bibliotecario ---");
        formularioLogin.mostrarFormulario(); // login bibliotecario

        boolean salir = false;
        while (!salir) {
            mostrarOpcionesBibliotecario();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1 -> registrarPrestamo();
                case 2 -> registrarDevolucion();
                case 3 -> consultarHistorial();
                case 4 -> generarReporte();
                case 0 -> {
                    salir = true;
                    System.out.println("Cerrando sesión de bibliotecario...");
                }
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // ======== FLUJO DEL SOCIO ========
    private void iniciarSocio() {
        System.out.println("\n=== Portal Web Simulado para Socios ===");
        boolean salir = false;

        while (!salir) {
            System.out.println("""
                    
                    ===== MENÚ SOCIO =====
                    1. Registrarse
                    2. Iniciar sesión
                    3. Consultar historial de préstamos
                    0. Volver al menú principal
                    """);
            System.out.print("Seleccione una opción: ");
            int opcion = leerOpcion();

            switch (opcion) {
                case 1 -> {
                    FormularioRegistroSocio registro = new FormularioRegistroSocio(controlUsuarios, controlValidaciones);
                    registro.mostrarFormulario();
                }
                case 2 -> {
                    FormularioLogin loginSocio = new FormularioLogin(controlUsuarios);
                    loginSocio.mostrarFormulario();
                }
                case 3 -> {
                    PantallaHistorial pantallaSocio = new PantallaHistorial(controlConsultas, controlHistorial);
                    pantallaSocio.mostrarPantalla();
                }
                case 0 -> salir = true;
                default -> System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // ======== MÉTODOS DEL BIBLIOTECARIO ========
    private void mostrarOpcionesBibliotecario() {
        System.out.println("""
                
                ===== MENÚ BIBLIOTECARIO =====
                1. Registrar préstamo
                2. Registrar devolución
                3. Consultar historial
                4. Generar reporte
                0. Cerrar sesión
                """);
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void registrarPrestamo() {
        System.out.println("\n--- Registrar Préstamo ---");
        FormularioPrestamos formulario = new FormularioPrestamos(
                controlPrestamos,
                controlValidaciones,
                controlComprobantes
        );
        formulario.mostrarFormulario();
    }

    private void registrarDevolucion() {
        System.out.println("\n--- Registrar Devolución ---");
        FormularioDevolucion formulario = new FormularioDevolucion(controlDevoluciones);
        formulario.mostrarFormulario();
    }

    private void consultarHistorial() {
        System.out.println("\n--- Consultar Historial ---");
        PantallaHistorial pantalla = new PantallaHistorial(controlConsultas, controlHistorial);
        pantalla.mostrarPantalla();
    }

    private void generarReporte() {
        System.out.println("\n--- Generar Reporte ---");
        // Implementar lógica de generación de reportes si aplica
    }
}


