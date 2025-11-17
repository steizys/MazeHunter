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

    /**
     * Implementa el comportamiento cuando el jugador entra en esta celda.
     *
     * <p>Para el camino libre, no se aplica ningún efecto especial al jugador,
     * permitiendo el movimiento libre a través de esta celda.</p>
     *
     * @param jugador El jugador que entra en la celda
     * @see Jugador
     */
    public void comportamiento(Jugador jugador) {
    }
}