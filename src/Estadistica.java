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
        String RESET = "\u001B[0m";
        String VERDE = "\u001B[32m";
        String AZUL = "\u001B[34m";
        String AMARILLO = "\u001B[33m";
        String ROJO = "\u001B[31m";
        String MORADO = "\u001B[35m";
        String CIAN = "\u001B[36m";
        String NEGRITA = "\u001B[1m";

        System.out.println(NEGRITA + AMARILLO + "╔═══════════════════════════════╗");
        System.out.println("║          ESTADÍSTICAS         ║");
        System.out.println("╠═══════════════════════════════╣" + RESET);
        String mostrarTiempo = "No disponible";
        if (tiempoJugado != null) {
            long horas = tiempoJugado.toHours();
            long minutos = tiempoJugado.toMinutes() % 60;
            long segundos = tiempoJugado.getSeconds() % 60;
            mostrarTiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        } else if (tiempoInicio != null && tiempoFinal != null) {
            Duration duracion = Duration.between(tiempoInicio, tiempoFinal);
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            long segundos = duracion.getSeconds() % 60;
            mostrarTiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }

        System.out.printf(NEGRITA + AMARILLO     + "║ " + RESET + "Tiempo: " + RESET + "%-30s " + AMARILLO    + "║\n", AMARILLO + mostrarTiempo + RESET);
        System.out.printf(AMARILLO   + "║ " + RESET + "Tamaño del laberinto: " + RESET + "%-16s " + AMARILLO  + "║\n", VERDE + this.tamanoDeMatriz + "x" + this.tamanoDeMatriz + RESET);
        System.out.printf(AMARILLO   + "║ " + RESET + "Cristales recolectados: " + RESET + "%-14s " + AMARILLO    + "║\n", AMARILLO + this.cristalesRecolectados + RESET);
        String colorVida = this.puntosDeVida > 50 ? VERDE : this.puntosDeVida > 25 ? AMARILLO : ROJO;
        System.out.printf(AMARILLO   + "║ " + RESET + "Puntos de vida: " + RESET + "%-22s " + AMARILLO    + "║\n", colorVida + this.puntosDeVida + "/100" + RESET);
        System.out.printf(AMARILLO   + "║ " + RESET + "Trampas activadas: " + RESET + "%-19s " + AMARILLO     + "║\n", ROJO + this.trampasActivadas + RESET);
        System.out.println(AMARILLO  + NEGRITA + "╚═══════════════════════════════╝" + RESET);
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