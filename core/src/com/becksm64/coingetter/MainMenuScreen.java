package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Stage stage;
    private Table table;
    private Skin skin;
    private Label gameTitle;
    private TextButton startBtn;
    private TextButton highScoreBtn;
    private Random rng;
    private List<Coin> coinArray;

    public MainMenuScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);

        //Setup stage and table for menu
        System.out.println(viewport.getWorldWidth());
        stage = new Stage(viewport);
        table = new Table();
        table.setFillParent(true);//Make table fill stage
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);//Set ability to take input on stage

        //Setup menu text and buttons
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        gameTitle = new Label("[COIN GETTER]", skin);
        gameTitle.setStyle(new Label.LabelStyle(CoinGetter.font, Color.WHITE));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = CoinGetter.font2;
        startBtn = new TextButton("START", buttonStyle);
        highScoreBtn = new TextButton("HIGH SCORES", buttonStyle);

        //Add texts and buttons to the table
        table.add(gameTitle);
        table.row();//Next row
        table.add(startBtn).padTop(10 * Gdx.graphics.getDensity());
        table.row();
        table.add(highScoreBtn);

        rng = new Random();

        //Create random number of coins in an array with random positions and velocities. May have to use iterator
        coinArray = new ArrayList<>();
        for(int i = 0; i < rng.nextInt(20); i++)
            coinArray.add(new Coin(rng.nextInt((int) viewport.getWorldWidth() - (int) Coin.WIDTH),
                    rng.nextInt((int) viewport.getWorldHeight() - (int ) Coin.HEIGHT),
                    (int) ((rng.nextInt(5)) * Gdx.graphics.getDensity()) + 1,
                    (int) ((rng.nextInt(5) + 1) * Gdx.graphics.getDensity()) + 1));
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

        //Button listener for high score button
        highScoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);//Set the input processor to null so buttons for this screen are no longer active on the next screen
                game.setScreen(new HighScoreScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
        gameTitle.setStyle(new Label.LabelStyle(CoinGetter.font, changeTitleColor()));//Change color of menu title every frame

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
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
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
        skin.dispose();
        stage.dispose();
        batch.dispose();
        for(Coin coin : coinArray)
            coin.dispose();
    }
}
