package biblioteca.entities.reportes;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.inventario.Ejemplar;
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
        this.idPrestamo = (prestamo != null ? prestamo.getId() : -1);
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

            Socio socio = prestamo.obtenerSocio();
            if (socio != null) {
                sb.append("Socio: ").append(socio.getNombreCompleto())
                        .append(" | Email: ").append(socio.getEmail()).append("\n");
            } else {
                sb.append("Socio: No disponible\n");
            }

            Ejemplar ejemplar = prestamo.obtenerEjemplar();
            if (ejemplar != null) {
                sb.append("Ejemplar: ").append(ejemplar.getCodigo()).append("\n");
            } else {
                sb.append("Ejemplar: No asignado\n");
            }

            sb.append("Estado actual: ").append(prestamo.getEstado()).append("\n");
            sb.append("Fecha vencimiento: ").append(prestamo.getFechaVencimiento()).append("\n");
        } else {
            sb.append("No hay información del préstamo asociada a este comprobante.\n");
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

