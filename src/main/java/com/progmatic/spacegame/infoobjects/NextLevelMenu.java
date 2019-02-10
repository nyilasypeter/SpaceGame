/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.infoobjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 *
 * @author peti
 */
public class NextLevelMenu extends JComponent {

    private static final String CONGRAT =           "   CONGRATULATIONS!";
    private static final String LEVEL_COMPLETE =    "YOU COMPLETED LEVEL $.";
    private static final String PRESS_TO_CONT =     "        Press enter to continue!";
    private static final int FONT_SIZE_LARGE = 70;
    private static final int FONT_SIZE_SMALL = 40;
    private int width;
    private int height;
    private final String textToDraw;

    public NextLevelMenu(int actLevel) {
        textToDraw = LEVEL_COMPLETE.replace("$", Integer.toString(actLevel));
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_LARGE);
        width = (int) (font.getStringBounds(textToDraw, frc).getWidth());
        height = (int) (font.getStringBounds(textToDraw, frc).getHeight())*2;
        
        Font font2 = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL);
        height += (int) (font2.getStringBounds(PRESS_TO_CONT, frc).getHeight());
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_LARGE));
        g.drawString(CONGRAT, 0, FONT_SIZE_LARGE);
        
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_LARGE));
        g.drawString(textToDraw, 0, FONT_SIZE_LARGE*2);
        
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL));
        g.drawString(PRESS_TO_CONT, 0, FONT_SIZE_LARGE*2+FONT_SIZE_SMALL);
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
