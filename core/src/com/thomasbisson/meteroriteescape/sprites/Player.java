package com.thomasbisson.meteroriteescape.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.thomasbisson.meteroriteescape.screens.PlayScreen;

/**
 * Created by Thomas on 23/02/2017.
 */

public class Player extends Sprite{

    public enum State{ STANDING, RUNNING, DEAD}
    private State currentState;
    private State previousState;
    private float stateTimer;
    private boolean runningRight;
    private Animation<TextureRegion> playerRunningLeft;
    private Animation<TextureRegion> playerRunningRight;
    private TextureRegion playerStanding;
    private TextureRegion playerDead;
    private boolean isRunning;
    private boolean isDead;
    private boolean isStanding;

    private Rectangle bounds;

    private static final float HAST = 10;

    public Player(PlayScreen screen, int x, int y){
        currentState = State.STANDING;
        previousState = State.STANDING;
        isRunning = false;
        isDead = false;
        isStanding = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i=0; i<2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("running"), i * 64, 0, 64, 64));
        playerRunningRight = new Animation(0.2f, frames);

        frames.clear();

        TextureRegion tr;
        for(int i=0; i<2; i++) {
            tr = new TextureRegion(screen.getAtlas().findRegion("running"), i * 64, 0, 64, 64);
            tr.flip(true, false);
            frames.add(tr);
        }
        playerRunningLeft = new Animation(0.2f, frames);


        frames.clear();

        playerStanding = new TextureRegion(screen.getAtlas().findRegion("standing"), 0, 0, 64, 64);
        playerDead = new TextureRegion(screen.getAtlas().findRegion("dying"), 0, 0, 64, 64);

        this.setPosition(x,y);
        this.setSize(64,64);
        this.setRegion(playerStanding);

        bounds = new Rectangle();
        bounds.set(x, y, 55, 55);
    }

    public void turnLeft()  { this.translateX(-HAST); }
    public void turnRight() { this.translateX(HAST);  }

    public void update(float dt){
        //set the texture
        setRegion(getFrame(dt));
        //the player can't get out of the screen
        if(this.getX() < 0)
            this.setPosition(0,this.getY());
        else if(this.getX() > 417)
            this.setPosition(417, this.getY());
    }

    private TextureRegion getFrame(float dt) {
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch (currentState) {
            case DEAD:
                region = playerDead;
                break;
            case RUNNING:
                if(runningRight)
                    region = playerRunningRight.getKeyFrame(stateTimer,true);
                else
                    region = playerRunningLeft.getKeyFrame(stateTimer, true);
                break;
            default:
                region = playerStanding;
                break;
        }

        //if the current state is the same as the previous state increase the state timer. (for animation)
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    private State getState(){
        if(isDead)
            return State.DEAD;
        else if(isRunning)
            return State.RUNNING;
        else if (isStanding)
            return State.STANDING;
        else
            return State.STANDING;
    }

    public void updateState(State s){
        if(s == State.RUNNING) {
            isDead = false;
            isRunning = true;
            isStanding = false;
        } else if (s == State.DEAD) {
            isDead = true;
            isRunning = false;
            isStanding = false;
        } else if (s == State.STANDING) {
            isDead = false;
            isRunning = false;
            isStanding = true;
        }
    }

    public boolean isDead(){ return isDead; }

    public void setRunningRight(boolean bool) { runningRight = bool; }
    public Rectangle getBounds()              { return bounds;       }
}
