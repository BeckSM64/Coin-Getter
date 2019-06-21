package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Random;

public class MainMenuScreen implements Screen {

    private Game game;
    private Camera cam;
    private Stage stage;
    private Table table;
    private Skin skin;
    private BitmapFont font;
    private Label gameTitle;
    private Random rng;
    private Music music;

    public MainMenuScreen(Game game) {

        this.game = game;
        cam = new OrthographicCamera();
        ((OrthographicCamera) cam).setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setup stage and table for menu
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);//Make table fill stage
        stage.addActor(table);

        //Setup fonts for menu text and buttons
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (50 * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts

        //Setup menu text and buttons
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        gameTitle = new Label("[COIN GETTER]", skin);
        gameTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));

        //Add texts and buttons to the table
        table.add(gameTitle);

        //Create and start background music
        rng = new Random();
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/renaissance_endo.mp3"));
        music.setLooping(true);
        music.play();
    }

    private Color changeTitleColor() {

        Color color = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1);
        return color;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();

        gameTitle.setStyle(new Label.LabelStyle(font, changeTitleColor()));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
}
