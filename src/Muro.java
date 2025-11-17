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

    /**
     * Implementa el comportamiento cuando el jugador entra en esta celda.
     *
     * <p>Para el muro, no se permite el movimiento y no se aplica ningún efecto
     * especial al jugador, ya que el movimiento es bloqueado antes de llegar
     * a ejecutar este método.</p>
     *
     * @param jugador El jugador que intenta entrar en la celda de muro
     * @see Jugador
     */
    public void comportamiento(Jugador jugador) {
    }
}
