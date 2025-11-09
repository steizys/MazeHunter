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

public class GestorJSON implements GuardarJson {
    private static final String ARCHIVO_JSON = "datos_juego.json";
    private ObjectMapper objectMapper;
    private AdministradorUsuario administradorUsuario;

    public GestorJSON(AdministradorUsuario administradorUsuario) {
        this.administradorUsuario = administradorUsuario;
        this.objectMapper = new ObjectMapper();

        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Configuración adicional
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void crearArchivoJson() {
        try {
            File archivo = new File(ARCHIVO_JSON);
            if (!archivo.exists()) {
                archivo.createNewFile();
                // Inicializar con lista vacía si el archivo es nuevo
                objectMapper.writeValue(archivo, new ArrayList<Usuario>());
                System.out.println("Archivo JSON creado exitosamente.");
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo JSON: " + e.getMessage());
        }
    }

    @Override
    public void mostrarArchivoJson() {
        try {
            List<Usuario> usuarios = cargarTodosLosUsuarios();
            System.out.println("=== CONTENIDO DEL ARCHIVO JSON ===");
            for (Usuario usuario : usuarios) {
                // Desencriptar para mostrar
                String correoDesencriptado = administradorUsuario.descifrar(usuario.getCorreo());
                System.out.println("Usuario: " + correoDesencriptado);
                System.out.println("Partida activa: " + (usuario.getPartida() != null ? "SÍ" : "NO"));
                if (usuario.getEstadisticas() != null) {
                    System.out.println("Estadísticas: " + usuario.getEstadisticas().size() + " partidas");
                }
                System.out.println("------------------------");
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo JSON: " + e.getMessage());
        }
    }

    /**
     * GUARDADO COMPLETO EN TIEMPO REAL - Se llama después de CADA movimiento
     * Guarda: usuario + partida actual + laberinto + jugador + estadísticas
     */
    public void guardarEstadoCompleto(Usuario usuarioActual) throws IOException {
        List<Usuario> todosLosUsuarios = cargarTodosLosUsuarios();

        // Buscar y actualizar el usuario actual
        String correoActualEncriptado = usuarioActual.getCorreo();
        boolean usuarioEncontrado = false;

        for (int i = 0; i < todosLosUsuarios.size(); i++) {
            Usuario usuarioGuardado = todosLosUsuarios.get(i);

            // Desencriptar correos para comparar
            String correoGuardado = administradorUsuario.descifrar(usuarioGuardado.getCorreo());
            String correoActual = administradorUsuario.descifrar(usuarioActual.getCorreo());

            if (correoGuardado.equals(correoActual)) {
                // Reemplazar usuario completo con estado actualizado
                todosLosUsuarios.set(i, usuarioActual);
                usuarioEncontrado = true;
                break;
            }
        }

        // Si es un usuario nuevo, agregarlo
        if (!usuarioEncontrado) {
            todosLosUsuarios.add(usuarioActual);
        }

        // Guardar todos los usuarios
        guardarUsuarios(todosLosUsuarios);
        System.out.println("💾 Estado del juego guardado exitosamente.");
    }

    /**
     * Cargar partida guardada de un usuario específico
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
     * Guardar estadística cuando el usuario gana una partida
     */
    public void guardarEstadistica(Usuario usuario, Estadistica nuevaEstadistica) throws IOException {
        if (usuario.getEstadisticas() == null) {
            usuario.setEstadisticas(new ArrayList<>());
        }

        usuario.getEstadisticas().add(nuevaEstadistica);
        guardarEstadoCompleto(usuario);
        System.out.println("📊 Estadística guardada exitosamente.");
    }

    // MÉTODOS ORIGINALES ACTUALIZADOS

    // Guardar todos los usuarios
    public void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        objectMapper.writeValue(new File(ARCHIVO_JSON), usuarios);
    }

    // Cargar todos los usuarios
    public List<Usuario> cargarTodosLosUsuarios() throws IOException {
        File archivo = new File(ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(archivo,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
    }

    // Guardar o actualizar un usuario específico
    public void guardarUsuario(Usuario usuarioActualizado) throws IOException {
        // Ahora usa guardarEstadoCompleto para mantener consistencia
        guardarEstadoCompleto(usuarioActualizado);
    }

    // Buscar usuario por correo (DESENCRIPTADO)
    public Usuario buscarUsuarioPorCorreo(String correo) throws IOException {
        List<Usuario> usuarios = cargarTodosLosUsuarios();

        for (Usuario usuario : usuarios) {
            String correoDesencriptado = administradorUsuario.descifrar(usuario.getCorreo());
            if (correoDesencriptado.equals(correo)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * NUEVO: Buscar usuario por correo ENCRIPTADO (para compatibilidad)
     */
    public Usuario buscarUsuarioPorCorreoEncriptado(String correoEncriptado) throws IOException {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equals(correoEncriptado)) {
                return usuario;
            }
        }
        return null;
    }
}