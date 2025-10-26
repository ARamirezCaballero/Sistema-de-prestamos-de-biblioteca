package biblioteca.ui;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;
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
    private ControlHistorial controlHistorial;
    private final ControlLibros controlLibros;
    private final FormularioLogin formularioLogin;
    private final ControlNotificaciones controlNotificaciones;

    public MenuPrincipal(ControlLibros controlLibros) {
        this.controlLibros = controlLibros;
        scanner = new Scanner(System.in);

        // Inicialización de controles
        controlUsuarios = new ControlUsuarios();
        controlPoliticas = new ControlPoliticas();
        controlValidaciones = new ControlValidaciones();
        controlDevoluciones = new ControlDevoluciones(controlHistorial);
        controlComprobantes = new ControlComprobantes();
        controlConsultas = new ControlConsultas();
        controlHistorial = new ControlHistorial();
        controlNotificaciones = new ControlNotificaciones(BaseDatosSimulada.getPrestamos());

        // Obtener socio inicial de prueba (el primero cargado en la BaseDatosSimulada)
        Socio socioInicial = BaseDatosSimulada.buscarSocioPorDni("12345678");
        PoliticaPrestamo politicaInicial;
        if (socioInicial != null) {
            politicaInicial = BaseDatosSimulada.obtenerPoliticaPorSocio(socioInicial.getDni());
        } else {
            // Fallback en caso de que no haya socios cargados
            politicaInicial = new PoliticaPrestamo(1, "Estándar", 7, 3, 50.0);
        }

        // Inicializar control de préstamos con política obtenida
        controlPrestamos = new ControlPrestamos(politicaInicial, controlPoliticas, controlValidaciones, controlHistorial);
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
        if (!formularioLogin.mostrarFormulario()) {
            System.out.println("No se pudo iniciar sesión. Volviendo al menú principal...");
            return;
        }

        boolean salir = false;
        while (!salir) {
            mostrarOpcionesBibliotecario();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1 -> registrarLibro();
                case 2 -> registrarPrestamo();
                case 3 -> registrarDevolucion();
                case 4 -> consultarHistorial();
                case 5 -> generarReporte();
                case 6 -> controlNotificaciones.procesoNotificaciones();
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
        Socio socioActivo = null; // mantiene referencia al socio logueado

        while (!salir) {
            if (socioActivo == null) {
                // Menú sin sesión iniciada
                System.out.println("""
                    
                    ===== MENÚ SOCIO =====
                    1. Registrarse
                    2. Iniciar sesión
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
                        socioActivo = loginSocio.mostrarFormularioYRetornarSocio();
                        if (socioActivo != null) {
                            System.out.println("Login exitoso. Bienvenido/a, " + socioActivo.getNombreCompleto() + ".");
                        } else {
                            System.out.println("Credenciales incorrectas o usuario no encontrado.");
                        }
                    }
                    case 0 -> salir = true;
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } else {
                // Menú con sesión iniciada
                System.out.println("\n===== MENÚ SOCIO (" + socioActivo.getNombreCompleto() + ") =====");
                System.out.println("""
                    1. Ver mi historial de préstamos
                    2. Ver libros y ejemplares disponibles
                    0. Cerrar sesión
                    """);
                System.out.print("Seleccione una opción: ");
                int opcion = leerOpcion();

                switch (opcion) {
                    case 1 -> {
                        System.out.println("\n=== HISTORIAL PERSONAL ===");
                        controlConsultas.consultarHistorialPorSocio(socioActivo.getDni());
                    }
                    case 2 -> {
                        System.out.println("\n=== LIBROS Y EJEMPLARES DISPONIBLES ===");
                        BaseDatosSimulada.getEjemplares().stream()
                                .filter(e -> e.verificarDisponibilidad())
                                .forEach(e -> System.out.printf("Libro: %s | Ejemplar: %s | Estado: %s%n",
                                        e.getLibro() != null ? e.getLibro().getTitulo() : "Sin título",
                                        e.getCodigo(),
                                        e.getEstado()));
                    }
                    case 0 -> {
                        System.out.println("Cerrando sesión de socio...");
                        socioActivo = null;
                    }
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            }
        }
    }

    // ======== MÉTODOS DEL BIBLIOTECARIO ========
    private void mostrarOpcionesBibliotecario() {
        System.out.println("""
                
                ===== MENÚ BIBLIOTECARIO =====
                1. Registrar libro o ejemplar
                2. Registrar préstamo
                3. Registrar devolución
                4. Consultar historial
                5. Generar reporte
                6. Ejecutar proceso de notificaciones automáticas
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

    private void registrarLibro() {
        System.out.println("\n--- Registrar Libro o Ejemplar ---");
        FormularioRegistroLibro formulario = new FormularioRegistroLibro(controlLibros);
        formulario.mostrarFormulario();
    }

    private void registrarPrestamo() {
        System.out.println("\n--- Registrar Préstamo ---");
        FormularioPrestamos formulario = new FormularioPrestamos(
                controlPrestamos,
                controlValidaciones,
                controlComprobantes,
                controlUsuarios,
                controlPoliticas
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
        System.out.println("\n--- REPORTE GENERAL ---");

        // 1. Préstamos activos
        System.out.println("\n1. Préstamos activos:");
        if (BaseDatosSimulada.getPrestamos().isEmpty()) {
            System.out.println("No hay préstamos registrados.");
        } else {
            for (Prestamo p : BaseDatosSimulada.getPrestamos()) {
                p.actualizarEstado(); // asegura que el estado esté correcto
                if (!"Devuelto".equalsIgnoreCase(p.getEstado())) {
                    System.out.printf("Socio: %s | Libro: %s | Ejemplar: %s | Fecha préstamo: %s | Vence: %s | Estado: %s%n",
                            p.getSocio().getNombreCompleto(),
                            p.getEjemplar().getLibro() != null ? p.getEjemplar().getLibro().getTitulo() : "Sin libro",
                            p.getEjemplar().getCodigo(),
                            p.getFechaPrestamo(),
                            p.getFechaVencimiento(),
                            p.getEstado());
                }
            }
        }

        // 2. Socios con atrasos o sanciones
        System.out.println("\n2. Socios con atrasos o sanciones:");
        boolean haySociosConProblemas = false;
        for (Socio s : BaseDatosSimulada.getSocios()) {
            if (s.isTieneAtrasos() || s.isTieneSanciones()) {
                System.out.printf("Socio: %s | Atrasos: %s | Sanciones: %s | Estado: %s%n",
                        s.getNombreCompleto(),
                        s.isTieneAtrasos(),
                        s.isTieneSanciones(),
                        s.getEstado());
                haySociosConProblemas = true;
            }
        }
        if (!haySociosConProblemas) {
            System.out.println("No hay socios con atrasos o sanciones.");
        }

        // 3. Inventario de libros
        System.out.println("\n3. Inventario de libros:");
        if (BaseDatosSimulada.getLibros().isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro l : BaseDatosSimulada.getLibros()) {
                long disponibles = BaseDatosSimulada.getEjemplares().stream()
                        .filter(e -> e.getLibro() != null && e.getLibro().equals(l) && e.verificarDisponibilidad())
                        .count();
                long prestados = BaseDatosSimulada.getEjemplares().stream()
                        .filter(e -> e.getLibro() != null && e.getLibro().equals(l) && !"Disponible".equalsIgnoreCase(e.getEstado()))
                        .count();

                System.out.printf("Libro: %s | Disponibles: %d | Prestados: %d%n",
                        l.getTitulo(), disponibles, prestados);
            }
        }
    }

}