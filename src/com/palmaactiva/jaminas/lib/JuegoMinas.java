package com.palmaactiva.jaminas.lib;

import com.palmaactiva.jaminas.lib.models.JuegoMinasSerializable;
import com.palmaactiva.jaminas.ui.events.DisparadorEventosCasilla;
import com.palmaactiva.jaminas.ui.events.EventoCasilla;
import com.palmaactiva.jaminas.ui.events.GestorEventosCasilla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JPanel;
import org.joda.time.Period;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class JuegoMinas implements GestorEventosCasilla, DisparadorEventosCasilla {

    public static JuegoMinas fromSerializable(JuegoMinasSerializable juegoCargado) {
        Posicion[] posicionesMinas = juegoCargado.getPosicionesMinas();
        int tamañoBoton = JuegoMinas.getTamañoBoton(juegoCargado.getFilas(), juegoCargado.getColumnas());
        JuegoMinas nuevoJuego = new JuegoMinas(juegoCargado.getFilas(), juegoCargado.getColumnas(), posicionesMinas.length, tamañoBoton);
        nuevoJuego.posicionesMinas = new ArrayList<>(Arrays.asList(juegoCargado.getPosicionesMinas()));
        nuevoJuego.partidaCargada = juegoCargado;
        nuevoJuego.cronoJuego.setTiempo(juegoCargado.getTiempoMillis());
        return nuevoJuego;
    }

    public static int getTamañoBoton(int filas, int columnas) {
        return (filas * columnas) < 200 ? 50 : 35;
    }

    private final int filas;
    private final int columnas;
    private final int numeroMinas;
    private final int tamañoBoton;
    private final Cronometro cronoJuego;
    private HashMap<Posicion, Casilla> mapaCasillas;
    private List<Posicion> posicionesMinas;
    private int numeroBanderas = 0;
    protected List<GestorEventosCasilla> gestoresEventos = new ArrayList<>();
    private HashSet<Casilla> casillasDescubiertas;
    private boolean juegoTerminado = false;
    private JuegoMinasSerializable partidaCargada;

    public JuegoMinas(int filas, int columnas, int numMinas, int tamañoBoton) {
        this.filas = filas;
        this.columnas = columnas;
        this.numeroMinas = numMinas;
        this.tamañoBoton = tamañoBoton;
        this.cronoJuego = new Cronometro();
    }

    public void inicializarPartida(JPanel panelCasillas) {
        Casilla.resetIconos();
        CasillaMina.resetIconos();
        Casilla nuevaCasilla;
        this.casillasDescubiertas = new HashSet<>();
        this.mapaCasillas = new HashMap<>(this.filas * this.columnas);
        if (this.posicionesMinas == null) {
            this.posicionesMinas = this.generarPosicionesMinas();
        }
        Posicion nuevaPos;
        panelCasillas.validate();
        for (int indiceFila = 0; indiceFila < this.filas; indiceFila++) {
            for (int indiceColumna = 0; indiceColumna < this.columnas; indiceColumna++) {
                nuevaPos = new Posicion(indiceFila, indiceColumna);
                if (posicionesMinas.contains(nuevaPos)) {
                    nuevaCasilla = new CasillaMina(nuevaPos, this.tamañoBoton);
                } else {
                    nuevaCasilla = new Casilla(nuevaPos, this.tamañoBoton);
                }
                nuevaCasilla.addGestorEventos(this);
                this.mapaCasillas.put(nuevaCasilla.getPosicion(), nuevaCasilla);
                panelCasillas.add(nuevaCasilla);
                nuevaCasilla.setFocusPainted(false);
            }
        }
        panelCasillas.validate();
        Casilla vecina;
        for (Casilla casilla : this.mapaCasillas.values()) {
            for (Posicion pos : casilla.getPosicion().getPosicionesVecinas()) {
                vecina = this.getCasillaPosicion(pos);
                if (vecina != null) {
                    casilla.addVecina(vecina);
                }
            }
        }
        if (this.partidaCargada != null) {
            Casilla casilla;
            for (Posicion pos : this.partidaCargada.getPosicionesDescubiertas()) {
                casilla = this.mapaCasillas.get(pos);
                casilla.revelarCasilla();
                this.casillasDescubiertas.add(casilla);
            }
            for (Posicion pos : this.partidaCargada.getPosicionesBanderas()) {
                casilla = this.mapaCasillas.get(pos);
                casilla.ponerBandera();
                this.numeroBanderas++;
            }
        }
        panelCasillas.repaint();
        panelCasillas.invalidate();
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    private List<Posicion> generarPosicionesMinas() {
        List<Posicion> nuevasMinas = new ArrayList<>(this.numeroMinas);
        Posicion pos;
        do {
            pos = Posicion.nuevaPosicionAleatoria(this.filas, this.columnas);
            if (!nuevasMinas.contains(pos)) {
                nuevasMinas.add(pos);
            }
        } while (nuevasMinas.size() != this.numeroMinas);
        return nuevasMinas;
    }

    private Casilla getCasillaPosicion(Posicion pos) {
        if (pos.dentroRegilla(this.filas, this.columnas)) {
            return this.mapaCasillas.get(pos);
        }
        return null;
    }

    public void empezarJuego() {
        this.cronoJuego.contar();
        this.dispararEvento(EventoCasilla.Tipo.INICIO, null);
    }

    public Period getTiempoJuego() {
        return this.cronoJuego.getTiempo();
    }

    public int getNumeroMinas() {
        return this.numeroMinas;
    }

    public int getNumeroCasillas() {
        return (this.filas * this.columnas) - this.numeroMinas;
    }

    public int getTamañoBoton() {
        return tamañoBoton;
    }

    public int getNumeroBanderas() {
        return this.numeroBanderas;
    }

    public int getNumeroCasillasDescubiertas() {
        return this.casillasDescubiertas.size();
    }

    private void gameOver() {
        for (Casilla casilla : this.mapaCasillas.values()) {
            if (casilla.esMina()) {
                casilla.descubrirCasilla();
            } else {
                casilla.setEnabled(false);
            }
        }
        this.juegoTerminado = true;
    }

    @Override
    public void gestionarEventoCasilla(EventoCasilla evento) {
        switch (evento.getTipoEvento()) {
            case BANDERA:
                this.numeroBanderas++;
                break;
            case QUITAR_BANDERA:
                this.numeroBanderas--;
                break;
            case DESCUBRIR:
                if (!this.casillasDescubiertas.contains(evento.getCasilla())) {
                    this.casillasDescubiertas.add(evento.getCasilla());
                    if (this.casillasDescubiertas.size() == this.getNumeroCasillas()) {
                        this.cronoJuego.pausa();
                        this.dispararEvento(EventoCasilla.Tipo.COMPLETADO, null);
                        this.juegoTerminado = true;
                    }
                }
                break;
            case BOMBA:
                this.gameOver();
                break;
        }
        this.dispararEvento(evento);
    }

    @Override
    public void addGestorEventos(GestorEventosCasilla gestor) {
        this.gestoresEventos.add(gestor);
    }

    @Override
    public List<GestorEventosCasilla> getGestoresEventos() {
        return this.gestoresEventos;
    }

    public int getPuntuacion() {
        float porcentajeMinas = 100 * this.numeroMinas / (float) (this.filas * this.columnas);
        double maxPuntos = this.filas * this.columnas * porcentajeMinas;
        double segundosJuego = this.cronoJuego.getDuracion().getMillis() / 1000;
        int puntuacion = (int) Math.round(maxPuntos - segundosJuego);
        return puntuacion < 0 ? 0 : puntuacion;
    }

    public void finalizarPartida() {
        this.gameOver();
    }

    public Collection<Casilla> getCasillas() {
        return this.mapaCasillas.values();
    }

    public void pausarJuego() {
        if (!this.juegoTerminado) {
            this.cronoJuego.pausa();
            for (Casilla casilla : this.mapaCasillas.values()) {
                casilla.setEnabled(false);
            }
        }
    }

    public void reanudarJuego() {
        if (!this.juegoTerminado) {
            this.cronoJuego.contar();
            for (Casilla casilla : this.mapaCasillas.values()) {
                casilla.setEnabled(true);
            }
        }
    }

}
