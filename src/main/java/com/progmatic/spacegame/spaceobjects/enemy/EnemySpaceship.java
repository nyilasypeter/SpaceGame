package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.events.SpaceshipMotionListener;
import com.progmatic.spacegame.spaceobjects.RightToLeftSpaceObject;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import com.progmatic.spacegame.spaceobjects.gifts.Gold;
import com.progmatic.spacegame.spaceobjects.projectile.*;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public class EnemySpaceship extends RightToLeftSpaceObject implements Hitable, FiringEnemy {

    protected Color WINDOW_COLOR_ALIVE = Color.decode("#891616");//#f3ff00
    protected Color SPACESIP_COLOR_TOP = Color.decode("#821175");
    protected Color SPACESIP_COLOR_BOTTOM = Color.decode("#D5880D");
    protected Color WINDOW_COLOR_DEAD = Color.WHITE;
    protected Color SPACESHIP_COLOR_AGONIZING = Color.lightGray;

    private int life = 4;
    private int nrOfMissiles = 4;

    protected int repeatNr = 0;
    protected int maxRepeatNr = 50;
    protected int speed = 6;
    protected Point absCent;
    protected double projectileSpeed = 0.4;
    protected int projectileLength = 30;
    protected int fireCounter = 0;
    protected int fireFrequency = 30;
    protected int nrOfProjectiles = 1;
    protected int firingDegree = 30;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            paintSpaceship(g);
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            paintAgonizingSpaceship(g);
        }

        setVisible(true);
    }

    private void paintSpaceship(Graphics g) {
        g.setColor(SPACESIP_COLOR_TOP);
        g.fillOval(20, 0, 60, 100);
        g.setColor(SPACESIP_COLOR_BOTTOM);
        g.fillRect(10, 35, 80, 60);
        g.fillOval(0, 30, 100, 70);


        g.setColor(WINDOW_COLOR_ALIVE);
        int windowStart = 15;
        for (int i = 0; i < 4; i++) {
            if (i >= life) {
                g.setColor(WINDOW_COLOR_DEAD);
            }
            g.fillOval(windowStart, 60, 10, 10);
            windowStart += 20;
        }
    }

    private void paintAgonizingSpaceship(Graphics g) {
        g.setColor(SPACESHIP_COLOR_AGONIZING);
        g.fillOval(20, 0, 60, 100);
        g.fillRect(10, 35, 80, 60);
        g.fillOval(0, 30, 100, 70);

        g.setColor(WINDOW_COLOR_DEAD);
        int windowStart = 15;
        for (int i = 0; i < 4; i++) {
            g.fillOval(windowStart, 60, 10, 10);
            windowStart += 20;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension();
        d.height = 100;
        d.width = 100;
        return d;
    }

    @Override
    public int getComponentWidth() {
        return 100;
    }

    @Override
    public int getComponentHeight() {
        return 100;
    }

    public Point getMyCenter() {
        Rectangle bounds = getBounds();
        Point ret = new Point(bounds.x + 50, bounds.y + 50);
        return ret;
    }

    @Override
    public void move() {
        if (this.state.equals(SpaceObjectState.ALIVE)) {
            absCent.x = absCent.x - speed;
            setBoundsAroundCenter(absCent, getComponentWidth(), getComponentHeight());
        } else if (this.state.equals(SpaceObjectState.AGOZNIZING)) {
            repeatNr++;
            if (repeatNr >= maxRepeatNr) {
                state = SpaceObjectState.DEAD;
            }
        }
    }


    @Override
    public void handleCollision(SpaceObject other) {
        if (other instanceof Spaceship) {
            life--;
        }
        if (life <= 0) {
            setState(SpaceObjectState.AGOZNIZING);
        }
    }

    public Projectile fireMissile() {
        nrOfMissiles--;
        if (nrOfMissiles >= 0) {
            Missile m = new Missile();
            Rectangle myBounds = getBounds();
            m.setBounds(myBounds.x + getComponentWidth() + 10, myBounds.y + 70, m.getComponentWidth(), m.getComponentHeight());
            return m;
        } else {
            return null;
        }
    }

    @Override
    public Shape getApproximationShape() {
        return new Arc2D.Double(getBounds(), 0, 360, Arc2D.Double.CHORD);
    }

    @Override
    public void beingHit(int damage) {
        life -= damage;
        if (life <= 0) {
            setState(SpaceObjectState.AGOZNIZING);
        }
    }

    public int getNrOfMissiles() {
        return Math.max(0, nrOfMissiles);
    }

    public void setNrOfMissiles(int nrOfMissiles) {
        this.nrOfMissiles = nrOfMissiles;
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

    private void initAbsoluteCenter() {
        if (absCent == null) {
            Rectangle bounds = getBounds();
            Point location = bounds.getLocation();
            location.x += bounds.width / 2;
            location.y += bounds.height / 2;
            absCent = location;
        }
    }


    @Override
    public List<Projectile> getProjectiles() {
        fireCounter++;
        if (fireCounter == fireFrequency) {
            fireCounter = 0;
            List<Projectile> projectiles = new ArrayList<>();

            projectiles.add(new LeaserBeam(270, getLeaseBeamStartPoint(), projectileSpeed, projectileLength));
            double grow = firingDegree / Math.max(1, nrOfProjectiles - 1);
            double angleUp = 270;
            double angleDown = 270;
            for (int i = 0; i < (nrOfProjectiles - 1) / 2; i++) {

                angleUp += grow;
                projectiles.add(new LeaserBeam(angleUp, getLeaseBeamStartPoint(), projectileSpeed, projectileLength));
                angleDown -= grow;
                projectiles.add(new LeaserBeam(angleDown, getLeaseBeamStartPoint(), projectileSpeed, projectileLength));

            }

            return projectiles;
        }
        return null;
    }

    private Point getLeaseBeamStartPoint(){
        Rectangle myBounds = getBounds();
        Point p = new Point();
        p.x = myBounds.x - 10;
        p.y = myBounds.y + 70;
        return p;
    }

    @Override
    public SpaceObject createGiftAfterDying() {
        Gift gift = new Gold(300);
        Point center = getAbsoluteCenter();
        gift.setBounds(
                center.x - gift.getComponentWidth() / 2,
                center.y - gift.getComponentHeight() / 2,
                gift.getComponentWidth(),
                gift.getComponentHeight());
        return gift;
    }

}
