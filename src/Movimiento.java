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
    public  void moverseArriba(Jugador jugador, Laberinto laberinto);
    public  void moverseAbajo(Jugador jugador, Laberinto laberinto);
    public  void moverseDerecha(Jugador jugador, Laberinto laberinto);
    public  void moverseIzquierda(Jugador jugador, Laberinto laberinto);


}
