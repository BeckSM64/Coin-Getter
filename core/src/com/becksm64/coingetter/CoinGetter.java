package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinGetter extends Game {

	SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
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
	}
}
