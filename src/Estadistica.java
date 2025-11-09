package src;

import java.time.Duration;
import java.time.Instant;

public class Estadistica {
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private int tamanoDeMatriz;
    private int cristalesRecolectados;
    private int puntosDeVida;
    private int trampasActivadas;
    public Estadistica() {
    }
    public Estadistica(Instant tiempoInicial, Instant tiempoFinal, int tamanoDeMatriz, int cristalesRecolectados, int puntosDeVida, int trampasActivadas) {
        this.tiempoInicio = tiempoInicial;
        this.tiempoFinal = tiempoFinal;
        this.tamanoDeMatriz = tamanoDeMatriz;
        this.cristalesRecolectados = cristalesRecolectados;
        this.puntosDeVida = puntosDeVida;
        this.trampasActivadas = trampasActivadas;
    }

    public void mostrarEstadistica(){
        System.out.println("---- ESTADISTICAS ----");
        System.out.println("- Tiempo: " +
        Duration.between(tiempoInicio, tiempoFinal).toHours() + "h " +
        (Duration.between(tiempoInicio, tiempoFinal).toMinutes() % 60) + "m " +
        (Duration.between(tiempoInicio, tiempoFinal).getSeconds() % 60) + "s");
        System.out.println("- Tamanio: " + this.tamanoDeMatriz);
        System.out.println("- Cristales recolectados: " + this.cristalesRecolectados);
        System.out.println("- Puntos de vida: " + this.puntosDeVida);
        System.out.println("- Trampas activadas: " + this.trampasActivadas);
    };

    public Instant getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(Instant tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public Instant getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(Instant tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
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
