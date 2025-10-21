package biblioteca.entities.notificaciones;

import biblioteca.entities.prestamos.Prestamo;
import java.time.LocalDate;

public class Notificacion {
    private int id;
    private LocalDate fechaEnvio;
    private String tipo;
    private String mensaje;
    private boolean leida;
    private Prestamo prestamo;

    public Notificacion(int id, String tipo, Prestamo prestamo) {
        this.id = id;
        this.tipo = tipo;
        this.prestamo = prestamo;
        this.fechaEnvio = LocalDate.now();
        this.mensaje = generarMensaje();
        this.leida = false;
    }

    public void enviar() {
        if (prestamo != null && prestamo.obtenerSocio() != null) {
            String email = prestamo.obtenerSocio().getEmail();
            System.out.println("Enviando notificación a: " + email);
            System.out.println("Asunto: " + tipo);
            System.out.println("Mensaje:\n" + mensaje);
        } else {
            System.out.println("No se pudo enviar la notificación: falta información del socio.");
        }
    }

    public void marcarComoLeida() {
        this.leida = true;
        System.out.println("Notificación marcada como leída.");
    }

    public Prestamo obtenerPrestamo() {
        return prestamo;
    }

    public String generarMensaje() {
        if (prestamo == null || prestamo.obtenerSocio() == null || prestamo.obtenerEjemplar() == null) {
            return "Información incompleta para generar el mensaje.";
        }

        String nombre = prestamo.obtenerSocio().getNombreCompleto();
        String tituloLibro = prestamo.obtenerEjemplar().obtenerLibro().getTitulo();
        String mensajeGenerado = "";

        switch (tipo.toLowerCase()) {
            case "recordatorio":
                mensajeGenerado = "Hola " + nombre + ", te recordamos que el libro '" +
                        tituloLibro + "' debe devolverse antes del " + prestamo.getFechaVencimiento() + ".";
                break;

            case "vencimiento":
                mensajeGenerado = "Hola " + nombre + ", tu préstamo del libro '" +
                        tituloLibro + "' ha vencido. Por favor, acercate a la biblioteca para regularizar la situación.";
                break;

            case "confirmación":
                mensajeGenerado = "Hola " + nombre + ", tu préstamo del libro '" +
                        tituloLibro + "' fue registrado con éxito. Fecha de vencimiento: " + prestamo.getFechaVencimiento() + ".";
                break;

            default:
                mensajeGenerado = "Hola " + nombre + ", este es un aviso del sistema de biblioteca sobre tu préstamo.";
                break;
        }

        return mensajeGenerado;
    }

    public int getId() { return id; }
    public LocalDate getFechaEnvio() { return fechaEnvio; }
    public String getTipo() { return tipo; }
    public String getMensaje() { return mensaje; }
    public boolean isLeida() { return leida; }

    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }

    @Override
    public String toString() {
        return "Notificación #" + id +
                " | Tipo: " + tipo +
                " | Fecha de envío: " + fechaEnvio +
                " | Leída: " + (leida ? "Sí" : "No");
    }

}
