package src;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Clase abstracta que representa una celda en el laberinto
 *
 * <p>Configuración Jackson para serialización polimórfica:</p>
 * <ul>
 *   <li>Usa propiedad "tipo" para identificar subtipos</li>
 *   <li>Define 8 tipos de celdas diferentes</li>
 * </ul>
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CaminoLibre.class, name = "camino"),
        @JsonSubTypes.Type(value = Muro.class, name = "pared"),
        @JsonSubTypes.Type(value = Trampa.class, name = "trampa"),
        @JsonSubTypes.Type(value = Cristal.class, name = "cristal"),
        @JsonSubTypes.Type(value = Inicio.class, name = "inicio"),
        @JsonSubTypes.Type(value = Llave.class, name = "llave"),
        @JsonSubTypes.Type(value = VidaExtra.class, name = "vidaextra"),
        @JsonSubTypes.Type(value = Meta.class, name = "meta")
})

public abstract class Celda {
    private boolean transitable;
    private boolean visitada;
    private String representacion;

    /**
     * Comportamiento cuando el jugador pisa la celda
     * @param jugador El jugador que entra en la celda
     */
    public abstract void comportamiento(Jugador jugador);

    /**
     * Constructor vacío para Jackson
     */
    public Celda() {
    }

    /**
     * Constructor completo para celdas
     * @param representacion Símbolo visual de la celda
     * @param visitada Si ya fue visitada
     * @param transitable Si el jugador puede pasar
     */
    public Celda(String representacion, boolean visitada, boolean transitable) {
        this.representacion = representacion;
        this.visitada = visitada;
        this.transitable = transitable;
    }

    /**
     * Verifica si la celda es transitable por el jugador.
     *
     * <p>Determina si el jugador puede moverse a través de esta celda durante el juego.</p>
     *
     * @return true si la celda permite el paso del jugador, false en caso contrario
     */
    public boolean isTransitable() {
        return transitable;
    }

    /**
     * Establece la transitabilidad de la celda.
     *
     * <p>Permite modificar dinámicamente si la celda puede ser atravesada o no.</p>
     *
     * @param transitable true para hacer la celda transitable, false para bloquear el paso
     */
    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }

    /**
     * Verifica si la celda ha sido visitada por el jugador.
     *
     * @return true si la celda ha sido visitada al menos una vez, false en caso contrario
     */
    public boolean isVisitada() {
        return visitada;
    }

    /**
     * Establece el estado de visita de la celda.
     *
     * <p>Se debe llamar este método cuando el jugador entra por primera vez en la celda
     * para marcarla como visitada. Esto permite llevar un registro de las áreas
     * exploradas del laberinto.</p>
     *
     * @param visitada true para marcar la celda como visitada, false para resetear su estado
     */
    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }

    /**
     * Obtiene la representación visual de la celda.
     *
     * <p>La representación es un símbolo que identificar visualmente el
     * tipo de celda. Cada subtipo de celda tiene su propio símbolo característico:</p>
     * <ul>
     *   <li>Camino libre: "."</li>
     *   <li>Muro: "#"</li>
     *   <li>Trampa: "T"</li>
     *   <li>Cristal: "C"</li>
     *   <li>Inicio: "I"</li>
     *   <li>Llave: "L"</li>
     *   <li>Vida extra: "V"</li>
     *   <li>Meta: "M"</li>
     * </ul>
     *
     * @return String que representa el símbolo visual de la celda
     */
    public String getRepresentacion() {
        return representacion;
    }

    /**
     * Establece la representación visual de la celda.
     *
     * <p>Permite cambiar dinámicamente la apariencia visual de la celda.<p>
     *
     * @param representacion Nuevo símbolo o carácter para representar la celda visualmente
     */
    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }
}