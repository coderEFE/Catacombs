package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by coder on 1/24/2018.
 * This is the target box which is used in the tutorial levels to introduce new things.
 */

public class Target {
    public Vector2 position;
    public boolean target;
    public float itemWidth;
    public boolean isArrow;
    public boolean left;

    //target variables
    private float targetSlide;
    public long startTime;
    private static final float TARGET_MOVEMENT_DISTANCE = 1.5f;
    private static final float TARGET_PERIOD = 1.5f;

    public Target () {
        position = new Vector2();
        target = false;
        itemWidth = 5;
        isArrow = false;
        left = false;
        targetSlide = 0f;
        startTime = TimeUtils.nanoTime();
    }

    public void update () {
        //update target box
        updateBox();
    }

    ///make target box update
    private void updateBox () {
        //Figure out how long it's been since the animation started using TimeUtils.nanoTime()
        long elapsedNanos = TimeUtils.nanoTime() - startTime;
        //Use MathUtils.nanoToSec to figure out how many seconds the animation has been running
        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanos;
        //Figure out how many cycles have elapsed since the animation started running
        float cycles = elapsedSeconds / TARGET_PERIOD;
        //Figure out where in the cycle we are
        float cyclePosition = cycles % 1;
        //make item box move
        targetSlide = 0f + TARGET_MOVEMENT_DISTANCE * MathUtils.sin(MathUtils.PI2 * cyclePosition);
    }

    //render target box
    public void render (ShapeRenderer renderer) {
        if (!isArrow) {
            Vector2 targetOffset = new Vector2(0, 0);
            renderer.setColor(new Color(0.5f, 0, 0.2f, 1));
            //top line
            renderer.rectLine((position.x - (itemWidth / 3f)) - targetOffset.x - targetSlide, position.y + 4 + targetSlide - targetOffset.y, position.x + (itemWidth * 1.6f) + targetSlide - targetOffset.x, position.y + 4 + targetSlide - targetOffset.y, 1);
            //bottom line
            renderer.rectLine((position.x - (itemWidth / 3f)) - targetOffset.x - targetSlide, (position.y - 5) - targetSlide - targetOffset.y, position.x + (itemWidth * 1.6f) + targetSlide - targetOffset.x, (position.y - 5) - targetSlide - targetOffset.y, 1);
            //left line
            renderer.rectLine((position.x - (itemWidth / 3f)) - targetOffset.x - targetSlide, position.y + 4 + targetSlide - targetOffset.y, (position.x - (itemWidth / 3f)) - targetSlide - targetOffset.x, (position.y - 5) - targetSlide - targetOffset.y, 1);
            //right line
            renderer.rectLine(position.x + (itemWidth * 1.6f) + targetSlide - targetOffset.x, position.y + 4 + targetSlide - targetOffset.y, position.x + (itemWidth * 1.6f) + targetSlide - targetOffset.x, (position.y - 5) - targetSlide - targetOffset.y, 1);
            //target lines
            renderer.rectLine((position.x - (5 / 4f)) - targetSlide - targetOffset.x, position.y - 1 - targetOffset.y, (position.x - 6) - targetOffset.x, position.y - 1 - targetOffset.y, 1);
            renderer.rectLine(position.x + (itemWidth * 1.6f) + targetSlide - targetOffset.x, position.y - 1 - targetOffset.y, position.x + (itemWidth * 1.6f) + 4 - targetOffset.x, position.y - 1 - targetOffset.y, 1);
            renderer.rectLine(position.x + (itemWidth / 2f) + 1 - targetOffset.x, position.y + 4 + targetSlide - targetOffset.y, position.x + (itemWidth / 2f) + 1 - targetOffset.x, position.y + 8 - targetOffset.y, 1);
            renderer.rectLine(position.x + (itemWidth / 2f) + 1 - targetOffset.x, (position.y - 5) - targetSlide - targetOffset.y, position.x + (itemWidth / 2f) + 1 - targetOffset.x, position.y - 9 - targetOffset.y, 1);
        } else {
            renderer.setColor(Color.BLUE);
            if (!left) {
                renderer.rect(position.x + targetSlide, position.y, itemWidth * 2, itemWidth * 2);
                renderer.triangle(position.x + (itemWidth * 2) + targetSlide, position.y - (itemWidth / 2), position.x + (itemWidth * 2) + targetSlide, position.y + (itemWidth * 2) + (itemWidth / 2), position.x + (itemWidth * 3.2f) + targetSlide, position.y + itemWidth);
            } else {
                renderer.rect(position.x - targetSlide, position.y, itemWidth * 2, itemWidth * 2);
                renderer.triangle(position.x - targetSlide, position.y - (itemWidth / 2), position.x - targetSlide, position.y + (itemWidth * 2) + (itemWidth / 2), (position.x - (itemWidth * 1.2f)) - targetSlide, position.y + itemWidth);
            }
        }
    }
}
