package com.game;

import javax.swing.JFrame;

import com.panel.GamePanel;

/**
 * Hello world!
 *
 */
public class App 
{
    /**
     * Entry point for the application.
     * 
     * This method creates a new JFrame that will hold the game panel and starts
     * the game thread.
     * 
     * @param args command line arguments
     */
    public static void main( String[] args )
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("2D Game");

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.setupGameObjects();
        gamePanel.startGameThread();
    }
}
