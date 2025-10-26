package biblioteca.services;

import biblioteca.data.BaseDatosSimulada;
import biblioteca.entities.notificaciones.Notificacion;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Usuario;
import biblioteca.entities.inventario.Ejemplar;
import biblioteca.ui.componentes.NotificadorEmail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ControlNotificaciones {

    private List<Notificacion> notificaciones;
    private List<Prestamo> prestamos;

    public ControlNotificaciones(List<Prestamo> prestamos) {
        this.prestamos = (prestamos != null) ? prestamos : new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public void verificarVencimientos() {
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados para verificar vencimientos.");
            return;
        }

        LocalDate hoy = LocalDate.now();

        for (Prestamo p : prestamos) {
            if (p == null || p.getSocio() == null || p.getEjemplar() == null) continue;

            p.actualizarEstado();
            long diasRestantes = ChronoUnit.DAYS.between(hoy, p.getFechaVencimiento());

            if ("Activo".equalsIgnoreCase(p.getEstado()) && diasRestantes == 2) {
                generarNotificacion(p, "Recordatorio: su préstamo vence en 2 días.");
            }
            else if ("Vencido".equalsIgnoreCase(p.getEstado())) {
                generarNotificacion(p, "Aviso: su préstamo ha vencido. Por favor, devuelva el ejemplar.");
            }
        }
    }

    private void generarNotificacion(Prestamo prestamo, String mensaje) {
        Usuario destinatario = prestamo.getSocio();
        Ejemplar ejemplar = prestamo.getEjemplar();

        for (Notificacion n : notificaciones) {
            if (n.getDestinatario() != null && n.getEjemplarRelacionado() != null) {
                boolean mismoUsuario = n.getDestinatario().equals(destinatario);
                boolean mismoEjemplar = n.getEjemplarRelacionado().equals(ejemplar);
                boolean mismoMensaje = n.getMensaje().equalsIgnoreCase(mensaje);
                if (mismoUsuario && mismoEjemplar && mismoMensaje) {
                    System.out.println("Ya existe una notificación similar para este préstamo.");
                    return;
                }
            }
        }

        Notificacion nueva = new Notificacion(
                notificaciones.size() + 1,
                mensaje,
                LocalDateTime.now(),
                destinatario,
                ejemplar
        );

        notificaciones.add(nueva);
        biblioteca.data.BaseDatosSimulada.agregarNotificacion(nueva);
        System.out.println("Notificación generada: " + nueva);
    }

    public void enviarNotificaciones() {
        if (notificaciones.isEmpty()) {
            System.out.println("No hay notificaciones pendientes para enviar.");
            return;
        }

        for (Notificacion n : notificaciones) {
            String destinatario = (n.getDestinatario() != null) ? n.getDestinatario().getNombreCompleto() : "Desconocido";
            System.out.println("Enviando notificación a " + destinatario + ": " + n.getMensaje());
        }

        System.out.println("Todas las notificaciones fueron enviadas correctamente (simulado).");
    }

    public void ejecutarProcesoNotificaciones() {
        System.out.println("=== INICIO DEL PROCESO DE NOTIFICACIONES ===");
        verificarVencimientos();
        enviarNotificaciones();
        System.out.println("=== PROCESO FINALIZADO ===");
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void mostrarNotificaciones() {
        if (notificaciones.isEmpty()) {
            System.out.println("No hay notificaciones registradas.");
            return;
        }

        System.out.println("=== LISTADO DE NOTIFICACIONES ===");
        for (Notificacion n : notificaciones) {
            System.out.println(n);
        }
    }

    public void procesoNotificaciones() {
        System.out.println("=== Envio de la notificaión ===");

        try {
            // 1. Obtener préstamos desde la BD simulada
            List<Prestamo> prestamos = BaseDatosSimulada.getPrestamos();

            // 2. Ejecutar proceso de generación de notificaciones
            ControlNotificaciones control = new ControlNotificaciones(prestamos);
            control.ejecutarProcesoNotificaciones();

            // 3. Enviar notificaciones simuladas
            NotificadorEmail notificador = new NotificadorEmail("smtp.biblioteca.edu", 587, control);
            notificador.enviarNotificacionPrestamo();

            System.out.println("=== FIN NOTIFICACIONES ===");

        } catch (Exception e) {
            System.err.println("Error en el envío de notificaciones: " + e.getMessage());
        }
    }
}
