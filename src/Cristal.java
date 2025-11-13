package src;

/**
 * Representa un cristal recolectable en el laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */

public class Cristal extends Celda {

    /**
     * Constructor - crea celda de cristal
     */
    public Cristal() {
        super("C", false, true);
    }

    /**
     * Aumenta en 1 los cristales recolectados del jugador
     * @param jugador Jugador que recolecta el cristal
     */
    public void comportamiento(Jugador jugador) {
        if (!isVisitada()) {
            setVisitada(true);
            jugador.setCristalesRecolectados(jugador.getCristalesRecolectados() + 1);
        }
    };
}