/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.infoobjects;

import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.gifts.Life;
import com.progmatic.spacegame.spaceobjects.projectile.Missile;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author peti
 */
public class InfoObject extends JComponent {

    private final Spaceship spaceship;
    private final Missile m;
    private final Life l;
    private final int sizeOfScoreText = 35;
    
    private final int spaceBetween = 5;
    private final int largeSpaceBetween = 25;

    public InfoObject(Spaceship sp) {
        super();
        this.spaceship = sp;
        setLayout(null);

        m = new Missile();
        add(m);
        
        l = new Life(10, 5, 10);
        add(l);
        
        m.setBounds(1, missileStartsAtHeight(), m.getComponentWidth(), m.getComponentHeight());
        l.setBounds(1, lifeStartsAtHeight(), l.getComponentWidth(), l.getComponentHeight());
    }
    
    private int missileStartsAtHeight(){
        return sizeOfScoreText + largeSpaceBetween;
    }
    
    private int lifeStartsAtHeight(){
        return missileStartsAtHeight() + m.getComponentHeight() + spaceBetween;
    }

    @Override
    protected void paintComponent(Graphics g) {
        
         //write scores
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, sizeOfScoreText));
        g.drawString(String.valueOf(spaceship.getScore()), 1, l.getComponentHeight() + spaceBetween);
  
        
        //write nr of missiles
        g.setColor(Color.GREEN);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        //g.drawString("x", m.getComponentWidth()+spaceBetween, missileStartsAtHeight() + m.getComponentHeight());
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        g.drawString(String.valueOf(spaceship.getNrOfMissiles()), 
                m.getComponentWidth() + spaceBetween + 20, 
                missileStartsAtHeight() + m.getComponentHeight());
        
        //write nr of life
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        //g.drawString("x", l.getComponentWidth() + spaceBetween, lifeStartsAtHeight() + l.getComponentHeight());
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        g.drawString(String.valueOf(spaceship.getLife()), 
                m.getComponentWidth() +  spaceBetween + 20, 
                lifeStartsAtHeight() + l.getComponentHeight());
        
         }

}
