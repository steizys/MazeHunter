package src;

/**
 * Celda que otorga vida extra al jugador.
 * Simbolo: "+", Transitable: true
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 * @see Celda
 * @see Jugador
 */
public class VidaExtra extends Celda {

    /**
     * Constructor - inicializa con simbolo "+"
     */
    public VidaExtra() {
        super("+", false, true);
    }

    /**
     * Añade 5 puntos de vida al jugador
     * @param jugador Jugador que recibe la vida extra
     */
    public void comportamiento(Jugador jugador) {
        jugador.setPuntosDeVida(jugador.getPuntosDeVida() + 5);
    }
}