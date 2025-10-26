package biblioteca.entities.usuarios;

import biblioteca.entities.prestamos.Prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Socio extends Usuario {
    private int numeroSocio;
    private LocalDate fechaVencimientoCarnet;
    private String estado;
    private boolean tieneSanciones;
    private boolean tieneAtrasos;
    private List<Prestamo> prestamos;

    // Constructor simplificado para pruebas o carga simulada
    public Socio(int id, String nombre, String apellido, String dni, String email, String telefono,
                 LocalDate fechaAlta, TipoUsuario tipo, String usuario, String contrasenia,
                 int numeroSocio, LocalDate fechaVencimientoCarnet, String estado,
                 boolean tieneSanciones, boolean tieneAtrasos) {
        super(id, nombre, apellido, dni, email, telefono, fechaAlta, tipo, usuario, contrasenia);
        this.numeroSocio = numeroSocio;
        this.fechaVencimientoCarnet = fechaVencimientoCarnet;
        this.estado = estado;
        this.tieneSanciones = tieneSanciones;
        this.tieneAtrasos = tieneAtrasos;
        this.prestamos = new ArrayList<>();
    }



    public void renovarCarnet(int mesesExtra) {
        if (mesesExtra <= 0) {
            throw new IllegalArgumentException("El número de meses debe ser mayor a cero.");
        }
        this.fechaVencimientoCarnet = this.fechaVencimientoCarnet.plusMonths(mesesExtra);
    }


    public boolean verificarVigencia() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isAfter(fechaVencimientoCarnet);
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        if (prestamos == null || prestamos.isEmpty()) {
            return activos;
        }
        for (Prestamo p : prestamos) {
            if (p != null && p.getEstado() != null && !p.getEstado().equalsIgnoreCase("Devuelto")) {
                activos.add(p);
            }
        }
        return activos;
    }

    public boolean verificarHabilitacion() {
        if (estado == null) return false;
        boolean carnetVigente = verificarVigencia();
        return carnetVigente && !tieneSanciones && !tieneAtrasos && estado.equalsIgnoreCase("Activo");
    }

    public void suspender() {
        this.estado = "Suspendido";
        System.out.println("El socio ha sido suspendido.");
    }

    public void activar() {
        this.estado = "Activo";
        this.tieneSanciones = false;
        this.tieneAtrasos = false;
        System.out.println("El socio ha sido reactivado.");
    }

    @Override
    public String getTipo() {
        return "Socio";
    }

    public int getNumeroSocio() {
        return numeroSocio;
    }

    public LocalDate getFechaVencimientoCarnet() {
        return fechaVencimientoCarnet;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isTieneSanciones() {
        return tieneSanciones;
    }

    public boolean isTieneAtrasos() {
        return tieneAtrasos;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void agregarPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo.");
        }
        for (Prestamo p : prestamos) {
            if (p.getEjemplar().getCodigo().equals(prestamo.getEjemplar().getCodigo())) {
                throw new IllegalStateException("El ejemplar ya fue prestado a este socio.");
            }
        }
        prestamos.add(prestamo);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | N° Socio: " + numeroSocio +
                " | Estado: " + estado +
                " | Vence: " + fechaVencimientoCarnet;
    }

}