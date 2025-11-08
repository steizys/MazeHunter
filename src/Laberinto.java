package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Laberinto {
    private final int tamanio;
    private Celda[][] matrizJuego;
    private Random random;

    public Laberinto(int tamanio) {
        // Asegurar tamaño impar para que funcione bien el algoritmo
        this.tamanio = (tamanio % 2 == 0) ? tamanio + 1 : tamanio;
        this.matrizJuego = new Celda[this.tamanio][this.tamanio];
        this.random = new Random();
        crearLaberinto();
    }

    public int getTamanio() {
        return tamanio;
    }

    public Celda[][] getMatrizJuego() {
        return matrizJuego;
    }

    public void setMatrizJuego(Celda[][] matrizJuego) {
        this.matrizJuego = matrizJuego;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Celda[][] crearLaberinto() {
        // 1. Inicializar la matriz totalmente con muros
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                matrizJuego[i][j] = new Muro();
            }
        }

        // 2. Generar punto de inicio en una posicion aletaoria
        int inicioX = random.nextInt((tamanio - 2) / 2) * 2 + 1;
        int inicioY = random.nextInt((tamanio - 2) / 2) * 2 + 1;

        matrizJuego[inicioX][inicioY] = new Inicio();
        matrizJuego[inicioX][inicioY].setVisitada(true);

        // 3. Generar el laberinto
        generarLaberinto(inicioX, inicioY);

        // 4. Generar punto de meta en una posicion aletaoria
        int metaX, metaY;
        do {
            metaX = random.nextInt((tamanio - 2) / 2) * 2 + 1;
            metaY = random.nextInt((tamanio - 2) / 2) * 2 + 1;
        } while ((metaX == inicioX && metaY == inicioY) ||
                !(matrizJuego[metaX][metaY] instanceof CaminoLibre));

        matrizJuego[metaX][metaY] = new Meta();

        // 5. Agregar elementos especiales
        agregarElementosEspeciales();

        return matrizJuego;
    }

    private void generarLaberinto(int x, int y) {
        // Direcciones posibles: arriba, derecha, abajo, izquierda
        int[][] direcciones = {{-2, 0}, {0, 2}, {2, 0}, {0, -2}};

        // Mezclar direcciones aleatoriamente
        List<int[]> dirList = new ArrayList<>();
        for (int[] dir : direcciones) {
            dirList.add(dir);
        }
        Collections.shuffle(dirList, random);

        // Explorar en cada dirección
        for (int[] dir : dirList) {
            int nuevoX = x + dir[0];
            int nuevoY = y + dir[1];

            // Verificar si la nueva posición no sale del rango y no ha sido visitada
            if (nuevoX >= 1 && nuevoX < tamanio - 1 &&
                    nuevoY >= 1 && nuevoY < tamanio - 1 &&
                    !matrizJuego[nuevoX][nuevoY].isVisitada()) {

                // Quitar el muro entre la celda actual y la nueva
                int muroX = x + dir[0] / 2;
                int muroY = y + dir[1] / 2;

                matrizJuego[muroX][muroY] = new CaminoLibre();
                matrizJuego[nuevoX][nuevoY] = new CaminoLibre();
                matrizJuego[nuevoX][nuevoY].setVisitada(true);

                // Llamada recursiva
                generarLaberinto(nuevoX, nuevoY);
            }
        }
    }

    private void agregarElementosEspeciales() {
        // Contar caminos disponibles
        int caminosDisponibles = 0;
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (matrizJuego[i][j] instanceof CaminoLibre) {
                    caminosDisponibles++;
                }
            }
        }

        // Colocar elementos proporcionalmente
        int numTrampas = Math.max(1, caminosDisponibles / 8);
        int numVidasExtra = Math.max(1, caminosDisponibles / 12);
        int numLlaves = 1;

        colocarElemento(numTrampas, "TRAMPA");
        colocarElemento(numVidasExtra, "VIDA_EXTRA");
        colocarElemento(numLlaves, "LLAVE");
    }

    private void colocarElemento(int cantidad, String tipo) {
        int colocados = 0;
        int intentosMaximos = tamanio * tamanio;
        int intentos = 0;

        while (colocados < cantidad && intentos < intentosMaximos) {
            int x = random.nextInt(tamanio - 2) + 1; // Evitar bordes
            int y = random.nextInt(tamanio - 2) + 1;

            if (matrizJuego[x][y] instanceof CaminoLibre) {

                switch (tipo) {
                    case "TRAMPA":
                        matrizJuego[x][y] = new Trampa();
                        break;
                    case "VIDA_EXTRA":
                        matrizJuego[x][y] = new VidaExtra();
                        break;
                    case "LLAVE":
                        matrizJuego[x][y] = new Llave();
                        break;
                }
                colocados++;
            }
            intentos++;
        }
    }

    public void mostrarLaberinto() {
        System.out.println("\n=== LABERINTO " + tamanio + "x" + tamanio + " ===");
        System.out.println("I = Inicio, M = Meta, # = Muro, T = Trampa, V = Vida, L = Llave\n");

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                System.out.print(matrizJuego[i][j].getRepresentacion() + " ");
            }
            System.out.println();
        }

        // Estadísticas
        int muros = 0, caminos = 0, otros = 0;
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (matrizJuego[i][j] instanceof Muro) muros++;
                else if (matrizJuego[i][j] instanceof CaminoLibre) caminos++;
                else otros++;
            }
        }

        System.out.println("\nEstadísticas:");
        System.out.println("Muros: " + muros + " (" + (muros * 100 / (tamanio * tamanio)) + "%)");
        System.out.println("Caminos: " + caminos + " (" + (caminos * 100 / (tamanio * tamanio)) + "%)");
        System.out.println("Otros: " + otros + " (" + (otros * 100 / (tamanio * tamanio)) + "%)");
    }
}