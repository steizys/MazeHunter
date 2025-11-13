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
    /**Getters and Setters**/
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
