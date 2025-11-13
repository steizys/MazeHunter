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
import java.io.IOException;

/**
 * Clase encargada de gestionar los usuarios del sistema.
 *
 * <p>Esta clase proporciona funcionalidades para:</p>
 * <ul>
 *   <li>Registrar nuevos usuarios</li>
 *   <li>Validar credenciales de inicio de sesión</li>
 *   <li>Recuperar contraseñas</li>
 *   <li>Cifrar y descifrar datos sensibles usando AES</li>
 *   <li>Validar formato de correo electrónico y contraseñas</li>
 * </ul>
 *
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 * @see Usuario
 * @see GestorJSON
 */
public class AdministradorUsuario {

    private ArrayList<Usuario> usuarios;
    private SecretKey claveSecreta;

    /**
     * Clave fija utilizada para el cifrado AES (16 caracteres exactos).
     */
    private static final String CLAVE_FIJA_BASE64 = "A1B2C3D4E5F6G7H8"; // 16 caracteres exactos

    /**
     * Genera una clave fija para cifrado AES basada en la constante definida.
     *
     * @return SecretKey configurada para AES
     * @throws RuntimeException Si ocurre un error durante la generación de la clave
     */
    private SecretKey generarClaveFija() {
        try {
            // Usar siempre la misma clave
            byte[] claveBytes = CLAVE_FIJA_BASE64.getBytes("UTF-8");
            return new SecretKeySpec(claveBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error creando clave fija", e);
        }
    }

    /**
     * Constructor principal que inicializa el administrador de usuarios.
     *
     * @param usuarios Lista de usuarios existentes en el sistema
     */
    public AdministradorUsuario(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
        try {
            this.claveSecreta = generarClaveFija();
        } catch (Exception e) {
            System.err.println("Error al inicializar: " + e.getMessage());
        }
    }

    /**
     * Getters and Setters.
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Verifica si una cadena contiene al menos un carácter en mayúscula.
     *
     * @param texto Texto a verificar
     * @return true si contiene al menos una mayúscula, false en caso contrario
     */
    private boolean contieneMayuscula(String texto) {
        for (char c : texto.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si una cadena contiene al menos un carácter especial.
     * Los caracteres especiales considerados son: !@#$%^&*()_+-=[]{};':"|,.<>/?
     *
     * @param texto Texto a verificar
     * @return true si contiene al menos un carácter especial, false en caso contrario
     */
    private boolean contieneCaracterEspecial(String texto) {
        String caracteresEspeciales = "!@#$%^&*()_+-=[]{};':\"|,.<>/?";
        for (char c : texto.toCharArray()) {
            if (caracteresEspeciales.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca si un correo electrónico ya está registrado en el sistema.
     *
     * @param correo Correo electrónico a buscar (en texto plano)
     * @return true si el correo existe, false en caso contrario
     */
    public boolean buscarCorreo(String correo) {
        for (Usuario usuario : usuarios) {
            try {
                String correoDescifrado = descifrar(usuario.getCorreo());
                if (correo.equals(correoDescifrado)) {
                    return true;
                }
            } catch (Exception e) {
                System.err.println("Error descifrando correo para usuario: " + e.getMessage());
                continue;
            }
        }
        return false;
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo electrónico del usuario a buscar (en texto plano)
     * @return El objeto Usuario si se encuentra, null en caso contrario
     */
    public Usuario buscarUsuario(String correo) {
        for (Usuario usuario : usuarios) {
            try {
                String correoDescifrado = descifrar(usuario.getCorreo());
                if (correo.equals(correoDescifrado)) {
                    return usuario;
                }
            } catch (Exception e) {
                System.err.println("Error descifrando correo para usuario: " + e.getMessage());
                continue;
            }
        }
        return null;
    }

    /**
     * Valida las credenciales de un usuario (correo y contraseña).
     *
     * @param correo Correo electrónico del usuario (texto plano)
     * @param contrasenia Contraseña del usuario (texto plano)
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validarUsuario(String correo, String contrasenia) {
        for (Usuario usuario : usuarios) {
            try {
                String correoAlmacenadoDescifrado = descifrar(usuario.getCorreo());  // Descifrar lo almacenado y comparar con lo ingresado
                String contraseniaAlmacenadaDescifrada = descifrar(usuario.getContrasenia());

                if (correoAlmacenadoDescifrado.equals(correo) &&
                        contraseniaAlmacenadaDescifrada.equals(contrasenia)) {
                    return true;
                }
            } catch (Exception e) {
                System.err.println("Error validando usuario: " + e.getMessage());
                continue;
            }
        }
        return false;
    }

    /**
     * Valida el formato de un correo electrónico.
     *
     * @param correo Correo electrónico a validar
     * @return true si el formato es válido, false en caso contrario
     */
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

    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad:
     * <ul>
     *   <li>Longitud mínima de 6 caracteres</li>
     *   <li>Al menos una letra mayúscula</li>
     *   <li>Al menos un carácter especial</li>
     * </ul>
     *
     * @param contrasenia Contraseña a validar
     * @return true si la contraseña cumple con todos los requisitos, false en caso contrario
     */
    public boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.isEmpty()) {
            return false;
        }
        if (contrasenia.length() < 6) {
            return false;
        }
        if (!contieneMayuscula(contrasenia)) {
            return false;
        }
        if (!contieneCaracterEspecial(contrasenia)) {
            return false;
        }
        return true;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Realiza las siguientes validaciones:</p>
     * <ul>
     *   <li>Correo no vacío y formato válido</li>
     *   <li>Contraseña no vacía y cumple requisitos de seguridad</li>
     *   <li>Correo no duplicado</li>
     * </ul>
     *
     * @param correo Correo electrónico del nuevo usuario
     * @param contrasenia Contraseña del nuevo usuario
     * @return El objeto Usuario creado si el registro es exitoso, null en caso de error
     */
    public Usuario registrar(String correo, String contrasenia) {
        if (correo == null || correo.trim().isEmpty()) {
            System.out.println(" Error: El correo electrónico no puede estar vacío");
            return null;
        }
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            System.out.println(" Error: La contraseña no puede estar vacía");
            return null;
        }
        if (!validarCorreo(correo)) {
            System.out.println(" Error: Correo electrónico no válido");
            return null;
        }
        if (!validarContrasenia(contrasenia)) {
            System.out.println(" Error: Contraseña no válida. Debe tener al menos 6 caracteres, una mayúscula y un carácter especial");
            return null;
        }
        if (buscarCorreo(correo)) {
            System.out.println(" Error: El correo ya está registrado");
            return null;
        }
        Laberinto laberinto = null;
        Jugador jugador = null;
        Partida partida = null;
        ArrayList<Estadistica> estadisticas = new ArrayList<>();

        // CIFRAR antes de guardar
        String correoCifrado = cifrar(correo);
        String contraseniaCifrada = cifrar(contrasenia);

        Usuario usuario = new Usuario(correoCifrado, contraseniaCifrada, partida, estadisticas);
        this.usuarios.add(usuario);

        System.out.println(" Usuario registrado exitosamente.");
        return usuario;

    }

    /**
     * Inicia sesión de un usuario con sus credenciales.
     *
     * @param correo Correo electrónico del usuario
     * @param contrasenia Contraseña del usuario
     * @return true si el inicio de sesión es exitoso, false en caso contrario
     */
    public boolean iniciarSesion(String correo, String contrasenia) {
        if (validarUsuario(correo, contrasenia)) {
            System.out.println(" BIENVENIDO ");
            return true;
        } else {
            System.out.println(" Usuario invalido ");
            return false;
        }
    }

    /**
     * Cifra un texto usando el algoritmo AES en modo ECB.
     *
     * @param texto Texto en plano a cifrar
     * @return Texto cifrado en formato Base64
     * @throws RuntimeException Si ocurre un error durante el cifrado
     */
    public String cifrar(String texto) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);
            byte[] textoCifrado = cipher.doFinal(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(textoCifrado);
        } catch (Exception e) {
            throw new RuntimeException("Error cifrando datos", e);
        }
    }

    /**
     * Descifra un texto previamente cifrado con AES.
     *
     * @param textoCifrado Texto cifrado en formato Base64
     * @return Texto descifrado en plano
     * @throws RuntimeException Si ocurre un error durante el descifrado
     */
    public String descifrar(String textoCifrado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, claveSecreta);
            byte[] textoPlano = cipher.doFinal(Base64.getDecoder().decode(textoCifrado));
            return new String(textoPlano, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando datos", e);
        }
    }

    /**
     * Obtiene el correo electrónico descifrado de un usuario.
     *
     * @param usuario Usuario del cual obtener el correo
     * @return Correo electrónico en texto plano
     */
    public String obtenerCorreoDescifrado(Usuario usuario) {
        return descifrar(usuario.getCorreo());
    }

    /**
     * Obtiene la contraseña descifrada de un usuario.
     *
     * @param usuario Usuario del cual obtener la contraseña
     * @return Contraseña en texto plano
     */
    public String obtenerContraseniaDescifrada(Usuario usuario) {
        return descifrar(usuario.getContrasenia());
    }

    /**
     * Recupera y muestra la contraseña de un usuario dado su correo electrónico.
     *
     * @param correo Correo electrónico del usuario que solicita la recuperación
     */
    public void recuperarContrasenia(String correo) {
        Usuario usuario = buscarUsuario(correo);
        if (usuario != null) {
            String contraseniaDescifrada = obtenerContraseniaDescifrada(usuario);
            System.out.println("La contraseña de su correo es: " + contraseniaDescifrada);
        } else {
            System.out.println("Error: Correo no encontrado en el sistema");
        }
    }

}