package com.utils;

import com.entity.object.SuperObject;
import com.panel.GamePanel;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    /**
     * Set the objects in the game panel.
     * 
     * This method sets the initial position and properties of the objects in the game panel.
     * 
     * The objects currently set are:
     *  - A key object located at (23, 7) in the game world
     */
    public void setObject(){
        // this supposed to be Socrates the philosopher, yet too lazy to make the art
        gp.objects[0] = new SuperObject("key", false);
        gp.objects[0].worldX = 23 * gp.tileSize;
        gp.objects[0].worldY = 7 * gp.tileSize;
    }
}
