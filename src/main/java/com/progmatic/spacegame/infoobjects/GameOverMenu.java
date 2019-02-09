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
public class GameOverMenu extends JComponent {

    private static final String GAME_OVER = "GAME OVER";
    private static final int FONT_SIZE = 100;
    private int width;
    private int height;

    public GameOverMenu() {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
        width = (int) (font.getStringBounds(GAME_OVER, frc).getWidth());
        height = (int) (font.getStringBounds(GAME_OVER, frc).getHeight());
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
        g.drawString(GAME_OVER, 0, FONT_SIZE);
        g.getFontMetrics().stringWidth(GAME_OVER);
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
