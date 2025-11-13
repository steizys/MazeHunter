package src;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal que gestiona el flujo del juego Maze Hunter
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static AdministradorUsuario administradorUsuario;
    private static GestorJSON gestorJSON;
    private static Usuario usuarioActual;

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }

    /**
     * Inicializa el sistema cargando usuarios y configurando componentes
     */
    private static void inicializarSistema() {
        try {
            // Inicializar con lista vacía
            ArrayList<Usuario> usuarios = new ArrayList<>();
            administradorUsuario = new AdministradorUsuario(usuarios);
            gestorJSON = new GestorJSON(administradorUsuario);

            // Crear archivo JSON si no existe
            gestorJSON.crearArchivoJson();

            // Cargar usuarios existentes
            usuarios = new ArrayList<>(gestorJSON.cargarTodosLosUsuarios());
            administradorUsuario.setUsuarios(usuarios);

            System.out.println("✅ Sistema inicializado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error al inicializar el sistema: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú principal del juego
     */
    private static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MAZE HUNTER ===");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Recuperar contraseña");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    recuperarContrasenia();
                    break;
                case 4:
                    System.out.println("¡Hasta pronto!");
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    /**
     * Registra un nuevo usuario en el sistema
     */
    private static void registrarUsuario() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");

        System.out.print("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine().trim();

        System.out.print("Ingrese su contraseña: ");
        String contrasenia = scanner.nextLine();

        System.out.print("Repita su contraseña: ");
        String confirmarContrasenia = scanner.nextLine();

        if (!contrasenia.equals(confirmarContrasenia)) {
            System.out.println("❌ Las contraseñas no coinciden");
            return;
        }

        Usuario nuevoUsuario = administradorUsuario.registrar(correo, contrasenia);
        if (nuevoUsuario != null) {
            try {
                gestorJSON.guardarEstadoCompleto(nuevoUsuario);
                System.out.println("✅ Usuario registrado exitosamente");
            } catch (IOException e) {
                System.out.println("❌ Error al guardar usuario: " + e.getMessage());
            }
        }
    }

    /**
     * Inicia sesión de un usuario
     */
    private static void iniciarSesion() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine().trim();

        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        if (administradorUsuario.validarUsuario(correo, contrasenia)) {
            usuarioActual = administradorUsuario.buscarUsuario(correo);
            System.out.println("✅ ¡Bienvenido " + correo + "!");
            mostrarMenuJuego();
        } else {
            System.out.println("❌ Credenciales inválidas");
        }
    }

    /**
     * Recupera la contraseña de un usuario
     */
    private static void recuperarContrasenia() {
        System.out.println("\n=== RECUPERAR CONTRASEÑA ===");

        System.out.print("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine().trim();

        administradorUsuario.recuperarContrasenia(correo);
    }

    /**
     * Muestra el menú principal del juego después del login
     */
    private static void mostrarMenuJuego() {
        while (usuarioActual != null) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Jugar laberinto nuevo");
            System.out.println("2. Jugar laberinto guardado");
            System.out.println("3. Ver estadísticas");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    jugarLaberintoNuevo();
                    break;
                case 2:
                    jugarLaberintoGuardado();
                    break;
                case 3:
                    verEstadisticas();
                    break;
                case 4:
                    System.out.println("✅ Sesión cerrada");
                    usuarioActual = null;
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        }
    }

    /**
     * Inicia un nuevo laberinto
     */
    private static void jugarLaberintoNuevo() {
        System.out.println("\n=== NUEVO LABERINTO ===");

        System.out.print("Ingrese el tamaño del laberinto (mínimo 5): ");
        int tamanio = leerEntero();

        if (tamanio < 5) {
            System.out.println("❌ El tamaño mínimo es 5");
            return;
        }

        // Crear nuevo laberinto
        Laberinto laberinto = new Laberinto(tamanio);
        laberinto.verificarConsistencia();

        // Crear jugador en posición inicial
        Posicion posicionInicial = laberinto.obtenerPosicionInicial();
        Jugador jugador = new Jugador(posicionInicial);

        // Crear partida
        Partida partida = new Partida();
        partida.setLaberinto(laberinto);
        partida.setJugador(jugador);
        partida.iniciarPartida();

        usuarioActual.setPartida(partida);

        System.out.println("✅ Laberinto " + tamanio + "x" + tamanio + " creado");
        jugarPartida();
    }

    /**
     * Carga y continúa un laberinto guardado
     */
    private static void jugarLaberintoGuardado() {
        System.out.println("\n=== LABERINTO GUARDADO ===");

        try {
            String correo = administradorUsuario.obtenerCorreoDescifrado(usuarioActual);
            Usuario usuarioCargado = gestorJSON.cargarPartidaUsuario(correo);

            if (usuarioCargado != null && usuarioCargado.getPartida() != null) {
                usuarioActual = usuarioCargado;
                ///////////////////////////////////
                Partida partida = usuarioActual.getPartida();
                if (partida.isPartidaActiva()) {
                    partida.reanudarTiempo();
                    System.out.println("⏱️  Tiempo reanudado");
                }
                /////////////////////////////////
                System.out.println("✅ Partida cargada exitosamente");
                jugarPartida();
            } else {
                System.out.println("❌ No hay partida guardada");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al cargar partida: " + e.getMessage());
        }
    }

    /**
     * Muestra las estadísticas del usuario
     */
    private static void verEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");

        ArrayList<Estadistica> estadisticas = usuarioActual.getEstadisticas();

        if (estadisticas == null || estadisticas.isEmpty()) {
            System.out.println("No hay estadísticas disponibles");
            return;
        }

        for (int i = 0; i < estadisticas.size(); i++) {
            System.out.println("\n--- Partida " + (i + 1) + " ---");
            estadisticas.get(i).mostrarEstadistica();
        }
    }

    /**
     * Formatea la duración a un string legible
     */
    private static String formatTiempo(Duration duracion) {
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;
        long segundos = duracion.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    /**
     * Controla el flujo de juego durante una partida
     */
    private static void jugarPartida() {
        Partida partida = usuarioActual.getPartida();
        Laberinto laberinto = partida.getLaberinto();
        Jugador jugador = partida.getJugador();

        System.out.println("🎮 ¡COMIENZA EL JUEGO!");
        System.out.println("Controles: W(Arriba), A(Izquierda), S(Abajo), D(Derecha), G(Guardar), X(Salir)");

        while (partida.isPartidaActiva() && jugador.sigueVivo()) {
            // Mostrar laberinto y estado
            laberinto.mostrarLaberinto(jugador.getPosicion());
            mostrarEstadoJugador(jugador);

            ///////////////////////
            Duration tiempoTranscurrido = partida.obtenerTiempoTranscurrido();
            System.out.println("⏱️  Tiempo: " + formatTiempo(tiempoTranscurrido));
            //////////////////////

            // Leer movimiento
            System.out.print("Ingrese movimiento: ");
            String movimiento = scanner.nextLine().trim().toLowerCase();

            switch (movimiento) {
                case "w":
                    jugador.moverseArriba(jugador, laberinto);
                    break;
                case "a":
                    jugador.moverseIzquierda(jugador, laberinto);
                    break;
                case "s":
                    jugador.moverseAbajo(jugador, laberinto);
                    break;
                case "d":
                    jugador.moverseDerecha(jugador, laberinto);
                    break;
                case "g":
                    guardarPartida();
                    continue;
                case "x":
                    System.out.println("¿Está seguro de que quiere salir? (S/N)");
                    String confirmacion = scanner.nextLine().trim().toLowerCase();
                    if (confirmacion.equals("s")) {
                        ///  ///////////////
                        partida.pausarTiempo();
                        System.out.println("⏸️  Tiempo pausado");
                        ///  ///////////////
                        guardarPartida();
                        return;
                    }
                    continue;
                default:
                    System.out.println("❌ Movimiento inválido. Use W,A,S,D,G,X");
                    continue;
            }

            // Verificar si llegó a la meta
            Posicion posicionActual = jugador.getPosicion();
            Posicion posicionMeta = laberinto.obtenerPosicionFinal();

            if (posicionActual.getX() == posicionMeta.getX() &&
                    posicionActual.getY() == posicionMeta.getY()) {

                Celda meta = laberinto.getMatrizJuegoPosicion(posicionMeta.getX(), posicionMeta.getY());
                meta.comportamiento(jugador);

                if (meta.isVisitada()) {
                    finalizarPartida(true);
                    return;
                }
            }

            // Guardar estado después de cada movimiento
            guardarPartida();

            // Verificar si perdió
            if (!jugador.sigueVivo()) {
                finalizarPartida(false);
                return;
            }
        }
    }

    /**
     * Muestra el estado actual del jugador
     */
    private static void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\n--- ESTADO DEL JUGADOR ---");
        System.out.println("Vida: " + jugador.getPuntosDeVida() + "/100");
        System.out.println("Cristales: " + jugador.getCristalesRecolectados());
        System.out.println("Llave: " + (jugador.isObtuvoLlave() ? "SÍ" : "NO"));
        System.out.println("Trampas activadas: " + jugador.getTrampasActivadas());
    }

    /**
     * Guarda la partida actual
     */
    private static void guardarPartida() {
        try {
            gestorJSON.guardarEstadoCompleto(usuarioActual);
            System.out.println("💾 Partida guardada");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar partida: " + e.getMessage());
        }
    }

    /**
     * Finaliza la partida actual y guarda estadísticas
     */
    private static void finalizarPartida(boolean victoria) {
        Partida partida = usuarioActual.getPartida();
        Jugador jugador = partida.getJugador();

        // Finalizar partida y obtener tiempo
        Duration tiempoJugado = partida.finalizarPartida();

        if (victoria) {
            System.out.println("\n🎉 ¡FELICIDADES! HAS GANADO");
        } else {
            System.out.println("\n💀 GAME OVER - Has perdido");
        }

        // Crear estadística
        Estadistica estadistica = new Estadistica(
                partida.getTiempoInicio(),
                partida.getTiempoFinal(),
                partida.getLaberinto().getTamanio(),
                jugador.getCristalesRecolectados(),
                jugador.getPuntosDeVida(),
                jugador.getTrampasActivadas(),
                tiempoJugado
        );

        // Mostrar estadísticas
        System.out.println("\n=== RESUMEN DE PARTIDA ===");
        estadistica.mostrarEstadistica();

        // Guardar estadística
        try {
            gestorJSON.guardarEstadistica(usuarioActual, estadistica);
        } catch (IOException e) {
            System.out.println("❌ Error al guardar estadística: " + e.getMessage());
        }

        // Limpiar partida actual
        usuarioActual.setPartida(null);
    }

    /**
     * Lee un número entero desde la consola con validación
     */
    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Ingrese un número válido: ");
            }
        }
    }
}