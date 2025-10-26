package biblioteca.data;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.inventario.Libro;
import biblioteca.entities.notificaciones.Notificacion;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.prestamos.PoliticaPrestamo;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.usuarios.TipoUsuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosSimulada {

    private static final List<Socio> socios = new ArrayList<>();
    private static final List<Libro> libros = new ArrayList<>();
    private static final List<Ejemplar> ejemplares = new ArrayList<>();
    private static final List<Prestamo> prestamos = new ArrayList<>();
    private static final List<PoliticaPrestamo> politicas = new ArrayList<>();
    private static final List<Devolucion> devoluciones = new ArrayList<>();



    public static void agregarSocio(Socio socio) {
        if (socio != null) {
            socios.add(socio);
        }
    }

    // === MÉTODOS DE CONSULTA ===
    public static Socio buscarSocioPorDni(String dni) {
        return socios.stream()
                .filter(s -> s.getDni().equalsIgnoreCase(dni))
                .findFirst()
                .orElse(null);
    }

    public static Ejemplar buscarEjemplarPorCodigo(String codigo) {
        return ejemplares.stream()
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public static List<Prestamo> buscarPrestamosPorSocio(String dni) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.getSocio().getDni().equalsIgnoreCase(dni)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public static String consultarEstadoEjemplar(String codigo) {
        Ejemplar e = buscarEjemplarPorCodigo(codigo);
        return (e != null) ? e.getEstado() : null;
    }

    public static boolean verificarEstadoSocio(String dni) {
        Socio s = buscarSocioPorDni(dni);
        if (s == null) return false;
        return !s.getEstado().equalsIgnoreCase("Inhabilitado");
    }

    // === MÉTODOS DE POLÍTICAS SIMULADAS ===
    public static PoliticaPrestamo obtenerPoliticaPorSocio(String dniSocio) {
        return politicas.isEmpty() ? null : politicas.get(0);
    }

    public static int obtenerPoliticaDiasPrestamo(String dniSocio) {
        PoliticaPrestamo p = obtenerPoliticaPorSocio(dniSocio);
        return (p != null) ? p.getDiasPrestamo() : 7;
    }

    public static int obtenerMaximoPrestamosSimultaneos(String dniSocio) {
        PoliticaPrestamo p = obtenerPoliticaPorSocio(dniSocio);
        return (p != null) ? p.getMaxPrestamosSimultaneos() : 3;
    }

    public static double obtenerMultaPorDia(String dniSocio) {
        PoliticaPrestamo p = obtenerPoliticaPorSocio(dniSocio);
        return (p != null) ? p.getMultaPorDia() : 50.0;
    }

    // === MÉTODOS DE MODIFICACIÓN ===
    public static void guardarPrestamo(Prestamo p) {
        prestamos.add(p);
    }

    public static void guardarLibro(Libro l) {
        libros.add(l);
    }

    public static void guardarEjemplar(Ejemplar e) {
        ejemplares.add(e);
    }

    public static void actualizarEstadoEjemplar(String codigo, String nuevoEstado) {
        Ejemplar e = buscarEjemplarPorCodigo(codigo);
        if (e != null) e.setEstado(nuevoEstado);
    }

    // === DEVOLUCIONES ===
    public static void agregarDevolucion(Devolucion d) {
        devoluciones.add(d);
    }

    public static List<Devolucion> getDevoluciones() {
        return new ArrayList<>(devoluciones);
    }

    // === MÉTODOS DE ACCESO GENERALES ===
    public static List<PoliticaPrestamo> getPoliticas() {
        return politicas;
    }

    public static int generarNuevoIdSocio() {
        return socios.size() + 1;
    }

    public static int generarNuevoNumeroSocio() {
        return 1000 + socios.size();
    }

    public static String generarCodigoEjemplar() {
        return "EJ" + String.format("%03d", ejemplares.size() + 1);
    }

    public static List<Socio> getSocios() {
        return socios;
    }

    public static List<Libro> getLibros() {
        return new ArrayList<>(libros);
    }

    public static List<Ejemplar> getEjemplares() {
        return new ArrayList<>(ejemplares);
    }

    public static List<Prestamo> getPrestamos() {
        return new ArrayList<>(prestamos);
    }

    // === NOTIFICACIONES ===
    private static final List<Notificacion> notificaciones = new ArrayList<>();

    public static void agregarNotificacion(Notificacion n) {
        if (n != null) notificaciones.add(n);
    }

    public static List<Notificacion> getNotificaciones() {
        return new ArrayList<>(notificaciones);
    }

    // === Inicialización de datos de prueba ===
    public static void inicializarDatos() {
        LocalDate fechaAlta = LocalDate.now().minusMonths(2);

        Socio s1 = new Socio(
                1, "Camila", "López", "12345678",
                "camila.lopez@email.com", "3875123456",
                fechaAlta, TipoUsuario.SOCIO, "CamilaL", "1234",
                1001, fechaAlta.plusYears(1), "Activo", false, false
        );

        Socio s2 = new Socio(
                2, "Carlos", "Gómez", "23456789",
                "carlos.gomez@email.com", "3875987654",
                fechaAlta.minusMonths(1), TipoUsuario.SOCIO, "cgomez", "abcd",
                1002, fechaAlta.plusYears(1), "Activo", false, true
        );

        socios.add(s1);
        socios.add(s2);

        Libro l1 = new Libro(1, "El Principito", "Antoine de Saint-Exupéry", "1111", "Ficción", "Andina", 1943);
        Libro l2 = new Libro(2, "1984", "George Orwell", "2222", "Distopía", "Planeta", 1949);

        libros.add(l1);
        libros.add(l2);

        Ejemplar e1 = new Ejemplar("EJ001", l1, "Disponible");
        Ejemplar e2 = new Ejemplar("EJ002", l1, "Disponible");
        Ejemplar e3 = new Ejemplar("EJ003", l2, "Disponible");
        Ejemplar e4 = new Ejemplar("EJ004", l2, "Disponible");

        l1.agregarEjemplar(e1);
        l1.agregarEjemplar(e2);
        l2.agregarEjemplar(e3);
        l2.agregarEjemplar(e4);

        ejemplares.add(e1);
        ejemplares.add(e2);
        ejemplares.add(e3);
        ejemplares.add(e4);

        PoliticaPrestamo politicaEstandar = new PoliticaPrestamo(1, "Estándar", 14, 3, 50.0);
        PoliticaPrestamo politicaDocente = new PoliticaPrestamo(2, "Docente", 21, 5, 30.0);
        PoliticaPrestamo politicaInfantil = new PoliticaPrestamo(3, "Infantil", 10, 2, 10.0);

        politicas.add(politicaEstandar);
        politicas.add(politicaDocente);
        politicas.add(politicaInfantil);

        // === Préstamos de prueba para notificaciones ===
        LocalDate hoy = LocalDate.now();

        // 1. Préstamo vigente (activo, dentro del plazo)
        Prestamo p1 = new Prestamo(1,
                hoy.minusDays(3),
                hoy.plusDays(10),
                s1, e1, politicaEstandar);
        e1.marcarComoPrestado();

        // 2. Préstamo por vencer (vence mañana)
        Prestamo p2 = new Prestamo(2,
                hoy.minusDays(13),
                hoy.plusDays(1),
                s1, e2, politicaEstandar);
        e2.marcarComoPrestado();

        // 3. Préstamo vencido (venció hace 5 días)
        Prestamo p3 = new Prestamo(3,
                hoy.minusDays(20),
                hoy.minusDays(5),
                s2, e4, politicaDocente);
        e4.marcarComoPrestado();

        prestamos.add(p1);
        prestamos.add(p2);
        prestamos.add(p3);
    }

}


