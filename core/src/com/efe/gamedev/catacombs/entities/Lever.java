package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 6/11/2018.
 * A lever which can be switched from on to off, or from off to on.
 * When the lever is on, it triggers its onFunction (opens a door or something) and the tip of lever turns green.
 */

public class Lever {
    //position: the position, triggered: boolean for on or off, onFunction: the function that is activated when lever is triggered, offFunction: the function that is activated when lever is not triggered, and level: the Level used to get viewportPosition for detecting touch input.
    public Vector2 position;
    public boolean triggered;
    private Runnable onFunction;
    private Runnable offFunction;
    private Level level;
    private float degrees;
    private float stickYOffset;
    private Vector2 handleOffset;

    public Lever (Vector2 position, final Runnable onFunction, final Runnable offFunction, Level level) {
        //set up initials
        this.position = position;
        triggered = false;
        this.onFunction = onFunction;
        this.offFunction = offFunction;
        degrees = 60;
        stickYOffset = 0;
        this.level = level;
        handleOffset = new Vector2();
    }

    public void update () {
        //check if lever is pressed
        if (level.touchPosition.x > position.x - 20 && level.touchPosition.x < position.x + 20 && level.touchPosition.y > position.y && level.touchPosition.y < position.y + 20 && level.getPlayer().position.x > position.x - 10 && level.getPlayer().position.x < position.x + 10) {
            if (triggered && degrees <= -59 && !level.touchPosition.equals(new Vector2())) {
                triggered = false;
            }
            if (!triggered && degrees >= 59 && !level.touchPosition.equals(new Vector2())) {
                triggered = true;
            }
            level.touchPosition.set(new Vector2());
        }
        //if lever is triggered, have the stick to the left
        if (triggered && degrees >= -60) {
            degrees-= 1.6f;
            stickYOffset += 0.08;
            handleOffset.x += 0.48f;
            handleOffset.y += (0.5f - (handleOffset.x / 40f));
            //if lever is triggered, run onFunction
            onFunction.run();
        }
        //if lever is not triggered, have the stick to the right
        if (!triggered && degrees <= 60) {
            degrees+= 1.6f;
            stickYOffset -= 0.08;
            handleOffset.x -= 0.48f;
            handleOffset.y += (0.5f - ((36.479973f - handleOffset.x) / 35f));
            //else, if lever is not triggered, run offFunction
            offFunction.run();
        }
        //reset handleOffset.y
        if (!triggered && degrees >= 59) {
            handleOffset.y = -0.55599916f;
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw lever
        //lever stick
        renderer.setColor(new Color(0.8f, 0.8f, 0.8f, 1));
        renderer.rect(position.x - 2, position.y + stickYOffset, 0, 0, 3, 20, 1, 1, degrees);
        //bottom arc
        renderer.setColor(Color.DARK_GRAY);
        renderer.arc(position.x, position.y,10, 0, 180, 20);
        //circle at the top of lever
        renderer.setColor(triggered ? Color.FOREST : Color.MAROON);
        renderer.circle((position.x - 19) + handleOffset.x, (position.y + 12) + handleOffset.y, 3, 6);
    }
}
