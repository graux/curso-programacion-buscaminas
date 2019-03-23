package com.palmaactiva.jaminas.lib;

import java.util.concurrent.TimeUnit;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class Cronometro {

    private long tiempoAcumulado;
    private long tiempoInicio;

    public Cronometro() {
        this.tiempoInicio = 0;
        this.tiempoAcumulado = 0;
    }

    public void contar() {
        this.tiempoInicio = System.nanoTime();
    }

    public void pausa() {
        this.computarTiempo();
        this.tiempoInicio = 0;
    }

    private void computarTiempo() {
        if (tiempoInicio > 0) {
            long tiempoActual = System.nanoTime();
            this.tiempoAcumulado += tiempoActual - tiempoInicio;
            this.tiempoInicio = System.nanoTime();
        }
    }

    public Period getTiempo() {
        this.computarTiempo();
        long millis = TimeUnit.MILLISECONDS.convert(this.tiempoAcumulado, TimeUnit.NANOSECONDS);
        return new Period(millis);
    }

    public Duration getDuracion() {
        return this.getTiempo().toStandardDuration();
    }

    public void setTiempo(long millis) {
        this.tiempoAcumulado = TimeUnit.NANOSECONDS.convert(millis, TimeUnit.MILLISECONDS);
    }
}
