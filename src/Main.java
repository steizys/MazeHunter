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
    private static Scanner scanner = new Scanner(System.in);
    private static AdministradorUsuario administradorUsuario;
    private static GestorJSON gestorJSON;
    private static Usuario usuarioActual;

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

        System.out.println("🎮 ¡COMIENZA EL JUEGO!");
        System.out.println("Controles: W(Arriba), A(Izquierda), S(Abajo), D(Derecha), X(Salir)");

        while (partida.isPartidaActiva() && jugador.sigueVivo()) {
            // Mostrar laberinto y estado
            laberinto.mostrarLaberinto(jugador.getPosicion());
            mostrarEstadoJugador(jugador);

            Duration tiempoTranscurrido = partida.obtenerTiempoTranscurrido();
            System.out.println("⏱️  Tiempo: " + formatTiempo(tiempoTranscurrido));

            // Leer movimiento
            System.out.print("Ingrese movimiento: ");
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
                System.out.println("¿Está seguro de que quiere salir? (S/N)");
                String confirmacion = scanner.nextLine().trim().toLowerCase();
                if (confirmacion.equals("s")) {
                    partida.pausarTiempo();
                    System.out.println("⏸️  Tiempo pausado");
                    guardarPartida();
                    return;
                }
                continue;
            }else{
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
     * Muestra el estado actual del jugador incluyendo vida, cristales y objetos recolectados.
     *
     * @param jugador El jugador cuyo estado se va a mostrar
     */
    private static void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\n--- ESTADO DEL JUGADOR ---");
        System.out.println("Vida: " + jugador.getPuntosDeVida() + "/100");
        System.out.println("Cristales: " + jugador.getCristalesRecolectados());
        System.out.println("Llave: " + (jugador.isObtuvoLlave() ? "SÍ" : "NO"));
        System.out.println("Trampas activadas: " + jugador.getTrampasActivadas());
    }

    /**
     * Guarda el estado actual de la partida en el archivo JSON.
     * Maneja posibles errores de entrada/salida.
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
     * Lee y valida un número entero desde la entrada estándar.
     *
     * @return El número entero validado ingresado por el usuario
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

    /**
     * Muestra y gestiona el menú principal del juego después del inicio de sesión.
     * Incluye opciones para jugar, ver estadísticas y cerrar sesión.
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
            if(opcion==1){
                System.out.println("\n=== NUEVO LABERINTO ===");

                System.out.print("Ingrese el tamaño del laberinto (mínimo 6): ");
                int tamanio = leerEntero();

                if (tamanio <= 5) {
                    System.out.println("❌ El tamaño mínimo es 6");
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
            else if(opcion==2){
                System.out.println("\n=== LABERINTO GUARDADO ===");

                try {
                    String correo = administradorUsuario.obtenerCorreoDescifrado(usuarioActual);
                    Usuario usuarioCargado = gestorJSON.cargarPartidaUsuario(correo);
                    if (usuarioCargado != null && usuarioCargado.getPartida().isPartidaActiva()) {
                        usuarioActual = usuarioCargado;
                        Partida partida = usuarioActual.getPartida();
                        if (partida.isPartidaActiva()) {
                            partida.reanudarTiempo();
                            System.out.println("⏱️  Tiempo reanudado");
                        }
                        System.out.println("✅ Partida cargada exitosamente");
                        jugarPartida();
                    } else {
                        System.out.println("❌ No hay partida guardada");
                    }
                } catch (IOException e) {
                    System.out.println("❌ Error al cargar partida: " + e.getMessage());
                }
            }
            else if(opcion==3){
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
            }else if(opcion==4){
                System.out.println("✅ Sesión cerrada");
                usuarioActual = null;
                break;
            }else{
                System.out.println("❌ Opción inválida");
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

            System.out.println("✅ Sistema inicializado correctamente");
        } catch (Exception e) {
            System.out.println("❌ Error al inicializar el sistema: " + e.getMessage());
        }
        int opcion=0;
        while (true && opcion!=3) {
            System.out.println("\n=== MAZE HUNTER ===");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();
            if(opcion==1){
                System.out.println("\n=== REGISTRO DE USUARIO ===");

                System.out.print("Ingrese su correo electrónico: ");
                String correo = scanner.nextLine().trim();

                Usuario nuevoUsuario = administradorUsuario.buscarUsuario(correo);
                if (nuevoUsuario != null) {
                    System.out.println(" Error: El correo ya está registrado");
                    System.out.println("¿Quieres recuperar la contraseña? (S/N)");
                    String confirmacion = scanner.nextLine().trim().toLowerCase();
                    if (confirmacion.equals("s")) {
                        administradorUsuario.recuperarContrasenia(correo);
                        continue;
                    }else{
                        continue;
                    }
                }
                System.out.print("Ingrese su contraseña: ");
                String contrasenia = scanner.nextLine();

                System.out.print("Repita su contraseña: ");
                String confirmarContrasenia = scanner.nextLine();

                if (!contrasenia.equals(confirmarContrasenia)) {
                    System.out.println("❌ Las contraseñas no coinciden");

                }else{
                    nuevoUsuario= administradorUsuario.registrar(correo, contrasenia);
                    if (nuevoUsuario != null) {
                        try {
                            gestorJSON.guardarEstadoCompleto(nuevoUsuario);
                            System.out.println("✅ Usuario registrado exitosamente");
                        } catch (IOException e) {
                            System.out.println("❌ Error al guardar usuario: " + e.getMessage());
                        }
                    }


                }


            }else if(opcion==2){
                System.out.println("\n=== INICIAR SESIÓN ===");

                System.out.print("Correo electrónico: ");
                String correo = scanner.nextLine().trim();


                System.out.print("Contraseña: ");
                System.out.print("\nPresione 1 para recuperar contraseña: \n");
                String contrasenia = scanner.nextLine().trim();
                if(contrasenia.equals("1")){
                    administradorUsuario.recuperarContrasenia(correo);

                }else{
                    if (administradorUsuario.validarUsuario(correo, contrasenia)) {
                        usuarioActual = administradorUsuario.buscarUsuario(correo);
                        System.out.println("✅ ¡Bienvenido " + correo + "!");
                        mostrarMenuJuego();
                    } else {
                        System.out.println("❌ Credenciales inválidas");
                    }
                }


            }else if(opcion==3){
                System.out.println("¡Hasta pronto!");
                return;
            }else{
                System.out.println("❌ Opción inválida");
            }
        }
    }
}