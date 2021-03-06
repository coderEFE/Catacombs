package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.entities.Laser;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 6/2/2018.
 * Stun Gun shoots stun item.lasers
 * It can be used by the Player or by Guards
 */

public class StunGun {

    private Vector2 position;
    public float gunWidth = 6f;
    public Enums.Facing gunFacing;
    public Vector2 shadowOffset;
    public boolean shadow;
    //fire is used for guards
    public boolean fire;
    //shoot is used for player
    public boolean shoot;
    public boolean reloading;

    public int lasersShot;

    public StunGun (Vector2 position) {
        this.position = position;
        gunFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
        fire = false;

        lasersShot = 0;
        shoot = false;
        reloading = false;
    }

    public void render (ShapeRenderer renderer, Level level, Item item) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //Some logic that deals with lasers and collisions
        //fire random item.lasers
        if (fire) {
            if (MathUtils.random() < 0.03f) {
                item.lasers.add(new Laser(new Vector2(position.x + 2, position.y + 3), true, gunFacing));
                lasersShot++;
                if (level.gameplayScreen.game.getSoundEffectsOn()) {
                    level.gameplayScreen.sound7.play();
                }
            }
        }
        //fire item.lasers by tapping, if level is 14, don't shoot until gun is facing the boss
        if (shoot && (level.superior.currentLevel != 14 || (level.bosses.get(0).position.x < level.getPlayer().position.x && gunFacing == Enums.Facing.LEFT || level.bosses.get(0).position.x >= level.getPlayer().position.x && gunFacing == Enums.Facing.RIGHT))) {
            //add a new laser to the lasers array
            item.lasers.add(new Laser(new Vector2(position.x + 2, position.y + 3), false, gunFacing));
            lasersShot++;
            if (level.gameplayScreen.game.getSoundEffectsOn()) {
                level.gameplayScreen.sound7.play();
            }
            shoot = false;
        }
        //item.lasers hit boss
        for (int i = 0; i < item.lasers.size; i++) {
            for (int j = 0; j < level.bosses.size; j++) {
                //make boss bounds
                Rectangle bossBounds = new Rectangle(level.bosses.get(j).position.x - Constants.HEAD_SIZE,
                        level.bosses.get(j).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                        Constants.PLAYER_WIDTH * 2f,
                        Constants.PLAYER_HEIGHT * 2.5f);
                //hurt boss when item.lasers' bounds overlap boss bounds
                if (item.lasers.get(i).laserBounds().overlaps(bossBounds) && !item.lasers.get(i).enemyLaser) {
                    if (level.bosses.get(j).health > 0) {
                        level.bosses.get(j).health -= 1;
                    }
                    //remove laser after colliding with boss
                    item.lasers.removeIndex(i);
                }
            }
        }
        //remove lasers when boss blocks lasers
        for (int i = 0; i < item.lasers.size; i++) {
            for (int j = 0; j < level.bosses.size; j++) {
                //make bigger boss bounds
                Rectangle bossBounds = new Rectangle(level.bosses.get(j).position.x - Constants.HEAD_SIZE - Constants.PLAYER_WIDTH - 1,
                        level.bosses.get(j).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                        Constants.PLAYER_WIDTH * 4f,
                        Constants.PLAYER_HEIGHT * 2.5f);
                //if boss is blocking laser with spear, remove laser
                if (item.lasers.get(i).laserBounds().overlaps(bossBounds) && level.bosses.get(j).bossItem.equals("spear") && level.bosses.get(j).heldItem.spear.strikeStatus.equals("BLOCK")) {
                    //remove laser after colliding
                    item.lasers.removeIndex(i);
                }
            }
        }

        //make the different directions for Stun Gun
        if (gunFacing == Enums.Facing.LEFT) {
            if (shadow) {
                //SHADOW
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.rectLine(position.x + 2 + gunWidth + shadowOffset.x, (position.y - 5) + shadowOffset.y, (position.x + 2 + gunWidth + shadowOffset.x), ((position.y - 7) + (gunWidth * 2) + shadowOffset.y) - 5, 3);
                renderer.rectLine((position.x - 2) + shadowOffset.x, (position.y - 10) + (gunWidth * 2) + shadowOffset.y, position.x + 5 + gunWidth + shadowOffset.x, (position.y - 10) + (gunWidth * 2) + shadowOffset.y, 5);
            }
            //ACTUAL
            //draw Stun Gun
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x + 2 + gunWidth, position.y - 5, position.x + 2 + gunWidth, (position.y - 7) + gunWidth * 2, 3);
            renderer.rectLine(position.x - 2, (position.y - 10) + gunWidth * 2, position.x + 5 + gunWidth, (position.y - 10) + gunWidth * 2, 5);
            renderer.setColor(reloading ? Color.RED : new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.rectLine(position.x + 1, (position.y - 10.2f) + gunWidth * 2, position.x + 4 + gunWidth, (position.y - 10.2f) + gunWidth * 2, 2.5f);
        } else if (gunFacing == Enums.Facing.RIGHT) {
            //No shadow for this direction
            //draw Stun Gun
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x, position.y - 5, position.x, (position.y - 7) + gunWidth * 2, 3);
            renderer.rectLine(position.x - 3, (position.y - 10) + gunWidth * 2, (position.x + 5) + gunWidth, (position.y - 10) + gunWidth * 2, 5);
            renderer.setColor(reloading ? Color.RED : new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.rectLine(position.x - 1, (position.y - 10.2f) + gunWidth * 2, position.x + 2 + gunWidth, (position.y - 10.2f) + gunWidth * 2, 2.5f);
        }
    }
}
