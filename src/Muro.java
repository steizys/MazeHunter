package src;
/**
 * Representa la celda muro dentro del laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Muro extends Celda {
    /**Constructor - crea celda de muro*/
    public Muro() {
        super("#", false, false);
    }

    public  void comportamiento(Jugador jugador){};

}
