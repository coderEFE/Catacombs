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
    public boolean changeable;
    public boolean solved;

    public Shape (Vector2 shapePosition, Level level, float size) {
        this.shapePosition = shapePosition;
        this.level = level;
        changeable = true;
        shapeType = Enums.Shape.SQUARE;
        this.size = size;
        solved = false;
    }
    public void render (ShapeRenderer renderer) {
        //if puzzle is solved, prevent player from changing it
        if (solved) {
            changeable = false;
        }
        //toggle shapeTypes if tapped: SQUARE to TRIANGLE to CIRCLE then back to SQUARE
        if (level.touchPosition.x > shapePosition.x && level.touchPosition.x < shapePosition.x + 15 && level.touchPosition.y > shapePosition.y && level.touchPosition.y < shapePosition.y + 15 && changeable) {
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
        //draw shapes depending on type of shape
        //change color depending on which shape it is. If square, draw purple. If circle, draw blue. If triangle, draw red. If solved, draw all gold. If not changeable, draw all dark gray.
        if (shapeType == Enums.Shape.SQUARE) {
            renderer.setColor(solved ? Color.GOLD : (changeable ? Color.PURPLE : Color.GRAY));
            renderer.rect(shapePosition.x, shapePosition.y, size, size);
        } else if (shapeType == Enums.Shape.TRIANGLE) {
            renderer.setColor(solved ? Color.GOLD : (changeable ? Color.RED : Color.GRAY));
            renderer.triangle(shapePosition.x, shapePosition.y, shapePosition.x + size, shapePosition.y, shapePosition.x + (size/2f), shapePosition.y + size);
        } else if (shapeType == Enums.Shape.CIRCLE) {
            renderer.setColor(solved ? Color.GOLD : (changeable ? Color.BLUE : Color.GRAY));
            renderer.circle(shapePosition.x + (size/2f), shapePosition.y + (size/2f), (size/2f));
        }
    }

    public Enums.Shape getShapeType () {
        return shapeType;
    }

    public void setShapeType (Enums.Shape newShape) {
        shapeType = newShape;
    }

    public void setShapePosition (Vector2 newShapePosition) {
        shapePosition.set(newShapePosition);
    }
}
