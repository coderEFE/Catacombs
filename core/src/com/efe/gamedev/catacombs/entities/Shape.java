package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 1/17/2018.
 */

public class Shape {
    private Level level;
    private Enums.Shape shapeType;
    private Vector2 shapePosition;
    private float size;
    public boolean changable;
    public boolean solved;

    public Shape (Vector2 shapePosition, Level level, float size) {
        this.shapePosition = shapePosition;
        this.level = level;
        changable = true;
        shapeType = Enums.Shape.SQUARE;
        this.size = size;
        solved = false;
    }
    public void render (ShapeRenderer renderer) {
        //if puzzle is solved, prevent player from changing it
        if (solved) {
            changable = false;
        }
        //toggle shapeTypes
        if (level.touchPosition.x > shapePosition.x && level.touchPosition.x < shapePosition.x + 15 && level.touchPosition.y > shapePosition.y && level.touchPosition.y < shapePosition.y + 15 && changable) {
            if (shapeType == Enums.Shape.SQUARE) {
                shapeType = Enums.Shape.TRIANGLE;
                level.touchPosition = new Vector2();
            } else if (shapeType == Enums.Shape.TRIANGLE) {
                shapeType = Enums.Shape.CIRCLE;
                level.touchPosition = new Vector2();
            } else if (shapeType == Enums.Shape.CIRCLE) {
                shapeType = Enums.Shape.SQUARE;
                level.touchPosition = new Vector2();
            }
        }

        if (shapeType == Enums.Shape.SQUARE) {
            renderer.setColor(solved ? Color.GOLD : Color.PURPLE);
            renderer.rect(shapePosition.x, shapePosition.y, size, size);
        } else if (shapeType == Enums.Shape.TRIANGLE) {
            renderer.setColor(solved ? Color.GOLD : Color.RED);
            renderer.triangle(shapePosition.x, shapePosition.y, shapePosition.x + size, shapePosition.y, shapePosition.x + (size/2f), shapePosition.y + size);
        } else if (shapeType == Enums.Shape.CIRCLE) {
            renderer.setColor(solved ? Color.GOLD : Color.BLUE);
            renderer.circle(shapePosition.x + (size/2f), shapePosition.y + (size/2f), (size/2f));
        }
    }

    public Enums.Shape getShapeType () {
        return shapeType;
    }

    public void setShapeType (Enums.Shape newShape) {
        shapeType = newShape;
    }
}
