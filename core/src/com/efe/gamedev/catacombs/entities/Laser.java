package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by coder on 6/3/2018.
 */

public class Laser {

    public Vector2 position;
    private float width;
    private Vector2 velocity;
    public boolean hit;

    public Laser (Vector2 position) {
        this.position = position;
        width = 6;
        velocity = new Vector2(-30.0f, 0);
        hit = false;
    }

    public void update () {
        //make lasers move
        if (!hit) {
            position.mulAdd(velocity, 0.05f);
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw lasers
        renderer.setColor(Color.ORANGE);
        renderer.rectLine(position.x - 0.1f, position.y - 0.1f, position.x + width + 0.2f, position.y - 0.1f, 3.4f);
        renderer.setColor(Color.RED);
        renderer.rectLine(position.x, position.y, position.x + width, position.y, 3);
    }

    public Rectangle laserBounds () {
        return new Rectangle(
                position.x,
                position.y,
                width,
                3
        );
    }

}
