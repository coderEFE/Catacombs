package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 5/29/2018.
 */

public class Stalactite {

    public Vector2 position;
    public float size;
    private Vector2 acceleration;
    private Vector2 velocity;
    public boolean hit;
    public float fadeTimer;

    public Stalactite (Vector2 position, float size) {
        this.position = position;
        this.size = size;
        acceleration = new Vector2(0, -100.0f);
        velocity = new Vector2(0, -30.0f);
        hit = false;
        fadeTimer = 0;
    }

    public void update (float delta) {
        //make stalactites drop
        if (!hit) {
            velocity.mulAdd(acceleration, delta);
            position.mulAdd(velocity, delta);
        } else {
            fadeTimer ++;
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw stalactites
        renderer.setColor(new Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1f - (fadeTimer / 20f)));
        renderer.triangle(position.x - (size / 5), position.y, position.x + (size / 5), position.y, position.x, position.y - size);
    }
}
