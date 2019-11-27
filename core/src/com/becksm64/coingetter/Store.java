package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private ImageButton shieldBtn;
    private ImageButton slowerRespawnBtn;
    private Random rng;
    private Sound purchaseSound;
    private Sound invalidSound;

    public Store(SpriteBatch batch) {

        rng =  new Random();//Create random number generator
        purchaseSound = Gdx.audio.newSound(Gdx.files.internal("audio/cash_register.mp3"));
        invalidSound = Gdx.audio.newSound(Gdx.files.internal("audio/invalid_selection.mp3"));

        int padding = (int) (12 * Gdx.graphics.getDensity());

        //Create viewport, stage, and table which will hold buttons
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        Table table = new Table();

        //Set background for table
        Texture bgTexture = new Texture(Gdx.files.internal("sprites/bgColor.png"));//Test image, replace with actual store button image when finished
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion bgRegion = new TextureRegion(bgTexture);
        Drawable bgDrawable = new TextureRegionDrawable(bgRegion);
        table.setBackground(bgDrawable);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));//Skin for store label

        //Create labels
        storeTitle = new Label("STORE", skin);
        storeTitle.setStyle(new Label.LabelStyle(CoinGetter.font4, Color.WHITE));

        //Create buttons
        Texture enemyTexture = new Texture(Gdx.files.internal("sprites/rmvEnemyBtn.png"));
        enemyTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion enemyRegion = new TextureRegion(enemyTexture);
        Drawable enemyDrawable = new TextureRegionDrawable(enemyRegion);
        Texture enemyTextureClicked = new Texture(Gdx.files.internal("sprites/rmvEnemyClicked.png"));
        TextureRegion enemyRegionClicked = new TextureRegion(enemyTextureClicked);
        Drawable enemyDrawableClicked = new TextureRegionDrawable(enemyRegionClicked);
        removeEnemyBtn = new ImageButton(enemyDrawable, enemyDrawableClicked);

        Texture shoesTexture = new Texture(Gdx.files.internal("sprites/runningShoesBtn.png"));
        shoesTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion shoesRegion = new TextureRegion(shoesTexture);
        Drawable shoesDrawable = new TextureRegionDrawable(shoesRegion);
        Texture shoesClickedTexture = new Texture(Gdx.files.internal("sprites/runningShoesClicked.png"));
        TextureRegion shoesClickedRegion = new TextureRegion(shoesClickedTexture);
        Drawable shoesClickedDrawable = new TextureRegionDrawable(shoesClickedRegion);
        runningShoesBtn = new ImageButton(shoesDrawable, shoesClickedDrawable);

        Texture shieldTexture = new Texture(Gdx.files.internal("sprites/shieldBtn.png"));
        shieldTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion shieldRegion = new TextureRegion(shieldTexture);
        Drawable shieldDrawable = new TextureRegionDrawable(shieldRegion);
        Texture shieldTextureClicked = new Texture(Gdx.files.internal("sprites/shieldClicked.png"));
        TextureRegion shieldRegionClicked = new TextureRegion(shieldTextureClicked);
        Drawable shieldDrawableClicked = new TextureRegionDrawable(shieldRegionClicked);
        shieldBtn = new ImageButton(shieldDrawable, shieldDrawableClicked);

        Texture slowerRespawnTexture = new Texture(Gdx.files.internal("sprites/slowerRespawnBtn.png"));
        slowerRespawnTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion slowerRespawnRegion = new TextureRegion(slowerRespawnTexture);
        Drawable slowerRespawnDrawable = new TextureRegionDrawable(slowerRespawnRegion);
        Texture slowerRespawnTextureClicked = new Texture(Gdx.files.internal("sprites/slowerRespawnClicked.png"));
        TextureRegion slowerRespawnRegionClicked = new TextureRegion(slowerRespawnTextureClicked);
        Drawable slowerRespawnDrawableClicked = new TextureRegionDrawable(slowerRespawnRegionClicked);
        slowerRespawnBtn = new ImageButton(slowerRespawnDrawable, slowerRespawnDrawableClicked);

        //Add actors to table with appropriate padding
        table.add(storeTitle);
        table.row();
        table.add(removeEnemyBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.row();
        table.add(runningShoesBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.row();
        table.add(shieldBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.row();
        table.add(slowerRespawnBtn).padTop(padding).size(storeTitle.getPrefWidth(), storeTitle.getPrefHeight());
        table.pad(padding);

        //Create the container table which will hold the scrollpane
        Table container = new Table();
        container.setFillParent(true);
        ScrollPane scrollPane = new ScrollPane(table);//Create scrollpane which holds the table that holds the buttons
        container.add(scrollPane).pad(padding);//Add scrollpane to table

        stage.addActor(container);//Add table which contains scrollpane to stage
    }

    public Stage getStage() {
        return stage;
    }

    public ImageButton getRemoveEnemyBtn() {
        return removeEnemyBtn;
    }

    public ImageButton getRunningShoesBtn() {
        return runningShoesBtn;
    }

    public ImageButton getShieldBtn() {
        return shieldBtn;
    }

    public ImageButton getSlowerRespawnBtn() {
        return slowerRespawnBtn;
    }

    public Sound getPurchaseSound() {
        return purchaseSound;
    }

    public Sound getInvalidSound() {
        return invalidSound;
    }

    private void changeTitleColor() {
        storeTitle.setStyle(new Label.LabelStyle(CoinGetter.font4, new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1)));
    }

    public void dispose() {
        stage.dispose();
    }
}
