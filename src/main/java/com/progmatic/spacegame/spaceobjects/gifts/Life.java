/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.gifts;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author peti
 */
public class Life extends Gift {

    private static final Color SUN = Color.decode("#fff716");
    private static final Color TEXT = Color.decode("#F57900");

    private final int baseRadius;
    private final int raylength;
    private final int nrOfRays;
    private int lifeNum = 1;
    
    public Life(){
        this.baseRadius = 20;
        this.raylength = 10;
        this.nrOfRays = 10;
    }

    public Life(int lifeNum){
        this.lifeNum = lifeNum;
        this.baseRadius = 20;
        this.raylength = 10;
        this.nrOfRays = 10;
    }

    public Life(int baseRadius, int raylength, int nrOfRays) {
        this.baseRadius = baseRadius;
        this.raylength = raylength;
        this.nrOfRays = nrOfRays;
    }   

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(SUN);

        Point center = getRelativeCenter();
        paintCircleAroundPoint(center.x, center.y, baseRadius, true, g);

        int fullDia = baseRadius + raylength;
        int smallDia = baseRadius / 2;

        int actAngle = 0;
        int angleGrow = 360 / nrOfRays;

        for (int i = 0; i < nrOfRays; i++) {
            Point p1 = calcPointOnCircle(center, fullDia, actAngle);
            Point p2 = calcPointOnCircle(center, smallDia, actAngle - (angleGrow / 2));
            Point p3 = calcPointOnCircle(center, smallDia, actAngle + (angleGrow / 2));
            fillTriangle(p1, p2, p3, g);
            actAngle += angleGrow;
        }

        String text = String.valueOf(lifeNum);
        g.setColor(TEXT);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        g.setFont(font);
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Rectangle2D stringBounds = font.getStringBounds(text, frc);
        g.drawString(text,(int)(center.x-stringBounds.getWidth()/2),(int)(center.y+stringBounds.getHeight()/2));

    }

    private void fillTriangle(Point p1, Point p2, Point p3, Graphics g) {
        int[] xPoints = new int[]{p1.x, p2.x, p3.x};
        int[] yPoints = new int[]{p1.y, p2.y, p3.y};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private Point calcPointOnCircle(Point center, int distance, double angle) {
        int x = (int) (Math.sin(Math.toRadians(angle)) * distance);
        int y = (int) (Math.cos(Math.toRadians(angle)) * distance);
        return new Point(center.x + x, center.y - y);
    }

    @Override
    public int getComponentWidth() {
        return (baseRadius + raylength)*2;
    }

    @Override
    public int getComponentHeight() {
        return getComponentWidth();
    }

    @Override
    public Shape getApproximationShape() {
        return getBounds();
    }

    @Override
    public int getValue() {
        return 10;
    }

    public int getLifeNum() {
        return lifeNum;
    }
}
