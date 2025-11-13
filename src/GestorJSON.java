package src;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar la informacion de datos del juego en formato JSON.
 *
 * <p>Esta clase implementa la interfaz {@link GuardarJson} y proporciona funcionalidades
 * completas para:</p>
 * <ul>
 *   <li>Crear y gestionar el archivo JSON de datos del juego</li>
 *   <li>Guardar y cargar el estado completo del juego en tiempo real</li>
 *   <li>Manejar la serialización y deserialización de objetos complejos</li>
 *   <li>Gestionar usuarios, partidas, laberintos y estadísticas</li>
 *   <li>Trabajar con datos encriptados para información sensible</li>
 * </ul>
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 * @see GuardarJson
 * @see AdministradorUsuario
 * @see Usuario
 * @see Estadistica
 */
public class GestorJSON implements GuardarJson {
    /**
     * Nombre del archivo JSON donde se almacenan todos los datos del juego.
     */
    private static final String ARCHIVO_JSON = "datos_juego.json";

    private ObjectMapper objectMapper;
    private AdministradorUsuario administradorUsuario;

    /**
     * Constructor que inicializa el gestor JSON con el administrador de usuarios.
     *
     * <p>Configura el ObjectMapper con las siguientes características:</p>
     * <ul>
     *   <li>Salida formateada con indentación</li>
     *   <li>Soporte para Java Time API (fechas)</li>
     *   <li>Tipado polimórfico para herencia</li>
     *   <li>Tolerancia a propiedades desconocidas</li>
     * </ul>
     *
     * @param administradorUsuario Administrador de usuarios para operaciones de encriptación
     */
    public GestorJSON(AdministradorUsuario administradorUsuario) {
        this.administradorUsuario = administradorUsuario;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//identa las lineas dentro del JSON
        this.objectMapper.registerModule(new JavaTimeModule());//convierte el instant y duration para manejar el tiempo
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);//formatea las fechas
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//maneja los campos nuevos/viejos
        this.objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(), //valida las clases a instanciar
                ObjectMapper.DefaultTyping.NON_FINAL,//califica el tipo que tiene el objeto
                JsonTypeInfo.As.PROPERTY//indica como guardar la informacion en el json de forma mas legible y entendible
        );
        // Configuración adicional
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);//maneja los diferentes casos de clases (clases sin atributos,
                                                                                            //atributos sin getters, o propiedades que el Json no puede convertir directamente)
    }

    /**
     * Crea el archivo JSON inicial si no existe.
     *
     * <p>Si el archivo no existe, lo crea e inicializa con una lista vacía de usuarios.
     * Si el archivo ya existe, no realiza ninguna acción.</p>
     *
     * @implNote Implementación del método de la interfaz {@link GuardarJson}
     */
    public void crearArchivoJson() {
        try {
            File archivo = new File(ARCHIVO_JSON);
            if (!archivo.exists()) {
                archivo.createNewFile();
                objectMapper.writeValue(archivo, new ArrayList<Usuario>());  // Inicializar con lista vacía si el archivo es nuevo
                System.out.println("Archivo JSON creado exitosamente.");
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo JSON: " + e.getMessage());
        }
    }

    /**
     * Carga todos los usuarios desde el archivo JSON.
     *
     * @return Lista de todos los usuarios guardados, o lista vacía si el archivo no existe
     * @throws IOException Si ocurre un error durante la lectura
     */
    public List<Usuario> cargarTodosLosUsuarios() throws IOException {
        File archivo = new File(ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(archivo,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
    }

    /**
     * Guarda la lista completa de usuarios en el archivo JSON.
     *
     * @param usuarios Lista de usuarios a guardar
     * @throws IOException Si ocurre un error durante la escritura
     */
    public void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        objectMapper.writeValue(new File(ARCHIVO_JSON), usuarios);
    }

    /**
     * Muestra el contenido del archivo JSON de forma legible.
     *
     * <p>Desencripta la información del JSON para mostrar un resumen
     * del estado guardado del juego.</p>
     *
     * @implNote Implementación del método de la interfaz {@link GuardarJson}
     */
    public void mostrarArchivoJson() {
        try {
            List<Usuario> usuarios = cargarTodosLosUsuarios();
            System.out.println("=== CONTENIDO DEL ARCHIVO JSON ===");
            for (Usuario usuario : usuarios) {
                // Desencriptar para mostrar
                String correoDesencriptado = administradorUsuario.descifrar(usuario.getCorreo());
                System.out.println("Usuario: " + correoDesencriptado);
                if (usuario.getPartida() != null) {
                    System.out.println("Partida activa: SÍ");
                } else {
                    System.out.println("Partida activa: NO");
                }
                if (usuario.getEstadisticas() != null) {
                    System.out.println("Estadísticas: " + usuario.getEstadisticas().size() + " partidas");
                }
                System.out.println("----------------------------");
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo JSON: " + e.getMessage());
        }
    }

    /**
     * Guarda el estado completo del juego en tiempo real.
     *
     * <p>Este método se debe llamar después de cada movimiento importante y guarda:</p>
     * <ul>
     *   <li>Información del usuario</li>
     *   <li>Partida actual con todo su estado</li>
     *   <li>Laberinto y posición del jugador</li>
     *   <li>Estadísticas acumuladas</li>
     * </ul>
     *
     * @param usuarioActual Usuario con el estado actual del juego a guardar
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    public void guardarEstadoCompleto(Usuario usuarioActual) throws IOException {
        List<Usuario> todosLosUsuarios = cargarTodosLosUsuarios();
        String correoActualEncriptado = usuarioActual.getCorreo();

        // Buscar por correo ENCRIPTADO (más confiable)
        boolean usuarioEncontrado = false;
        for (int i = 0; i < todosLosUsuarios.size(); i++) {
            Usuario usuarioGuardado = todosLosUsuarios.get(i);

            // Comparar correos encriptados directamente
            if (usuarioGuardado.getCorreo().equals(correoActualEncriptado)) {
                // ✅ REEMPLAZO COMPLETO del usuario
                todosLosUsuarios.set(i, usuarioActual);
                usuarioEncontrado = true;
                System.out.println("🔄 Usuario actualizado en la lista");
                break;
            }
        }

        if (!usuarioEncontrado) {
            todosLosUsuarios.add(usuarioActual);
            System.out.println("➕ Usuario nuevo agregado a la lista");
        }

        // Guardar la lista completa
        guardarUsuarios(todosLosUsuarios);

        // ✅ ACTUALIZAR la lista en memoria
        administradorUsuario.setUsuarios(new ArrayList<>(todosLosUsuarios));

        System.out.println("💾 Estado del juego guardado exitosamente.");
    }


    /**
     * Carga la partida guardada de un usuario específico.
     *
     * <p>Busca en el archivo JSON el usuario correspondiente al correo proporcionado
     * y reinicia el estado del laberinto para garantizar consistencia al cargar.</p>
     *
     * @param correo Correo electrónico del usuario (en texto plano)
     * @return Objeto Usuario con toda su información y partida cargada, o null si no se encuentra
     * @throws IOException Si ocurre un error durante la lectura del archivo
     */
    public Usuario cargarPartidaUsuario(String correo) throws IOException {
        List<Usuario> usuarios = cargarTodosLosUsuarios();

        for (Usuario usuario : usuarios) {
            String correoDesencriptado = administradorUsuario.descifrar(usuario.getCorreo());
            if (correoDesencriptado.equals(correo)) {
                // REINICIAR ESTADO DEL LABERINTO AL CARGAR
                if (usuario.getPartida() != null && usuario.getPartida().getLaberinto() != null) {
                    usuario.getPartida().getLaberinto().reiniciarEstado();
                }
                System.out.println("✅ Partida cargada para: " + correo);
                return usuario;
            }
        }

        System.out.println("⚠️ No se encontró partida guardada para: " + correo);
        return null;
    }

    /**
     * Guarda una nueva estadística cuando el usuario completa una partida.
     *
     * <p>Agrega la estadística a la lista del usuario y guarda inmediatamente
     * el estado completo para persistir el cambio.</p>
     *
     * @param usuario Usuario al que se le agregará la estadística
     * @param nuevaEstadistica Objeto Estadistica con los datos de la partida completada
     * @throws IOException Si ocurre un error durante el guardado
     */
    public void guardarEstadistica(Usuario usuario, Estadistica nuevaEstadistica) throws IOException {
        if (usuario.getEstadisticas() == null) {
            usuario.setEstadisticas(new ArrayList<>());
        }

        usuario.getEstadisticas().add(nuevaEstadistica);
        guardarEstadoCompleto(usuario);
        System.out.println(" Estadística guardada exitosamente.");
    }


}