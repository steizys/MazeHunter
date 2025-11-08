package src;

import java.time.Instant;

public class Estadistica {
    private Instant tiempo;
    private int tamanoDeMatriz;
    private int cristalesRecolectados;
    private int puntosDeVida;

    public Estadistica(Instant tiempo, int tamanoDeMatriz, int cristalesRecolectados, int puntosDeVida) {
        this.tiempo = tiempo;
        this.tamanoDeMatriz = tamanoDeMatriz;
        this.cristalesRecolectados = cristalesRecolectados;
        this.puntosDeVida = puntosDeVida;
    }

    public void mostrarEstadistica(){};

    public Instant getTiempo() {
        return tiempo;
    }

    public void setTiempo(Instant tiempo) {
        this.tiempo = tiempo;
    }

    public int getTamanoDeMatriz() {
        return tamanoDeMatriz;
    }

    public void setTamanoDeMatriz(int tamanoDeMatriz) {
        this.tamanoDeMatriz = tamanoDeMatriz;
    }

    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    public void setPuntosDeVida(int puntosDeVida) {
        this.puntosDeVida = puntosDeVida;
    }
}
