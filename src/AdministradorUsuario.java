package src;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;
import java.time.Instant;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;


public class AdministradorUsuario implements GuardarJson{
    private ArrayList<Usuario> usuarios;

    private SecretKey claveSecreta;


    public AdministradorUsuario(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
        try {
            this.claveSecreta = generarClaveSecreta();
        } catch (RuntimeException e) {
            // Manejar si la generación de clave falla, aunque no debería
            System.err.println("Error crítico al inicializar la clave de cifrado: " + e.getMessage());
            // Puedes decidir si abortar o continuar con un valor nulo, pero lo ideal es fallar rápido
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    private boolean contieneMayuscula(String texto) {
        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean contieneCaracterEspecial(String texto) {
        String caracteresEspeciales = "!@#$%^&*()_+-=[]{};':\"|,.<>/?";

        for (char c : texto.toCharArray()) {
            if (caracteresEspeciales.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }


    public boolean buscarCorreo(String correo) {
        for (Usuario usuario : usuarios) {
            if  (correo.equals(usuario.getCorreo())) {
                return true;
            }
        }
        return false;
    }

    public Usuario buscarUsuario(String correo) {
        for (Usuario usuario : usuarios) {
            if  (correo.equals(obtenerCorreoDescifrado(usuario))) {
                return usuario;
            }
        }
        return null;
    }
    public boolean validarUsuario(String correo, String contrasenia) {
        for (Usuario usuario : usuarios) {
            // Cifrar las credenciales ingresadas y comparar con las almacenadas
            String correoCifradoIngresado = cifrar(correo);
            String contraseniaCifradaIngresada = cifrar(contrasenia);

            if (usuario.getCorreo().equals(correoCifradoIngresado) &&
                    usuario.getContrasenia().equals(contraseniaCifradaIngresada)) {
                return true;
            }
        }
        return false;
    }

    public boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }

        try {
            InternetAddress correoAddr = new InternetAddress(correo.trim());
            correoAddr.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.isEmpty()) {
            return false;
        }

        // Verificar longitud mínima
        if (contrasenia.length() < 6) {
            return false;
        }

        // Verificar que tenga al menos una mayúscula
        if (!contieneMayuscula(contrasenia)) {
            return false;
        }

        // Verificar que tenga al menos un carácter especial
        if (!contieneCaracterEspecial(contrasenia)) {
            return false;
        }

        return true;
    }

    public void registrar(String correo, String contrasenia ) {
        Laberinto laberinto=null;
        Jugador jugador=null;
        Partida partida =null;
        ArrayList<Estadistica> estadisticas =null; //Creado bajo la informacion del JSON
        Usuario usuario= new Usuario(cifrar(correo),cifrar(contrasenia), partida,estadisticas);
        this.usuarios.add(usuario);
    }

    public boolean iniciarSesion(String correo, String contrasenia) {
        if (validarUsuario(correo, contrasenia)){
            System.out.println(" BIENVENIDO ");
            return true;
        }else{
            System.out.println(" Usuario invalido ");
            return false;
        }
    }
    public void autenticar(String correo, String contrasenia ) {

    }

    public void recuperarContrasenia(String correo ) {
        Usuario usuario = buscarUsuario(correo);
        System.out.println(" La contraseña de su correo es "+ obtenerContraseniaDescifrada(usuario));

    }


    private SecretKey generarClaveSecreta() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generando clave AES", e);
        }
    }

    public String cifrar(String contrasenia) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);
            byte[] textoCifrado = cipher.doFinal(contrasenia.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(textoCifrado);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando datos", e);
        }
    }

    public String descifrar(String contraseniaCifrada) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, claveSecreta);
            byte[] textoPlano = cipher.doFinal(Base64.getDecoder().decode(contraseniaCifrada));
            return new String(textoPlano, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando datos", e);
        }
    }

    public String obtenerCorreoDescifrado(Usuario usuario) {
        return descifrar(usuario.getCorreo());
    }
    public String obtenerContraseniaDescifrada(Usuario usuario) {
        return descifrar(usuario.getContrasenia());
    }
    public void crearArchivoJson(){};
    public void mostrarArchivoJson(){};
}
