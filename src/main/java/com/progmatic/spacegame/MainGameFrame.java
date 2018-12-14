/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.spaceobjects.Blinker;
import com.progmatic.spacegame.spaceobjects.Bullet;
import com.progmatic.spacegame.utils.CollisionChecker;
import com.progmatic.spacegame.spaceobjects.Exploder;
import com.progmatic.spacegame.spaceobjects.GrowShrinkStar;
import com.progmatic.spacegame.spaceobjects.Hitable;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.Planet;
import com.progmatic.spacegame.spaceobjects.Projectile;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.listeners.MainFrameComponentListener;
import com.progmatic.spacegame.listeners.SpaceshipDirectKeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class MainGameFrame extends JFrame {

    private Spaceship sp;

    private List<SpaceObject> spaceObjects = Collections.synchronizedList(new ArrayList<>());
    private Timer planetAnimator;
    private boolean initialized = false;

    /**
     * pixel / 100 ms
     */
    Random r = new Random();

    public MainGameFrame() {
    }

    public void init() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        //setSize(800, 600);
        getContentPane().setBackground(Color.DARK_GRAY);

        //no layout manager is used, because we want to use absolute positioning
        setLayout(null);
        //pack();

    }

    public void addSpaceShip() {
        sp = new Spaceship();
        sp.setBounds(100, 100, sp.getComponentWidth(), sp.getComponentHeight());
        sp.setVisible(true);
        add(sp);
        SpaceshipDirectKeyListener skListener = new SpaceshipDirectKeyListener(sp, getContentPane().getSize(), this);
        addKeyListener(skListener);
        MainFrameComponentListener mfcl = new MainFrameComponentListener(skListener);
        addComponentListener(mfcl);

    }

    public void addBlinker() {
        Blinker b = new Blinker();
        b.setBounds(100, 100, 120, 120);
        add(b);
        b.startBlinking();
    }

    public void addExploder() {
        Exploder b = new Exploder();
        b.setBounds(300, 300, 120, 120);
        add(b);
        b.startToExplode();
    }

    public void addGrowShrinkStar() {
        GrowShrinkStar b = new GrowShrinkStar();
        b.setBounds(500, 500, 120, 120);
        add(b);
        b.startToExplode();
    }

    public void addBullet(Bullet b) {
        add(b);
        spaceObjects.add(b);
    }

    public void initializeIfNeeded() {
        if (!initialized) {
            initialized = true;
            for (int i = 0; i < 6; i++) {
                spaceObjects.add(createRandomPlanet());
            }
            planetAnimator = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ListIterator<SpaceObject> li = spaceObjects.listIterator();
                    while (li.hasNext()) {
                        SpaceObject so = li.next();
                        if (so.getBounds().x < 0 - so.getWidth()) {
                            li.remove();
                            li.add(createRandomPlanet());

                        } else {
                            if (so.getState().equals(SpaceObjectState.ALIVE)) {
                                so.move();
                                if (CollisionChecker.collided(so, sp)) {
                                    so.handleCollision();
                                    sp.handleCollision();
                                }
                                checkHit(so);
                            } else if (so.getState().equals(SpaceObjectState.DEAD)) {
                                remove(so);
                                repaint();
                                li.remove();
                                if (so instanceof Planet) {
                                    li.add(createRandomPlanet());
                                }
                            }

                        }
                    }
                }
            });
            planetAnimator.start();
        }

    }

    private void checkHit(SpaceObject so) {
        if (so instanceof Projectile) {
            Projectile projectile = (Projectile) so;
            for (SpaceObject spaceObject : spaceObjects) {
                if (spaceObject.getState().equals(SpaceObjectState.ALIVE) && spaceObject instanceof Hitable) {
                    Hitable hitable = (Hitable) spaceObject;
                    if (CollisionChecker.collided(projectile, spaceObject)) {
                        hitable.beingHit();
                        projectile.hitTheTarget();
                    }
                }
            }
        }
    }

    private Planet createRandomPlanet() {
        Dimension size = getContentPane().getSize();
        Planet p = new Planet();
        add(p);
        p.setBounds(
                r.nextInt(size.width) + size.width, 
                r.nextInt(size.height) - p.getComponentHeight() / 2, 
                p.getComponentWidth(), 
                p.getComponentHeight());
        return p;
    }

}