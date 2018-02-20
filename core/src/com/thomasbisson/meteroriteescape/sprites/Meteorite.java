package com.thomasbisson.meteroriteescape.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;
/**
 * Created by Thomas on 23/02/2017.
 */

public class Meteorite extends Sprite {

    private int gravity = -10;
    private Vector3 position;
    private Vector3 velocity;
    private Random random;

    private Rectangle bounds;

    public Meteorite(int x, int y) {
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        random = new Random();
        int r = random.nextInt(2);
        if(r == 0)
            this.setRegion(new Texture(Gdx.files.internal("meteorjaune.png")));
        else if(r == 1)
            this.setRegion(new Texture(Gdx.files.internal("meteor.png")));
        this.setPosition(x,y);
        this.setSize(64,100);
        bounds = new Rectangle();
        bounds.set(x, y+36, 55, 55);
    }

    public void update(float dt){
        //The gravity
        velocity.add(0,gravity,0);
        velocity.scl(dt);
        position.add(0,velocity.y,0);
        velocity.scl(1/dt);
        //Adaptation of the gravity to the sprite
        this.setPosition(position.x,position.y);
    }

    public Vector3 getPosition() {
        return position;
    }
    public Rectangle getBounds() { return bounds;   }

    public void dispose() {
        this.getTexture().dispose();
    }
}
