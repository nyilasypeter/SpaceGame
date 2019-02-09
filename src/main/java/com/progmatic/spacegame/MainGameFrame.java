/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.infoobjects.GameOverMenu;
import com.progmatic.spacegame.infoobjects.InfoObject;
import com.progmatic.spacegame.utils.CollisionChecker;
import com.progmatic.spacegame.spaceobjects.enemy.GrowShrinkStar;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.Planet;
import com.progmatic.spacegame.spaceobjects.projectile.Projectile;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.listeners.MainFrameComponentListener;
import com.progmatic.spacegame.listeners.SpaceshipDirectKeyListener;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private final List<SpaceObject> spaceObjects = Collections.synchronizedList(new ArrayList<>());
    private InfoObject infoObject;
    private Timer mainAnimator;
    private boolean initialized = false;

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
        add(sp);
        SpaceshipDirectKeyListener skListener = new SpaceshipDirectKeyListener(sp, getContentPane().getSize(), this);
        addKeyListener(skListener);
        MainFrameComponentListener mfcl = new MainFrameComponentListener(skListener);
        addComponentListener(mfcl);
    }

    public void addGrowShrinkStar() {
        GrowShrinkStar b = new GrowShrinkStar();
        b.setBounds(500, 500, 120, 120);
        add(b);
        b.startToExplode();
    }

    public void addBullet(Projectile b) {
        add(b);
        spaceObjects.add(b);
    }

    public void addGift(Gift gift) {
        add(gift);
        spaceObjects.add(gift);
    }

    /**
     * the MainFrame should be intiailized after it is fully built up.
     */
    public void initializeIfNeeded() {
        if (!initialized) {
            SpaceObjectProvider.sizeOfGameField = getContentPane().getSize();
            initialized = true;
            for (int i = 0; i < 5; i++) {
                SpaceObject sp = SpaceObjectProvider.instance().createSpaceObject();
                spaceObjects.add(sp);
                add(sp);
            }
            mainAnimator = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainTimerFired();
                }

            });
            mainAnimator.start();

            Rectangle frameBounds = getBounds();
            infoObject = new InfoObject(sp);
            add(infoObject);
            infoObject.setBounds(frameBounds.width - 200, 30, 200, 200);
            infoObject.repaint();
        }

    }

    private void mainTimerFired() {
        ListIterator<SpaceObject> li = spaceObjects.listIterator();

        while (li.hasNext()) {
            SpaceObject so = li.next();
            if (so.isOutOfGameField(getContentPane().getBounds())) {
                SpaceObject other = SpaceObjectProvider.instance().replace(so);
                remove(so);
                li.remove();
                if (other != null) {
                    this.add(other);
                    li.add(other);
                }

            } else {
                if (so.getState().equals(SpaceObjectState.ALIVE)) {
                    so.move();
                    if (CollisionChecker.collided(so, sp)) {
                        so.handleCollision(sp);
                        sp.handleCollision(so);
                    }
                    checkHit(so);
                } else if (so.getState().equals(SpaceObjectState.AGOZNIZING)) {
                    so.move();
                } else if (so.getState().equals(SpaceObjectState.DEAD)) {
                    SpaceObject gift = so.createGiftAfterDying();
                    SpaceObject other = SpaceObjectProvider.instance().replace(so);
                    remove(so);
                    li.remove();
                    if (other != null) {
                        this.add(other);
                        li.add(other);
                    }
                    if (gift != null) {
                        li.add(gift);
                        add(gift);
                    }
                    repaint();

                }

            }
        }
        if (sp.getLife() == 0) {
            gameOver();
        }
        infoObject.repaint();
    }

    public void gameOver() {
        mainAnimator.stop();
        Rectangle frameBounds = getBounds();
        GameOverMenu goMenu = new GameOverMenu();
        add(goMenu, 1);
        goMenu.setBounds(frameBounds.width / 2 - goMenu.getWidth() / 2,
                frameBounds.height / 2 - goMenu.getHeight() / 2,
                goMenu.getWidth(),
                goMenu.getHeight());
//goMenu.setBounds(10, 10, 200, 200);
        goMenu.repaint();

    }

    private void checkHit(SpaceObject so) {
        if (so instanceof Projectile) {
            Projectile projectile = (Projectile) so;
            for (SpaceObject spaceObject : spaceObjects) {
                if (spaceObject.getState().equals(SpaceObjectState.ALIVE) && spaceObject instanceof Hitable) {
                    Hitable hitable = (Hitable) spaceObject;
                    if (CollisionChecker.collided(projectile, spaceObject)) {
                        hitable.beingHit(projectile.damage());
                        projectile.hitTheTarget();
                    }
                }
            }
        }
    }

}
