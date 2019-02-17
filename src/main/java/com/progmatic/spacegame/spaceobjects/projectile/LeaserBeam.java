/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.projectile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 *
 * @author peti
 */
public class LeaserBeam extends Projectile {

    private final int length;
    private int width;
    private int height;
    private final Point startPoint;
    private final Point endPoint;
    private final double speed;
    private final int strokeWidth = 5;
    private boolean isHorizontal = false;
    private boolean isVertical = false;

    public LeaserBeam(double angleOfDirection, Point startPoint, double speed, int length) {
        this.startPoint = startPoint;
        this.length = length;
        this.endPoint = calcPointFromOhterPointByAngle(startPoint, length, angleOfDirection);
        if (this.startPoint.x == this.endPoint.x) {
            isVertical = true;
        }
        if (this.startPoint.y == this.endPoint.y) {
            isHorizontal = true;
        }
        this.width = (this.endPoint.x - this.startPoint.x);
        if (Math.abs(this.width) == 0) {
            this.width = strokeWidth;
        }
        this.height = (this.endPoint.y - this.startPoint.y);
        if (Math.abs(this.height) == 0) {
            this.height = strokeWidth;
        }

        this.speed = speed;

        setMyBounds();

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(strokeWidth));
        if (startPoint.x > endPoint.x) {
            if (isHorizontal) {
                g.drawLine(0, Math.abs(height / 2), Math.abs(width), Math.abs(height / 2));
            } else if (startPoint.y > endPoint.y) {
                g.drawLine(0, 0, Math.abs(width), Math.abs(height));

            } else {
                g.drawLine(0, Math.abs(height), Math.abs(width), 0);

            }

        } else {
            if (isVertical) {
                g.drawLine(Math.abs(width / 2), Math.abs(height), Math.abs(width / 2), 0);
            } else if (startPoint.y > endPoint.y) {
                g.drawLine(0, Math.abs(height), Math.abs(width), 0);
            } else {
                g.drawLine(0, 0, Math.abs(width), Math.abs(height));
            }
        }

    }

    @Override
    public void move() {
        if (!isVertical) {
            startPoint.x += width * speed;
            endPoint.x += width * speed;
        }
        if (!isHorizontal) {
            startPoint.y += height * speed;
            endPoint.y += height * speed;
        }
        setMyBounds();
    }

    private void setMyBounds() {
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        setBounds(x, y, Math.abs(width), Math.abs(height));
    }

    @Override
    public int damage() {
        return 1;
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
        Line2D line = new Line2D.Double(startPoint, endPoint);
        return line;
    }

    @Override
    public boolean isOutOfGameField(Rectangle rectangle) {
        Rectangle bounds = this.getBounds();
        if(
                bounds.x < rectangle.x - bounds.width ||
                bounds.x > rectangle.width ||
                bounds.y < rectangle.y - bounds.height ||
                bounds.y > rectangle.height){
            return true;
        }
        return false;
    }

}
