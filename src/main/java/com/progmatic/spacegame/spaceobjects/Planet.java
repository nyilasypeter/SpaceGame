/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.MainGameFrame;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.gifts.Gold;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class Planet extends SpaceObject implements Hitable {

    private final Random r = new Random();
    private final int diameter;
    private final int strokesize;
    private final Color color;
    private final int nrOfExtraCircles;
    private final boolean[] isFilledCircle;
    private final Color[] extraCircleColors;
    private int bulletResistance;
    private final MainGameFrame mainGameFrame;

    private int repeatNr = 0;
    private Timer t;

    private final int explodedDiameter;
    private final int maxRepeatNr = 15;
    private final int nrOfPieces = 10;

    private final int planetAnimationSpeed;

    private int agonize;

    public Planet(MainGameFrame mainGameFrame) {
        this.mainGameFrame = mainGameFrame;
        this.diameter = r.nextInt(250) + 30;
        this.bulletResistance = (diameter / 50) + 2;
        this.explodedDiameter = diameter + 50;
        this.planetAnimationSpeed = r.nextInt(10) + 10;
        this.strokesize = r.nextInt(10) + 3;
        this.color = randomColor();
        this.nrOfExtraCircles = r.nextInt(6) + 1;
        this.isFilledCircle = new boolean[this.nrOfExtraCircles];
        for (int i = 0; i < this.nrOfExtraCircles; i++) {
            this.isFilledCircle[i] = r.nextBoolean();
        }
        this.extraCircleColors = new Color[this.nrOfExtraCircles];
        for (int i = 0; i < this.nrOfExtraCircles; i++) {
            this.extraCircleColors[i] = randomColor();
        }
    }

    private Color randomColor() {
        return Color.getHSBColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (repeatNr == 0) {
            paintPlanet(g);
        } else if (repeatNr == -1) {

        } else {
            paintExplodedPlanet(g);
        }

    }

    private void paintPlanet(Graphics g) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(strokesize));
        Point center = getRelativeCenter();
        g.fillOval(strokesize, strokesize, diameter, diameter);

        if (nrOfExtraCircles != 0) {
            int step = (int) (diameter / (nrOfExtraCircles));
            int nextDiam = diameter - step;
            for (int i = 0; i < nrOfExtraCircles; i++) {
                g.setColor(extraCircleColors[i]);
                paintCircleAorundPoint(center.x, center.y, nextDiam, g2);
                nextDiam = nextDiam - step;
            }
        }
    }

    private void paintExplodedPlanet(Graphics g) {
        int centerDist = calcExplodedCenterDistance();
        int startAngle = 0;
        int angleGrow = 360 / nrOfPieces;
        int swingStartAngle = 90 + angleGrow / 2;
        Point center = getRelativeCenter();
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(strokesize));
        for (int i = 0; i < nrOfPieces; i++) {
            Point cp = calcCenterOfExplodingPiece(center, centerDist, startAngle);
            paintArcAorundPoint(cp.x, cp.y, diameter, swingStartAngle, -1 * angleGrow, true, g);
            startAngle += angleGrow;
            swingStartAngle -= angleGrow;

        }

        if (nrOfExtraCircles != 0) {

            int step = (int) (diameter / (nrOfExtraCircles));
            int nextDiam = diameter - step;
            for (int j = 0; j < nrOfExtraCircles; j++) {
                centerDist = calcExplodedCenterDistance();
                startAngle = 0;
                angleGrow = 360 / nrOfPieces;
                swingStartAngle = 90 + angleGrow / 2;
                center = getRelativeCenter();
                g.setColor(extraCircleColors[j]);
                for (int i = 0; i < nrOfPieces; i++) {
                    Point cp = calcCenterOfExplodingPiece(center, centerDist, startAngle);
                    paintArcAorundPoint(cp.x, cp.y, nextDiam, swingStartAngle, -1 * angleGrow, false, g);
                    startAngle += angleGrow;
                    swingStartAngle -= angleGrow;

                }
                nextDiam = nextDiam - step;
            }
        }
    }

    public void startToExplode() {
        t = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point absCenter = getAbsoluteCenter();
                repeatNr++;
                int actDiameter = diameter + calcExplodedCenterDistance() * 2 + strokesize * 2;
                setBoundsAroundCenter(absCenter, actDiameter, actDiameter);
                repaint();
                if (repeatNr >= maxRepeatNr) {
                    //repeatNr = -1;
                    repaint();
                    t.stop();
                    showGift();
                    state = SpaceObjectState.DEAD;
                }
            }
        });
        t.start();
    }
    
    private void showGift(){
        Gold gold = new Gold();
        Point center = getAbsoluteCenter();
        gold.setBounds(
                center.x-gold.getComponentWidth()/2, 
                center.y-gold.getComponentHeight()/2, 
                gold.getComponentWidth(), 
                gold.getComponentHeight());
        mainGameFrame.addGift(gold);
    }

    @Override
    public void handleCollision(SpaceObject other) {
        this.state = SpaceObjectState.AGOZNIZING;
        startToExplode();
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

    public int getDiameter() {
        return diameter;
    }

    private int calcExplodedDiameter() {
        return diameter + calcExplodedCenterDistance();

    }

    private int calcExplodedCenterDistance() {
        return calcStep() * repeatNr;

    }

    private int calcStep() {
        int diff = explodedDiameter - diameter;
        int step = diff / maxRepeatNr;
        return step;
    }

    @Override
    public void move() {
        Rectangle bounds = getBounds();
        setBounds(bounds.x - planetAnimationSpeed, bounds.getBounds().y, getComponentWidth(), getComponentHeight());

    }

    @Override
    public Shape getApproximationShape() {
        Shape sh = new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
        return sh;
    }

    @Override
    public void beingHit(int damage) {
        bulletResistance-=damage;
        if (bulletResistance <= 0) {
            this.state = SpaceObjectState.AGOZNIZING;
            startToExplode();
        }
    }

}
