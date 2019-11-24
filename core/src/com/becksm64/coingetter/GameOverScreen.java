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

import java.util.Random;

public class GameOverScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera cam;
    private Skin skin;
    private Label gameOver;
    private TextButton newBtn, menuBtn;
    private Random rng;

    public GameOverScreen(Game game, int score) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setup stage and table for menu
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);//Make table fill stage
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);//Set ability to take input on stage

        //Setup menu text and buttons
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        gameOver = new Label("[GAME OVER]", skin);
        gameOver.setStyle(new Label.LabelStyle(CoinGetter.font, Color.WHITE));
        Label finalScore = new Label("SCORE: " + String.valueOf(score), skin);
        finalScore.setStyle(new Label.LabelStyle(CoinGetter.font2, Color.WHITE));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = CoinGetter.font2;
        newBtn = new TextButton("NEW GAME", buttonStyle);
        menuBtn = new TextButton("MAIN MENU", buttonStyle);

        //Add texts and buttons to the table
        table.add(gameOver);
        table.row();//Next row
        table.add(finalScore).padTop(10 * Gdx.graphics.getDensity());
        table.row();
        table.add(newBtn).padTop(10 * Gdx.graphics.getDensity());
        table.row();
        table.add(menuBtn).padTop(10 * Gdx.graphics.getDensity());

        rng = new Random();
    }

    private Color changeTitleColor() {
        return new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1);
    }

    @Override
    public void show() {

        newBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.setInputProcessor(null);
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {

        //Clear screen and adjust viewport
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        stage.draw();
        gameOver.setStyle(new Label.LabelStyle(CoinGetter.font, changeTitleColor()));//Change color of menu title every frame
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

        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
