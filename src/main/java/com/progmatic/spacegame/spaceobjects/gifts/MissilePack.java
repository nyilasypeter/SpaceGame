/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.gifts;

import com.progmatic.spacegame.spaceobjects.projectile.Missile;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author peti
 */
public class MissilePack extends Gift{
    
    
    private final int nrOfMissiles;
    private final List<Missile> missiles = new ArrayList<>();
    private final int gap = 10;
    private final Random r = new Random();

    public MissilePack() {
        this.nrOfMissiles = r.nextInt(4)+2;
        int height = 0;
        for (int i = 0; i < nrOfMissiles; i++) {
            Missile m = new Missile();
            missiles.add(m);
            this.add(m);
            m.setBounds(0, height, m.getComponentWidth(), m.getComponentHeight());
            height += m.getComponentHeight() + gap;
        }
    }

    public MissilePack(int nrOfMissiles){
        this.nrOfMissiles = nrOfMissiles;
        int height = 0;
        for (int i = 0; i < nrOfMissiles; i++) {
            Missile m = new Missile();
            missiles.add(m);
            this.add(m);
            m.setBounds(0, height, m.getComponentWidth(), m.getComponentHeight());
            height += m.getComponentHeight() + gap;
        }
    }
    
    

    @Override
    public int getComponentWidth() {
        if(missiles.isEmpty()){
            return 0;
        }
        return missiles.get(0).getComponentWidth();
    }

    @Override
    public int getComponentHeight() {
        if(missiles.isEmpty()){
            return 0;
        }
        return (missiles.get(0).getComponentHeight()+gap) * missiles.size();
               
    }

    @Override
    public Shape getApproximationShape() {
        return getBounds();
    }

    public int getNrOfMissiles() {
        return nrOfMissiles;
    }
    
}
