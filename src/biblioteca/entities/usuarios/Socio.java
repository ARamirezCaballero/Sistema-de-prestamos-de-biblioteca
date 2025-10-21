package biblioteca.entities.usuarios;

import biblioteca.entities.prestamos.Prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Socio extends Usuario {
    private int numeroSocio;
    private LocalDate fechaVencimientoCarnet;
    private String estado;
    private boolean tieneSanciones;
    private boolean tieneAtrasos;
    public List<Prestamo> prestamos;

    public Socio(int id, String nombre, String apellido, String dni, String email, String telefono, Date fecha, TipoUsuario tipoUsuario, String usuario, String contrasenia, int numeroSocio, LocalDate fechaVencimientoCarnet, String estado, boolean tieneSanciones, boolean tieneAtrasos) {

        super(id, nombre, apellido, dni, email, telefono, fecha, tipoUsuario, usuario, contrasenia);
        this.numeroSocio = numeroSocio;
        this.fechaVencimientoCarnet = fechaVencimientoCarnet;
        this.estado = estado;
        this.tieneSanciones = tieneSanciones;
        this.tieneAtrasos = tieneAtrasos;
        this.prestamos = new ArrayList<>();
    }

    public void renovarCarnet(int mesesExtra) {
        LocalDate fechaRenovada = this.fechaVencimientoCarnet.plusMonths(mesesExtra);
        this.fechaVencimientoCarnet = fechaRenovada;
        System.out.println("Carnet renovado hasta: "+ fechaRenovada);
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
        this.prestamos.add(prestamo);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | N° Socio: " + numeroSocio +
                " | Estado: " + estado +
                " | Vence: " + fechaVencimientoCarnet;
    }

}