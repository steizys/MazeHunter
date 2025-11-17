package src;
/**
 * La interfaz {@code Movimiento} define los metodos básicas de movimiento
 * que un jugador puede realizar dentro de un laberinto.
 *
 * Esta interfaz es implementada por la clase Movimiento que maneja como el jugador
 * se desplaza sobre el laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public interface Movimiento {
    /**
     * Mueve al jugador hacia arriba en el laberinto (disminuye coordenada X).
     *
     * <p>El movimiento se realiza solo si la celda destino es transitable.</p>
     *
     * @param jugador El jugador que se está moviendo
     * @param laberinto El laberinto donde se realiza el movimiento
     */
    public void moverseArriba(Jugador jugador, Laberinto laberinto);

    /**
     * Mueve al jugador hacia abajo en el laberinto (aumenta coordenada X).
     *
     * <p>El movimiento se realiza solo si la celda destino es transitable.</p>
     *
     * @param jugador El jugador que se está moviendo
     * @param laberinto El laberinto donde se realiza el movimiento
     */
    public void moverseAbajo(Jugador jugador, Laberinto laberinto);

    /**
     * Mueve al jugador hacia la derecha en el laberinto (aumenta coordenada Y).
     *
     * <p>El movimiento se realiza solo si la celda destino es transitable.</p>
     *
     * @param jugador El jugador que se está moviendo
     * @param laberinto El laberinto donde se realiza el movimiento
     */
    public void moverseDerecha(Jugador jugador, Laberinto laberinto);

    /**
     * Mueve al jugador hacia la izquierda en el laberinto (disminuye coordenada Y).
     *
     * <p>El movimiento se realiza solo si la celda destino es transitable.</p>
     *
     * @param jugador El jugador que se está moviendo
     * @param laberinto El laberinto donde se realiza el movimiento
     */
    public void moverseIzquierda(Jugador jugador, Laberinto laberinto);
}