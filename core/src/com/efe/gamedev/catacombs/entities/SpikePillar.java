package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.efe.gamedev.catacombs.Level;

/**
 * Created by coder on 9/23/2018.
 * SpikePillar that is used in BOSS FIGHT, STAGE TWO
 */

public class SpikePillar {

    private Level level;
    public Vector2 position;
    public boolean strike;
    public boolean hitGround;
    private float spikeSize;
    public float strikeTimer;
    public float strikeFall;
    public int strikeValue;

    public SpikePillar (Vector2 position, Level level) {
        this.position = position;
        this.level = level;
        strike = false;
        hitGround = false;
        spikeSize = 10;
        strikeTimer = 0;
        strikeFall = 0;
        strikeValue = 0;
    }

    public void update (float delta) {
        //keep track of how many spikePillars are striking
        strikeValue = ((strike && !hitGround) ? 1 : 0);
        //start strike sequence
        if (strike) {
            strikeTimer++;
            //make pillar fall before it hits the ground
            if (!hitGround && strikeTimer > 70) {
                if (strikeFall < 170) {
                    strikeFall += (delta * 400);
                } else {
                    hitGround = true;
                    level.gameplayScreen.sound8.play();
                }
            }
            //make pillar go up when it hits the ground
            if (hitGround && strikeTimer > 120) {
                if (strikeFall > 0) {
                    strikeFall -= (delta * 50);
                } else {
                    //reset strikeTimer
                    strikeTimer = 0;
                    //end strike
                    hitGround = false;
                    strike = false;
                }
            }
        }
    }

    public void render (ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        if (strike) {
            //draw spike pillar
            renderer.setColor(new Color((1 / 255f) * 92, (1 / 255f) * 92, (1 / 255f) * 92, 1f));
            renderer.rect(position.x, position.y - strikeFall, 40, strikeFall);
            //draw spikes
            for (int i = 0; i < (40 / spikeSize); i++) {
                renderer.setColor(new Color(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 1f));
                renderer.triangle(position.x + (i * spikeSize), position.y - strikeFall, position.x + spikeSize + (i * spikeSize), position.y - strikeFall, position.x + (spikeSize / 2f) + (i * spikeSize), (position.y - (spikeSize)) - strikeFall);
            }
        }
    }

    public void renderSpikeWarning (ShapeRenderer renderer) {
        //draw warning sign before pillar falls
        float warningOffset = 80;
        if (strike && strikeTimer <= 70) {
            //outer red triangle
            /*renderer.setColor(Color.RED);
            renderer.triangle(position.x + 10, position.y - warningOffset, position.x + 30, position.y - warningOffset, position.x + 20, (position.y + 20) - warningOffset);
            //inner gray triangle
            renderer.setColor(Color.GRAY);
            renderer.triangle(position.x + 13, (position.y - warningOffset) + 1.5f, position.x + 27, (position.y - warningOffset) + 1.5f, position.x + 20, (position.y + 17f) - warningOffset);
            */
            renderer.setColor(Color.RED);
            renderer.rectLine(position.x + 10, position.y - warningOffset, position.x + 30, position.y - warningOffset, 2);
            renderer.rectLine(position.x + 20, (position.y + 20) - warningOffset, position.x + 30, position.y - warningOffset, 2);
            renderer.rectLine(position.x + 10, position.y - warningOffset, position.x + 20, (position.y + 20) - warningOffset, 2);
            //exclamation mark
            //line
            renderer.setColor(Color.RED);
            renderer.rectLine(position.x + 20, (position.y + 14f) - warningOffset, position.x + 20, (position.y + 5.5f) - warningOffset, 2);
            //dot
            renderer.circle(position.x + 20, (position.y + 3.5f) - warningOffset, 1f, 10);
        }
    }

}
