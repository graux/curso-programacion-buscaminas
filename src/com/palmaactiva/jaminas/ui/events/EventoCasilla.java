package com.palmaactiva.jaminas.ui.events;

import com.palmaactiva.jaminas.lib.Casilla;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class EventoCasilla {

    public enum Tipo {
        INICIO,
        DESCUBRIR,
        BANDERA,
        QUITAR_BANDERA,
        BOMBA,
        COMPLETADO
    };
    private final Tipo tipoEvento;
    private final Casilla casilla;

    public EventoCasilla(Tipo tipoEvento, Casilla casilla) {
        this.tipoEvento = tipoEvento;
        this.casilla = casilla;
    }

    public Tipo getTipoEvento() {
        return tipoEvento;
    }

    public Casilla getCasilla() {
        return casilla;
    }
}
