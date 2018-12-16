/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.gifts;

import com.progmatic.spacegame.SpaceObjectState;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import java.util.Random;

/**
 *
 * @author peti
 */
public abstract class Gift extends SpaceObject{
    
    protected final Random r = new Random();
    private final long createdAt;
    private final long timeToLive;

    public Gift() {
        this.timeToLive = r.nextInt(10_000)+1000;
        this.createdAt = System.currentTimeMillis();
    }
    
    @Override
    public void move() {
        if(System.currentTimeMillis()-createdAt > timeToLive){
            this.setState(SpaceObjectState.DEAD);
        }
    }
    
    public int getValue() {
        return 0;
    }  
    
}
