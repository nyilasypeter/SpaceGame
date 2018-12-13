/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import java.awt.Color;
import javax.swing.SwingUtilities;

/**
 *
 * @author peti
 */
public class Game {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainGameFrame frame = new MainGameFrame();
                frame.init();
                frame.addSpaceShip();
                //frame.addBlinker();
                //frame.addExploder();
                //frame.addGrowShrinkStar();
                frame.setVisible(true);
            }
        });

    }
}
