package src;

public abstract class Celda {
    private boolean transitable;
    private boolean visitada;
    private String representacion;

    public abstract void comportamiento(Jugador jugador);

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
