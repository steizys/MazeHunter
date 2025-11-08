package src;
import java.time.Instant;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Laberinto laberinto ;
        ArrayList<Usuario> usuarios = new ArrayList<>(); //Creado bajo la informacion del JSON
        AdministradorUsuario administradorUsuario=new AdministradorUsuario(usuarios);
        Usuario usuario=null;
        administradorUsuario.registrar("amelie@gmail.com", "AmelieMoreno24*");;




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
            System.out.println("La informacion del usuario resgistrado es " + administradorUsuario.obtenerCorreoDescifrado(usuario)+ "  "+ administradorUsuario.obtenerContraseniaDescifrada(usuario));
            for (Usuario us : usuarios) {
                System.out.println(us.getCorreo()+ "  "+ us.getContrasenia());

            }
        }


    }
}

