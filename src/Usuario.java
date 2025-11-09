package src;

import java.util.ArrayList;

public class Usuario {
    private String correo;
    private String contrasenia;
    private Partida partida;
    private ArrayList<Estadistica> estadisticas;

    public Usuario(String correo, String contrasenia, Partida partida, ArrayList<Estadistica> estadisticas) {
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.partida = partida;
        this.estadisticas = estadisticas;
    }

    public void guardarPartida(Partida partida) {};
    public void guardarPartidaCompleta(Partida partida) {};

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public ArrayList<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(ArrayList<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }





}
