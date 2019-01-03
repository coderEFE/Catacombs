package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 7/25/2018.
 * Fire can be used to light torches and is an item
 */

public class Fire {

    private Vector2 position;
    public float fireWidth = 6f;
    public Enums.Facing fireFacing;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Fire (Vector2 position) {
        this.position = position;
        fireFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }

    public void render (ShapeRenderer renderer) {
        //make the fire
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (shadow) {
            //SHADOW
            renderer.setColor(Constants.SHADOW_COLOR);
        }
        //ACTUAL
        //draw fire
        //animated fire
        flame(new Vector2(position.x, position.y), (fireWidth / 3f), fireWidth / 2f, renderer);
        flame(new Vector2(position.x + (fireWidth / 3f), position.y), (fireWidth / 3f), fireWidth * 0.9f, renderer);
        flame(new Vector2(position.x + (fireWidth / 3f) + (fireWidth / 3f), position.y), (fireWidth / 2f), fireWidth / 2f, renderer);
        flame(new Vector2(position.x + (fireWidth / 3f) + (fireWidth / 3f) + (fireWidth / 2f), position.y), (fireWidth / 3f), fireWidth / 1.5f, renderer);
        flame(new Vector2(position.x + (fireWidth / 3f) + (fireWidth / 3f) + (fireWidth / 3f) + (fireWidth / 2f), position.y), (fireWidth / 3f), fireWidth, renderer);
        //fire bowl
        renderer.setColor(new Color(0.9f, 0.9f, 0.6f, 1f));
        renderer.arc(position.x + fireWidth, position.y, fireWidth, 180, 180, 20);
    }
    //draws flames for the fire, red at the bottom and orange at the top
    private void flame (Vector2 flamePosition, float width, float height, ShapeRenderer renderer) {
        //fiery orange color
        renderer.triangle(flamePosition.x, flamePosition.y, flamePosition.x + width, flamePosition.y, flamePosition.x + (width / 2f), flamePosition.y + height - (MathUtils.random(0, 2f)), new Color((1f / 255f) * 254, (1f / 255f) * 56, (1f / 255f), 1f),  new Color((1f / 255f) * 254, (1f / 255f) * 56, (1f / 255f), 1f), new Color((1f / 255f) * 254, (1f / 255f) * 99, (1f / 255f), 1f));
    }

}
