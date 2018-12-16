/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.infoobjects;

import com.progmatic.spacegame.spaceobjects.projectile.Missile;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author peti
 */
public class InfoObject extends JComponent {

    private final Spaceship spaceship;
    Missile m;

    public InfoObject(Spaceship sp) {
        super();
        this.spaceship = sp;
        setLayout(null);

        m = new Missile();
        add(m);
        m.setBounds(1, 1, m.getComponentWidth(), m.getComponentHeight());

    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(Color.red);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.drawString("x", m.getComponentWidth()/2-22, 50);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
        g.drawString(String.valueOf(spaceship.getNrOfMissiles()), m.getComponentWidth()/2-2, 50);
        
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        g.drawString(String.valueOf(spaceship.getScore()), 100, 24);
    }

}
