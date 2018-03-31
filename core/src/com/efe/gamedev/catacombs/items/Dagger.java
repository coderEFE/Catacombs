package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 2/27/2018.
 * Dagger item used as weapon
 */

public class Dagger {

    public Vector2 position;
    public float daggerWidth = 8;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Dagger (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //make the dagger
            if (shadow) {
                //SHADOW
                // draw gray dagger shadow
                //renderer.setColor(new Color(0,0,0,0.4f));
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.rect(position.x + 4 + shadowOffset.x, (position.y - 9) + shadowOffset.y, daggerWidth/4, 6);
                renderer.rect(position.x + 1 + shadowOffset.x, (position.y - 3) + shadowOffset.y, daggerWidth, 1.5f);
                renderer.triangle(position.x + 3 + shadowOffset.x, (position.y - 1.5f) + shadowOffset.y, position.x + (daggerWidth/2) + 3 + shadowOffset.x, (position.y - 1.5f) + shadowOffset.y, position.x + 3 + (daggerWidth/4) + shadowOffset.x, position.y + 7 + shadowOffset.y);
            }
            //ACTUAL
            //draw gray dagger
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rect(position.x + 4, position.y - 9, daggerWidth/4, 6);
            renderer.rect(position.x + 1, position.y - 3, daggerWidth, 1.5f);
            renderer.setColor(new Color(0.4f, 0.4f,0.4f, 1));
            renderer.triangle(position.x + 3, position.y - 1.5f, position.x + (daggerWidth/2) + 3, position.y - 1.5f, position.x + 3 + (daggerWidth/4), position.y + 7);
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            renderer.line(position.x + 4.1f, position.y - 5, position.x + 4 + (daggerWidth/4), position.y - 4);
            renderer.line(position.x + 4.1f, position.y - 6, position.x + 4 + (daggerWidth/4), position.y - 5);
            renderer.line(position.x + 4.1f, position.y - 7, position.x + 4 + (daggerWidth/4), position.y - 8);
    }

}
