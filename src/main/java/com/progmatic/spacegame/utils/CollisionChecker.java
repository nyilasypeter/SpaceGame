/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.utils;

import com.progmatic.spacegame.spaceobjects.SpaceObject;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author peti
 */
public class CollisionChecker {

    public static boolean collided(SpaceObject so1, SpaceObject so2) {
        Shape apprShape = so1.getApproximationShape();
        Shape apprShape2 = so2.getApproximationShape();
        if (apprShape != null && apprShape2 != null) {
            //oval, oval
            if (apprShape instanceof Arc2D && apprShape2 instanceof Arc2D) {
                Arc2D a1 = (Arc2D) apprShape;
                Arc2D a2 = (Arc2D) apprShape2;
                return ovalsCollided(a1, a2);
            }
            //rectangle, rectangle
            else if (apprShape instanceof Rectangle2D && apprShape2 instanceof Rectangle2D) {
                Rectangle2D r1 = (Rectangle2D) apprShape;
                Rectangle2D r2 = (Rectangle2D) apprShape2;
                return r1.intersects(r2);
            }
            //rectangle, oval
            Shape[] sos = isInstanceOf(apprShape, apprShape2, Arc2D.class, Rectangle2D.class);
            if (sos != null) {
                Arc2D a2 = (Arc2D) sos[0];
                Rectangle2D r = (Rectangle2D) sos[1];
                return a2.intersects(r);
            }
            //oval, line
            //TODO make it smarter
            sos =isInstanceOf(apprShape, apprShape2, Arc2D.class, Line2D.class);
            if (sos != null) {
                Arc2D arc = (Arc2D) sos[0];
                Line2D line = (Line2D) sos[1];
                return arc.getBounds2D().intersectsLine(line);
            }
            //rectangle, line
            sos =isInstanceOf(apprShape, apprShape2, Rectangle2D.class, Line2D.class);
            if (sos != null) {
                Rectangle2D rect = (Rectangle2D) sos[0];
                Line2D line = (Line2D) sos[1];
                return rect.intersectsLine(line);
            }
            throw new RuntimeException(String.format("Collision detection is not supported for these shapes: %s, and: %s", 
                    apprShape.getClass().getName(),
                    apprShape2.getClass().getName()));
        }
        throw new RuntimeException("Collision detection of shapes not supporting getApproximationShape is not yet implemented!");

    }

    private static boolean ovalsCollided(Arc2D a1, Arc2D a2) {
        Point center1 = new Point((int) a1.getCenterX(), (int) a1.getCenterY());
        Point center2 = new Point((int) a2.getCenterX(), (int) a2.getCenterY());
        double distance = center1.distance(center2);
        double radiusDistance = Math.max(a1.getHeight(), a1.getWidth()) + Math.max(a2.getHeight(), a2.getWidth());
        return distance < radiusDistance/2;

    }

    private static Shape[] isInstanceOf(Shape so1, Shape so2, Class<? extends Shape> aClass, Class<? extends Shape> aClass0) {
        if (aClass.isInstance(so1) && aClass0.isInstance(so2)) {
            return new Shape[]{so1, so2};
        }
        else if(aClass.isInstance(so2) && aClass0.isInstance(so1)){
            return new Shape[]{so2, so1};
        }
        return null;

    }
}
