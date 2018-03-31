package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 2/28/2018.
 */

public class Gold {

    public Vector2 position;
    public float goldWidth = 8;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Gold (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //make the gold
        if (shadow) {
            //SHADOW
            renderer.setColor(Constants.SHADOW_COLOR);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + shadowOffset.x + goldWidth/4, position.y + shadowOffset.y + 4, position.x + shadowOffset.x + goldWidth, position.y + shadowOffset.y);
            renderer.triangle(position.x + shadowOffset.x + goldWidth/4, position.y + shadowOffset.y + 4, position.x + shadowOffset.x + (goldWidth*0.75f), position.y + shadowOffset.y + 4, position.x + shadowOffset.x + goldWidth, position.y + shadowOffset.y);
            renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y - 4, position.x + shadowOffset.x + goldWidth/4, position.y + shadowOffset.y, position.x + shadowOffset.x + goldWidth, position.y + shadowOffset.y - 4);
            renderer.triangle(position.x + shadowOffset.x + goldWidth/4, position.y + shadowOffset.y, position.x + shadowOffset.x + (goldWidth*0.75f), position.y + shadowOffset.y, position.x + shadowOffset.x + goldWidth, position.y + shadowOffset.y - 4);
        }
        //ACTUAL
        //Draw gold bars
        renderer.setColor(Color.GOLD);
        renderer.polygon(new float[]{ position.x, position.y, position.x + goldWidth/4, position.y + 4, position.x + (goldWidth*0.75f), position.y + 4, position.x + goldWidth, position.y });
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x, position.y, position.x + goldWidth/4, position.y + 4, position.x + goldWidth, position.y);
        renderer.triangle(position.x + goldWidth/4, position.y + 4, position.x + (goldWidth*0.75f), position.y + 4, position.x + goldWidth, position.y);
        renderer.polygon(new float[]{ position.x, position.y - 4, position.x + goldWidth/4, position.y, position.x + (goldWidth*0.75f), position.y, position.x + goldWidth, position.y - 4 });
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x, position.y - 4, position.x + goldWidth/4, position.y, position.x + goldWidth, position.y - 4);
        renderer.triangle(position.x + goldWidth/4, position.y, position.x + (goldWidth*0.75f), position.y, position.x + goldWidth, position.y - 4);
        renderer.setColor(new Color(Color.GOLD.r * 0.8f, Color.GOLD.g * 0.8f, Color.GOLD.b * 0.8f, 1));
        renderer.triangle(position.x + 1, position.y - 2, position.x + goldWidth/4, position.y, position.x + goldWidth/2, position.y);
    }

}
