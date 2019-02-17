/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.infoobjects.GameOverMenu;
import com.progmatic.spacegame.infoobjects.InfoObject;
import com.progmatic.spacegame.infoobjects.NextLevelMenu;
import com.progmatic.spacegame.utils.CollisionChecker;
import com.progmatic.spacegame.spaceobjects.enemy.GrowShrinkStar;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.projectile.Projectile;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.listeners.MainFrameComponentListener;
import com.progmatic.spacegame.listeners.SpaceshipDirectKeyListener;
import com.progmatic.spacegame.spaceobjects.enemy.FiringEnemy;
import com.progmatic.spacegame.spaceobjects.gifts.Gift;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
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
    private int actLevel = 1;
    private SpaceshipDirectKeyListener skListener;
    private NextLevelMenu nextLevMenu;

    private static final int[] LEVEL_SCORES = {1000, 2000, 3000};

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
        skListener = new SpaceshipDirectKeyListener(sp, getContentPane().getSize(), this);
        addKeyListener(skListener);
        MainFrameComponentListener mfcl = new MainFrameComponentListener(skListener);
        addComponentListener(mfcl);
    }

    public void addGrowShrinkStar() {
        GrowShrinkStar b = new GrowShrinkStar();
        b.setBounds(200, 200, b.getComponentWidth(), b.getComponentHeight());
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
            initialized = true;
            SpaceObjectProvider.instance().configure(getContentPane().getSize());
            SpaceObjectProvider.instance().setLevel(actLevel);
            for (SpaceObject so : SpaceObjectProvider.instance().initSpaceObjects()) {
                spaceObjects.add(so);
                add(so);
            }
            if (mainAnimator == null) {
                mainAnimator = new Timer(20, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainTimerFired();
                    }

                });
            }
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
//                    if (CollisionChecker.collided(so, sp)) {
//                        so.handleCollision(sp);
//                        sp.handleCollision(so);
//                    }
                    checkHit(so);
                    if(so instanceof FiringEnemy){
                        FiringEnemy enemy = (FiringEnemy) so;
                        List<Projectile> projectiles = enemy.getProjectiles();
                        if(projectiles != null){
                            for (Projectile projectile : projectiles) {
                                this.add(projectile);
                                li.add(projectile);
                            }
                        }
                    }
                    repaint();
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
        if (getLevelByScore(sp.getScore()) > actLevel) {
            showNextLevelMenu();
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
        goMenu.repaint();

    }

    private void showNextLevelMenu() {
        mainAnimator.stop();
        Rectangle frameBounds = getBounds();
        nextLevMenu = new NextLevelMenu(actLevel);
        add(nextLevMenu, 1);
        nextLevMenu.setBounds(frameBounds.width / 2 - nextLevMenu.getWidth() / 2,
                frameBounds.height / 2 - nextLevMenu.getHeight() / 2,
                nextLevMenu.getWidth(),
                nextLevMenu.getHeight());
        nextLevMenu.repaint();
        skListener.setInNextLevelMenu();
    }
    
    public void nextLevel(){
        actLevel++;
        SpaceObjectProvider.instance().setLevel(actLevel);
        for (SpaceObject spaceObject : spaceObjects) {
            remove(spaceObject);
        }
        spaceObjects.clear();
        initialized = false;
        initializeIfNeeded();
        remove(nextLevMenu);
        sp.setLife(Math.max(sp.getLife(), 4));
        repaint();
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

    private int getLevelByScore(int score) {
        for (int i = 0; i < LEVEL_SCORES.length; i++) {
            if (score <= LEVEL_SCORES[i]) {
                return i + 1;
            }
        }
        return LEVEL_SCORES.length + 1;
    }

}
