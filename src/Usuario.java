package src;

import java.util.ArrayList;

/**
 * Representa un usuario del sistema con sus credenciales, partida actual e historial de estadísticas.
 * Esta clase almacena la información básica del usuario y su progreso en el juego.
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Usuario {
    private String correo;
    private String contrasenia;
    private Partida partida;
    private ArrayList<Estadistica> estadisticas;

    /**
     * Constructor vacío necesario para la deserialización con Jackson.
     * Crea una instancia de Usuario sin inicializar sus atributos.
     */
    public Usuario() {
    }

    /**
     * Constructor que inicializa un usuario con todos sus atributos.
     *
     * @param correo       Dirección de correo electrónico del usuario (identificador único)
     * @param contrasenia  Contraseña del usuario para autenticación
     * @param partida      Partida actual del usuario, puede ser null si no hay partida activa
     * @param estadisticas Lista de estadísticas históricas del usuario
     */
    public Usuario(String correo, String contrasenia, Partida partida, ArrayList<Estadistica> estadisticas) {
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.partida = partida;
        this.estadisticas = estadisticas;
    }

    /**
     * Getters and Setters
     */
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