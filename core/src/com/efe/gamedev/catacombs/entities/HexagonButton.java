package com.efe.gamedev.catacombs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.efe.gamedev.catacombs.MenuScreen;
import com.efe.gamedev.catacombs.items.Diamond;
import com.efe.gamedev.catacombs.util.Constants;

/**
 * Created by coder on 11/11/2018.
 * Hexagon buttons are in the main menu and are used to select levels
 * They are different from the regular rectangular button class
 */

public class HexagonButton {
    private Vector2 position;
    private Color color;
    private int levelNum;
    public boolean selected;
    public boolean locked;
    public FitViewport viewport;
    private MenuScreen menu;
    //keep track of diamonds
    public Array<Diamond> scoreDiamonds;


    //draws hexagon buttons to select levels with
    public HexagonButton (Color color, Vector2 position, int levelNum, boolean selected, MenuScreen menu) {
        this.position = position;
        this.color = color;
        this.levelNum = levelNum;
        this.selected = selected;
        locked = true;
        viewport = new FitViewport(Constants.MENU_WORLD_SIZE, Constants.MENU_WORLD_SIZE);
        this.menu = menu;
        initializeDiamonds();
    }

    //sets the amount of diamonds shown on the level button
    public void initializeDiamonds () {
        //reset diamond array
        scoreDiamonds = new Array<>();
        //set diamonds to each button
        int diamondNum = (menu.game.getMaxDiamonds(levelNum));
        //add diamonds to array
        for (int i = 0; i < diamondNum; i++) {
            scoreDiamonds.add(new Diamond(new Vector2()));
        }
    }

    public void render (ShapeRenderer renderer) {
        //set colors
        Color darkColor;
        Color lightColor;
        //change color of button
        if (touchButton(menu.tapPosition) && menu.pressDown) {
            darkColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
            lightColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
        } else {
            darkColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
            lightColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
        }
        //select button by pressing it
        if (touchButton(menu.tapPosition) && touchButton(menu.releasePosition) && !menu.pressDown && !locked && menu.selectedLevel != levelNum) {
            if (levelNum >= 0) {
                menu.selectedLevel = levelNum;
            }
            menu.sound5.play();
            if (levelNum == -1 && !menu.showCredits) {
                menu.showCredits = true;
            } else if (levelNum == -2 && menu.showCredits) {
                menu.showCredits = false;
            }
        }
        //draw hexagons
        //outer
        renderer.setColor(selected ? lightColor : darkColor);
        renderer.ellipse(position.x, position.y, viewport.getWorldWidth() / 5f, viewport.getWorldWidth() / 5f, 6);
        //inner
        renderer.setColor(lightColor);
        renderer.ellipse(position.x + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 6f)) / 2f, position.y + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 6f)) / 2f, viewport.getWorldWidth() / 6f, viewport.getWorldWidth() / 6f, 6);
        //credit button icon
        if (levelNum == -1) {
            //three circles
            renderer.setColor(darkColor);
            renderer.ellipse(position.x + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 25f)) / 2f, position.y + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 25f)) / 2f, viewport.getWorldWidth() / 25f, viewport.getWorldWidth() / 25f, 20);
            renderer.ellipse(position.x + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 25f)) / 2f, position.y + ((viewport.getWorldWidth() / 5f) + (viewport.getWorldWidth() / 25f)) / 2f, viewport.getWorldWidth() / 25f, viewport.getWorldWidth() / 25f, 20);
            renderer.ellipse(position.x + ((viewport.getWorldWidth() / 5f) - (viewport.getWorldWidth() / 25f)) / 2f, position.y + ((viewport.getWorldWidth() / 5f) - ((viewport.getWorldWidth() / 25f))) / 4f, viewport.getWorldWidth() / 25f, viewport.getWorldWidth() / 25f, 20);
        } else if (levelNum == -2) {
            //back button icon
            renderer.setColor(darkColor);
            renderer.triangle(position.x + (viewport.getWorldWidth() / 20f), position.y + (viewport.getWorldWidth() / 10f), position.x + (viewport.getWorldWidth() / 7f), position.y + (viewport.getWorldWidth() / 20f), position.x + (viewport.getWorldWidth() / 7f), position.y + (viewport.getWorldWidth() / 7f));
        }
        //draw lock if button is locked
        if (levelNum == 0) {
            locked = false;
        }
        //make sure button is not selected if it is locked
        if (locked) { selected = false; }
        if (levelNum <= menu.game.getFurthestLevel()) {
            locked = false;
        }
        //draw lock symbol
        if (locked) {
            renderer.setColor(darkColor);
            renderer.rect(position.x + (viewport.getWorldWidth() / 13f), position.y + (viewport.getWorldWidth() / 10f / 2f), viewport.getWorldWidth() / 20f, viewport.getWorldWidth() / 20f);
            renderer.arc(position.x + (viewport.getWorldWidth() / 13f) + (viewport.getWorldWidth() / 40f), position.y + (viewport.getWorldWidth() / 10f), viewport.getWorldWidth() / 40f, 0, 180, 20);
            renderer.setColor(lightColor);
            renderer.arc(position.x + (viewport.getWorldWidth() / 13f) + (viewport.getWorldWidth() / 40f), position.y + (viewport.getWorldWidth() / 10f), viewport.getWorldWidth() / 60f, 0, 180, 20);
        }
        //draw collected diamonds: MAX DIAMONDS PER LEVEL IS 3.
        for (Diamond diamond : scoreDiamonds) {
            diamond.position.set(new Vector2(position.x + (viewport.getWorldWidth() / 9f) - (scoreDiamonds.indexOf(diamond, true) * diamond.diamondWidth * 1.2f) - ((3 - scoreDiamonds.size) * 5f), position.y + viewport.getWorldHeight() / 25f));
            diamond.render(renderer);
        }
    }

    public void drawFont (BitmapFont font, SpriteBatch batch) {
        Color darkColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
        Color lightColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);

        final GlyphLayout numberLayout = new GlyphLayout(font, (levelNum + 1) + "");
        font.setColor((touchButton(menu.tapPosition) && menu.pressDown) ? lightColor : darkColor);
        font.draw(batch, (levelNum + 1) + "", position.x + (viewport.getWorldWidth() / 11f) + 2, position.y + 2 + (viewport.getWorldWidth() / 11f) + numberLayout.height / 2, 0, Align.center, false);
    }

    private boolean touchButton (Vector2 touchPosition) {
        return (touchPosition.dst(new Vector2(position.x + (viewport.getWorldWidth() / 5f / 2f), position.y + (viewport.getWorldWidth() / 5f / 2f))) < viewport.getWorldWidth() / 12f);
    }
}
