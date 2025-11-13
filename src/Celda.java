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
     * @param jugador Jugador que activa la celda
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

    public boolean isTransitable() {
        return transitable;
    }

    public void setTransitable(boolean transitable) {
        this.transitable = transitable;
    }

    public boolean isVisitada() {
        return visitada;
    }

    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }

    public String getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }
}