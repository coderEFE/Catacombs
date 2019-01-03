package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by coder on 7/9/2018.
 * This is a Sign which can be read by the player to learn about level or other surroundings
 */

public class Sign {

    private Vector2 position;
    private float width;

    public Sign (Vector2 position) {
        this.position = position;
        width = 30;
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //draw sign
        renderer.setColor(Color.BROWN);
        //post
        renderer.rect(position.x, position.y, 8, 30);
        //board
        renderer.rect((position.x - (width / 2f) + 4), position.y + 30, width, 20);
        //draw small fake words on sign
        renderer.setColor(new Color(Color.BROWN.r * 0.7f, Color.BROWN.g * 0.7f, Color.BROWN.b * 0.7f, 1f));
        //top line
        renderer.rect((position.x - ((width) / 2f) + 6), position.y + 45, width * (4f / 8f) - 2, 1.5f);
        renderer.rect((position.x - (width / 2f) + 8 + (width * (4f / 8f) - 2)), position.y + 45, width * (1f / 8f) - 2, 1.5f);
        renderer.rect((position.x - (width / 2f) + 10 + (width * (5f / 8f) - 4)), position.y + 45, width * (3f / 8f) - 4, 1.5f);
        //middle line
        renderer.rect((position.x - (width / 2f) + 6), position.y + 40, width * (2f / 8f) - 2, 1.5f);
        renderer.rect((position.x - (width / 2f) + 8 + (width * (2f / 8f) - 2)), position.y + 40, width * (6f / 8f) - 4, 1.5f);
        //bottom line
        renderer.rect((position.x - (width / 2f) + 6), position.y + 35, width * (5f / 8f) - 2, 1.5f);
        renderer.rect((position.x - (width / 2f) + 8 + (width * (5f / 8f) - 2)), position.y + 35, width * (3f / 8f) - 4, 1.5f);
    }
    //checks if a Vector2 is inside sign's bounds
    public boolean touchesSign (Vector2 objectPosition) {
        return (objectPosition.x > (position.x - (width / 2f) + 4) && objectPosition.x < (position.x - (width / 2f) + 4) + width && objectPosition.y > position.y && objectPosition.y < position.y + 70);
    }

}
