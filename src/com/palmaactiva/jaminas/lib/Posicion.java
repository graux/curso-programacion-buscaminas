package com.palmaactiva.jaminas.lib;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class Posicion implements Serializable {

    private static final Random RND = new Random();

    static Posicion nuevaPosicionAleatoria(int filas, int columnas) {
        return new Posicion(RND.nextInt(filas), RND.nextInt(columnas));
    }

    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Posicion pos = (Posicion) obj;
        return this.equals(pos);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.fila;
        hash = 53 * hash + this.columna;
        return hash;
    }

    public boolean equals(Posicion pos) {
        return pos.fila == this.fila && pos.columna == this.columna;
    }

    public Posicion[] getPosicionesVecinas() {
        Posicion[] vecinas = new Posicion[8];
        int indice = 0;
        for (int indiceFila = this.fila - 1; indiceFila < this.fila + 2; indiceFila++) {
            for (int indiceColumna = this.columna - 1; indiceColumna < this.columna + 2; indiceColumna++) {
                if (indiceFila != this.fila || indiceColumna != this.columna) {
                    vecinas[indice] = new Posicion(indiceFila, indiceColumna);
                    indice++;
                }
            }
        }
        return vecinas;
    }

    public boolean dentroRegilla(int numFilas, int numColumnas) {
        if (fila >= 0 && fila < numFilas) {
            return columna >= 0 && columna < numColumnas;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + fila + "," + columna + "]";
    }
}
