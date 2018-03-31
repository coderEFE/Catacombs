package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 1/10/2018.
 */

public class Chest {

    public Vector2 position;
    private Level level;
    private Runnable touchFunction;
    public boolean opened;

    public Chest (Vector2 position, Level level, final Runnable touchFunction) {
        this.position = position;
        this.level = level;
        this.touchFunction = touchFunction;
        opened = false;
    }

    public void update () {
        if (level.touchPosition.x > position.x && level.touchPosition.x < position.x + 40 && level.touchPosition.y > position.y && level.touchPosition.y < position.y + 26 && level.getPlayer().position.x > position.x && level.getPlayer().position.x < position.x + 40 && !opened) {
            touchFunction.run();
            opened = true;
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BROWN);
        renderer.rect(position.x, position.y, 40, opened ? 20 : 26);
        renderer.setColor(Color.GOLD);
        renderer.rect(position.x, position.y + 20, 40, 4);
        renderer.setColor(Color.GOLD);
        renderer.circle(position.x + 20, position.y + 15, 3, 6);
        renderer.triangle(position.x + 20, position.y + 15, position.x + 15, position.y + 5, position.x + 25, position.y + 5);
    }

}
