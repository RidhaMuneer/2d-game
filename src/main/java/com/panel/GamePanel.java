package com.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.entity.Player;
import com.entity.object.SuperObject;
import com.handlers.KeyHandler;
import com.tile.TileManager;
import com.utils.AssetSetter;
import com.utils.CollisionChecker;

public class GamePanel extends JPanel implements Runnable {
    final int orginalTileSize = 16;
    final int scale = 3;

    final public int tileSize = orginalTileSize * scale;

    final public int maxScreenCol = 16;
    final public int maxScreenRow = 12;
    final public int screenWidth = tileSize * maxScreenCol;
    final public int screenHeight = tileSize * maxScreenRow;

    // World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxScreenRow;

    final int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    public Player player = new Player(this, keyHandler);

    public TileManager tl = new TileManager(this);

    public CollisionChecker collisionChecker = new CollisionChecker(this);

    public SuperObject objects[] = new SuperObject[10];

    public AssetSetter assetSetter = new AssetSetter(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

/**
 * Sets up the game objects in the game panel.
 * 
 * This method uses the AssetSetter to initialize and place game objects
 * in their starting positions within the game world.
 */
    public void setupGameObjects(){
        assetSetter.setObject();
    }

    /**
     * Starts the game thread. This method should be called once the game
     * panel is set up and ready to be run. It creates a new thread and starts
     * it, which will run the game loop.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Main game loop. Updates the game state and redraws the game panel as
     * fast as possible, while limiting the frame rate to the specified FPS.
     * 
     * The loop runs until the game thread is stopped, at which point the game
     * panel is no longer updated.
     */
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    /**
     * Updates the game state by updating the player's position and collision status.
     * 
     * This method is called once per frame, and is responsible for updating the
     * game state.
     */
    public void update(){
        player.update();
    }

/**
 * Paints the components of the game panel, including the tiles, objects, and player.
 * 
 * This method overrides the JPanel's paintComponent to draw the current state of the game.
 * It first calls the superclass's paintComponent to ensure the panel is properly rendered,
 * then it uses Graphics2D to draw all visible tiles, objects, and the player on the screen.
 * 
 * @param g the Graphics object used for drawing
 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        tl.draw(g2);
        for (SuperObject object : objects) {
            if (object != null) {
                object.draw(g2, this);
            }
        }
        player.draw(g2);
        g2.dispose();
    }
}
