package src;

public class Jugador implements Movimiento {
    private int puntosDeVida;
    private static final int maxVida = 100;
    private String representacion;
    private boolean obtuvoLlave;
    private Posicion posicion;
    private int cristalesRecolectados ;
    private int trampasActivadas ;

    public Jugador() {
        this.puntosDeVida = 100;
        this.representacion = "J";
        this.obtuvoLlave = false;
        this.cristalesRecolectados = 0;
        this.trampasActivadas = 0;
    }

    public Jugador(Posicion posicion) {
        this();
        this.posicion = posicion;
    }

    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    public void setPuntosDeVida(int puntosDeVida) {
        this.puntosDeVida = puntosDeVida;
    }

    public String getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }

    public boolean isObtuvoLlave() {
        return obtuvoLlave;
    }

    public void setObtuvoLlave(boolean obtuvoLlave) {
        this.obtuvoLlave = obtuvoLlave;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    public  void moverseArriba(Jugador jugador, Laberinto laberinto){
        Celda celda=laberinto.getMatrizJuegoPosicion(this.posicion.getX()-1, this.posicion.getY());
        if (celda.isTransitable()){
            celda.comportamiento(jugador);
            Jugador.this.posicion.setX(this.posicion.getX()-1);
        }else{
            System.out.println("❌ No puede mover porque es un MURO");
        }
    };

    public  void moverseAbajo(Jugador jugador, Laberinto laberinto){
        Celda celda=laberinto.getMatrizJuegoPosicion(this.posicion.getX()+1, this.posicion.getY());
        if (celda.isTransitable()){
            celda.comportamiento(jugador);
            Jugador.this.posicion.setX(this.posicion.getX()+1);
        }else{
            System.out.println("❌ No puede mover porque es un MURO");
        }
    };

    public  void moverseDerecha(Jugador jugador, Laberinto laberinto){
        Celda celda=laberinto.getMatrizJuegoPosicion(this.posicion.getX(), this.posicion.getY()+1);
        if (celda.isTransitable()){
            celda.comportamiento(jugador);
            Jugador.this.posicion.setY(this.posicion.getY()+1);
        }else{
            System.out.println("❌ No puede mover porque es un MURO");
        }
    };

    public  void moverseIzquierda(Jugador jugador, Laberinto laberinto){
        Celda celda=laberinto.getMatrizJuegoPosicion(this.posicion.getX(), this.posicion.getY()-1);
        if (celda.isTransitable()){
            celda.comportamiento(jugador);
            Jugador.this.posicion.setY(this.posicion.getY()-1);
        }else{
            System.out.println("❌ No puede mover porque es un MURO");
        }
    };

    public boolean sigueVivo(){
        if (this.puntosDeVida<=0){
            return false;
        }else{
            return true;
        }
    };

}
