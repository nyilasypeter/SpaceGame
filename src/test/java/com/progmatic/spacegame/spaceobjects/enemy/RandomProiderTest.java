package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.utils.RandomProvider;
import com.progmatic.spacegame.utils.RandomProviderBuilder;
import org.junit.Assert;
import org.junit.Test;

public class RandomProiderTest {

    @Test
    public void testRandomProvider(){
        RandomProviderBuilder<String> builder = new RandomProviderBuilder();
        RandomProvider<String> rp = builder
                .add("csoki", 70)
                .add("vanillia", 15)
                .add("eper", 15)
                .build();
        int csokiCnt = 0, vaniliaCnt= 0, eperCnt = 0;
        for (int i = 0; i < 1000; i++) {
            String rstr = rp.getRandomObject();
            switch (rstr){
                case "csoki": csokiCnt++;break;
                case "vanillia": vaniliaCnt++; break;
                case "eper": eperCnt++; break;
                default:;
            }
        }
        //might fail with an extremely low probability...
        Assert.assertEquals(700, csokiCnt, 100);
        Assert.assertEquals(150, vaniliaCnt, 100);
        Assert.assertEquals(150, eperCnt, 100);
        System.out.println(" csoki: " + csokiCnt + " vanillia: " + vaniliaCnt + " eper: " + eperCnt);
    }
}
