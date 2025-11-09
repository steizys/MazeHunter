package src;

public class Jugador implements Movimiento {
    private int puntosDeVida;
    private static final int maxVida = 100;
    private String representacion;
    private boolean obtuvoLlave;
    private Posicion posicion;
    private int cristalesRecolectados ;
    private int trampasActivadas ;

    public Jugador(int puntosDeVida, String representacion, boolean obtuvoLlave, Posicion posicion, int cristalesRecolectados, int trampasActivadas) {
        this.puntosDeVida = puntosDeVida;
        this.representacion = representacion;
        this.obtuvoLlave = obtuvoLlave;
        this.posicion = posicion;
        this.cristalesRecolectados = cristalesRecolectados;
        this.trampasActivadas = trampasActivadas;
    }

    public  void moverseArriba(Jugador jugador, Laberinto laberinto){};
    public  void moverseAbajo(Jugador jugador, Laberinto laberinto){};
    public  void moverseDerecha(Jugador jugador, Laberinto laberinto){};
    public  void moverseIzquierda(Jugador jugador, Laberinto laberinto){};

    public boolean sigueVivo(){
        if (this.puntosDeVida<=0){
            return false;
        }else{
            return true;
        }
    };

    public  void sumarVida(int cantidad){
        this.puntosDeVida+=cantidad;
    };

    public  void restarVida(int cantidad){
        this.puntosDeVida-=cantidad;
    };

}
