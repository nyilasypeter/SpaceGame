/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.RightToLeftSpaceObject;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import java.awt.Color;
import java.awt.Graphics;
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
public class GrowShrinkStar extends RightToLeftSpaceObject {

    private final Random r = new Random();

    private int repeatNr = 0;
    private Timer t;

    private int radius;
    private int explodedRadius;
    private int maxRepeatNr = 30;
    private int nrOfPieces = 10;
    private int actDiameter;

    private final int planetAnimationSpeed;

    public GrowShrinkStar() {
        this.radius = 50;
        this.actDiameter = radius;
        this.explodedRadius = 150;
        planetAnimationSpeed = 10;
        this.bulletResistance = (radius * 3 / 50) + 2;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN);
        if (repeatNr == 0) {
            paintCircleAroundPoint(radius, radius, radius, true, g);
        } else if (repeatNr == -1) {

        } else {
            actDiameter = calcExplodedDiameter();
            int centerDist = calcExplodedCenterDistance();

            int startAngle = 0;
            Point center = new Point(radius, radius);
            Point absCenter = getAbsoluteCenter();
            setBoundsAroundCenter(absCenter, actDiameter*2, actDiameter*2);

            for (int i = 0; i < nrOfPieces; i++) {
                Point cp = calcCenterOfPiece(center, centerDist, startAngle);

                paintArcAorundPoint(cp.x, cp.y, actDiameter, startAngle, 36, true, g);
                startAngle += 36;

            }
            //paintCircleAorundPoint(50, 50, diameter, true, g);
        }

    }

    public void startToExplode() {
        t = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repeatNr++;
                repaint();
                if (repeatNr > maxRepeatNr) {
                    repeatNr = 0;
//                    repeatNr = -1;
//                    repaint();
//                    t.stop();
                }
            }
        });
        t.setInitialDelay(1000);
        t.start();
    }

    @Override
    public int getComponentWidth() {
        //if (state.equals(SpaceObjectState.AGOZNIZING)) {
        System.out.println(actDiameter);
        return (actDiameter * 2 + 2);
        //}
        //return (radius * 2);
//        if (repeatNr == 0) {
//            return diameter;
//        } else if (repeatNr > 0) {
//            calcExplodedDiameter();
//        }
//        return 0;
    }

    @Override
    public int getComponentHeight() {
        return getComponentWidth();
    }

    private Point calcCenterOfPiece(Point center, int distance, double angle) {
        int x = (int) (Math.sin(Math.toRadians(angle)) * distance);
        int y = (int) (Math.cos(Math.toRadians(angle)) * distance);
        return new Point(center.x + x, center.y + y);
    }

    private int calcExplodedDiameter() {
        return radius + calcExplodedCenterDistance();

    }

    private int calcExplodedCenterDistance() {
        return calcStep() * repeatNr;

    }

    private int calcStep() {
        int diff = explodedRadius - radius;
        int step = diff / maxRepeatNr;
        return step;
    }

    @Override
    public void move() {
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            Rectangle bounds = getBounds();
            setBounds(bounds.x - planetAnimationSpeed, bounds.getBounds().y, getComponentWidth(), getComponentHeight());
        }

    }

    @Override
    public Shape getApproximationShape() {
        return new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
    }

    @Override
    public void beingHit(int damage) {
        this.state = SpaceObjectState.AGOZNIZING;
    }

}
