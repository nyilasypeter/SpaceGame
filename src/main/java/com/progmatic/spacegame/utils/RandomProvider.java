/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author peti
 */
public class RandomProvider {

    Pair<String, Integer>[] randomPairs;
    Random r = new Random();

    public RandomProvider(Pair<String, Integer>... randomPairs) {
        int sum = 0;
        for (Pair<String, Integer> randomPair : randomPairs) {
            sum += randomPair.getValue();
        }
        if (sum != 100) {
            throw new RuntimeException("Sum of values must be 100 int RandomProvider's constructor");
        }
        this.randomPairs = randomPairs;
        Arrays.sort(this.randomPairs, Comparator.comparing(p -> p.getValue()));
    }

    public String getRandomString() {
        int rand = r.nextInt(100) + 1;
        int sum = 0;
        for (Pair<String, Integer> pair : randomPairs) {
            sum += pair.getValue();
            if (rand <= sum) {
                return pair.getKey();
            }
        }
        throw new RuntimeException("Error in getRranodmString");
    }

    public Object getRandomObject() {
        try {
            String className = getRandomString();
            return Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
