/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.spaceobjects.Planet;
import com.progmatic.spacegame.spaceobjects.RightToLeftSpaceObject;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.spaceobjects.enemy.GrowShrinkStar;
import com.progmatic.spacegame.spaceobjects.enemy.GrowShrinkPlanet;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author peti
 */
public class SpaceObjectProvider {

    private static final SpaceObjectProvider me = new SpaceObjectProvider();
    private final Random r;
    private Dimension sizeOfGameField;
    private final static Map<Integer, RandomProvider> spaceObjectsPerLevel = new HashMap<>();
    private final static Map<Integer, Integer> nrOfSpaceObjectsPerLevel = new HashMap<>();
    private int level = 1;
    
    
    static{
        spaceObjectsPerLevel.put(1, new RandomProvider(
                new Pair<>(Planet.class.getName(), 100)));
        nrOfSpaceObjectsPerLevel.put(1, 5);
        
        spaceObjectsPerLevel.put(2, new RandomProvider(
                new Pair<>(Planet.class.getName(), 75),
                new Pair<>(GrowShrinkPlanet.class.getName(), 25)));
        nrOfSpaceObjectsPerLevel.put(2, 6);
    
    }

    private SpaceObjectProvider() {
        this.r = new Random();
    }
    
    public void configure(Dimension d){
        this.sizeOfGameField = d;
    }

    public static SpaceObjectProvider instance() {
        return me;
    }

    public SpaceObject replace(SpaceObject toReplace) {
        if (shouldReplace(toReplace)) {
            return createSpaceObject();
        }
        return null;
    }
    
    public List<SpaceObject> initSpaceObjects(){
        List<SpaceObject> ret = new ArrayList<>();
        for (int i = 0; i < nrOfSpaceObjectsPerLevel.get(level); i++) {
            ret.add(createSpaceObject());
        }
        return ret;
    }
    
    private SpaceObject createSpaceObject(){
        RandomProvider rp = spaceObjectsPerLevel.get(level);
        String className = rp.getRandomString();
        if(Planet.class.getName().equals(className)){
            return createRandomPlanet();
        }
        else if(GrowShrinkStar.class.getName().equals(className)){
            return createRandomGrowShrinkStar();
        }
        else if(GrowShrinkPlanet.class.getName().equals(className)){
            return createRandomGrowShrinkPlanet();
        }
        else{
            throw new RuntimeException("unknown spaceobject returned by RandomProvider.getRandomString(): " + className);
        }
    }

    private Planet createRandomPlanet() {
        Planet p = new Planet();
        setRandomBounds(p);
        return p;
    }
    
    private GrowShrinkPlanet createRandomGrowShrinkPlanet() {
        GrowShrinkPlanet p = new GrowShrinkPlanet();
        setRandomBounds(p);
         
        return p;
    }
    
    private GrowShrinkStar createRandomGrowShrinkStar() {
        GrowShrinkStar p = new GrowShrinkStar();
        p.setBounds(
                r.nextInt(sizeOfGameField.width/2) + sizeOfGameField.width,
                r.nextInt(sizeOfGameField.height/2) - p.getComponentHeight() / 2,
                p.getComponentWidth(),
                p.getComponentHeight());
        return p;
    }
    
    private void setRandomBounds(RightToLeftSpaceObject p){
        p.setBounds(
                r.nextInt(sizeOfGameField.width) + sizeOfGameField.width,
                r.nextInt(sizeOfGameField.height) - p.getComponentHeight() / 2,
                p.getComponentWidth(),
                p.getComponentHeight());
    }

    private boolean shouldReplace(SpaceObject toReplace) {
        return (toReplace instanceof RightToLeftSpaceObject);
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    
    
    private static class RandomProvider{
        
        Pair<String, Integer>[] randomPairs;
        Random r = new Random();

        public RandomProvider(Pair<String, Integer>... randomPairs) {
            int sum = 0;
            for (Pair<String, Integer> randomPair : randomPairs) {
                sum += randomPair.getValue();
            }
            if(sum != 100){
                throw new RuntimeException("Sum of values must be 100 int RandomProvider's constructor");
            }
            this.randomPairs = randomPairs;
            Arrays.sort(this.randomPairs, Comparator.comparing(p -> p.getValue()));
        }
        
        public String getRandomString(){
            int rand = r.nextInt(100)+1;
            int sum = 0;
            for (Pair<String, Integer> pair : randomPairs) {
                sum += pair.getValue();
                if(rand <= sum){
                    return pair.getKey();
                }
            }
            throw new RuntimeException("Error in getRranodmString");
        }
        
        
        
        
    }

}
