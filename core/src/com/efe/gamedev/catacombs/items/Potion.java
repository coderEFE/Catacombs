package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 6/13/2018.
 */

public class Potion {

    public Vector2 position;
    public float potionWidth = 8;
    public Vector2 shadowOffset;
    public boolean shadow;
    public boolean full;

    public Potion (Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
        full = true;
    }

    public void render (ShapeRenderer renderer, String type) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //make the potion
        if (shadow) {
            //SHADOW
            renderer.setColor(Constants.SHADOW_COLOR);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            //base of potion
            renderer.triangle(position.x + shadowOffset.x, position.y + shadowOffset.y - 4, position.x + shadowOffset.x + potionWidth/4, position.y + shadowOffset.y, position.x + shadowOffset.x + potionWidth, position.y + shadowOffset.y - 4);
            renderer.triangle(position.x + shadowOffset.x + potionWidth/4, position.y + shadowOffset.y, position.x + shadowOffset.x + (potionWidth*0.75f), position.y + shadowOffset.y, position.x + shadowOffset.x + potionWidth, position.y + shadowOffset.y - 4);
            //top of potion
            renderer.rect((position.x + potionWidth/4) + shadowOffset.x, position.y + shadowOffset.y, (potionWidth*0.75f) - (potionWidth/4f), 4);
        }
        //ACTUAL
        //Draw potion
        renderer.setColor(new Color(Color.GRAY.r * 1.2f, Color.GRAY.g * 1.2f, Color.GRAY.b * 1.2f, 1));
        //base of potion
        renderer.polygon(new float[]{ position.x, position.y - 4, position.x + potionWidth/4, position.y, position.x + (potionWidth*0.75f), position.y, position.x + potionWidth, position.y - 4 });
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(position.x, position.y - 4, position.x + potionWidth/4, position.y, position.x + potionWidth, position.y - 4);
        renderer.triangle(position.x + potionWidth/4, position.y, position.x + (potionWidth*0.75f), position.y, position.x + potionWidth, position.y - 4);
        //top of potion
        renderer.rect(position.x + potionWidth/4, position.y, (potionWidth*0.75f) - (potionWidth/4f), 4);
        //liquid inside of potion
        if (type.equals("invisibility")) {
            renderer.setColor(Color.SKY);
        } else if (type.equals("ghost")) {
            renderer.setColor(Color.WHITE);
        } else if (type.equals("shock")) {
            renderer.setColor(Color.GREEN);
        }
        if (full) {
            renderer.triangle(position.x + 0.5f, (position.y - 4) + 0.5f, position.x + potionWidth / 4, position.y - 1f, (position.x + potionWidth) - 1, position.y - 3.5f);
            renderer.triangle(position.x + potionWidth / 4, position.y - 1f, position.x + (potionWidth * 0.75f), position.y - 1f, (position.x + potionWidth) - 1, position.y - 3.5f);
        }
    }

    public boolean touchPotion (Vector2 touchPosition) {
        return (touchPosition.x > position.x && touchPosition.x < position.x + potionWidth && touchPosition.y > position.y - 4 && touchPosition.y < position.y + 4);
    }
    
}
