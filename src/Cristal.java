package src;

public class Cristal extends Celda{

    public Cristal() {
        super("C", false, true);
    }

    public  void comportamiento(Jugador jugador){
        jugador.setCristalesRecolectados(jugador.getCristalesRecolectados() +1);

    };
}
