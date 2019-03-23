package com.palmaactiva.jaminas.lib;

import com.palmaactiva.jaminas.ui.events.EventoCasilla;
import java.awt.Image;
import java.io.IOException;
import javax.swing.Icon;

/**
 *
 * @author Fran Grau <fran@kydemy.com>
 */
public class CasillaMina extends Casilla {

    private static Icon ICONO_BOMBA;
    private static Icon ICONO_EXPLOSION;

    public static void resetIconos() {
        ICONO_BOMBA = null;
        ICONO_EXPLOSION = null;
    }

    public CasillaMina(Posicion posicion, int tamañoBoton) {
        super(posicion, tamañoBoton);
    }

    @Override
    protected void inicializarComponentes() {
        super.inicializarComponentes();
        if (ICONO_BOMBA == null) {
            try {
                int tamañoIcono = Math.round(this.tamañoBoton * 0.8f);
                ICONO_BOMBA = new javax.swing.ImageIcon(javax.imageio.ImageIO.read(getClass().getResource("/com/palmaactiva/jaminas/ui/icons/bomb.png")).getScaledInstance(tamañoIcono, tamañoIcono, Image.SCALE_SMOOTH));
                ICONO_EXPLOSION = new javax.swing.ImageIcon(javax.imageio.ImageIO.read(getClass().getResource("/com/palmaactiva/jaminas/ui/icons/explosion.png")).getScaledInstance(tamañoIcono, tamañoIcono, Image.SCALE_SMOOTH));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }

    @Override
    public boolean esMina() {
        return true;
    }

    @Override
    protected void descubrirCasilla() {
        if (!this.isSelected()) {
            this.setIcon(ICONO_BOMBA);
            this.setSelected(true);
        }
    }

    @Override
    protected void clickCasilla() {
        this.setIcon(ICONO_EXPLOSION);
        this.setSelected(true);
        this.dispararEvento(EventoCasilla.Tipo.BOMBA, this);
    }

}
