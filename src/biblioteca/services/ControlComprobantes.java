package biblioteca.services;

import biblioteca.entities.prestamos.Prestamo;
import biblioteca.entities.reportes.Comprobante;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControlComprobantes {

    private final List<Comprobante> comprobantes = new ArrayList<>();
    private int contadorIds = 1;

    public Comprobante generarComprobante(String tipo, Prestamo prestamo) {
        if (prestamo == null) throw new IllegalArgumentException("Préstamo no válido.");
        Comprobante c = new Comprobante(contadorIds++, tipo, prestamo);
        c.generar();
        comprobantes.add(c);
        return c;
    }

    public void emitirComprobante(Comprobante comprobante) {
        if (comprobante == null) throw new IllegalArgumentException("Comprobante nulo.");
        System.out.println("=== EMITIENDO COMPROBANTE ===");
        comprobante.imprimir();
    }

    public void imprimirComprobante(Comprobante comprobante) { emitirComprobante(comprobante); }

    public void enviarComprobanteEmail(Comprobante comprobante) {
        if (comprobante == null) throw new IllegalArgumentException("Comprobante nulo.");
        System.out.println("=== ENVÍO POR EMAIL ===");
        System.out.println(comprobante.prepararEmail());
    }

    public Comprobante buscarComprobantePorId(int id) {
        Optional<Comprobante> resultado = comprobantes.stream().filter(c -> c.getId() == id).findFirst();
        return resultado.orElse(null);
    }

    public boolean eliminarComprobante(int id) { return comprobantes.removeIf(c -> c.getId() == id); }

    public List<Comprobante> getComprobantes() { return new ArrayList<>(comprobantes); }
}


