/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.projectile.Hitable;
import java.awt.Rectangle;

/**
 *
 * @author peti
 */
public abstract class RightToLeftSpaceObject extends SpaceObject implements Hitable {

    protected int bulletResistance = 3;

    @Override
    public boolean isOutOfGameField(Rectangle rectangle) {
        return this.getBounds().x < 0 - this.getWidth();
    }

    @Override
    public void handleCollision(SpaceObject other) {
        this.state = SpaceObjectState.AGOZNIZING;
    }

    @Override
    public void beingHit(int damage) {
        bulletResistance -= damage;
        if (bulletResistance <= 0) {
            this.state = SpaceObjectState.AGOZNIZING;
            //startToExplode();
        }
    }

}
