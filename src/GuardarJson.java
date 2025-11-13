package src;

/**
 * Interfaz para operaciones de persistencia en JSON
 * Define los métodos básicos para crear y mostrar archivos JSON
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public interface GuardarJson {
    /**
     * Crea el archivo JSON inicial si no existe
     */
    public void crearArchivoJson();

    /**
     * Muestra el contenido del archivo JSON de forma legible
     */
    public void mostrarArchivoJson();
}