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
    
    private Spaceship spaceship;
    private List<Missile> missiles = new ArrayList<>();

    public InfoObject(Spaceship sp){
        super();
        this.spaceship = sp;
        setLayout(null);
        
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(Color.red);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        g.drawString(String.valueOf(spaceship.getScore()), 100, 24);
    }
    
    public void refresh(){
        refreshNumberOfMissiles();
        
    }

    private void refreshNumberOfMissiles() {
        int nrOfMissiles = spaceship.getNrOfMissiles();
        for(int i=missiles.size(); i<nrOfMissiles; i++){
            Missile m = new Missile();
            missiles.add(m);
            add(m);
            m.setBounds(0, i*m.getComponentHeight(), m.getComponentWidth(), m.getComponentHeight());
        }
        while(missiles.size() > nrOfMissiles){
            Missile m = missiles.remove(missiles.size()-1);
            remove(m);
        }
    }
    
    
}
