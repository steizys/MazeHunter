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

public class AdministradorUsuario {

    private ArrayList<Usuario> usuarios;
    private SecretKey claveSecreta;

    // CLAVE FIJA - MISMA SIEMPRE
    private static final String CLAVE_FIJA_BASE64 = "A1B2C3D4E5F6G7H8"; // 16 caracteres exactos

    public AdministradorUsuario(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
        try {
            // SIEMPRE usar la misma clave fija
            this.claveSecreta = generarClaveFija();
        } catch (Exception e) {
            System.err.println("Error al inicializar: " + e.getMessage());
        }
    }

    // NUEVO: Constructor con GestorJSON (opcional)
    public AdministradorUsuario(ArrayList<Usuario> usuarios, GestorJSON gestorJSON) {
        this.usuarios = usuarios;
        try {
            this.claveSecreta = generarClaveFija();
        } catch (Exception e) {
            System.err.println("Error al inicializar: " + e.getMessage());
        }
    }

    // NUEVO: Método para obtener la clave (necesario para GestorJSON)
    public SecretKey getClaveSecreta() {
        return claveSecreta;
    }

    // NUEVO: Método para establecer la clave (necesario para GestorJSON)
    public void setClaveSecreta(SecretKey claveSecreta) {
        this.claveSecreta = claveSecreta;
    }

    private SecretKey generarClaveFija() {
        try {
            // Usar siempre la misma clave
            byte[] claveBytes = CLAVE_FIJA_BASE64.getBytes("UTF-8");
            return new SecretKeySpec(claveBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error creando clave fija", e);
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

    public boolean validarUsuario(String correo, String contrasenia) {
        for (Usuario usuario : usuarios) {
            try {
                // Descifrar lo almacenado y comparar con lo ingresado
                String correoAlmacenadoDescifrado = descifrar(usuario.getCorreo());
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

    public Usuario registrar(String correo, String contrasenia) {
        try {
            // Validar campos vacíos
            if (correo == null || correo.trim().isEmpty()) {
                System.out.println("❌ Error: El correo electrónico no puede estar vacío");
                return null;
            }

            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                System.out.println("❌ Error: La contraseña no puede estar vacía");
                return null;
            }

            // Validar primero
            if (!validarCorreo(correo)) {
                System.out.println("❌ Error: Correo electrónico no válido");
                return null;
            }

            if (!validarContrasenia(contrasenia)) {
                System.out.println("❌ Error: Contraseña no válida. Debe tener al menos 6 caracteres, una mayúscula y un carácter especial");
                return null;
            }

            // Verificar si el correo ya existe
            if (buscarCorreo(correo)) {
                System.out.println("❌ Error: El correo ya está registrado");
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

            System.out.println("✅ Usuario registrado exitosamente.");
            return usuario; // ← IMPORTANTE: Retornar el usuario creado

        } catch (Exception e) {
            System.out.println("❌ Error inesperado durante el registro: " + e.getMessage());
            return null;
        }
    }

    public boolean iniciarSesion(String correo, String contrasenia) {
        if (validarUsuario(correo, contrasenia)) {
            System.out.println(" BIENVENIDO ");
            return true;
        } else {
            System.out.println(" Usuario invalido ");
            return false;
        }
    }

    public void recuperarContrasenia(String correo) {
        try {
            Usuario usuario = buscarUsuario(correo);
            if (usuario != null) {
                String contraseniaDescifrada = obtenerContraseniaDescifrada(usuario);
                System.out.println("La contraseña de su correo es: " + contraseniaDescifrada);
            } else {
                System.out.println("Error: Correo no encontrado en el sistema");
            }
        } catch (Exception e) {
            System.out.println("Error recuperando contraseña: " + e.getMessage());
        }
    }

    // MÉTODOS de cifrado/descifrado (MANTENER igual):
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

    public String obtenerCorreoDescifrado(Usuario usuario) {
        return descifrar(usuario.getCorreo());
    }

    public String obtenerContraseniaDescifrada(Usuario usuario) {
        return descifrar(usuario.getContrasenia());
    }
}