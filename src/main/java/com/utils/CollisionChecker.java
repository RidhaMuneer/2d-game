package com.utils;

import com.entity.Entity;
import com.panel.GamePanel;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    /**
     * Checks if the given entity is colliding with any tiles in the game map.
     * If the entity is moving up, it checks the two tiles above it. If the entity
     * is moving down, it checks the two tiles below it. If the entity is moving
     * left, it checks the two tiles to its left. If the entity is moving right,
     * it checks the two tiles to its right. If either of the tiles is a solid
     * tile, it sets the entity's collisionOn flag to true.
     */
    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entityTopWorldY + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tl.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tl.mapTileNum[entityRightCol][entityTopRow];
                if(gp.tl.tile[tileNum1].collision == true || gp.tl.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tl.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tl.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tl.tile[tileNum1].collision == true || gp.tl.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tl.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tl.mapTileNum[entityLeftCol][entityBottomRow];
                if(gp.tl.tile[tileNum1].collision == true || gp.tl.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tl.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tl.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tl.tile[tileNum1].collision == true || gp.tl.tile[tileNum2].collision == true){
                    entity.collisionOn = true;
                }
                break;
        }
    }

    /**
     * Checks if the given entity is colliding with any objects in the game map.
     * The entity's solid area is moved in the direction it is moving, and then
     * checked for intersection with the solid area of each object. If the entity
     * is colliding with an object, its collisionOn flag is set to true.
     * 
     * If the entity is a player, the index of the object it is colliding with is
     * returned. Otherwise, 9999 is returned.
     * 
     * @param entity the entity to check for collision
     * @param player whether the entity is a player
     * @return the index of the object the entity is colliding with, if it is a
     *         player, otherwise 9999
     */
    public int checkObject(Entity entity, boolean player){
        int index = 9999;

        for (int i = 0; i < gp.objects.length; i++) {
            if(gp.objects[i] != null){
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                gp.objects[i].solidArea.x = gp.objects[i].worldX + gp.objects[i].solidArea.x;
                gp.objects[i].solidArea.y = gp.objects[i].worldY + gp.objects[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(gp.objects[i].solidArea)){
                            if(gp.objects[i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(gp.objects[i].solidArea)){
                            if(gp.objects[i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(gp.objects[i].solidArea)){
                            if(gp.objects[i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(gp.objects[i].solidArea)){
                            if(gp.objects[i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objects[i].solidArea.x = gp.objects[i].solidAreaDefaultX;
                gp.objects[i].solidArea.y = gp.objects[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}
