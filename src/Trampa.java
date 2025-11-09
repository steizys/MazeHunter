package src;

public class Trampa extends Celda {
    public Trampa() {
        super("T", false, true);
    }

    public  void comportamiento(Jugador jugador){
        jugador.setPuntosDeVida(jugador.getPuntosDeVida() - 5);
        jugador.setTrampasActivadas(jugador.getTrampasActivadas() + 1);
    };
}
