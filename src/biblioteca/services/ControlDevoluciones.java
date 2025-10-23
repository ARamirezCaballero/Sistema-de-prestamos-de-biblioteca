package biblioteca.services;

import biblioteca.entities.inventario.Ejemplar;
import biblioteca.entities.prestamos.Devolucion;
import biblioteca.entities.prestamos.Prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlDevoluciones {

    private List<Prestamo> prestamos;
    private List<Devolucion> devoluciones;
    private static final double MULTA_POR_DIA = 50.0;

    public ControlDevoluciones() {
        prestamos = new ArrayList<>();
        devoluciones = new ArrayList<>();
    }

    public boolean validarPrestamo(int idPrestamo) {
        for (Prestamo p : prestamos) {
            if (p.getId() == idPrestamo) {
                p.actualizarEstado();
                return p.getEstado().equalsIgnoreCase("Activo") || p.getEstado().equalsIgnoreCase("Vencido");
            }
        }
        return false;
    }

    public double calcularMulta(Prestamo prestamo) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), hoy);
            return diasAtraso * MULTA_POR_DIA;
        }
        return 0.0;
    }

    public void liberarEjemplar(Prestamo prestamo) {
        Ejemplar ejemplar = prestamo.getEjemplar();
        if (ejemplar != null) ejemplar.marcarComoDisponible();
    }

    public Devolucion registrarDevolucion(int idPrestamo, String estadoEjemplar, String observaciones) throws Exception {
        Prestamo prestamo = prestamos.stream()
                .filter(p -> p.getId() == idPrestamo)
                .findFirst()
                .orElseThrow(() -> new Exception("Préstamo no encontrado."));

        if (!validarPrestamo(idPrestamo)) throw new Exception("El préstamo no está activo o ya fue devuelto.");

        double multa = calcularMulta(prestamo);

        Devolucion devolucion = new Devolucion(
                devoluciones.size() + 1,
                LocalDate.now(),
                estadoEjemplar,
                observaciones,
                prestamo,
                multa
        );

        devoluciones.add(devolucion);

        // Actualiza estado y libera ejemplar
        prestamo.marcarComoDevuelto();
        liberarEjemplar(prestamo);

        return devolucion;
    }

    public void agregarPrestamo(Prestamo prestamo) { if (prestamo != null) prestamos.add(prestamo); }

    public List<Prestamo> getPrestamos() { return prestamos; }
    public List<Devolucion> getDevoluciones() { return devoluciones; }
}


