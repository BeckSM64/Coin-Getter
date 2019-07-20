package com.becksm64.coingetter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class HealthBonus {

    public static final float WIDTH = Gdx.graphics.getWidth() / 23.0f;
    public static final float HEIGHT = Gdx.graphics.getHeight() / 12.0f;

    private Texture healthBonusImage;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;

    public HealthBonus(int x, int y, int velX, int velY) {

        healthBonusImage = new Texture("sprites/healthBonus.png");
        position = new Vector3(x, y, 0);
        velocity = new Vector3(velX, velY, 0);
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        position.add(velocity.x, velocity.y, 0);
        setBounds(position.x, position.y);
        constrainHealthBonus();
    }

    public void constrainHealthBonus() {

        if(position.x > Gdx.graphics.getWidth() - WIDTH || position.x < 0) {
            velocity.x *= -1;
        } else if(position.y > Gdx.graphics.getHeight() - HEIGHT || position.y < 0) {
            velocity.y *= -1;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    private void setBounds(float x, float y) {
        bounds.setPosition(x, y);
    }

    public Texture getHealthBonusImage() {
        return healthBonusImage;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position = new Vector3(x, y, 0);
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(int velX, int velY) {
        this.velocity = new Vector3(velX, velY, 0);
    }

    public void dispose() {
        healthBonusImage.dispose();
    }
}
