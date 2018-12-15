package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 6/3/2018.
 * This is the laser that can be shot by Stun Guns carried by the player or Guards.
 */

public class Laser {

    public Vector2 position;
    private float width;
    private Vector2 velocity;
    public boolean hit;
    public Enums.Facing direction;
    public boolean enemyLaser;

    public Laser (Vector2 position, boolean enemyLaser, Enums.Facing direction) {
        this.position = position;
        width = 6;
        this.direction = direction;
        velocity = new Vector2(direction == Enums.Facing.LEFT ? -30.0f : 30.0f, 0);
        hit = false;
        this.enemyLaser = enemyLaser;
    }

    public void update () {
        //update velocity
        velocity = new Vector2(direction == Enums.Facing.LEFT ? -30.0f : 30.0f, 0);
        //make lasers move
        if (!hit) {
            position.mulAdd(velocity, 0.05f);
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw lasers
        //renderer.setColor(Color.ORANGE);
        //renderer.rectLine(position.x - 0.1f, position.y - 0.1f, position.x + width + 0.2f, position.y - 0.1f, 3.4f);
        renderer.setColor(enemyLaser ? new Color((1 / 255f) * 190, (1 / 255f) * 254, (1 / 255f), 1f) : new Color((1 / 255f) * 254, (1 / 255f), (1 / 255f) * 122, 1f));
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
