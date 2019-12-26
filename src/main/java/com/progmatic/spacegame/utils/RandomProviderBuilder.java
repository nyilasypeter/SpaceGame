package com.progmatic.spacegame.utils;

import java.util.ArrayList;
import java.util.List;

public class RandomProviderBuilder<T> {

    List<Pair<T, Integer>> pairs = new ArrayList<>();

    public RandomProviderBuilder<T> add(T t, Integer i){
        pairs.add(new Pair<>(t, i));
        return this;
    }

    public RandomProvider<T> build(){
        return new RandomProvider(pairs);
    }
}
