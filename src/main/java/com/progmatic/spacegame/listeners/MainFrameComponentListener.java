/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.listeners;

import com.progmatic.spacegame.MainGameFrame;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 *
 * Handles window resize.
 *
 * @author peti
 */
public class MainFrameComponentListener implements ComponentListener {

    SpaceshipDirectKeyListener spaceshipDirectKeyListener;
    boolean palentsInitialiezd = false;

    public MainFrameComponentListener(SpaceshipDirectKeyListener spaceshipDirectKeyListener) {
        this.spaceshipDirectKeyListener = spaceshipDirectKeyListener;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        MainGameFrame mFrame = (MainGameFrame) e.getComponent();
        Dimension size = mFrame.getSize();
        spaceshipDirectKeyListener.setMainFrameDimensions(size);

//         if (!palentsInitialiezd) {
//            mFrame.addPlanets();
//            palentsInitialiezd = true;
//        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
       
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

}
