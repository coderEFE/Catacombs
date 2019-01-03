package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 6/24/2018.
 * Shield that can be used by Player to block enemy lasers and other obstacles
 */

public class Shield {

    private Vector2 position;
    public float shieldWidth = 6f;
    public Enums.Facing shieldFacing;
    public Vector2 shadowOffset;
    public boolean shadow;

    public Shield (Vector2 position) {
        this.position = position;
        shieldFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
    }
    
    public void render (ShapeRenderer renderer) {
        //make the different directions for the shield
        //special direction for the front of the shield
        if (shieldFacing == Enums.Facing.LEFT) {
            if (shadow) {
                //SHADOW
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.circle(position.x + 5 + shadowOffset.x, position.y + shadowOffset.y, 7, 7);
            }
            //ACTUAL
            //draw shield
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.circle(position.x + 5, position.y, 7, 7);
            //draw center
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.circle(position.x + 5, position.y, 2, 20);
            //draw bolts around shield's edge
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.circle(position.x, position.y, 0.75f, 20);
            renderer.circle(position.x + 10, position.y, 0.75f, 20);
            renderer.circle(position.x + 5, position.y + 5, 0.75f, 20);
            renderer.circle(position.x + 5, position.y - 5, 0.75f, 20);
        } else if (shieldFacing == Enums.Facing.RIGHT) {
            //No shadow for this direction
            //draw shield
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.circle(position.x, position.y, 7, 7);
            //draw center
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.circle(position.x, position.y, 2, 20);
            //draw bolts around shield's edge
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.circle(position.x - 5, position.y, 0.75f, 20);
            renderer.circle(position.x + 5, position.y, 0.75f, 20);
            renderer.circle(position.x, position.y + 5, 0.75f, 20);
            renderer.circle(position.x, position.y - 5, 0.75f, 20);
        }
    }

}
