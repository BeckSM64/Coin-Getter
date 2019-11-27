package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class CoinGetter extends Game {

	SpriteBatch batch;
	public static BitmapFont font, font2, font3, font4;
	
	@Override
	public void create () {

		batch = new SpriteBatch();

		//Setup fonts for menu text and buttons
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.TTF"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int) (50 * Gdx.graphics.getDensity());
		font = generator.generateFont(parameter);
		parameter.size = (int) (30 * Gdx.graphics.getDensity());
		font2 = generator.generateFont(parameter);
		parameter.size = (int) (35 * Gdx.graphics.getDensity());
		font3 = generator.generateFont(parameter);
		parameter.size = (int) (70 * Gdx.graphics.getDensity());
		font4 = generator.generateFont(parameter);
		generator.dispose();//Get rid of generator after done making fonts

		//Set the first screen to main menu screen
		this.setScreen(new MainMenuScreen(this));

		//Music in game class avoids issue of multiple instances when going back to the main menu screen
		Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/renaissance_endo.mp3"));
		music.setLooping(true);
		music.play();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		font2.dispose();
		font3.dispose();
		font4.dispose();
	}
}
