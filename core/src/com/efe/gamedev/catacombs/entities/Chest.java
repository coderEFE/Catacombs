package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 1/10/2018.
 * This is a Chest which can be found throughout the Catacombs containing various types of treasure.
 * Some chests require certain items to be unlocked.
 */

public class Chest {

    public Vector2 position;
    private Level level;
    private Runnable touchFunction;
    public boolean opened;
    private String requiredItem;
    private Item thoughtItem;
    public boolean fade;
    private float fadeOpacity;

    public Chest (Vector2 position, String requiredItem, Level level, final Runnable touchFunction) {
        this.position = position;
        this.level = level;
        this.requiredItem = requiredItem;
        this.touchFunction = touchFunction;
        opened = false;
        thoughtItem = new Item(new Vector2(position.x + 13, position.y + 60), level.viewportPosition, requiredItem);
        fade = false;
        fadeOpacity = 1f;
    }

    public void update () {
        //if you have the right item and you press on chest, it will open.
        if ((requiredItem.equals("") || requiredItem.equals(level.getPlayer().heldItem.itemType)) && !fade) {
            if (level.touchPosition.x > position.x && level.touchPosition.x < position.x + 40 && level.touchPosition.y > position.y && level.touchPosition.y < position.y + 26 && level.getPlayer().position.x > position.x && level.getPlayer().position.x < position.x + 40 && !opened) {
                touchFunction.run();
                opened = true;
                level.gameplayScreen.sound6.play();
            }
        }
        //make chest fade away
        if (fade && fadeOpacity > 0f) {
            fadeOpacity-=0.01;
        }
        //have chest un-fade
        if (!fade && fadeOpacity < 1f) {
            fadeOpacity+=0.01;
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //chest
        renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, fadeOpacity));
        renderer.rect(position.x, position.y, 40, opened ? 20 : 26);
        renderer.setColor(new Color(Color.GOLD.r, Color.GOLD.g, Color.GOLD.b, fadeOpacity));
        renderer.rect(position.x, position.y + 20, 40, 4);
        renderer.setColor(new Color(Color.GOLD.r, Color.GOLD.g, Color.GOLD.b, fadeOpacity));
        renderer.circle(position.x + 20, position.y + 15, 3, 6);
        renderer.triangle(position.x + 20, position.y + 15, position.x + 15, position.y + 5, position.x + 25, position.y + 5);
        //if you need an item to open chest, there will be a thought-bubble over chest with item in it.
        if (!requiredItem.equals("") && !opened && !fade) {
            renderer.setColor(Color.WHITE);
            //big thought bubble
            renderer.circle(position.x + 18, position.y + 60, 15, 40);
            renderer.circle(position.x + 10, position.y + 62, 10, 30);
            renderer.circle(position.x + 26, position.y + 62, 10, 30);
            renderer.circle(position.x + 28, position.y + 54, 8, 30);
            renderer.circle(position.x + 18, position.y + 54, 8, 30);
            renderer.circle(position.x + 8, position.y + 54, 8, 30);
            //bubbles leading up to it
            renderer.circle(position.x + 37, position.y + 42, 5, 30);
            renderer.circle(position.x + 31, position.y + 34, 3, 30);
            renderer.circle(position.x + 25, position.y + 30, 2, 20);
            thoughtItem.collected = true;
            thoughtItem.itemType = requiredItem;
            thoughtItem.render(renderer, level);
        }
    }

}
