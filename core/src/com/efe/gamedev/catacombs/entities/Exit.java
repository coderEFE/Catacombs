package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 12/6/2017.
 */

public class Exit {
    private Vector2 position;
    private Vector2 buttonPosition;
    private float buttonSlide;
    private float doorSlide;
    private Level level;
    public boolean unlocked;
    public boolean playerHasEscaped;
    public Color padColor;

    public Exit(Vector2 position, Vector2 buttonPosition, Level level) {
        this.position = position;
        this.buttonPosition = buttonPosition;
        this.level = level;
        unlocked = false;
        playerHasEscaped = false;
        buttonSlide = 0;
        doorSlide = 0;
        padColor = Color.RED;
    }

    public void update (float delta) {
        if (level.getPlayer().getPosition().x > buttonPosition.x && level.getPlayer().getPosition().x < buttonPosition.x + 30 && level.getPlayer().getPosition().y > buttonPosition.y + 20 && level.getPlayer().getPosition().y < buttonPosition.y + 70) {
            unlocked = true;
        }
        if (level.getPlayer().getPosition().x > position.x && level.getPlayer().getPosition().x < position.x + 30 && level.getPlayer().getPosition().y > position.y + 20 && level.getPlayer().getPosition().y < position.y + 70 && unlocked && level.getPlayer().jumpState == Enums.JumpState.GROUNDED) {
            playerHasEscaped = true;
        }
        if (unlocked && buttonSlide < 15) {
            buttonSlide += delta * 30;
        }
        if (buttonSlide > 15) {
            buttonSlide = 15;
        }
        if (unlocked && doorSlide < 30 && !playerHasEscaped) {
            doorSlide += delta * 30;
        }
        if (playerHasEscaped && doorSlide > 0) {
            doorSlide -= delta * 30;
        }
    }

    public void render (ShapeRenderer renderer) {
        //start making Exit door
        renderer.set(ShapeRenderer.ShapeType.Filled);

        //draw Exit door
        //main frame
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(position.x, position.y, 40, 70);
    }

    public void renderDoor (ShapeRenderer renderer) {
        //door
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(position.x + 2 + doorSlide, position.y + 2, 36, 66);
        //doorknob
        if (unlocked) {
            renderer.setColor(Color.FOREST);
        } else {
            renderer.setColor(Color.MAROON);
        }
        renderer.circle(position.x + 32 + doorSlide, position.y + 35, 3, 6);
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

}
