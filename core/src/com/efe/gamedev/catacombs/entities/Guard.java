package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
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
 */

public class Guard {

    public Vector2 position;
    private Vector2 velocity;

    private float spawnOpacity;

    //rotate arms
    public float armRotate;

    //player jumpState
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
    private boolean weaponForward;

    public Guard (Vector2 position, Level level) {
        this.position = position;
        this.level = level;
        armRotate = 0;
        spawnOpacity = 1;
        legRotate = 170;
        startTime = TimeUtils.nanoTime();
        //get velocity and start player off as falling
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
        weaponForward = false;
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
            targetPosition.set(level.getPlayer().getPosition());
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
        //check if guard spots you
        if (position.dst(level.getPlayer().getPosition()) < 50 && guardState != Enums.GuardState.ATTACK) {
            if (((position.x >= level.getPlayer().getPosition().x && facing == Enums.Facing.LEFT) || (position.x <= level.getPlayer().getPosition().x && facing == Enums.Facing.RIGHT)) && jumpState == Enums.JumpState.GROUNDED) {
                alert = true;
                guardState = Enums.GuardState.ALERT;
            }
        } else {
            if (alertTimer < 50 && alert) {
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
            if (timeSeeingPlayer < 100 && !talkToPlayer) {
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
                unsureTimer = 0;
                guardState = Enums.GuardState.PATROLLING;
            }
            if (level.getPlayer().getPosition().x < position.x && jumpState == Enums.JumpState.GROUNDED) {
                facing = Enums.Facing.LEFT;
                targetPosition.set(level.getPlayer().getPosition().x - 10, level.getPlayer().getPosition().y);
            } else if (level.getPlayer().getPosition().x >= position.x && jumpState == Enums.JumpState.GROUNDED) {
                facing = Enums.Facing.RIGHT;
                targetPosition.set(level.getPlayer().getPosition().x + 10, level.getPlayer().getPosition().y);
            }
            //try jumping to find player when unsure
            tryJumping();
        }
        //look at player when alert
        if (alert) {
            if (level.getPlayer().getPosition().x >= position.x) {
                facing = Enums.Facing.RIGHT;
            } else if (level.getPlayer().getPosition().x < position.x) {
                facing = Enums.Facing.LEFT;
            }
            targetPosition.set(level.getPlayer().getPosition());
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
        lookAround(delta, 5);
        //guard states
        if (!targetPosition.equals(new Vector2()) && (insideCatacomb() || outsideCatacombLeft() || outsideCatacombRight())) {
            if (guardState == Enums.GuardState.PATROLLING) {
                moveGuard(delta, 20);
            } else if (guardState == Enums.GuardState.ALERT) {
                resetLegs();
            } else if (guardState != Enums.GuardState.DEFEATED) {
                moveGuard(delta, jumpState == Enums.JumpState.FALLING ? 60 : 40);
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
        if (level.getPlayer().fighting) {
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

    private void tryJumping () {
        //try jumping
        if (position.dst(level.getPlayer().getPosition()) < 150 && level.getPlayer().getPosition().y > position.y + 10 && jumpState == Enums.JumpState.GROUNDED) {
            if (position.x < level.getPlayer().getPosition().x - 10) {
                facing = Enums.Facing.RIGHT;
                if (position.x > level.catacombs.get(guardCatacomb).position.x + (level.catacombs.get(guardCatacomb).width - 57.14f)) {
                    velocity = new Vector2(30, 170);
                    jumpState = Enums.JumpState.JUMPING;
                } else {
                    targetPosition.x = level.catacombs.get(guardCatacomb).position.x + (level.catacombs.get(guardCatacomb).width - 57.14f);
                }
            } else if (position.x > level.getPlayer().getPosition().x + 10) {
                facing = Enums.Facing.LEFT;
                if (position.x < level.catacombs.get(guardCatacomb).position.x + (200 / 3.5f)) {
                    velocity = new Vector2(-30, 170);
                    jumpState = Enums.JumpState.JUMPING;
                } else {
                    targetPosition.x = level.catacombs.get(level.currentCatacomb).position.x + (200 / 3.5f);
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

    public void moveGuard (float delta, float moveSpeed) {
        if ((!(position.x < targetPosition.x + 1) || !(position.x > targetPosition.x)) && !targetPosition.equals(new Vector2())) {
            //go left
            if (position.x > targetPosition.x) {
                position.x -= delta * moveSpeed;
                rotateLegs();
                facing = Enums.Facing.LEFT;
            } else if (position.x < targetPosition.x) {
                //go right
                position.x += delta * moveSpeed;
                rotateLegs();
                facing = Enums.Facing.RIGHT;
            }
        } else {
            //otherwise, reset
            resetLegs();
        }
        //move away from player
        /*if (position.x >= level.getPlayer().getPosition().x && guardTouchesPlayer()) {
            position.x += delta * moveSpeed;
        }
        if (position.x < level.getPlayer().getPosition().x && guardTouchesPlayer()) {
            position.x -= delta * moveSpeed;
        }*/
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
        boolean inside = position.x > currentCatacomb.position.x + 55 && position.x < currentCatacomb.position.x + currentCatacomb.width - 55;
        //return results to determine if player is within catacomb's bounds
        return inside;
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

    private void lookAround (float delta, float moveSpeed) {

        //set mouth moving speed
        float mouthSpeed = (moveSpeed / 2);

        //if viewport is touched
        if (!targetPosition.equals(new Vector2())) {
            //move eye x
            if (targetPosition.x + 0.5f > position.x && eyeLookLeft.x < 0) {
                eyeLookLeft.x += delta * moveSpeed;
                eyeLookRight.x += delta * moveSpeed;
                //move mouth
                mouthOffset.x += delta * mouthSpeed;
            } else if (targetPosition.x < position.x && eyeLookLeft.x > 0 - (Constants.HEAD_SIZE / 2)) {
                eyeLookLeft.x -= delta * moveSpeed;
                eyeLookRight.x -= delta * moveSpeed;
                //move mouth
                mouthOffset.x -= delta * mouthSpeed;
            }
            //move eye y
            if (targetPosition.y > position.y && eyeLookLeft.y < 0 + (Constants.HEAD_SIZE / 3)) {
                eyeLookLeft.y += delta * moveSpeed;
                eyeLookRight.y += delta * moveSpeed;
                //move mouth
                mouthOffset.y += delta * mouthSpeed;
            } else if (targetPosition.y < position.y && eyeLookLeft.y > 0) {
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

    private void resetLegs () {
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

    private void talk (ShapeRenderer renderer, float talkSpeed) {
        //speed
        mouthFrameTimer += talkSpeed;

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
                guardEnergy += (MathUtils.random(0.01f, 0.05f));
            }
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
            talk(renderer, Constants.MOUTH_TALKING_SPEED);
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
        //Draw Item player is holding
        if (jumpState == Enums.JumpState.GROUNDED) {
            heldItem.render(renderer, level);
        }
    }

}
