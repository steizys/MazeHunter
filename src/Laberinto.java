package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Genera y gestiona laberintos para el juego
 * Implementa algoritmo de generación recursivo con backtracking
 *
 * @author Gabriela Cantos, Steizy Fornica, Amelie Moreno
 * @version 1.0
 */
public class Laberinto {
    private int tamanio;
    private Celda[][] matrizJuego;
    private Random random;
    private Posicion posicionInicial;
    private Posicion posicionFinal;

    /**
     * Constructor vacío para Jackson
     */
    public Laberinto() {

    }

    /**
     * Constructor que crea laberinto del tamaño especificado
     * @param tamanio Tamaño del laberinto (se ajusta a impar si es par)
     */
    public Laberinto(int tamanio) {
        // Asegurar tamaño impar para que funcione bien el algoritmo
        this.tamanio = tamanio;
        this.matrizJuego = new Celda[this.tamanio][this.tamanio];
        this.random = new Random();
        crearLaberinto();
    }

    /**
     * Getters and Setters
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }
    public void setMatrizJuego(Celda[][] matrizJuego) {
        this.matrizJuego = matrizJuego;
    }
    public void setPosicionInicial(Posicion posicionInicial) {
        this.posicionInicial = posicionInicial;
    }
    public void setPosicionFinal(Posicion posicionFinal) {
        this.posicionFinal = posicionFinal;
    }
    public int getTamanio() {
        return tamanio;
    }
    public Celda[][] getMatrizJuego() {
        return matrizJuego;
    }
    public Celda getMatrizJuegoPosicion(int x, int y) {
        return matrizJuego[x][y];
    }

    /**
     * Coloca elementos especiales en el laberinto
     * @param cantidad Número de elementos a colocar
     * @param tipo Tipo de elemento: "TRAMPA", "VIDA_EXTRA", "LLAVE", "CRISTAL"
     */
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
                    case "CRISTAL":
                        matrizJuego[x][y] = new Cristal();
                        break;
                }
                colocados++;
            }
            intentos++;
        }
    }

    /**
     * Agrega elementos especiales proporcionalmente al tamaño del laberinto
     */
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
        int numTrampas = Math.max(1, caminosDisponibles / 5);
        int numVidasExtra = Math.max(1, caminosDisponibles / 10);
        int numCristal = Math.max(1, caminosDisponibles / 15);
        int numLlaves = 1;

        colocarElemento(numTrampas, "TRAMPA");
        colocarElemento(numVidasExtra, "VIDA_EXTRA");
        colocarElemento(numLlaves, "LLAVE");
        colocarElemento(numCristal, "CRISTAL");
    }

    /**
     * Genera el laberinto usando algoritmo recursivo con backtracking
     * @param x Posición X actual
     * @param y Posición Y actual
     */
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

    /**
     * Crea un nuevo laberinto con inicio, meta y elementos especiales
     * @return Matriz de celdas del laberinto generado
     */
    public Celda[][] crearLaberinto() {
        // Inicializar la matriz totalmente con muros
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                matrizJuego[i][j] = new Muro();
            }
        }

        // Generar punto de inicio en una posicion aletaoria
        int inicioX = random.nextInt((tamanio - 2) / 2) * 2 + 1;
        int inicioY = random.nextInt((tamanio - 2) / 2) * 2 + 1;

        posicionInicial = new Posicion(inicioX, inicioY);

        matrizJuego[inicioX][inicioY] = new Inicio();
        matrizJuego[inicioX][inicioY].setVisitada(true);

        // Generar el laberinto
        generarLaberinto(inicioX, inicioY);

        // Generar punto de meta en una posicion aletaoria
        int metaX, metaY;
        do {
            metaX = random.nextInt((tamanio - 2) / 2) * 2 + 1;
            metaY = random.nextInt((tamanio - 2) / 2) * 2 + 1;
        } while ((metaX == inicioX && metaY == inicioY) ||
                !(matrizJuego[metaX][metaY] instanceof CaminoLibre));

        matrizJuego[metaX][metaY] = new Meta();
        posicionFinal = new Posicion(metaX, metaY);

        if (posicionFinal != null) {
            matrizJuego[posicionFinal.getX()][posicionFinal.getY()].setVisitada(false);
        }

        // Agregar elementos especiales
        agregarElementosEspeciales();

        return matrizJuego;
    }

    public Posicion obtenerPosicionInicial(){
        return posicionInicial;
    }

    public Posicion obtenerPosicionFinal(){
        return posicionFinal;
    }

    /**
     * Muestra el laberinto en consola con la posición del jugador
     * @param posicion Posición actual del jugador
     */
    public void mostrarLaberinto(Posicion posicion) {
        System.out.println("\n=== LABERINTO " + tamanio + "x" + tamanio + " ===");
        System.out.println("I = Inicio, M = Meta, # = Muro, T = Trampa, V = Vida, L = Llave, C = Cristal\n");

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (i==posicion.getX() &&  j==posicion.getY()) {
                    System.out.print("@"+ " ");
                }else{
                    Celda celda = matrizJuego[i][j];
                    // Si es muro, inicio o meta, mostramos su representación
                    if(!celda.getRepresentacion().equals("#") &&
                            !celda.getRepresentacion().equals("S") && !celda.getRepresentacion().equals("X")){
                        if (celda.isVisitada()) {
                            System.out.print("."+" ");
                        } else {
                            System.out.print(celda.getRepresentacion() + " ");
                        }
                    }else{
                        System.out.print(celda.getRepresentacion() + " ");
                    }

                }
            }
            System.out.println();
        }
    }

    /**
     * Busca la posición inicial en la matriz
     */
    private void buscarPosicionInicial() {
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (matrizJuego[i][j] instanceof Inicio) {
                    posicionInicial = new Posicion(i, j);
                    return;
                }
            }
        }
        // Si no encuentra inicio, poner uno por defecto
        if (posicionInicial == null) {
            posicionInicial = new Posicion(1, 1);
        }
    }

    /**
     * Busca la posición final en la matriz
     */
    private void buscarPosicionFinal() {
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (matrizJuego[i][j] instanceof Meta) {
                    posicionFinal = new Posicion(i, j);
                    return;
                }
            }
        }
        // Si no encuentra meta, poner una por defecto
        if (posicionFinal == null) {
            posicionFinal = new Posicion(tamanio - 2, tamanio - 2);
        }
    }

    /**
     * Repara las posiciones si se perdieron al cargar desde JSON
     */
    /**
     * Repara las posiciones si se perdieron al cargar desde JSON
     */
    public void repararPosiciones() {
        // Si las posiciones son null, buscarlas en la matriz
        if (posicionInicial == null) {
            buscarPosicionInicial();
        }
        if (posicionFinal == null) {
            buscarPosicionFinal();
        }

        // Verificar que las posiciones sean válidas
        if (posicionInicial != null &&
                (posicionInicial.getX() < 0 || posicionInicial.getX() >= tamanio ||
                        posicionInicial.getY() < 0 || posicionInicial.getY() >= tamanio)) {
            System.out.println("⚠️  Posición inicial inválida, reparando...");
            buscarPosicionInicial();
        }

        if (posicionFinal != null &&
                (posicionFinal.getX() < 0 || posicionFinal.getX() >= tamanio ||
                        posicionFinal.getY() < 0 || posicionFinal.getY() >= tamanio)) {
            System.out.println("⚠️  Posición final inválida, reparando...");
            buscarPosicionFinal();
        }
    }

    /**
     * Verifica que todas las posiciones sean válidas y consistentes
     */
    public void verificarConsistencia() {
        repararPosiciones();

        // Verificar que la posición inicial y final sean transitables
        if (posicionInicial != null && matrizJuego != null) {
            Celda celdaInicio = matrizJuego[posicionInicial.getX()][posicionInicial.getY()];
            if (celdaInicio != null && !celdaInicio.isTransitable()) {
                System.out.println("⚠️  Advertencia: Posición inicial no transitable");
            }
        }

        if (posicionFinal != null && matrizJuego != null) {
            Celda celdaMeta = matrizJuego[posicionFinal.getX()][posicionFinal.getY()];
            if (celdaMeta != null && !celdaMeta.isTransitable()) {
                System.out.println("⚠️  Advertencia: Posición final no transitable");
            }
        }
    }

    /**
     * Reinicia el estado del laberinto al cargar una partida
     * Repara posiciones y restaura representaciones visuales
     */
    public void reiniciarEstado() {
        repararPosiciones();// Busca la posicion Inicial y Final, en caso de que sean null las busca en la matriz

        if (matrizJuego != null && posicionFinal != null) {
            // Asegurar que la meta no esté visitada al cargar
            matrizJuego[posicionFinal.getX()][posicionFinal.getY()].setVisitada(false);

            // Verificar que todas las celdas tengan el estado correcto
            for (int i = 0; i < tamanio; i++) {
                for (int j = 0; j < tamanio; j++) {
                    if (matrizJuego[i][j] != null) {
                        // Restaurar la representación si es necesario
                        if (matrizJuego[i][j] instanceof Inicio) {
                            matrizJuego[i][j].setRepresentacion("S");
                        } else if (matrizJuego[i][j] instanceof Meta) {
                            matrizJuego[i][j].setRepresentacion("X");
                        } else if (matrizJuego[i][j] instanceof CaminoLibre) {
                            matrizJuego[i][j].setRepresentacion(".");
                        } else if (matrizJuego[i][j] instanceof Muro) {
                            matrizJuego[i][j].setRepresentacion("#");
                        } else if (matrizJuego[i][j] instanceof Trampa) {
                            matrizJuego[i][j].setRepresentacion("T");
                        } else if (matrizJuego[i][j] instanceof VidaExtra) {
                            matrizJuego[i][j].setRepresentacion("V");
                        } else if (matrizJuego[i][j] instanceof Llave) {
                            matrizJuego[i][j].setRepresentacion("L");
                        } else if (matrizJuego[i][j] instanceof Cristal) {
                            matrizJuego[i][j].setRepresentacion("C");
                        }
                    }
                }
            }
        }
    }
}