package com.palmaactiva.jaminas.lib.models;

import com.palmaactiva.jaminas.lib.JuegoMinas;
import com.palmaactiva.jaminas.lib.Posicion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.Period;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class JuegoMinasSerializable implements Serializable {

    private final int filas;
    private final int columnas;
    private final Posicion[] posicionesMinas;
    private final Posicion[] posicionesBanderas;
    private final Posicion[] posicionesDescubiertas;
    private final long tiempoMillis;

    public JuegoMinasSerializable(JuegoMinas juego) {
        this.filas = juego.getFilas();
        this.columnas = juego.getColumnas();
        Period tiempoJuego = juego.getTiempoJuego();
        this.tiempoMillis = ((long) tiempoJuego.getHours() * 60 * 60 * 1000)
                + ((long) tiempoJuego.getMinutes() * 60 * 1000)
                + ((long) tiempoJuego.getSeconds() * 1000)
                + ((long) tiempoJuego.getMillis());
        List<Posicion> posMinas = new ArrayList<>();
        List<Posicion> posBanderas = new ArrayList<>();
        List<Posicion> posDescubiertas = new ArrayList<>();
        juego.getCasillas().forEach(c -> {
            if (c.esMina()) {
                posMinas.add(c.getPosicion());
            }
            if (c.tieneBandera()) {
                posBanderas.add(c.getPosicion());
            } else if (c.isSelected()) {
                posDescubiertas.add(c.getPosicion());
            }
        });
        this.posicionesMinas = posMinas.toArray(new Posicion[posMinas.size()]);
        this.posicionesBanderas = posBanderas.toArray(new Posicion[posBanderas.size()]);
        this.posicionesDescubiertas = posDescubiertas.toArray(new Posicion[posDescubiertas.size()]);
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public Posicion[] getPosicionesMinas() {
        return posicionesMinas;
    }

    public Posicion[] getPosicionesBanderas() {
        return posicionesBanderas;
    }

    public Posicion[] getPosicionesDescubiertas() {
        return posicionesDescubiertas;
    }

    public long getTiempoMillis() {
        return tiempoMillis;
    }
}
