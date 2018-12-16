/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.projectile;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.SpaceObject;

/**
 *
 * @author peti
 */
public abstract class Projectile extends SpaceObject{
    public void hitTheTarget(){
        this.state = SpaceObjectState.DEAD;
    }
    public abstract int damage();
}
