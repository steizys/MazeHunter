package src;
import java.time.Instant;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;


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

        AdministradorUsuario administradorUsuario = new AdministradorUsuario(new ArrayList<>());


        Usuario usuario = null;

        // Registrar usuario (se cifrará automáticamente)
        administradorUsuario.registrar("amelie@gmail.com", "AmelieMoreno24*");

        System.out.println("Inicio del juego");
        System.out.println("1. Iniciar sesion");
        System.out.println("2. Registrar usuario");

        int opcion;
        opcion = scanner.nextInt();

        if (opcion == 1) {
            System.out.println("Iniciando usuario");
            System.out.println("Ingrese correo: ");
            String correo = scanner.next();
            System.out.println("Ingrese contrasenia: ");
            System.out.println("Marque 1 para recuperar su contrasenia" );
            String contrasenia = scanner.next();
            if (contrasenia.equals("1")){
                administradorUsuario.recuperarContrasenia(correo);
            }else{
                if (administradorUsuario.iniciarSesion(correo, contrasenia)){
                    usuario=administradorUsuario.buscarUsuario(correo);
                }else{
                    System.out.println(" Usuario no encontrado ");
                }
            }

        }else if (opcion == 2) {
            System.out.println("Registrar usuario");
            System.out.println("Ingrese correo: ");
            String correo = scanner.next();
            System.out.println("Ingrese contrasenia: ");
            String contrasenia = scanner.next();
            System.out.println("Repita su contrasenia: ");
            String contrasenia2 = scanner.next();
            if (contrasenia.equals(contrasenia2)){
                if (administradorUsuario.buscarCorreo(correo)){
                    System.out.println("Correo ya existe");
                    System.out.println(" Quiere registrar su contrasenia? \n1. Si \n2. No");
                    int opcion2 = scanner.nextInt();
                    if (opcion2 == 1){
                        administradorUsuario.recuperarContrasenia(correo);
                    }
                }else {
                    administradorUsuario.registrar(correo, contrasenia);
                    usuario=administradorUsuario.buscarUsuario(correo);
                }
            }
        }

        if (usuario!=null){
            System.out.println("Iniciando juego");
            System.out.println("Bienvenido " + administradorUsuario.obtenerCorreoDescifrado(usuario)+ "  "+ administradorUsuario.obtenerContraseniaDescifrada(usuario));
            System.out.println(AZUL + NEGRITA + "-----------------------------------");
            System.out.println("|          MENÚ PRINCIPAL         |");
            System.out.println("-----------------------------------");
            System.out.println("| " + MAGENTA +  "1. Jugar laberinto nuevo             " + AZUL + NEGRITA + "|");
            System.out.println("| " + AMARILLO + "2. Jugar laberinto guardado               " + AZUL + NEGRITA + "|");
            System.out.println("| " + CYAN +     "3. Ver estadísticas  " + AZUL + NEGRITA + "|");
            System.out.println("| " + ROJO +     "4. Salir                       " + AZUL + NEGRITA + "|");
            System.out.println("-----------------------------------" + RESET);
            opcion = scanner.nextInt();

            if (opcion == 1){
                System.out.println("      JUGAR NUEVO LABERINTO         ");
                System.out.println("\n Ingrese el tamaño del laberinto (6 - ...):  ");
                int tamanio = scanner.nextInt();
                if  (tamanio < 6){
                    System.out.println("\n Error: El laberinto debe ser de un tamaño igual o mayor a 6 ");
                }

                Instant tiempoInicio = Instant.now();

                Laberinto laberinto= new Laberinto(tamanio);
                Jugador jugador=new Jugador(laberinto.obtenerPosicionInicial());
                laberinto.mostrarLaberintoPrincipal(jugador.getPosicion());

                char movimiento = ' ';

                while(movimiento != 'X' && !laberinto.getMatrizJuegoPosicion(laberinto.obtenerPosicionFinal().getX(), laberinto.obtenerPosicionFinal().getY()).isVisitada()){
                    System.out.println("W = Mover Arriba");
                    System.out.println("S = Mover Abajo");
                    System.out.println("D = Mover Derecha");
                    System.out.println("A = Mover Izquierda");
                    System.out.println("X = Salir");
                    String opcion2 = scanner.nextLine();

                    if (opcion2 == null || opcion2.trim().isEmpty()) {
                        System.out.println(" Error: Ingrese un comando válido");
                        continue;
                    }

                    movimiento = opcion2.trim().charAt(0);
                    movimiento = Character.toUpperCase(movimiento);

                    if (movimiento == 'W') {
                        jugador.moverseArriba(jugador, laberinto);
                    } else if (movimiento == 'S') {
                        jugador.moverseAbajo(jugador, laberinto);
                    } else if (movimiento == 'D') {
                        jugador.moverseDerecha(jugador, laberinto);
                    } else if (movimiento == 'A') {
                        jugador.moverseIzquierda(jugador, laberinto);
                    } else if (movimiento == 'X') {
                    } else {
                        System.out.println(" Movimiento inválido. Use W, A, S, D");
                    }
                    laberinto.mostrarLaberinto(jugador.getPosicion(), jugador);

                }

                Instant tiempoFinal = Instant.now();

                Estadistica estadistica = new Estadistica(tiempoInicio, tiempoFinal, tamanio, jugador.getCristalesRecolectados(), jugador.getPuntosDeVida(), jugador.getTrampasActivadas());
                ArrayList<Estadistica> estadisticas = new ArrayList<>();
                estadistica.mostrarEstadistica();

            }
        }
    }
}

