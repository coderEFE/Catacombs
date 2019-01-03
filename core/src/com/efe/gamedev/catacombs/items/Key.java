package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/24/2017.
 * The key that opens Catacomb doors
 * can be used to open locks and double-locks
 * There are also Double-Keys that can only open double-locks
 */

public class Key {

    private Vector2 position;
    public float keyWidth = MathUtils.random(8, 12);
    public Enums.Facing keyFacing;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Key (Vector2 position) {
        this.position = position;
        keyFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }

    public void render (ShapeRenderer renderer, boolean doubleKey) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //if key is a doubleKey, then have a big width
        if (doubleKey) {
            keyWidth = 10;
        }
        //make the different directions for key
        if (keyFacing == Enums.Facing.LEFT) {
            if (shadow) {
                //SHADOW
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.rectLine(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + keyWidth + shadowOffset.x, position.y + shadowOffset.y, 1);
                //picks on key
                renderer.rectLine(position.x + shadowOffset.x, position.y + shadowOffset.y, position.x + shadowOffset.x, (position.y - 3) + shadowOffset.y, 1);
                renderer.rectLine(position.x + (doubleKey ? (keyWidth / 6f) : (keyWidth / 5f)) + shadowOffset.x, position.y + shadowOffset.y, position.x + (doubleKey ? (keyWidth / 6f) : (keyWidth / 5f)) + shadowOffset.x, (position.y - 2) + shadowOffset.y, 1);
                //add a fourth if key is a doubleKey
                if (doubleKey) {
                    renderer.rectLine(position.x + (keyWidth / 4f) + shadowOffset.x, position.y + shadowOffset.y, position.x + (keyWidth / 4f) + shadowOffset.x, (position.y - 2) + shadowOffset.y, 1);
                }
                renderer.rectLine(position.x + (keyWidth * 0.4f) + shadowOffset.x, position.y + shadowOffset.y, position.x + (keyWidth * 0.4f) + shadowOffset.x, (position.y - 3) + shadowOffset.y, 1);
                //hand grip (the circle)
                renderer.circle(position.x + keyWidth + shadowOffset.x, position.y + shadowOffset.y, 2, 10);
            }
            //ACTUAL
            //draw golden key if normal, else if key is a doubleKey, then draw key purple
            renderer.setColor(doubleKey ? Color.PURPLE : Color.GOLD);
            //main line
            renderer.rectLine(position.x, position.y, position.x + keyWidth, position.y, 1);
            //picks on key
            renderer.rectLine(position.x, position.y, position.x, position.y - 3, 1);
            renderer.rectLine(position.x + (doubleKey ? (keyWidth / 6f) : (keyWidth / 5f)), position.y, position.x + (doubleKey ? (keyWidth / 6f) : (keyWidth / 5f)), position.y - 2, 1);
            //add a fourth if key is a doubleKey
            if (doubleKey) {
                renderer.rectLine(position.x + (keyWidth / 4f), position.y, position.x + (keyWidth / 4f), position.y - 2, 1);
            }
            renderer.rectLine(position.x + (keyWidth * 0.4f), position.y, position.x + (keyWidth * 0.4f), position.y - 3, 1);
            //hand grip (the circle)
            renderer.circle(position.x + keyWidth, position.y, 2, 10);
        } else if (keyFacing == Enums.Facing.RIGHT) {
            //No shadow for this direction
            //draw golden key if normal, else if key is a doubleKey, then draw key purple
            renderer.setColor(doubleKey ? Color.PURPLE : Color.GOLD);
            //main line
            renderer.rectLine(position.x, position.y, position.x + keyWidth, position.y, 1);
            //picks on key
            renderer.rectLine(position.x + keyWidth, position.y, position.x + keyWidth, position.y - 3, 1);
            renderer.rectLine(position.x + keyWidth - (keyWidth / 6f), position.y, position.x + keyWidth - (keyWidth / 5f), position.y - 2, 1);
            //add a fourth if key is a doubleKey
            if (doubleKey) {
                renderer.rectLine(position.x + keyWidth - (keyWidth / 4f), position.y, position.x + keyWidth - (keyWidth / 4f), position.y - 2, 1);
            }
            renderer.rectLine(position.x + keyWidth - (keyWidth * 0.4f), position.y, position.x + keyWidth - (keyWidth * 0.4f), position.y - 3, 1);
            //hand grip (the circle)
            renderer.circle(position.x, position.y, 2, 10);
        }
    }

}
