/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import java.awt.Rectangle;

/**
 *
 * @author peti
 */
public abstract class UnMovingSpaceObject extends SpaceObject{

    @Override
    public boolean isOutOfGameField(Rectangle rectangle) {
        return false;
    }
    
}
