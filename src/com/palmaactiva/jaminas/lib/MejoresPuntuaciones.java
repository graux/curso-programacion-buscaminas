package com.palmaactiva.jaminas.lib;

import com.palmaactiva.jaminas.io.ProveedorDatos;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Fran Grau <fran@kydemy.com>
 */
public class MejoresPuntuaciones {

    private final SortedSet<Puntuacion> puntuaciones;
    private final ProveedorDatos proveedorDatos;

    public SortedSet<Puntuacion> getPuntuaciones() {
        return ((TreeSet) puntuaciones).descendingSet();
    }

    public MejoresPuntuaciones(ProveedorDatos datoJaminas) {
        this.puntuaciones = new TreeSet<>();
        this.proveedorDatos = datoJaminas;
    }

    public void addPuntuacion(Puntuacion puntuacion) {
        this.puntuaciones.add(puntuacion);
        this.proveedorDatos.guardarPuntuaciones(this.puntuaciones);
    }

    public void addPuntuacion(String nombre, JuegoMinas juego) {
        this.addPuntuacion(new Puntuacion(nombre, juego));
    }

    public void cargarPuntuaciones() {
        List<Puntuacion> puntuacionesCargadas = this.proveedorDatos.cargarPuntuaciones();
        if (puntuacionesCargadas != null && puntuacionesCargadas.size() > 0) {
            this.puntuaciones.addAll(puntuacionesCargadas);
        }
    }
}
