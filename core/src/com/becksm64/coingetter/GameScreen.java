package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Vector3 touchPos;//Vector to keep track of where screen was touched
    private Player player;
    private List<Coin> coinArray;
    private Random rng;
    private Hud hud;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        touchPos = new Vector3(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);//Start relatively in center of screen
        player = new Player(touchPos.x, touchPos.y);
        rng = new Random();
        coinArray = new ArrayList<Coin>();
        hud = new Hud(batch);
        generateCoins();
    }

    /*
     * Generate a random number of coins and add them to the coin array
     */
    private void generateCoins() {
        for(int i = 0; i < rng.nextInt(10); i++)
            coinArray.add(new Coin(rng.nextInt(Gdx.graphics.getWidth() - (int) Coin.WIDTH),
                    rng.nextInt(Gdx.graphics.getHeight() - (int) Coin.HEIGHT),
                    (int) ((rng.nextInt(5)) * Gdx.graphics.getDensity()) + 1,
                    (int) ((rng.nextInt(5)) * Gdx.graphics.getDensity()) + 1));
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

        //Update camera, player, and coins
        cam.update();
        player.update();
        for(Coin coin : coinArray)
            coin.update();

        //Update touched position
        if(Gdx.input.isTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);//Gets correct touch position relative to camera
            player.setPosition(touchPos.x, touchPos.y);
            player.update();
        }

        //Draw HUD
        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        //Draw assets
        batch.begin();
        batch.draw(player.getPlayerImage(), player.getPosition().x, player.getPosition().y, Player.SIZE, Player.SIZE);//Draw player
        //Draw coins if they exist or create more if they don't
        if(coinArray.size() > 0) {
            for (Coin coin : coinArray)
                batch.draw(coin.getCoinImage(), coin.getPosition().x, coin.getPosition().y, Coin.WIDTH, Coin.HEIGHT);
        } else {
            generateCoins();
        }
        batch.end();

        collision();//Check for collision
        hud.setCoinLabel(player.getCoinsCollected());//Update the hud to reflect player coins collected
    }

    /*
     * Check for collision between game objects
     */
    private void collision() {

        for(int i = 0; i < coinArray.size(); i++) {
            if(player.getBounds().overlaps(coinArray.get(i).getBounds())) {
                coinArray.remove(i);//Remove coin from list if player touches it
                player.setCoinsCollected(player.getCoinsCollected() + 1);//Increment coins collected when coin is collected
            }
        }
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
        for(Coin coin : coinArray)
            coin.dispose();
        hud.dispose();
    }
}
