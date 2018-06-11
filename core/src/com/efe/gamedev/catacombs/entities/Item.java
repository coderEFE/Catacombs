package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.items.Dagger;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.items.Gold;
import com.efe.gamedev.catacombs.items.Key;
import com.efe.gamedev.catacombs.items.Phone;
import com.efe.gamedev.catacombs.items.StunGun;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/24/2017.
 */

public class Item {

    public Vector2 position;
    public Vector2 viewportPosition;
    public String itemType;
    public float itemWidth;
    public float itemHeight;
    public boolean collected;
    public Enums.Facing itemFacing;
    public Vector2 shadowOffset;
    public boolean targeted;
    private float itemSlide;

    private Key key;
    private Diamond diamond;
    private Dagger dagger;
    private Gold gold;
    private Phone phone;
    public StunGun stungun;

    private long startTime;
    private static final float TARGET_MOVEMENT_DISTANCE = 1.5f;
    private static final float TARGET_PERIOD = 1.5f;

    public Item (Vector2 position, Vector2 viewportPosition, String itemType) {
        this.position = position;
        this.viewportPosition = viewportPosition;
        this.itemType = itemType;
        itemWidth = 0;
        itemHeight = 0;
        collected = false;
        itemFacing = Enums.Facing.LEFT;
        shadowOffset = new Vector2();
        targeted = false;
        itemSlide = 0;
        key = new Key(position);
        diamond = new Diamond(position);
        dagger = new Dagger(position);
        gold = new Gold(position);
        phone = new Phone(position);
        stungun = new StunGun(position);
        startTime = TimeUtils.nanoTime();
    }

    public void render (ShapeRenderer renderer, Level level) {
        //if item is key, draw a key
        if (itemType.equals("key")) {
            //set initials
            itemWidth = key.keyWidth;
            itemHeight = 9;
            //set item direction
            key.keyFacing = itemFacing;
            //shadow
            key.shadow = !collected;
            key.shadowOffset = shadowOffset;
            //render key
            key.render(renderer);
        } else if (itemType.equals("diamond")) {
            //if item is diamond, draw a diamond
            //set initials
            itemWidth = diamond.diamondWidth;
            itemHeight = 8;
            //shadow
            diamond.shadowOffset = shadowOffset;
            //render diamond
            diamond.render(renderer);
        } else if (itemType.equals("dagger")) {
            //if item is dagger, draw a dagger
            //set initials
            itemWidth = dagger.daggerWidth;
            itemHeight = 8;
            //set item direction
            dagger.daggerFacing = itemFacing;
            //shadow
            dagger.shadow = !collected;
            dagger.shadowOffset = shadowOffset;
            //render dagger
            dagger.render(renderer);
        } else if (itemType.equals("gold")) {
            //if item is gold, draw gold bars
            //set initials
            itemWidth = gold.goldWidth;
            itemHeight = 8;
            //shadow
            gold.shadow = !collected;
            gold.shadowOffset = shadowOffset;
            //render gold
            gold.render(renderer);
        } else if (itemType.equals("phone")) {
            //if item is phone, draw phone
            //set initials
            itemWidth = phone.phoneWidth;
            itemHeight = 8;
            //shadow
            phone.shadow = !collected;
            phone.shadowOffset = shadowOffset;
            //render gold
            phone.render(renderer);
        } else if (itemType.equals("stungun")) {
            //set initials
            itemWidth = stungun.gunWidth;
            itemHeight = 9;
            //set item direction
            stungun.gunFacing = itemFacing;
            //shadow
            stungun.shadow = !collected;
            stungun.shadowOffset = shadowOffset;
            //render StunGun
            stungun.render(renderer, level);
        }

            if (!targeted) {
                startTime = TimeUtils.nanoTime();
            }
            if (targeted) {
                updateBox();
            }

            //TODO: Maybe remove this box
        //if player is touching within the item's bounds, draw a target box around item.
        if ((viewportPosition.x > position.x && viewportPosition.x < position.x + itemWidth && viewportPosition.y < position.y + 2 && viewportPosition.y > position.y - 5 && !collected) || targeted) {
            renderer.setColor(Constants.TARGET_COLOR);
            //top line
            renderer.rectLine((position.x - (itemWidth / 3f)) - itemSlide, position.y + 4 + itemSlide, position.x + (itemWidth * 1.6f) + itemSlide, position.y + 4 + itemSlide, 1);
            //bottom line
            renderer.rectLine((position.x - (itemWidth / 3f)) - itemSlide, (position.y - 5) - itemSlide, position.x + (itemWidth * 1.6f) + itemSlide, (position.y - 5) - itemSlide, 1);
            //left line
            renderer.rectLine((position.x - (itemWidth / 3f)) - itemSlide, position.y + 4 + itemSlide, (position.x - (itemWidth / 3f)) - itemSlide, (position.y - 5) - itemSlide, 1);
            //right line
            renderer.rectLine(position.x + (itemWidth * 1.6f) + itemSlide, position.y + 4 + itemSlide, position.x + (itemWidth * 1.6f) + itemSlide, (position.y - 5) - itemSlide, 1);
            //target lines
            renderer.rectLine((position.x - (itemWidth / 3f)) - itemSlide, position.y - 1, (position.x - 8), position.y - 1, 1);
            renderer.rectLine(position.x + (itemWidth * 1.6f) + itemSlide, position.y - 1, position.x + (itemWidth * 1.6f) + 4, position.y - 1, 1);
            renderer.rectLine(position.x + (itemWidth / 2f) + 1, position.y + 4 + itemSlide, position.x + (itemWidth / 2f) + 1, position.y + 8, 1);
            renderer.rectLine(position.x + (itemWidth / 2f) + 1, (position.y - 5) - itemSlide, position.x + (itemWidth / 2f) + 1, position.y - 9, 1);
        }
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
        itemSlide = 0f + TARGET_MOVEMENT_DISTANCE * MathUtils.sin(MathUtils.PI2 * cyclePosition);
    }

}
