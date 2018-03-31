package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 3/8/2018.
 */

public class Guard {

    private Vector2 position;
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
    private Vector2 eyeLookLeft;
    private Vector2 eyeLookRight;

    //mouth offset
    private Vector2 mouthOffset;
    //mouth frames
    public Enums.MouthState mouthState;
    private float mouthFrameTimer;

    //The item that the player holds
    private Item heldItem;
    private Enums.Facing facing;

    private Level level;

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
        eyeLookLeft = new Vector2(-Constants.EYE_OFFSET,Constants.EYE_OFFSET);
        eyeLookRight = new Vector2(Constants.EYE_OFFSET, Constants.EYE_OFFSET);
        //initialize mouth
        mouthOffset = new Vector2();
        mouthState = Enums.MouthState.NORMAL;
        mouthFrameTimer = 0;
        //initialize facing
        heldItem = new Item(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f), position.y - (Constants.PLAYER_HEIGHT / 1.05f)), level.viewportPosition, "");
        facing = Enums.Facing.LEFT;
    }

    public void holdItem (String itemType) {
        //set initials
        heldItem.collected = true;
        heldItem.itemType = itemType;
        //set directions and render item accordingly
        if (facing == Enums.Facing.LEFT) {
            heldItem.itemFacing = Enums.Facing.LEFT;
            heldItem.position.set(new Vector2(position.x - (Constants.PLAYER_WIDTH * 1.2f) - heldItem.itemWidth + (velocity.x / 40), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 5 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60))));
        } else if (facing == Enums.Facing.RIGHT) {
            heldItem.itemFacing = Enums.Facing.RIGHT;
            heldItem.position.set(new Vector2(position.x + (Constants.PLAYER_WIDTH * 1.2f) + (velocity.x / 40), heldItem.itemType.equals("dagger") ? (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60)) + 5 : (position.y - (Constants.PLAYER_HEIGHT / 1.05f) + (velocity.y / 60))));
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
            heldItem.render(renderer);
        }
    }

}
