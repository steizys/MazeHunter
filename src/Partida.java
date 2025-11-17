package src;

import java.time.Instant;
import java.time.Duration;

/**
 * Representa una partida del juego con su estado, temporizador y estadísticas.
 * Permite gestionar el tiempo de juego mediante pausas y reanudaciones,
 * así como controlar el estado activo/finalizado de la partida.
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Partida {
    private Laberinto laberinto;
    private Jugador jugador;
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private Estadistica estadistica;
    private Duration tiempoAcumulado;
    private boolean partidaActiva;

    /**
     * Constructor por defecto que inicializa una partida inactiva
     * con tiempo acumulado cero.
     */
    public Partida() {
        this.tiempoAcumulado = Duration.ZERO;
        this.partidaActiva = false;
    }

    /**
     * Constructor que inicializa una partida con parámetros específicos.
     *
     * @param laberinto    El laberinto asociado a la partida
     * @param jugador      El jugador que participa en la partida
     * @param tiempoInicio Momento de inicio de la partida (null si no activa)
     * @param tiempoFinal  Momento de finalización de la partida
     * @param estadistica  Estadísticas asociadas a la partida
     */
    public Partida(Laberinto laberinto, Jugador jugador, Instant tiempoInicio, Instant tiempoFinal, Estadistica estadistica) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFinal = tiempoFinal;
        this.estadistica = estadistica;
        this.tiempoAcumulado = Duration.ZERO;
        this.partidaActiva = (tiempoInicio != null);
    }

    /**
     * Inicia la partida estableciendo el tiempo de inicio y reiniciando
     * el tiempo acumulado. Solo tiene efecto si la partida no está activa.
     */
    public void iniciarPartida() {
        if (!partidaActiva) {
            this.tiempoInicio = Instant.now();
            this.tiempoFinal = null;
            this.tiempoAcumulado = Duration.ZERO;
            this.partidaActiva = true;
        }
    }

    /**
     * Pausa el temporizador de la partida si está activa.
     * El tiempo transcurrido se acumula para posibles reanudaciones.
     */
    public void pausarTiempo() {
        if (partidaActiva && tiempoInicio != null) {
            Duration tiempoTranscurrido = Duration.between(tiempoInicio, Instant.now());
            tiempoAcumulado = tiempoAcumulado.plus(tiempoTranscurrido);
            tiempoInicio = null;
        }
    }

    /**
     * Reanuda el temporizador de la partida si está pausada.
     * Establece un nuevo tiempo de inicio para continuar el conteo.
     */
    public void reanudarTiempo() {
        if (partidaActiva && tiempoInicio == null) {
            tiempoInicio = Instant.now();
        }
    }

    /**
     * Calcula el tiempo total transcurrido en la partida,
     * considerando tanto el tiempo acumulado como el tiempo actual si está activa.
     *
     * @return Duración total del tiempo de juego
     */
    public Duration obtenerTiempoTranscurrido() {
        Duration tiempoTotal = tiempoAcumulado;
        if (tiempoInicio != null) {
            tiempoTotal = tiempoTotal.plus(Duration.between(tiempoInicio, Instant.now()));
        }
        return tiempoTotal;
    }

    /**
     * Finaliza la partida calculando el tiempo final y actualizando el estado.
     *
     * @return Duración total acumulada de la partida
     */
    public Duration finalizarPartida() {
        if (partidaActiva) {
            if (tiempoInicio != null) {
                Duration tiempoTranscurrido = Duration.between(tiempoInicio, Instant.now());
                tiempoAcumulado = tiempoAcumulado.plus(tiempoTranscurrido);
            }
            this.tiempoFinal = Instant.now();
            this.partidaActiva = false;
            this.tiempoInicio = null;
        }
        return tiempoAcumulado;
    }

    /**
     * Verifica si la partida está actualmente activa.
     *
     * @return true si la partida está activa, false en caso contrario
     */
    public boolean isPartidaActiva() {
        return partidaActiva;
    }

    /**
     * Getters and Setters
     */
    public Laberinto getLaberinto() {
        return laberinto;
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
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

    public Estadistica getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(Estadistica estadistica) {
        this.estadistica = estadistica;
    }

    public Duration getTiempoAcumulado() {
        return tiempoAcumulado;
    }

    public void setTiempoAcumulado(Duration tiempoAcumulado) {
        this.tiempoAcumulado = tiempoAcumulado;
    }
}