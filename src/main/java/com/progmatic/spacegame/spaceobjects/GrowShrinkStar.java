/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class GrowShrinkStar extends SpaceObject {

    private int repeatNr = 0;
    private Timer t;

    private int diameter = 50;
    private int explodedDiameter = 100;
    private int maxRepeatNr = 30;
    private int nrOfPieces = 10;

    Point center = new Point(50, 50);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.CYAN);
        if (repeatNr == 0) {
            paintCircleAorundPoint(50, 50, diameter, true, g);
        } else if (repeatNr == -1) {

        } else {
            int actDiameter = calcExplodedDiameter();
            int centerDist = calcExplodedCenterDistance();
            int startAngle = 0;
            for (int i = 0; i < nrOfPieces; i++) {
                Point cp = calcCenterOfPiece(center, centerDist, startAngle);

                paintArcAorundPoint(cp.x, cp.y, diameter, startAngle, 36, true, g);
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
        if (repeatNr == 0) {
            return diameter;
        } else if (repeatNr > 0) {
            calcExplodedDiameter();
        }
        return 0;
    }

    @Override
    public int getComponentHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Point calcCenterOfPiece(Point center, int distance, double angle) {
        int x = (int) (Math.sin(Math.toRadians(angle)) * distance);
        int y = (int) (Math.cos(Math.toRadians(angle)) * distance);
        return new Point(center.x + x, center.y + y);
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
    
    }

    @Override
    public Shape getApproximationShape() {
        return new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
    }

    
   

}
