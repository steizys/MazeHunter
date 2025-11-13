package src;

import java.time.Duration;
import java.time.Instant;

/**
 * Almacena estadísticas de una partida del juego
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno

 * @version 1.0
 */
public class Estadistica {
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private int tamanoDeMatriz;
    private int cristalesRecolectados;
    private int puntosDeVida;
    private int trampasActivadas;
    private Duration tiempoJugado;

    /**
     * Constructor vacío para Jackson
     */
    public Estadistica() {
    }

    /**
     * Crea nueva estadística con todos los datos de partida
     * @param tiempoInicial Inicio de partida
     * @param tiempoFinal Fin de partida
     * @param tamanoDeMatriz Tamaño del laberinto
     * @param cristalesRecolectados Cristales recogidos
     * @param puntosDeVida Vida restante
     * @param trampasActivadas Trampas activadas
     * @param tiempoJugado Tiempo real jugado
     */
    public Estadistica(Instant tiempoInicial, Instant tiempoFinal, int tamanoDeMatriz,
                       int cristalesRecolectados, int puntosDeVida, int trampasActivadas,
                       Duration tiempoJugado) {
        this.tiempoInicio = tiempoInicial;
        this.tiempoFinal = tiempoFinal;
        this.tamanoDeMatriz = tamanoDeMatriz;
        this.cristalesRecolectados = cristalesRecolectados;
        this.puntosDeVida = puntosDeVida;
        this.trampasActivadas = trampasActivadas;
        this.tiempoJugado = tiempoJugado;
    }

    /**
     * Muestra las estadísticas en consola
     */
    public void mostrarEstadistica(){
        System.out.println("---- ESTADISTICAS ----");
        if (tiempoJugado != null) {
            long horas = tiempoJugado.toHours();
            long minutos = tiempoJugado.toMinutes() % 60;
            long segundos = tiempoJugado.getSeconds() % 60;
            System.out.println("- Tiempo: " + horas + "h " + minutos + "m " + segundos + "s");
        } else if (tiempoInicio != null && tiempoFinal != null) {
            Duration duracion = Duration.between(tiempoInicio, tiempoFinal);
            System.out.println("- Tiempo: " +
                    duracion.toHours() + "h " +
                    (duracion.toMinutes() % 60) + "m " +
                    (duracion.getSeconds() % 60) + "s");
        } else {
            System.out.println("- Tiempo: No disponible");
        }

        System.out.println("- Tamanio: " + this.tamanoDeMatriz);
        System.out.println("- Cristales recolectados: " + this.cristalesRecolectados);
        System.out.println("- Puntos de vida: " + this.puntosDeVida);
        System.out.println("- Trampas activadas: " + this.trampasActivadas);
    };

    /**
     * Getters and Setters
     */
    public Duration getTiempoJugado() {
        return tiempoJugado;
    }

    public void setTiempoJugado(Duration tiempoJugado) {
        this.tiempoJugado = tiempoJugado;
    }

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