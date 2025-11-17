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

    /**
     * Implementa el comportamiento cuando el jugador entra en esta celda.
     *
     * @param jugador El jugador que entra en la celda
     * @see Jugador
     */
    public  void comportamiento(Jugador jugador){};
}
