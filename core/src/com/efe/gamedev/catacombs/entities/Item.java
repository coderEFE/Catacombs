package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.efe.gamedev.catacombs.Level;
import com.efe.gamedev.catacombs.items.Bomb;
import com.efe.gamedev.catacombs.items.Dagger;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.items.Disguise;
import com.efe.gamedev.catacombs.items.Fire;
import com.efe.gamedev.catacombs.items.Gem;
import com.efe.gamedev.catacombs.items.Gold;
import com.efe.gamedev.catacombs.items.Key;
import com.efe.gamedev.catacombs.items.Phone;
import com.efe.gamedev.catacombs.items.Potion;
import com.efe.gamedev.catacombs.items.Shield;
import com.efe.gamedev.catacombs.items.Spear;
import com.efe.gamedev.catacombs.items.StunGun;
import com.efe.gamedev.catacombs.util.Constants;
import com.efe.gamedev.catacombs.util.Enums;

/**
 * Created by coder on 11/24/2017.
 * This is the Item class which keeps track of all the different kinds of items and can be called to generate an item.
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

    //different types of items
    private Key key;
    private Diamond diamond;
    private Dagger dagger;
    private Gold gold;
    private Phone phone;
    //stungun lasers
    public StunGun stungun;
    public DelayedRemovalArray<Laser> lasers;
    //more items
    private Gem gem;
    public Potion potion;
    private Shield shield;
    public Bomb bomb;
    private Fire fire;
    private Disguise disguise;
    public Spear spear;

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
        gem = new Gem(position);
        potion = new Potion(position);
        shield = new Shield(position);
        bomb = new Bomb(position);
        fire = new Fire(position);
        disguise = new Disguise(position);
        spear = new Spear(position);
        startTime = TimeUtils.nanoTime();
        lasers = new DelayedRemovalArray<Laser>();
    }

    public void render (ShapeRenderer renderer, Level level) {
        //stungun lasers
        for (int i = 0; i < lasers.size; i++) {
            //render and update lasers
            lasers.get(i).render(renderer);
            lasers.get(i).update();
        }

        //item.lasers
        //make player bounds
        Rectangle playerBounds = new Rectangle(level.getPlayer().getPosition().x - Constants.HEAD_SIZE,
                level.getPlayer().getPosition().y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                Constants.PLAYER_WIDTH * 2f,
                Constants.PLAYER_HEIGHT * 2.5f);

        for (int i = 0; i < lasers.size; i++) {
            if (stungun.fire) {
                //item.lasers collide
                for (int j = 0; j < lasers.size; j++) {
                    if (lasers.get(i).laserBounds().overlaps(lasers.get(j).laserBounds()) && i != j) {
                        lasers.removeIndex(i);
                    }
                }
            }
        }
        //hit player
        for (int i = 0; i < lasers.size; i++) {
            if (lasers.get(i).laserBounds().overlaps(playerBounds) && !level.getPlayer().duck && stungun.fire && lasers.size > 0 && lasers.get(i).enemyLaser) {
                if (level.getPlayer().health > 0) {
                    level.getPlayer().health -= 2;
                    // Vibrate device for 400 milliseconds
                    Gdx.input.vibrate(400);
                }
                lasers.removeIndex(i);
            }
        }
        //hit player's shield
        for (int i = 0; i < lasers.size; i++) {
            if (level.getPlayer().heldItem.itemType.equals("shield") && lasers.get(i).enemyLaser && ((level.getPlayer().facing == Enums.Facing.RIGHT && lasers.get(i).position.x < level.getPlayer().getPosition().x + Constants.PLAYER_WIDTH + 6f && lasers.get(i).position.x > (level.getPlayer().getPosition().x + Constants.PLAYER_WIDTH) - 6f) || (level.getPlayer().facing == Enums.Facing.LEFT && lasers.get(i).position.x > level.getPlayer().getPosition().x - Constants.PLAYER_WIDTH && lasers.get(i).position.x < (level.getPlayer().getPosition().x - Constants.PLAYER_WIDTH) + 6f))) {
                lasers.removeIndex(i);
            }
        }
        //hit catacomb left wall
        for (int i = 0; i < lasers.size; i++) {
            if (lasers.get(i).position.x < level.catacombs.get(level.currentCatacomb).position.x + 30 && lasers.size > 0) {
                lasers.removeIndex(i);
            }
        }
        //hit catacomb right wall
        for (int i = 0; i < lasers.size; i++) {
            if (lasers.get(i).position.x > level.catacombs.get(level.currentCatacomb).position.x + level.catacombs.get(level.currentCatacomb).width - 30 && lasers.size > 0) {
                lasers.removeIndex(i);
            }
        }

        //lasers hit guard
        for (int i = 0; i < lasers.size; i++) {
            for (int j = 0; j < level.guards.size; j++) {
                //make guard bounds
                Rectangle guardBounds = new Rectangle(level.guards.get(j).position.x - Constants.HEAD_SIZE,
                        level.guards.get(j).position.y + Constants.HEAD_SIZE - Constants.PLAYER_HEIGHT * 2.5f,
                        Constants.PLAYER_WIDTH * 2f,
                        Constants.PLAYER_HEIGHT * 2.5f);
                //hurt guard when item.lasers' bounds overlap guards' bounds
                if (lasers.get(i).laserBounds().overlaps(guardBounds) && !level.guards.get(j).guardItem.equals("shield") && !lasers.get(i).enemyLaser) {
                    if (level.guards.get(j).health > 0) {
                        level.guards.get(j).health -= 2;
                    }
                    //remove laser after colliding with guard
                    lasers.removeIndex(i);
                }
            }
        }

        //if item is key, draw a key
        if (itemType.equals("key") || itemType.equals("doubleKey")) {
            //set initials
            itemWidth = key.keyWidth;
            itemHeight = 9;
            //set item direction
            key.keyFacing = itemFacing;
            //shadow
            key.shadow = !collected;
            key.shadowOffset = shadowOffset;
            //render key, if key is a doubleKey, then set a variable called doubleKey to true, else if not doubleKey, set to false.
            key.render(renderer, !itemType.equals("key"));
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
            stungun.render(renderer, level, this);
        } else if (itemType.equals("sapphire") || itemType.equals("ruby") || itemType.equals("emerald")) {
            //if item is sapphire, draw a sapphire
            //set initials
            itemWidth = gem.gemWidth;
            itemHeight = 8;
            //shadow
            gem.shadow = !collected;
            gem.shadowOffset = shadowOffset;
            //render sapphire
            gem.render(renderer, itemType);
        } else if (itemType.equals("invisibility") || itemType.equals("ghost") || itemType.equals("shock")) {
            //if item is ruby, draw a ruby
            //set initials
            itemWidth = potion.potionWidth;
            itemHeight = 8;
            //shadow
            potion.shadow = !collected;
            potion.shadowOffset = shadowOffset;
            //render ruby
            potion.render(renderer, itemType);
        } else if (itemType.equals("shield")) {
            //set initials
            itemWidth = shield.shieldWidth;
            itemHeight = 9;
            //set item direction
            shield.shieldFacing = itemFacing;
            //shadow
            shield.shadow = !collected;
            shield.shadowOffset = shadowOffset;
            //render Shield
            shield.render(renderer, level);
        } else if (itemType.equals("bomb")) {
            //set initials
            itemWidth = bomb.bombWidth;
            itemHeight = 9;
            //set item direction
            bomb.bombFacing = itemFacing;
            //shadow
            bomb.shadow = !collected;
            bomb.shadowOffset = shadowOffset;
            //render Bomb
            if (!bomb.triggered && !bomb.exploding && collected) {
                bomb.position.set(new Vector2(position.x + 2, position.y));
            }
            bomb.render(renderer, level, this);
        } else if (itemType.equals("fire")) {
            //set initials
            itemWidth = fire.fireWidth;
            itemHeight = 9;
            //set item direction
            fire.fireFacing = itemFacing;
            //shadow
            fire.shadow = !collected;
            fire.shadowOffset = shadowOffset;
            //render Fire
            fire.render(renderer);
        } else if (itemType.equals("disguise")) {
            //set initials
            itemWidth = disguise.clothesWidth;
            itemHeight = 9;
            //shadow
            disguise.shadow = !collected;
            disguise.shadowOffset = shadowOffset;
            //render Disguise
            disguise.render(renderer);
        } else if (itemType.equals("spear")) {
            //if item is spear, draw a spear
            //set initials
            itemWidth = spear.spearWidth;
            itemHeight = 8;
            //set item direction
            spear.spearFacing = itemFacing;
            //shadow
            spear.shadow = !collected;
            spear.shadowOffset = shadowOffset;
            //render spear
            spear.render(renderer);
        }
            //update item's targetBox
            if (!targeted) {
                startTime = TimeUtils.nanoTime();
            }
            if (targeted) {
                updateBox();
            }

            //reset stungun lasers
            if (!itemType.equals("stungun") && lasers.size == 0) {
                lasers = new DelayedRemovalArray<Laser>();
            }

        //if player is touching within the item's bounds, draw a target box around item.
        //(viewportPosition.x > position.x && viewportPosition.x < position.x + itemWidth && viewportPosition.y < position.y + 2 && viewportPosition.y > position.y - 5 && !collected) ||
        if (targeted) {
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
