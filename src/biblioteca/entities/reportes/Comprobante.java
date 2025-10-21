package biblioteca.entities.reportes;

import biblioteca.entities.prestamos.Prestamo;
import java.time.LocalDate;

public class Comprobante {

    private int id;
    private LocalDate fechaEmision;
    private String tipo;
    private String contenido;
    private int idPrestamo;
    private Prestamo prestamo;

    public Comprobante(int id, String tipo, Prestamo prestamo) {
        this.id = id;
        this.tipo = tipo;
        this.prestamo = prestamo;
        this.idPrestamo = prestamo.getId();
        this.fechaEmision = LocalDate.now();
        this.contenido = "";
    }

    public void generar() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== COMPROBANTE DE ").append(tipo.toUpperCase()).append(" =====\n");
        sb.append("ID Comprobante: ").append(id).append("\n");
        sb.append("Fecha de emisión: ").append(fechaEmision).append("\n\n");

        if (prestamo != null) {
            sb.append("Préstamo N°: ").append(prestamo.getId()).append("\n");
            sb.append("Socio: ").append(prestamo.obtenerSocio().getNombreCompleto()).append("\n");
            sb.append("Ejemplar: ").append(prestamo.obtenerEjemplar().getCodigo()).append("\n");
            sb.append("Estado actual: ").append(prestamo.getEstado()).append("\n");
            sb.append("Fecha vencimiento: ").append(prestamo.getFechaVencimiento()).append("\n");
        }

        sb.append("\nGracias por utilizar el sistema de la Biblioteca.\n");
        this.contenido = sb.toString();
    }

    public void imprimir() {
        if (contenido == null || contenido.isEmpty()) {
            System.out.println("El comprobante aún no fue generado.");
        } else {
            System.out.println(contenido);
        }
    }

    //Simulación del envio del comprobante por email.
    //Sin conexión real, solo es un mensaje en consola
    public void enviarPorEmail() {
        if (prestamo != null && prestamo.obtenerSocio() != null) {
            String email = prestamo.obtenerSocio().getEmail();
            System.out.println("Enviando comprobante al correo: " + email);
            System.out.println("Asunto: Comprobante de " + tipo);
            System.out.println("Contenido:\n" + contenido);
        } else {
            System.out.println("No se pudo enviar el comprobante: falta información del socio.");
        }
    }

    public Prestamo obtenerPrestamo() {
        return prestamo;
    }

    public int getId() { return id; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public String getTipo() { return tipo; }
    public String getContenido() { return contenido; }
    public int getIdPrestamo() { return idPrestamo; }

    @Override
    public String toString() {
        return "Comprobante #" + id + " (" + tipo + ") - Emitido el " + fechaEmision;
    }

}
