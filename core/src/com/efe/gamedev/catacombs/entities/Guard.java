package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 3/8/2018.
 * This is the Guard class, which has many different states it can be in
 * The default state of the Guard is patrolling the Catacombs that it is in, but it can also talk to and attack the player
 * One of the player's enemy classes
 */

public class Guard {

    //position variables
    public Vector2 position;
    private Vector2 velocity;

    private float spawnOpacity;

    //rotate arms
    public float armRotate;

    //guard jumpState
    public Enums.JumpState jumpState;

    //rotate legs
    public long startTime;
    public float legRotate;
    private static final float LEG_MOVEMENT_DISTANCE = 20;
    private static final float LEG_PERIOD = 1.5f;

    //eye positions
    public Vector2 eyeLookLeft;
    public Vector2 eyeLookRight;

    //mouth offset
    public Vector2 mouthOffset;
    //mouth frames
    public Enums.MouthState mouthState;
    private float mouthFrameTimer;
    //look both ways
    public boolean suspicious;
    private float suspicionTimer;

    //The item that the guard holds
    public Item heldItem;
    public String guardItem;
    public Enums.Facing facing;

    private Level level;

    public int guardCatacomb;
    //target guard chases after
    public Vector2 targetPosition;
    public boolean chasePlayer;
    private float waitTimer;
    //guard states
    public Enums.GuardState guardState;
    private boolean patrolLeft;
    private boolean patrolRight;
    public boolean alert;
    private float alertTimer;
    private float unsureTimer;
    //talk with player
    public boolean talkToPlayer;
    private float timeSeeingPlayer;
    //fight with player
    public float guardEnergy;
    public float health;
    public boolean danger;
    private boolean weaponForward;

    //sleeping
    public boolean asleep;
    private float sleepTimer;
    //shocked
    private float shockTimer;

    public Guard (Vector2 position, Level level) {
        this.position = position;
        this.level = level;
        armRotate = 0;
        spawnOpacity = 1;
        legRotate = 170;
        startTime = TimeUtils.nanoTime();
        //get velocity and start guard off as falling
        velocity = new Vector2();
        jumpState = Enums.JumpState.FALLING;
        //initialize eyes
        eyeLookLeft = new Vector2(-Constants.EYE_OFFSET,Constants.EYE_OFFSET - 1);
        eyeLookRight = new Vector2(Constants.EYE_OFFSET, Constants.EYE_OFFSET - 1);
        //initialize mouth
        mouthOffset = new Vector2(0, -0.5f);
        mouthState = Enums.MouthState.NORMAL;
        mouthFrameTimer = 0;
        //initialize facing
        heldItem = new Item(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f), position.y - (Constants.PLAYER_HEIGHT / 1.05f)), level.viewportPosition, "");
        facing = Enums.Facing.LEFT;
        guardItem = "";
        targetPosition = new Vector2();
        //guard patrolling area
        guardState = Enums.GuardState.PATROLLING;
        patrolLeft = false;
        patrolRight = false;
        //timers for certain things
        alert = false;
        alertTimer = 0;
        unsureTimer = 0;
        talkToPlayer = false;
        timeSeeingPlayer = 0;
        chasePlayer = false;
        waitTimer = 0;
        guardEnergy = 15;
        health = 20;
        danger = false;
        weaponForward = false;
        suspicious = false;
        suspicionTimer = 0;
        asleep = false;
        sleepTimer = 0;
        shockTimer = 0;
    }

    public void update (float delta) {
        //constantly find nearest catacomb to guard and set that to guardCatacomb
        findNearestCatacomb();
        //add velocity and gravity. Add less gravity when going into the next catacomb
        if (insideCatacomb()) {
            velocity.y -= Constants.GRAVITY;
        } else {
            velocity.y -= Constants.GRAVITY / 4f;
        }
        position.mulAdd(velocity, delta);

        //guard states
        //make guard patrol back and forth
        if (guardState == Enums.GuardState.PATROLLING) {
            if (!patrolLeft) {
                targetPosition.x = (level.catacombs.get(guardCatacomb).position.x + 55.5f);
            }
            if (!patrolRight && patrolLeft) {
                targetPosition.x = (level.catacombs.get(guardCatacomb).position.x + level.catacombs.get(guardCatacomb).width - 58f);
            }
            if (position.x <= (level.catacombs.get(guardCatacomb).position.x + 57f)) {
                patrolLeft = true;
                patrolRight = false;
            }
            if (position.x >= (level.catacombs.get(guardCatacomb).position.x + level.catacombs.get(guardCatacomb).width - 58f)) {
                patrolLeft = false;
                patrolRight = true;
            }
            suspicious = false;
        }
        //see if guard wants to chase player
        if (chasePlayer) {
            //timer that waits for a while then makes guard chase player
            if (waitTimer < 150 && guardState != Enums.GuardState.ATTACK) {
                waitTimer++;
            }
            if (waitTimer > 149) {
                guardState = Enums.GuardState.ATTACK;
                waitTimer = 0;
            }
        }
        //guard chases you
        if (guardState == Enums.GuardState.ATTACK) {
            //guard gets confused if it tries to attack player and player turns invisible
            if (!level.getPlayer().invisibility) {
                targetPosition.set(level.superior.currentLevel != 14 ? level.getPlayer().getPosition() : new Vector2(230, -20));
                tryJumping();
                suspicious = false;
            } else {
                suspicious = true;
            }
            //after a while of not seeing player, guard will resume patrolling
            if (position.dst(level.getPlayer().getPosition()) > 100) {
                unsureTimer++;
            }
            if (unsureTimer >= 200 && jumpState == Enums.JumpState.GROUNDED) {
                unsureTimer = 0;
                chasePlayer = false;
                guardState = Enums.GuardState.PATROLLING;
            }
        }
        //check if guard spots player, or, if player is invisible and guard touches player.
        if ((position.dst(level.getPlayer().getPosition()) < 50 && guardState != Enums.GuardState.ATTACK && guardState != Enums.GuardState.DEFEATED && !level.getPlayer().invisibility) || (guardTouchesPlayer() && guardState != Enums.GuardState.ATTACK && guardState != Enums.GuardState.DEFEATED && level.getPlayer().invisibility) && level.superior.currentLevel == 7) {
            if (((position.x >= level.getPlayer().getPosition().x && facing == Enums.Facing.LEFT) || (position.x <= level.getPlayer().getPosition().x && facing == Enums.Facing.RIGHT)) && jumpState == Enums.JumpState.GROUNDED) {
                alert = true;
                guardState = Enums.GuardState.ALERT;
            }
        } else {
            //if player runs out of guard's sight and guard is currently alert, set guardState to UNSURE and have guard try to find player
            if (alertTimer < 50 && alert && guardState != Enums.GuardState.DEFEATED) {
                alertTimer++;
            } else if (alertTimer >= 50) {
                unsureTimer = 0;
                guardState = Enums.GuardState.UNSURE;
                alert = false;
                alertTimer = 0;
            }
        }
        //If guard sees player for long enough, it will talk to player.
        if (guardState == Enums.GuardState.ALERT) {
            if (timeSeeingPlayer < 100 && !talkToPlayer && !level.getPlayer().invisibility) {
                timeSeeingPlayer++;
            }
            if (timeSeeingPlayer > 99) {
                talkToPlayer = true;
                level.touchLocked = true;
            }
        }
        //if guard is unsure, it tries to look for you
        if (guardState == Enums.GuardState.UNSURE) {
            talkToPlayer = false;
            timeSeeingPlayer = 0;
            unsureTimer++;
            if (unsureTimer >= 200 && jumpState == Enums.JumpState.GROUNDED) {
                suspicious = false;
                unsureTimer = 0;
                guardState = Enums.GuardState.PATROLLING;
            }
            //if player is not invisible, try to find player
            if (!level.getPlayer().invisibility) {
                if (level.getPlayer().getPosition().x < position.x && jumpState == Enums.JumpState.GROUNDED) {
                    facing = Enums.Facing.LEFT;
                    targetPosition.set(level.getPlayer().getPosition().x - 10, level.getPlayer().getPosition().y);
                } else if (level.getPlayer().getPosition().x >= position.x && jumpState == Enums.JumpState.GROUNDED) {
                    facing = Enums.Facing.RIGHT;
                    targetPosition.set(level.getPlayer().getPosition().x + 10, level.getPlayer().getPosition().y);
                }
                //try jumping to find player when unsure
                tryJumping();
            } else {
                suspicious = true;
            }
        }
        //look at player when alert
        if (alert) {
            if (level.getPlayer().getPosition().x >= position.x) {
                facing = Enums.Facing.RIGHT;
            } else if (level.getPlayer().getPosition().x < position.x) {
                facing = Enums.Facing.LEFT;
            }
            if (guardState != Enums.GuardState.DEFEATED && !level.getPlayer().invisibility) {
                targetPosition.set(level.getPlayer().getPosition());
            }
        }
        //make sure player is facing guard
        if (talkToPlayer) {
            level.getPlayer().facing = (facing == Enums.Facing.LEFT ? Enums.Facing.RIGHT : Enums.Facing.LEFT);
        }
        //if guard is talking to player and guardTouchesPlayer, player will move to the side
        if (talkToPlayer && guardTouchesPlayer() && !level.getPlayer().moving && guardState != Enums.GuardState.ATTACK && guardState != Enums.GuardState.DEFEATED) {
            makePlayerMoveWhileTalking();
        }
        //make guard look around at player
        if (!suspicious) {
            lookAround(delta, targetPosition);
            //reset suspicionTimer
            suspicionTimer = 0;
        } else {
            //if suspicious, look both ways several times
            lookBothWays(delta);
        }
        //guard states
        if (!targetPosition.equals(new Vector2()) && (insideCatacomb() || outsideCatacombLeft() || outsideCatacombRight())) {
            if (guardState == Enums.GuardState.PATROLLING) {
                moveGuard(delta, 20, targetPosition);
            } else if (guardState == Enums.GuardState.ALERT) {
                resetLegs();
            } else if (guardState != Enums.GuardState.DEFEATED) {
                moveGuard(delta, jumpState == Enums.JumpState.FALLING ? 60 : 40, targetPosition);
            }
        }
        //stop guard from falling below current catacomb if bottom is locked
        if (position.y < level.catacombs.get(guardCatacomb).position.y + level.catacombs.get(guardCatacomb).wallThickness + 61 && (!level.catacombs.get(guardCatacomb).getLockedDoors().get(5).equals("Unlocked") && level.catacombs.get(guardCatacomb).bottomsOffset.x > -60) && insideCatacomb()) {
            velocity.y = 0;
            velocity.x = 0;
            position.y = level.catacombs.get(guardCatacomb).position.y + level.catacombs.get(guardCatacomb).wallThickness + 61;
            jumpState = Enums.JumpState.GROUNDED;
        }

        //if guard is outside of current catacomb on left side, make guard assigned to that catacomb on left side.
        if (outsideCatacombLeft() && jumpState != Enums.JumpState.JUMPING) {
            targetPosition.x = level.catacombs.get(guardCatacomb).position.x + 1;
            jumpState = Enums.JumpState.FALLING;
        }
        //if guard is outside of current catacomb on right side, make guard assigned to that catacomb on right side.
        if (outsideCatacombRight() && jumpState != Enums.JumpState.JUMPING) {
            targetPosition.x = level.catacombs.get(guardCatacomb).position.x + level.catacombs.get(guardCatacomb).width;
            jumpState = Enums.JumpState.FALLING;
        }

        //update held item position
        if (!guardItem.equals("")) {
            if (!level.getPlayer().fighting) {
                holdItem(guardItem);
            }
        } else {
            heldItem.itemType = "";
        }
        //set up weapon push
        if (level.getPlayer().fighting && level.currentCatacomb == guardCatacomb) {
            //adjust guard position if to close to player
            if (level.getPlayer().getPosition().x > position.x && position.x != level.getPlayer().getPosition().x - 30) {
                position.x = level.getPlayer().getPosition().x - 30;
            }
            if (level.getPlayer().getPosition().x < position.x && position.x != level.getPlayer().getPosition().x + 30) {
                position.x = level.getPlayer().getPosition().x + 30;
            }
            //weapons go back and forth when fighting with player
            if (weaponForward) {
                heldItem.position.x += 0.5;
                level.getPlayer().heldItem.position.x += 0.5;
            }
            if (!weaponForward) {
                heldItem.position.x -= 0.5;
                level.getPlayer().heldItem.position.x -= 0.5;
            }
            if (position.x > level.getPlayer().getPosition().x) {
                if ((heldItem.position.x > (position.x - 1) - Constants.HEAD_SIZE * 1.65f)) {
                    weaponForward = false;
                }
                if ((heldItem.position.x < (level.getPlayer().getPosition().x + 1) + Constants.HEAD_SIZE * 1.65f)) {
                    weaponForward = true;
                }
            } else {
                if ((heldItem.position.x < (position.x + 1) + Constants.HEAD_SIZE * 0.8f)) {
                    weaponForward = true;
                }
                if ((heldItem.position.x > (level.getPlayer().getPosition().x - 1) - Constants.HEAD_SIZE * 2.5f)) {
                    weaponForward = false;
                }
            }
        }

        if (shockTimer > 0) {
            shockTimer--;
            guardState = Enums.GuardState.DEFEATED;
            if (armRotate < 50) {
                armRotate += 4f;
            }
            mouthState = Enums.MouthState.OPEN;
            resetLegs();
            //drop any items
            if (!guardItem.equals("")) {
                level.items.add(new Item(new Vector2(position.x, position.y - 50), level.viewportPosition, guardItem));
                guardItem = "";
            }
        } else {
            shockTimer = 0;
            if (guardState != Enums.GuardState.ATTACK) {
                guardState = Enums.GuardState.PATROLLING;
            }
            if (armRotate > 0) {
                armRotate -= 2f;
                mouthState = Enums.MouthState.NORMAL;
            }
        }
        //player shocks (with electricity) guard.
        if (level.getPlayer().shock && guardTouchesPlayer()) {
            shockTimer = 400;
        }
    }

    public boolean guardTouchesPlayer () {
        //make player bounds
        Rectangle playerBounds = new Rectangle(level.getPlayer().getPosition().x - Constants.HEAD_SIZE,
                level.getPlayer().getPosition().y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                Constants.PLAYER_WIDTH * 2f,
                Constants.PLAYER_HEIGHT * 2.5f);
        //make guard bounds
        Rectangle guardBounds = new Rectangle(jumpState.equals(Enums.JumpState.GROUNDED) ? (position.x - Constants.HEAD_SIZE) - 12 : (position.x - Constants.HEAD_SIZE),
                position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f),
                Constants.PLAYER_HEIGHT * 2.5f);
        //if player bounds touches guard bounds, return true.
        return guardBounds.overlaps(playerBounds);
    }

    //detects if this guard touches another guard's back
    private boolean guardTouchesGuardBack () {
        boolean guardTouchBack = false;
        //this guard's bounds
        Rectangle guardBounds = new Rectangle(jumpState.equals(Enums.JumpState.GROUNDED) ? (position.x - Constants.HEAD_SIZE) - 12 : (position.x - Constants.HEAD_SIZE),
                position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f),
                Constants.PLAYER_HEIGHT * 2.5f);
        //loop through guards
        for (int i = 0; i < level.guards.size; i++) {
            //other guards' bounds
            Rectangle otherGuardBounds = new Rectangle(jumpState.equals(Enums.JumpState.GROUNDED) ? (level.guards.get(i).position.x - Constants.HEAD_SIZE) - 12 : (level.guards.get(i).position.x - Constants.HEAD_SIZE),
                    level.guards.get(i).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                    jumpState.equals(Enums.JumpState.GROUNDED) ? (Constants.PLAYER_WIDTH * 2f) + 12 : (Constants.PLAYER_WIDTH * 2f),
                    Constants.PLAYER_HEIGHT * 2.5f);
            //if the other guard is truly a different guard, check if they overlap each other
            if (i != level.guards.indexOf(this, true)) {
                guardTouchBack = guardBounds.overlaps(otherGuardBounds) && (jumpState == Enums.JumpState.GROUNDED && level.guards.get(i).jumpState == Enums.JumpState.GROUNDED) && (facing == level.guards.get(i).facing) && (facing == Enums.Facing.LEFT ? position.x > level.guards.get(i).position.x : position.x < level.guards.get(i).position.x);
            }
        }
        //return results
        return guardTouchBack;
    }

    private void tryJumping () {
        //try jumping
        if (position.dst(level.getPlayer().getPosition()) < 250 && level.getPlayer().getPosition().y > position.y + 10 && jumpState == Enums.JumpState.GROUNDED) {
            if (position.x < level.getPlayer().getPosition().x - 10 && (level.catacombs.get(guardCatacomb).getLockedDoors().get(3).equals("Unlocked")) && !level.catacombs.get(guardCatacomb).getLockedDoors().get(4).equals("Unlocked")) {
                facing = Enums.Facing.RIGHT;
                if (position.x > level.catacombs.get(guardCatacomb).position.x + (level.catacombs.get(guardCatacomb).width - 57.14f)) {
                    velocity = new Vector2(30, 170);
                    jumpState = Enums.JumpState.JUMPING;
                } else {
                    targetPosition.x = level.catacombs.get(guardCatacomb).position.x + (level.catacombs.get(guardCatacomb).width - 57.14f);
                }
            } else if (position.x > level.getPlayer().getPosition().x + 10 && (level.catacombs.get(guardCatacomb).getLockedDoors().get(1).equals("Unlocked")) && !level.catacombs.get(guardCatacomb).getLockedDoors().get(0).equals("Unlocked")) {
                facing = Enums.Facing.LEFT;
                if (position.x < level.catacombs.get(guardCatacomb).position.x + (200 / 3.5f)) {
                    velocity = new Vector2(-30, 170);
                    jumpState = Enums.JumpState.JUMPING;
                } else {
                    targetPosition.x = level.catacombs.get(guardCatacomb).position.x + (200 / 3.5f) - 1;
                }
            }
        }
    }

    private void makePlayerMoveWhileTalking () {
        //Deals with four different situations when player touches guard while talking
        Vector2 playerPosition = new Vector2(level.getPlayer().getPosition());
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);
        //Before setting viewportPosition.x, viewportPosition.y must be set to the center of screen in order to avoid the player not moving since viewportPosition.x will not move player when it is in the inventory bar.
        level.viewportPosition.y = playerPosition.y;
        //First: if player is to the right of guard and on left side of Catacomb.
        //Second: if player is to the right of guard and on right side of Catacomb.
        if (position.x <= playerPosition.x && playerPosition.x <= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.viewportPosition.x = position.x + (Constants.PLAYER_WIDTH * 4);
        } else if (position.x <= playerPosition.x && playerPosition.x >= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.viewportPosition.x = position.x - (Constants.PLAYER_WIDTH * 4);
        }
        //Third: if player is to the left of guard and on right side of Catacomb.
        //Fourth: if player is to the left of guard and on left side of Catacomb.
        if (position.x >= playerPosition.x && playerPosition.x >= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.viewportPosition.x = position.x - (Constants.PLAYER_WIDTH * 4);
        } else if (position.x >= playerPosition.x && playerPosition.x <= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.viewportPosition.x = position.x + (Constants.PLAYER_WIDTH * 4);
        }
    }

    public void moveGuard (float delta, float moveSpeed, Vector2 movePosition) {
        //function which moves guard towards its target position.
        if ((!(position.x < movePosition.x + 1) || !(position.x > movePosition.x)) && !movePosition.equals(new Vector2()) && !guardTouchesGuardBack()) {
            //go left
            if (position.x > movePosition.x) {
                position.x -= delta * moveSpeed;
                rotateLegs();
                facing = Enums.Facing.LEFT;
            } else if (position.x < movePosition.x) {
                //go right
                position.x += delta * moveSpeed;
                rotateLegs();
                facing = Enums.Facing.RIGHT;
            }
        } else {
            //otherwise, reset
            resetLegs();
        }
        //guard collides with player when falling
        Vector2 playerPosition = new Vector2(level.getPlayer().getPosition());
        Catacomb currentCatacomb = level.catacombs.get(level.currentCatacomb);

        if (jumpState == Enums.JumpState.FALLING && guardTouchesPlayer() && position.x > level.getPlayer().getPosition().x && playerPosition.x >= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.getPlayer().setVelocity(new Vector2(-100, 20));
            level.viewportPosition.set(new Vector2());
        } else if (jumpState == Enums.JumpState.FALLING && guardTouchesPlayer() && position.x < level.getPlayer().getPosition().x && playerPosition.x <= currentCatacomb.position.x + currentCatacomb.width / 2) {
            level.getPlayer().setVelocity(new Vector2(100, 20));
            level.viewportPosition.set(new Vector2());
        }
    }

    //find catacomb nearest to guard and assign guard to it
    private void findNearestCatacomb () {
        for (Catacomb catacomb : level.catacombs) {
            if (position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) + 50)) < catacomb.width / 2 && position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) - 50)) < catacomb.width / 2) {
                guardCatacomb = level.catacombs.indexOf(catacomb,true);
            }
        }
    }

    private boolean insideCatacomb () {
        Catacomb currentCatacomb = level.catacombs.get(guardCatacomb);
        //check if position.x is more than catacomb's left side
        //return results to determine if player is within catacomb's bounds
        return (position.x > currentCatacomb.position.x + 55 && position.x < currentCatacomb.position.x + currentCatacomb.width - 55);
    }

    private boolean outsideCatacombLeft () {
        Catacomb currentCatacomb = level.catacombs.get(guardCatacomb);
        //check if position.x is more than catacomb's left side
        boolean outsideX = position.x <= currentCatacomb.position.x + 55;

        if (outsideX && (currentCatacomb.getLockedDoors().get(0).equals("Locked") || currentCatacomb.getLockedDoors().get(0).equals("DoubleLocked") || currentCatacomb.getLockedDoors().get(0).equals("Closed")) && jumpState != Enums.JumpState.JUMPING) {
            targetPosition.x = currentCatacomb.position.x + 55.5f;
            resetLegs();
        }
        //return results to determine if guard is outside catacomb's bounds
        return outsideX;
    }

    private boolean outsideCatacombRight () {
        Catacomb currentCatacomb = level.catacombs.get(guardCatacomb);
        //check if position.x is less than catacomb's right side
        boolean outsideWidth = position.x >= currentCatacomb.position.x + currentCatacomb.width - 55;
        if (outsideWidth && (currentCatacomb.getLockedDoors().get(4).equals("Locked") || currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked") || currentCatacomb.getLockedDoors().get(4).equals("Closed")) && jumpState != Enums.JumpState.JUMPING) {
            targetPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
            resetLegs();
        }
        //return results to determine if guard is outside catacomb's bounds
        return outsideWidth;
    }

    private void lookAround (float delta, Vector2 movePosition) {

        float moveSpeed = 5;
        //set mouth moving speed
        float mouthSpeed = (moveSpeed / 2);

        //if viewport is touched
        if (!movePosition.equals(new Vector2())) {
            //move eye x
            if (movePosition.x + 0.5f > position.x && eyeLookLeft.x < 0) {
                eyeLookLeft.x += delta * moveSpeed;
                eyeLookRight.x += delta * moveSpeed;
                //move mouth
                mouthOffset.x += delta * mouthSpeed;
            } else if (movePosition.x < position.x && eyeLookLeft.x > 0 - (Constants.HEAD_SIZE / 2)) {
                eyeLookLeft.x -= delta * moveSpeed;
                eyeLookRight.x -= delta * moveSpeed;
                //move mouth
                mouthOffset.x -= delta * mouthSpeed;
            }
            //move eye y
            if (movePosition.y > position.y && eyeLookLeft.y < 0 + (Constants.HEAD_SIZE / 3)) {
                eyeLookLeft.y += delta * moveSpeed;
                eyeLookRight.y += delta * moveSpeed;
                //move mouth
                mouthOffset.y += delta * mouthSpeed;
            } else if (movePosition.y < position.y && eyeLookLeft.y > 0) {
                eyeLookLeft.y -= delta * moveSpeed;
                eyeLookRight.y -= delta * moveSpeed;
                //move mouth
                mouthOffset.y -= delta * mouthSpeed;
            }
        }
        //reset eyes and mouth
        if (eyeLookLeft.y > 1 + (Constants.HEAD_SIZE / 3)) {
            //initialize eyes
            eyeLookLeft = new Vector2(-Constants.EYE_OFFSET,Constants.EYE_OFFSET - 1);
            eyeLookRight = new Vector2(Constants.EYE_OFFSET, Constants.EYE_OFFSET - 1);
            //initialize mouth
            mouthOffset = new Vector2(0, -0.5f);
        }
    }

    //look both ways
    private void lookBothWays (float delta) {
        //A simple timer loop that makes Guard look right, then look left, then repeat.
        suspicionTimer++;
        if (suspicionTimer > 0 && suspicionTimer <= 100) {
            lookAround(delta, new Vector2(position.x + 20, position.y + 20));
            facing = Enums.Facing.RIGHT;
        } else if (suspicionTimer > 100 && suspicionTimer <= 200) {
            lookAround(delta, new Vector2(position.x - 20, position.y + 20));
            facing = Enums.Facing.LEFT;
        } else if (suspicionTimer > 200) {
            suspicionTimer = 0;
        }
    }

    private void holdItem (String itemType) {
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

    public void resetLegs () {
        //if guard is on ground and resetLegs() is called, this will reset guard's legs.
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
    }

    private void talk (ShapeRenderer renderer) {
        //speed
        mouthFrameTimer += Constants.MOUTH_TALKING_SPEED;

        //talk frames to make mouth move
        if (mouthFrameTimer >= 0 && mouthFrameTimer < 50) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 5, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 50 && mouthFrameTimer < 100) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 100 && mouthFrameTimer < 150) {
            renderer.arc(position.x + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 4, 180, 180);
        } else if (mouthFrameTimer >= 150 && mouthFrameTimer < 200) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 4, 170, 170);
        } else if (mouthFrameTimer >= 200 && mouthFrameTimer < 250) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 250 && mouthFrameTimer < 300) {
            renderer.circle(position.x + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 300 && mouthFrameTimer < 350) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 350 && mouthFrameTimer < 400) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y, Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
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

        //ARMS
        //arms
        renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r, Constants.GUARD_CLOTHES_COLOR.g, Constants.GUARD_CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x, position.y - Constants.HEAD_SIZE, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate);
        renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate);

        //hands
        renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
        renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
        renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

        //LEGS
        //legs
        renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r, Constants.GUARD_CLOTHES_COLOR.g, Constants.GUARD_CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x, position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, legRotate);
        renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, -legRotate);

        //BODY
        renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r, Constants.GUARD_CLOTHES_COLOR.g, Constants.GUARD_CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.3f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        //high ranking symbols
        if (level.superior.currentLevel == 8) {
            //high ranking symbols
            renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r * 0.4f, Constants.GUARD_CLOTHES_COLOR.g * 0.4f, Constants.GUARD_CLOTHES_COLOR.b * 0.4f, spawnOpacity));
            renderer.rect((position.x - (Constants.HEAD_SIZE / 2)) + 1, position.y - (Constants.PLAYER_HEIGHT * 0.5f), Constants.PLAYER_WIDTH / 3f, Constants.PLAYER_HEIGHT / 11f);
            renderer.rect((position.x - (Constants.HEAD_SIZE / 2)) + 1, position.y - (Constants.PLAYER_HEIGHT * 0.6f), Constants.PLAYER_WIDTH / 3f, Constants.PLAYER_HEIGHT / 11f);
        }
        //belt
        renderer.setColor(new Color(0.2f, 0.2f, 0.2f, spawnOpacity));
        renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.2f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT / 10);

        //HEAD
        //general shape and outline
        renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
        //main head
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE, Constants.HEAD_SEGMENTS);
        renderer.setColor(new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity));
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE * 15 / 16, Constants.HEAD_SEGMENTS);

        //if alert, show exclamation mark
        if (alert && guardState != Enums.GuardState.FIGHT) {
            renderer.setColor(Color.WHITE);
            renderer.circle(position.x, position.y + Constants.HEAD_SIZE * 1.5f, Constants.HEAD_SIZE/4, 10);
            renderer.rectLine(position.x, position.y + (Constants.HEAD_SIZE*2), position.x, position.y + Constants.HEAD_SIZE*3, Constants.HEAD_SIZE/6);
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

        //if fighting with player, show fight bar
        if (guardState == Enums.GuardState.FIGHT) {
            renderer.setColor(Color.WHITE);
            renderer.rectLine(position.x, position.y + Constants.HEAD_SIZE * 1.5f, level.getPlayer().getPosition().x, position.y + Constants.HEAD_SIZE * 1.5f, 4);
            renderer.setColor(Color.RED);
            renderer.rectLine(position.x, position.y + Constants.HEAD_SIZE * 1.5f, (position.x < level.getPlayer().getPosition().x) ? (position.x + guardEnergy) : (position.x - guardEnergy), position.y + Constants.HEAD_SIZE * 1.5f, 4);
            renderer.setColor(Color.BLUE);
            renderer.rectLine(level.getPlayer().getPosition().x, position.y + Constants.HEAD_SIZE * 1.5f, (position.x > level.getPlayer().getPosition().x) ? (level.getPlayer().getPosition().x + (30 - guardEnergy)) : (level.getPlayer().getPosition().x - (30 - guardEnergy)), position.y + Constants.HEAD_SIZE * 1.5f, 4);
            //reaction to losing
            if (guardEnergy < 10) {
                mouthState = Enums.MouthState.OPEN;
            } else {
                mouthState = Enums.MouthState.NORMAL;
            }
            if (!level.inventory.paused) {
                guardEnergy += (level.superior.currentLevel == 4 ? MathUtils.random(0.01f, 0.05f) : MathUtils.random(0.04f, 0.08f));
            }
        }

        //draw sleeping "Z"s
        if (asleep) {
            //switch between three Zs in different positions
            sleepTimer ++;
            if (sleepTimer >= 0 && sleepTimer <= 30) {
                sleepZ(new Vector2(position.x + (Constants.HEAD_SIZE), position.y + (Constants.HEAD_SIZE)), Constants.HEAD_SIZE / 2f, renderer);
            } else if (sleepTimer > 30 && sleepTimer <= 60) {
                sleepZ(new Vector2(position.x + (Constants.HEAD_SIZE * 1.5f), position.y + (Constants.HEAD_SIZE * 1.5f)), Constants.HEAD_SIZE / 3f, renderer);
            } else if (sleepTimer > 60 && sleepTimer <= 90) {
                sleepZ(new Vector2(position.x + (Constants.HEAD_SIZE * 2f), position.y + (Constants.HEAD_SIZE * 2f)), Constants.HEAD_SIZE / 4f, renderer);
            } else if (sleepTimer > 90) {
                sleepTimer = 0;
            }
        } else {
            //reset sleepTimer
            sleepTimer = 0;
        }

        //eyes
        if (shockTimer > 0) {
            renderer.setColor(new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, spawnOpacity));
        } else {
            renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
        }
        //draw the eyes as circles when awake but as slits when asleep
        if (!asleep) {
            renderer.circle(eyeLookLeft.x + position.x, eyeLookLeft.y + position.y, Constants.HEAD_SIZE / 10, 4);
            renderer.circle(eyeLookRight.x + position.x, eyeLookRight.y + position.y, Constants.HEAD_SIZE / 10, 4);
        } else {
            renderer.rectLine((eyeLookLeft.x + position.x) - (Constants.HEAD_SIZE / 10), eyeLookLeft.y + position.y, (eyeLookLeft.x + position.x) + (Constants.HEAD_SIZE / 10), eyeLookLeft.y + position.y, 0.5f);
            renderer.rectLine((eyeLookRight.x + position.x) - (Constants.HEAD_SIZE / 10), eyeLookRight.y + position.y, (eyeLookRight.x + position.x) + (Constants.HEAD_SIZE / 10), eyeLookRight.y + position.y, 0.5f);
        }
        //mouth
        renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
        //mouth states
        if (mouthState == Enums.MouthState.NORMAL) {
            renderer.rectLine(position.x - (Constants.HEAD_SIZE / 4f) + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, position.x + (Constants.HEAD_SIZE / 4) + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.MOUTH_THICKNESS);
        } else if (mouthState == Enums.MouthState.OPEN) {
            renderer.circle(position.x + mouthOffset.x, position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y, Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
        } else if (mouthState == Enums.MouthState.TALKING) {
            talk(renderer);
        }

        //electricity shocking guard from player
        if (shockTimer > 0) {
            Spark(renderer, new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)), new Vector2(position.x + 10, position.y - Constants.HEAD_SIZE - 10));
            Spark(renderer, new Vector2(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f)), new Vector2(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f) - 10, position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f) - 10));
            Spark(renderer, new Vector2((position.x - (Constants.HEAD_SIZE / 2)) + 2, position.y - (Constants.PLAYER_HEIGHT * 1.2f)), new Vector2((position.x - (Constants.HEAD_SIZE / 2)) - 8, position.y - (Constants.PLAYER_HEIGHT * 3f)));
            Spark(renderer, new Vector2((position.x - (Constants.HEAD_SIZE / 2)) + 6, position.y - (Constants.PLAYER_HEIGHT * 1.2f)), new Vector2((position.x - (Constants.HEAD_SIZE / 2)) + 16, position.y - (Constants.PLAYER_HEIGHT * 3f)));
        }

        //Draw guard's cap
        //renderer.setColor(new Color(0.2f, 0.2f, 0.2f, spawnOpacity));
        renderer.setColor(new Color(Constants.GUARD_CLOTHES_COLOR.r, Constants.GUARD_CLOTHES_COLOR.g, Constants.GUARD_CLOTHES_COLOR.b, spawnOpacity));
        if (facing == Enums.Facing.LEFT) {
            renderer.rect((position.x - Constants.HEAD_SIZE) + 3, position.y + Constants.HEAD_SIZE * 0.5f, (Constants.HEAD_SIZE * 2f) - 4, Constants.HEAD_SIZE / 2f);
            renderer.rect((position.x - Constants.HEAD_SIZE) - 2, position.y + Constants.HEAD_SIZE * 0.5f, 5, Constants.HEAD_SIZE / 3f);
        }
        if (facing == Enums.Facing.RIGHT) {
            renderer.rect((position.x - Constants.HEAD_SIZE) + 2, position.y + Constants.HEAD_SIZE * 0.5f, (Constants.HEAD_SIZE * 2f) - 3, Constants.HEAD_SIZE / 2f);
            renderer.rect((position.x - Constants.HEAD_SIZE) + 1 + ((Constants.HEAD_SIZE * 2f) - 3), position.y + Constants.HEAD_SIZE * 0.5f, 5, Constants.HEAD_SIZE / 3f);
        }
        //Draw Item guard is holding
        if (jumpState == Enums.JumpState.GROUNDED) {
            heldItem.render(renderer, level);
        }
    }
    //draw Zs for sleeping animation
    private void sleepZ (Vector2 zPosition, float size, ShapeRenderer renderer) {
        //bottom line
        renderer.rectLine(zPosition.x, zPosition.y, zPosition.x + size, zPosition.y, size / 5);
        //top line
        renderer.rectLine(zPosition.x, zPosition.y + size, zPosition.x + size, zPosition.y + size, size / 5);
        //middle slanted line
        renderer.rectLine(zPosition.x, zPosition.y, zPosition.x + size, zPosition.y + size, size / 5);
    }

}
