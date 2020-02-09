/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame;

import com.progmatic.spacegame.spaceobjects.Planet;
import com.progmatic.spacegame.spaceobjects.RightToLeftSpaceObject;
import com.progmatic.spacegame.spaceobjects.SpaceObject;
import com.progmatic.spacegame.spaceobjects.Spaceship;
import com.progmatic.spacegame.spaceobjects.enemy.EnemySpaceship;
import com.progmatic.spacegame.spaceobjects.enemy.FollowingEnemySpaeceship;
import com.progmatic.spacegame.spaceobjects.enemy.GrowShrinkPlanet;
import com.progmatic.spacegame.utils.RandomProvider;
import com.progmatic.spacegame.utils.RandomProviderBuilder;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author peti
 */
public class SpaceObjectProvider {

    private static final SpaceObjectProvider me = new SpaceObjectProvider();
    private final Random r;
    private Dimension sizeOfGameField;
    private final static Map<Integer, RandomProvider<Class>> spaceObjectsPerLevel = new HashMap<>();
    private final static Map<Integer, Integer> nrOfSpaceObjectsPerLevel = new HashMap<>();
    private int level = 1;
    private Spaceship spaceship = new Spaceship();

    private static final int[] LEVEL_SCORES = {2000, 4000, 6000, 10000};
    //private static final int[] LEVEL_SCORES = {100, 200, 300, 500};

    static {
        spaceObjectsPerLevel.put(1,
                new RandomProviderBuilder<Class>()
                        .add(Planet.class, 100)
                        .build());
        nrOfSpaceObjectsPerLevel.put(1, 5);

        spaceObjectsPerLevel.put(2, new RandomProviderBuilder<Class>()
                .add(Planet.class, 80)
                .add(EnemySpaceship.class, 20)
                .build());
        nrOfSpaceObjectsPerLevel.put(2, 5);

        spaceObjectsPerLevel.put(3, new RandomProviderBuilder<Class>()
                .add(Planet.class, 72)
                .add(EnemySpaceship.class, 10)
                .add(GrowShrinkPlanet.class, 18)
                .build());
        nrOfSpaceObjectsPerLevel.put(3, 6);

        spaceObjectsPerLevel.put(4, new RandomProviderBuilder<Class>()
                .add(Planet.class, 50)
                .add(GrowShrinkPlanet.class, 30)
                .add(FollowingEnemySpaeceship.class, 20)
                .build());
        nrOfSpaceObjectsPerLevel.put(4, 6);

    }

    private SpaceObjectProvider() {
        this.r = new Random();
    }

    public void configure(Dimension d) {
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

    public List<SpaceObject> initSpaceObjects() {
        List<SpaceObject> ret = new ArrayList<>();
        for (int i = 0; i < nrOfSpaceObjectsPerLevel.get(level); i++) {
            ret.add(createSpaceObject());
        }
        return ret;
    }

    private SpaceObject createSpaceObject() {
        RandomProvider<Class> rp = spaceObjectsPerLevel.get(level);
        Class className = rp.getRandomObject();
        if (Planet.class.equals(className)) {
            return createRandomPlanet();
        } else if (GrowShrinkPlanet.class.equals(className)) {
            return createRandomGrowShrinkPlanet();
        }
        else if(EnemySpaceship.class.equals(className)){
            return createEnemySpaceship();
        }
        else if(FollowingEnemySpaeceship.class.equals(className)){
            return createFollowingEnemySpaceship();
        }
        else {
            throw new RuntimeException("unknown spaceobject returned by RandomProvider.getRandomString(): " + className);
        }
    }

    private Planet createRandomPlanet() {
        Planet p = new Planet(level);
        setRandomBounds(p);
        return p;
    }

    private GrowShrinkPlanet createRandomGrowShrinkPlanet() {
        GrowShrinkPlanet p = new GrowShrinkPlanet(level);
        setRandomBounds(p);
        return p;
    }

    private EnemySpaceship createEnemySpaceship(){
        EnemySpaceship sp = new EnemySpaceship();
        setRandomBounds(sp);
        return sp;
    }

    private FollowingEnemySpaeceship createFollowingEnemySpaceship(){
        FollowingEnemySpaeceship sp = new FollowingEnemySpaeceship(spaceship.getBounds());
        spaceship.addSpaceshipMotionListener(sp);
        setRandomBounds(sp);
        return sp;
    }

    private void setRandomBounds(RightToLeftSpaceObject p) {
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

    public int getLevelByScore(int score) {
        for (int i = 0; i < LEVEL_SCORES.length; i++) {
            if (score <= LEVEL_SCORES[i]) {
                return i + 1;
            }
        }
        return LEVEL_SCORES.length + 1;
    }

    public int getNrofLevels(){
        return spaceObjectsPerLevel.size();
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public Dimension getSizeOfGameField() {
        return new Dimension(sizeOfGameField);
    }
}
