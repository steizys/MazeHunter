package src;

/**
 * Clase que representa al jugador en el laberinto del juego.
 *
 * <p>El jugador es el personaje principal que se mueve por el laberinto,
 * interactúa con las celdas y recoge objetos. Implementa la interfaz
 * {@link Movimiento} para definir su comportamiento de movimiento.</p>
 *
 * <p><strong>Características del jugador:</strong></p>
 * <ul>
 *   <li>Sistema de puntos de vida</li>
 *   <li>Capacidad de recolectar cristales</li>
 *   <li>Manejo de llaves para acceder a áreas especiales</li>
 *   <li>Registro de trampas activadas</li>
 *   <li>Movimiento en cuatro direcciones</li>
 * </ul>
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 * @see Movimiento
 * @see Laberinto
 * @see Celda
 * @see Posicion
 */
public class Jugador implements Movimiento {
    private int puntosDeVida;
    private static final int maxVida = 100;
    private String representacion;
    private boolean obtuvoLlave;
    private Posicion posicion;
    private int cristalesRecolectados;
    private int trampasActivadas;

    /**
     * Constructor por defecto que inicializa un jugador con valores predeterminados.
     *
     * <p>Configuración inicial:</p>
     * <ul>
     *   <li>Puntos de vida: 100</li>
     *   <li>Representación: "@"</li>
     *   <li>Llave: false</li>
     *   <li>Cristales: 0</li>
     *   <li>Trampas: 0</li>
     * </ul>
     */
    public Jugador() {
        this.puntosDeVida = 100;
        this.representacion = "@";
        this.obtuvoLlave = false;
        this.cristalesRecolectados = 0;
        this.trampasActivadas = 0;
    }

    /**
     * Constructor que inicializa un jugador en una posición específica.
     *
     * @param posicion Posición inicial del jugador en el laberinto
     */
    public Jugador(Posicion posicion) {
        this();
        this.posicion = posicion;
    }

    /**
     * Getters and Setters
     */
    public int getPuntosDeVida() {
        return puntosDeVida;
    }
    public void setPuntosDeVida(int puntosDeVida) {
        this.puntosDeVida = puntosDeVida;
    }
    public String getRepresentacion() {
        return representacion;
    }
    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }
    public boolean isObtuvoLlave() {
        return obtuvoLlave;
    }
    public void setObtuvoLlave(boolean obtuvoLlave) {
        this.obtuvoLlave = obtuvoLlave;
    }
    public Posicion getPosicion() {
        return posicion;
    }
    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }
    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }
    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }
    public int getTrampasActivadas() {
        return trampasActivadas;
    }
    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    /**
     * Mueve al jugador hacia arriba en el laberinto.
     *
     * <p>Verifica si la celda superior es transitable y aplica el comportamiento
     * de dicha celda antes de actualizar la posición.</p>
     *
     * @param jugador Instancia del jugador que se está moviendo
     * @param laberinto Laberinto donde se realiza el movimiento
     * @implNote Implementación del método de la interfaz {@link Movimiento}
     */
    public void moverseArriba(Jugador jugador, Laberinto laberinto) {
        Celda celda = laberinto.getMatrizJuegoPosicion(this.posicion.getX()-1, this.posicion.getY());
        if (celda.isTransitable()) {
            celda.comportamiento(jugador);
            Jugador.this.posicion.setX(this.posicion.getX()-1);
        } else {
            System.out.println(" No puede mover porque es un MURO");
        }
    }

    /**
     * Mueve al jugador hacia abajo en el laberinto.
     *
     * <p>Verifica si la celda inferior es transitable y aplica el comportamiento
     * de dicha celda antes de actualizar la posición.</p>
     *
     * @param jugador Instancia del jugador que se está moviendo
     * @param laberinto Laberinto donde se realiza el movimiento
     * @implNote Implementación del método de la interfaz {@link Movimiento}
     */
    public void moverseAbajo(Jugador jugador, Laberinto laberinto) {
        Celda celda = laberinto.getMatrizJuegoPosicion(this.posicion.getX()+1, this.posicion.getY());
        if (celda.isTransitable()) {
            celda.comportamiento(jugador);
            Jugador.this.posicion.setX(this.posicion.getX()+1);
        } else {
            System.out.println(" No puede mover porque es un MURO");
        }
    }

    /**
     * Mueve al jugador hacia la derecha en el laberinto.
     *
     * <p>Verifica si la celda derecha es transitable y aplica el comportamiento
     * de dicha celda antes de actualizar la posición.</p>
     *
     * @param jugador Instancia del jugador que se está moviendo
     * @param laberinto Laberinto donde se realiza el movimiento
     * @implNote Implementación del método de la interfaz {@link Movimiento}
     */
    public void moverseDerecha(Jugador jugador, Laberinto laberinto) {
        Celda celda = laberinto.getMatrizJuegoPosicion(this.posicion.getX(), this.posicion.getY()+1);
        if (celda.isTransitable()) {
            celda.comportamiento(jugador);
            Jugador.this.posicion.setY(this.posicion.getY()+1);
        } else {
            System.out.println(" No puede mover porque es un MURO");
        }
    }

    /**
     * Mueve al jugador hacia la izquierda en el laberinto.
     *
     * <p>Verifica si la celda izquierda es transitable y aplica el comportamiento
     * de dicha celda antes de actualizar la posición.</p>
     *
     * @param jugador Instancia del jugador que se está moviendo
     * @param laberinto Laberinto donde se realiza el movimiento
     * @implNote Implementación del método de la interfaz {@link Movimiento}
     */
    public void moverseIzquierda(Jugador jugador, Laberinto laberinto) {
        Celda celda = laberinto.getMatrizJuegoPosicion(this.posicion.getX(), this.posicion.getY()-1);
        if (celda.isTransitable()) {
            celda.comportamiento(jugador);
            Jugador.this.posicion.setY(this.posicion.getY()-1);
        } else {
            System.out.println(" No puede mover porque es un MURO");
        }
    }

    /**
     * Verifica si el jugador sigue con vida.
     *
     * <p>Un jugador se considera vivo si sus puntos de vida son mayores a 0.</p>
     *
     * @return true si el jugador tiene puntos de vida mayores a 0, false en caso contrario
     * @implNote Implementación del método de la interfaz {@link Movimiento}
     */
    public boolean sigueVivo() {
        if (this.puntosDeVida <= 0) {
            return false;
        } else {
            return true;
        }
    }
}