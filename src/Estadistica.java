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
     * Obtiene el tiempo total jugado en la partida
     * @return Duración del tiempo jugado
     */
    public Duration getTiempoJugado() {
        return tiempoJugado;
    }

    /**
     * Establece el tiempo total jugado en la partida
     * @param tiempoJugado Duración del tiempo jugado
     */
    public void setTiempoJugado(Duration tiempoJugado) {
        this.tiempoJugado = tiempoJugado;
    }

    /**
     * Obtiene el momento de inicio de la partida
     * @return Instant representando el inicio
     */
    public Instant getTiempoInicio() {
        return tiempoInicio;
    }

    /**
     * Establece el momento de inicio de la partida
     * @param tiempoInicio Instant representando el inicio
     */
    public void setTiempoInicio(Instant tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    /**
     * Obtiene el momento de finalización de la partida
     * @return Instant representando el final
     */
    public Instant getTiempoFinal() {
        return tiempoFinal;
    }

    /**
     * Establece el momento de finalización de la partida
     * @param tiempoFinal Instant representando el final
     */
    public void setTiempoFinal(Instant tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    /**
     * Obtiene el número de trampas activadas durante la partida
     * @return Cantidad de trampas activadas
     */
    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    /**
     * Establece el número de trampas activadas durante la partida
     * @param trampasActivadas Cantidad de trampas activadas
     */
    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    /**
     * Obtiene el tamaño de la matriz del laberinto
     * @return Tamaño de la matriz (n x n)
     */
    public int getTamanoDeMatriz() {
        return tamanoDeMatriz;
    }

    /**
     * Establece el tamaño de la matriz del laberinto
     * @param tamanoDeMatriz Tamaño de la matriz (n x n)
     */
    public void setTamanoDeMatriz(int tamanoDeMatriz) {
        this.tamanoDeMatriz = tamanoDeMatriz;
    }

    /**
     * Obtiene la cantidad de cristales recolectados
     * @return Número de cristales recolectados
     */
    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    /**
     * Establece la cantidad de cristales recolectados
     * @param cristalesRecolectados Número de cristales recolectados
     */
    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    /**
     * Obtiene los puntos de vida restantes al finalizar la partida
     * @return Puntos de vida restantes
     */
    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    /**
     * Establece los puntos de vida restantes al finalizar la partida
     * @param puntosDeVida Puntos de vida restantes
     */
    public void setPuntosDeVida(int puntosDeVida) {
        this.puntosDeVida = puntosDeVida;
    }
}