package com.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.entity.object.SuperObject;
import com.panel.GamePanel;
import com.handlers.KeyHandler;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 18;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;

        setDefaultValues();
        getPlayerImage();
    }

/**
 * Sets the default values for the player's position, speed, and direction.
 * Initializes the player's world coordinates to a specific tile position,
 * sets the movement speed, and defines the initial direction facing down.
 */
    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    /**
     * Loads the player's image from the resources folder.
     * 
     * The images are stored in the resources folder under the path
     * "/player/boy_{direction}_{frame}.png", where {direction} is one of
     * "up", "down", "left", or "right", and {frame} is one of "1" or "2".
     * 
     * Catches and prints stack trace for any IOException that occurs.
     */
    public void getPlayerImage(){
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the player's state based on the input from the user.
     * 
     * This method is called once per frame, and is responsible for updating the
     * player's direction, checking for collision with tiles and objects, and
     * moving the player accordingly. It also handles the animation of the
     * player.
     */
    public void update() {
        if(keyHandler.upPressed == false && keyHandler.downPressed == false && keyHandler.leftPressed == false && keyHandler.rightPressed == false) {
            return;
        }

        if(keyHandler.upPressed == true) {
            direction = "up";
        }
        if(keyHandler.downPressed == true) {
            direction = "down";
        }
        if(keyHandler.leftPressed == true) {
            direction = "left";
        }
        if(keyHandler.rightPressed == true) {
            direction = "right";
        }

        collisionOn = false;
        gp.collisionChecker.checkTile(this);

        handleObjectInteraction(gp.collisionChecker.checkObject(this, true));

        if(collisionOn == false){
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    /**
     * Handle object interaction.
     * 
     * This method is called when the player has moved into a new tile in the game world.
     * It first resets all object active states, and then activates only the object the player
     * is touching (if any).
     * 
     * @param index the index of the object the player is touching, if any
     */
    public void handleObjectInteraction(int index){
        for (SuperObject object : gp.objects) {
            if (object != null) {
                object.isActive = false;
            }
        }
    
        if(index != 9999){
            gp.objects[index].isActive = true;
        }
    }

    /**
     * Draws the player on the screen.
     * 
     * This method is called once per frame, and is responsible for drawing the player
     * at its current position on the screen. The player's direction is used to determine
     * which image to draw, and the player's animation is handled by switching between
     * the two images for each direction.
     * 
     * @param g2 the Graphics2D object used for drawing
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if(spriteNum == 1) {
                    image = up1;    
                }
                if(spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;    
                }
                if(spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;    
                }
                if(spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;    
                }
                if(spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
