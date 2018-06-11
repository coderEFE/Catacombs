package com.efe.gamedev.catacombs.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.entities.Catacomb;
import com.efe.gamedev.catacombs.entities.Laser;
import com.efe.gamedev.catacombs.entities.Player;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 6/2/2018.
 * Stun Gun shoots stun lasers
 */

public class StunGun {

    private Vector2 position;
    public float gunWidth = 6f;
    public Enums.Facing gunFacing;
    public Vector2 shadowOffset;
    public boolean shadow;
    public boolean fire;
    public DelayedRemovalArray<Laser> lasers;
    private Level level;
    public int lasersShot;

    public StunGun (Vector2 position) {
        this.position = position;
        gunFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2(2, 1.5f);
        shadow = true;
        fire = false;
        lasers = new DelayedRemovalArray<Laser>();
        lasersShot = 0;
    }

    public void render (ShapeRenderer renderer, Level level) {
        this.level = level;
        renderer.set(ShapeRenderer.ShapeType.Filled);
        //lasers
        //make player bounds
        Rectangle playerBounds = new Rectangle(level.getPlayer().getPosition().x - Constants.HEAD_SIZE,
                level.getPlayer().getPosition().y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                Constants.PLAYER_WIDTH * 2f,
                Constants.PLAYER_HEIGHT * 2.5f);

        for (int i = 0; i < lasers.size; i++) {
            lasers.get(i).render(renderer);
            lasers.get(i).update();
            if (fire) {
                //lasers collide
                for (int j = 0; j < lasers.size; j++) {
                    if (lasers.get(i).laserBounds().overlaps(lasers.get(j).laserBounds()) && i != j) {
                        lasers.removeIndex(i);
                    }
                }
            }
        }
        //hit player
        for (int i = 0; i < lasers.size; i++) {
            if (lasers.get(i).laserBounds().overlaps(playerBounds) && !level.getPlayer().duck && fire && lasers.size > 0) {
                if (level.getPlayer().health > 0) {
                    level.getPlayer().health -= 2;
                }
                lasers.removeIndex(i);
            }
        }
        //hit wall
        for (int i = 0; i < lasers.size; i++) {
            if (lasers.get(i).position.x < level.catacombs.get(level.currentCatacomb).position.x + 30 && lasers.size > 0) {
                lasers.removeIndex(i);
            }
        }
        if (fire) {
            if (MathUtils.random() < 0.03f) {
                lasers.add(new Laser(new Vector2(position.x + 2, position.y + 3)));
                lasersShot++;
            }
        }
        //make the different directions for stungun
        if (gunFacing == Enums.Facing.LEFT) {
            if (shadow) {
                //SHADOW
                renderer.setColor(Constants.SHADOW_COLOR);
                renderer.rectLine(position.x + 2 + gunWidth + shadowOffset.x, (position.y - 5) + shadowOffset.y, (position.x + 2 + gunWidth + shadowOffset.x), ((position.y - 7) + (gunWidth * 2) + shadowOffset.y) - 5, 3);
                renderer.rectLine((position.x - 2) + shadowOffset.x, (position.y - 10) + (gunWidth * 2) + shadowOffset.y, position.x + 5 + gunWidth + shadowOffset.x, (position.y - 10) + (gunWidth * 2) + shadowOffset.y, 5);
            }
            //ACTUAL
            //draw stungun
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x + 2 + gunWidth, position.y - 5, position.x + 2 + gunWidth, (position.y - 7) + gunWidth * 2, 3);
            renderer.rectLine(position.x - 2, (position.y - 10) + gunWidth * 2, position.x + 5 + gunWidth, (position.y - 10) + gunWidth * 2, 5);
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.rectLine(position.x + 1, (position.y - 10.2f) + gunWidth * 2, position.x + 4 + gunWidth, (position.y - 10.2f) + gunWidth * 2, 2.5f);
        } else if (gunFacing == Enums.Facing.RIGHT) {
            //No shadow for this direction
            //draw stungun
            renderer.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
            renderer.rectLine(position.x, position.y - 5, position.x, (position.y - 7) + gunWidth * 2, 3);
            renderer.rectLine(position.x - 3, (position.y - 10) + gunWidth * 2, (position.x + 5) + gunWidth, (position.y - 10) + gunWidth * 2, 5);
            renderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1));
            renderer.rectLine(position.x - 1, (position.y - 10.2f) + gunWidth * 2, position.x + 2 + gunWidth, (position.y - 10.2f) + gunWidth * 2, 2.5f);
        }
    }
}
