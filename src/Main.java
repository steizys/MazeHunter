package src;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal que gestiona el flujo completo del juego Maze Hunter.
 * Controla la inicialización del sistema, autenticación de usuarios,
 * gestión de partidas y la interfaz de usuario por consola.
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String VERDE_OSCURO = "\u001B[38;5;28m";
    public static final String AZUL = "\u001B[34m";
    public static final String AZUL_CLARO = "\u001B[36m";
    public static final String ROJO = "\u001B[31m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String NARANJA = "\u001B[38;5;214m";
    public static final String MARRON = "\u001B[38;5;94m";
    public static final String GRIS = "\u001B[38;5;248m";
    public static final String MORADO = "\u001B[35m";
    public static final String NEGRITA = "\u001B[1m";
    public static final String CIAN = "\u001B[36m";

    private static Scanner scanner = new Scanner(System.in);
    private static AdministradorUsuario administradorUsuario;
    private static GestorJSON gestorJSON;
    private static Usuario usuarioActual;

    private static void limpiarPantalla(){
        for (int i = 0; i < 50; i++) {
                System.out.println();
            }
    }
    /**
     * Convierte una duración en un formato de tiempo legible (HH:MM:SS)
     *
     * @param duracion La duración a formatear
     * @return String con el tiempo formateado en horas, minutos y segundos
     */
    private static String formatTiempo(Duration duracion) {
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;
        long segundos = duracion.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    /**
     * Controla el flujo principal de juego durante una partida activa.
     * Gestiona los movimientos del jugador, la visualización del laberinto,
     * y las interacciones con las celdas especiales.
     */
    private static void jugarPartida() {
        Partida partida = usuarioActual.getPartida();
        Laberinto laberinto = partida.getLaberinto();
        Jugador jugador = partida.getJugador();

        limpiarPantalla();
        System.out.println(AMARILLO + "\n╔════════════════════════════════╗");
        System.out.println("║       COMIENZA EL JUEGO        ║");
        System.out.println("╚════════════════════════════════╝" + RESET);
        //System.out.println(AMARILLO + "Controles: " + VERDE + "W(Arriba), A(Izquierda), S(Abajo), D(Derecha), X(Salir)" + RESET);

        while (partida.isPartidaActiva() && jugador.sigueVivo()) {
            // Mostrar laberinto y estado
            laberinto.mostrarLaberinto(jugador.getPosicion());
            mostrarEstadoJugador(jugador);

            Duration tiempoTranscurrido = partida.obtenerTiempoTranscurrido();
            System.out.println(AZUL_CLARO + "⏱️  Tiempo: " + AMARILLO + formatTiempo(tiempoTranscurrido) + RESET);

            // Leer movimiento
            System.out.print(AZUL + "Ingrese movimiento: " + RESET);
            String movimiento = scanner.nextLine().trim().toLowerCase();
            if(movimiento.equals("w")){
                jugador.moverseArriba(jugador, laberinto);
            }else if(movimiento.equals("a")){
                jugador.moverseIzquierda(jugador, laberinto);
            }else if(movimiento.equals("s")){
                jugador.moverseAbajo(jugador, laberinto);
            }else if(movimiento.equals("d")){
                jugador.moverseDerecha(jugador, laberinto);
            }else if(movimiento.equals("x")){
                System.out.println(AMARILLO + "¿Está seguro de que quiere salir? (S/N): " + RESET);
                String confirmacion = scanner.nextLine().trim().toLowerCase();
                if (confirmacion.equals("s")) {
                    partida.pausarTiempo();
                    System.out.println(AZUL + ">> Tiempo pausado" + RESET);
                    guardarPartida();
                    return;
                }
                continue;
            }else{
                System.out.println(ROJO + ">> Movimiento inválido. Use W,A,S,D,X" + RESET);
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
     * Muestra el estado actual del jugador incluyendo vida, cristales y objetos recolectados.
     *
     * @param jugador El jugador cuyo estado se va a mostrar
     */
    private static void mostrarEstadoJugador(Jugador jugador) {
        String colorVida = jugador.getPuntosDeVida() > 50 ? VERDE + NEGRITA :
                jugador.getPuntosDeVida() > 25 ? AMARILLO + NEGRITA : ROJO + NEGRITA;

        System.out.println(AZUL + NEGRITA + "\n┌─────────────────────────────┐" + RESET);
        System.out.println(AZUL + NEGRITA + "│     ESTADO DEL JUGADOR      │" + RESET);
        System.out.println(AZUL + NEGRITA + "├─────────────────────────────┤" + RESET);
        System.out.printf(AZUL + NEGRITA + "│ " + colorVida + "Vida: %-21s " + AZUL + NEGRITA + "│\n" + RESET, jugador.getPuntosDeVida() + "/100");
        System.out.printf(AZUL + NEGRITA + "│ " + AZUL_CLARO + NEGRITA + "Cristales: %-16s " + AZUL + NEGRITA + "│\n" + RESET, jugador.getCristalesRecolectados());
        System.out.printf(AZUL + NEGRITA + "│ " + GRIS + NEGRITA + "Llave: %-25s " + AZUL + NEGRITA + "│\n" + RESET, (jugador.isObtuvoLlave() ? VERDE + "SÍ" : ROJO + "NO"));
        System.out.printf(AZUL + NEGRITA + "│ " + ROJO + NEGRITA + "Trampas: %-18s " + AZUL + NEGRITA + "│\n" + RESET, jugador.getTrampasActivadas());
        System.out.println(AZUL + NEGRITA + "└─────────────────────────────┘" + RESET);
    }

    /**
     * Guarda el estado actual de la partida en el archivo JSON.
     * Maneja posibles errores de entrada/salida.
     */
    private static void guardarPartida() {
        try {
            gestorJSON.guardarEstadoCompleto(usuarioActual);
            //System.out.println(VERDE + ">> Partida guardada correctamente" + RESET);
        } catch (IOException e) {
            System.out.println(ROJO + ">> Error al guardar partida: " + e.getMessage() + RESET);
        }
    }

    /**
     * Finaliza la partida actual, calcula estadísticas y las guarda.
     *
     * @param victoria true si el jugador ganó la partida, false si perdió
     */
    private static void finalizarPartida(boolean victoria) {
        Partida partida = usuarioActual.getPartida();
        Jugador jugador = partida.getJugador();

        // Finalizar partida y obtener tiempo
        Duration tiempoJugado = partida.finalizarPartida();

        if (victoria) {
            System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
            scanner.nextLine();
            limpiarPantalla();
            System.out.println(VERDE + "\n╔═══════════════════════════════╗");
            System.out.println("║    ¡FELICIDADES! HAS GANADO   ║");
            System.out.println("╚═══════════════════════════════╝" + RESET);
        } else {
            limpiarPantalla();
            System.out.println(ROJO + "\n╔════════════════════════════════╗");
            System.out.println("║          GAME OVER             ║");
            System.out.println("║        Has perdido             ║");
            System.out.println("╚════════════════════════════════╝" + RESET);
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
        estadistica.mostrarEstadistica();
        System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
        scanner.nextLine();
        limpiarPantalla();

        // Guardar estadística
        try {
            gestorJSON.guardarEstadistica(usuarioActual, estadistica);
        } catch (IOException e) {
            System.out.println(ROJO + ">> Error al guardar estadística: " + e.getMessage() + RESET);
        }

        // Limpiar partida actual
        usuarioActual.setPartida(null);
    }

    /**
     * Lee y valida un número entero desde la entrada estándar.
     *
     * @return El número entero validado ingresado por el usuario
     */
    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(ROJO + ">> Ingrese un número válido: " + RESET);
            }
        }
    }

    /**
     * Muestra y gestiona el menú principal del juego después del inicio de sesión.
     * Incluye opciones para jugar, ver estadísticas y cerrar sesión.
     */
    private static void mostrarMenuJuego() {
        while (usuarioActual != null) {
            limpiarPantalla();
            System.out.println(AZUL + "\n╔════════════════════════════════╗");
            System.out.println("║" + AZUL_CLARO + NEGRITA + "         MENÚ PRINCIPAL         " + RESET + AZUL + "║");
            System.out.println("╠════════════════════════════════╣" + RESET);
            System.out.println(AZUL + "║" + AZUL + NEGRITA + "  1. Jugar laberinto nuevo      " + AZUL + "║");
            System.out.println(AZUL + "║" + AZUL_CLARO + NEGRITA + "  2. Jugar laberinto guardado   " + AZUL + "║");
            System.out.println(AZUL + "║" + MORADO + NEGRITA + "  3. Ver estadísticas           " + AZUL + "║");
            System.out.println(AZUL + "║" + GRIS + NEGRITA + "  4. Cerrar sesión              " + AZUL + "║");
            System.out.println(AZUL + "╚════════════════════════════════╝" + RESET);
            System.out.print(NEGRITA + AMARILLO + "Seleccione una opción [" + VERDE + "1-4" + AMARILLO + "]: " + RESET);

            int opcion = leerEntero();
            if(opcion==1){
                limpiarPantalla();
                System.out.println(VERDE + "\n┌─────────────────────────────┐");
                System.out.println("│       NUEVO LABERINTO       │");
                System.out.println("└─────────────────────────────┘" + RESET);

                boolean tamanioValido = false;
                int tamanio = 0;


                while (!tamanioValido) {
                    System.out.print(AZUL + "Ingrese el tamaño del laberinto (mínimo 10): " + RESET);
                    tamanio = leerEntero();

                    if (tamanio >= 10) {
                        tamanioValido = true;
                    } else {
                        System.out.println(ROJO + ">> ERROR: El tamaño mínimo es 10" + RESET);
                        System.out.print(AMARILLO + "¿Desea intentar con otro tamaño? (S/N): " + RESET);
                        String respuesta = scanner.nextLine().trim().toLowerCase();

                        if (!respuesta.equals("s") && !respuesta.equals("si")) {
                            break;
                        }
                    }
                }

                if (!tamanioValido) {
                    continue;
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

                System.out.println(VERDE + ">> Laberinto " + tamanio + "x" + tamanio + " creado exitosamente" + RESET);
                System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
                scanner.nextLine();
                limpiarPantalla();
                jugarPartida();
            }
            else if(opcion==2){
                limpiarPantalla();
                System.out.println(AZUL + "\n┌─────────────────────────────┐");
                System.out.println("│     LABERINTO GUARDADO      │");
                System.out.println("└─────────────────────────────┘" + RESET);

                try {
                    String correo = administradorUsuario.obtenerCorreoDescifrado(usuarioActual);
                    Usuario usuarioCargado = gestorJSON.cargarPartidaUsuario(correo);
                    if (usuarioCargado != null && usuarioCargado.getPartida()!= null && usuarioCargado.getPartida().isPartidaActiva())  {
                        usuarioActual = usuarioCargado;
                        Partida partida = usuarioActual.getPartida();
                        if (partida.isPartidaActiva()) {
                            partida.reanudarTiempo();
                            System.out.println(AZUL + ">> Tiempo reanudado" + RESET);
                        }
                        System.out.println(VERDE + ">> Partida cargada exitosamente" + RESET);
                        jugarPartida();
                    } else {
                        System.out.println(ROJO + ">> No hay partida guardada" + RESET);
                        System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
                        scanner.nextLine();
                        limpiarPantalla();
                    }
                } catch (IOException e) {
                    System.out.println(ROJO + ">> Error al cargar partida: " + e.getMessage() + RESET);
                }
            }
            else if(opcion==3){
                limpiarPantalla();
                ArrayList<Estadistica> estadisticas = usuarioActual.getEstadisticas();

                if (estadisticas == null || estadisticas.isEmpty()) {
                    System.out.println(AMARILLO + "No hay estadísticas disponibles" + RESET);
                    continue;
                }

                System.out.println(NEGRITA + MORADO + "╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                                  HISTORIAL DE ESTADÍSTICAS                                   ║");
                System.out.println("╠══════╦══════════╦══════════════════╦══════════════════════╦════════════════╦═════════════════╣");
                System.out.println("║" + CIAN + "  #   " + MORADO + "║" + CIAN + "  Tiempo  " + MORADO + "║" + CIAN + "      Tamaño      " + MORADO + "║" + CIAN + "       Cristales      " + MORADO + "║" + CIAN + "      Vida      " + MORADO + "║" + CIAN + "     Trampas     " + MORADO + "║");
                System.out.println("╠══════╬══════════╬══════════════════╬══════════════════════╬════════════════╬═════════════════╣" + RESET);

                for (int i = 0; i < estadisticas.size(); i++) {
                    Estadistica estadistica = estadisticas.get(i);

                    String mostrarTiempo = "N/A";
                    if (estadistica.getTiempoJugado() != null) {
                        Duration tiempo = estadistica.getTiempoJugado();
                        long horas = tiempo.toHours();
                        long minutos = tiempo.toMinutes() % 60;
                        long segundos = tiempo.getSeconds() % 60;
                        mostrarTiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
                    } else if (estadistica.getTiempoInicio() != null && estadistica.getTiempoFinal() != null) {
                        Duration duracion = Duration.between(estadistica.getTiempoInicio(), estadistica.getTiempoFinal());
                        long horas = duracion.toHours();
                        long minutos = duracion.toMinutes() % 60;
                        long segundos = duracion.getSeconds() % 60;
                        mostrarTiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
                    }

                    String colorVida = estadistica.getPuntosDeVida() > 50 ? VERDE :
                            estadistica.getPuntosDeVida() > 25 ? AMARILLO : ROJO;

                    System.out.printf(MORADO + "║" + RESET + "  %-2d  " + MORADO + "║ " + RESET + "%-8s " + MORADO + "║ " +  RESET + "      %-2dx%-2d      " + MORADO + "║ " + AZUL + "         %-2d          " + MORADO + "║ " + colorVida + "    %-3d/100    " + MORADO + "║ " + ROJO + "        %-2d      " + MORADO + "║\n" + RESET,
                            i + 1,
                            mostrarTiempo,
                            estadistica.getTamanoDeMatriz(),
                            estadistica.getTamanoDeMatriz(),
                            estadistica.getCristalesRecolectados(),
                            estadistica.getPuntosDeVida(),
                            estadistica.getTrampasActivadas()
                    );

                    if (i < estadisticas.size() - 1) {
                        System.out.println(MORADO + "╠══════╬══════════╬══════════════════╬══════════════════════╬════════════════╬═════════════════╣" + RESET);
                    }
                }

                System.out.println(MORADO + "╚══════╩══════════╩══════════════════╩══════════════════════╩════════════════╩═════════════════╝" + RESET);
                System.out.print(AMARILLO + "\nPresione Enter para continuar..." + RESET);
                scanner.nextLine();
                limpiarPantalla();
            }else if(opcion==4){
                //System.out.println(AZUL + ">> Sesión cerrada correctamente" + RESET);
                usuarioActual = null;
                break;
            }else{
                System.out.println(ROJO + ">> Opción inválida" + RESET);
            }
        }
    }

    /**
     * Punto de entrada principal del programa Maze Hunter.
     * Inicializa el sistema, carga usuarios existentes y gestiona el menú de autenticación.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
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

            System.out.println(VERDE + ">> Sistema inicializado correctamente" + RESET);
        } catch (Exception e) {
            System.out.println(ROJO + ">> Error al inicializar el sistema: " + e.getMessage() + RESET);
        }
        int opcion=0;
        while (true && opcion!=3) {
            limpiarPantalla();
            System.out.println(GRIS + "╔══════════════════════════════════════════════════════╗" + RESET);
            System.out.println(AMARILLO + "          ███╗   ███╗" + NARANJA + " █████╗" + VERDE + " ███████╗" + VERDE_OSCURO + "███████╗" + RESET);
            System.out.println(AMARILLO + "          ████╗ ████║" + NARANJA + "██╔══██╗" + VERDE + "╚══███╔╝" + VERDE_OSCURO + "██╔════╝" + RESET);
            System.out.println(AMARILLO + "          ██╔████╔██║" + NARANJA + "███████║" + VERDE + "  ███╔╝ " + VERDE_OSCURO + "█████╗  " + RESET);
            System.out.println(AMARILLO + "          ██║╚██╔╝██║" + NARANJA + "██╔══██║" + VERDE + " ███╔╝  " + VERDE_OSCURO + "██╔══╝  " + RESET);
            System.out.println(AMARILLO + "          ██║ ╚═╝ ██║" + NARANJA + "██║  ██║" + VERDE + "███████╗" + VERDE_OSCURO + "███████╗" + RESET);
            System.out.println(AMARILLO + "          ╚═╝     ╚═╝" + NARANJA + "╚═╝  ╚═╝" + VERDE + "╚══════╝" + VERDE_OSCURO + "╚══════╝" + RESET);
            System.out.println(VERDE_OSCURO + "  ██╗  ██╗" + AMARILLO + "██╗   ██╗" + NARANJA + "███╗   ██╗" + VERDE + "████████╗" + AMARILLO + "███████╗" + NARANJA + "██████╗" + RESET);
            System.out.println(VERDE_OSCURO + "  ██║  ██║" + AMARILLO + "██║   ██║" + NARANJA + "████╗  ██║" + VERDE + "╚══██╔══╝" + AMARILLO + "██╔════╝" + NARANJA + "██╔══██╗" + RESET);
            System.out.println(VERDE_OSCURO + "  ███████║" + AMARILLO + "██║   ██║" + NARANJA + "██╔██╗ ██║" + VERDE + "   ██║   " + AMARILLO + "█████╗  " + NARANJA + "█████╔╝" + RESET);
            System.out.println(VERDE_OSCURO + "  ██╔══██║" + AMARILLO + "██║   ██║" + NARANJA + "██║╚██╗██║" + VERDE + "   ██║   " + AMARILLO + "██╔══╝  " + NARANJA + "██╔══██╗" + RESET);
            System.out.println(VERDE_OSCURO + "  ██║  ██║" + AMARILLO + "╚██████╔╝" + NARANJA + "██║ ╚████║" + VERDE + "   ██║   " + AMARILLO + "███████╗" + NARANJA + "██║  ██║" + RESET);
            System.out.println(VERDE_OSCURO + "  ╚═╝  ╚═╝" + AMARILLO + " ╚═════╝ " + NARANJA + "╚═╝  ╚═══╝" + VERDE + "   ╚═╝   " + AMARILLO + "╚══════╝" + NARANJA + "╚═╝  ╚═╝" + RESET);
            System.out.println(GRIS + "╚══════════════════════════════════════════════════════╝" + RESET);
            // Centrado con el título MAZE HUNTER
            System.out.println(AZUL + "          ╔════════════════════════════════╗" + RESET);
            System.out.printf(AZUL + "          ║" + NEGRITA + AMARILLO + "   %-28s " + RESET + AZUL + "║\n" + RESET, "1. REGISTRARSE");
            System.out.printf(AZUL + "          ║" + NEGRITA + VERDE + "   %-28s " + RESET + AZUL + "║\n" + RESET, "2. INICIAR SESIÓN");
            System.out.printf(AZUL + "          ║" + NEGRITA + ROJO + "   %-28s " + RESET + AZUL + "║\n" + RESET, "3. SALIR");
            System.out.println(AZUL + "          ╚════════════════════════════════╝" + RESET);
            System.out.print(NEGRITA + AMARILLO + "Seleccione una opción [" + VERDE + "1-3" + AMARILLO + "]: " + RESET);

            opcion = leerEntero();
            if(opcion==1){
                limpiarPantalla();
                System.out.println(VERDE + "\n┌─────────────────────────────┐");
                System.out.println("│         REGISTRARSE         │");
                System.out.println("└─────────────────────────────┘" + RESET);

                System.out.print(AZUL + "Ingrese su correo electrónico: " + RESET);
                String correo = scanner.nextLine().trim();

                Usuario nuevoUsuario = administradorUsuario.buscarUsuario(correo.toLowerCase());
                if (nuevoUsuario != null) {
                    System.out.println(ROJO + ">> Error: El correo ya está registrado" + RESET);
                    System.out.print(AMARILLO + "¿Quieres recuperar la contraseña? (S/N): " + RESET);
                    String confirmacion = scanner.nextLine().trim().toLowerCase();
                    if (confirmacion.equals("s")) {
                        administradorUsuario.recuperarContrasenia(correo);
                        continue;
                    }else{
                        continue;
                    }
                }
                System.out.print(AZUL + "Ingrese su contraseña: " + RESET);
                String contrasenia = scanner.nextLine();

                System.out.print(AZUL + "Repita su contraseña: " + RESET);
                String confirmarContrasenia = scanner.nextLine();

                if (!contrasenia.equals(confirmarContrasenia)) {
                    System.out.println(ROJO + ">> Las contraseñas no coinciden" + RESET);
                    System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
                    scanner.nextLine();
                    limpiarPantalla();
                }else{
                    nuevoUsuario= administradorUsuario.registrar(correo, contrasenia);
                    if (nuevoUsuario != null) {
                        try {
                            gestorJSON.guardarEstadoCompleto(nuevoUsuario);
                            System.out.println(VERDE + ">> Usuario registrado exitosamente" + RESET);
                        } catch (IOException e) {
                            System.out.println(ROJO + ">> Error al guardar usuario: " + e.getMessage() + RESET);
                        }
                    }


                }


            }else if(opcion==2){
                limpiarPantalla();
                System.out.println(AZUL + "\n┌─────────────────────────────┐");
                System.out.println("│       INICIAR SESIÓN        │");
                System.out.println("└─────────────────────────────┘" + RESET);

                System.out.print(AZUL + "Correo electrónico: " + RESET);
                String correo = scanner.nextLine().trim();


                System.out.print(AZUL + "Contraseña: " + RESET);
                System.out.print(AMARILLO + "\nPresione 1 para recuperar contraseña: \n" + RESET);
                String contrasenia = scanner.nextLine().trim();
                if(contrasenia.equals("1")){
                    administradorUsuario.recuperarContrasenia(correo);
                    System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
                    scanner.nextLine();
                    limpiarPantalla();
                    continue;

                }else{
                    if (administradorUsuario.validarUsuario(correo, contrasenia)) {
                        usuarioActual = administradorUsuario.buscarUsuario(correo);
                        System.out.println(VERDE + ">> ¡Bienvenido " + correo.toLowerCase() + "!" + RESET);
                        mostrarMenuJuego();
                    } else {
                        System.out.println(ROJO + ">> Credenciales inválidas" + RESET);
                        System.out.print(AMARILLO + "Presione Enter para continuar..." + RESET);
                        scanner.nextLine();
                        limpiarPantalla();
                    }
                }
            }else if(opcion==3){
                limpiarPantalla();
                System.out.println(MARRON + ">> ¡Hasta pronto!" + RESET);
                return;
            }else{
                System.out.println(ROJO + ">> Opción inválida" + RESET);
            }
        }
    }
}