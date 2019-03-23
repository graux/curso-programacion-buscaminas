package com.palmaactiva.jaminas.lib;

import com.palmaactiva.jaminas.ui.events.DisparadorEventosCasilla;
import com.palmaactiva.jaminas.ui.events.EventoCasilla;
import com.palmaactiva.jaminas.ui.events.GestorEventosCasilla;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class Casilla extends JToggleButton implements DisparadorEventosCasilla {

    private static Icon ICONO_BANDERA;
    private static final Color COLOR_1VECINA = new Color(0, 68, 85);
    private static final Color COLOR_2VECINA = new Color(22, 80, 68);
    private static final Color COLOR_3VECINA = new Color(103, 120, 30);
    private static final Color COLOR_4VECINA = new Color(160, 137, 44);
    private static final Color COLOR_5VECINA = new Color(85, 68, 0);
    private static final Color COLOR_6VECINA = new Color(120, 68, 33);
    private static final Color COLOR_7VECINA = new Color(85, 33, 0);
    private static final Color COLOR_8VECINA = new Color(85, 0, 0);

    public static void resetIconos() {
        ICONO_BANDERA = null;
    }
    protected Posicion posicion;
    protected int tamañoBoton;
    private List<Casilla> vecinas;
    private int numVecinasMinas = -1;
    protected boolean conBandera = false;
    protected List<GestorEventosCasilla> gestoresEventos = new ArrayList<>();

    public Casilla(Posicion posicion, int tamañoBoton) {
        this.posicion = posicion;
        this.tamañoBoton = tamañoBoton;
        this.vecinas = new ArrayList<>();
        this.inicializarComponentes();
        this.inicializarEventos();
    }

    public Casilla(int fila, int columna, int tamañoBoton) {
        this(new Posicion(fila, columna), tamañoBoton);
    }

    protected void inicializarComponentes() {
        this.setSize(tamañoBoton, tamañoBoton);
        this.addActionListener((e) -> {
            if (!this.conBandera) {
                Casilla.this.clickCasilla();
            } else {
                this.setSelected(false);
            }
        });
        this.setBorder(null);
        this.setFont(new Font("Liberation Sans", Font.BOLD, (this.tamañoBoton - 15)));
        if (ICONO_BANDERA == null) {
            try {
                ICONO_BANDERA = new javax.swing.ImageIcon(javax.imageio.ImageIO.read(getClass().getResource("/com/palmaactiva/jaminas/ui/icons/flag.png")).getScaledInstance((int) Math.round(this.tamañoBoton * 0.6), (int) Math.round(this.tamañoBoton * 0.6), Image.SCALE_SMOOTH));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }

    protected void clickCasilla() {
        this.descubrirCasilla();
    }

    protected void descubrirCasilla() {
        this.setSelected(true);
        if (this.tieneMinasVecinas()) {
            this.revelarCasilla();
        } else {
            this.vecinas.forEach(vecina -> {
                if (!vecina.isSelected() && !vecina.esMina() && !vecina.conBandera) {
                    vecina.descubrirCasilla();
                }
            });
        }
        this.dispararEvento(EventoCasilla.Tipo.DESCUBRIR, this);
    }

    protected void revelarCasilla() {
        this.comprobarMinasVecinas();
        this.setSelected(true);
        if (this.numVecinasMinas > 0) {
            this.setText(Integer.toString(this.numVecinasMinas));
        }
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void addVecina(Casilla vecina) {
        this.vecinas.add(vecina);
    }

    public boolean esMina() {
        return false;
    }

    public boolean tieneMinasVecinas() {
        if (this.numVecinasMinas < 0) {
            this.comprobarMinasVecinas();
        }
        return this.numVecinasMinas > 0;
    }

    private void comprobarMinasVecinas() {
        this.numVecinasMinas = 0;
        for (Casilla casilla : this.vecinas) {
            if (casilla.esMina()) {
                this.numVecinasMinas++;
            }
        }
        this.setForeground(this.getColorBoton());
    }

    private Color getColorBoton() {
        switch (this.numVecinasMinas) {
            case 1:
                return COLOR_1VECINA;
            case 2:
                return COLOR_2VECINA;
            case 3:
                return COLOR_3VECINA;
            case 4:
                return COLOR_4VECINA;
            case 5:
                return COLOR_5VECINA;
            case 6:
                return COLOR_6VECINA;
            case 7:
                return COLOR_7VECINA;
            case 8:
                return COLOR_8VECINA;
        }
        return COLOR_1VECINA;
    }

    @Override
    public String toString() {
        return this.posicion.toString() + " - " + (this.isSelected() ? "Descubierta" : "Cubierta");
    }

    private void inicializarEventos() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (evento.getButton() == MouseEvent.BUTTON3) {
                    Casilla.this.ponerBandera();
                } else {
                    super.mouseClicked(evento);
                }
            }
        });
    }

    protected void ponerBandera() {
        if (!this.isSelected()) {
            Casilla.this.conBandera = !Casilla.this.conBandera;
            if (Casilla.this.conBandera) {
                this.setIcon(ICONO_BANDERA);
                this.dispararEvento(EventoCasilla.Tipo.BANDERA, this);
            } else {
                this.setIcon(null);
                this.dispararEvento(EventoCasilla.Tipo.QUITAR_BANDERA, this);
            }
        }
    }

    public boolean tieneBandera() {
        return this.conBandera;
    }

    @Override
    public void addGestorEventos(GestorEventosCasilla gestor) {
        this.gestoresEventos.add(gestor);
    }

    @Override
    public List<GestorEventosCasilla> getGestoresEventos() {
        return this.gestoresEventos;
    }
}
