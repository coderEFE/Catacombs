package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.entities.Item;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 7/22/2018.
 * Bomb used to blow up catacomb walls
 * It looks like a hand grenade
 */

public class Bomb {

    public Vector2 position;
    private float velocityY;
    public float bombWidth = 8;
    public Vector2 shadowOffset;
    public Enums.Facing bombFacing;
    public boolean shadow;
    //explosion variables
    public boolean triggered;
    public boolean exploding;
    private float triggerTimer;
    private boolean flashRed;
    private float explosion;
    private float explodeFade;
    public int currentCatacomb;

    public Bomb (Vector2 position) {
        this.position = position;
        velocityY = 0;
        shadowOffset = new Vector2(1.5f, 2);
        bombFacing = Enums.Facing.RIGHT;
        shadow = true;
        triggered = false;
        exploding = false;
        triggerTimer = 0;
        flashRed = false;
        explosion = 0;
        explodeFade = 1f;
        currentCatacomb = 0;
    }

    public void render (ShapeRenderer renderer, Level level, Item thisItem) {
        //set shapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (!exploding) {
            if (bombFacing == Enums.Facing.RIGHT) {
                //show shadow if bomb is triggered
                if (shadow || triggered) {
                    //SHADOW
                    //set transparent shadow color
                    renderer.setColor(Constants.SHADOW_COLOR);
                    //draw bomb trigger
                    renderer.rect(position.x + 5 + shadowOffset.x, position.y + shadowOffset.y, bombWidth / 4f, bombWidth / 2f);
                    renderer.rect(position.x + 3 + shadowOffset.x, position.y + shadowOffset.y, bombWidth / 8f, bombWidth * 0.7f);
                    renderer.rectLine(position.x + 2 + shadowOffset.x, position.y + (bombWidth * 0.7f) + shadowOffset.y, position.x + 3 + (bombWidth / 2f) + shadowOffset.x, position.y + (bombWidth * 0.7f) + shadowOffset.y, bombWidth / 8f);
                    renderer.rectLine((position.x + 8) + (bombWidth / 2f) + shadowOffset.x, position.y + shadowOffset.y, position.x + 3 + (bombWidth / 2f) + shadowOffset.x, position.y + (bombWidth * 0.7f) + shadowOffset.y, bombWidth / 8f);
                    //draw bomb body
                    renderer.ellipse(position.x + shadowOffset.x, (position.y - 6) + shadowOffset.y, bombWidth, bombWidth * 1.25f, 20);

                }
                //ACTUAL
                //draw bomb trigger
                renderer.setColor(flashRed ? Color.RED : Color.BLACK);
                renderer.rect(position.x + 5, position.y, bombWidth / 4f, bombWidth / 2f);
                renderer.rect(position.x + 3, position.y, bombWidth / 8f, bombWidth * 0.7f);
                renderer.rectLine(position.x + 2, position.y + bombWidth * 0.7f, position.x + 3 + (bombWidth / 2f), position.y + bombWidth * 0.7f, bombWidth / 8f);
                renderer.rectLine((position.x + 8) + (bombWidth / 2f), position.y, position.x + 3 + (bombWidth / 2f), position.y + bombWidth * 0.7f, bombWidth / 8f);
                //set gray bomb color
                renderer.setColor(flashRed ? Color.RED : (new Color(Color.DARK_GRAY.r * 0.7f, Color.DARK_GRAY.g * 0.7f, Color.DARK_GRAY.b * 0.7f, 1f)));
                //draw bomb
                renderer.ellipse(position.x, position.y - 6, bombWidth, bombWidth * 1.25f, 20);
            } else {
                //ACTUAL
                //draw bomb trigger
                renderer.setColor(flashRed ? Color.RED : Color.BLACK);
                renderer.rect(position.x + 1, position.y, bombWidth / 4f, bombWidth / 2f);
                renderer.rect(position.x + 3, position.y, bombWidth / 8f, bombWidth * 0.7f);
                renderer.rectLine(position.x + 5, position.y + bombWidth * 0.7f, position.x + 5 - (bombWidth / 2f), position.y + bombWidth * 0.7f, bombWidth / 8f);
                renderer.rectLine(position.x + 5 - (bombWidth / 2f), position.y + bombWidth * 0.7f, position.x - (bombWidth / 2f), position.y, bombWidth / 8f);
                //set gray bomb color
                renderer.setColor(flashRed ? Color.RED : (new Color(Color.DARK_GRAY.r * 0.7f, Color.DARK_GRAY.g * 0.7f, Color.DARK_GRAY.b * 0.7f, 1f)));
                //draw bomb
                renderer.ellipse(position.x, position.y - 6, bombWidth, bombWidth * 1.25f, 20);
            }
        }
        //draw explosion when bomb is exploding
        if (exploding) {
            if (!level.inventory.newItem && !level.inventory.paused) {
                explosion += 5;
            }
            //light blue circle
            renderer.setColor(new Color((1 / 255f) * 254, (1 / 255f) * 5, (1 / 255f) * 8, explodeFade));
            renderer.circle(position.x, position.y - 6, explosion <= 60 ? explosion : 60, 40);
            //dark blue circle
            if (explosion > 40) {
                renderer.setColor(new Color((1 / 255f) * 187, (1 / 255f) * 3, (1 / 255f) * 5, explodeFade));
                renderer.circle(position.x, position.y - 6, explosion <= 100 ? explosion - 40 : 60, 40);
            }
            //fade explosion
            if (explosion > 100) {
                if (explodeFade > 0f) {
                    explodeFade -= 0.02f;
                } else {
                    explodeFade = 0f;
                }
                //check if explosion hit player
                if (new Vector2(level.getPlayer().getPosition().x, level.getPlayer().getPosition().y - Constants.PLAYER_HEIGHT).dst(new Vector2(position.x, position.y - 6)) < 60) {
                    level.touchLocked = true;
                    level.show = false;
                    level.continueBubbles = false;
                    level.getPlayer().mouthState = Enums.MouthState.OPEN;
                    //player loses level
                    level.verdict.verdict = false;
                    level.defeat = true;
                    level.verdict.verdictPhrase = "You were hit by a bomb!";
                }
            }
            //destroy bomb
            if (explodeFade == 0f) {
                //if destroyed, remove bomb from array
                level.items.removeIndex(level.items.indexOf(thisItem, true));
            }
        }
        //if triggered, continually add to velocity with gravity
        //make bomb fall to ground when triggered
        if (triggered && position.y > level.catacombs.get(currentCatacomb).position.y + 25) {
            position.y += velocityY;
            velocityY -= 0.05f;
        } else {
            velocityY = 0;
        }
        //reset position if bomb is lower than currentCatacomb.
        if (position.y < level.catacombs.get(currentCatacomb).position.y + 25) {
            position.y = level.catacombs.get(currentCatacomb).position.y + 25;
        }
        //if triggered, and bomb has hit the ground start trigger timer and have bomb flash red
        if (triggered && position.y == level.catacombs.get(currentCatacomb).position.y + 25 && !level.inventory.newItem && !level.inventory.paused) {
            triggerTimer++;
            if (triggerTimer > 10 && triggerTimer <= 40) {
                flashRed = true;
            } else if (triggerTimer > 40 && triggerTimer <= 70) {
                flashRed = false;
            } else if (triggerTimer > 70 && triggerTimer <= 100) {
                flashRed = true;
            } else if (triggerTimer > 100 && triggerTimer <= 130) {
                flashRed = false;
            } else if (triggerTimer > 130 && triggerTimer <= 160) {
                flashRed = true;
            } else if (triggerTimer > 160 && triggerTimer <= 190) {
                flashRed = false;
            } else if (triggerTimer > 190) {
                //play sound and explode bomb
                level.gameplayScreen.sound9.play();
                exploding = true;
                triggerTimer = 0;
                triggered = false;
            }
        }
    }
}
