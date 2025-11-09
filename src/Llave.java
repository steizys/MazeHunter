package src;

public class Llave extends Celda {
    public Llave() {
        super("L", false, true);
    }

    public  void comportamiento(Jugador jugador){
        jugador.setObtuvoLlave(true);
        System.out.println(" HAS OBTENIDO LA LLAVE");
    };


}
