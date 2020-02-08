/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.infoobjects;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 *
 * @author peti
 */
public class GameCompletedMenu extends JComponent {

    private static final String CONGRAT =           "   CONGRATULATIONS!";
    private static final String GAME_COMPLETE =     "   YOU WON THE GAME";
    private static final String PRESS_TO_CONT =     "Press enter to restart, or esc to quit!";
    private static final int FONT_SIZE_LARGE = 70;
    private static final int FONT_SIZE_SMALL = 40;
    private int width;
    private int height;

    public GameCompletedMenu() {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Font largeFont = largeFont();
        Font smallFont = smallFont();

        width = (int) (smallFont.getStringBounds(PRESS_TO_CONT, frc).getWidth());
        System.out.println("width: of  PRESS_TO_CONT: " + width);
        height = (int) (largeFont.getStringBounds(CONGRAT, frc).getHeight());
        height += (int) (largeFont.getStringBounds(GAME_COMPLETE, frc).getHeight());
        height += (int) (smallFont.getStringBounds(PRESS_TO_CONT, frc).getHeight());
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.WHITE);
        g.setFont(largeFont());
        g.drawString(CONGRAT, 0, FONT_SIZE_LARGE);
        
        //g.setFont(largeFont());
        g.drawString(GAME_COMPLETE, 0, FONT_SIZE_LARGE*2);
        
        g.setFont(smallFont());
        g.drawString(PRESS_TO_CONT, 0, FONT_SIZE_LARGE*2+FONT_SIZE_SMALL);
    }

    private Font largeFont(){
        return new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_LARGE);
    }

    private Font smallFont(){
        return new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL);
    }

    @Override
    public int getWidth() {
        
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
