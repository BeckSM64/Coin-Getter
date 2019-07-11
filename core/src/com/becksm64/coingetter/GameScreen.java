package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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
    private Store store;

    private float timeSeconds;
    private boolean showStore;

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
        enemyArray.add(new Enemy(rng.nextInt(Gdx.graphics.getWidth() - (int) Enemy.SIZE),
                        rng.nextInt(Gdx.graphics.getHeight() - (int) Enemy.SIZE)));//Add initial enemy
        hud = new Hud(batch);
        store = new Store(batch);

        //Setup input for multiple stages
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(store.getStage());

        //Generate initial coins and enemy
        generateCoins();
        addEnemy();

        timeSeconds = 0;//Keeps track of time passed for events that require specific time lapse
        showStore = false;
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
        if (timeSeconds > 20f) {
            timeSeconds -= 20f;//Reset time passed
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

    /*
     * Moves player towards touch position. May move this into player class
     */
    private void movePlayer() {

        if(Gdx.input.isTouched()) {

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);//Gets correct touch position relative to camera

            float maxDistance = player.getSpeed() * Gdx.graphics.getDeltaTime();//Distance the player can move during current frame
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
    }

    @Override
    public void show() {

        //Store button to purchase remove enemy power up which removes one enemy from the screen on purchase
        store.getRemoveEnemyBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //Player must have at least 30 coins and there must be at least one enemy on screen to purchase this power up
                if(player.getCoinsCollected() >= 30 && enemyArray.size() > 0) {
                    player.setCoinsCollected(player.getCoinsCollected() - 30);//Reduce player coin count
                    hud.setCoinLabel(player.getCoinsCollected());//Update hud
                    enemyArray.get(0).dispose();
                    enemyArray.remove(0);//Remove the first enemy in the array list
                }
            }
        });

        store.getRunningShoesBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if(player.getCoinsCollected() >= 50 && player.hasRunningShoes() ==  false) {
                    player.setHasRunningShoes(true);
                    player.setCoinsCollected(player.getCoinsCollected() - 50);
                    hud.setCoinLabel(player.getCoinsCollected());
                    player.setSpeed(player.getSpeed() * 1.25f);
                }
            }
        });

        //Open store button
        hud.getStoreBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showStore = !showStore;
            }
        });
    }

    /*
     * Draw all the assets that need to be drawn for the game
     */
    private void drawAssets() {

        //Draw assets
        batch.begin();

        //Set transparency if player is invincible
        Color c = batch.getColor();
        if(player.isInvincible())
            batch.setColor(c.r, c.g, c.b, 0.5f);

        batch.draw(player.getPlayerImage(), player.getPosition().x, player.getPosition().y, Player.SIZE, Player.SIZE);//Draw player
        batch.setColor(c.r, c.g, c.b, 1);//Go back to no transparency

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
    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update camera, player, and coins
        if(!showStore) {
            cam.update();
            player.update();
            for (Coin coin : coinArray)
                coin.update();
            for (Enemy enemy : enemyArray)
                enemy.update();
            addEnemy();
            movePlayer();
            increaseScore();
            collision();//Check for collision
            hud.setCoinLabel(player.getCoinsCollected());//Update the hud to reflect player coins collected
            drawAssets();//Draw player, coins, and enemies
        }

        //Draw HUD
        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        //Draw Store
        if(showStore) {
            batch.setProjectionMatrix(store.getStage().getCamera().combined);
            store.getStage().draw();
        }

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
            if (player.getBounds().overlaps(enemy.getBounds()) && !player.isInvincible()) {

                player.setHealth(player.getHealth() - 10);//Decrease health by 10 if hit by enemy
                hud.setHealth(player.getHealth());//Update hud to reflect current player health
                enemy.setVelocity(enemy.getVelocity().x * -1, enemy.getVelocity().y * -1);
                player.setInvincible(true);
            }
        }

        //Check if player invincibility is up
        if(player.isInvincible()) {
            player.setInvincibleTime(player.getInvincibleTime() + Gdx.graphics.getRawDeltaTime());//Increment invincible time
            if (player.getInvincibleTime() > 2f && player.isInvincible()) {
                player.setInvincibleTime(0);//Reset invincible time
                player.setInvincible(false);//No longer invincible after 2 seconds
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
        for(Enemy enemy : enemyArray)
            enemy.dispose();
        hud.dispose();
        store.dispose();
    }
}
