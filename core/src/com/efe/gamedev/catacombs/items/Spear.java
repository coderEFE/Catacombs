package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 9/29/2018.
 * Spear that is held by the Boss in the final level of Catacombs.
 */

public class Spear {

    public Vector2 position;
    public float spearWidth = 8;
    public Vector2 shadowOffset;
    public Enums.Facing spearFacing;
    public boolean shadow;
    public String strikeStatus;

    public Spear(Vector2 position) {
        this.position = position;
        shadowOffset = new Vector2(2, 1.5f);
        spearFacing = Enums.Facing.RIGHT;
        shadow = true;
        strikeStatus = "BLOCK";
    }

    public void render(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //find distance of spear using the distance formula between two points
        double spearDistance = Math.sqrt(Math.pow((position.x - 2) - (position.x + 10), 2) + Math.pow((position.y - 5) - (position.y + 13), 2));

        if (spearFacing == Enums.Facing.LEFT) {
            //make the spear different directions
            //ACTUAL
            switch (strikeStatus) {
                case "BLOCK": {
                    Vector2 spearOffset = new Vector2(0, 0);
                    //draw gray spear
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine(((position.x + spearOffset.x) + spearOffset.x) + 8, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 4, ((position.x + spearOffset.x) + spearOffset.x) - 4, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 22, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine(((position.x + spearOffset.x) + spearOffset.x) + 1, (((position.y + spearOffset.y) + spearOffset.y) - 6) + 4, (((position.x + spearOffset.x) + spearOffset.x) + 1) + (spearWidth * 0.8f), ((position.y + spearOffset.y) + spearOffset.y) + 4, 1.5f);
                    //spear point
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    renderer.triangle(((position.x + spearOffset.x) + spearOffset.x) - 4f, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 18f, ((position.x + spearOffset.x) + spearOffset.x) + (spearWidth / 2) - 4.5f, ((position.y + spearOffset.y) + spearOffset.y) + 12f, ((position.x + spearOffset.x) + spearOffset.x) - 6f, (((position.y + spearOffset.y) + spearOffset.y)) + 16);
                    //decorating lines
                    renderer.set(ShapeRenderer.ShapeType.Line);
                    renderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
                    renderer.line(((position.x + spearOffset.x) + spearOffset.x) + 4.1f, ((position.y + spearOffset.y) + spearOffset.y) - 1, ((position.x + spearOffset.x) + spearOffset.x) + 4 + (spearWidth / 4), ((position.y + spearOffset.y) + spearOffset.y));
                    renderer.line(((position.x + spearOffset.x) + spearOffset.x) + 4.7f, ((position.y + spearOffset.y) + spearOffset.y) - 2, ((position.x + spearOffset.x) + spearOffset.x) + 4.5f + (spearWidth / 4), ((position.y + spearOffset.y) + spearOffset.y) - 1);
                    renderer.line(((position.x + spearOffset.x) + spearOffset.x) + 5.4f, ((position.y + spearOffset.y) + spearOffset.y) - 3, ((position.x + spearOffset.x) + spearOffset.x) + 6.1f + (spearWidth / 4), ((position.y + spearOffset.y) + spearOffset.y) - 4);
                    break;
                }
                case "PARRY": {
                    Vector2 spearOffset = new Vector2(0, 0);
                    renderer.set(ShapeRenderer.ShapeType.Filled);
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine((position.x + spearOffset.x) - 12, ((position.y + spearOffset.y) - 9) + 8, (position.x + spearOffset.x) + (float) (spearDistance) - 10, ((position.y + spearOffset.y) - 9) + 8, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine(((position.x + spearOffset.x) - 1) + (spearWidth * 0.8f), (position.y + spearOffset.y) + 3.5f, ((position.x + spearOffset.x) - 1) + (spearWidth * 0.8f), (position.y + spearOffset.y) - 5.5f, 1.6f);
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    //spear point
                    //renderer.triangle((position.x + spearOffset.x) + (spearWidth / 2) + 6f, ((position.y + spearOffset.y) + 9f), (position.x + spearOffset.x) + 6.5f, (position.y + spearOffset.y) + 12f, (position.x + spearOffset.x) + (spearWidth / 2) + 8f, ((position.y + spearOffset.y)) + 16);
                    renderer.triangle((position.x + spearOffset.x) - 8f, ((position.y + spearOffset.y) + 1.5f), (position.x + spearOffset.x) - 8f, ((position.y + spearOffset.y) - 3.5f), (position.x + spearOffset.x) - 15.5f, ((position.y + spearOffset.y) - 1f));
                    break;
                }
                case "THRUST": {
                    Vector2 spearOffset = new Vector2(0, 2.5f);
                    //draw gray spear
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine((position.x + spearOffset.x) + 8, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 4, (position.x + spearOffset.x) - 4, (((position.y + spearOffset.y) + spearOffset.y) - 9) - 14, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine((position.x + spearOffset.x) + 1, (((position.y + spearOffset.y) + spearOffset.y) - 8), ((position.x + spearOffset.x) + 1) + (spearWidth * 0.8f), ((position.y + spearOffset.y) + spearOffset.y) - 14, 1.5f);
                    //spear point
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    renderer.triangle((position.x + spearOffset.x) - 4f, (((position.y + spearOffset.y) + spearOffset.y) - 19), (position.x + spearOffset.x) + (spearWidth / 2) - 4.5f, ((position.y + spearOffset.y) + spearOffset.y) - 22f, (position.x + spearOffset.x) - 6f, (((position.y + spearOffset.y) + spearOffset.y)) - 26);
                    break;
                }
            }
        } else if (spearFacing == Enums.Facing.RIGHT) {
            //draw gray spear
            switch (strikeStatus) {
                case "BLOCK": {
                    Vector2 spearOffset = new Vector2(0, 0);
                    renderer.set(ShapeRenderer.ShapeType.Filled);
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine((position.x + spearOffset.x) - 2, ((position.y + spearOffset.y) - 9) + 4, (position.x + spearOffset.x) + 10, ((position.y + spearOffset.y) - 9) + 22, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine((position.x + spearOffset.x) - 1, (position.y + spearOffset.y) + 4, ((position.x + spearOffset.x) - 1) + (spearWidth * 0.8f), (position.y + spearOffset.y) - 2, 1.5f);
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    //spear point
                    renderer.triangle((position.x + spearOffset.x) + (spearWidth / 2) + 6f, ((position.y + spearOffset.y) - 9) + 18f, (position.x + spearOffset.x) + 6.5f, (position.y + spearOffset.y) + 12f, (position.x + spearOffset.x) + (spearWidth / 2) + 8f, ((position.y + spearOffset.y)) + 16);
                    break;
                }
                case "PARRY": {
                    Vector2 spearOffset = new Vector2(0, 0);
                    renderer.set(ShapeRenderer.ShapeType.Filled);
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine(((position.x + spearOffset.x) + spearOffset.x) - 2, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 8, ((position.x + spearOffset.x) + spearOffset.x) + (float) (spearDistance), (((position.y + spearOffset.y) + spearOffset.y) - 9) + 8, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine((((position.x + spearOffset.x) + spearOffset.x) - 1) + (spearWidth * 0.8f) - 1, ((position.y + spearOffset.y) + spearOffset.y) + 3.5f, (((position.x + spearOffset.x) + spearOffset.x) - 1) + (spearWidth * 0.8f) - 1, ((position.y + spearOffset.y) + spearOffset.y) - 5.5f, 1.6f);
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    //spear point
                    //renderer.triangle(((position.x + spearOffset.x) + spearOffset.x) + (spearWidth / 2) + 6f, (((position.y + spearOffset.y) + spearOffset.y) + 9f), ((position.x + spearOffset.x) + spearOffset.x) + 6.5f, ((position.y + spearOffset.y) + spearOffset.y) + 12f, ((position.x + spearOffset.x) + spearOffset.x) + (spearWidth / 2) + 8f, (((position.y + spearOffset.y) + spearOffset.y)) + 16);
                    renderer.triangle(((position.x + spearOffset.x) + spearOffset.x) + (float) (spearDistance) - 4f, (((position.y + spearOffset.y) + spearOffset.y) + 1.5f), ((position.x + spearOffset.x) + spearOffset.x) + (float) (spearDistance) - 4f, (((position.y + spearOffset.y) + spearOffset.y) - 3.5f), ((position.x + spearOffset.x) + spearOffset.x) + (float) (spearDistance) + 3.5f, (((position.y + spearOffset.y) + spearOffset.y) - 1f));
                    break;
                }
                case "THRUST": {
                    Vector2 spearOffset = new Vector2(1, 2.5f);
                    renderer.set(ShapeRenderer.ShapeType.Filled);
                    renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                    //handle
                    renderer.rectLine(((position.x + spearOffset.x) + spearOffset.x) - 2, (((position.y + spearOffset.y) + spearOffset.y) - 9) + 4, ((position.x + spearOffset.x) + spearOffset.x) + 10, (((position.y + spearOffset.y) + spearOffset.y) - 9) - 14, spearWidth / 3);
                    //bar between spear point and handle
                    renderer.rectLine(((position.x + spearOffset.x) + spearOffset.x) - 1, ((position.y + spearOffset.y) + spearOffset.y) - 14, (((position.x + spearOffset.x) + spearOffset.x) - 1) + (spearWidth * 0.8f), ((position.y + spearOffset.y) + spearOffset.y) - 8, 1.5f);
                    renderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1));
                    //spear point
                    renderer.triangle(((position.x + spearOffset.x) + spearOffset.x) + (spearWidth / 2) + 6f, ((position.y + spearOffset.y) + spearOffset.y) - 19, ((position.x + spearOffset.x) + spearOffset.x) + 6.5f, ((position.y + spearOffset.y) + spearOffset.y) - 22, ((position.x + spearOffset.x) + spearOffset.x) + (spearWidth / 2) + 8f, (((position.y + spearOffset.y) + spearOffset.y)) - 26);
                    break;
                }
            }
        }
    }
    
}
