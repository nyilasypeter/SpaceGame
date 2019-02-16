/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.gifts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

/**
 *
 * @author peti
 */
public class Gold extends Gift{
    
    private static final Color GOLD = Color.decode("#e8a806");
    
    final int width = 35;
    final int height = 20;
    
    final int value;

    public Gold() {
        this.value = r.nextInt(50)+100;
    }

    public Gold(int value) {
        this.value = value;
    }
    
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.setColor(GOLD);
        g.fill3DRect(0, 0, width, height, true);
    }

    @Override
    public int getComponentWidth() {
        return width;
    }

    @Override
    public int getComponentHeight() {
        return height;
    }

    @Override
    public Shape getApproximationShape() {
        return getBounds();
    }

    public int getValue() {
        return value;
    }
    
}
