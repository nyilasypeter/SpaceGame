/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import com.progmatic.spacegame.spaceobjects.gifts.Gold;
import com.progmatic.spacegame.spaceobjects.gifts.Life;
import com.progmatic.spacegame.spaceobjects.gifts.MissilePack;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import com.progmatic.spacegame.utils.Pair;
import com.progmatic.spacegame.utils.RandomProvider;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class Planet extends RightToLeftSpaceObject implements Hitable {

    private final Random r = new Random();
    private final int diameter;
    private final int strokesize;
    private final Color color;
    private final int nrOfExtraCircles;
    private final boolean[] isFilledCircle;
    private final Color[] extraCircleColors;
    private Gift gift;
    private int repeatNr = 0;
    private Timer t;
    private final int level;

    private final int explodedDiameter;
    private final int maxRepeatNr = 75;//15
    private final int nrOfPieces = 10;

    private final int planetAnimationSpeed;

    private final static Map<Integer, RandomProvider> giftsPerLevel = new HashMap<>();

    static {
        giftsPerLevel.put(1, new RandomProvider(
                new Pair<>(Gold.class, 80),
                new Pair<>(Life.class, 10),
                new Pair<>(MissilePack.class, 10)));

        giftsPerLevel.put(2, new RandomProvider(
                new Pair<>(Gold.class, 60),
                new Pair<>(Life.class, 20),
                new Pair<>(MissilePack.class, 20)));

        giftsPerLevel.put(3, new RandomProvider(
                new Pair<>(Gold.class, 40),
                new Pair<>(Life.class, 30),
                new Pair<>(MissilePack.class, 30)));

        giftsPerLevel.put(4, new RandomProvider(
                new Pair<>(Gold.class, 20),
                new Pair<>(Life.class, 40),
                new Pair<>(MissilePack.class, 40)));
    }

    public Planet(int level) {
        this.diameter = r.nextInt(250) + 30;
        this.bulletResistance = (diameter / 50) + 2;
        this.explodedDiameter = diameter + 50;
        switch (level) {
            case 1:
                this.planetAnimationSpeed = r.nextInt(10) + 5;
                break;
            default:
                this.planetAnimationSpeed = r.nextInt(10) + 10;
        }

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
        this.level = level;

    }

    private Color randomColor() {
        return Color.getHSBColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
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
            Point cp = calcPointFromOhterPointByAngle(center, centerDist, startAngle);
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
                    Point cp = calcPointFromOhterPointByAngle(center, centerDist, startAngle);
                    paintArcAorundPoint(cp.x, cp.y, nextDiam, swingStartAngle, -1 * angleGrow, false, g);
                    startAngle += angleGrow;
                    swingStartAngle -= angleGrow;

                }
                nextDiam = nextDiam - step;
            }
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

    private int calcExplodedCenterDistance() {
        int ret = (int) (calcStep() * repeatNr);
        return ret;

    }

    private double calcStep() {
        int diff = explodedDiameter - diameter;
        double step = (double) diff / maxRepeatNr;
        return step;
    }

    @Override
    public void move() {
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            Rectangle bounds = getBounds();
            setBounds(bounds.x - planetAnimationSpeed, bounds.getBounds().y, getComponentWidth(), getComponentHeight());
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            Point absCenter = getAbsoluteCenter();
            repeatNr++;
            int actDiameter = diameter + calcExplodedCenterDistance() * 2 + strokesize * 2;
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
        RandomProvider rp = giftsPerLevel.get(level);
        gift = (Gift) rp.getRandomObject();
        Point center = getAbsoluteCenter();
        gift.setBounds(
                center.x - gift.getComponentWidth() / 2,
                center.y - gift.getComponentHeight() / 2,
                gift.getComponentWidth(),
                gift.getComponentHeight());
        return gift;
    }

}
