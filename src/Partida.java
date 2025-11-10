package src;

import java.time.Instant;
import java.time.Duration;

public class Partida {
    private Laberinto laberinto;
    private Jugador jugador;
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private Estadistica estadistica;

    private Duration tiempoAcumulado; // ✅ NUEVO: Tiempo acumulado cuando se pausa

    public Partida() {
        this.tiempoAcumulado = Duration.ZERO;
    }

    public Partida(Laberinto laberinto, Jugador jugador, Instant tiempoInicio, Instant tiempoFinal, Estadistica estadistica) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFinal = tiempoFinal;
        this.estadistica = estadistica;
        this.tiempoAcumulado = Duration.ZERO;
    }

    // Método para pausar el tiempo
    public void pausarTiempo() {
        if (tiempoInicio != null && tiempoAcumulado != null) {
            Duration tiempoTranscurrido = Duration.between(tiempoInicio, Instant.now());
            tiempoAcumulado = tiempoAcumulado.plus(tiempoTranscurrido);
            tiempoInicio = null; // Detener el contador
        }
    }

    // Método para reanudar el tiempo
    public void reanudarTiempo() {
        if (tiempoInicio == null && tiempoAcumulado != null) {
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

    // Método para finalizar la partida y obtener tiempo final
    public Duration finalizarPartida() {
        if (tiempoInicio != null) {
            pausarTiempo(); // Pausar antes de finalizar
        }
        return tiempoAcumulado;
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
