package src;

import java.util.ArrayList;

public class AdministradorDeEstadisticas implements GuardarJson {
    private ArrayList<Estadistica> estadisticas;

    public AdministradorDeEstadisticas(ArrayList<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public ArrayList<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(ArrayList<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void guardarEstadisticas( ){};
    public void mostrarEstadisticas( ){};
    public void crearArchivoJson(){};
    public void mostrarArchivoJson(){};
}
