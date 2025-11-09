package src;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

    public abstract void comportamiento(Jugador jugador);
    public Celda() {
    }
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
