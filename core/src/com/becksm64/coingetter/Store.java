package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class Store {

    private Stage stage;
    private Label storeTitle;
    private ImageButton removeEnemyBtn;
    private ImageButton runningShoesBtn;
    private BitmapFont font;
    private Random rng;

    public Store(SpriteBatch batch) {

        rng =  new Random();//Create random number generator

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (70 * Gdx.graphics.getDensity());
        font = generator.generateFont(parameter);
        generator.dispose();
        int padding = (int) (12 * Gdx.graphics.getDensity());

        //Create viewport, stage, and table which will hold buttons
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        Table table = new Table();

        //Set background for table
        Texture bgTexture = new Texture(Gdx.files.internal("sprites/bgColor.png"));//Test image, replace with actual store button image when finished
        TextureRegion bgRegion = new TextureRegion(bgTexture);
        Drawable bgDrawable = new TextureRegionDrawable(bgRegion);
        table.setBackground(bgDrawable);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));//Skin for store label

        //Create labels
        storeTitle = new Label("STORE", skin);
        storeTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));

        //Create buttons
        Texture enemyTexture = new Texture(Gdx.files.internal("sprites/rmvEnemyBtn.png"));//Test image, replace with actual store button image when finished
        TextureRegion enemyRegion = new TextureRegion(enemyTexture);
        Drawable enemyDrawable = new TextureRegionDrawable(enemyRegion);
        removeEnemyBtn = new ImageButton(enemyDrawable);

        Texture shoesTexture = new Texture(Gdx.files.internal("sprites/runningShoesBtn.png"));//Test image, replace with actual store button image when finished
        TextureRegion shoesRegion = new TextureRegion(shoesTexture);
        Drawable shoesDrawable = new TextureRegionDrawable(shoesRegion);
        runningShoesBtn = new ImageButton(shoesDrawable);

        //Add actors to table with appropriate padding
        table.add(storeTitle);
        table.row();
        table.add(removeEnemyBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.row();
        table.add(runningShoesBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.pad(padding);

        //Create the container table which will hold the scrollpane
        Table container = new Table();
        container.setFillParent(true);
        ScrollPane scrollPane = new ScrollPane(table);//Create scrollpane which holds the table that holds the buttons
        container.add(scrollPane).pad(padding);//Add scrollpane to table

        stage.addActor(container);//Add table which contains scrollpane to stage
        Gdx.input.setInputProcessor(this.getStage());//Set ability to take input in store
    }

    public Stage getStage() {
        //changeTitleColor();
        return stage;
    }

    public ImageButton getRemoveEnemyBtn() {
        return removeEnemyBtn;
    }

    public ImageButton getRunningShoesBtn() {
        return runningShoesBtn;
    }

    private void changeTitleColor() {
        storeTitle.setStyle(new Label.LabelStyle(font, new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1)));
    }

    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
