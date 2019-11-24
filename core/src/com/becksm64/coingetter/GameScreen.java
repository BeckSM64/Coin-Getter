package com.becksm64.coingetter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
    private PauseMenu pauseMenu;
    private HealthBonus healthBonus;
    private InputMultiplexer multiplexer;
    private InputProcessor storeProcessor;

    private float timeSeconds;
    private float enemyRespawnTime;
    private float timeSecondsBonus;//Timer for health bonus (Change this probably)
    private float healthBonusSpawnTime;//Randomly generated cooldown time for health bonus spawn
    private boolean showStore;
    private boolean isPaused;

    public GameScreen(Game game) {

        this.game = game;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        touchPos = new Vector3(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0);//Start relatively in center of screen
        player = new Player(touchPos.x, touchPos.y);
        rng = new Random();
        coinArray = new ArrayList<>();
        enemyArray = new ArrayList<>();
        enemyArray.add(new Enemy(rng.nextInt(Gdx.graphics.getWidth() - (int) Enemy.SIZE),
                        rng.nextInt(Gdx.graphics.getHeight() - (int) Enemy.SIZE)));//Add initial enemy
        healthBonus = null;
        hud = new Hud(batch);
        store = new Store(batch);
        pauseMenu = new PauseMenu(batch);
        enemyRespawnTime = 20f;
        healthBonusSpawnTime = rng.nextInt(60);//Spawn time for health bonus randomly generated between 0 and 1 minute

        //Setup input for multiple stages
        multiplexer = new InputMultiplexer();//Holds all input processors
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(pauseMenu.getStage());
        storeProcessor = store.getStage();//Store input processor

        //Generate initial coins and enemy
        generateCoins();
        addEnemy();

        timeSeconds = 0;//Keeps track of time passed for events that require specific time lapse
        showStore = false;
        isPaused = false;
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
     * Generate a health bonus powerup at a random position with a random velocity
     */
    private void generateHealthBonus() {

        timeSecondsBonus += Gdx.graphics.getRawDeltaTime();
        if (timeSecondsBonus > healthBonusSpawnTime) {

            timeSecondsBonus -= healthBonusSpawnTime;//Reset time passed
            if(healthBonus == null) {

                healthBonus = new HealthBonus(rng.nextInt(Gdx.graphics.getWidth() - (int) Coin.WIDTH),
                        rng.nextInt(Gdx.graphics.getHeight() - (int) Coin.HEIGHT),
                        (int) ((rng.nextInt(5)) * Gdx.graphics.getDensity()) + 1,
                        (int) ((rng.nextInt(5)) * Gdx.graphics.getDensity()) + 1);
            }
            healthBonusSpawnTime = rng.nextInt(60);//Generate new health bonus spawn time between 0 and 1 minute
        }
    }

    /*
     * Adds an enemy to the enemy array with a randomly generated position on the screen
     * Only adds a new enemy if 10 seconds has passed
     */
    private void addEnemy() {

        timeSeconds += Gdx.graphics.getRawDeltaTime();
        if (timeSeconds > enemyRespawnTime) {
            timeSeconds -= enemyRespawnTime;//Reset time passed
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

        //Touch movement controls
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

        //Keyboard movement controls
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {//Check if W is pressed (Up)
            player.setVelocity(player.getVelocity().x, (float) 5 * Gdx.graphics.getDensity());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {//Check if D is pressed (Right)
            player.setVelocity((float) 5 * Gdx.graphics.getDensity(), player.getVelocity().y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {//Check if S is pressed (Down)
            player.setVelocity(player.getVelocity().x, (float) -5 * Gdx.graphics.getDensity());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {//Check if A is pressed (Left)
            player.setVelocity((float) -5 * Gdx.graphics.getDensity(), player.getVelocity().y);
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {//Stop moving up or down
            player.setVelocity(player.getVelocity().x, 0);
        }

        if(!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {//Stop moving left or right
            player.setVelocity(0, player.getVelocity().y);
        }
    }

    @Override
    public void show() {

        //Store button to purchase remove enemy power up which removes one enemy from the screen on purchase
        store.getRemoveEnemyBtn().addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //Player must have at least 30 coins and there must be at least one enemy on screen to purchase this power up
                if(player.getCoinsCollected() >= 30 && enemyArray.size() > 0) {
                    player.setCoinsCollected(player.getCoinsCollected() - 30);//Reduce player coin count
                    hud.setCoinLabel(player.getCoinsCollected());//Update hud
                    enemyArray.get(0).dispose();
                    enemyArray.remove(0);//Remove the first enemy in the array list
                    store.getPurchaseSound().play();//Play purchase sound
                } else {
                    store.getInvalidSound().play();//Play invalid selection sound
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Listener for the running shoes power up button which increases player speed
        store.getRunningShoesBtn().addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(player.getCoinsCollected() >= 50 && !player.hasRunningShoes()) {
                    player.setHasRunningShoes(true);
                    player.setCoinsCollected(player.getCoinsCollected() - 50);
                    hud.setCoinLabel(player.getCoinsCollected());
                    player.setSpeed(player.getSpeed() * 1.25f);
                    store.getPurchaseSound().play();//Play purchase sound
                } else {
                    store.getInvalidSound().play();//Play invalid selection sound
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Store button to purchase shield power up which reduces enemy damage taken by 50%
        store.getShieldBtn().addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //Player must have at least 60 coins and not already have the shield powerup
                if(player.getCoinsCollected() >= 60 && !player.hasShield()) {
                    player.setHasShield(true);
                    player.setCoinsCollected(player.getCoinsCollected() - 60);
                    hud.setCoinLabel(player.getCoinsCollected());
                    store.getPurchaseSound().play();//Play purchase sound
                } else {
                    store.getInvalidSound().play();//Play invalid selection sound
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Store button to purchase slower enemy respawn powerup which increases enemy respawn time by 50%
        store.getSlowerRespawnBtn().addListener(new ClickListener() {

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(player.getCoinsCollected() >= 100 && enemyRespawnTime < 40) {
                    enemyRespawnTime *= 1.5;//Increase enemy respawn time by 50%
                    player.setCoinsCollected(player.getCoinsCollected() - 100);
                    hud.setCoinLabel(player.getCoinsCollected());
                    store.getPurchaseSound().play();//Play purchase sound
                } else {
                    store.getInvalidSound().play();//Play invalid selection sound
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Open store button
        hud.getStoreBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //Only show the store if the pause menu isn't open
                if(!isPaused)
                    showStore = !showStore;

                //Take input on store, only when it is visible
                if(showStore)
                    multiplexer.addProcessor(storeProcessor);
                else
                    multiplexer.removeProcessor(storeProcessor);
            }
        });

        //Open pause menu button
        hud.getPauseBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //Only pause the game if the store isn't open
                if(!showStore)
                    isPaused = !isPaused;
            }
        });

        pauseMenu.getMainMenuBtn().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                dispose();
                game.setScreen(new MainMenuScreen(game));
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
            generateCoins();
        }

        //Draw enemies
        for(Enemy enemy : enemyArray)
            batch.draw(enemy.getEnemyImage(), enemy.getPosition().x, enemy.getPosition().y, Enemy.SIZE, Enemy.SIZE);//Draw enemy

        if(healthBonus != null)
            batch.draw(healthBonus.getHealthBonusImage(), healthBonus.getPosition().x, healthBonus.getPosition().y, HealthBonus.WIDTH, HealthBonus.HEIGHT);
        batch.end();
    }

    @Override
    public void render(float delta) {

        //Clear screen with specified color
        Gdx.gl.glClearColor(0.008f, 0.15f, 0.38f, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update camera, player, and coins
        if(!showStore && !isPaused) {

            cam.update();
            player.update();
            for (Coin coin : coinArray)
                coin.update();
            for (Enemy enemy : enemyArray)
                enemy.update();
            if(healthBonus != null)
                healthBonus.update();
            addEnemy();
            generateHealthBonus();
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
            store.getStage().act();
        }

        //Draw pause menu
        if(isPaused) {

            batch.setProjectionMatrix(pauseMenu.getStage().getCamera().combined);
            pauseMenu.getStage().draw();
        }

        //Check if game is over
        if(isGameOver()) {

            this.dispose();
            Preferences prefs = Gdx.app.getPreferences("Coin Getter Preferences");
            if(player.getScore() > prefs.getInteger("score")) {
                prefs.getInteger("score3", prefs.getInteger("score2"));
                prefs.getInteger("score2", prefs.getInteger("score"));
                prefs.putInteger("score", player.getScore());
            } else if(player.getScore() > prefs.getInteger("score2")) {
                prefs.getInteger("score3", prefs.getInteger("score2"));
                prefs.putInteger("score2", player.getScore());
            } else if(player.getScore() > prefs.getInteger("score3")) {
                prefs.putInteger("score3", player.getScore());
            }
            prefs.flush();
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

                if(!player.hasShield())
                    player.setHealth(player.getHealth() - 10);//Decrease health by 10 if hit by enemy
                else
                    player.setHealth(player.getHealth() - 5);//Decrease health by 5 if player has shield

                hud.setHealth(player.getHealth());//Update hud to reflect current player health
                enemy.setVelocity(enemy.getVelocity().x * -1, enemy.getVelocity().y * -1);
                player.setInvincible(true);

                Gdx.input.vibrate(200);//Android vibration for enemy collission
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

        //Check if player collided with the health bonus
        if(healthBonus != null && player.getBounds().overlaps(healthBonus.getBounds()) && player.getHealth() < 100) {
            player.setHealth(100);//Set player health to max health
            hud.setHealth(player.getHealth());//Update hud
            healthBonus = null;
        } else if(healthBonus != null && player.getBounds().overlaps(healthBonus.getBounds()) && player.getHealth() == 100) {
            player.setScore(player.getScore() + 500);//Increase score by 500 if player has full health when bonus is collected
            hud.setScore(player.getScore());
            healthBonus = null;
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
        if(healthBonus != null)
            healthBonus.dispose();
        hud.dispose();
        store.dispose();
        pauseMenu.dispose();
    }
}
