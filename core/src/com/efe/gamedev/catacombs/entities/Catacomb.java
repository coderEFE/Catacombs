package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;


/**
 * Created by coder on 11/17/2017.
 */

public class Catacomb {

    public Vector2 position;
    private Array<String> lockedDoors;
    public float height;
    public float width;
    public float wallThickness;
    private Vector2 shadowOffset;

    //left side
    Vector2 topLeft;
    Vector2 middleLeft;
    public Vector2 bottomLeft;
    //right side
    Vector2 topRight;
    Vector2 middleRight;
    Vector2 bottomRight;

    public Vector2 bottomLeftOffset;
    Vector2 middleLeftOffset;
    Vector2 topLeftOffset;
    Vector2 bottomRightOffset;
    Vector2 middleRightOffset;
    Vector2 bottomsOffset;

    public Catacomb (Vector2 position, String Door1, String Door2, String Door3, String Door4, String Door5, String Door6) {
        this.position = position;
        lockedDoors = new Array<String>();
        lockedDoors.add(Door1);
        lockedDoors.add(Door2);
        lockedDoors.add(Door3);
        lockedDoors.add(Door4);
        lockedDoors.add(Door5);
        lockedDoors.add(Door6);
        height = 200;
        width = 200;
        wallThickness = 5;
        topLeft = new Vector2(position.x + 50, position.y + height - 15);
        middleLeft = new Vector2(position.x, position.y + height - 100);
        bottomLeft = new Vector2(position.x + 50, position.y + height - 185);
        topRight = new Vector2(position.x + width - 50, position.y + height - 15);
        middleRight = new Vector2(position.x + width, position.y + height - 100);
        bottomRight = new Vector2(position.x + width - 50, position.y + height - 185);
        bottomLeftOffset = new Vector2(0, 0);
        middleLeftOffset = new Vector2(0, 0);
        topLeftOffset = new Vector2(0, 0);
        bottomRightOffset = new Vector2(0, 0);
        middleRightOffset = new Vector2(0, 0);
        bottomsOffset = new Vector2(0, 0);
        shadowOffset = new Vector2(1.5f, 2);
    }

    public void update (float delta) {
        //Move catacomb walls when they are unlocked or locked
        float moveSpeed = 80;
        //bottomLeft
        if (lockedDoors.get(0).equals("Unlocked")) {
            if (bottomLeftOffset.y < 60) {
                bottomLeftOffset.y += delta * moveSpeed;
                bottomLeftOffset.x -= delta * (moveSpeed * 0.6f);
            }
        } else if (lockedDoors.get(0).equals("Locked") || lockedDoors.get(0).equals("DoubleLocked")) {
            if (bottomLeftOffset.y > 0) {
                bottomLeftOffset.y -= delta * moveSpeed;
                bottomLeftOffset.x += delta * (moveSpeed * 0.6f);
            }
        }
        //middleLeft
        if (lockedDoors.get(1).equals("Unlocked")) {
            if (middleLeftOffset.y < 60) {
                middleLeftOffset.y += delta * moveSpeed;
                middleLeftOffset.x += delta * (moveSpeed * 0.6f);
            }
        } else if (lockedDoors.get(1).equals("Locked") || lockedDoors.get(1).equals("DoubleLocked")) {
            if (middleLeftOffset.y > 0) {
                middleLeftOffset.y -= delta * moveSpeed;
                middleLeftOffset.x -= delta * (moveSpeed * 0.6f);
            }
        }
        //top
        if (lockedDoors.get(2).equals("Unlocked")) {
            if (topLeftOffset.x < 80) {
                topLeftOffset.x += delta * moveSpeed * 2f;
            }
        }
        //middleRight
        if (lockedDoors.get(3).equals("Unlocked")) {
            if (middleRightOffset.y < 60) {
                middleRightOffset.y += delta * moveSpeed;
                middleRightOffset.x -= delta * (moveSpeed * 0.6f);
            }
        } else if (lockedDoors.get(3).equals("Locked") || lockedDoors.get(3).equals("DoubleLocked")) {
            if (middleRightOffset.y > 0) {
                middleRightOffset.y -= delta * moveSpeed;
                middleRightOffset.x += delta * (moveSpeed * 0.6f);
            }
        }
        //bottomRight
        if (lockedDoors.get(4).equals("Unlocked")) {
            if (bottomRightOffset.y < 60) {
                bottomRightOffset.y += delta * moveSpeed;
                bottomRightOffset.x += delta * (moveSpeed * 0.6f);
            }
        } else if (lockedDoors.get(4).equals("Locked") || lockedDoors.get(4).equals("DoubleLocked")) {
            if (bottomRightOffset.y > 0) {
                bottomRightOffset.y -= delta * moveSpeed;
                bottomRightOffset.x -= delta * (moveSpeed * 0.6f);
            }
        }
        //bottom
        if (lockedDoors.get(5).equals("Unlocked")) {
            if (bottomsOffset.x > -80) {
                bottomsOffset.x -= delta * moveSpeed * 2f;
            }
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //general shape
        renderer.setColor(Color.GRAY);
        renderer.ellipse(position.x, position.y, width, height, 0, 6);
    }

    public void renderWalls (ShapeRenderer renderer) {
        //catacomb walls
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //SHADOW
        renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.5f));
        renderer.rectLine(bottomLeft.x + bottomLeftOffset.x + shadowOffset.x, bottomLeft.y + bottomLeftOffset.y + shadowOffset.y, middleLeft.x + shadowOffset.x, middleLeft.y + shadowOffset.y, wallThickness);
        renderer.rectLine(middleLeft.x + middleLeftOffset.x + shadowOffset.x + 2, middleLeft.y + middleLeftOffset.y + shadowOffset.y, topLeft.x + shadowOffset.x, topLeft.y + shadowOffset.y - 3, wallThickness);
        renderer.rectLine(bottomRight.x + bottomsOffset.x + shadowOffset.x + 2, bottomRight.y + bottomsOffset.y + shadowOffset.y, bottomLeft.x + shadowOffset.x, bottomLeft.y + shadowOffset.y, wallThickness);
        //ACTUAL
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rectLine(bottomLeft.x + bottomLeftOffset.x, bottomLeft.y + bottomLeftOffset.y, middleLeft.x, middleLeft.y, wallThickness);
        renderer.rectLine(middleLeft.x + middleLeftOffset.x, middleLeft.y + middleLeftOffset.y, topLeft.x, topLeft.y, wallThickness);
        renderer.rectLine(topLeft.x  + topLeftOffset.x, topLeft.y + topLeftOffset.y, topRight.x, topRight.y, wallThickness);
        renderer.rectLine(topRight.x, topRight.y, middleRight.x + middleRightOffset.x, middleRight.y + middleRightOffset.y, wallThickness);
        renderer.rectLine(middleRight.x, middleRight.y, bottomRight.x + bottomRightOffset.x, bottomRight.y + bottomRightOffset.y, wallThickness);
        renderer.rectLine(bottomRight.x + bottomsOffset.x, bottomRight.y + bottomsOffset.y, bottomLeft.x, bottomLeft.y, wallThickness);
    }

    public void renderButton (ShapeRenderer renderer) {
        //buttons for toggling locked and unlocked
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //bottomLeft
        if (lockedDoors.get(0).equals("Unlocked")) {
            renderer.setColor(Color.FOREST);
            renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(0).equals("Locked")) {
            renderer.setColor(Color.MAROON);
            renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(0).equals("DoubleLocked")) {
            renderer.setColor(Color.PURPLE);
            renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
        }
        //bottomRight
        if (lockedDoors.get(4).equals("Unlocked")) {
            renderer.setColor(Color.FOREST);
            renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(4).equals("Locked")) {
            renderer.setColor(Color.MAROON);
            renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(4).equals("DoubleLocked")) {
            renderer.setColor(Color.PURPLE);
            renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
        }
        //middleLeft
        if (lockedDoors.get(1).equals("Unlocked")) {
            renderer.setColor(Color.FOREST);
            renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(1).equals("Locked")) {
            renderer.setColor(Color.MAROON);
            renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(1).equals("DoubleLocked")) {
            renderer.setColor(Color.PURPLE);
            renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
        }
        //middleRight
        if (lockedDoors.get(3).equals("Unlocked")) {
            renderer.setColor(Color.FOREST);
            renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(3).equals("Locked")) {
            renderer.setColor(Color.MAROON);
            renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
        } else if (lockedDoors.get(3).equals("DoubleLocked")) {
            renderer.setColor(Color.PURPLE);
            renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
        }
    }

    public Array<String> getLockedDoors () {
        return lockedDoors;
    }

 }