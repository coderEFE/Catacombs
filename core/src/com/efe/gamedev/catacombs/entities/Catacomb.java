package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/17/2017.
 * This is the Catacomb class, which make up most of the level environments and can be long or skinny widths
 */

public class Catacomb {

    public Vector2 position;
    private Array<String> lockedDoors;
    public float height;
    public float width;
    public boolean longCatacomb;
    public float wallThickness;
    private Vector2 shadowOffset;

    //left side
    private Vector2 topLeft;
    Vector2 middleLeft;
    Vector2 bottomLeft;
    //right side
    private Vector2 topRight;
    Vector2 middleRight;
    Vector2 bottomRight;

    public Vector2 bottomLeftOffset;
    Vector2 middleLeftOffset;
    private Vector2 topLeftOffset;
    Vector2 bottomRightOffset;
    Vector2 middleRightOffset;
    public Vector2 bottomsOffset;

    //drop stalactites
    public boolean stalactites;
    public boolean drop;
    public boolean floorSpikes;
    private Array<Stalactite> catacombStalactites;
    private Level level;
    private float fadeRed;

    public Catacomb (Vector2 position, String Door1, String Door2, String Door3, String Door4, String Door5, String Door6, Level level) {
        this.position = position;
        lockedDoors = new Array<>();
        lockedDoors.add(Door1);
        lockedDoors.add(Door2);
        lockedDoors.add(Door3);
        lockedDoors.add(Door4);
        lockedDoors.add(Door5);
        lockedDoors.add(Door6);
        height = 200;
        width = 200;
        longCatacomb = false;
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
        stalactites = false;
        drop = false;
        catacombStalactites = new Array<>();
        floorSpikes = false;
        this.level = level;
        fadeRed = 0;
    }

    public void update (float delta) {
        /* MOVE CATACOMB WALLS */
        //Move catacomb walls when they are unlocked or locked
        float moveSpeed = 80;
        //bottomLeft
        if (lockedDoors.get(0).equals("Unlocked")) {
            if (bottomLeftOffset.y < 60) {
                bottomLeftOffset.y += delta * moveSpeed;
                bottomLeftOffset.x -= delta * (moveSpeed * 0.6f);
            } else {
                bottomLeftOffset.y = 60;
                bottomLeftOffset.x = -36.5f;
            }
        } else if (lockedDoors.get(0).equals("Locked") || lockedDoors.get(0).equals("DoubleLocked") || lockedDoors.get(0).equals("Closed")) {
            if (bottomLeftOffset.y > 0) {
                bottomLeftOffset.y -= delta * moveSpeed;
                bottomLeftOffset.x += delta * (moveSpeed * 0.6f);
            } else {
                bottomLeftOffset.y = 0;
                bottomLeftOffset.x = 0;
            }
        }
        //middleLeft
        if (lockedDoors.get(1).equals("Unlocked")) {
            if (middleLeftOffset.y < 60) {
                middleLeftOffset.y += delta * moveSpeed;
                middleLeftOffset.x += delta * (moveSpeed * 0.6f);
            } else {
                middleLeftOffset.y = 60;
                middleLeftOffset.x = 36.5f;
            }
        } else if (lockedDoors.get(1).equals("Locked") || lockedDoors.get(1).equals("DoubleLocked") || lockedDoors.get(1).equals("Closed")) {
            if (middleLeftOffset.y > 0) {
                middleLeftOffset.y -= delta * moveSpeed;
                middleLeftOffset.x -= delta * (moveSpeed * 0.6f);
            }else {
                middleLeftOffset.y = 0;
                middleLeftOffset.x = 0;
            }
        }
        //top
        if (lockedDoors.get(2).equals("Unlocked")) {
            if (topLeftOffset.x < (level.superior.currentLevel == 14 ? (width - 250) : (width - 120))) {
                topLeftOffset.x += delta * moveSpeed * 2f;
            } else {
                topLeftOffset.x = (level.superior.currentLevel == 14 ? (width - 250) : (width - 120));
            }
        } else {
            if (topLeftOffset.x > 0) {
                topLeftOffset.x -= delta * moveSpeed * 2f;
            } else {
                topLeftOffset.x = 0;
            }
        }
        //middleRight
        if (lockedDoors.get(3).equals("Unlocked")) {
            if (middleRightOffset.y < 60) {
                middleRightOffset.y += delta * moveSpeed;
                middleRightOffset.x -= delta * (moveSpeed * 0.6f);
            } else {
                middleRightOffset.y = 60;
                middleRightOffset.x = -36.5f;
            }
        } else if (lockedDoors.get(3).equals("Locked") || lockedDoors.get(3).equals("DoubleLocked") || lockedDoors.get(3).equals("Closed")) {
            if (middleRightOffset.y > 0) {
                middleRightOffset.y -= delta * moveSpeed;
                middleRightOffset.x += delta * (moveSpeed * 0.6f);
            } else {
                middleRightOffset.y = 0;
                middleRightOffset.x = 0;
            }
        }
        //bottomRight
        if (lockedDoors.get(4).equals("Unlocked")) {
            if (bottomRightOffset.y < 60) {
                bottomRightOffset.y += delta * moveSpeed;
                bottomRightOffset.x += delta * (moveSpeed * 0.6f);
            } else {
                bottomRightOffset.y = 60;
                bottomRightOffset.x = 36.5f;
            }
        } else if (lockedDoors.get(4).equals("Locked") || lockedDoors.get(4).equals("DoubleLocked") || lockedDoors.get(4).equals("Closed")) {
            if (bottomRightOffset.y > 0) {
                bottomRightOffset.y -= delta * moveSpeed;
                bottomRightOffset.x -= delta * (moveSpeed * 0.6f);
            } else {
                bottomRightOffset.y = 0;
                bottomRightOffset.x = 0;
            }
        }
        //bottom
        if (lockedDoors.get(5).equals("Unlocked")) {
            if (bottomsOffset.x > (-1 * (width - 120))) {
                bottomsOffset.x -= delta * moveSpeed * 2f;
            } else {
                bottomsOffset.x = (-1 * (width - 120));
            }
        } else {
            if (bottomsOffset.x < 0) {
                bottomsOffset.x += delta * moveSpeed * 2f;
            } else {
                bottomsOffset.x = 0;
            }
        }
        /* UPDATE STALACTITES */
        //loop through all stalactites, if drop is true, then spawn stalactites in random positions and remove them if they touch the player or ground.
        for (int i = 0; i < catacombStalactites.size; i++) {
            if (drop) {
                catacombStalactites.get(i).update(delta);
                if (catacombStalactites.get(i).position.y < bottomLeft.y + catacombStalactites.get(i).size) {
                    if (!catacombStalactites.get(i).hit) {
                        catacombStalactites.get(i).hit = true;
                        if (level.gameplayScreen.game.getSoundEffectsOn()) {
                            level.gameplayScreen.sound8.play();
                        }
                    }
                    if (catacombStalactites.get(i).fadeTimer > 20) {
                        catacombStalactites.removeIndex(i);
                    }
                }
                //player yells when they get close
                if ((new Vector2(catacombStalactites.get(i).position.x, catacombStalactites.get(i).position.y - catacombStalactites.get(i).size)).dst(level.getPlayer().getPosition()) < Constants.HEAD_SIZE * 4f && level.currentCatacomb == level.catacombs.indexOf(this, true)) {
                    level.getPlayer().mouthState = Enums.MouthState.OPEN;
                }
                //remove stalactites when hit player
                if ((new Vector2(catacombStalactites.get(i).position.x, catacombStalactites.get(i).position.y - catacombStalactites.get(i).size)).dst(level.getPlayer().getPosition()) < Constants.HEAD_SIZE) {
                    if (level.getPlayer().health > 0) {
                        level.getPlayer().health -= 5;
                        if (level.gameplayScreen.game.getVibrationOn()) {
                            // Vibrate device for 400 milliseconds
                            Gdx.input.vibrate(400);
                        }
                    }
                    catacombStalactites.removeIndex(i);
                }
            }
        }
        //spawn stalactites
        if (drop) {
            //spawn rate of stalactites is less if you are in a smaller catacomb
            if (MathUtils.random() < (width == 300 ? (delta * 5f) : (delta * 3f))) {
                catacombStalactites.add(new Stalactite(new Vector2(MathUtils.random(position.x + 60, position.x + width - 50), (position.y + height) - 17), MathUtils.random(20, 30)));
            }
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //if the catacomb is a "long catacomb", set width to 300 and reset corners of catacomb
        if (longCatacomb) {
            width = 300;
            topLeft = new Vector2(position.x + 50, position.y + height - 15);
            middleLeft = new Vector2(position.x, position.y + height - 100);
            bottomLeft = new Vector2(position.x + 50, position.y + height - 185);
            topRight = new Vector2(position.x + width - 50, position.y + height - 15);
            middleRight = new Vector2(position.x + width, position.y + height - 100);
            bottomRight = new Vector2(position.x + width - 50, position.y + height - 185);
        }
        //general shape
        renderer.setColor(Color.GRAY);
        //renderer.ellipse(position.x, position.y, width, height, 0, 6);
        //render the background shape of the catacomb
        renderer.triangle(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, middleLeft.x, middleLeft.y);
        renderer.triangle(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, middleRight.x, middleRight.y);
        renderer.triangle(topLeft.x, topLeft.y, topRight.x, topRight.y, middleRight.x, middleRight.y);
        renderer.triangle(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, middleRight.x, middleRight.y);
    }

    //this renders a translucent red covering over the catacomb when the player tries to place an item in a catacomb that the player is not in.
    public void renderRedCatacomb (ShapeRenderer renderer) {
        //checks if the player is trying to place an item and if this is not the player's currentCatacomb
        if (level.inventory.dragItem && level.catacombs.indexOf(this, true) != level.currentCatacomb) {
            //red translucent color
            renderer.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, fadeRed));
            //render the red translucent shape of the catacomb
            renderer.triangle(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, middleLeft.x, middleLeft.y);
            renderer.triangle(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, middleRight.x, middleRight.y);
            renderer.triangle(topLeft.x, topLeft.y, topRight.x, topRight.y, middleRight.x, middleRight.y);
            renderer.triangle(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, middleRight.x, middleRight.y);
            //make covering gradually look red
            if (fadeRed < 0.3f) {
                fadeRed += 0.01f;
            }
        } else {
            if (fadeRed > 0f) {
                fadeRed -= 0.01f;
            }
        }
    }

    public void renderWalls (ShapeRenderer renderer) {
        //catacomb walls
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //Draw stalactites
        if (stalactites) {
            //((catacombStalactites.get(catacombStalactites.size - 1)).size/2)
            if (!drop) {
                catacombStalactites = new Array<>();
                for (int i = 0; i < (width - 100); i += 10) {
                    //draw stalactites
                    renderer.setColor(Color.DARK_GRAY);
                    renderer.triangle((position.x + 60 + i) - (20 / 5), ((position.y + height) - 17), (position.x + 60 + i) + (20 / 5), ((position.y + height) - 17), (position.x + 60 + i), ((position.y + height) - 17) - 20);
                }
            }
            for (int i = 0; i < catacombStalactites.size; i++) {
                catacombStalactites.get(i).render(renderer);
            }
        }
        //draw spikes on floor
        if (floorSpikes) {
            for (int i = 0; i < (width - 100); i += 10) {
                //draw spikes
                renderer.setColor(Color.DARK_GRAY);
                renderer.triangle((position.x + 60 + i) - (20 / 5), ((position.y + 30) - 17), (position.x + 60 + i) + (20 / 5), ((position.y + 30) - 17), (position.x + 60 + i), ((position.y + 30) - 17) + 20);
            }
        }
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

    public boolean touchingFloorSpikes (Vector2 objectPosition) {
        return (floorSpikes && objectPosition.x > bottomLeft.x && objectPosition.x < bottomRight.x && objectPosition.y > bottomLeft.y && objectPosition.y < bottomLeft.y + 20);
    }

    public void renderButton (ShapeRenderer renderer) {
        //buttons for toggling locked and unlocked
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //Forest color means unlocked, Maroon color means locked, and Purple color means double-locked
        //bottomLeft
        switch (lockedDoors.get(0)) {
            case "Unlocked":
                renderer.setColor(Color.FOREST);
                renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
                break;
            case "Locked":
                renderer.setColor(Color.MAROON);
                renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
                break;
            case "DoubleLocked":
                renderer.setColor(Color.PURPLE);
                renderer.circle(bottomLeft.x + bottomLeftOffset.x - 5.5f, bottomLeft.y + bottomLeftOffset.y + 10, wallThickness / 2, 10);
                break;
        }
        //bottomRight
        switch (lockedDoors.get(4)) {
            case "Unlocked":
                renderer.setColor(Color.FOREST);
                renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
                break;
            case "Locked":
                renderer.setColor(Color.MAROON);
                renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
                break;
            case "DoubleLocked":
                renderer.setColor(Color.PURPLE);
                renderer.circle(bottomRight.x + bottomRightOffset.x + 5.5f, bottomRight.y + bottomRightOffset.y + 10, wallThickness / 2, 10);
                break;
        }
        //middleLeft
        switch (lockedDoors.get(1)) {
            case "Unlocked":
                renderer.setColor(Color.FOREST);
                renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
                break;
            case "Locked":
                renderer.setColor(Color.MAROON);
                renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
                break;
            case "DoubleLocked":
                renderer.setColor(Color.PURPLE);
                renderer.circle(middleLeft.x + middleLeftOffset.x + 5.5f, middleLeft.y + middleLeftOffset.y + 10, wallThickness / 2, 10);
                break;
        }
        //middleRight
        switch (lockedDoors.get(3)) {
            case "Unlocked":
                renderer.setColor(Color.FOREST);
                renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
                break;
            case "Locked":
                renderer.setColor(Color.MAROON);
                renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
                break;
            case "DoubleLocked":
                renderer.setColor(Color.PURPLE);
                renderer.circle(middleRight.x + middleRightOffset.x - 5.5f, middleRight.y + middleRightOffset.y + 10, wallThickness / 2, 10);
                break;
        }
    }

    public Array<String> getLockedDoors () {
        return lockedDoors;
    }

 }