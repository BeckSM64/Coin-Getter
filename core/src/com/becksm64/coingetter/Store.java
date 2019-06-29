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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Store {

    private Stage stage;
    private ImageButton removeEnemyBtn;

    public Store(SpriteBatch batch) {

        //Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (35 * Gdx.graphics.getDensity());
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        int padding = (int) (12 * Gdx.graphics.getDensity());

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(this.getStage());//Set ability to take input in store
        Table table = new Table();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        //Create labels
        Label storeTitle = new Label("STORE", skin);
        storeTitle.setStyle(new Label.LabelStyle(font, Color.WHITE));

        //Create buttons
        Texture enemyTexture = new Texture(Gdx.files.internal("sprites/rmvEnemyBtn.png"));//Test image, replace with actual store button image when finished
        TextureRegion enemyRegion = new TextureRegion(enemyTexture);
        Drawable enemyDrawable = new TextureRegionDrawable(enemyRegion);
        removeEnemyBtn = new ImageButton(enemyDrawable);

        //Add actors to table
        table.add(storeTitle);
        table.row();
        table.add(removeEnemyBtn).size(removeEnemyBtn.getWidth() * Gdx.graphics.getDensity(), removeEnemyBtn.getHeight() / 2 * Gdx.graphics.getDensity());

        stage.addActor(table);//Add table to stage
    }

    public Stage getStage() {
        return stage;
    }

    public ImageButton getRemoveEnemyBtn() {
        return removeEnemyBtn;
    }

    public void setRemoveEnemyBtn(ImageButton removeEnemyBtn) {
        this.removeEnemyBtn = removeEnemyBtn;
    }

    public void dispose() {
        stage.dispose();
    }
}
