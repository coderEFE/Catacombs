package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 12/2/2017.
 */

public class Diamond {

    public Vector2 position;
    public float diamondWidth = 8;
    public Vector2 shadowOffset;

    public Diamond (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(1.5f, 2);
    }

    public void render (ShapeRenderer renderer) {
        //set shapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //SHADOW
        //set transparent shadow color
        renderer.setColor(Constants.SHADOW_COLOR);
        //draw diamond
        renderer.polygon(new float[]{ position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + (diamondWidth / 8f) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, (position.x + diamondWidth - (diamondWidth / 8f)) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, position.x + diamondWidth + shadowOffset.x, position.y + shadowOffset.y, position.x + (diamondWidth / 2) + shadowOffset.x, (position.y - (diamondWidth * 0.8f)) + shadowOffset.y});
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + diamondWidth + shadowOffset.x, position.y + shadowOffset.y, position.x + (diamondWidth / 2f) + shadowOffset.x, (position.y - (diamondWidth * 0.8f)) + shadowOffset.y);
        renderer.triangle(position.x + (diamondWidth / 8f) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, (position.x + diamondWidth - (diamondWidth / 8f)) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, position.x + (diamondWidth / 2f) + shadowOffset.x, (position.y - (diamondWidth / 2f)) + shadowOffset.y);
        renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + (diamondWidth / 8f) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, position.x + (diamondWidth / 4f) + shadowOffset.x, position.y + shadowOffset.y);
        renderer.triangle(position.x + diamondWidth + shadowOffset.x, position.y + shadowOffset.y, (position.x + diamondWidth - (diamondWidth / 8f)) + shadowOffset.x, position.y + (diamondWidth / 8f) + shadowOffset.y, (position.x + diamondWidth - (diamondWidth / 4f)) + shadowOffset.x, position.y + shadowOffset.y);
        //ACTUAL
        //set transparent blue color
        renderer.setColor(Color.SKY);
        //draw diamond
        renderer.polygon(new float[]{ position.x, position.y, position.x + (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + diamondWidth - (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + diamondWidth, position.y, position.x + (diamondWidth / 2), position.y - (diamondWidth * 0.8f)});
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x, position.y, position.x + diamondWidth, position.y, position.x + (diamondWidth / 2f), position.y - (diamondWidth * 0.8f));
        renderer.triangle(position.x + (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + diamondWidth - (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + (diamondWidth / 2f), position.y - (diamondWidth / 2f));
        renderer.setColor(Color.WHITE);
        renderer.triangle(position.x, position.y, position.x + (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + (diamondWidth / 4f), position.y);
        renderer.triangle(position.x + diamondWidth, position.y, position.x + diamondWidth - (diamondWidth / 8f), position.y + (diamondWidth / 8f), position.x + diamondWidth - (diamondWidth / 4f), position.y);
    }

}
