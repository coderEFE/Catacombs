package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 8/26/2018.
 * Prisoners other than the player that give player information
 * They have similar clothing to the player
 */

public class Prisoner {
    public Vector2 position;
    private Vector2 velocity;
    public boolean caged;
    public boolean cageUnlocked;
    private float cageSlide;
    public boolean mindWipe;
    private Level level;
    private int prisonerCatacomb;
    private Color CLOTHES_COLOR = Color.GOLDENROD;
    //eye positions
    public Vector2 eyeLookLeft;
    public Vector2 eyeLookRight;
    //mouth offset
    public Vector2 mouthOffset;
    //mouth frames
    public Enums.MouthState mouthState;
    private float mouthFrameTimer;
    //opacity
    private float spawnOpacity;

    //rotate legs
    public long startTime;
    public float legRotate;

    public Enums.Facing facing;

    //rotate arms
    public float armRotate;

    public Prisoner (Vector2 position, Level level) {
        this.position = position;
        velocity = new Vector2();
        caged = false;
        cageUnlocked = false;
        cageSlide = 80;
        mindWipe = false;
        this.level = level;
        prisonerCatacomb = 0;
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
        facing = Enums.Facing.LEFT;
        //initialize other variables
        armRotate = 0;
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

        //stop player from falling below current catacomb if bottom is locked
        if (position.y < level.catacombs.get(prisonerCatacomb).position.y + level.catacombs.get(prisonerCatacomb).wallThickness + 61 && (!level.catacombs.get(prisonerCatacomb).getLockedDoors().get(5).equals("Unlocked") || level.catacombs.get(prisonerCatacomb).bottomsOffset.x > (-1 * (level.catacombs.get(prisonerCatacomb).width - 150))) && insideCatacomb()) {
            velocity.y = 0;
            velocity.x = 0;
            position.y = level.catacombs.get(prisonerCatacomb).position.y + level.catacombs.get(prisonerCatacomb).wallThickness + 61;
        }

        //unlock prisoners cage
        if (!cageUnlocked && caged && level.touchPosition.dst(new Vector2(position.x + (30 - 7), position.y - 15)) < 4 && level.getPlayer().getPosition().x > position.x + 13 && level.getPlayer().getPosition().x < position.x + 33 && level.getPlayer().heldItem.itemType.equals("key")) {
            level.inventory.deleteCurrentItem();
            cageUnlocked = true;
        }
        //move eyes and mouth
        lookAround(delta);
    }
    //look at player
    private void lookAround (float delta) {

        float moveSpeed = 5;
        //set mouth moving speed
        float mouthSpeed = (moveSpeed / 2);

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
    
    //find catacomb nearest to prisoner and assign prisoner to it
    private void findNearestCatacomb () {
        for (Catacomb catacomb : level.catacombs) {
            if (position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) + 50)) < catacomb.width / 2 && position.dst(new Vector2(catacomb.position.x + catacomb.width / 2, (catacomb.position.y + catacomb.height / 2) - 50)) < catacomb.width / 2) {
                prisonerCatacomb = level.catacombs.indexOf(catacomb,true);
            }
        }
    }

    private boolean insideCatacomb () {
        Catacomb currentCatacomb = level.catacombs.get(prisonerCatacomb);
        //check if position.x is more than catacomb's left side
        //return results to determine if player is within catacomb's bounds
        return (position.x > currentCatacomb.position.x + 55 && position.x < currentCatacomb.position.x + currentCatacomb.width - 55);
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

    private void cage (ShapeRenderer renderer) {
        renderer.setColor(Color.DARK_GRAY);
        renderer.rectLine(position.x - 45, position.y + 40, position.x + 45, position.y + 40, 5);
        renderer.rect(position.x - 45, position.y + 40, 7, (level.catacombs.get(prisonerCatacomb).position.y + 15) - (position.y + 40));
        renderer.rect(position.x + 38, position.y + 40, 7, (level.catacombs.get(prisonerCatacomb).position.y + 15) - (position.y + 40));
        //prison bars
        for (int i = 0; i < cageSlide; i += 9) {
            renderer.rectLine((position.x - 40) + i, position.y + 40, (position.x - 40) + i, (level.catacombs.get(prisonerCatacomb).position.y + 15), 3);
        }
        //if cage is not unlocked, draw lock
        if (!cageUnlocked) {
            //door knob
            renderer.setColor(Color.BROWN);
            renderer.circle(position.x + (30 - 7), position.y - 15, 4, 6);
            //key hole
            renderer.setColor(Color.DARK_GRAY);
            renderer.circle(position.x + (30 - 7), position.y - 14, 1, 6);
            renderer.rectLine(position.x + (30 - 7), position.y - 15, position.x + (30 - 7), position.y - 16.5f, 0.5f);
            cageSlide = 80;
        } else {
            if (cageSlide > 0) {
                cageSlide--;
            }
        }
    }

    private void mindWipeMachine (ShapeRenderer renderer) {
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(position.x - 10, position.y + 20, 20, 100);
        renderer.arc(position.x, position.y + 20, 20, 0, 180, 20);
        renderer.setColor(Color.SKY);
        renderer.rect(position.x - 10, position.y + 45, 20, 5);
        renderer.rect(position.x - 10, position.y + 60, 20, 5);
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
        renderer.setColor(level.superior.currentLevel == 13 ? new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity) : new Color(Constants.PRISONER_SKIN_COLOR.r, Constants.PRISONER_SKIN_COLOR.g, Constants.PRISONER_SKIN_COLOR.b, spawnOpacity));
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
        renderer.setColor(new Color(Color.BROWN.r, Color.BROWN.g, Color.BROWN.b, spawnOpacity));
        //renderer.setColor(new Color((1 / 255f) * 70, (1 / 255f) * 47, (1 / 255f) * 19, spawnOpacity));
        //main head
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE, Constants.HEAD_SEGMENTS);
        renderer.setColor(level.superior.currentLevel == 13 ? new Color(Constants.SKIN_COLOR.r, Constants.SKIN_COLOR.g, Constants.SKIN_COLOR.b, spawnOpacity) : new Color(Constants.PRISONER_SKIN_COLOR.r, Constants.PRISONER_SKIN_COLOR.g, Constants.PRISONER_SKIN_COLOR.b, spawnOpacity));
        renderer.circle(position.x, position.y, Constants.HEAD_SIZE * 15 / 16, Constants.HEAD_SEGMENTS);

        //eyes
        renderer.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, spawnOpacity));
        renderer.circle(eyeLookLeft.x + position.x, (eyeLookLeft.y + position.y), Constants.HEAD_SIZE / 10, 4);
        renderer.circle(eyeLookRight.x + position.x, (eyeLookRight.y + position.y), Constants.HEAD_SIZE / 10, 4);

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

        //draw cage around prisoner if caged is true
        if (caged) {
            cage(renderer);
        }
        //if prisoner is going to get mind-wiped, draw mindWipeMachine
        if (mindWipe) {
            mindWipeMachine(renderer);
        }
    }

}
