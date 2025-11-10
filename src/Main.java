package src;
import java.time.Instant;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // ========== RESET Y ESTILOS ==========
        final String RESET = "\u001B[0m";
        final String NEGRITA = "\u001B[1m";
        final String TENUE = "\u001B[2m";
        final String ITALICA = "\u001B[3m";
        final String SUBRAYADO = "\u001B[4m";
        final String PARPADEO = "\u001B[5m";
        final String INVERTIDO = "\u001B[7m";
        final String OCULTO = "\u001B[8m";
        final String TACHADO = "\u001B[9m";

        // ========== COLORES DE TEXTO ==========
        final String NEGRO = "\u001B[30m";
        final String GRIS = "\u001B[90m";
        final String ROJO = "\u001B[91m";
        final String VERDE = "\u001B[92m";
        final String AMARILLO = "\u001B[93m";
        final String AZUL = "\u001B[94m";
        final String MAGENTA = "\u001B[95m";
        final String CYAN = "\u001B[96m";
        final String BLANCO = "\u001B[97m";

        // ========== COLORES DE FONDO ==========
        final String FONDO_NEGRO = "\u001B[40m";
        final String FONDO_GRIS = "\u001B[100m";
        final String FONDO_ROJO = "\u001B[101m";
        final String FONDO_VERDE = "\u001B[102m";
        final String FONDO_AMARILLO = "\u001B[103m";
        final String FONDO_AZUL = "\u001B[104m";
        final String FONDO_MAGENTA = "\u001B[105m";
        final String FONDO_CYAN = "\u001B[106m";
        final String FONDO_BLANCO = "\u001B[107m";

        Scanner scanner = new Scanner(System.in);

        ArrayList<Usuario> usuarios = new ArrayList<>();
        AdministradorUsuario administradorUsuario = new AdministradorUsuario(usuarios);

        // Luego crear GestorJSON pasando el administrador
        GestorJSON gestorJSON = new GestorJSON(administradorUsuario);
        gestorJSON.crearArchivoJson();

        // Cargar usuarios existentes del JSON
        try {
            usuarios = new ArrayList<>(gestorJSON.cargarTodosLosUsuarios());
            administradorUsuario.setUsuarios(usuarios);
            System.out.println("✅ Usuarios cargados: " + usuarios.size());
        } catch (IOException e) {
            System.out.println("❌ Error cargando usuarios: " + e.getMessage());
        }

        Usuario usuario = null;

        System.out.println(AZUL + NEGRITA + "===================================");
        System.out.println("|      INICIO DEL JUEGO         |");
        System.out.println("===================================" + RESET);
        System.out.println("| " + VERDE + "1. Iniciar sesión              " + AZUL + NEGRITA + "|");
        System.out.println("| " + CYAN + "2. Registrar usuario           " + AZUL + NEGRITA + "|");
        System.out.println("===================================" + RESET);
        System.out.print("Seleccione una opción: ");

        int opcion;
        opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (opcion == 1) {
            System.out.println("\n🔐 INICIANDO SESIÓN");
            System.out.print("Ingrese correo: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese contraseña: ");
            System.out.println("(Marque 1 para recuperar su contraseña)");
            String contrasenia = scanner.nextLine();

            if (contrasenia.equals("1")){
                administradorUsuario.recuperarContrasenia(correo);
                System.out.println("Volviendo al menú principal...");
                return;
            } else {
                if (administradorUsuario.iniciarSesion(correo, contrasenia)){
                    usuario = administradorUsuario.buscarUsuario(correo);
                    if (usuario != null) {
                        System.out.println("✅ Sesión iniciada correctamente");
                    }
                } else {
                    System.out.println("❌ Usuario no encontrado o contraseña incorrecta");
                    return;
                }
            }

        } else if (opcion == 2) {
            System.out.println("\n📝 REGISTRAR USUARIO");
            System.out.print("Ingrese correo: ");
            String correo = scanner.nextLine();
            System.out.print("Ingrese contraseña: ");
            String contrasenia = scanner.nextLine();
            System.out.print("Repita su contraseña: ");
            String contrasenia2 = scanner.nextLine();

            if (contrasenia.equals(contrasenia2)){
                if (administradorUsuario.buscarCorreo(correo)){
                    System.out.println("⚠️ Correo ya existe");
                    System.out.print("¿Quiere recuperar su contraseña? \n1. Sí \n2. No\nSeleccione: ");
                    int opcion2 = scanner.nextInt();
                    scanner.nextLine();
                    if (opcion2 == 1){
                        administradorUsuario.recuperarContrasenia(correo);
                    }
                    return;
                } else {
                    administradorUsuario.registrar(correo, contrasenia);
                    usuario = administradorUsuario.buscarUsuario(correo);
                    try {
                        gestorJSON.guardarEstadoCompleto(usuario);
                        System.out.println("✅ Usuario registrado y guardado exitosamente");
                    } catch (IOException e) {
                        System.out.println("⚠️ Error guardando usuario: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("❌ Las contraseñas no coinciden");
                return;
            }
        }

        if (usuario != null){
            System.out.println("\n🎮 Bienvenido " + administradorUsuario.obtenerCorreoDescifrado(usuario));

            boolean enMenuPrincipal = true;
            while (enMenuPrincipal) {
                System.out.println(AZUL + NEGRITA + "===================================");
                System.out.println("|          MENÚ PRINCIPAL         |");
                System.out.println("===================================");
                System.out.println("| " + MAGENTA + "1. Jugar laberinto nuevo        " + AZUL + NEGRITA + "|");
                System.out.println("| " + AMARILLO + "2. Jugar laberinto guardado     " + AZUL + NEGRITA + "|");
                System.out.println("| " + CYAN + "3. Ver estadísticas           " + AZUL + NEGRITA + "|");
                System.out.println("| " + VERDE + "4. Mostrar archivo JSON       " + AZUL + NEGRITA + "|");
                System.out.println("| " + ROJO + "5. Salir                    " + AZUL + NEGRITA + "|");
                System.out.println("===================================" + RESET);
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                if (opcion == 1){
                    System.out.println("\n🎯 JUGAR NUEVO LABERINTO");
                    System.out.print("Ingrese el tamaño del laberinto (6 o más): ");
                    int tamanio = scanner.nextInt();
                    scanner.nextLine();

                    if (tamanio < 6){
                        System.out.println("❌ Error: El laberinto debe ser de un tamaño igual o mayor a 6");
                        continue;
                    }

                    Instant tiempoInicio = Instant.now();

                    Laberinto laberinto = new Laberinto(tamanio);
                    Jugador jugador = new Jugador(laberinto.obtenerPosicionInicial());

                    Partida nuevaPartida = new Partida(laberinto, jugador, tiempoInicio, null, null);
                    usuario.setPartida(nuevaPartida);

                    // ✅ NUEVO: GUARDAR INMEDIATAMENTE LA NUEVA PARTIDA
                    try {
                        gestorJSON.guardarEstadoCompleto(usuario);
                        System.out.println("✅ Nueva partida creada y guardada exitosamente");
                    } catch (IOException e) {
                        System.out.println("⚠️ Error guardando nueva partida: " + e.getMessage());
                    }

                    System.out.println("✅ Laberinto creado exitosamente");
                    laberinto.mostrarLaberintoPrincipal(jugador.getPosicion());

                    jugarPartida(usuario, laberinto, jugador, tiempoInicio, gestorJSON, administradorUsuario, scanner);

                } else if (opcion == 2) {
                    // JUGAR LABERINTO GUARDADO
                    try {
                        Usuario usuarioConPartida = gestorJSON.cargarPartidaUsuario(
                                administradorUsuario.obtenerCorreoDescifrado(usuario));

                        if (usuarioConPartida != null && usuarioConPartida.getPartida() != null) {
                            Partida partidaGuardada = usuarioConPartida.getPartida();
                            Laberinto laberinto = partidaGuardada.getLaberinto();
                            Jugador jugador = partidaGuardada.getJugador();
                            Instant tiempoInicio = partidaGuardada.getTiempoInicio();

                            // ✅ NUEVO: REPARAR EL LABERINTO ANTES DE USARLO
                            if (laberinto != null) {
                                laberinto.repararPosiciones();
                                laberinto.reiniciarEstado();
                            }

                            // VERIFICAR QUE EL LABERINTO SE CARGÓ CORRECTAMENTE
                            if (laberinto == null || jugador == null || jugador.getPosicion() == null) {
                                System.out.println("❌ Error: Partida guardada corrupta o incompleta");

                                // DEBUG: Mostrar qué está fallando
                                if (laberinto == null) System.out.println(" - Laberinto es null");
                                if (jugador == null) System.out.println(" - Jugador es null");
                                if (jugador != null && jugador.getPosicion() == null)
                                    System.out.println(" - Posición del jugador es null");

                                continue;
                            }

                            // ✅ NUEVO: VERIFICAR POSICIONES DEL LABERINTO
                            if (laberinto.obtenerPosicionInicial() == null || laberinto.obtenerPosicionFinal() == null) {
                                System.out.println("⚠️ Advertencia: Posiciones del laberinto no encontradas, reparando...");
                                laberinto.repararPosiciones();
                            }

                            System.out.println("🎮 PARTIDA CARGADA - Continuando desde posición guardada");
                            System.out.println("Posición actual: (" + jugador.getPosicion().getX() +
                                    ", " + jugador.getPosicion().getY() + ")");
                            System.out.println("Vida: " + jugador.getPuntosDeVida());
                            System.out.println("Cristales: " + jugador.getCristalesRecolectados());

                            laberinto.mostrarLaberinto(jugador.getPosicion(), jugador);

                            jugarPartida(usuario, laberinto, jugador, tiempoInicio, gestorJSON, administradorUsuario, scanner);

                        } else {
                            System.out.println("❌ No tienes una partida guardada.");
                        }
                    } catch (IOException e) {
                        System.out.println("❌ Error cargando partida: " + e.getMessage());
                    }

                } else if (opcion == 3) {
                    System.out.println("\n📊 ESTADÍSTICAS");
                    if (usuario.getEstadisticas() != null && !usuario.getEstadisticas().isEmpty()) {
                        for (int i = 0; i < usuario.getEstadisticas().size(); i++) {
                            System.out.println("--- Partida #" + (i + 1) + " ---");
                            usuario.getEstadisticas().get(i).mostrarEstadistica();
                            System.out.println();
                        }
                    } else {
                        System.out.println("No hay estadísticas registradas.");
                    }

                } else if (opcion == 4) {
                    // MOSTRAR ARCHIVO JSON
                    gestorJSON.mostrarArchivoJson();

                } else if (opcion == 5) {
                    System.out.println("👋 ¡Hasta pronto!");
                    enMenuPrincipal = false;
                } else {
                    System.out.println("❌ Opción inválida");
                }
            }
        }
    }

    /**
     * MÉTODO PARA MANEJAR EL JUEGO DE UNA PARTIDA
     */
    /**
     * MÉTODO PARA MANEJAR EL JUEGO DE UNA PARTIDA
     */
    private static void jugarPartida(Usuario usuario, Laberinto laberinto, Jugador jugador,
                                     Instant tiempoInicio, GestorJSON gestorJSON,
                                     AdministradorUsuario administradorUsuario, Scanner scanner) {

        boolean jugando = true;
        boolean partidaGanada = false;

        // Obtener la partida actual y reanudar tiempo
        Partida partidaActual = usuario.getPartida();
        if (partidaActual != null) {
            partidaActual.reanudarTiempo();
        } else {
            // Si no existe partida, crear una nueva
            partidaActual = new Partida(laberinto, jugador, tiempoInicio, null, null);
            usuario.setPartida(partidaActual);
        }

        // VERIFICAR POSICIONES ANTES DE COMENZAR
        if (laberinto.obtenerPosicionFinal() == null) {
            System.out.println("⚠️ Reparando posiciones del laberinto...");
            laberinto.repararPosiciones();
        }

        while(jugando && jugador.sigueVivo()) {
            System.out.println("\n🎮 CONTROLES:");
            System.out.println("W = Mover Arriba");
            System.out.println("S = Mover Abajo");
            System.out.println("D = Mover Derecha");
            System.out.println("A = Mover Izquierda");
            System.out.println("X = Salir y Guardar");
            System.out.println("-----------------------------------");
            System.out.println("Vida: " + jugador.getPuntosDeVida() + " | Cristales: " + jugador.getCristalesRecolectados());
            System.out.println("Llave: " + (jugador.isObtuvoLlave() ? "✅" : "❌"));

            // Mostrar tiempo transcurrido
            Duration tiempoTranscurrido = partidaActual.obtenerTiempoTranscurrido();
            System.out.println("⏱️  Tiempo: " +
                    tiempoTranscurrido.toMinutes() + "m " +
                    (tiempoTranscurrido.getSeconds() % 60) + "s");

            System.out.print("Ingrese movimiento: ");

            String opcionMovimiento = scanner.nextLine();

            if (opcionMovimiento == null || opcionMovimiento.trim().isEmpty()) {
                System.out.println("❌ Error: Ingrese un comando válido");
                continue;
            }

            char movimiento = opcionMovimiento.trim().toUpperCase().charAt(0);

            if (movimiento == 'W') {
                jugador.moverseArriba(jugador, laberinto);
            } else if (movimiento == 'S') {
                jugador.moverseAbajo(jugador, laberinto);
            } else if (movimiento == 'D') {
                jugador.moverseDerecha(jugador, laberinto);
            } else if (movimiento == 'A') {
                jugador.moverseIzquierda(jugador, laberinto);
            } else if (movimiento == 'X') {
                System.out.println("💾 Saliendo y guardando partida...");

                // PAUSAR EL TIEMPO ANTES DE SALIR
                partidaActual.pausarTiempo();
                break;

            } else {
                System.out.println("❌ Movimiento inválido. Use W, A, S, D");
                continue;
            }

            // VERIFICACIÓN SEGURA DE POSICIÓN FINAL
            Posicion posFinal = laberinto.obtenerPosicionFinal();
            if (posFinal != null) {
                // VERIFICAR SI LLEGÓ A LA META
                if (jugador.getPosicion().getX() == posFinal.getX() &&
                        jugador.getPosicion().getY() == posFinal.getY()) {

                    if (jugador.isObtuvoLlave()) {
                        System.out.println("🎉 ¡FELICIDADES! HAS GANADO LA PARTIDA");
                        partidaGanada = true;
                        jugando = false;
                    } else {
                        System.out.println("⚠️ Has llegado a la meta pero necesitas la llave!");
                    }
                }
            } else {
                System.out.println("⚠️ Advertencia: No se pudo determinar la posición final del laberinto");
            }

            // MOSTRAR ESTADO ACTUAL
            laberinto.mostrarLaberinto(jugador.getPosicion(), jugador);

            // VERIFICAR SI PERDIÓ
            if (!jugador.sigueVivo()) {
                System.out.println("💀 ¡HAS PERDIDO! Te quedaste sin vida");
                jugando = false;
            }

            // ========== GUARDADO EN TIEMPO REAL ==========
            usuario.setPartida(partidaActual);

            // VERIFICAR ANTES DE GUARDAR
            if (usuario.getPartida() == null || usuario.getPartida().getLaberinto() == null) {
                System.out.println("⚠️ Advertencia: Problema al preparar datos para guardar");
            } else {
                try {
                    // PAUSAR TEMPORALMENTE PARA GUARDAR
                    partidaActual.pausarTiempo();
                    gestorJSON.guardarEstadoCompleto(usuario);
                    partidaActual.reanudarTiempo(); // Reanudar después de guardar

                    System.out.println("💾 Progreso guardado automáticamente");

                } catch (IOException e) {
                    System.out.println("⚠️ No se pudo guardar el progreso: " + e.getMessage());
                }
            }
        }

        // SI LA PARTIDA TERMINÓ (GANÓ O PERDIÓ)
        if (!jugando) {
            // FINALIZAR PARTIDA Y OBTENER TIEMPO FINAL
            Duration tiempoTotal = partidaActual.finalizarPartida();
            Instant tiempoFinal = Instant.now();

            if (partidaGanada || !jugador.sigueVivo()) {
                // ✅ CORREGIDO: Pasar el tiempo jugado real al constructor
                Estadistica estadistica = new Estadistica(
                        tiempoInicio,
                        tiempoFinal,
                        laberinto.getTamanio(),
                        jugador.getCristalesRecolectados(),
                        jugador.getPuntosDeVida(),
                        jugador.getTrampasActivadas(),
                        tiempoTotal  // ✅ Este es el tiempo real jugado (con pausas)
                );

                // GUARDAR ESTADÍSTICA Y LIMPIAR PARTIDA ACTUAL
                try {
                    gestorJSON.guardarEstadistica(usuario, estadistica);
                    usuario.setPartida(null); // Limpiar partida actual (ya terminó)
                    gestorJSON.guardarEstadoCompleto(usuario);

                    System.out.println("\n📊 ESTADÍSTICAS FINALES:");
                    // Mostrar tiempo total correcto
                    long minutos = tiempoTotal.toMinutes();
                    long segundos = tiempoTotal.getSeconds() % 60;
                    System.out.println("⏱️  Tiempo total jugado: " + minutos + " minutos " + segundos + " segundos");
                    estadistica.mostrarEstadistica();

                } catch (IOException e) {
                    System.out.println("⚠️ Error guardando estadística: " + e.getMessage());
                }
            }
        }
    }
}