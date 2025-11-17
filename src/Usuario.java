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
     * Obtiene la dirección de correo electrónico del usuario.
     *
     * @return Correo electrónico del usuario (almacenado cifrado)
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece la dirección de correo electrónico del usuario.
     *
     * @param correo Nueva dirección de correo electrónico (debe almacenarse cifrada)
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return Contraseña del usuario (almacenada cifrada)
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasenia Nueva contraseña (debe almacenarse cifrada)
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Obtiene la partida actual del usuario.
     *
     * @return Partida actual en progreso, o null si no hay partida activa
     */
    public Partida getPartida() {
        return partida;
    }

    /**
     * Establece la partida actual del usuario.
     *
     * @param partida Nueva partida en progreso, o null para indicar que no hay partida activa
     */
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    /**
     * Obtiene el historial de estadísticas del usuario.
     *
     * @return Lista de estadísticas de partidas anteriores del usuario
     */
    public ArrayList<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    /**
     * Establece el historial de estadísticas del usuario.
     *
     * @param estadisticas Nueva lista de estadísticas que reemplazará el historial existente
     */
    public void setEstadisticas(ArrayList<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }
}