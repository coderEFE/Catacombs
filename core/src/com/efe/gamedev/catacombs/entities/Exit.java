package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 12/6/2017.
 * This is the exit door which the player attempts to travel to in each level
 * It needs to be unlocked by the exit pad before being used
 */

public class Exit {
    private Vector2 position;
    private Vector2 buttonPosition;
    private float buttonSlide;
    private float doorSlide;
    private Level level;
    public boolean unlocked;
    public boolean playerHasEscaped;
    private Color padColor;
    private float doorFade;
    public boolean show;

    public Exit(Vector2 position, Vector2 buttonPosition, Level level) {
        this.position = position;
        this.buttonPosition = buttonPosition;
        this.level = level;
        unlocked = false;
        playerHasEscaped = false;
        buttonSlide = 0;
        doorSlide = 0;
        padColor = Color.RED;
        doorFade = 1;
        show = true;
    }

    public void update (float delta) {
        if (!unlocked && level.getPlayer().getPosition().x > buttonPosition.x && level.getPlayer().getPosition().x < buttonPosition.x + 30 && level.getPlayer().getPosition().y > buttonPosition.y + 20 && level.getPlayer().getPosition().y < buttonPosition.y + 70) {
            if (level.gameplayScreen.game.getSoundEffectsOn()) {
                level.gameplayScreen.sound2.play();
            }
            unlocked = true;
        }
        if (level.getPlayer().getPosition().x > position.x && level.getPlayer().getPosition().x < position.x + 30 && level.getPlayer().getPosition().y > position.y + 20 && level.getPlayer().getPosition().y < position.y + 70 && unlocked && level.getPlayer().jumpState == Enums.JumpState.GROUNDED && show) {
            playerHasEscaped = true;
        }
        if (unlocked && buttonSlide < 15) {
            buttonSlide += delta * 30;
        }
        if (buttonSlide > 15) {
            buttonSlide = 15;
        }
        if (unlocked && doorSlide < 25 && !playerHasEscaped) {
            doorSlide += delta * 30;
        }
        if (playerHasEscaped && doorSlide > 0) {
            doorSlide -= delta * 30;
        }
    }

    public void render (ShapeRenderer renderer) {
        //start making Exit door
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (show) {
            //draw Exit door
            //main frame
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect(position.x, position.y, 40, 70);
            renderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            renderer.rect(position.x + 2, position.y + 2, 36, 66);
        }
    }

    public void renderDoor (ShapeRenderer renderer) {
        //door
        if (show) {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.rect(position.x + 2, position.y + 2, 18 - doorSlide, 66);
            renderer.rect(position.x + 20 + (doorSlide), position.y + 2, 18 - doorSlide, 66);
            //doorknob
            if (unlocked) {
                renderer.setColor(Color.FOREST.r, Color.FOREST.g, Color.FOREST.b, doorFade);
                if (doorFade > 0) {
                    doorFade -= 0.02;
                }
            } else {
                renderer.setColor(Color.MAROON);
            }
            //draw two if currentLevel == 13 or 14, remember, level 14 is actually level 13 in the array
            if (level.superior.currentLevel == 13 || level.superior.currentLevel == 14) {
                renderer.circle(position.x + 13, position.y + 35, 2.5f, 6);
                renderer.circle(position.x + 27, position.y + 35, 2.5f, 6);
            } else {
                renderer.circle(position.x + 20, position.y + 35, 5, 6);
            }
            //added stuff for elevator in the 14th and 15th level
            if (level.superior.currentLevel == 13 || level.superior.currentLevel == 14) {
                //line between doors
                renderer.setColor(Color.DARK_GRAY);
                renderer.rect(position.x + 20, position.y + 2, 2, 66);
                //little button
                renderer.setColor(Color.LIGHT_GRAY);
                renderer.rect(position.x - 4.5f, position.y + 35, 2.5f, 5);
            }
        }
    }

    public void renderButton (ShapeRenderer renderer) {
        //start making Exit button
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw exit button
        renderer.setColor(padColor);
        renderer.rectLine(buttonPosition.x, buttonPosition.y, buttonPosition.x + 30, buttonPosition.y, 4);
        renderer.setColor(Color.GREEN);
        renderer.rectLine((buttonPosition.x + 15) - buttonSlide, buttonPosition.y, (buttonPosition.x + 15) + buttonSlide, buttonPosition.y, 4);
    }
    //used to set the exit button position
    public void setButtonPosition (Vector2 newButtonPosition) { buttonPosition.set(newButtonPosition); }

}
