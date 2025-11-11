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
        final String ROJO = "\u001B[38;5;196m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";
        final String AZUL = "\u001B[38;5;39m";
        final String MAGENTA = "\u001B[38;5;201m";
        final String CYAN = "\u001B[38;5;51m";
        final String NARANJA = "\u001B[38;5;214m";
        final String ROSA = "\u001B[38;5;213m";
        final String GRIS = "\u001B[38;5;245m";


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
            System.out.println(VERDE + "✅ Usuarios cargados: " + usuarios.size() + RESET);
        } catch (IOException e) {
            System.out.println(ROJO + "❌ Error cargando usuarios: " + e.getMessage() + RESET);
        }

        Usuario usuario = null;
        boolean salirPrograma = false;

        while (!salirPrograma) {
            System.out.println(AZUL + NEGRITA + "\n╔═══════════════════════════════╗");
            System.out.println("║" + CYAN + "          MAZE HUNTER          " + AZUL + "║");
            System.out.println("╠═══════════════════════════════╣");
            System.out.println("║                               ║");
            System.out.println("║  " + VERDE + "1. Iniciar sesión            " + AZUL + "║");
            System.out.println("║  " + CYAN + "2. Registrar usuario         " + AZUL + "║");
            System.out.println("║  " + ROJO + "3. Salir del programa        " + AZUL + "║");
            System.out.println("║                               ║");
            System.out.println("╚═══════════════════════════════╝" + RESET);
            System.out.print(NARANJA + "🎯 Seleccione una opción: " + RESET);

            int opcion = -1;
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println(ROJO + "\n⚠️  Error: Debe ingresar un número válido" + RESET);
                scanner.nextLine(); // Limpiar buffer en caso de error
                continue;
            }

            if (opcion == 1) {
                boolean intentarLogin = true;
                while (intentarLogin) {
                    System.out.println(MAGENTA + NEGRITA + "\n┌──────────────────────────────────────────┐");
                    System.out.println("│               INICIAR SESIÓN             │");
                    System.out.println("└──────────────────────────────────────────┘" + RESET);
                    System.out.print(CYAN + "📧 Ingrese correo: " + RESET);
                    String correo = scanner.nextLine().trim().toLowerCase();

                    if (correo.isEmpty()) {
                        System.out.println(ROJO + "❌ Error: El correo no puede estar vacío" + RESET);
                        continue;
                    }

                    System.out.print(CYAN + "🔒 Ingrese contraseña: " + RESET);
                    System.out.println(AMARILLO + "(Escriba '1' para recuperar contraseña)" + RESET);
                    String contrasenia = scanner.nextLine();

                    if (contrasenia.equals("1")) {
                        String correoRecuperacion = correo.toLowerCase().trim();
                        administradorUsuario.recuperarContrasenia(correoRecuperacion);
                        System.out.print(AMARILLO + "¿Desea intentar iniciar sesión nuevamente? (s/n): " + RESET);
                        String respuesta = scanner.nextLine().toLowerCase();
                        if (!respuesta.equals("s")) {
                            intentarLogin = false;
                        }
                    } else {
                        if (administradorUsuario.iniciarSesion(correo, contrasenia)) {
                            usuario = administradorUsuario.buscarUsuario(correo);
                            if (usuario != null) {
                                System.out.println(VERDE + "✅ Sesión iniciada correctamente" + RESET);
                                intentarLogin = false;
                                menuPrincipal(usuario, administradorUsuario, gestorJSON, scanner);
                            }
                        } else {
                            System.out.println(ROJO + "❌ Usuario no encontrado o contraseña incorrecta" + RESET);
                            System.out.print(AMARILLO + "¿Desea intentar nuevamente? (s/n): " + RESET);
                            String respuesta = scanner.nextLine().toLowerCase();
                            if (!respuesta.equals("s")) {
                                intentarLogin = false;
                            }
                        }
                    }
                }

            } else if (opcion == 2) {
                boolean intentarRegistro = true;
                while (intentarRegistro) {
                    System.out.println(MAGENTA + NEGRITA + "\n┌──────────────────────────────────────────┐");
                    System.out.println("│              REGISTRAR USUARIO           │");
                    System.out.println("└──────────────────────────────────────────┘" + RESET);
                    System.out.print(CYAN + "📧 Ingrese correo: " + RESET);
                    String correo = scanner.nextLine().trim().toLowerCase();

                    if (correo.isEmpty()) {
                        System.out.println(ROJO + "❌ Error: El correo no puede estar vacío" + RESET);
                        continue;
                    }

                    System.out.print(CYAN + "🔒 Ingrese contraseña: " + RESET);
                    String contrasenia = scanner.nextLine();
                    System.out.print(CYAN + "🔁 Repita su contraseña: " + RESET);
                    String contrasenia2 = scanner.nextLine();

                    if (!contrasenia.equals(contrasenia2)) {
                        System.out.println(ROJO + "❌ Las contraseñas no coinciden" + RESET);
                        System.out.print(AMARILLO + "¿Desea intentar nuevamente? (s/n): " + RESET);
                        String respuesta = scanner.nextLine().toLowerCase();
                        if (!respuesta.equals("s")) {
                            intentarRegistro = false;
                        }
                        continue;
                    }

                    if (administradorUsuario.buscarCorreo(correo)) {
                        System.out.println(AMARILLO + "⚠️ Correo ya existe" + RESET);
                        System.out.print("¿Quiere recuperar su contraseña? \n" +
                                VERDE + "1. Sí \n" +
                                ROJO + "2. No\n" +
                                CYAN + "Seleccione: " + RESET);
                        try {
                            int opcion2 = scanner.nextInt();
                            scanner.nextLine();
                            if (opcion2 == 1) {
                                administradorUsuario.recuperarContrasenia(correo);
                            }
                        } catch (Exception e) {
                            System.out.println(ROJO + "❌ Opción inválida" + RESET);
                            scanner.nextLine();
                        }
                        System.out.print(AMARILLO + "¿Desea intentar con otro correo? (s/n): " + RESET);
                        String respuesta = scanner.nextLine().toLowerCase();
                        if (!respuesta.equals("s")) {
                            intentarRegistro = false;
                        }
                    } else {
                        Usuario nuevoUsuario = administradorUsuario.registrar(correo, contrasenia);
                        if (nuevoUsuario != null) {
                            usuario = nuevoUsuario;
                            try {
                                gestorJSON.guardarEstadoCompleto(usuario);
                                System.out.println(VERDE + "✅ Usuario registrado y guardado exitosamente" + RESET);
                                System.out.println(CYAN + "✨ Bienvenido a Maze Game, " + usuario.getCorreo() + "!" + RESET);
                                intentarRegistro = false;
                                menuPrincipal(usuario, administradorUsuario, gestorJSON, scanner);
                            } catch (IOException e) {
                                System.out.println(ROJO + "⚠️ Error guardando usuario: " + e.getMessage() + RESET);
                            }
                        } else {
                            System.out.print(AMARILLO + "¿Desea intentar el registro nuevamente? (s/n): " + RESET);
                            String respuesta = scanner.nextLine().toLowerCase();
                            if (!respuesta.equals("s")) {
                                intentarRegistro = false;
                            }
                        }
                    }
                }

            } else if (opcion == 3) {
                System.out.println(VERDE + "\n✨ ¡Gracias por jugar! ¡Hasta pronto! 👋" + RESET);
                salirPrograma = true;
            } else {
                System.out.println(ROJO + "❌ Opción inválida. Por favor seleccione 1, 2 o 3." + RESET);
            }
        }
        scanner.close();
    }

    /**
     * MENÚ PRINCIPAL DEL JUEGO
     */
    private static void menuPrincipal(Usuario usuario, AdministradorUsuario administradorUsuario,
                                      GestorJSON gestorJSON, Scanner scanner) {

        final String RESET = "\u001B[0m";
        final String NEGRITA = "\u001B[1m";
        final String ROJO = "\u001B[38;5;196m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";
        final String AZUL = "\u001B[38;5;39m";
        final String MAGENTA = "\u001B[38;5;201m";
        final String CYAN = "\u001B[38;5;51m";
        final String NARANJA = "\u001B[38;5;214m";

        boolean enMenuPrincipal = true;
        while (enMenuPrincipal) {
            System.out.println(AZUL + NEGRITA + "\n╔══════════════════════════════════════════╗");
            System.out.println("║" + CYAN + "           MENÚ PRINCIPAL              " + AZUL + "║");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║                                          ║");
            System.out.println("║  " + MAGENTA + "1. Nuevo laberinto                 " + AZUL + "║");
            System.out.println("║  " + AMARILLO + "2. Continuar partida              " + AZUL + "║");
            System.out.println("║  " + CYAN + "3. Mis estadísticas               " + AZUL + "║");
            System.out.println("║  " + VERDE + "4. Ver archivo JSON               " + AZUL + "║");
            System.out.println("║  " + NARANJA + "5. Cerrar sesión                 " + AZUL + "║");
            System.out.println("║  " + ROJO + "6. Salir del programa              " + AZUL + "║");
            System.out.println("║                                          ║");
            System.out.println("╚══════════════════════════════════════════╝" + RESET);
            System.out.print(NARANJA + "🎯 Seleccione una opción: " + RESET);

            int opcion = -1;
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println(ROJO + "❌ Error: Debe ingresar un número válido" + RESET);
                scanner.nextLine(); // Limpiar buffer en caso de error
                continue;
            }

            switch (opcion) {
                case 1:
                    jugarNuevoLaberinto(usuario, administradorUsuario, gestorJSON, scanner);
                    break;
                case 2:
                    jugarLaberintoGuardado(usuario, administradorUsuario, gestorJSON, scanner);
                    break;
                case 3:
                    verEstadisticas(usuario);
                    break;
                case 4:
                    gestorJSON.mostrarArchivoJson();
                    break;
                case 5:
                    System.out.println(VERDE + "\n👋 Sesión cerrada correctamente" + RESET);
                    enMenuPrincipal = false;
                    break;
                case 6:
                    System.out.println(VERDE + "\n✨ ¡Gracias por jugar! ¡Hasta pronto! 👋" + RESET);
                    enMenuPrincipal = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println(ROJO + "\n❌ Opción inválida. Por favor seleccione 1-6." + RESET);
            }
        }
    }

    /**
     * JUGAR NUEVO LABERINTO CON VALIDACIONES
     */
    private static void jugarNuevoLaberinto(Usuario usuario, AdministradorUsuario administradorUsuario,
                                            GestorJSON gestorJSON, Scanner scanner) {

        final String NEGRITA = "\u001B[1m";
        final String RESET = "\u001B[0m";
        final String ROJO = "\u001B[38;5;196m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";
        final String CYAN = "\u001B[38;5;51m";

        boolean seleccionandoTamanio = true;
        int tamanio = 0;

        while (seleccionandoTamanio) {
            System.out.println(CYAN + NEGRITA + "\n┌──────────────────────────────────────────┐");
            System.out.println("│              NUEVO LABERINTO             │");
            System.out.println("└──────────────────────────────────────────┘" + RESET);
            System.out.print(AMARILLO + "📏 Ingrese el tamaño del laberinto (6 o más): " + RESET);

            try {
                tamanio = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                if (tamanio < 6) {
                    System.out.println(ROJO + "\n❌ El laberinto debe ser de tamaño 6 o mayor" + RESET);
                    System.out.print(AMARILLO + "¿Desea intentar con otro tamaño? (s/n): " + RESET);
                    String respuesta = scanner.nextLine().toLowerCase();
                    if (!respuesta.equals("s")) {
                        return;
                    }
                } else {
                    seleccionandoTamanio = false;
                }
            } catch (Exception e) {
                System.out.println(ROJO + "\n❌ Error: Debe ingresar un número válido" + RESET);
                scanner.nextLine(); // Limpiar buffer en caso de error
                System.out.print(AMARILLO + "¿Desea intentar nuevamente? (s/n): " + RESET);
                String respuesta = scanner.nextLine().toLowerCase();
                if (!respuesta.equals("s")) {
                    return;
                }
            }
        }

        Instant tiempoInicio = Instant.now();
        Laberinto laberinto = new Laberinto(tamanio);
        Jugador jugador = new Jugador(laberinto.obtenerPosicionInicial());

        Partida nuevaPartida = new Partida(laberinto, jugador, tiempoInicio, null, null);
        nuevaPartida.iniciarPartida(); // ✅ INICIAR CORRECTAMENTE LA PARTIDA
        usuario.setPartida(nuevaPartida);

        // ✅ GUARDAR INMEDIATAMENTE LA NUEVA PARTIDA
        try {
            gestorJSON.guardarEstadoCompleto(usuario);
            System.out.println(VERDE + "\n✅ Partida creada y guardada exitosamente" + RESET);
        } catch (IOException e) {
            System.out.println(ROJO + "⚠️  Error guardando nueva partida: " + e.getMessage() + RESET);
        }

        System.out.println(VERDE + "✅ Laberinto creado exitosamente" + RESET);
        laberinto.mostrarLaberintoPrincipal(jugador.getPosicion());

        jugarPartida(usuario, laberinto, jugador, tiempoInicio, gestorJSON, administradorUsuario, scanner);
    }

    /**
     * JUGAR LABERINTO GUARDADO CON VALIDACIONES
     */
    private static void jugarLaberintoGuardado(Usuario usuario, AdministradorUsuario administradorUsuario,
                                               GestorJSON gestorJSON, Scanner scanner) {

        final String NEGRITA = "\u001B[1m";
        final String RESET = "\u001B[0m";
        final String ROJO = "\u001B[38;5;196m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";
        final String CYAN = "\u001B[38;5;51m";

        try {
            System.out.println(CYAN + NEGRITA + "\n┌──────────────────────────────────────────┐");
            System.out.println("│             CARGANDO PARTIDA             │");
            System.out.println("└──────────────────────────────────────────┘" + RESET);

            Usuario usuarioConPartida = gestorJSON.cargarPartidaUsuario(
                    administradorUsuario.obtenerCorreoDescifrado(usuario));

            if (usuarioConPartida != null && usuarioConPartida.getPartida() != null) {
                Partida partidaGuardada = usuarioConPartida.getPartida();
                Laberinto laberinto = partidaGuardada.getLaberinto();
                Jugador jugador = partidaGuardada.getJugador();
                Instant tiempoInicio = partidaGuardada.getTiempoInicio();

                // ✅ REPARAR EL LABERINTO ANTES DE USARLO
                if (laberinto != null) {
                    laberinto.repararPosiciones();
                    laberinto.reiniciarEstado();
                }

                // VERIFICAR QUE EL LABERINTO SE CARGÓ CORRECTAMENTE
                if (laberinto == null || jugador == null || jugador.getPosicion() == null) {
                    System.out.println(ROJO + "❌ Error: Partida guardada corrupta o incompleta" + RESET);
                    return;
                }

                // ✅ VERIFICAR POSICIONES DEL LABERINTO
                if (laberinto.obtenerPosicionInicial() == null || laberinto.obtenerPosicionFinal() == null) {
                    System.out.println(AMARILLO + "⚠️ Advertencia: Posiciones del laberinto no encontradas, reparando...");
                    laberinto.repararPosiciones();
                }

                // ✅ REANUDAR LA PARTIDA CARGADA
                partidaGuardada.reanudarTiempo();

                System.out.println(VERDE + "🎮 ¡Partida cargada exitosamente!" + RESET);
                System.out.println(CYAN + "📍 Posición actual: (" + jugador.getPosicion().getX() +
                        ", " + jugador.getPosicion().getY() + ")");

                System.out.println("❤️  Vida: " + jugador.getPuntosDeVida());
                System.out.println("💎 Cristales: " + jugador.getCristalesRecolectados());
                System.out.println("🔑 Llave: " + (jugador.isObtuvoLlave() ? "Sí" : "No") + RESET);

                laberinto.mostrarLaberinto(jugador.getPosicion(), jugador);

                jugarPartida(usuario, laberinto, jugador, tiempoInicio, gestorJSON, administradorUsuario, scanner);

            } else {
                System.out.println(ROJO + "❌ No tienes una partida guardada." + RESET);
                System.out.println(AMARILLO + "💡 Inicia un nuevo laberinto para comenzar a jugar!" + RESET);
            }
        } catch (IOException e) {
            System.out.println(ROJO + "❌ Error cargando partida: " + e.getMessage() + RESET);
        }
    }

    /**
     * VER ESTADÍSTICAS CON VALIDACIONES
     */
    private static void verEstadisticas(Usuario usuario) {
        final String NEGRITA = "\u001B[1m";
        final String RESET = "\u001B[0m";
        final String CYAN = "\u001B[38;5;51m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";

        System.out.println(CYAN + NEGRITA + "\n┌──────────────────────────────────────────┐");
        System.out.println("│               MIS ESTADÍSTICAS           │");
        System.out.println("└──────────────────────────────────────────┘" + RESET);

        if (usuario.getEstadisticas() != null && !usuario.getEstadisticas().isEmpty()) {
            for (int i = 0; i < usuario.getEstadisticas().size(); i++) {
                System.out.println(AMARILLO + "\n═══════════ PARTIDA #" + (i + 1) + " ═══════════" + RESET);
                usuario.getEstadisticas().get(i).mostrarEstadistica();
                System.out.println();
            }
        } else {
            System.out.println(AMARILLO + "📝 Aún no hay estadísticas registradas." + RESET);
            System.out.println("🎯 ¡Juega algunas partidas para ver tus estadísticas aquí!" + RESET);
        }
    }

    /**
     * MÉTODO PARA MANEJAR EL JUEGO DE UNA PARTIDA CON VALIDACIONES MEJORADAS
     */
    private static void jugarPartida(Usuario usuario, Laberinto laberinto, Jugador jugador,
                                     Instant tiempoInicio, GestorJSON gestorJSON,
                                     AdministradorUsuario administradorUsuario, Scanner scanner) {

        final String NEGRITA = "\u001B[1m";
        final String RESET = "\u001B[0m";
        final String ROJO = "\u001B[38;5;196m";
        final String VERDE = "\u001B[38;5;46m";
        final String AMARILLO = "\u001B[38;5;226m";
        final String CYAN = "\u001B[38;5;51m";
        final String MAGENTA = "\u001B[38;5;201m";

        boolean jugando = true;
        boolean partidaGanada = false;

        // Obtener la partida actual
        Partida partidaActual = usuario.getPartida();
        if (partidaActual != null && !partidaActual.isPartidaActiva()) {
            partidaActual.reanudarTiempo(); // Solo reanudar si estaba pausada
        }

        while(jugando && jugador.sigueVivo()) {
            System.out.println(CYAN + NEGRITA + "\n┌──────────────────────────────────────────┐");
            System.out.println("│                  CONTROLES               │");
            System.out.println("├──────────────────────────────────────────┤" + RESET);
            System.out.println(CYAN + "   W = ⬆️  Mover Arriba");
            System.out.println("   S = ⬇️  Mover Abajo");
            System.out.println("   D = ➡️  Mover Derecha");
            System.out.println("   A = ⬅️  Mover Izquierda");
            System.out.println("   X = 💾 Salir y Guardar");
            System.out.println("────────────────────────────────────────────" + RESET);

            // Panel de estado del jugador
            System.out.println(MAGENTA + "❤️   Vida: " + jugador.getPuntosDeVida() +
                    "  |  " + "💎 Cristales: " + jugador.getCristalesRecolectados() +
                    "  |  " + "🔑 Llave: " + (jugador.isObtuvoLlave() ? "✅" : "❌") + RESET);

            // Mostrar tiempo transcurrido
            Duration tiempoTranscurrido = partidaActual.obtenerTiempoTranscurrido();
            System.out.println(AMARILLO + "⏱️  Tiempo: " +
                    tiempoTranscurrido.toMinutes() + "m " +
                    (tiempoTranscurrido.getSeconds() % 60) + "s" + RESET);

            System.out.print(VERDE + "🎯 Ingrese movimiento: " + RESET);

            String opcionMovimiento = scanner.nextLine();

            if (opcionMovimiento == null || opcionMovimiento.trim().isEmpty()) {
                System.out.println(ROJO + "❌ Error: Ingrese un comando válido" + RESET);
                continue;
            }

            char movimiento = opcionMovimiento.trim().toUpperCase().charAt(0);

            // Validar movimiento
            if (movimiento != 'W' && movimiento != 'S' && movimiento != 'D' && movimiento != 'A' && movimiento != 'X') {
                System.out.println(ROJO + "❌ Movimiento inválido. Use W, A, S, D o X para salir" + RESET);
                continue;
            }

            if (movimiento == 'W') {
                // Validar límites antes de mover
                if (jugador.getPosicion().getX() > 0) {
                    jugador.moverseArriba(jugador, laberinto);
                } else {
                    System.out.println(AMARILLO + "🚫 No puedes moverte fuera del laberinto" + RESET);
                }
            } else if (movimiento == 'S') {
                // Validar límites antes de mover
                if (jugador.getPosicion().getX() < laberinto.getTamanio() - 1) {
                    jugador.moverseAbajo(jugador, laberinto);
                } else {
                    System.out.println(AMARILLO + "🚫 No puedes moverte fuera del laberinto" + RESET);
                }
            } else if (movimiento == 'D') {
                // Validar límites antes de mover
                if (jugador.getPosicion().getY() < laberinto.getTamanio() - 1) {
                    jugador.moverseDerecha(jugador, laberinto);
                } else {
                    System.out.println(AMARILLO + "🚫 No puedes moverte fuera del laberinto" + RESET);
                }
            } else if (movimiento == 'A') {
                // Validar límites antes de mover
                if (jugador.getPosicion().getY() > 0) {
                    jugador.moverseIzquierda(jugador, laberinto);
                } else {
                    System.out.println(AMARILLO + "🚫 No puedes moverte fuera del laberinto" + RESET);
                }
            } else if (movimiento == 'X') {
                System.out.println(AMARILLO + "\n💾 Guardando partida..." + RESET);
                partidaActual.pausarTiempo(); // ✅ Pausar correctamente

                // Confirmar salida
                System.out.print(AMARILLO + "¿Está seguro de que desea salir? (s/n): " + RESET);
                String confirmacion = scanner.nextLine().toLowerCase();
                if (confirmacion.equals("s")) {
                    break;
                } else {
                    System.out.println(VERDE + "🔄 Continuando con la partida..." + RESET);
                    partidaActual.reanudarTiempo(); // Reanudar si cancela
                    continue;
                }
            }

            // VERIFICACIÓN SEGURA DE POSICIÓN FINAL
            Posicion posFinal = laberinto.obtenerPosicionFinal();
            if (posFinal != null) {
                // VERIFICAR SI LLEGÓ A LA META
                if (jugador.getPosicion().getX() == posFinal.getX() &&
                        jugador.getPosicion().getY() == posFinal.getY()) {

                    if (jugador.isObtuvoLlave()) {
                        System.out.println(VERDE + NEGRITA + "\n🎉 ✨ ¡FELICIDADES! ¡HAS GANADO LA PARTIDA! ✨ 🎉" + RESET);
                        partidaGanada = true;
                        jugando = false;
                    } else {
                        System.out.println(AMARILLO + "⚠️ Has llegado a la meta pero necesitas la llave!" + RESET);
                    }
                }
            } else {
                System.out.println(ROJO + "⚠️ Advertencia: No se pudo determinar la posición final del laberinto" + RESET);
            }

            // MOSTRAR ESTADO ACTUAL
            laberinto.mostrarLaberinto(jugador.getPosicion(), jugador);

            // VERIFICAR SI PERDIÓ
            if (!jugador.sigueVivo()) {
                System.out.println(ROJO + NEGRITA + "\n💀 ¡HAS PERDIDO! Te quedaste sin vida" + RESET);
                jugando = false;
            }

            // ========== GUARDADO EN TIEMPO REAL ==========
            usuario.setPartida(partidaActual);

            // VERIFICAR ANTES DE GUARDAR
            if (usuario.getPartida() == null || usuario.getPartida().getLaberinto() == null) {
                System.out.println(ROJO + "⚠️ Advertencia: Problema al preparar datos para guardar" + RESET);
            } else {
                try {
                    // PAUSAR TEMPORALMENTE PARA GUARDAR
                    partidaActual.pausarTiempo();
                    gestorJSON.guardarEstadoCompleto(usuario);
                    partidaActual.reanudarTiempo(); // Reanudar después de guardar

                    System.out.println(VERDE + "💾 Progreso guardado automáticamente" + RESET);

                } catch (IOException e) {
                    System.out.println(ROJO + "⚠️ No se pudo guardar el progreso: " + e.getMessage() + RESET);
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
                        partidaActual.getTiempoInicio(), // Tiempo real de inicio
                        tiempoFinal,                     // Tiempo real de finalización
                        laberinto.getTamanio(),
                        jugador.getCristalesRecolectados(),
                        jugador.getPuntosDeVida(),
                        jugador.getTrampasActivadas(),
                        tiempoTotal  // ✅ Este es el tiempo real jugado (con pausas descontadas)
                );

                // GUARDAR ESTADÍSTICA Y LIMPIAR PARTIDA ACTUAL
                try {
                    gestorJSON.guardarEstadistica(usuario, estadistica);
                    usuario.setPartida(null); // Limpiar partida actual (ya terminó)
                    gestorJSON.guardarEstadoCompleto(usuario);

                    System.out.println(CYAN + NEGRITA + "\n┌──────────────────────────────────────────┐");
                    System.out.println("│             ESTADÍSTICAS FINALES         │");
                    System.out.println("└──────────────────────────────────────────┘" + RESET);
                    // Mostrar tiempo total correcto
                    long minutos = tiempoTotal.toMinutes();
                    long segundos = tiempoTotal.getSeconds() % 60;
                    System.out.println("⏱️  Tiempo total jugado: " + minutos + " minutos " + segundos + " segundos");
                    estadistica.mostrarEstadistica();

                } catch (IOException e) {
                    System.out.println(ROJO + "⚠️ Error guardando estadística: " + e.getMessage() + RESET);
                }
            }
        }
    }
}