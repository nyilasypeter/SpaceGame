/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.components;

import com.progmatic.spacegame.SpaceObjectState;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JComponent;

/**
 *
 * @author peti
 */
public abstract class SpaceObject extends JComponent {

    protected SpaceObjectState state = SpaceObjectState.ALIVE;

    public abstract int getComponentWidth();

    public abstract int getComponentHeight();
    
    public abstract void move();

    protected void paintCircleAorundPoint(int x, int y, int diameter, Graphics g) {
        paintCircleAorundPoint(x, y, diameter, false, g);
    }

    protected void paintCircleAorundPoint(int x, int y, int diameter, boolean fill, Graphics g) {
        int swingX = x - diameter / 2;
        int swingY = y - diameter / 2;
        if (fill) {
            g.fillOval(swingX, swingY, diameter, diameter);

        } else {
            g.drawOval(swingX, swingY, diameter, diameter);
        }
    }

    protected void paintArcAorundPoint(int x, int y, int diameter, int startAngle, int arcAngle, Graphics g) {
        paintArcAorundPoint(x, y, diameter, startAngle, arcAngle, false, g);
    }

    protected void paintArcAorundPoint(int x, int y, int diameter, int startAngle, int arcAngle, boolean fill, Graphics g) {
        int swingX = x - diameter / 2;
        int swingY = y - diameter / 2;
        if (fill) {
            g.fillArc(swingX, swingY, diameter, diameter, startAngle, arcAngle);

        } else {
            g.drawArc(swingX, swingY, diameter, diameter, startAngle, arcAngle);

        }
    }
    
    protected void setBoundsAroundCenter(Point center, int width, int height){
        setBounds(center.x-(width/2), center.y-(height/2), width, height);
    }

    public SpaceObjectState getState() {
        return state;
    }

    public void setState(SpaceObjectState state) {
        this.state = state;
    }

    public void handleCollision() {

    }

}
