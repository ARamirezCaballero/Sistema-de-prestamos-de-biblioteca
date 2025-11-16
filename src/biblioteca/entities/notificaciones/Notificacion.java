package biblioteca.entities.notificaciones;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notificacion {

    private int idNotificacion;
    private String mensaje;
    private LocalDateTime fechaHora;
    private Usuario destinatario;
    private Ejemplar ejemplarRelacionado;
    private Prestamo prestamo;

    private boolean leida;   // === NUEVO ===


    public Notificacion(int idNotificacion,
                        String mensaje,
                        LocalDateTime fechaHora,
                        Usuario destinatario,
                        Ejemplar ejemplarRelacionado) {

        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
        this.destinatario = destinatario;
        this.ejemplarRelacionado = ejemplarRelacionado;
        this.leida = false;           // por defecto
    }

    // Getters y setters
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Ejemplar getEjemplarRelacionado() {
        return ejemplarRelacionado;
    }

    public void setEjemplarRelacionado(Ejemplar ejemplarRelacionado) {
        this.ejemplarRelacionado = ejemplarRelacionado;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    // ===== Nuevo comportamiento =====
    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    // === Representación ===
    @Override
    public String toString() {
        return "Notificacion{" +
                "mensaje='" + mensaje + '\'' +
                ", fechaHora=" + fechaHora +
                ", destinatario=" + (destinatario != null ? destinatario.getNombreCompleto() : "Sin destinatario") +
                ", ejemplarRelacionado=" + (ejemplarRelacionado != null ? ejemplarRelacionado.getCodigo() : "Sin ejemplar") +
                ", leida=" + leida +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificacion)) return false;
        Notificacion that = (Notificacion) o;
        return idNotificacion == that.idNotificacion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNotificacion);
    }
}


