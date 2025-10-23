package biblioteca.entities.reportes;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.usuarios.Socio;
import biblioteca.entities.inventario.Ejemplar;

import java.time.LocalDate;
import java.util.Objects;

public class Comprobante {

    private final int id;
    private final LocalDate fechaEmision;
    private final String tipo;
    private String contenido;
    private final Prestamo prestamo;

    public Comprobante(int id, String tipo, Prestamo prestamo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo de comprobante no puede ser nulo o vacío.");
        }
        this.id = id;
        this.tipo = tipo;
        this.prestamo = prestamo;
        this.fechaEmision = LocalDate.now();
        this.contenido = "";
    }

    public void generar() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== COMPROBANTE DE ").append(tipo.toUpperCase()).append(" =====\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Fecha de emisión: ").append(fechaEmision).append("\n\n");

        if (prestamo != null) {
            sb.append("Préstamo N°: ").append(prestamo.getId()).append("\n");
            Socio socio = prestamo.getSocio();
            Ejemplar ejemplar = prestamo.getEjemplar();

            sb.append("Socio: ").append(socio != null ? socio.getNombreCompleto() + " | Email: " + socio.getEmail() : "No disponible").append("\n");
            sb.append("Ejemplar: ").append(ejemplar != null ? ejemplar.getCodigo() : "No asignado").append("\n");
            sb.append("Estado préstamo: ").append(prestamo.getEstado()).append("\n");
            sb.append("Fecha vencimiento: ").append(prestamo.getFechaVencimiento()).append("\n");
        } else {
            sb.append("No hay información de préstamo.\n");
        }

        sb.append("\nGracias por utilizar el sistema de Biblioteca.\n");
        this.contenido = sb.toString();
    }

    public void imprimir() {
        System.out.println(contenido.isBlank() ? "Comprobante no generado aún." : contenido);
    }

    public String prepararEmail() {
        if (prestamo == null || prestamo.getSocio() == null) {
            return "No se pudo enviar el comprobante: falta información del socio.";
        }
        return "Enviando a: " + prestamo.getSocio().getEmail() + "\nAsunto: Comprobante de " + tipo + "\nContenido:\n" + contenido;
    }

    // Getters
    public int getId() { return id; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public String getTipo() { return tipo; }
    public Prestamo getPrestamo() { return prestamo; }
    public String getContenido() { return contenido; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comprobante)) return false;
        Comprobante that = (Comprobante) o;
        return id == that.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Comprobante #" + id + " (" + tipo + ") - Emitido: " + fechaEmision;
    }
}




