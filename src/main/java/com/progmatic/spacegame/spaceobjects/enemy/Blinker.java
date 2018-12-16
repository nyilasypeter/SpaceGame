/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.enemy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class Blinker extends JComponent{
    
    private int repeatNr = 0;
    private Timer t;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        int blinkStatus = repeatNr % 3;
        switch (blinkStatus) {
            case 0:
                drawOval(Color.RED, g);
                break;
            case 1:
                drawOval(Color.YELLOW, g);
                break;
            case 2:
                //drawOval(Color.PINK, g);
                break;
            default:
                break;
        }
        
    }
    
    private void drawOval(Color color, Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        g2.setColor(color);
        g2.drawOval(10, 10, 100, 100);
        
    }
    
    public void startBlinking(){
        t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repeatNr++;
                repaint();
                if(repeatNr>500){
                    repeatNr = 2;
                    repaint();
                    t.stop();
                }
            }
        });
        t.setInitialDelay(300);
        t.start();
    }
    
}
