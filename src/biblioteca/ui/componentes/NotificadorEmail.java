package biblioteca.ui.componentes;

import biblioteca.entities.usuarios.Usuario;
import biblioteca.entities.prestamos.Prestamo;
import biblioteca.services.ControlNotificaciones;

import java.util.regex.Pattern;

/**
 * Simula el envío de notificaciones por email a los socios con préstamos próximos a vencer o vencidos.
 * Controlador asociado: ControlNotificaciones
 */
public class NotificadorEmail {

    private String servidor;
    private int puerto;
    private boolean conectado;
    private final ControlNotificaciones controlNotificaciones;

    public NotificadorEmail(String servidor, int puerto, ControlNotificaciones controlNotificaciones) {
        this.servidor = servidor;
        this.puerto = puerto;
        this.controlNotificaciones = controlNotificaciones;
        this.conectado = false;
    }

    /** Simula conexión al servidor de correo */
    public void conectar() {
        if (servidor == null || servidor.isBlank() || puerto <= 0) {
            System.out.println("Error: servidor o puerto inválido.");
            return;
        }
        conectado = true;
        System.out.println("Conectado al servidor de email " + servidor + ":" + puerto);
    }

    /** Simula envío de email */
    public void enviarEmail(String destinatario, String asunto, String mensaje) {
        if (!conectado) {
            System.out.println("No conectado al servidor. Conecte primero.");
            return;
        }
        if (!validarEmail(destinatario)) {
            System.out.println("Email inválido: " + destinatario);
            return;
        }

        // Simulación de envío
        System.out.println("Enviando email a " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
        System.out.println("Email enviado (simulado).");
    }

    /** Simula desconexión del servidor */
    public void desconectar() {
        if (conectado) {
            conectado = false;
            System.out.println("Desconectado del servidor de email.");
        } else {
            System.out.println("Ya estaba desconectado.");
        }
    }

    /** Valida sintaxis básica de un email */
    public boolean validarEmail(String email) {
        if (email == null) return false;
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }

    /**
     * Envía notificaciones de préstamos próximos a vencer o vencidos
     * recorriendo las notificaciones generadas por ControlNotificaciones.
     */
    public void enviarNotificacionPrestamo() {
        conectar();

        if (controlNotificaciones.getNotificaciones().isEmpty()) {
            System.out.println("No hay notificaciones pendientes para enviar.");
            desconectar();
            return;
        }

        for (var n : controlNotificaciones.getNotificaciones()) {
            Usuario socio = n.getDestinatario();
            if (socio != null && validarEmail(socio.getEmail())) {
                enviarEmail(
                        socio.getEmail(),
                        "Notificación de Biblioteca",
                        n.getMensaje()
                );
            } else {
                System.out.println("No se puede enviar notificación: email inválido o socio nulo.");
            }
        }

        desconectar();
    }

}
