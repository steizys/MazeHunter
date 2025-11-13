package src;

/**
 * Representa el inicio del laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */

public class Inicio extends Celda {
    /**
     * Constructor - crea celda de inicio
     */
    public Inicio() {
        super("S", false, true);
    }

    public  void comportamiento(Jugador jugador){};
}
