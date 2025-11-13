package src;

/**
 * Representa un camino libre transitable en el laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class CaminoLibre extends Celda {

    /**
     * Constructor - crea celda de camino libre
     */
    public CaminoLibre() {
        super(".", false, true);
    }

    public void comportamiento(Jugador jugador){};
}