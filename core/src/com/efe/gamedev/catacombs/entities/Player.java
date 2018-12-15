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

import static java.lang.Math.random;

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
    //opacity
    private float spawnOpacity;
    public float spawnTimer;
    //special effects
    public boolean invisibility;
    public boolean ghost;
    public boolean shock;
    public Color CLOTHES_COLOR = Color.GOLDENROD;
    public boolean drinkPotion;
    public float potionTimer;
    public boolean disguise;

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
        //initialize legs
        legRotate = 170;
        startTime = TimeUtils.nanoTime();
        //initialize direction
        heldItem = new Item(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f), position.y - (Constants.PLAYER_HEIGHT / 1.05f)), viewportPosition, "");
        facing = Enums.Facing.LEFT;
        //initialize other variables
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
        invisibility = false;
        ghost = false;
        shock = false;
        drinkPotion = false;
        potionTimer = 40;
        disguise = false;
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
        if ((position.y < level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).wallThickness + 61 && (!level.catacombs.get(level.currentCatacomb).getLockedDoors().get(5).equals("Unlocked") || level.catacombs.get(level.currentCatacomb).bottomsOffset.x > (-1 * (level.catacombs.get(level.currentCatacomb).width - 150))) && insideCatacomb()) || (level.superior.currentLevel == 14 && level.currentBubble > 21 && level.currentBubble < 62)) {
            velocity.y = 0;
            velocity.x = 0;
            position.y = level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).wallThickness + 61;
            jumpState = Enums.JumpState.GROUNDED;
        }

        //move player's eyes and mouth
        //TODO: make player's facial features and body move according to delta
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
            //set direction if you are not changing items in inventory
            if (level.touchPosition.y > level.inventory.position.y || level.touchPosition.y < level.inventory.position.y - (level.viewport.getWorldHeight() / 7)) {
                if (level.touchPosition.x < position.x) {
                    facing = Enums.Facing.LEFT;
                }
                if (level.touchPosition.x > position.x) {
                    facing = Enums.Facing.RIGHT;
                }
            }
            //unlock high walls
            //if heldItem is a key, a Locked door becomes Unlocked and a DoubleLocked door becomes Locked. Also, an Unlocked door can become Locked. If heldItem is a doubleKey, then a DoubleLocked door can become Unlocked.
            //middleRight
            if (position.x >= currentCatacomb.position.x + currentCatacomb.width - 55 && (heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey") && currentCatacomb.getLockedDoors().get(3).equals("DoubleLocked"))) && level.touchPosition.dst(new Vector2(currentCatacomb.middleRight.x + currentCatacomb.middleRightOffset.x - 5.5f, currentCatacomb.middleRight.y + currentCatacomb.middleRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && !currentCatacomb.getLockedDoors().get(3).equals("Closed")) {
                //with normal key
                if (currentCatacomb.getLockedDoors().get(3).equals("Locked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(3, "Unlocked");
                    level.gameplayScreen.sound6.play();
                } else if (currentCatacomb.getLockedDoors().get(3).equals("DoubleLocked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(3, "Locked");
                    level.gameplayScreen.sound6.play();
                }
                //with doubleKey
                if (currentCatacomb.getLockedDoors().get(3).equals("DoubleLocked") && heldItem.itemType.equals("doubleKey")) {
                    currentCatacomb.getLockedDoors().set(3, "Unlocked");
                    level.gameplayScreen.sound6.play();
                }
                //take away key after it is used
                level.inventory.deleteCurrentItem();
                //lock doors
            } else if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleRight.x + currentCatacomb.middleRightOffset.x - 5.5f, currentCatacomb.middleRight.y + currentCatacomb.middleRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && currentCatacomb.getLockedDoors().get(3).equals("Unlocked")) {
                currentCatacomb.getLockedDoors().set(3, "Locked");
                level.gameplayScreen.sound6.play();
                level.inventory.deleteCurrentItem();
            }
            //middleLeft
            if (position.x <= currentCatacomb.position.x + 55 && (heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey") && currentCatacomb.getLockedDoors().get(1).equals("DoubleLocked"))) && level.touchPosition.dst(new Vector2(currentCatacomb.middleLeft.x + currentCatacomb.middleLeftOffset.x + 5.5f, currentCatacomb.middleLeft.y + currentCatacomb.middleLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && !currentCatacomb.getLockedDoors().get(1).equals("Closed")) {
                //with normal key
                if (currentCatacomb.getLockedDoors().get(1).equals("Locked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(1, "Unlocked");
                    level.gameplayScreen.sound6.play();
                } else if (currentCatacomb.getLockedDoors().get(1).equals("DoubleLocked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(1, "Locked");
                    level.gameplayScreen.sound6.play();
                }
                //with doubleKey
                if (currentCatacomb.getLockedDoors().get(1).equals("DoubleLocked") && heldItem.itemType.equals("doubleKey")) {
                    currentCatacomb.getLockedDoors().set(1, "Unlocked");
                    level.gameplayScreen.sound6.play();
                }
                //take away key after it is used
                level.inventory.deleteCurrentItem();
                //lock doors
            } else if (heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.middleLeft.x + currentCatacomb.middleLeftOffset.x + 5.5f, currentCatacomb.middleLeft.y + currentCatacomb.middleLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4) && currentCatacomb.getLockedDoors().get(1).equals("Unlocked")) {
                currentCatacomb.getLockedDoors().set(1, "Locked");
                level.gameplayScreen.sound6.play();
                level.inventory.deleteCurrentItem();
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
            //Don't allow player to pick up a bomb if it is triggered or exploding
            if (itemBounds.overlaps(playerBounds) && !item.collected && !item.bomb.triggered && !item.bomb.exploding) {
                //if player has a weapon, set hasWeapon to true
                if (item.itemType.equals("dagger") || item.itemType.equals("stungun") || item.itemType.equals("bomb")) {
                    hasWeapon = true;
                }
                //if player has gold, set hasGold to true
                if (item.itemType.equals("gold")) {
                    hasGold = true;
                }
                //collect item
                item.collected = true;
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
        //if player taps on potions, player will drink the potion.
        if (heldItem.potion.touchPotion(level.touchPosition) && heldItem.potion.full && !drinkPotion && level.pressDown) {
            //different effects for different potions
            if (heldItem.itemType.equals("invisibility") && level.superior.currentLevel > 7) {
                drinkPotion = true;
                potionTimer = 40;
                invisibility = true;
                level.inventory.inventoryItems.get(level.inventory.selectedItem).potion.full = false;
            } else if (heldItem.itemType.equals("ghost") && (level.superior.currentLevel > 8 || level.currentBubble > 2)) {
                drinkPotion = true;
                potionTimer = 40;
                ghost = true;
                invisibility = true;
                level.inventory.inventoryItems.get(level.inventory.selectedItem).potion.full = false;
            } else if (heldItem.itemType.equals("shock")) {
                drinkPotion = true;
                potionTimer = 40;
                shock = true;
                level.inventory.inventoryItems.get(level.inventory.selectedItem).potion.full = false;
            }
        }
        //if potion has not been drunk, make it full, and vise versa
        if (level.inventory.selectedItem != -1 && (heldItem.itemType.equals("invisibility") || heldItem.itemType.equals("ghost") || heldItem.itemType.equals("shock"))) {
            heldItem.potion.full = level.inventory.inventoryItems.get(level.inventory.selectedItem).potion.full;
        }
        //reset touchPosition
        /*if (level.touchPosition.dst(new Vector2(heldItem.itemType.equals("dagger") ? (position.x - (Constants.PLAYER_WIDTH * 1.0f) - heldItem.itemWidth + (velocity.x / 40)) : (position.x - (Constants.PLAYER_WIDTH * 1.2f) - heldItem.itemWidth + (velocity.x / 40)), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 1 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)))) < 8 && facing == Enums.Facing.RIGHT) {
            level.touchPosition.set(new Vector2());
        }
        if (level.touchPosition.dst(new Vector2(heldItem.itemType.equals("dagger") ? (position.x + (Constants.PLAYER_WIDTH * 0.8f) + (velocity.x / 40)) : (position.x + (Constants.PLAYER_WIDTH * 1.2f) + (velocity.x / 40)), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 1 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)))) < 8 && facing == Enums.Facing.LEFT) {
            level.touchPosition.set(new Vector2());
        }*/
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
        } else if (spawnTimer >= 110 && spawnTimer < 119) {
            spawnOpacity = 1;
        }
    }
    //allows player to jump to other catacombs
    public void tryJumping () {
        //player can jump through walls when the "ghost" Potion is drunk.
        //try jumping right if the player tapped in the upper right corner of the screen
        if (level.touchPosition.x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width - 25 && jumpState == Enums.JumpState.GROUNDED && (level.catacombs.get(level.currentCatacomb).getLockedDoors().get(3).equals("Unlocked") || (level.catacombs.get(level.currentCatacomb).getLockedDoors().get(3).equals("Locked") && ghost)) && !level.catacombs.get(level.currentCatacomb).getLockedDoors().get(4).equals("Unlocked")) {
            if (position.x > level.catacombs.get(level.currentCatacomb).position.x + (level.catacombs.get(level.currentCatacomb).width - 57.14f)) {
                level.viewportPosition.set(new Vector2(level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width + 20, (level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).height / 2f) + 25));
                velocity = new Vector2(20, 190);
                jumpState = Enums.JumpState.JUMPING;
            } else {
                viewportPosition.x = level.catacombs.get(level.currentCatacomb).position.x + (level.catacombs.get(level.currentCatacomb).width - 57.14f);
            }//try jumping left if the player tapped in the upper left corner of the screen
        } else if (level.touchPosition.x < level.catacombs.get(level.currentCatacomb).position.x + 25 && jumpState == Enums.JumpState.GROUNDED && (level.catacombs.get(level.currentCatacomb).getLockedDoors().get(1).equals("Unlocked") || (level.catacombs.get(level.currentCatacomb).getLockedDoors().get(1).equals("Locked") && ghost)) && !level.catacombs.get(level.currentCatacomb).getLockedDoors().get(0).equals("Unlocked")) {
            if (position.x < level.catacombs.get(level.currentCatacomb).position.x + (200 / 3.5f)) {
                level.viewportPosition.set(new Vector2(level.catacombs.get(level.currentCatacomb).position.x - 20, (level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).height / 2f) + 25));
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
        //Unlock door on left side
        if (outsideX && (currentCatacomb.getLockedDoors().get(0).equals("Closed") || currentCatacomb.getLockedDoors().get(5).equals("Unlocked")) && jumpState != Enums.JumpState.JUMPING) {
            viewportPosition.x = currentCatacomb.position.x + 55.5f;
            resetLegs();
        } else if (outsideX && (currentCatacomb.getLockedDoors().get(0).equals("Locked") || currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked")) && jumpState != Enums.JumpState.JUMPING) {
            if (((heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey") && currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked"))) && level.touchPosition.dst(new Vector2(currentCatacomb.bottomLeft.x + currentCatacomb.bottomLeftOffset.x - 5.5f, currentCatacomb.bottomLeft.y + currentCatacomb.bottomLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) || ghost) {
                //with normal key
                if (currentCatacomb.getLockedDoors().get(0).equals("Locked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(0, "Unlocked");
                    level.gameplayScreen.sound6.play();
                } else if (currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(0, "Locked");
                    level.gameplayScreen.sound6.play();
                }
                //with doubleKey
                if (currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked") && heldItem.itemType.equals("doubleKey")) {
                    currentCatacomb.getLockedDoors().set(0, "Unlocked");
                    level.gameplayScreen.sound6.play();
                }
                //take away key after it is used
                if ((heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey")))) {
                    level.inventory.deleteCurrentItem();
                }
            } else {
                viewportPosition.x = currentCatacomb.position.x + 55.5f;
                resetLegs();
            }
            //lock door
        } else if (currentCatacomb.getLockedDoors().get(0).equals("Unlocked") && jumpState != Enums.JumpState.JUMPING && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomLeft.x + currentCatacomb.bottomLeftOffset.x - 6, currentCatacomb.bottomLeft.y + currentCatacomb.bottomLeftOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
            currentCatacomb.getLockedDoors().set(0, "Locked");
            level.gameplayScreen.sound6.play();
            level.inventory.deleteCurrentItem();
        }
            //return results to determine if player is outside catacomb's bounds
        return outsideX;
    }
    //setNearestCatacombDoors is a function that unlocks and locks doors if the door on currentCatacomb is unlocked or locked
    private void setNearCatacombDoors () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        for (Catacomb catacomb : level.catacombs) {
            //bottom left
            if ((new Vector2((catacomb.position.x + (catacomb.width - 100)), (catacomb.position.y + catacomb.height / 2))).dst(new Vector2((currentCatacomb.position.x + 200 / 2) - 150, (currentCatacomb.position.y + currentCatacomb.height / 2) - 85)) < 200 / 2) {
                catacomb.getLockedDoors().set(3, currentCatacomb.getLockedDoors().get(0));
            }
            //top left
            if ((new Vector2((catacomb.position.x + (catacomb.width - 100)), (catacomb.position.y + catacomb.height / 2))).dst(new Vector2((currentCatacomb.position.x + 200 / 2) - 150, (currentCatacomb.position.y + currentCatacomb.height / 2) + 85)) < 200 / 2) {
                catacomb.getLockedDoors().set(4, currentCatacomb.getLockedDoors().get(1));
            }
            //top right
            if ((new Vector2((catacomb.position.x + 200 / 2), (catacomb.position.y + catacomb.height / 2))).dst(new Vector2((currentCatacomb.position.x + (currentCatacomb.width - 100)) + 150, (currentCatacomb.position.y + currentCatacomb.height / 2) + 85)) < 200 / 2) {
                catacomb.getLockedDoors().set(0, currentCatacomb.getLockedDoors().get(3));
            }
            //bottom right
            if ((new Vector2((catacomb.position.x + 200 / 2), (catacomb.position.y + catacomb.height / 2))).dst(new Vector2((currentCatacomb.position.x + (currentCatacomb.width - 100)) + 150, (currentCatacomb.position.y + currentCatacomb.height / 2) - 85)) < 200 / 2) {
                catacomb.getLockedDoors().set(1, currentCatacomb.getLockedDoors().get(4));
            }
        }
    }

    private boolean outsideCatacombRight () {
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        //check if position.x is less than catacomb's right side
        boolean outsideWidth = position.x >= currentCatacomb.position.x + currentCatacomb.width - 55;
        //Unlock door on right side
        if (outsideWidth && (currentCatacomb.getLockedDoors().get(4).equals("Closed") || currentCatacomb.getLockedDoors().get(5).equals("Unlocked")) && jumpState != Enums.JumpState.JUMPING) {
            viewportPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
            resetLegs();
        } else if (outsideWidth && (currentCatacomb.getLockedDoors().get(4).equals("Locked") || currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked")) && jumpState != Enums.JumpState.JUMPING) {
            if (((heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey") && currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked"))) && level.touchPosition.dst(new Vector2(currentCatacomb.bottomRight.x + currentCatacomb.bottomRightOffset.x + 5.5f, currentCatacomb.bottomRight.y + currentCatacomb.bottomRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) || ghost) {
                //with normal key
                if (currentCatacomb.getLockedDoors().get(4).equals("Locked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(4, "Unlocked");
                    level.gameplayScreen.sound6.play();
                } else if (currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked") && heldItem.itemType.equals("key")) {
                    currentCatacomb.getLockedDoors().set(4, "Locked");
                    level.gameplayScreen.sound6.play();
                }
                //with doubleKey
                if (currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked") && heldItem.itemType.equals("doubleKey")) {
                    currentCatacomb.getLockedDoors().set(4, "Unlocked");
                    level.gameplayScreen.sound6.play();
                }
                //take away key after it is used
                if ((heldItem.itemType.equals("key") || (heldItem.itemType.equals("doubleKey")))) {
                    level.inventory.deleteCurrentItem();
                }
            } else {
                viewportPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
                resetLegs();
            }
            //lock door
        } else if (currentCatacomb.getLockedDoors().get(4).equals("Unlocked") && jumpState != Enums.JumpState.JUMPING && heldItem.itemType.equals("key") && level.touchPosition.dst(new Vector2(currentCatacomb.bottomRight.x + currentCatacomb.bottomRightOffset.x + 5.5f, currentCatacomb.bottomRight.y + currentCatacomb.bottomRightOffset.y + 10)) < (currentCatacomb.wallThickness + 4)) {
            currentCatacomb.getLockedDoors().set(4, "Locked");
            level.gameplayScreen.sound6.play();
            level.inventory.deleteCurrentItem();
        }
        //return results to determine if player is outside catacomb's bounds
        return outsideWidth;
    }

    private void movePlayer (float delta, float moveSpeed) {
        //move player if player is not where you touched
        if (!(position.x < viewportPosition.x + 1) || !((position.x < level.catacombs.get(level.currentCatacomb).position.x + 55 || position.x > (level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width) - 65) ? (position.x > viewportPosition.x) : (position.x > viewportPosition.x - 1))) {
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
    //electricity
    private void Spark (ShapeRenderer renderer, Vector2 startPosition, Vector2 endPosition) {
        //calculate distance
        //double distance = Math.sqrt(Math.pow((endPosition.x - startPosition.x), 2) + Math.pow((endPosition.y - startPosition.y), 2));
        float xIncrement = (endPosition.x - startPosition.x) / 6;
        float yIncrement = (endPosition.y - startPosition.y) / 6;
        renderer.setColor(Color.GREEN);
        Vector2 r1 = new Vector2(MathUtils.random(startPosition.x, startPosition.x + 3), MathUtils.random(startPosition.y, startPosition.y + 3));
        Vector2 r2 = new Vector2(MathUtils.random(r1.x, r1.x + xIncrement), MathUtils.random(r1.y, r1.y + yIncrement));
        Vector2 r3 = new Vector2(MathUtils.random(r2.x, r2.x + xIncrement), MathUtils.random(r2.y, r2.y + yIncrement));
        Vector2 r4 = new Vector2(MathUtils.random(r3.x, r3.x + xIncrement), MathUtils.random(r3.y, r3.y + yIncrement));
        Vector2 r5 = new Vector2(MathUtils.random(r4.x, r4.x + xIncrement), MathUtils.random(r4.y, r4.y + yIncrement));
        Vector2 r6 = new Vector2(MathUtils.random(r5.x, r5.x + xIncrement), MathUtils.random(r5.y, r5.y + yIncrement));

        renderer.line(startPosition, r1);
        renderer.line(r1, r2);
        renderer.line(r2, r3);
        renderer.line(r3, r4);
        renderer.line(r4, r5);
        renderer.line(r5, r6);
    }

    public void render (ShapeRenderer renderer) {
        //ShapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //make player invisible
        if (invisibility) {
            if (spawnOpacity > 0.3f) {
                spawnOpacity -= 0.01f;
            }
        } else {
            if (spawnOpacity < 1f) {
                spawnOpacity += 0.01f;
            }
        }
        //make player look white
        //r:0.85490197, g:0.64705884, b:0.1254902
        if (ghost) {
            //red value
            if (CLOTHES_COLOR.r < 1f) {
                CLOTHES_COLOR.r += 0.01f;
            }
            //green value
            if (CLOTHES_COLOR.g < 1f) {
                CLOTHES_COLOR.g += 0.01f;
            }
            //blue value
            if (CLOTHES_COLOR.b < 1f) {
                CLOTHES_COLOR.b += 0.01f;
            }
        } else {
            //reset color to normal
            //red value
            if (CLOTHES_COLOR.r > 0.85490197f) {
                CLOTHES_COLOR.r -= 0.01f;
            }
            //green value
            if (CLOTHES_COLOR.g > 0.64705884f) {
                CLOTHES_COLOR.g -= 0.01f;
            }
            //blue value
            if (CLOTHES_COLOR.b > 0.1254902f) {
                CLOTHES_COLOR.b -= 0.01f;
            }
        }
        //disguise changes clothes color
        if (disguise) {
            CLOTHES_COLOR = new Color(Constants.GUARD_CLOTHES_COLOR);
        } else {
            CLOTHES_COLOR = new Color(Color.GOLDENROD);
        }
        if (!duck) {
            //ARMS
            //arms
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, position.y - Constants.HEAD_SIZE, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));

            //hands
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
            renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

            //LEGS
            //legs
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, legRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, -legRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));

            //BODY
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.3f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
            //high ranking symbols
            if (disguise) {
                //high ranking symbols
                renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r * 0.4f, Constants.GUARD_CLOTHES_COLOR.g * 0.4f, Constants.GUARD_CLOTHES_COLOR.b * 0.4f, spawnOpacity));
                renderer.rect((position.x - (Constants.HEAD_SIZE / 2)) + 1, position.y - (Constants.PLAYER_HEIGHT * 0.5f), Constants.PLAYER_WIDTH / 3f, Constants.PLAYER_HEIGHT / 11f);
                renderer.rect((position.x - (Constants.HEAD_SIZE / 2)) + 1, position.y - (Constants.PLAYER_HEIGHT * 0.6f), Constants.PLAYER_WIDTH / 3f, Constants.PLAYER_HEIGHT / 11f);
            }
            //belt
            renderer.setColor(disguise ? new Color(0.2f, 0.2f, 0.2f, spawnOpacity) : new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
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
            if (shock) {
                renderer.setColor(new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, spawnOpacity));
            } else {
                renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            }
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
            //when player drinks a potion, display potion bar
            if (drinkPotion) {
                renderer.setColor(new Color(Color.GRAY.r * 1.2f, Color.GRAY.g * 1.2f, Color.GRAY.b * 1.2f, 1));
                renderer.rectLine(position.x - 20, (position.y + Constants.HEAD_SIZE * 1.5f) + 12, position.x + 20, (position.y + Constants.HEAD_SIZE * 1.5f) + 12, 4);
                renderer.setColor(Color.WHITE);
                renderer.rectLine(position.x - 20, (position.y + Constants.HEAD_SIZE * 1.5f) + 12, (position.x - 20) + potionTimer, (position.y + Constants.HEAD_SIZE * 1.5f) + 12, 4);
                if (shock && potionTimer > 39) {
                    //stop holding any items
                    level.inventory.selectedItem = -1;
                }
                if (!level.show && !level.inventory.paused && !level.inventory.newItem) {
                    potionTimer -= 0.05;
                }
                if (potionTimer <= 0 && jumpState == Enums.JumpState.GROUNDED) {
                    ghost = false;
                    invisibility = false;
                    shock = false;
                    drinkPotion = false;
                    potionTimer = 40;
                }
            } else {
                potionTimer = 40;
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
            float energyY;
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

            if (level.superior.currentLevel != 14) {
                //Draw cx-7 (the robot voice with the Player, it looks like a head microphone)
                renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
                renderer.ellipse(position.x - Constants.HEAD_SIZE, position.y - (Constants.HEAD_SIZE / 4f), Constants.HEAD_SIZE / 6f, Constants.HEAD_SIZE / 1.5f);
                renderer.rectLine(position.x - Constants.HEAD_SIZE, position.y - (Constants.HEAD_SIZE / 4f), position.x - (Constants.HEAD_SIZE / 2f), position.y - (Constants.HEAD_SIZE / 2f), 1);
            }

            if (disguise) {
                //Draw guard's cap if player has disguise.
                renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r * 0.4f, Constants.GUARD_CLOTHES_COLOR.g * 0.4f, Constants.GUARD_CLOTHES_COLOR.b * 0.4f, spawnOpacity));
                if (facing == Enums.Facing.LEFT) {
                    renderer.rect((position.x - Constants.HEAD_SIZE) + 3, position.y + Constants.HEAD_SIZE * 0.5f, (Constants.HEAD_SIZE * 2f) - 4, Constants.HEAD_SIZE / 2f);
                    renderer.rect((position.x - Constants.HEAD_SIZE) - 2, position.y + Constants.HEAD_SIZE * 0.5f, 5, Constants.HEAD_SIZE / 3f);
                }
                if (facing == Enums.Facing.RIGHT) {
                    renderer.rect((position.x - Constants.HEAD_SIZE) + 2, position.y + Constants.HEAD_SIZE * 0.5f, (Constants.HEAD_SIZE * 2f) - 3, Constants.HEAD_SIZE / 2f);
                    renderer.rect((position.x - Constants.HEAD_SIZE) + 1 + ((Constants.HEAD_SIZE * 2f) - 3), position.y + Constants.HEAD_SIZE * 0.5f, 5, Constants.HEAD_SIZE / 3f);
                }
            }
        } else {
            Vector2 duckOffset = new Vector2(0, -24);
            armRotate = 25;
            //ARMS
            //arms
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, (position.y - Constants.HEAD_SIZE) + duckOffset.y, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), (position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f)) + duckOffset.y, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));

            //hands
            renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
            renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)) + duckOffset.y, Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
            renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)) + duckOffset.y, Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

            //LEGS
            //legs
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x, (position.y - (Constants.PLAYER_HEIGHT * 1f)) + duckOffset.y + 1, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 0.6f, 1, 1, 100, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), (position.y - (Constants.PLAYER_HEIGHT * 1f)) + duckOffset.y + 4, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 0.6f, 1, 1, -100, new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, invisibility ? spawnOpacity - 0.3f : spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity), new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
            renderer.rectLine(position.x - (Constants.PLAYER_WIDTH * 2), (position.y - (Constants.PLAYER_HEIGHT * 2.08f)), position.x, (position.y - (Constants.PLAYER_HEIGHT * 2.1f)), Constants.PLAYER_WIDTH / 2);
            renderer.rectLine(position.x + (Constants.PLAYER_WIDTH * 2), (position.y - (Constants.PLAYER_HEIGHT * 2.08f)), position.x + 1, (position.y - (Constants.PLAYER_HEIGHT * 2.1f)), Constants.PLAYER_WIDTH / 2);

            //1.2f
            //BODY
            renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
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
            if (shock) {
                renderer.setColor(new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, spawnOpacity));
            } else {
                renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
            }
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
            float energyY;
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

            if (level.superior.currentLevel != 14) {
                //Draw cx-7 (the robot voice with the Player, it looks like a head microphone)
                renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
                renderer.ellipse(position.x - Constants.HEAD_SIZE, (position.y - (Constants.HEAD_SIZE / 4f)) + duckOffset.y, Constants.HEAD_SIZE / 6f, Constants.HEAD_SIZE / 1.5f);
                renderer.rectLine(position.x - Constants.HEAD_SIZE, (position.y - (Constants.HEAD_SIZE / 4f)) + duckOffset.y, position.x - (Constants.HEAD_SIZE / 2f), (position.y - (Constants.HEAD_SIZE / 2f)) + duckOffset.y, 1);
            }
        }
        //if shock, render electric sparks around player
        if (shock) {
            Spark(renderer, new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)), new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f) - 20, position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f) - 20));
            Spark(renderer, new Vector2(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)), new Vector2(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f) + 20, position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f) - 20));
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
                if (heldItem.itemType.equals("stungun")) {
                    if (level.superior.currentLevel != 14) {
                        energy += 1f;
                    } else {
                        //reload energy every 10 shots during boss battle
                        energy += ((level.getPlayer().heldItem.stungun.lasersShot % 10 == 0) ? 0.2f : 1f);
                        if (level.getPlayer().heldItem.stungun.lasersShot % 10 == 0 && energy < 19) {
                            level.getPlayer().heldItem.stungun.reloading = true;
                        } else {
                            level.getPlayer().heldItem.stungun.reloading = false;
                        }
                    }
                } else {
                    energy += 0.2f;
                }
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
