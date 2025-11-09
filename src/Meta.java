package src;

public class Meta extends Celda {

    public Meta() {
        super("X", false, true);
    }

    public  void comportamiento(Jugador jugador){
        if (jugador.isObtuvoLlave()){
            System.out.println("HAS LLEGADO A LA META Y TIENES LA LLAVE");
            setVisitada(true);
        }else{
            System.out.println("CUIDADO! HAS LLEGADO A LA META PERO NO TIENES LA LLAVE");
        }
    };


}
