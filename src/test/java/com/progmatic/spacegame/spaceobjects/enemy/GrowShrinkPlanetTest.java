/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.spaceobjects.enemy;

import com.progmatic.spacegame.SpaceObjectState;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peti
 */
public class GrowShrinkPlanetTest {

    public GrowShrinkPlanetTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of paintComponent method, of class GrowShrinkPlanet.
     */
    @Test
    public void testPaintComponent() {
    }

    /**
     * Test of getRelativeCenter method, of class GrowShrinkPlanet.
     */
    @Test
    public void testGetRelativeCenter() {
    }

    /**
     * Test of getComponentWidth method, of class GrowShrinkPlanet.
     */
    @Test
    public void testGetComponentWidth() {
    }

    /**
     * Test of getComponentHeight method, of class GrowShrinkPlanet.
     */
    @Test
    public void testGetComponentHeight() {
    }

    /**
     * Test of move method, of class GrowShrinkPlanet.
     */
    @Test
    public void testMove() {
        GrowShrinkPlanet gp = new GrowShrinkPlanet(1);
        int x = 200;
        int y = 100;
        gp.setBounds(x, y, gp.getComponentWidth(), gp.getComponentHeight());
        Point absoluteCenter = gp.getAbsoluteCenter();
        System.out.println("absoluteCenter: " + absoluteCenter);
        for (int i = 0; i < 100; i++) {
            gp.move();
            Point actAbsoluteCenter = gp.getAbsoluteCenter();
            assertEquals(absoluteCenter.y, actAbsoluteCenter.y, 1);
            assertNotEquals(x, gp.getBounds().x);
            x = actAbsoluteCenter.x;
        }
    }

    @Test
    public void testMove2() {
        GrowShrinkPlanet gp = new GrowShrinkPlanet(1);
        int x = 200;
        int y = 100;
        gp.setBounds(x, y, gp.getComponentWidth(), gp.getComponentHeight());
        Point absoluteCenter = gp.getAbsoluteCenter();
        System.out.println("absoluteCenter: " + absoluteCenter);

        Point centerBeforeAgonoizing = gp.getAbsoluteCenter();
        System.out.println("centerBeforeAgonoizing: " + centerBeforeAgonoizing);
        gp.setState(SpaceObjectState.AGOZNIZING);
        for (int i = 0; i < 100; i++) {

            gp.move();
            if (gp.getState().equals(SpaceObjectState.AGOZNIZING)) {
                Point actAbsoluteCenter = gp.getAbsoluteCenter();
                assertEquals(centerBeforeAgonoizing, actAbsoluteCenter);
            }
        }
    }

    /**
     * Test of getApproximationShape method, of class GrowShrinkPlanet.
     */
    @Test
    public void testGetApproximationShape() {
    }

    /**
     * Test of createGiftAfterDying method, of class GrowShrinkPlanet.
     */
    @Test
    public void testCreateGiftAfterDying() {
    }

}
