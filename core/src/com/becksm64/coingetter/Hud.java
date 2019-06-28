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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    private Stage stage;
    private Label health;
    private Label coinLabel;
    private Label score;

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

        table.left();
        table.add(healthLabel).padLeft(padding).padRight(padding);
        table.add(health);
        table.add(new Image(new Texture("sprites/coin.png"))).width(Gdx.graphics.getWidth() / 10.0f).height(Gdx.graphics.getHeight() / 6.0f);
        table.add(coinLabel);
        table.add(scoreLabel).padLeft(padding).padRight(padding);
        table.add(score);
        //table.debug();

        float healthWidth = table.getCell(health).getMinWidth();//Get width of health when label is three digits long
        table.getCell(health).width(healthWidth);//Force cell to stay the width of three digit long label regardless of label size
        float coinLabelWidth = table.getCell(coinLabel).getMinWidth() * 2;//Calculate size for cell
        table.getCell(coinLabel).width(coinLabelWidth);//Set fixed cell size

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

    public void dispose() {
        this.stage.dispose();
    }
}