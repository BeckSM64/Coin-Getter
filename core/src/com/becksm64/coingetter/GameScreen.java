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
    private List<Enemy> enemyArray;
    private Random rng;
    private Hud hud;

    private float timeSeconds;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        touchPos = new Vector3(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);//Start relatively in center of screen
        player = new Player(touchPos.x, touchPos.y);
        rng = new Random();
        coinArray = new ArrayList<Coin>();
        enemyArray = new ArrayList<Enemy>();
        hud = new Hud(batch);
        generateCoins();
        addEnemy();

        timeSeconds = 0;
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

    /*
     * Adds an enemy to the enemy array with a randomly generated position on the screen
     * Only adds a new enemy if 10 seconds has passed
     */
    private void addEnemy() {

        timeSeconds += Gdx.graphics.getRawDeltaTime();//Wait specified amount of time until opponent takes their turn
        if (timeSeconds > 10f) {
            timeSeconds -= 10f;//Reset time passed
            enemyArray.add(new Enemy(rng.nextInt(Gdx.graphics.getWidth() - (int) Enemy.SIZE),
                    rng.nextInt(Gdx.graphics.getHeight() - (int) Enemy.SIZE)));
        }
    }

    /*
     * Checks to see if player health has reached 0
     * Returns true if it has, returns false otherwise
     */
    private boolean isGameOver() {
        return player.getHealth() <= 0;
    }

    /*
     * Increases the player score every frame and updates the hud to reflect this
     */
    private void increaseScore() {
        player.setScore(player.getScore() + 1);
        hud.setScore(player.getScore());
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
        for(Enemy enemy : enemyArray)
            enemy.update();

        addEnemy();

        //Update touched position
        if(Gdx.input.isTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);//Gets correct touch position relative to camera

            float maxDistance = Player.SPEED * Gdx.graphics.getDeltaTime();//Distance the player can move during current frame
            Player.tmp.set(touchPos.x, touchPos.y).sub(player.getPosition().x, player.getPosition().y);//A vector from the player to the touch point

            //Close enough to just set the player at the target but if not, move along vector towards touch position
            if (Player.tmp.len() <= maxDistance) {
                player.getPosition().x = touchPos.x;
                player.getPosition().y = touchPos.y;
            } else {
                Player.tmp.nor().scl(maxDistance); //Reduce the length of the vector by the distance player just traveled
                player.getPosition().x += Player.tmp.x;//Move player by the vector length
                player.getPosition().y += Player.tmp.y;
            }
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

            //Add 10 to player health whenever they collect all the coins on the screen, only if they have less than 100 health
            if(!player.hasFullHealth()) {
                player.setHealth(player.getHealth() + 10);
                hud.setHealth(player.getHealth());
            }
            generateCoins();
        }

        //Draw enemies
        for(Enemy enemy : enemyArray)
            batch.draw(enemy.getEnemyImage(), enemy.getPosition().x, enemy.getPosition().y, Enemy.SIZE, Enemy.SIZE);//Draw enemy
        batch.end();

        collision();//Check for collision
        hud.setCoinLabel(player.getCoinsCollected());//Update the hud to reflect player coins collected
        increaseScore();

        //Check if game is over
        if(isGameOver()) {
            this.dispose();
            game.setScreen(new GameOverScreen(game, player.getScore()));//Set game over screen
        }
    }

    /*
     * Check for collision between game objects
     */
    private void collision() {

        //Check for player and coin collision
        for(int i = 0; i < coinArray.size(); i++) {
            if(player.getBounds().overlaps(coinArray.get(i).getBounds())) {
                coinArray.get(i).dispose();//Dispose of asset before removing from array
                coinArray.remove(i);//Remove coin from list if player touches it
                player.setCoinsCollected(player.getCoinsCollected() + 1);//Increment coins collected when coin is collected
                player.setScore(player.getScore() + 50);//Increment player score by 50 for every coin collected
            }
        }

        //Check for player and enemy collision
        for(Enemy enemy : enemyArray) {
            if (player.getBounds().overlaps(enemy.getBounds())) {

                player.setPosition(Gdx.graphics.getWidth() / 2.0f - (Player.SIZE / 2), Gdx.graphics.getHeight() / 2.0f - (Player.SIZE / 2));
                player.setHealth(player.getHealth() - 10);//Decrease health by 10 if hit by enemy
                hud.setHealth(player.getHealth());//Update hud to reflect current player health
                enemy.setVelocity(enemy.getVelocity().x * -1, enemy.getVelocity().y * -1);
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
