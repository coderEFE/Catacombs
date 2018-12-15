package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 7/3/2018.
 * This is NOT the Exit Door.
 */

public class Door {

    public Vector2 position;
    public int exitDoorIndex;
    private final Level level;
    //door variables
    public boolean enteredDoor;
    private float doorWidth;
    private float doorHeight;
    public boolean unlocked;
    private float unlockFade;
    private Button yesButton;
    private Button noButton;
    public boolean show;
    public boolean usingDoor;

    public Door (Vector2 position, final int exitDoorIndex, final Level level) {
        this.position = position;
        this.exitDoorIndex = exitDoorIndex;
        this.level = level;
        enteredDoor = false;
        doorWidth = 30;
        doorHeight = 60;
        unlocked = false;
        unlockFade = 1f;
        show = true;
        usingDoor = false;
        yesButton = new Button(new Vector2((position.x - (doorWidth)) + 10, position.y + doorHeight + 15), "Yes", 20, 20, Color.BROWN,
                new Runnable () {public void run() {
                    //remove key from player if door is not unlocked yet
                    if (!unlocked) {
                        level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                        level.inventory.selectedItem = -1;
                        unlocked = true;
                        level.gameplayScreen.sound6.play();
                    }
                    usingDoor = true;
                    level.touchPosition = new Vector2();
                }},
                level);
        noButton = new Button(new Vector2(position.x + (doorWidth), position.y + doorHeight + 15), "No", 20, 20, Color.BROWN,
                new Runnable () {public void run() {
                    level.touchPosition = new Vector2();
                }},
                level);
    }

    public void render (ShapeRenderer renderer) {
        //draw door if show is true
        if (show) {
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(new Color(Color.BROWN.r * 0.8f, Color.BROWN.g * 0.8f, Color.BROWN.b * 0.8f, 1));
            renderer.arc(position.x + doorWidth / 2, position.y + doorHeight, doorWidth / 2, 0, 180, 20);
            renderer.rect(position.x, position.y, doorWidth, doorHeight);
            renderer.setColor(new Color(Color.BROWN.r * (unlockFade), Color.BROWN.g * (unlockFade), Color.BROWN.b * (unlockFade), 1f));
            renderer.arc(position.x + doorWidth / 2, position.y + doorHeight, (doorWidth / 2) - 2, 0, 180, 20);
            renderer.rect(position.x + 2, position.y + 2, doorWidth - 4, doorHeight - 2);
            if (!unlocked) {
                //door knob
                renderer.setColor(Color.GRAY);
                renderer.circle(position.x + (doorWidth - 7), position.y + (doorHeight / 2), 4, 6);
                //key hole
                renderer.setColor(Color.DARK_GRAY);
                renderer.circle(position.x + (doorWidth - 7), position.y + (doorHeight / 2) + 1, 1, 6);
                renderer.rectLine(position.x + (doorWidth - 7), position.y + (doorHeight / 2), position.x + (doorWidth - 7), position.y + (doorHeight / 2) - 1.5f, 0.5f);
            } else {
                //fade inner door
                if (unlockFade > 0.6f) {
                    unlockFade -= 0.01f;
                }
            }
            if (usingDoor) {
                //fade level torch
                if (level.torchLight < 59 || !level.torchUp) {
                    level.torchFade = true;
                }
                if (level.torchLight > 0f && !level.torchUp) {
                    level.touchLocked = true;
                    level.viewportPosition.set(new Vector2(position.x + (doorWidth / 2f), position.y + 48f));
                }
                //when torchlight is very low, make it become larger again and move player to the location of the exitDoorIndex.
                if (level.torchLight <= 1f) {
                    level.torchUp = true;
                    level.viewportPosition.set(new Vector2());
                    level.touchPosition.set(new Vector2());
                    level.touchLocked = false;
                    level.getPlayer().position.set(new Vector2(level.doors.get(exitDoorIndex).position.x + (doorWidth / 2f), level.doors.get(exitDoorIndex).position.y + 48f));
                    usingDoor = false;
                }
            }
            //unlock door if player is on it and has a key
            if (level.getPlayer().heldItem.itemType.equals("key") && insideBounds(level.getPlayer().getPosition()) && !unlocked && !level.touchPosition.equals(new Vector2())) {
                yesButton.render(renderer);
                noButton.render(renderer);
            }
            //if door is already unlocked with a key, player can use it.
            if (insideBounds(level.getPlayer().getPosition()) && unlocked && !level.touchPosition.equals(new Vector2())) {
                yesButton.render(renderer);
                noButton.render(renderer);
            }
        }
    }
    //A function that returns whether or not a Vector2 is inside the door's boundaries.
    private boolean insideBounds (Vector2 objectPosition) {
        return (objectPosition.x > position.x && objectPosition.x < position.x + doorWidth && objectPosition.y > position.y && objectPosition.y < position.y + doorHeight);
    }

}
