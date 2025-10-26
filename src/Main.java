import biblioteca.data.BaseDatosSimulada;
import biblioteca.services.ControlLibros;
import biblioteca.ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        // Crear el control de libros
        ControlLibros controlLibros = new ControlLibros();
        BaseDatosSimulada.inicializarDatos();

        // Pasar el control de libros al menú principal
        MenuPrincipal menu = new MenuPrincipal(controlLibros);

        // Iniciar la aplicación
        menu.iniciar();
    }
}