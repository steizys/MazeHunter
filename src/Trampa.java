package src;
/**
 * Representa la celda trampa dentro del laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Trampa extends Celda {
    /**Constructor - crea celda de trampa*/
    public Trampa() {
        super("T", false, true);
    }
    /**
     * Procedimiento que define el efecto o efecto que una trampa
     * tiene sobre el jugador al interactuar con ella dentro del laberinto.
     * <p>
     * <ul>
     * <li>Resta 5 puntos de vida al {@link Jugador}.</li>
     * <li>Incrementa el contador de trampas activadas del {@link Jugador} en 1.</li>
     * </ul>
     *
     * @param jugador El objeto {@link Jugador} afectado por este comportamiento.
     */
    public  void comportamiento(Jugador jugador){
        if (!isVisitada()){
            setVisitada(true);
            jugador.setPuntosDeVida(jugador.getPuntosDeVida() - 5);
            jugador.setTrampasActivadas(jugador.getTrampasActivadas() + 1);
        }

    };
}
