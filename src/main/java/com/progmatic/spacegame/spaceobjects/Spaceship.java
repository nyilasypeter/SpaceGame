/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import com.progmatic.spacegame.spaceobjects.gifts.Life;
import com.progmatic.spacegame.spaceobjects.gifts.MissilePack;
import com.progmatic.spacegame.spaceobjects.projectile.Bullet;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import com.progmatic.spacegame.spaceobjects.projectile.Missile;
import com.progmatic.spacegame.spaceobjects.projectile.Projectile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;

/**
 *
 * @author peti
 */
public class Spaceship extends SpaceObject implements Hitable {

    private static final Color WINDOW_COLOR_ALIVE = Color.decode("#e9f409");//#f3ff00
    private static final Color WINDOW_COLOR_DEAD = Color.BLACK;

    private int life = 4;
    private int nrOfMissiles = 4;
    private int score = 0;

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
            if (i >= life) {
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

    public Point getMyCenter() {
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
    public void handleCollision(SpaceObject other) {
        if (other instanceof RightToLeftSpaceObject) {
            life--;
        } 
        else if (other instanceof Gift) {
            Gift g = (Gift) other;
            score += g.getValue();

            if (g instanceof Life) {
                this.life++;
            } else if (g instanceof MissilePack) {
                MissilePack mp = (MissilePack) g;
                this.nrOfMissiles += mp.getNrOfMissiles();
            }
        }
    }

    public Projectile fireBullet() {

        Bullet b = new Bullet();
        Rectangle myBounds = getBounds();
        b.setBounds(myBounds.x + getComponentWidth() + 10, myBounds.y + 70, b.getComponentWidth(), b.getComponentHeight());
        return b;

    }

    public Projectile fireMissile() {
        nrOfMissiles--;
        if (nrOfMissiles >= 0) {
            Missile m = new Missile();
            Rectangle myBounds = getBounds();
            m.setBounds(myBounds.x + getComponentWidth() + 10, myBounds.y + 70, m.getComponentWidth(), m.getComponentHeight());
            return m;
        } else {
            return null;
        }
    }

    @Override
    public Shape getApproximationShape() {
        return new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
    }

    @Override
    public void beingHit(int damage) {
        life -= damage;
    }

    public int getNrOfMissiles() {
        return Math.max(0, nrOfMissiles);
    }

    public void setNrOfMissiles(int nrOfMissiles) {
        this.nrOfMissiles = nrOfMissiles;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    

    @Override
    public boolean isOutOfGameField(Rectangle rectangle) {
        return false;
    }
    
    public SpaceShipInfo spaceShipInfo(){
        SpaceShipInfo si = new SpaceShipInfo();
        si.score = this.score;
        si.nrOfMissiles = this.nrOfMissiles;
        si.life = this.life;
        return si;
    }

    public class SpaceShipInfo {
        private int score;
        private int nrOfMissiles;
        private int life;

        public int getScore() {
            return score;
        }

        public int getNrOfMissiles() {
            return nrOfMissiles;
        }

        public int getLife() {
            return life;
        }
        
       
    }

}
