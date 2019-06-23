package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Stage stage;
    private Table table;
    private Skin skin;
    private BitmapFont font, font2;
    private Label gameTitle;
    private TextButton startBtn;
    private Random rng;
    private Music music;
    private List<Coin> coinArray;

    public MainMenuScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setup stage and table for menu
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);//Make table fill stage
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);//Set ability to take input on stage

        //Setup fonts for menu text and buttons
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (50 * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        parameter.size = (int) (30 * Gdx.graphics.getDensity());
        font2 = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts

        //Setup menu text and buttons
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        gameTitle = new Label("[COIN GETTER]", skin);
        gameTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font2;
        startBtn = new TextButton("START", buttonStyle);

        //Add texts and buttons to the table
        table.add(gameTitle);
        table.row();//Next row
        table.add(startBtn).padTop(10 * Gdx.graphics.getDensity());

        //Create and start background music
        rng = new Random();
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/renaissance_endo.mp3"));
        music.setLooping(true);
        music.play();

        //Create random number of coins in an array with random positions and velocities. May have to use iterator
        coinArray = new ArrayList<Coin>();
        for(int i = 0; i < rng.nextInt(20); i++)
            coinArray.add(new Coin(rng.nextInt(Gdx.graphics.getWidth() - (int) Coin.WIDTH),
                    rng.nextInt(Gdx.graphics.getHeight() - (int ) Coin.HEIGHT),
                    (int) ((rng.nextInt(5) + 1) * Gdx.graphics.getDensity()),
                    (int) ((rng.nextInt(5) + 1) * Gdx.graphics.getDensity())));
    }

    private Color changeTitleColor() {

        return new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1);
    }

    @Override
    public void show() {

        //Button listener for start button
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
        gameTitle.setStyle(new Label.LabelStyle(font, changeTitleColor()));//Change color of menu title every frame

        //Update all the coins in the coin array
        for(Coin coin : coinArray)
            coin.update();

        //Draw all the coins in the coin array
        batch.begin();
        for(Coin coin : coinArray)
            batch.draw(coin.getCoinImage(), coin.getPosition().x, coin.getPosition().y, Coin.WIDTH, Coin.HEIGHT);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        cam.setToOrtho(false, width, height);
        cam.update();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        for(Coin coin : coinArray)
            coin.update();
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
        font2.dispose();
        skin.dispose();
        stage.dispose();
        batch.dispose();
        for(Coin coin : coinArray)
            coin.dispose();
    }
}
