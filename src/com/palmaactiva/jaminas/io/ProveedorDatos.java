package com.palmaactiva.jaminas.io;

import com.palmaactiva.jaminas.lib.JuegoMinas;
import com.palmaactiva.jaminas.lib.Puntuacion;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public interface ProveedorDatos {

    JuegoMinas cargarPartida(String archivo);

    List<Puntuacion> cargarPuntuaciones();

    void guardarPartida(JuegoMinas juegoMinas, String archivo);

    void guardarPuntuaciones(Collection<Puntuacion> puntuaciones);

}
