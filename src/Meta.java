package src;
/**
 * Representa la celda meta del laberinto
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Meta extends Celda {
    /**Constructor - crea celda de meta*/
    public Meta() {
        super("X", false, true);
    }
    /**
     * Comprueba que el jugador tenga la llave para para marcar que el jugador
     * ha llegado a la meta y valida que la cantidad de cristales recolectados sea mayor igual a 1
     *
     * @param jugador Jugador que pasa por la meta
     */
    public  void comportamiento(Jugador jugador){
        if (jugador.isObtuvoLlave() && (jugador.getCristalesRecolectados()>=1)){
            System.out.println("HAS LLEGADO A LA META Y TIENES LA LLAVE");
            setVisitada(true);
        }else{
            System.out.println("CUIDADO! HAS LLEGADO A LA META PERO NO TIENES LA LLAVE");
        }
    };


}
