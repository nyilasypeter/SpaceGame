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
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 *
 * @author peti
 */
public class GameOverMenu extends JComponent {

    private static final String GAME_OVER = "GAME OVER";
    private static final String PRESS_TO_CONT = "Press enter to start from level $";
    private static final String PRESS_TO_CONT2 = "or space to start over!";
    private static final int FONT_SIZE = 100;
    private static final int FONT_SIZE_SMALL = 40;
    private final int width;
    private final int height;
    private final int row_1_startAt;
    private final int row_2_startAt;
    private final int row_3_startAt;
    private final String textToDraw;

    public GameOverMenu(int lastLevel) {
        textToDraw = PRESS_TO_CONT.replace("$", Integer.toString(lastLevel));
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
        final Rectangle2D row1Bounds = font.getStringBounds(GAME_OVER, frc);
        int h1 = (int) (row1Bounds.getHeight());

        Font font2 = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL);
        final Rectangle2D row2Bounds = font2.getStringBounds(textToDraw, frc);
        int h2 = (int) (row2Bounds.getHeight());
        
        final Rectangle2D row3Bounds = font2.getStringBounds(PRESS_TO_CONT2, frc);
        int h3 = (int) (row3Bounds.getHeight());
        
        height = h1 + h2 + h3;
        width = (int) (Math.max(Math.max(row1Bounds.getWidth(),
                row2Bounds.getWidth()), row3Bounds.getWidth()));
        
        row_1_startAt = (int) ((width - row1Bounds.getWidth())/2);
        row_2_startAt = (int) ((width - row2Bounds.getWidth())/2);
        row_3_startAt = (int) ((width - row3Bounds.getWidth())/2);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
        g.drawString(GAME_OVER, row_1_startAt, FONT_SIZE);

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL));
        g.drawString(textToDraw, row_2_startAt, FONT_SIZE + FONT_SIZE_SMALL);
        
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE_SMALL));
        g.drawString(PRESS_TO_CONT2, row_3_startAt, FONT_SIZE + FONT_SIZE_SMALL + FONT_SIZE_SMALL);

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
