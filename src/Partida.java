package src;

import java.time.Instant;
import java.time.Duration;

public class Partida {
    private Laberinto laberinto;
    private Jugador jugador;
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private Estadistica estadistica;
    private Duration tiempoAcumulado;
    private boolean partidaActiva;

    public Partida() {
        this.tiempoAcumulado = Duration.ZERO;
        this.partidaActiva = false;
    }

    public Partida(Laberinto laberinto, Jugador jugador, Instant tiempoInicio, Instant tiempoFinal, Estadistica estadistica) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFinal = tiempoFinal;
        this.estadistica = estadistica;
        this.tiempoAcumulado = Duration.ZERO;
        this.partidaActiva = (tiempoInicio != null);
    }

    // Método para INICIAR la partida
    public void iniciarPartida() {
        if (!partidaActiva) {
            this.tiempoInicio = Instant.now();
            this.tiempoFinal = null;
            this.tiempoAcumulado = Duration.ZERO;
            this.partidaActiva = true;
        }
    }

    // Método para pausar el tiempo
    public void pausarTiempo() {
        if (partidaActiva && tiempoInicio != null) {
            Duration tiempoTranscurrido = Duration.between(tiempoInicio, Instant.now());
            tiempoAcumulado = tiempoAcumulado.plus(tiempoTranscurrido);
            tiempoInicio = null;
        }
    }

    // Método para reanudar el tiempo
    public void reanudarTiempo() {
        if (partidaActiva && tiempoInicio == null) {
            tiempoInicio = Instant.now();
        }
    }

    // Método para obtener el tiempo total transcurrido
    public Duration obtenerTiempoTranscurrido() {
        Duration tiempoTotal = tiempoAcumulado;
        if (tiempoInicio != null) {
            tiempoTotal = tiempoTotal.plus(Duration.between(tiempoInicio, Instant.now()));
        }
        return tiempoTotal;
    }

    // Método para finalizar la partida CORREGIDO
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

    // Método para verificar si la partida está activa
    public boolean isPartidaActiva() {
        return partidaActiva;
    }

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
