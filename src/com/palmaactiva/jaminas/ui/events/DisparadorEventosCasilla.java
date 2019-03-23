package com.palmaactiva.jaminas.ui.events;

import com.palmaactiva.jaminas.lib.Casilla;
import java.util.List;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public interface DisparadorEventosCasilla {
    
    void addGestorEventos(GestorEventosCasilla gestor);
    
    List<GestorEventosCasilla> getGestoresEventos();
    
    default void dispararEvento(EventoCasilla.Tipo tipo, Casilla casilla) {
        EventoCasilla nuevoEvento = new EventoCasilla(tipo, casilla);
        this.dispararEvento(nuevoEvento);
    }
    
    default void dispararEvento(EventoCasilla nuevoEvento) {
        List<GestorEventosCasilla> gestores = this.getGestoresEventos();
        if (gestores != null && gestores.size() > 0) {
            gestores.forEach(gestor -> gestor.gestionarEventoCasilla(nuevoEvento));
        }
    }
}
