package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by coder on 6/12/2018.
 * Hole used for the "whacker" puzzles
 * Similar to the mini-game: "Whack-A-Mole"
 */

public class Hole {
    private Vector2 position;
    boolean hasBall;
    boolean ballTouched;
    boolean golden;

    Hole (Vector2 position) {
        this.position = position;
        ballTouched = false;
        hasBall = false;
        golden = false;
    }

    public void render (ShapeRenderer renderer) {
        //darker color border of hole
        renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
        renderer.arc(position.x, position.y, 8, 0, 180, 20);
        renderer.rect((position.x - 8), (position.y - 12), 16, 12);
        //lighter color inside of hole
        renderer.setColor(new Color(0.9f, 0.9f, 0.9f, 1));
        renderer.arc(position.x, position.y, 6, 0, 180, 20);
        renderer.rect((position.x - 6f), (position.y - 11f), 12, 11f);
        //ball
        if (hasBall) {
            //set colors
            if (golden) {
                renderer.setColor(Color.GOLD);
            } else {
                if (ballTouched) {
                    renderer.setColor(Color.FOREST);
                } else {
                    renderer.setColor(Color.MAROON);
                }
            }
            //draw ball: (actually, it's a hexagon!)
            renderer.circle(position.x, position.y - 6, 4, 6);
        }
    }

    boolean touchHole (Vector2 touchPosition) {
        return (touchPosition.x > position.x - 8) && (touchPosition.x < position.x + 8) && (touchPosition.y > position.y - 12) && (touchPosition.y < position.y + 2);
    }

}
