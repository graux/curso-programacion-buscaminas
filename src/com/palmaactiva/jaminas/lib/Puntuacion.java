package com.palmaactiva.jaminas.lib;

import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class Puntuacion implements Comparable<Puntuacion>, Serializable {

    static final long serialVersionUID = 333L;
    private final String nombre;
    private final int puntuacion;
    private final LocalDateTime fecha;
    private final int filas;
    private final int columnas;
    private final int numeroMinas;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("D MMM yyyy HH:mm", Locale.getDefault());
    private static final NumberFormat FORMATO_PUNTUACION = NumberFormat.getInstance(Locale.getDefault());

    public Puntuacion(String nombre, JuegoMinas juego) {
        this.nombre = nombre;
        this.filas = juego.getFilas();
        this.columnas = juego.getColumnas();
        this.numeroMinas = juego.getNumeroMinas();
        this.puntuacion = juego.getPuntuacion();
        this.fecha = LocalDateTime.now();
    }

    @Override
    public int compareTo(Puntuacion otraPuntuacion) {
        return this.puntuacion - otraPuntuacion.puntuacion;
    }

    public Object[] getColumnas() {
        return new Object[]{
            this.fecha.format(FORMATO_FECHA),
            this.nombre,
            this.filas,
            this.columnas,
            this.numeroMinas,
            FORMATO_PUNTUACION.format(this.puntuacion)
        };
    }
}
