/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.projectile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 *
 * @author peti
 */
public class Missile extends Projectile{
    
    private final int width = 60;
    private final int height = 20;
    
    private static final Color BULLET_COLOR = Color.decode("#a00817"); 
    private static final Color BULLET_END_COLOR = Color.decode("#edb604"); 
    

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BULLET_COLOR);
        g.fillRect(10, 5, width-10*2, height-5*2);

        g.setColor(BULLET_END_COLOR);
        
        int[] frontTriangleX = new int[]{width-10-1, width-10-1, width-1};
        int[] frontTriangleY = new int[]{height-1, 1, height/2};
        g.fillPolygon(frontTriangleX, frontTriangleY, 3);
        
        
        int[] endTriangleX = new int[]{10, 1, 5, 1};
        int[] endTriangleY = new int[]{height/2, height-1, height/2, 1};
        g.fillPolygon(endTriangleX, endTriangleY, 4);
    }

    @Override
    public int damage() {
        return 10;
    }

    @Override
    public int getComponentHeight() {
        return height;
    }

    @Override
    public int getComponentWidth() {
        return width;
    }   

    @Override
    public Shape getApproximationShape() {
        return getBounds();
    }

    @Override
    public void move() {
        Rectangle bounds = getBounds();
        setBounds(bounds.x + 25, bounds.getBounds().y, getComponentWidth(), getComponentHeight());
    }
    
    
    
    
}
