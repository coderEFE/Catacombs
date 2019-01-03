package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 9/16/2018.
 * This is the Boss used in the last level with the boss fight
 */

public class Boss {
    public Vector2 position;
    private Vector2 velocity;
    private Level level;
    private Color CLOTHES_COLOR = Color.DARK_GRAY;
    //eye positions
    public Vector2 eyeLookLeft;
    public Vector2 eyeLookRight;
    //mouth offset
    public Vector2 mouthOffset;
    //mouth frames
    public Enums.MouthState mouthState;
    private float mouthFrameTimer;
    //opacity
    public float spawnOpacity;

    //boss jumpState
    public Enums.BossState bossState;
    public Enums.JumpState jumpState;
    public Vector2 targetPosition;
    public float moveSpeed;

    //rotate legs
    public long startTime;
    public float legRotate;
    private static final float LEG_MOVEMENT_DISTANCE = 20;
    private static final float LEG_PERIOD = 1.5f;

    //The item that the boss holds
    public Item heldItem;
    public String bossItem;
    public Enums.Facing facing;

    //rotate arms
    public float armRotate;
    //boss fight variables
    //guns for lasers
    public String laserShaftStage;
    public float laserShaftY;
    private float duelGunsFirst;
    private float duelGunsSecond;
    public float duelGunsThird;
    //lasers
    public boolean shootLasers;
    public DelayedRemovalArray<Laser> lasers;
    private float laserTimer;
    public int lasersShot;
    //spike pillars
    public boolean useSpikePillars;
    public float spikePillarTimer;
    private Array<SpikePillar> spikePillars;
    //fight with player
    public float health;
    public boolean danger;
    private float strikeTimer;
    public boolean useEnergy;
    public float energy;
    private float smashTimer;
    public boolean alert;

    public Boss (Vector2 position, Level level) {
        this.position = position;
        this.level = level;
        //get velocity and start boss off as falling
        velocity = new Vector2();
        bossState = Enums.BossState.NORMAL;
        jumpState = Enums.JumpState.FALLING;
        spawnOpacity = 1;
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
        //initialize facing
        heldItem = new Item(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f), position.y - (Constants.PLAYER_HEIGHT / 1.05f)), level.viewportPosition, "");
        facing = Enums.Facing.LEFT;
        bossItem = "";
        //initialize other variables
        armRotate = 0;
        //laser variables
        laserShaftStage = "DORMANT";
        laserShaftY = 0;
        duelGunsFirst = 0;
        duelGunsSecond = 0;
        duelGunsThird = 0;
        shootLasers = false;
        laserTimer = 0;
        lasersShot = 0;
        lasers = new DelayedRemovalArray<>();
        //spike pillars
        useSpikePillars = false;
        spikePillarTimer = 0;
        spikePillars = new Array<>();
        spikePillars.add(new SpikePillar(new Vector2(-100, 100), level));
        spikePillars.add(new SpikePillar(new Vector2(-60, 100), level));
        spikePillars.add(new SpikePillar(new Vector2(-20, 100), level));
        spikePillars.add(new SpikePillar(new Vector2(20, 100), level));
        spikePillars.add(new SpikePillar(new Vector2(60, 100), level));
        //fight variables
        health = 20;
        danger = false;
        strikeTimer = 0;
        useEnergy = false;
        energy = 20;
        smashTimer = 0;
        alert = false;
        //target location
        targetPosition = new Vector2();
        moveSpeed = 20;
    }

    public void update (float delta) {
        //add velocity and gravity. Add less gravity when going into the next catacomb
        if (insideCatacomb()) {
            velocity.y -= Constants.GRAVITY;
        } else {
            velocity.y -= Constants.GRAVITY / 4f;
        }
        position.mulAdd(velocity, delta);

        //stop boss from falling below current catacomb if bottom is locked
        if (position.y < level.catacombs.get(0).position.y + level.catacombs.get(0).wallThickness + 61 && (!level.catacombs.get(0).getLockedDoors().get(5).equals("Unlocked") || level.catacombs.get(0).bottomsOffset.x > (-1 * (level.catacombs.get(0).width - 150))) && insideCatacomb()) {
            velocity.y = 0;
            velocity.x = 0;
            position.y = level.catacombs.get(0).position.y + level.catacombs.get(0).wallThickness + 61;
            jumpState = Enums.JumpState.GROUNDED;
        }

        //move boss
        if (!targetPosition.equals(new Vector2()) && (insideCatacomb() || outsideCatacombLeft() || outsideCatacombRight())) {
            moveBoss(delta, moveSpeed, targetPosition);
        }

        /*LASERS STAGE - STAGE 1*/
        //move and setup laserShaft
        if (laserShaftStage.equals("CONSTRUCTED") && laserShaftY < 57) {
            laserShaftY ++;
        }
        if (laserShaftStage.equals("CONSTRUCTED") && laserShaftY > 55) {
            if (duelGunsFirst <= 10) {
                duelGunsFirst+=0.5f;
            }
            if (duelGunsFirst >= 9.5f && duelGunsSecond <= 10) {
                duelGunsSecond+=0.5f;
            }
            if (duelGunsSecond >= 9.5f && duelGunsThird <= 10) {
                duelGunsThird+=0.5f;
            }
        }
        //deconstruct laser shaft
        if (laserShaftStage.equals("DECONSTRUCTED")) {
            if (duelGunsThird > 0) {
                duelGunsThird-=0.5f;
            }
            if (duelGunsSecond > 0 && duelGunsThird <= 0.5f) {
                duelGunsSecond-=0.5f;
            }
            if (duelGunsFirst > 0 && duelGunsSecond <= 0.5f) {
                duelGunsFirst-=0.5f;
            }
            if (laserShaftY > 0 && duelGunsFirst <= 0.5f) {
                laserShaftY --;
            }
        }
        //make player bounds
        Rectangle playerBounds = new Rectangle(level.getPlayer().getPosition().x - Constants.HEAD_SIZE,
                level.getPlayer().getPosition().y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                Constants.PLAYER_WIDTH * 2f,
                Constants.PLAYER_HEIGHT * 2.5f);
        //fire lasers at intervals from random guns
        if (shootLasers) {
            laserTimer++;
            if (laserTimer > 70) {
                lasers.add(new Laser(new Vector2(position.x + 60, MathUtils.random(2) >= 1.2f ? position.y : position.y - 40), true, Enums.Facing.RIGHT));
                lasersShot++;
                level.gameplayScreen.sound7.play();
                laserTimer = 0;
            }
        } else {
            laserTimer = 0;
        }
        if (shootLasers && lasers.size > 0) {
            //lasers hit player
            for (int i = 0; i < lasers.size; i++) {
                if (lasers.get(i).laserBounds().overlaps(playerBounds) && ((lasers.get(i).position.y == position.y && !level.getPlayer().duck) || (lasers.get(i).position.y == position.y - 40 && (level.getPlayer().position.y < level.catacombs.get(level.currentCatacomb).position.y + level.catacombs.get(level.currentCatacomb).wallThickness + 64))) && lasers.size > 0 && lasers.get(i).enemyLaser) {
                    if (level.getPlayer().health > 0) {
                        level.getPlayer().health -= 2;
                        // Vibrate device for 400 milliseconds
                        Gdx.input.vibrate(400);
                    }
                    lasers.removeIndex(i);
                }
            }
            //lasers hit wall
            for (int i = 0; i < lasers.size; i++) {
                if (lasers.get(i).position.y == position.y) {
                    if (lasers.get(i).position.x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width - 25) {
                        lasers.removeIndex(i);
                    }
                } else {
                    if (lasers.get(i).position.x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width - 50) {
                        lasers.removeIndex(i);
                    }
                }
            }
            //update lasers
            for (int i = 0; i < lasers.size; i++) {
                lasers.get(i).update();
            }
        }

        /*SPIKE-PILLAR STAGE - STAGE 2*/
        //update spikePillars
        if (useSpikePillars) {
            spikePillarTimer ++;
            for (SpikePillar spikePillar : spikePillars) {
                spikePillar.update(delta);
            }
            //shake level when pillars hit ground
            level.shake = (spikePillars.get(0).hitGround && spikePillars.get(0).strikeTimer < 115) || (spikePillars.get(1).hitGround && spikePillars.get(1).strikeTimer < 115) || (spikePillars.get(2).hitGround && spikePillars.get(2).strikeTimer < 115) || (spikePillars.get(3).hitGround && spikePillars.get(3).strikeTimer < 115) || (spikePillars.get(4).hitGround && spikePillars.get(4).strikeTimer < 115);
            level.getPlayer().mouthState = (level.shake ? Enums.MouthState.OPEN : Enums.MouthState.NORMAL);

            //keep track of how many spikePillars are striking
            int strikeNum = (spikePillars.get(0).strikeValue + spikePillars.get(1).strikeValue + spikePillars.get(2).strikeValue + spikePillars.get(3).strikeValue + spikePillars.get(4).strikeValue);

            //randomize spikePillar falling
            if (MathUtils.random(5) > 4.5f) {
                if (!spikePillars.get(MathUtils.floor(MathUtils.random(0, 4))).strike && strikeNum < 3) {
                    spikePillars.get(MathUtils.floor(MathUtils.random(0, 4))).strike = true;
                }
            }
            //prevent player from going through pillars
            playerTouchSpikePillar();
        } else {
            //reset pillars
            spikePillarTimer = 0;
            for (SpikePillar spikePillar : spikePillars) {
                if (spikePillar.strike) {
                    if (spikePillar.strikeFall > 0) {
                        spikePillar.strikeFall -= (delta * 50);
                    } else {
                        //reset strikeTimer
                        spikePillar.strikeTimer = 0;
                        //end strike
                        spikePillar.hitGround = false;
                        spikePillar.strike = false;
                    }
                }
            }
        }

        /*FIGHT STAGE - STAGE 3*/
        if (level.currentBubble > 50) {
            //change boss's direction to face the player
            if (position.x < level.getPlayer().getPosition().x) {
                facing = Enums.Facing.RIGHT;
            } else {
                facing = Enums.Facing.LEFT;
            }
        }
        //stalk player
        if (bossState == Enums.BossState.HUNT) {
            //move boss close to player
            //boss on left side of player
            if (position.x < level.getPlayer().getPosition().x) {
                targetPosition.set(new Vector2(level.getPlayer().getPosition().x - (Constants.PLAYER_WIDTH * 4), level.getPlayer().getPosition().y));
            }
            //boss on right side of player
            if (position.x > level.getPlayer().getPosition().x) {
                targetPosition.set(new Vector2(level.getPlayer().getPosition().x + (Constants.PLAYER_WIDTH * 4), level.getPlayer().getPosition().y));
            }
            if (position.x > targetPosition.x - 1f && position.x < targetPosition.x + 1f && legRotate > 168 && legRotate < 172) {
                targetPosition.set(new Vector2());
                resetLegs();
                bossState = Enums.BossState.ATTACK;
            }
        }
        //attack player when in position
        if (bossState == Enums.BossState.ATTACK) {
            strikeTimer ++;
            //sword strike animation
            if (strikeTimer >= 0 && strikeTimer < 20) {
                heldItem.spear.strikeStatus = "BLOCK";
            } else if (strikeTimer >= 20 && strikeTimer < 25) {
                //damage player
                if (position.dst(level.getPlayer().getPosition()) < 40 && level.getPlayer().health > 0) {
                    level.getPlayer().health -= (2.5f / 5f);
                    level.getPlayer().mouthState = Enums.MouthState.OPEN;
                    // Vibrate device for 400 milliseconds
                    Gdx.input.vibrate(400);
                }
                heldItem.spear.strikeStatus = "PARRY";
            } else if (strikeTimer >= 25 && strikeTimer < 80) {
                heldItem.spear.strikeStatus = "THRUST";
            } else if (strikeTimer >= 80 && strikeTimer < 85) {
                heldItem.spear.strikeStatus = "PARRY";
            } else if (strikeTimer >= 85 && strikeTimer < 105) {
                heldItem.spear.strikeStatus = "BLOCK";
            } else {
                //reset strike animation
                strikeTimer = 0;
                if (position.dst(level.getPlayer().getPosition()) > 50) {
                    bossState = Enums.BossState.HUNT;
                }
                //change boss's tactic when its health gets low
                if (health <= 10) {
                    bossState = Enums.BossState.SMASH;
                }
            }
        }
        //when boss is in SMASH mode, it will try to jump onto player
        if (bossState == Enums.BossState.SMASH) {
            useEnergy = true;
            smashTimer ++;
            //start off with spear in BLOCK mode
            if (smashTimer >= 0 && smashTimer < 30) {
                heldItem.spear.strikeStatus = "BLOCK";
            } else if (smashTimer >= 30 && smashTimer < 31) {
                //make boss jump high into the air, targeting player
                velocity.set((level.getPlayer().getPosition().x - position.x), 300);
                jumpState = Enums.JumpState.JUMPING;
                energy = 0;
            } else if (smashTimer >= 31 && jumpState == Enums.JumpState.GROUNDED) {
                //smash spear into ground
                heldItem.spear.strikeStatus = "THRUST";
            }
            //shake screen
            if (smashTimer >= 90 && smashTimer < 95) {
                level.shake = true;
                //damage player
                if (position.dst(level.getPlayer().getPosition()) < 40 && smashTimer < 91 && level.getPlayer().health > 0) {
                    level.getPlayer().health -= (5f);
                    level.getPlayer().mouthState = Enums.MouthState.OPEN;
                    // Vibrate device for 500 milliseconds
                    Gdx.input.vibrate(500);
                }
            } else if (smashTimer >= 95) {
                level.shake = false;
            }
            //reset energy and timer
            if (smashTimer > 90 && energy < 20) {
                energy += 0.1f;
            }
            if (smashTimer > 110 && energy > 19) {
                smashTimer = 0;
                energy = 20;
            }
        }

        //update held item position
        if (!bossItem.equals("")) {
            holdItem(bossItem);
        } else {
            heldItem.itemType = "";
        }

        //move eyes and mouth
        lookAround(delta);
    }

    public boolean playerHitBySpikePillar () {
        boolean playerHit = false;
        for (SpikePillar spikePillar : spikePillars) {
            if (spikePillar.strikeFall >= 150 && level.getPlayer().getPosition().x >= spikePillar.position.x && level.getPlayer().getPosition().x < spikePillar.position.x + 40) {
                playerHit = true;
            }
        }
        return playerHit;
    }

    //stop player from passing through spikePillars when they are down
    private void playerTouchSpikePillar () {
        for (SpikePillar spikePillar : spikePillars) {
            if (spikePillar.strikeFall >= 140 && level.getPlayer().getPosition().x + Constants.HEAD_SIZE >= spikePillar.position.x && level.getPlayer().getPosition().x < spikePillar.position.x + 20) {
                level.viewportPosition.x = spikePillar.position.x - Constants.HEAD_SIZE;
            }
            if (spikePillar.strikeFall >= 140 && level.getPlayer().getPosition().x - Constants.HEAD_SIZE <= spikePillar.position.x + 40 && level.getPlayer().getPosition().x > spikePillar.position.x + 20) {
                level.viewportPosition.x = spikePillar.position.x + 40 + Constants.HEAD_SIZE;
            }
        }
    }

    private void moveBoss (float delta, float moveSpeed, Vector2 movePosition) {
        //move boss when a move position is entered
        if ((!(position.x < movePosition.x + 1) || !(position.x > movePosition.x)) && !movePosition.equals(new Vector2())) {
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
    }

    public void resetLegs () {
        //if boss is on ground and resetLegs() is called, this will reset boss's legs.
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

    //look at player
    private void lookAround (float delta) {

        float moveSpeed = 5;
        //set mouth moving speed
        float mouthSpeed = (5 / 2);

        //if viewport is touched
        if (!level.getPlayer().getPosition().equals(new Vector2())) {
            //move eye x
            if (level.getPlayer().getPosition().x + 0.5f > position.x && eyeLookLeft.x < 0) {
                eyeLookLeft.x += delta * moveSpeed;
                eyeLookRight.x += delta * moveSpeed;
                //move mouth
                mouthOffset.x += delta * mouthSpeed;
            } else if (level.getPlayer().getPosition().x < position.x && eyeLookLeft.x > 0 - (Constants.HEAD_SIZE / 2)) {
                eyeLookLeft.x -= delta * moveSpeed;
                eyeLookRight.x -= delta * moveSpeed;
                //move mouth
                mouthOffset.x -= delta * mouthSpeed;
            }
            //move eye y
            if (level.getPlayer().getPosition().y > position.y && eyeLookLeft.y < 0 + (Constants.HEAD_SIZE / 3)) {
                eyeLookLeft.y += delta * moveSpeed;
                eyeLookRight.y += delta * moveSpeed;
                //move mouth
                mouthOffset.y += delta * mouthSpeed;
            } else if (level.getPlayer().getPosition().y < position.y && eyeLookLeft.y > 0) {
                eyeLookLeft.y -= delta * moveSpeed;
                eyeLookRight.y -= delta * moveSpeed;
                //move mouth
                mouthOffset.y -= delta * mouthSpeed;
            }
        }
    }

    private boolean insideCatacomb () {
        //since the boss is always in catacombs index 0, we can always use the 0 index for the currentCatacomb
        Catacomb currentCatacomb = level.catacombs.get(0);
        //check if position.x is more than catacomb's left side
        //return results to determine if player is within catacomb's bounds
        return (position.x > currentCatacomb.position.x + 55 && position.x < currentCatacomb.position.x + currentCatacomb.width - 55);
    }

    private boolean outsideCatacombLeft () {
        Catacomb currentCatacomb = level.catacombs.get(0);
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
        Catacomb currentCatacomb = level.catacombs.get(0);
        //check if position.x is less than catacomb's right side
        boolean outsideWidth = position.x >= currentCatacomb.position.x + currentCatacomb.width - 55;
        if (outsideWidth && (currentCatacomb.getLockedDoors().get(4).equals("Locked") || currentCatacomb.getLockedDoors().get(4).equals("DoubleLocked") || currentCatacomb.getLockedDoors().get(4).equals("Closed")) && jumpState != Enums.JumpState.JUMPING) {
            targetPosition.x = currentCatacomb.position.x + currentCatacomb.width - 58f;
            resetLegs();
        }
        //return results to determine if guard is outside catacomb's bounds
        return outsideWidth;
    }

    private void talk (ShapeRenderer renderer) {
        //speed
        mouthFrameTimer += Constants.MOUTH_TALKING_SPEED;

        //talk frames to make mouth move
        if (mouthFrameTimer >= 0 && mouthFrameTimer < 50) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) , Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 5, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 50 && mouthFrameTimer < 100) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) , Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 100 && mouthFrameTimer < 150) {
            renderer.arc(position.x + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) , Constants.HEAD_SIZE / 4, 180, 180);
        } else if (mouthFrameTimer >= 150 && mouthFrameTimer < 200) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) , Constants.HEAD_SIZE / 4, 170, 170);
        } else if (mouthFrameTimer >= 200 && mouthFrameTimer < 250) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) , Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 250 && mouthFrameTimer < 300) {
            renderer.circle(position.x + mouthOffset.x, (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) , Constants.HEAD_SIZE / 6, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 300 && mouthFrameTimer < 350) {
            renderer.arc(position.x + (mouthOffset.x * 1.1f), (position.y - (Constants.HEAD_SIZE / 3) + mouthOffset.y) , Constants.HEAD_SIZE / 4, 175, 175);
        } else if (mouthFrameTimer >= 350 && mouthFrameTimer < 400) {
            renderer.ellipse(position.x + mouthOffset.x - (Constants.HEAD_SIZE / 8), (position.y - (Constants.HEAD_SIZE / 2) + mouthOffset.y) , Constants.HEAD_SIZE / 4, Constants.HEAD_SIZE / 4, Constants.HEAD_SEGMENTS);
        } else if (mouthFrameTimer >= 400) {
            mouthFrameTimer = 0;
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

    private void laserShaft (ShapeRenderer renderer) {
        if (shootLasers && lasers.size > 0) {
            //render lasers
            for (Laser laser : lasers) {
                laser.render(renderer);
            }
        }
        //laser guns
        renderer.setColor(Color.DARK_GRAY);
        renderer.rectLine(position.x + 40, (position.y), position.x + 40 + duelGunsFirst, (position.y), 7);
        renderer.rectLine(position.x + 50, (position.y), position.x + 50 + duelGunsSecond, (position.y), 5);
        renderer.rectLine(position.x + 60, (position.y), position.x + 60 + duelGunsThird, (position.y), 3);
        renderer.rectLine(position.x + 40, (position.y - 40), position.x + 40 + duelGunsFirst, (position.y - 40), 7);
        renderer.rectLine(position.x + 50, (position.y - 40), position.x + 50 + duelGunsSecond, (position.y - 40), 5);
        renderer.rectLine(position.x + 60, (position.y - 40), position.x + 60 + duelGunsThird, (position.y - 40), 3);
        //laser shaft
        renderer.setColor(Color.DARK_GRAY);
        renderer.rectLine(position.x + 40, (position.y - 50), position.x + 40, (position.y - 50) + laserShaftY, 5);
    }

    public void render (ShapeRenderer renderer) {
        //ShapeType
        renderer.set(ShapeRenderer.ShapeType.Filled);

        //ARMS
        //arms
        renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x, position.y - Constants.HEAD_SIZE, 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, 150f - armRotate);
        renderer.rect(position.x + (Constants.HEAD_SIZE / 2) - (armRotate / 15f), position.y - (Constants.HEAD_SIZE / 1.2f) + (armRotate / 20f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT / 1.5f, 1, 1, -150f + armRotate);

        //hands
        renderer.setColor(new Color(Constants.BOSS_SKIN_COLOR.r, Constants.BOSS_SKIN_COLOR.g, Constants.BOSS_SKIN_COLOR.b, spawnOpacity));
        renderer.circle(position.x - (Constants.PLAYER_WIDTH * 1.2f) - (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);
        renderer.circle(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (armRotate / 10f), position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (armRotate / 4f), Constants.HAND_SIZE, Constants.HEAD_SEGMENTS);

        //LEGS
        //legs
        renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x, position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, legRotate);
        renderer.rect(position.x + (Constants.PLAYER_WIDTH / 2), position.y - (Constants.PLAYER_HEIGHT * 1f), 0, 0, Constants.PLAYER_WIDTH / 2, Constants.PLAYER_HEIGHT * 1.2f, 1, 1, -legRotate);

        //BODY
        renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
        renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.3f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);

        //belt
        renderer.setColor(new Color(0.2f, 0.2f, 0.2f, spawnOpacity));
        renderer.rect(position.x - (Constants.HEAD_SIZE / 2), position.y - (Constants.PLAYER_HEIGHT * 1.2f), Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT / 10);

        //HEAD
        //general shape and outline
        renderer.setColor(new Color(CLOTHES_COLOR.r, CLOTHES_COLOR.g, CLOTHES_COLOR.b, spawnOpacity));
        //main head
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE, Constants.HEAD_SEGMENTS);
        renderer.setColor(new Color(Constants.BOSS_SKIN_COLOR.r, Constants.BOSS_SKIN_COLOR.g, Constants.BOSS_SKIN_COLOR.b, spawnOpacity));
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE * 15 / 16, Constants.HEAD_SEGMENTS);

        //eyes
        renderer.setColor(new Color(Color.RED.r, Color.RED.g, Color.RED.b, spawnOpacity));
        renderer.circle(position.x - 6, position.y + 2,Constants.HEAD_SIZE / 10, 4);
        renderer.circle(position.x + 6, position.y + 2, Constants.HEAD_SIZE / 10, 4);
        renderer.rectLine(position.x - 6, position.y + 2, position.x + 6, position.y + 2, Constants.HEAD_SIZE / 4f);

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

        //if alert, show exclamation mark over head
        if (alert) {
            renderer.setColor(Color.WHITE);
            renderer.circle(position.x, position.y + Constants.HEAD_SIZE * 1.5f, Constants.HEAD_SIZE / 4, 10);
            renderer.rectLine(position.x, position.y + (Constants.HEAD_SIZE * 2), position.x, position.y + Constants.HEAD_SIZE * 3, Constants.HEAD_SIZE / 6);
        }

        //draw lasers for first BOSS STAGE
        if (!laserShaftStage.equals("DORMANT")) {
            laserShaft(renderer);
        }
        //draw spikePillars for second BOSS STAGE
            for (SpikePillar spikePillar : spikePillars) {
                spikePillar.render(renderer);
            }
        //draw boss's item
        heldItem.render(renderer, level);
    }

    public void renderSpikeWarning (ShapeRenderer renderer) {
        for (SpikePillar spikePillar : spikePillars) {
            spikePillar.renderSpikeWarning(renderer);
        }
    }

}
