/*
 * MIT License
 * Copyright (c) 2019 Francisco Grau
 */
package com.palmaactiva.jaminas;

import com.palmaactiva.jaminas.ui.DialogoNuevoJuego;
import com.palmaactiva.jaminas.ui.Ventana;

/**
 *
 * @author Fran Grau - fran@kydemy.com
 */
public class Jaminas {

    private Ventana ventanaJuego;
    
    private Jaminas(){
        this.ventanaJuego = new Ventana();
    }
    
    public void lanzarVentanaJuego(){
        this.ventanaJuego.setVisible(true);
        this.ventanaJuego.setLocationRelativeTo(null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Jaminas jaminas = new Jaminas();
        jaminas.lanzarVentanaJuego();
    }
}
