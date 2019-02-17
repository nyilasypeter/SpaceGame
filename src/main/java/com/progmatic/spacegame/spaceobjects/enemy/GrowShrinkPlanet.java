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
import com.progmatic.spacegame.spaceobjects.projectile.LeaserBeam;
import com.progmatic.spacegame.spaceobjects.projectile.Projectile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class GrowShrinkPlanet extends RightToLeftSpaceObject implements Hitable, FiringEnemy {

    private final Random r = new Random();
    private final int origDiameter;
    private int diameter;
    private final int strokesize;
    private final Color color;
    private final Color thornColor;
    private final Gift gift;

    private int repeatNr = 0;
    private Timer t;

    private final int explodedDiameter;
    private final int maxRepeatNr = 75;//15
    private final int maxThornRepeatNr = 150;
    private final int nrOfPieces = 10;

    private final int nrOfThorns;

    private final int planetSpeed;

    private int growShinkCounter = 0;
    private final int sizeToGrow;
    
    private Point absCent;
    
    private final int projectileLength;
    private final double projectileSpeed;
    private List<Projectile> projectiles;

    public GrowShrinkPlanet() {
        this.origDiameter = r.nextInt(150) + 30;
        this.diameter = origDiameter;
        this.bulletResistance = (origDiameter / 50) + 10;
        this.explodedDiameter = origDiameter + 50;
        this.planetSpeed = r.nextInt(5) + 1;
        this.strokesize = 1;
        this.sizeToGrow = r.nextInt(origDiameter) + 50;
        this.color = randomColor();
        this.thornColor = randomColor();
        this.nrOfThorns = r.nextInt(5) + 7;
        int giftType = r.nextInt(4);
        if (giftType >= 2) {
            gift = new Life();
        } else {
            gift = new Gold(r.nextInt(100) + 200);
        }
        projectileSpeed = Math.max(0.1, r.nextDouble());
        projectileLength = r.nextInt(100) + 40;
    }

    private Color randomColor() {
        return Color.getHSBColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height); 
        initAbsoluteCenter();
    }
    
    

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r); 
        initAbsoluteCenter();
    }
    
    private void initAbsoluteCenter(){
        if(absCent == null){
            Rectangle bounds = getBounds();
            Point location = bounds.getLocation();
            location.x += bounds.width / 2;
            location.y += bounds.height / 2;
            absCent = location;
        }
    }
    
    

    private void calcDiameter() {
        growShinkCounter++;
        if (growShinkCounter > maxThornRepeatNr) {
            growShinkCounter = 0;
            diameter = origDiameter;
        } else {
            double thornStep = calcThornStep();
            diameter += thornStep;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            paintPlanet(g);
            paintThorns(g);

        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            paintExplodedPlanet(g);
        }

    }

    private void paintPlanet(Graphics g) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        Point center = getRelativeCenter();
        paintCircleAroundPoint(center.x, center.y, origDiameter, true, g);
    }

    private void paintThorns(Graphics g) {
        g.setColor(thornColor);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(strokesize));
        Point center = getRelativeCenter();
        int startAngle = 0;
        int angleGrow = 360 / nrOfThorns;
        if (growShinkCounter == maxThornRepeatNr-1) {
                projectiles = new ArrayList<>();
            }
        for (int i = 0; i < nrOfThorns; i++) {
            Point cp = calcPointFromOhterPointByAngle(center, diameter / 2, startAngle);
            Point lp = calcPointFromOhterPointByAngle(center, origDiameter / 2, startAngle - angleGrow / 3);
            Point rp = calcPointFromOhterPointByAngle(center, origDiameter / 2, startAngle + angleGrow / 3);
            g.fillPolygon(new int[]{cp.x, lp.x, rp.x}, new int[]{cp.y, lp.y, rp.y}, 3);

            int diameterIn = origDiameter - (diameter - origDiameter);
            Point lpIn = calcPointFromOhterPointByAngle(center, diameterIn / 2, startAngle - angleGrow / 3);
            Point rpIn = calcPointFromOhterPointByAngle(center, diameterIn / 2, startAngle + angleGrow / 3);
            g.fillPolygon(new int[]{cp.x, lpIn.x, rpIn.x}, new int[]{cp.y, lpIn.y, rpIn.y}, 3);
            
            if (growShinkCounter == maxThornRepeatNr-1) {
                Point p = new Point();
                Rectangle bounds = getBounds();
                p.x = cp.x + bounds.x;
                p.y = cp.y + bounds.y;
                projectiles.add(new LeaserBeam(startAngle, p, projectileSpeed, projectileLength));
            }

            startAngle += angleGrow;
        }
    }

    private void paintExplodedPlanet(Graphics g) {
        Point center = getRelativeCenter();
        diameter = origDiameter;
        int centerDist = calcExplodedCenterDistance();
        int startAngle = 0;
        int angleGrow = 360 / nrOfPieces;
        int swingStartAngle = 90 + angleGrow / 2;
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(strokesize));
        for (int i = 0; i < nrOfPieces; i++) {
            Point cp = calcPointFromOhterPointByAngle(center, centerDist, startAngle);
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

    

    private int calcExplodedCenterDistance() {
        int ret = (int) (calcStep() * repeatNr);
        return ret;

    }

    private double calcStep() {
        int diff = explodedDiameter - origDiameter;
        double step = (double) diff / maxRepeatNr;
        return step;
    }

    private double calcThornStep() {
        double step = (double) sizeToGrow / maxThornRepeatNr;
        if ((int) step == 0) {
            step = 1d;
        }
        return step;
    }

    @Override
    public void move() {
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            calcDiameter();
            absCent.x = absCent.x - planetSpeed;
            setBoundsAroundCenter(absCent, getComponentWidth(), getComponentHeight());
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            repeatNr++;
            int actDiameter = origDiameter + calcExplodedCenterDistance() * 2 + strokesize * 2;
            setBoundsAroundCenter(absCent, actDiameter, actDiameter);
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
        gift.setBounds(
                absCent.x - gift.getComponentWidth() / 2,
                absCent.y - gift.getComponentHeight() / 2,
                gift.getComponentWidth(),
                gift.getComponentHeight());
        return gift;
    }

    @Override
    public List<Projectile> getProjectiles() {
        if(projectiles != null){
            List<Projectile> ret = projectiles;
            projectiles = null;
            return ret;
        }
        return null;
    }

}
