package src;
/**
 * Representa la posicion del jugador dentro del laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
        */
public class Posicion {
    private int x;
    private int y;
    public Posicion() {
    }
    /**Constructor - crea la posicion del jugador*/
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene la coordenada X (fila) de la posición.
     *
     * @return Coordenada X que representa la fila en la matriz del laberinto
     */
    public int getX() {
        return x;
    }

    /**
     * Establece la coordenada X (fila) de la posición.
     *
     * @param x Nueva coordenada X que representa la fila en la matriz del laberinto
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Obtiene la coordenada Y (columna) de la posición.
     *
     * @return Coordenada Y que representa la columna en la matriz del laberinto
     */
    public int getY() {
        return y;
    }

    /**
     * Establece la coordenada Y (columna) de la posición.
     *
     * @param y Nueva coordenada Y que representa la columna en la matriz del laberinto
     */
    public void setY(int y) {
        this.y = y;
    }
}