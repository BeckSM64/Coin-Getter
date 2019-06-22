package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Vector3 touchPos;//Vector to keep track of where screen was touched
    private Player player;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        touchPos = new Vector3(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);//Start relatively in center of screen
        player = new Player(touchPos.x, touchPos.y);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();

        //Update touched position
        if(Gdx.input.isTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);//Gets correct touch position relative to camera
            player.setPosition(touchPos.x, touchPos.y);
        }

        //Draw player
        batch.begin();
        batch.draw(player.getPlayerImage(), player.getPosition().x, player.getPosition().y, Player.SIZE, Player.SIZE);
        batch.end();
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
        player.dispose();
        batch.dispose();
    }
}
