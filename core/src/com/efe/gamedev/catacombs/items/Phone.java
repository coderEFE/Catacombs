package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 5/5/2018.
 * An old piece of technology, used by guards.
 */

public class Phone {

    public Vector2 position;
    public float phoneWidth = 4;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Phone (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //make the phone
        //Draw phone, looks like a walkie-talkie
        renderer.setColor(Color.DARK_GRAY);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, phoneWidth, phoneWidth * 1.6f);
        renderer.rectLine(position.x + (phoneWidth * 0.75f), position.y + phoneWidth * 1.6f, position.x + (phoneWidth * 0.75f), position.y + phoneWidth * 2.5f, phoneWidth * 0.25f);
        renderer.setColor(Color.GRAY);
        renderer.rect(position.x + phoneWidth / 6f, position.y + phoneWidth * 0.75f, phoneWidth * 4f / 6f, phoneWidth * 0.5f);
    }

}
