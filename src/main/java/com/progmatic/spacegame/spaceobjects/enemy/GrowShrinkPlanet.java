/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.RightToLeftSpaceObject;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import com.progmatic.spacegame.spaceobjects.gifts.Gold;
import com.progmatic.spacegame.spaceobjects.gifts.Life;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class GrowShrinkPlanet extends RightToLeftSpaceObject implements Hitable {
    
    private final Random r = new Random();
    private final int origDiameter;
    private int diameter;
    private final int strokesize;
    private final Color color;
    private final Gift gift;
    
    private int repeatNr = 0;
    private Timer t;
    
    private final int explodedDiameter;
    private final int maxRepeatNr = 75;//15
    private final int nrOfPieces = 10;
    
    private final int planetSpeed;
    
    private int growShinkCounter = 0;
    private final int sizeToGrow;
    
    
    public GrowShrinkPlanet() {
        this.origDiameter = r.nextInt(150) + 30;
        this.diameter = origDiameter;
        this.bulletResistance = (origDiameter / 50) + 2;
        this.explodedDiameter = origDiameter + 50;
        this.planetSpeed = r.nextInt(5) + 1;
        this.strokesize = 0;
        this.sizeToGrow = r.nextInt(80)+60;
        this.color = randomColor();
        int giftType = r.nextInt(4);
        if (giftType >= 3) {
            gift = new Life();
        } else {
            gift = new Gold();
        }
    }
    
    private Color randomColor() {
        return Color.getHSBColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }
    
    private void calcDiameter(){
        growShinkCounter++;
        if(growShinkCounter == 50){
            growShinkCounter = 0;
            diameter = origDiameter;
        }
        else {
            double growRatio = (double)sizeToGrow / 50d;
           //int grow = (int) (diameter + growRatio);
            int grow = 1;
           grow = Math.max(grow, 1);
           diameter += grow;
           diameter = Math.min(diameter, origDiameter + sizeToGrow);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            paintPlanet(g);
            
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            paintExplodedPlanet(g);
        }

    }
    
    private void paintPlanet(Graphics g) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        //g2.setStroke(new BasicStroke(strokesize));
        Point center = getRelativeCenter();
        g.fillOval(0, 0, diameter, diameter);

    }
    
    private void paintExplodedPlanet(Graphics g) {
        int centerDist = calcExplodedCenterDistance();
        int startAngle = 0;
        int angleGrow = 360 / nrOfPieces;
        int swingStartAngle = 90 + angleGrow / 2;
        Point center = getRelativeCenter();
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        //g2.setStroke(new BasicStroke(strokesize));
        for (int i = 0; i < nrOfPieces; i++) {
            Point cp = calcCenterOfExplodingPiece(center, centerDist, startAngle);
            paintArcAorundPoint(cp.x, cp.y, origDiameter, swingStartAngle, -1 * angleGrow, true, g);
            startAngle += angleGrow;
            swingStartAngle -= angleGrow;
            
        }
        
    }
    
    
    @Override
    public Point getRelativeCenter() {
        int actDiameter = diameter;
        if (state.equals(SpaceObjectState.AGOZNIZING)) {
            actDiameter = diameter + calcExplodedCenterDistance() * 2;
        }
        
        int centerX = actDiameter / 2 + strokesize;
        int centery = actDiameter / 2 + strokesize;
        Point p = new Point(centerX, centery);
        return p;
    }
    
    @Override
    public int getComponentWidth() {
        if (state.equals(SpaceObjectState.AGOZNIZING)) {
            return (explodedDiameter + strokesize * 2);
        }
        return (diameter + strokesize * 2);
    }
    
    @Override
    public int getComponentHeight() {
        return getComponentWidth();
    }
    
    private Point calcCenterOfExplodingPiece(Point center, int distance, double angle) {
        int x = (int) (Math.sin(Math.toRadians(angle)) * distance);
        int y = (int) (Math.cos(Math.toRadians(angle)) * distance);
        return new Point(center.x + x, center.y - y);
    }
    
    private int calcExplodedCenterDistance() {
        int ret =  (int)(calcStep() * repeatNr);
        return ret;
        
    }
    
    private double calcStep() {
        int diff = explodedDiameter - origDiameter;
        double step = (double)diff / maxRepeatNr;
        return step;
    }
    
    @Override
    public void move() {
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            Point center = getAbsoluteCenter();
            calcDiameter();
            Rectangle bounds = getBounds();
            
            center.x = center.x-planetSpeed;
            setBoundsAroundCenter(center, getComponentWidth(), getComponentHeight());
            //setBounds(bounds.x - planetSpeed, bounds.getBounds().y, getComponentWidth(), getComponentHeight());
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            Point absCenter = getAbsoluteCenter();
            repeatNr++;
            int actDiameter = origDiameter + calcExplodedCenterDistance() * 2 + strokesize * 2;
            setBoundsAroundCenter(absCenter, actDiameter, actDiameter);
            repaint();
            if (repeatNr >= maxRepeatNr) {
                repaint();
                state = SpaceObjectState.DEAD;
            }
        }
        
    }
    
    @Override
    public Shape getApproximationShape() {
        Shape sh = new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
        return sh;
    }
    
    
    
    @Override
    public SpaceObject createGiftAfterDying() {
        Point center = getAbsoluteCenter();
        gift.setBounds(
                center.x - gift.getComponentWidth() / 2,
                center.y - gift.getComponentHeight() / 2,
                gift.getComponentWidth(),
                gift.getComponentHeight());
        return gift;
    }
    
}
