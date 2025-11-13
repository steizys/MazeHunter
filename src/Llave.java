package src;
/**
 * Representa la celda que posee la llave
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */

public class Llave extends Celda {
    /**Constructor - crea celda de llave*/

    public Llave() {
        super("L", false, true);
    }
    /**
     * Otorga la llave al jugador cuando pisa esta celda
     * Marca que el jugador obtuvo la llave
     *
     * @param jugador Jugador que obtiene la llave
     */
    public  void comportamiento(Jugador jugador){
        if (!isVisitada()) {
            setVisitada(true);
            jugador.setObtuvoLlave(true);
            System.out.println(" HAS OBTENIDO LA LLAVE");
        }
    };


}
