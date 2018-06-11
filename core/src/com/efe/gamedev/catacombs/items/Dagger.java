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
    public Enums.Facing daggerFacing;
    public boolean shadow;

    public Dagger (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        daggerFacing = Enums.Facing.RIGHT;
        shadow = true;
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (daggerFacing == Enums.Facing.LEFT) {
            //make the dagger different directions
            if (shadow) {
                //SHADOW
                // draw gray dagger shadow
                //renderer.setColor(new Color(0,0,0,0.4f));
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.rectLine(position.x + 8 + shadowOffset.x, (position.y - 9) + shadowOffset.y + 4, position.x + 4 + shadowOffset.x, (position.y - 9) + 6 + shadowOffset.y + 4, daggerWidth / 4);
                renderer.rectLine(position.x + 1 + shadowOffset.x, (position.y - 6) + shadowOffset.y + 4,(position.x + 1) + (daggerWidth * 0.8f) + shadowOffset.x, position.y + shadowOffset.y + 4, 1.5f);
                renderer.triangle(position.x + 2f + shadowOffset.x, (position.y - 9) + 5f + shadowOffset.y + 4, position.x + (daggerWidth / 2) + 1.5f + shadowOffset.x, (position.y - 1f) + shadowOffset.y + 4, position.x + shadowOffset.x, (position.y) + 7 + shadowOffset.y);
            }
            //ACTUAL
            //draw gray dagger
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x + 8, (position.y - 9) + 4, position.x + 4, (position.y - 9) + 10, daggerWidth / 4);
            renderer.rectLine(position.x + 1, (position.y - 6) + 4,(position.x + 1) + (daggerWidth * 0.8f), position.y + 4, 1.5f);
            renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
            renderer.triangle(position.x + 2f, (position.y - 9) + 9f, position.x + (daggerWidth / 2) + 1.5f, position.y + 3f, position.x, (position.y) + 7);
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
            renderer.line(position.x + 4.1f, position.y - 1, position.x + 4 + (daggerWidth / 4), position.y);
            renderer.line(position.x + 4.7f, position.y - 2, position.x + 4.5f + (daggerWidth / 4), position.y - 1);
            renderer.line(position.x + 5.4f, position.y - 3, position.x + 6.1f + (daggerWidth / 4), position.y - 4);
        } else if (daggerFacing == Enums.Facing.RIGHT) {
            //draw gray dagger
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x + 2, position.y - 5, position.x + 6, (position.y - 9) + 10, daggerWidth / 4);
            renderer.rectLine(position.x + 3, position.y + 4,(position.x + 3) + (daggerWidth * 0.8f), position.y - 2, 1.5f);
            renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
            renderer.triangle(position.x + (daggerWidth / 2) + 4f, (position.y - 9) + 9f, position.x + 4.5f, position.y + 3f, position.x + (daggerWidth / 2) + 6f, (position.y) + 7);
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
        }
    }

}
