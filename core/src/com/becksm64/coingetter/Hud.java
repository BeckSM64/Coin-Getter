package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private Stage stage;
    private Label health;
    private Label coinLabel;
    private Label score;
    private TextButton storeBtn;
    private TextButton pauseBtn;

    public Hud(SpriteBatch batch) {

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (35 * Gdx.graphics.getDensity());
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        int padding = (int) (12 * Gdx.graphics.getDensity());

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        //Gdx.input.setInputProcessor(stage);//Take input for stage
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Label healthLabel = new Label("HEALTH:", skin);
        healthLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));

        Label scoreLabel = new Label("SCORE:", skin);
        scoreLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));

        health = new Label("100", skin);
        health.setStyle(new Label.LabelStyle(font, Color.WHITE));

        coinLabel = new Label(": 0", skin);
        coinLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));

        score = new Label("0", skin);
        score.setStyle(new Label.LabelStyle(font, Color.WHITE));

        TextButton.TextButtonStyle btnStyle= new TextButton.TextButtonStyle();
        btnStyle.font = font;
        storeBtn = new TextButton("STORE", btnStyle);
        pauseBtn = new TextButton("PAUSE", btnStyle);

        table.left();
        table.add(healthLabel).padLeft(padding).padRight(padding).right();
        table.add(health).width(healthLabel.getPrefWidth() / 2).left();
        table.add(new Image(new Texture("sprites/coin.png"))).width(Gdx.graphics.getWidth() / 10.0f).height(Gdx.graphics.getHeight() / 6.0f).right();
        table.add(coinLabel).width(scoreLabel.getPrefWidth()).left();
        table.add(scoreLabel).padLeft(padding).padRight(padding).left();
        table.add(score).width(scoreLabel.getPrefWidth()).left();
        table.row();
        table.add(pauseBtn).left().bottom().pad(padding).expand().colspan(4);
        table.add(storeBtn).right().bottom().pad(padding).expand().colspan(3);

        stage.addActor(table);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setHealth(int health) {
        this.health.setText(health);
    }

    public void setCoinLabel(int coinCount) {
        this.coinLabel.setText(":" + coinCount);
    }

    public void setScore(int score) {
        this.score.setText(score);
    }

    public TextButton getStoreBtn() {
        return storeBtn;
    }

    public TextButton getPauseBtn() {
        return pauseBtn;
    }

    public void dispose() {
        this.stage.dispose();
    }
}