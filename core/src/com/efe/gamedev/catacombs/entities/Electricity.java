package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 8/13/2018.
 * This is a puzzle where you have to complete an electrical circuit to solve the puzzle by sliding metal pieces into place
 * In levels that have this puzzle in, there are arrays of this class where each class is a single wire
 * This is one of the most complicated puzzles in the game (and the coolest)
 */

public class Electricity {

    private Vector2 startPosition;
    private Vector2 endPosition;
    //if metalWidth = 0, there is no metal piece at the end of that electrical wire
    private float metalWidth;
    private boolean metalOn;
    private boolean metalUp;
    private float metalSlide;
    //Whether this wire is on or not
    private boolean on;
    //whether this wire is the first wire to turn on or not
    public boolean first;
    //the function that is activated when this wire is on
    private Runnable onFunction;
    //the direction in which the electricity is flowing
    private String firstWireDirection;
    //this boolean makes sure that the onFunction will only be run once
    private boolean runFunction;

    public Electricity (Vector2 startPosition, Vector2 endPosition, float metalWidth, final Runnable onFunction) {
        //initialize variables
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.metalWidth = metalWidth;
        this.onFunction = onFunction;
        first = false;
        metalOn = false;
        metalUp = false;
        metalSlide = 10;
        firstWireDirection = "NULL";
        runFunction = false;
    }

    public void render (ShapeRenderer renderer, Level level) {
        //make the electrical wire
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rectLine(startPosition.x, startPosition.y, (endPosition.x) - metalWidth, endPosition.y, 5);
        renderer.setColor(on ? Color.GREEN : Color.BLACK);
        renderer.rectLine(startPosition.x, startPosition.y, (endPosition.x) - metalWidth, endPosition.y, 3);
        renderer.setColor(on ? Color.GREEN : Color.DARK_GRAY);
        renderer.rect(startPosition.x + 3.5f, startPosition.y + 3.5f, 3.5f, 3.5f);
        renderer.rect((endPosition.x + 3.5f), (endPosition.y + 3.5f), 3.5f, 3.5f);
        //line that metal piece slides on
        if (metalWidth > 0) {
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect((endPosition.x - (metalWidth / 2f)) - 2f, endPosition.y - metalSlide, 4f, metalSlide);
        }
        //draw metal pieces at end of wire
        if (metalWidth > 0) {
            renderer.setColor(Color.DARK_GRAY);
            renderer.rect((endPosition.x) - metalWidth, (endPosition.y - metalSlide) - 3.5f, metalWidth, 7f);
            renderer.setColor(metalOn ? Color.GREEN : Color.BLACK);
            renderer.rect(((endPosition.x) + 1f) - metalWidth, (endPosition.y - metalSlide) + 1f - 3.5f, metalWidth - 2f, 5f);
        }
        //slide metal piece into place when it is touched
        if (level.touchPosition.x > (endPosition.x + 3.5f) - metalWidth - 5f && level.touchPosition.x < ((endPosition.x + 3.5f) - metalWidth) + metalWidth + 5f && level.touchPosition.y > ((endPosition.y - metalSlide) - 8.5f) && level.touchPosition.y < ((endPosition.y - metalSlide) - 3.5f) + 12f && level.pressDown && metalWidth > 0) {
            if (metalSlide == 10 && !metalUp) {
                metalUp = true;
            }
            if (metalSlide == 0 && metalUp) {
                metalUp = false;
            }
        }
        //move piece when on
        if (metalUp && metalSlide > 0) {
            metalSlide --;
        }
        //move piece when off
        if (!metalUp && metalSlide < 10) {
            metalSlide ++;
        }
        //turn metal on
        if (metalUp && nearbyWireOn(level)) {
            metalOn = true;
        }
        //turn metal off
        if (!metalUp || !nearbyWireOn(level)) {
            metalOn = false;
        }
        //if wire is the first wire of its group, turn it on
        if (first) {
            on = true;
        }
        //run on function if on
        if (on && !runFunction) {
            onFunction.run();
            runFunction = true;
        }
        //if one wire's end is touching another wire's start and the first wire is on, the second wire will also turn on.
        //Basically, if either the wire on the right or the wire on the left is on, this wire is on
        if (!leftWireOff(level) || !rightWireOff(level)) {
            on = true;
        }

        //find first wire position
        //find what direction the electricity is flowing in from the first wire and set a variable to it
        for (int i = 0; i < level.wires.size; i++) {
            if (level.wires.get(i).endPosition.dst(startPosition) < 7f && level.wires.get(i).first) {
                firstWireDirection = "LEFT";
            } else if (level.wires.get(i).startPosition.dst(endPosition) < 7f && level.wires.get(i).first) {
                firstWireDirection = "RIGHT";
            }
        }
        //make sure that all wires have the same value for the firstWireDirection
        for (int i = 0; i < level.wires.size; i++) {
            if ((level.wires.get(i).endPosition.dst(startPosition) < 7f || level.wires.get(i).startPosition.dst(endPosition) < 7f) && !level.wires.get(i).first && !level.wires.get(i).firstWireDirection.equals("NULL")) {
                firstWireDirection = level.wires.get(i).firstWireDirection;
            }
        }
        //turn wires off based on direction of electricity flow
        if (firstWireDirection.equals("LEFT") && leftWireOff(level) && !first) {
            on = false;
        }
        if (firstWireDirection.equals("RIGHT") && rightWireOff(level) && !first) {
            on = false;
        }

    }
    //check if the wire to the left of this wire is on or off
    private boolean leftWireOff (Level level) {
        boolean leftOff = true;
        for (int i = 0; i < level.wires.size; i++) {
            if (i != level.wires.indexOf(this, true)) {
                if (level.wires.get(i).endPosition.dst(startPosition) < 7f && (!level.wires.get(i).on || (level.wires.get(i).metalWidth > 0 && !level.wires.get(i).metalOn))) {
                    leftOff = true;
                }
                if (level.wires.get(i).endPosition.dst(startPosition) < 7f && (level.wires.get(i).on) && (level.wires.get(i).metalWidth == 0 || level.wires.get(i).metalOn)) {
                    leftOff = false;
                }
            }
        }
        return leftOff;
    }
    //check if the wire to the right of this wire is on or off
    private boolean rightWireOff (Level level) {
        boolean rightOff = true;
        for (int i = 0; i < level.wires.size; i++) {
            if (i != level.wires.indexOf(this, true)) {
                if (level.wires.get(i).startPosition.dst(endPosition) < 7f && (!level.wires.get(i).on || (metalWidth > 0 && !metalOn))) {
                    rightOff = true;
                }
                if (level.wires.get(i).startPosition.dst(endPosition) < 7f && (level.wires.get(i).on) && (metalWidth == 0 || metalOn)) {
                    rightOff = false;
                }
            }
        }
        return rightOff;
    }

    private boolean nearbyWireOn (Level level) {
        boolean nearbyOn = false;
        for (int i = 0; i < level.wires.size; i++) {
            //see if this wire is currently near a wire that is on
            if (i != level.wires.indexOf(this, true)) {
                if ((level.wires.get(i).endPosition.dst(startPosition) < 7f && level.wires.get(i).on && (level.wires.get(i).metalWidth == 0 || level.wires.get(i).metalOn)) || (level.wires.get(i).startPosition.dst(endPosition) < 7f && level.wires.get(i).on)) {
                    nearbyOn = true;
                }
            }
            //if metal is off turn nearby wire off
            if (level.wires.get(i).endPosition.dst(startPosition) < 7f && (level.wires.get(i).metalWidth > 0 && !level.wires.get(i).metalOn)) {
                nearbyOn = false;
                on = false;
            }
        }
        return nearbyOn;
    }

}
