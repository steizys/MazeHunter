package src;

import java.time.Instant;

public class Partida {
    private Laberinto laberinto;
    private Jugador jugador;
    private Instant tiempoInicio;
    private Instant tiempoFinal;
    private Estadistica estadistica;

    public Partida(Laberinto laberinto, Jugador jugador, Instant tiempoInicio, Instant tiempoFinal, Estadistica estadistica) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFinal = tiempoFinal;
        this.estadistica = estadistica;
    }

    public Estadistica mostrarEstadistica(){
        return estadistica;
    };

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
}
