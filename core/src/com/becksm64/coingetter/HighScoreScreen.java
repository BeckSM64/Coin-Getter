package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Random;

public class HighScoreScreen implements Screen {

    private Game game;
    private BitmapFont font, font2;
    private Skin skin;
    private Label highScoreTitle;
    private Label score;
    private Label score2;
    private Label score3;
    private TextButton backBtn;
    private Stage stage;
    private Random rng;

    public HighScoreScreen(Game game) {

        this.game = game;
        rng = new Random();

        //Setup stage and table for menu
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);//Make table fill stage
        table.top();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);//Take input on stage so buttons work

        //Setup fonts for menu text and buttons
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (50 * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        parameter.size = (int) (30 * Gdx.graphics.getDensity());
        font2 = generator.generateFont(parameter);
        generator.dispose();//Get rid of generator after done making fonts
        int padding = (int) (12 * Gdx.graphics.getDensity());

        //Setup menu text and buttons
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        highScoreTitle = new Label("[HIGH SCORES]", skin);
        highScoreTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));
        score = new Label("1. ", skin);
        score2 = new Label("2. ", skin);
        score3 = new Label("3. ", skin);
        score.setStyle(new Label.LabelStyle(font2, Color.WHITE));
        score2.setStyle(new Label.LabelStyle(font2, Color.WHITE));
        score3.setStyle(new Label.LabelStyle(font2, Color.WHITE));
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font2;
        backBtn = new TextButton("BACK", buttonStyle);

        Preferences prefs = Gdx.app.getPreferences("Coin Getter Preferences");//Get scores from preferences

        //Set high scores based on saved preferences
        score.setText(score.getText() + String.valueOf(prefs.getInteger("score")));
        score2.setText(score2.getText() + String.valueOf(prefs.getInteger("score2")));
        score3.setText(score3.getText() + String.valueOf(prefs.getInteger("score3")));

        table.add(highScoreTitle).pad(padding);
        table.row();
        table.add(score).pad(padding);
        table.row();
        table.add(score2).pad(padding).width(score.getPrefWidth());//Force to same size as above cell (width will always be smaller because score will be lower)
        table.row();
        table.add(score3).pad(padding).width(score.getPrefWidth());
        table.row();
        table.add(backBtn).expand().right().bottom().pad(padding);//Push button to the bottom right of screen and pad it

    }

    private Color changeTitleColor() {
        return new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1);
    }

    @Override
    public void show() {

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        highScoreTitle.setStyle(new Label.LabelStyle(font, changeTitleColor()));//Change color of high score title every frame
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
        stage.dispose();
        font.dispose();
        font2.dispose();
        skin.dispose();
    }
}
