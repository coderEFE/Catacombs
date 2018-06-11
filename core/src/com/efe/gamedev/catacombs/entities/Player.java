package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/13/2017.
 * This is the Player for the Catacombs game.
 * The Player can Walk, Look around, Pick up items, Jump, Unlock or Lock Doors, ect...
 * The Player is probably the most complex code in the game.
 */

public class Player {
    //TODO: when game is done, remove all these logging TAGs from the game.
    public static final String TAG = Player.class.getName();

    //player position
    public Vector2 position;
    private Vector2 velocity;

    //get level reference
    private Level level;

    //eye positions
    private Vector2 eyeLookLeft;
    private Vector2 eyeLookRight;

    //mouth offset
    private Vector2 mouthOffset;
    //mouth frames
    public Enums.MouthState mouthState;
    private float mouthFrameTimer;
    //save touch position
    public Vector2 viewportPosition;
    //player jumpState
    public Enums.JumpState jumpState;
    private float spawnOpacity;
    public float spawnTimer;

    //rotate legs
    public long startTime;
    public float legRotate;
    private static final float LEG_MOVEMENT_DISTANCE = 20;
    private static final float LEG_PERIOD = 1.5f;
    public boolean moving;

    //The item that the player holds
    public Item heldItem;
    public Enums.Facing facing;
    public boolean hasWeapon;
    public boolean hasGold;

    //rotate arms
    public float armRotate;
    public boolean fighting;
    public boolean danger;
    public float health;
    public boolean alert;
    public boolean duck;
    public boolean useEnergy;
    public float energy;
    public boolean recoverE;

    public Player (Vector2 position, Vector2 viewportPosition, Level level) {
        //initialize parameters
        this.position = position;
        //this.position.set(position);
        this.viewportPosition = viewportPosition;
        this.level = level;
        //get velocity and start player off as falling
        velocity = new Vector2();
        jumpState = Enums.JumpState.FALLING;
        //initialize eyes
        eyeLookLeft = new Vector2(-Constants.EYE_OFFSET,Constants.EYE_OFFSET);
        eyeLookRight = new Vector2(Constants.EYE_OFFSET, Constants.EYE_OFFSET);
        //initialize mouth
        mouthOffset = new Vector2();
        mouthState = Enums.MouthState.NORMAL;
        mouthFrameTimer = 0;
        legRotate = 170;
        startTime = TimeUtils.nanoTime();
        heldItem = new Item(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f), position.y - (Constants.PLAYER_HEIGHT / 1.05f)), viewportPosition, "");
        facing = Enums.Facing.LEFT;
        armRotate = 0;
        spawnOpacity = 1;
        spawnTimer = 0;
        moving = false;
        hasWeapon = false;
        hasGold = false;
        fighting = false;
        danger = false;
        health = 20;
        alert = false;
        duck = false;
        useEnergy = true;
        energy = 20;
        recoverE = false;
    }

    public void update (float delta) {
        //constantly find nearest catacomb to player and set that to currentCatacomb in level
        findNearestCatacomb();

        //add velocity and gravity. Add less gravity when going into the next catacomb
        if (insideCatacomb()) {
            velocity.y -= Constants.GRAVITY;
        } else {
            velocity.y -= Constants.GRAVITY / 4f;
        }
        position.mulAdd(velocity, delta);

        //stop player from falling below current catacomb if bottom is locked
        if (position.y < level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).wallThickness + 61 && (!level.catacombs.get(level.currentCatacomb).getLockedDoors().get(5).equals("Unlocked") && level.catacombs.get(level.currentCatacomb).bottomsOffset.x > -60) && insideCatacomb()) {
            velocity.y = 0;
            velocity.x = 0;
            position.y = level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).wallThickness + 61;
            jumpState = Enums.JumpState.GROUNDED;
        }

        //move player's eyes and mouth
        lookAround(delta, 5);
        if (spawnTimer >= 110) {
            //move player when player touches screen
            if (!viewportPosition.equals(new Vector2()) && (insideCatacomb() || outsideCatacombLeft() || outsideCatacombRight()) && (jumpState != Enums.JumpState.GROUNDED || viewportPosition.y > level.inventory.position.y)) {
                movePlayer(delta, 60);
            } else {
                resetLegs();
            }
            //if player is outside of current catacomb on left side, make player assigned to that catacomb on left side.
            if (outsideCatacombLeft() && jumpState != Enums.JumpState.JUMPING) {
                viewportPosition.x = level.catacombs.get(level.currentCatacomb).position.x + 1;
                jumpState = Enums.JumpState.FALLING;
            }
            //if player is outside of current catacomb on right side, make player assigned to that catacomb on right side.
            if (outsideCatacombRight() && jumpState != Enums.JumpState.JUMPING) {
                viewportPosition.x = level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width;
                jumpState = Enums.JumpState.FALLING;
            }

            //rotate player arms when jumping or falling
            Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
            if ((position.x < currentCatacomb.position.x + 50 || position.x > currentCatacomb.position.x + currentCatacomb.width - 50) && jumpState != Enums.JumpState.GROUNDED) {
                if (armRotate < 50) {
                    armRotate += 4f;
                }
                if (mouthState == Enums.MouthState.NORMAL) {
                    mouthState = Enums.MouthState.OPEN;
                }
            } else if (!fighting) {
                if (armRotate > 0) {
                    armRotate -= 2f;
                }
                if (mouthState == Enums.MouthState.OPEN) {
                    mouthState = Enums.MouthState.NORMAL;
                }
            }

            //try to collect items
            collectItem();
            //set direction
            if (level.touchPosition.x < position.x) {
                facing = Enums.Facing.LEFT;
            }
            if (level.touchPosition.x > position.x) {
                facing = Enums.Facing.RIGHT;
            }
            //unlock high walls
            //middleRight
            if (position.x >= currentCatacomb.position.x + currentCatacomb.width - 55 && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleRight.x + currentCatacomb.middleRightOffset.x - 5.5f, currentCatacomb.middleRight.y + currentCatacomb.middleRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && !currentCatacomb.getLockedDoors().get(3).equals("Closed")) {
                if (currentCatacomb.getLockedDoors().get(3).equals("Locked")) {
                    currentCatacomb.getLockedDoors().set(3, "Unlocked");
                } else if (currentCatacomb.getLockedDoors().get(3).equals("DoubleLocked")) {
                    currentCatacomb.getLockedDoors().set(3, "Locked");
                }
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            } else if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleRight.x + currentCatacomb.middleRightOffset.x - 5.5f, currentCatacomb.middleRight.y + currentCatacomb.middleRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && currentCatacomb.getLockedDoors().get(3).equals("Unlocked")) {
                currentCatacomb.getLockedDoors().set(3, "Locked");
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            }
            //middleLeft
            if (position.x <= currentCatacomb.position.x + 55 && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleLeft.x + currentCatacomb.middleLeftOffset.x + 5.5f, currentCatacomb.middleLeft.y + currentCatacomb.middleLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && !currentCatacomb.getLockedDoors().get(1).equals("Closed")) {
                if (currentCatacomb.getLockedDoors().get(1).equals("Locked")) {
                    currentCatacomb.getLockedDoors().set(1, "Unlocked");
                } else if (currentCatacomb.getLockedDoors().get(1).equals("DoubleLocked")) {
                    currentCatacomb.getLockedDoors().set(1, "Locked");
                }
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            } else if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleLeft.x + currentCatacomb.middleLeftOffset.x + 5.5f, currentCatacomb.middleLeft.y + currentCatacomb.middleLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && currentCatacomb.getLockedDoors().get(1).equals("Unlocked")) {
                currentCatacomb.getLockedDoors().set(1, "Locked");
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            }
        }
        //find nearest catacomb's doors and set them to currentCatacomb doors
        setNearCatacombDoors();
    }

    //find catacomb nearest to player and assign player to it
    private void findNearestCatacomb () {
        for (Catacomb catacomb : level.catacombs) {
            if (position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) + 50)) < catacomb.width / 2 && position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) - 50)) < catacomb.width / 2) {
                level.currentCatacomb = level.catacombs.indexOf(catacomb,true);
            }
        }
    }

    //try to collect items
    private void collectItem () {
        //make player bounds
        Rectangle playerBounds = new Rectangle(position.x - Constants.HEAD_SIZE,
                position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                Constants.PLAYER_WIDTH * 2f,
                Constants.PLAYER_HEIGHT * 2.5f);
        //loop through all items
        for (Item item : level.items) {
            //make item bounds
            Rectangle itemBounds = new Rectangle(item.position.x - (item.itemWidth / 5f),
                    item.position.y + 4 - item.itemHeight,
                    item.itemWidth * 1.4f,
                    item.itemHeight);
            //if player bounds touches item bounds, remove item.
            if (itemBounds.overlaps(playerBounds)) {
                if (item.itemType.equals("dagger")) {
                    hasWeapon = true;
                }
                if (item.itemType.equals("gold")) {
                    hasGold = true;
                }
                item.collected = true;
                //TODO: do this with chest too
                //if item has not been collected before, pop up a screen telling what item is.
                if (!level.collectedItems[level.collectedItemTypes.indexOf(item.itemType, true)]) {
                    level.collectedItems[level.collectedItemTypes.indexOf(item.itemType, true)] = true;
                    level.touchPosition = new Vector2();
                    level.inventory.newItemType = item.itemType;
                    level.inventory.newItem = true;
                }
            }
        }
    }

    public void holdItem (String itemType) {
        //set initials
        heldItem.collected = true;
        heldItem.itemType = itemType;
        //set directions and render item accordingly
        if (facing == Enums.Facing.LEFT) {
            heldItem.itemFacing = Enums.Facing.LEFT;
            heldItem.position.set(new Vector2(heldItem.itemType.equals("dagger") ? (position.x - (Constants.PLAYER_WIDTH * 1.0f) - heldItem.itemWidth + (velocity.x / 40)) : (position.x - (Constants.PLAYER_WIDTH * 1.2f) - heldItem.itemWidth + (velocity.x / 40)), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 1 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60))));
        } else if (facing == Enums.Facing.RIGHT) {
            heldItem.itemFacing = Enums.Facing.RIGHT;
            heldItem.position.set(new Vector2(heldItem.itemType.equals("dagger") ? (position.x + (Constants.PLAYER_WIDTH * 0.8f) + (velocity.x / 40)) : (position.x + (Constants.PLAYER_WIDTH * 1.2f) + (velocity.x / 40)), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 1 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60))));
        }
    }

    //when player spawns, its opacity flashes a few times
    public void spawn (){
        if (spawnTimer < 120) {
            spawnTimer++;
        }
        if (spawnTimer >= 0 && spawnTimer < 10) {
            spawnOpacity = 1;
        } else if (spawnTimer >= 10 && spawnTimer < 30) {
            spawnOpacity = 0;
        } else if (spawnTimer >= 30 && spawnTimer < 50) {
            spawnOpacity = 1;
        } else if (spawnTimer >= 50 && spawnTimer < 70) {
            spawnOpacity = 0;
        } else if (spawnTimer >= 70 && spawnTimer < 90) {
            spawnOpacity = 1;
        } else if (spawnTimer >= 90 && spawnTimer < 110) {
            spawnOpacity = 0;
        } else if (spawnTimer >= 110) {
            spawnOpacity = 1;
        }
    }

    public void tryJumping () {
        //try jumping right if the player tapped in the upper right corner of the screen
        if (level.touchPosition.y > level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).height / 2 && level.touchPosition.x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width + 10 && jumpState == Enums.JumpState.GROUNDED && level.catacombs.get(level.currentCatacomb).getLockedDoors().get(3).equals("Unlocked") && !level.catacombs.get(level.currentCatacomb).getLockedDoors().get(4).equals("Unlocked")) {
            if (position.x > level.catacombs.get(level.currentCatacomb).position.x + (level.catacombs.get(level.currentCatacomb).width - 57.14f)) {
                velocity = new Vector2(20, 190);
                jumpState = Enums.JumpState.JUMPING;
            } else {
                viewportPosition.x = level.catacombs.get(level.currentCatacomb).position.x + (level.catacombs.get(level.currentCatacomb).width - 57.14f);
            }//try jumping left if the player tapped in the upper left corner of the screen
        } else if (level.touchPosition.y > level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).height / 2 && level.touchPosition.x < level.catacombs.get(level.currentCatacomb).position.x - 10 && jumpState == Enums.JumpState.GROUNDED && level.catacombs.get(level.currentCatacomb).getLockedDoors().get(1).equals("Unlocked")  && !level.catacombs.get(level.currentCatacomb).getLockedDoors().get(0).equals("Unlocked")) {
            if (position.x < level.catacombs.get(level.currentCatacomb).position.x + (200 / 3.5f)) {
                velocity = new Vector2(-20, 190);
                jumpState = Enums.JumpState.JUMPING;
            } else {
                viewportPosition.x = level.catacombs.get(level.currentCatacomb).position.x + (200 / 3.5f) - 1;
            }
        }
    }

    private boolean insideCatacomb () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        //check if position.x is more than catacomb's left side
        boolean inside = position.x > currentCatacomb.position.x + 55 && position.x < currentCatacomb.position.x + currentCatacomb.width - 55;
        //return results to determine if player is within catacomb's bounds
        return inside;
    }

    private boolean outsideCatacombLeft () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        //check if position.x is more than catacomb's left side
        boolean outsideX = position.x <= currentCatacomb.position.x + 55;
        if (outsideX && currentCatacomb.getLockedDoors().get(0).equals("Closed") && jumpState != Enums.JumpState.JUMPING) {
            viewportPosition.x = currentCatacomb.position.x + 55.5f;
            resetLegs();
        } else if (outsideX && (currentCatacomb.getLockedDoors().get(0).equals("Locked") || currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked")) && jumpState != Enums.JumpState.JUMPING) {
            if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomLeft.x + currentCatacomb.bottomLeftOffset.x - 5.5f, currentCatacomb.bottomLeft.y + currentCatacomb.bottomLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
                if (currentCatacomb.getLockedDoors().get(0).equals("Locked")) {
                    currentCatacomb.getLockedDoors().set(0, "Unlocked");
                } else if (currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked")) {
                    currentCatacomb.getLockedDoors().set(0, "Locked");
                }
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            } else {
                viewportPosition.x = currentCatacomb.position.x + 55.5f;
                resetLegs();
            }
            //lock door
        } else if (currentCatacomb.getLockedDoors().get(0).equals("Unlocked") && jumpState != Enums.JumpState.JUMPING && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomLeft.x + currentCatacomb.bottomLeftOffset.x - 6, currentCatacomb.bottomLeft.y + currentCatacomb.bottomLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
            currentCatacomb.getLockedDoors().set(0, "Locked");
            level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
            level.inventory.selectedItem = -1;
        }
            //return results to determine if player is outside catacomb's bounds
        return outsideX;
    }
    //setNearestCatacombDoors is a function that unlocks and locks doors if the door on currentCatacomb is unlocked or locked
    private void setNearCatacombDoors () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        for (Catacomb catacomb : level.catacombs) {
        if ((new Vector2((catacomb.position.x + catacomb.width / 2), (catacomb.position.y + catacomb.width / 2))).dst(new Vector2((currentCatacomb.position.x + currentCatacomb.width / 2) - 150, (currentCatacomb.position.y + currentCatacomb.width / 2) - 85)) < currentCatacomb.width / 2) {
            catacomb.getLockedDoors().set(3, currentCatacomb.getLockedDoors().get(0));
        }
        if ((new Vector2((catacomb.position.x + catacomb.width / 2), (catacomb.position.y + catacomb.width / 2))).dst(new Vector2((currentCatacomb.position.x + currentCatacomb.width / 2) - 150, (currentCatacomb.position.y + currentCatacomb.width / 2) + 85)) < currentCatacomb.width / 2) {
            catacomb.getLockedDoors().set(4, currentCatacomb.getLockedDoors().get(1));
        }
        if ((new Vector2((catacomb.position.x + catacomb.width / 2), (catacomb.position.y + catacomb.width / 2))).dst(new Vector2((currentCatacomb.position.x + currentCatacomb.width / 2) + 150, (currentCatacomb.position.y + currentCatacomb.width / 2) + 85)) < currentCatacomb.width / 2) {
            catacomb.getLockedDoors().set(0, currentCatacomb.getLockedDoors().get(3));
        }
        if ((new Vector2((catacomb.position.x + catacomb.width / 2), (catacomb.position.y + catacomb.width / 2))).dst(new Vector2((currentCatacomb.position.x + currentCatacomb.width / 2) + 150, (currentCatacomb.position.y + currentCatacomb.width / 2) - 85)) < currentCatacomb.width / 2) {
            catacomb.getLockedDoors().set(1, currentCatacomb.getLockedDoors().get(4));
        }
    }
}

    private boolean outsideCatacombRight () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        //check if position.x is less than catacomb's right side
        boolean outsideWidth = position.x >= currentCatacomb.position.x + currentCatacomb.width - 55;
        if (outsideWidth && currentCatacomb.getLockedDoors().get(4).equals("Closed") && jumpState != Enums.JumpState.JUMPING) {
            viewportPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
            resetLegs();
        } else if (outsideWidth && (currentCatacomb.getLockedDoors().get(4).equals("Locked") || currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked")) && jumpState != Enums.JumpState.JUMPING) {
            if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomRight.x + currentCatacomb.bottomRightOffset.x + 5.5f, currentCatacomb.bottomRight.y + currentCatacomb.bottomRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
                if (currentCatacomb.getLockedDoors().get(4).equals("Locked")) {
                    currentCatacomb.getLockedDoors().set(4, "Unlocked");
                } else if (currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked")) {
                    currentCatacomb.getLockedDoors().set(4, "Locked");
                }
                level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
                level.inventory.selectedItem = -1;
            } else {
                viewportPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
                resetLegs();
            }
            //lock door
        } else if (currentCatacomb.getLockedDoors().get(4).equals("Unlocked") && jumpState != Enums.JumpState.JUMPING && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomRight.x + currentCatacomb.bottomRightOffset.x + 5.5f, currentCatacomb.bottomRight.y + currentCatacomb.bottomRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
            currentCatacomb.getLockedDoors().set(4, "Locked");
            level.inventory.inventoryItems.removeIndex(level.inventory.selectedItem);
            level.inventory.selectedItem = -1;
        }
        //return results to determine if player is outside catacomb's bounds
        return outsideWidth;
    }

    private void movePlayer (float delta, float moveSpeed) {
        //move player if player is not where you touched
        if (!(position.x < viewportPosition.x + 1) || !(position.x > viewportPosition.x)) {
            //go left
            if (position.x > viewportPosition.x) {
                position.x -= delta * moveSpeed;
                rotateLegs();
            } else if (position.x < viewportPosition.x) {
                //go right
                position.x += delta * moveSpeed;
                rotateLegs();
            }
        } else {
            //otherwise, reset
            resetLegs();
        }
    }

    public void resetLegs () {
        //if player is on ground and resetLegs() is called, this will reset player's legs.
        if (jumpState == Enums.JumpState.GROUNDED) {
            //reset legs back to original stance
            if (legRotate > 170) {
                legRotate -= 2;
            }
            if (legRotate < 170) {
                legRotate += 2;
            }
            //reset the leg rotation startTime
            startTime = TimeUtils.nanoTime();
            moving = false;
        }
    }

    private void rotateLegs () {
        //Figure out how long it's been since the animation started using TimeUtils.nanoTime()
        long elapsedNanos = TimeUtils.nanoTime() - startTime;
        //Use MathUtils.nanoToSec to figure out how many seconds the animation has been running
        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanos;
        //Figure out how many cycles have elapsed since the animation started running
        float cycles = elapsedSeconds / LEG_PERIOD;
        //Figure out where in the cycle we are
        float cyclePosition = cycles % 1;
        //move legs in a reciprocating motion
        legRotate = 180 + LEG_MOVEMENT_DISTANCE * MathUtils.sin(MathUtils.PI2 * cyclePosition);
        //set moving to true
        moving = true;
    }

    private void lookAround (float delta, float moveSpeed) {

        //set mouth moving speed
        float mouthSpeed = (moveSpeed / 2);

        //if viewport is touched
        if (!level.lookPosition.equals(new Vector2())) {
            //move eye x
            if (level.lookPosition.x + 0.5f > position.x && eyeLookLeft.x < 0) {
                eyeLookLeft.x += delta * moveSpeed;
                eyeLookRight.x += delta * moveSpeed;
                //move mouth
                mouthOffset.x += delta * mouthSpeed;
            } else if (level.lookPosition.x < position.x && eyeLookLeft.x > 0 - (Constants.HEAD_SIZE / 2)) {
                eyeLookLeft.x -= delta * moveSpeed;
                eyeLookRight.x -= delta * moveSpeed;
                //move mouth
                mouthOffset.x -= delta * mouthSpeed;
            }
            //move eye y
            if (level.lookPosition.y > position.y && eyeLookLeft.y < 0 + (Constants.HEAD_SIZE / 3)) {
                eyeLookLeft.y += delta * moveSpeed;
                eyeLookRight.y += delta * moveSpeed;
                //move mouth
                mouthOffset.y += delta * mouthSpeed;
            } else if (level.lookPosition.y < position.y && eyeLookLeft.y > 0) {
                eyeLookLeft.y -= delta * moveSpeed;
                eyeLookRight.y -= delta * moveSpeed;
                //move mouth
                mouthOffset.y -= delta * mouthSpeed;
            }
        }
    }

    private void talk (ShapeRenderer renderer, float talkSpeed, Vector2 duckOffset) {
        //speed
        mouthFrameTimer += talkSpeed;

        //talk frames to make mouth move
        if (mouthFrameTimer >= 0 && mouthFrameTimer < 50) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 5, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 50 && mouthFrameTimer < 100) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 100 && mouthFrameTimer < 150) {
            renderer.arc(position.x + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, 180, 180);
        } else if (mouthFrameTimer >= 150 && mouthFrameTimer < 200) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, 170, 170);
        } else if (mouthFrameTimer >= 200 && mouthFrameTimer < 250) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 250 && mouthFrameTimer < 300) {
            renderer.circle(position.x + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 300 && mouthFrameTimer < 350) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 350 && mouthFrameTimer < 400) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 400) {
            mouthFrameTimer = 0;
        }
    }

    public void render (ShapeRenderer renderer) {

        //ShapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (!duck) {
            //ARMS
            //arms
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, position.y - Constants.HEAD_SIZE, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate);
            renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate);

            //hands
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
            renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

            //LEGS
            //legs
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, legRotate);
            renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, -legRotate);

            //BODY
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.3f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
            //belt
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.2f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT / 10);

            //HEAD
            //general shape and outline
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            //main head
            renderer.circle(position.x, position.y, Constants.HEAD_SIZE, Constants.HEAD_SEGMENTS);
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x, position.y, Constants.HEAD_SIZE * 15 / 16, Constants.HEAD_SEGMENTS);
            //if alert, show exclamation mark
            if (alert) {
                renderer.setColor(Color.WHITE);
                renderer.circle(position.x, position.y + Constants.HEAD_SIZE * 1.5f, Constants.HEAD_SIZE / 4, 10);
                renderer.rectLine(position.x, position.y + (Constants.HEAD_SIZE * 2), position.x, position.y + Constants.HEAD_SIZE * 3, Constants.HEAD_SIZE / 6);
            }

            //eyes
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            renderer.circle(eyeLookLeft.x + position.x, eyeLookLeft.y + position.y, Constants.HEAD_SIZE / 10, 4);
            renderer.circle(eyeLookRight.x + position.x, eyeLookRight.y + position.y, Constants.HEAD_SIZE / 10, 4);
            //mouth
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            //mouth states
            if (mouthState == Enums.MouthState.NORMAL) {
                renderer.rectLine(position.x - (Constants.HEAD_SIZE / 4f) + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, position.x + (Constants.HEAD_SIZE / 4) + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.MOUTH_THICKNESS);
            } else if (mouthState == Enums.MouthState.OPEN) {
                renderer.circle(position.x + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
            } else if (mouthState == Enums.MouthState.TALKING) {
                talk(renderer, Constants.MOUTH_TALKING_SPEED, new Vector2());
            }
            //health bar
            if (danger) {
                renderer.setColor(Color.WHITE);
                renderer.rectLine(position.x - 10, position.y + Constants.HEAD_SIZE * 1.5f, position.x + 10, position.y + Constants.HEAD_SIZE * 1.5f, 4);
                renderer.setColor(Color.BLUE);
                renderer.rectLine(position.x - 10, position.y + Constants.HEAD_SIZE * 1.5f, (position.x - 10) + health, position.y + Constants.HEAD_SIZE * 1.5f, 4);
                renderer.setColor(Color.RED);
                renderer.rectLine((position.x - 10) + health, position.y + Constants.HEAD_SIZE * 1.5f, position.x + 10, position.y + Constants.HEAD_SIZE * 1.5f, 4);
            }
            //energy bar
            float energyY = 0;
            if (useEnergy) {
                //adjust energy bar y position
                if (danger) {
                    energyY = 6;
                } else {
                    energyY = 0;
                }
                renderer.setColor(Color.WHITE);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, 4);
                renderer.setColor(Color.GREEN);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, (position.x - 10) + energy, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, 4);
                renderer.setColor(Color.PURPLE);
                renderer.rectLine((position.x - 10) + energy, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY, 4);
            }

            //Draw cx-7 (the robot voice with the Player, it looks like a head microphone)
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            renderer.ellipse(position.x - Constants.HEAD_SIZE, position.y - (Constants.HEAD_SIZE / 4f), Constants.HEAD_SIZE / 6f, Constants.HEAD_SIZE / 1.5f);
            renderer.rectLine(position.x - Constants.HEAD_SIZE, position.y - (Constants.HEAD_SIZE / 4f), position.x - (Constants.HEAD_SIZE / 2f), position.y - (Constants.HEAD_SIZE / 2f), 1);
        } else {
            Vector2 duckOffset = new Vector2(0, -24);
            armRotate = 25;
            //ARMS
            //arms
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, (position.y - Constants.HEAD_SIZE) + duckOffset.y, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate);
            renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), (position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f)) + duckOffset.y, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate);

            //hands
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)) + duckOffset.y, Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
            renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)) + duckOffset.y, Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

            //LEGS
            //legs
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, (position.y - (Constants.PLAYER_HEIGHT * 1f)) + duckOffset.y + 1, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 0.6f, 1, 1, 100);
            renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), (position.y - (Constants.PLAYER_HEIGHT * 1f)) + duckOffset.y + 4, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 0.6f, 1, 1, -100);
            renderer.rectLine(position.x - (Constants.PLAYER_WIDTH * 2), (position.y - (Constants.PLAYER_HEIGHT * 2.08f)), position.x, (position.y - (Constants.PLAYER_HEIGHT * 2.1f)), Constants.PLAYER_WIDTH / 2);
            renderer.rectLine(position.x + (Constants.PLAYER_WIDTH * 2), (position.y - (Constants.PLAYER_HEIGHT * 2.08f)), position.x + 1, (position.y - (Constants.PLAYER_HEIGHT * 2.1f)), Constants.PLAYER_WIDTH / 2);

            //1.2f
            //BODY
            renderer.setColor(new Color(Constants.CLOTHES_COLOR.r, Constants.CLOTHES_COLOR.g, Constants.CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x - (Constants.HEAD_SIZE / 2), (position.y - (Constants.PLAYER_HEIGHT * 1.3f)) + duckOffset.y + 7, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT - 7);
            //belt
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            renderer.rect(position.x - (Constants.HEAD_SIZE / 2), (position.y - (Constants.PLAYER_HEIGHT * 1.2f))  + (duckOffset.y + 9), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT / 10);

            //HEAD
            //general shape and outline
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            //main head
            renderer.circle(position.x, position.y + duckOffset.y, Constants.HEAD_SIZE, Constants.HEAD_SEGMENTS);
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x, position.y + duckOffset.y, Constants.HEAD_SIZE * 15 / 16, Constants.HEAD_SEGMENTS);
            //if alert, show exclamation mark
            if (alert) {
                renderer.setColor(Color.WHITE);
                renderer.circle(position.x, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, Constants.HEAD_SIZE / 4, 10);
                renderer.rectLine(position.x, (position.y + (Constants.HEAD_SIZE * 2)) + duckOffset.y, position.x, (position.y + Constants.HEAD_SIZE * 3) + duckOffset.y, Constants.HEAD_SIZE / 6);
            }

            //eyes
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            renderer.circle(eyeLookLeft.x + position.x, (eyeLookLeft.y + position.y) + duckOffset.y, Constants.HEAD_SIZE / 10, 4);
            renderer.circle(eyeLookRight.x + position.x, (eyeLookRight.y + position.y) + duckOffset.y, Constants.HEAD_SIZE / 10, 4);
            //mouth
            renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
            //mouth states
            if (mouthState == Enums.MouthState.NORMAL) {
                renderer.rectLine(position.x - (Constants.HEAD_SIZE / 4f) + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, position.x + (Constants.HEAD_SIZE / 4) + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.MOUTH_THICKNESS);
            } else if (mouthState == Enums.MouthState.OPEN) {
                renderer.circle(position.x + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) + duckOffset.y, Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
            } else if (mouthState == Enums.MouthState.TALKING) {
                talk(renderer, Constants.MOUTH_TALKING_SPEED, duckOffset);
            }
            //health bar
            if (danger) {
                renderer.setColor(Color.WHITE);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, 4);
                renderer.setColor(Color.BLUE);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, (position.x - 10) + health, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, 4);
                renderer.setColor(Color.RED);
                renderer.rectLine((position.x - 10) + health, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + duckOffset.y, 4);
            }
            //energy bar
            float energyY = 0;
            if (useEnergy) {
                //adjust energy bar y position
                if (danger) {
                    energyY = 6;
                } else {
                    energyY = 0;
                }
                renderer.setColor(Color.WHITE);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, 4);
                renderer.setColor(Color.GREEN);
                renderer.rectLine(position.x - 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, (position.x - 10) + energy, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, 4);
                renderer.setColor(Color.PURPLE);
                renderer.rectLine((position.x - 10) + energy, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, position.x + 10, (position.y + Constants.HEAD_SIZE * 1.5f) + energyY + duckOffset.y, 4);
            }

            //Draw cx-7 (the robot voice with the Player, it looks like a head microphone)
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            renderer.ellipse(position.x - Constants.HEAD_SIZE, (position.y - (Constants.HEAD_SIZE / 4f)) + duckOffset.y, Constants.HEAD_SIZE / 6f, Constants.HEAD_SIZE / 1.5f);
            renderer.rectLine(position.x - Constants.HEAD_SIZE, (position.y - (Constants.HEAD_SIZE / 4f)) + duckOffset.y, position.x - (Constants.HEAD_SIZE / 2f), (position.y - (Constants.HEAD_SIZE / 2f)) + duckOffset.y, 1);

        }
        //ducking and energy balance
        if (useEnergy) {
            if (duck) {
                energy -= 0.1f;
            }
            if (energy <= 0) {
                duck = false;
                recoverE = true;
            }
            if ((!duck || recoverE) && energy < 20) {
                energy += 0.2f;
            }
            if (energy > 4) {
                recoverE = false;
            }
        }
        //Draw Item player is holding
        if (jumpState == Enums.JumpState.GROUNDED && !heldItem.itemType.equals("") && !duck) {
            heldItem.render(renderer, level);
        }
    }



    //return player position if needed
    public Vector2 getPosition () {
        return position;
    }
    //return player velocity if needed
    public Vector2 getVelocity () {
        return velocity;
    }
    //set player velocity if needed
    public void setVelocity (Vector2 newVelocity) { velocity.set(newVelocity); }
}
