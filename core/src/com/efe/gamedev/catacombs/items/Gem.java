package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 6/11/2018.
 * Various types of gems that can be collected
 * there are sapphires, emeralds, and rubies
 */

public class Gem {

    public Vector2 position;
    public float gemWidth = 8;
    public boolean shadow;
    public Vector2 shadowOffset;

    public Gem (Vector2 position) {
        this.position = position;
        shadow = false;
        shadowOffset = new Vector2(1.5f, 2);
    }

    public void render (ShapeRenderer renderer, String gemType) {
        //set shapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //SHADOW
        if (shadow) {
            //set transparent shadow color
            renderer.setColor(Constants.SHADOW_COLOR);
            //draw gem
            renderer.polygon(new float[]{position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + (gemWidth / 8f) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, (position.x + gemWidth - (gemWidth / 8f)) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, position.x + gemWidth + shadowOffset.x, position.y + shadowOffset.y, position.x + (gemWidth / 2) + shadowOffset.x, (position.y - (gemWidth * 0.8f)) + shadowOffset.y});
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + gemWidth + shadowOffset.x, position.y + shadowOffset.y, position.x + (gemWidth / 2f) + shadowOffset.x, (position.y - (gemWidth * 0.8f)) + shadowOffset.y);
            renderer.triangle(position.x + (gemWidth / 8f) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, (position.x + gemWidth - (gemWidth / 8f)) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, position.x + (gemWidth / 2f) + shadowOffset.x, (position.y - (gemWidth / 2f)) + shadowOffset.y);
            renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + (gemWidth / 8f) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, position.x + (gemWidth / 4f) + shadowOffset.x, position.y + shadowOffset.y);
            renderer.triangle(position.x + gemWidth + shadowOffset.x, position.y + shadowOffset.y, (position.x + gemWidth - (gemWidth / 8f)) + shadowOffset.x, position.y + (gemWidth / 8f) + shadowOffset.y, (position.x + gemWidth - (gemWidth / 4f)) + shadowOffset.x, position.y + shadowOffset.y);
        }
        //ACTUAL
        //set transparent gem color depending on gemType
        switch (gemType) {
            case "sapphire":
                renderer.setColor(Color.BLUE);
                break;
            case "ruby":
                renderer.setColor(Color.RED);
                break;
            case "emerald":
                renderer.setColor(Color.GREEN);
                break;
        }
        //draw gem
        renderer.polygon(new float[]{ position.x, position.y, position.x + (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + gemWidth - (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + gemWidth, position.y, position.x + (gemWidth / 2), position.y - (gemWidth * 0.8f)});
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x, position.y, position.x + gemWidth, position.y, position.x + (gemWidth / 2f), position.y - (gemWidth * 0.8f));
        renderer.triangle(position.x + (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + gemWidth - (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + (gemWidth / 2f), position.y - (gemWidth / 2f));
        renderer.setColor(new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.5f));
        renderer.triangle(position.x, position.y, position.x + (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + (gemWidth / 4f), position.y);
        renderer.triangle(position.x + gemWidth, position.y, position.x + gemWidth - (gemWidth / 8f), position.y + (gemWidth / 8f), position.x + gemWidth - (gemWidth / 4f), position.y);
    }
    
}
