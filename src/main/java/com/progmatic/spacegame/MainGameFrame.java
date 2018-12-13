/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.components.Blinker;
import com.progmatic.spacegame.components.Exploder;
import com.progmatic.spacegame.components.GrowShrinkStar;
import com.progmatic.spacegame.components.Spaceship;
import com.progmatic.spacegame.components.Planet;
import com.progmatic.spacegame.components.SpaceObject;
import com.progmatic.spacegame.listeners.MainFrameComponentListener;
import com.progmatic.spacegame.listeners.SpaceshipDirectKeyListener;
import com.sun.java.accessibility.util.SwingEventMonitor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author peti
 */
public class MainGameFrame extends JFrame {

    Spaceship sp;

    private List<SpaceObject> spaceObjects = Collections.synchronizedList(new ArrayList<>());
    Timer planetAnimator;

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
    
    public void addBlinker(){
        Blinker b = new Blinker();
        b.setBounds(100, 100, 120, 120);
        add(b);
        b.startBlinking();
    }
    
    public void addExploder(){
        Exploder b = new Exploder();
        b.setBounds(300, 300, 120, 120);
        add(b);
        b.startToExplode();
    }
    
    public void addGrowShrinkStar(){
        GrowShrinkStar b = new GrowShrinkStar();
        b.setBounds(500, 500, 120, 120);
        add(b);
        b.startToExplode();
    }

    public void addPlanetsIfNeeded() {

        if (spaceObjects.isEmpty()) {
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
                                if (sp.collided(so)) {
                                    so.handleCollision();
                                    sp.handleCollision();
                                }
                            }
                            else if (so.getState().equals(SpaceObjectState.DEAD)) {
                                remove(so);
                                repaint();
                                li.remove();
                                li.add(createRandomPlanet());
                            } 

                        }
                    }
                }
            });
            planetAnimator.start();
        }

    }

    private Planet createRandomPlanet() {
        Dimension size = getContentPane().getSize();
        Planet p = new Planet();
        add(p);
        p.setBounds(r.nextInt(size.width) + size.width, r.nextInt(size.height-100), p.getComponentWidth(), p.getComponentHeight());
        return p;
    }

}
