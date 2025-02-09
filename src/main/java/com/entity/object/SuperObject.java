package com.entity.object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dialog.DialogBox;
import com.panel.GamePanel;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public final DialogBox db = new DialogBox();

    public boolean isActive = false;

    public SuperObject(String name, boolean collision){
        this.name = name;
        this.collision = collision;
        String path = "/objects/" + name + ".png";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
        }
    }

/**
 * Draws the object on the game panel, calculating its screen position
 * relative to the player's position. If the object is within the player's
 * view, it is drawn on the screen. If the object is active, a dialog box
 * is also displayed, allowing interaction via mouse clicks.
 * 
 * @param g2 the Graphics2D object used for drawing
 * @param gp the GamePanel containing the game state and player information
 */
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(
            worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY
        )
        {
            if(this.isActive){
                db.setVisible(isActive);
                db.draw(g2, gp);
                gp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        db.handleMouseClick(e);
                    }
                });
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }else {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}
