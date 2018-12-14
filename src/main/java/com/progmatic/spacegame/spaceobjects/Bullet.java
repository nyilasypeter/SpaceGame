/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.SpaceObjectState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author peti
 */
public class Bullet extends Projectile {

    private final int strokeWidth = 1;
    private final int bulletWidth = 20;
    private final int bulletHeight = 8;

    @Override
    public void paint(Graphics g1) {
        super.paint(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(strokeWidth));
        g.fillOval(strokeWidth, strokeWidth, bulletWidth, bulletHeight);
    }

    @Override
    public int getComponentWidth() {
        return bulletWidth + strokeWidth * 2;
    }

    @Override
    public int getComponentHeight() {
        return bulletHeight + strokeWidth * 2;
    }

    @Override
    public void move() {
        Rectangle bounds = getBounds();
        setBounds(bounds.x + 40, bounds.getBounds().y, getComponentWidth(), getComponentHeight());
    }

    @Override
    protected Point getRelativeCenter() {
        return new Point(getComponentWidth() / 2, getComponentHeight() / 2);
    }

    @Override
    public void handleCollision() {
        this.state = SpaceObjectState.DEAD;
    }

    @Override
    public Shape getApproximationShape() {
        return getBounds();
    }


}
