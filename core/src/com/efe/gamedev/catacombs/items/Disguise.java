package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 8/30/2018.
 * Item used to blend in with the guards.
 * Is only used in the 14th level
 */

public class Disguise {

    public Vector2 position;
    public float clothesWidth = 12;
    public boolean shadow;
    public Vector2 shadowOffset;

    public Disguise (Vector2 position) {
        this.position = position;
        shadow = false;
        shadowOffset = new Vector2(1.5f, 2);
    }

    public void render (ShapeRenderer renderer) {
        //set shapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //SHADOW
        if (shadow) {
            //set transparent shadow color
            renderer.setColor(Constants.SHADOW_COLOR);
            //draw shirt
            renderer.rect(position.x + 1 + shadowOffset.x, position.y - clothesWidth / 2f + shadowOffset.y, (clothesWidth * 0.75f), clothesWidth);
            //draw sleeves
            renderer.rectLine(position.x + 2 + shadowOffset.x, position.y + 5 + shadowOffset.y, position.x - 1 + shadowOffset.x, (position.y - clothesWidth / 2f) + 5 + shadowOffset.y, clothesWidth / 3f);
            renderer.rectLine(position.x + (clothesWidth * 0.75f) + shadowOffset.x, position.y + 5 + shadowOffset.y, position.x + (clothesWidth * 0.75f) + 3 + shadowOffset.x, (position.y - clothesWidth / 2f) + 5 + shadowOffset.y, clothesWidth / 3f);
        }
        //ACTUAL
        //set shirt color
        renderer.setColor(Constants.GUARD_CLOTHES_COLOR);
        //draw shirt
        renderer.rect(position.x + 1, position.y - clothesWidth / 2f, (clothesWidth * 0.75f), clothesWidth);
        //draw sleeves
        renderer.rectLine(position.x + 2, position.y + 5, position.x - 1, (position.y - clothesWidth / 2f) + 5, clothesWidth / 3f);
        renderer.rectLine(position.x + (clothesWidth * 0.75f), position.y + 5, position.x + (clothesWidth * 0.75f) + 3, (position.y - clothesWidth / 2f) + 5, clothesWidth / 3f);
        //draw symbols
        renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r * 0.4f, Constants.GUARD_CLOTHES_COLOR.g * 0.4f, Constants.GUARD_CLOTHES_COLOR.b * 0.4f, 1f));
        renderer.rect(position.x + 2, position.y + 1, 2, 1);
        renderer.rect(position.x + 2, position.y - 0.5f, 2, 1);
    }
    
}
