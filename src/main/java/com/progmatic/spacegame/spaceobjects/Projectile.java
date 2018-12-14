/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects;

import com.progmatic.spacegame.SpaceObjectState;

/**
 *
 * @author peti
 */
public abstract class Projectile extends SpaceObject{
    public void hitTheTarget(){
        this.state = SpaceObjectState.DEAD;
    }
}
