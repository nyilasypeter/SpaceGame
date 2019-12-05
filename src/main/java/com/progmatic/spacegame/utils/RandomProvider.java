/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * RandomProvider can create objects randomly but with a given probability.
 * E.g. if we intitialize the provider this way
 * RandomProvider rp = new RandomProvider(
 *                 new Pair<>(Circle.class, 82),
 *                 new Pair<>(Rectangle.class, 18)));
 * then
 * rp.getRandomObject() will return either a Circle object (with 82% probability)
 * or a Rectangle object (with 18% probability)
 * @author peti
 *
 */
public class RandomProvider {

    Pair<Class, Integer>[] randomPairs;
    Random r = new Random();

    public RandomProvider(Pair<Class, Integer>... randomPairs) {
        int sum = 0;
        for (Pair<Class, Integer> randomPair : randomPairs) {
            sum += randomPair.getValue();
        }
        if (sum != 100) {
            throw new RuntimeException("Sum of values must be 100 in RandomProvider's constructor");
        }
        this.randomPairs = randomPairs;
        Arrays.sort(this.randomPairs, Comparator.comparing(p -> p.getValue()));
    }

    public Class getRandomString() {
        int rand = r.nextInt(100) + 1;
        int sum = 0;
        for (Pair<Class, Integer> pair : randomPairs) {
            sum += pair.getValue();
            if (rand <= sum) {
                return pair.getKey();
            }
        }
        throw new RuntimeException("Error in getRranodmString");
    }

    public Object getRandomObject() {
        try {
            Class clazz = getRandomString();
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
