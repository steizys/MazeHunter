package src;
import java.util.ArrayList;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class AdministradorDeEstadisticas {
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

    /**
     * ACTUALIZADO: Solo agrega la estadística al usuario
     * El guardado en JSON lo hace el Main a través de GestorJSON
     */
    public void agregarEstadistica(Usuario usuario, Estadistica nuevaEstadistica) {
        if (usuario.getEstadisticas() == null) {
            usuario.setEstadisticas(new ArrayList<>());
        }
        usuario.getEstadisticas().add(nuevaEstadistica);
        System.out.println("📊 Estadística agregada al usuario.");
    }

    /**
     * MÉTODO COMPATIBLE: Mantiene el nombre original pero sin guardar en JSON
     */
    public void guardarEstadisticas(Usuario usuario, Estadistica nuevaEstadistica) {
        agregarEstadistica(usuario, nuevaEstadistica);
        System.out.println("✅ Estadística preparada para guardar (use GestorJSON para guardar en archivo)");
    }

    public void mostrarEstadisticas(Usuario usuario) {
        if (usuario.getEstadisticas() == null || usuario.getEstadisticas().isEmpty()) {
            System.out.println("No hay estadísticas para mostrar.");
            return;
        }

        System.out.println("=== ESTADÍSTICAS ===");
        for (int i = 0; i < usuario.getEstadisticas().size(); i++) {
            System.out.println("Partida #" + (i + 1) + ":");
            usuario.getEstadisticas().get(i).mostrarEstadistica();
            System.out.println("-------------------");
        }
    }

    /**
     * NUEVO: Método para obtener estadísticas resumidas
     */
    public void mostrarResumenEstadisticas(Usuario usuario) {
        if (usuario.getEstadisticas() == null || usuario.getEstadisticas().isEmpty()) {
            System.out.println("No hay estadísticas registradas.");
            return;
        }

        System.out.println("=== RESUMEN ESTADÍSTICAS ===");
        System.out.println("Total de partidas: " + usuario.getEstadisticas().size());

        int totalCristales = 0;
        int totalTrampas = 0;
        Duration tiempoTotal = Duration.ZERO;

        for (Estadistica stats : usuario.getEstadisticas()) {
            totalCristales += stats.getCristalesRecolectados();
            totalTrampas += stats.getTrampasActivadas();
            if (stats.getTiempoInicio() != null && stats.getTiempoFinal() != null) {
                tiempoTotal = tiempoTotal.plus(Duration.between(stats.getTiempoInicio(), stats.getTiempoFinal()));
            }
        }

        System.out.println("Cristales totales: " + totalCristales);
        System.out.println("Trampas totales: " + totalTrampas);
        System.out.println("Tiempo total jugado: " +
                tiempoTotal.toHours() + "h " +
                (tiempoTotal.toMinutes() % 60) + "m " +
                (tiempoTotal.getSeconds() % 60) + "s");
    }
}