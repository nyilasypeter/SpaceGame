/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author peti
 */
public class Spaceship extends SpaceObject {
    
    private static final Color WINDOW_COLOR_ALIVE = Color.decode("#e9f409");//#f3ff00
    private static final Color WINDOW_COLOR_DEAD = Color.BLACK;
    
    private int life = 4;


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(20, 0, 60, 100);

        g.setColor(Color.red);
        g.fillOval(0, 30, 100, 70);

        g.setColor(WINDOW_COLOR_ALIVE);
        int windowStart = 15;
        for (int i = 0; i < 4; i++) {
            if(i>=life){
                g.setColor(WINDOW_COLOR_DEAD);
            }
            g.fillOval(windowStart, 60, 10, 10);
            windowStart += 20;
        }

        //paintCircleAorundPoint(50, 50, 100, (Graphics2D) g);
        setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension();
        d.height = 100;
        d.width = 100;
        return d;
    }

    @Override
    public int getComponentWidth() {
        return 100;
    }

    @Override
    public int getComponentHeight() {
        return 100;
    }

    /**
     * First attempt, TODO make it more precise...
     *
     * @param p
     * @return
     */
    public boolean collided(SpaceObject so) {
        if (so instanceof Planet) {
            Planet p = (Planet) so;
            Point planetCenter = p.getAbsoluteCenter();
            int panelDiameter = p.getDiameter();
            Point myCenter = getMyCenter();
            int myDiameter = 100;
            double distance = planetCenter.distance(myCenter);
            return distance < panelDiameter / 2 + myDiameter / 2;
        } //implement this if there are other SpaceObjects....
        else {
            return true;
        }
    }

    private Point getMyCenter() {
        Rectangle bounds = getBounds();
        Point ret = new Point(bounds.x + 50, bounds.y + 50);
        return ret;
    }

    @Override
    public void move() {
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public void handleCollision() {
        life--;
    }
    
    
}
