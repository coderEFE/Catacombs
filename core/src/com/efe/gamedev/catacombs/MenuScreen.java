package com.efe.gamedev.catacombs;

/**
 * Created by coder on 11/11/2018.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.efe.gamedev.catacombs.entities.HexagonButton;
import com.efe.gamedev.catacombs.util.Constants;

import javax.naming.Context;


public class MenuScreen extends InputAdapter implements Screen {

    public static final String TAG = MenuScreen.class.getName();

    public CatacombsGame game;

    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private FitViewport viewport;

    private BitmapFont font;
    //TODO: Remove second font
    private BitmapFont font2;

    //hexagon buttons
    private Array<HexagonButton> hexagonButtons;
    public int selectedLevel;
    private int furthestLevel;

    public Vector2 tapPosition;
    public Vector2 releasePosition;
    public boolean pressDown;

    //add menu audio
    //sounds
    public Sound sound5;
    //background music
    public Music the_path_of_the_goblin_king;
    private boolean play_background_music;

    public MenuScreen(CatacombsGame game, int furthestLevel) {
        this.game = game;
        this.furthestLevel = furthestLevel;
    }

    @Override
    public void show() {
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();

        viewport = new FitViewport(Constants.MENU_WORLD_SIZE, Constants.MENU_WORLD_SIZE);
        Gdx.input.setInputProcessor(this);

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(2f);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        font2 = new BitmapFont();
        font2.getData().setScale(4f);
        font2.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        /*if (inputLevel >= furthestLevel) {
            furthestLevel = inputLevel;
        }*/
        furthestLevel = game.getFurthestLevel();

        //add hexagon buttons to the array
        hexagonButtons = new Array<HexagonButton>();
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 0, ((viewport.getWorldHeight() / 5f) * 3)), 0, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 1, ((viewport.getWorldHeight() / 5f) * 3)), 1, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 2, ((viewport.getWorldHeight() / 5f) * 3)), 2, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 3, ((viewport.getWorldHeight() / 5f) * 3)), 3, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 4, ((viewport.getWorldHeight() / 5f) * 3)), 4, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 0, ((viewport.getWorldHeight() / 5f) * 2)), 5, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 1, ((viewport.getWorldHeight() / 5f) * 2)), 6, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 2, ((viewport.getWorldHeight() / 5f) * 2)), 7, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 3, ((viewport.getWorldHeight() / 5f) * 2)), 8, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 4, ((viewport.getWorldHeight() / 5f) * 2)), 9, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 0, ((viewport.getWorldHeight() / 5f) * 1)), 10, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 1, ((viewport.getWorldHeight() / 5f) * 1)), 11, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 2, ((viewport.getWorldHeight() / 5f) * 1)), 12, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 3, ((viewport.getWorldHeight() / 5f) * 1)), 13, false,this));
        hexagonButtons.add(new HexagonButton(Color.BLUE, new Vector2((viewport.getWorldHeight() / 5f) * 4, ((viewport.getWorldHeight() / 5f) * 1)), 14, false,this));

        selectedLevel = furthestLevel;
        unlockLocks();

        //set diamonds for first and second level
        if (game.getFurthestLevel() > 0) {
            game.setMaxDiamonds(3,0);
        }
        if (game.getFurthestLevel() > 1) {
            game.setMaxDiamonds(3,1);
        }

        //input positions
        tapPosition = new Vector2();
        releasePosition = new Vector2();
        pressDown = false;

        //menu sounds
        sound5 = Gdx.audio.newSound(Gdx.files.internal("sounds/nff_confirm_02.wav"));
        //initialize background music files
        the_path_of_the_goblin_king = Gdx.audio.newMusic(Gdx.files.internal("music/the_path_of_the_goblin_king.mp3"));
        the_path_of_the_goblin_king.setLooping(true);
        the_path_of_the_goblin_king.setVolume(0.8f);
        play_background_music = true;
    }

    private void renderTitle (ShapeRenderer renderer, Vector2 position, float width, float height) {
        renderer.setColor(new Color(Color.BLUE.r / 5, Color.BLUE.g / 5, Color.BLUE.b / 5, 1));
        //C
        renderer.rectLine(position.x, position.y, position.x, position.y + height, height / 8f);
        renderer.rectLine(position.x, position.y, position.x + width / 8f, position.y, height / 8f);
        renderer.rectLine(position.x, position.y + height, position.x + width / 8f, position.y + height, height / 8f);
        //a
        renderer.rectLine(position.x + (width / 8f) * 1.5f, position.y, position.x  + (width / 8f) * 1.5f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 1.5f, position.y, position.x + (width / 8f) * 2f, position.y, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 1.5f, position.y + height / 2f, position.x + (width / 8f) * 2f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 2f, position.y, position.x  + (width / 8f) * 2f, position.y + height / 1.5f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 1.5f, position.y + height / 1.5f, position.x  + (width / 8f) * 2f, position.y + height / 1.5f, height / 8f);
        //t
        renderer.rectLine(position.x + (width / 8f) * 2.5f, position.y, position.x  + (width / 8f) * 2.5f, position.y + height / 1f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 2.25f, position.y + height / 1.5f, position.x  + (width / 8f) * 2.75f, position.y + height / 1.5f, height / 8f);
        //a
        renderer.rectLine(position.x + (width / 8f) * 3f, position.y, position.x  + (width / 8f) * 3f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 3f, position.y, position.x + (width / 8f) * 3.5f, position.y, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 3f, position.y + height / 2f, position.x + (width / 8f) * 3.5f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 3.5f, position.y, position.x  + (width / 8f) * 3.5f, position.y + height / 1.5f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 3f, position.y + height / 1.5f, position.x  + (width / 8f) * 3.5f, position.y + height / 1.5f, height / 8f);
        //c
        renderer.rectLine(position.x + (width / 8f) * 4f, position.y, position.x  + (width / 8f) * 4f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 4f, position.y, position.x + (width / 8f) * 4.5f, position.y, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 4f, position.y + height / 2f, position.x + (width / 8f) * 4.5f, position.y + height / 2f, height / 8f);
        //o is a hexagon
        renderer.ellipse(position.x + (width / 8f) * 4.7f, position.y - (width / 8f) * 0.1f, width / 8f, width / 8f, 6);
        //m
        renderer.rectLine(position.x + (width / 8f) * 6f, position.y, position.x  + (width / 8f) * 6f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 6f, position.y + height / 2f, position.x + (width / 8f) * 6.25f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 6.25f, position.y, position.x  + (width / 8f) * 6.25f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 6f, position.y + height / 2f, position.x + (width / 8f) * 6.5f, position.y + height / 2f, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 6.5f, position.y, position.x  + (width / 8f) * 6.5f, position.y + height / 2f, height / 8f);
        //b
        renderer.rectLine(position.x + (width / 8f) * 7f, position.y, position.x  + (width / 8f) * 7f, position.y + height, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 7f, position.y + height / 2, position.x + (width / 8f) * 7.5f, position.y + height / 2, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 7.5f, position.y + height / 2f, position.x + (width / 8f) * 7.5f, position.y, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 7f, position.y, position.x  + (width / 8f) * 7.5f, position.y, height / 8f);
        //s
        renderer.rectLine(position.x + (width / 8f) * 8f, position.y + height, position.x + (width / 8f) * 8.5f, position.y + height, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 8f, position.y + height / 2, position.x  + (width / 8f) * 8f, position.y + height, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 8f, position.y + height / 2, position.x + (width / 8f) * 8.5f, position.y + height / 2, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 8.5f, position.y + height / 2f, position.x + (width / 8f) * 8.5f, position.y, height / 8f);
        renderer.rectLine(position.x + (width / 8f) * 8f, position.y, position.x  + (width / 8f) * 8.5f, position.y, height / 8f);
    }

    @Override
    public void render(float delta) {
        //play song
        if (play_background_music && !the_path_of_the_goblin_king.isPlaying()) {
            the_path_of_the_goblin_king.play();
            play_background_music = false;
        }

        viewport.apply();
        Gdx.gl.glClearColor(Constants.MENU_BACKGROUND_COLOR.r, Constants.MENU_BACKGROUND_COLOR.g, Constants.MENU_BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //draw stuff with special blends to enable color alpha changes
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin(ShapeType.Filled);

        //big "CATACOMB" word
        renderTitle(renderer, new Vector2 (viewport.getWorldWidth() / 40f, viewport.getWorldHeight() * 0.85f), viewport.getWorldWidth() * 0.9f, viewport.getWorldHeight() / 10f);

        //render hexagon buttons
        for (int i = 0; i < hexagonButtons.size; i++) {
            hexagonButtons.get(i).viewport = viewport;
            hexagonButtons.get(i).render(renderer);
            hexagonButtons.get(i).selected = (i == selectedLevel);
            hexagonButtons.get(i).initializeDiamonds();
        }

        //render play button
        Color color = Color.BLUE;
        Color darkColor; Color lightColor;
        //change button colors
        if (tapPosition.x > viewport.getWorldWidth() / 2.6f && tapPosition.x < viewport.getWorldWidth() / 1.5f && tapPosition.y > viewport.getWorldHeight() / 40f && tapPosition.y < viewport.getWorldHeight() / 5f && pressDown) {
            darkColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
            lightColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
        } else {
            darkColor = new Color(color.r / 5, color.g / 5, color.b / 5, 1);
            lightColor = new Color(color.r / 2, color.g / 2, color.b / 2, 1);
        }
        //draw hexagons
        //outer
        renderer.setColor(darkColor);
        renderer.ellipse(viewport.getWorldWidth() / 2.9f, 0, viewport.getWorldWidth() / 4.5f, viewport.getWorldWidth() / 4.5f, 6);
        renderer.ellipse(viewport.getWorldWidth() / 2.4f, 0, viewport.getWorldWidth() / 4.5f, viewport.getWorldWidth() / 4.5f, 6);
        //inner
        renderer.setColor(lightColor);
        renderer.ellipse(viewport.getWorldWidth() / 2.9f + ((viewport.getWorldWidth() / 4.5f) - (viewport.getWorldWidth() / 5.5f)) / 2f, 0 + ((viewport.getWorldWidth() / 4.5f) - (viewport.getWorldWidth() / 5.5f)) / 2f, viewport.getWorldWidth() / 5.5f, viewport.getWorldWidth() / 5.5f, 6);
        renderer.ellipse(viewport.getWorldWidth() / 2.4f + ((viewport.getWorldWidth() / 4.5f) - (viewport.getWorldWidth() / 5.5f)) / 2f, 0 + ((viewport.getWorldWidth() / 4.5f) - (viewport.getWorldWidth() / 5.5f)) / 2f, viewport.getWorldWidth() / 5.5f, viewport.getWorldWidth() / 5.5f, 6);
        //triangle
        renderer.setColor(darkColor);
        renderer.triangle(viewport.getWorldWidth() / 2.3f, viewport.getWorldHeight() / 20f, viewport.getWorldWidth() / 2.3f, viewport.getWorldHeight() / 6f, viewport.getWorldWidth() / 1.7f, viewport.getWorldHeight() / 9f);

        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //draw text to menu screen
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        //render hexagon buttons text if button is not locked
        for (int i = 0; i < hexagonButtons.size; i++) {
            if (!hexagonButtons.get(i).locked) {
                hexagonButtons.get(i).drawFont(font, batch);
            }
        }

        batch.end();
    }

    private void unlockLocks () {
        for (int i = 0; i < game.getFurthestLevel() - 1; i++) {
            hexagonButtons.get(i).locked = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //dispose of drawing tools
        batch.dispose();
        font.dispose();
        renderer.dispose();
        //dispose of sounds
        sound5.dispose();
        the_path_of_the_goblin_king.dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tapPosition = viewport.unproject(new Vector2(screenX, screenY));

        pressDown = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        releasePosition = viewport.unproject(new Vector2(screenX, screenY));

        pressDown = false;
        //if you press play button, it takes you to the selected level
        if (tapPosition.x > viewport.getWorldWidth() / 2.6f && tapPosition.x < viewport.getWorldWidth() / 1.5f && tapPosition.y > viewport.getWorldHeight() / 40f && tapPosition.y < viewport.getWorldHeight() / 5f && releasePosition.x > viewport.getWorldWidth() / 2.6f && releasePosition.x < viewport.getWorldWidth() / 1.5f && releasePosition.y > viewport.getWorldHeight() / 40f && releasePosition.y < viewport.getWorldHeight() / 5f && !pressDown) {
            the_path_of_the_goblin_king.dispose();
            game.showGamePlayScreen(selectedLevel);
        }
        return true;
    }
}