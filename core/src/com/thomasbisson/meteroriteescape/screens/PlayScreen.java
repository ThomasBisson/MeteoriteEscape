package com.thomasbisson.meteroriteescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasbisson.meteroriteescape.MeteoriteEscape;
import com.thomasbisson.meteroriteescape.hud.HUD;
import com.thomasbisson.meteroriteescape.other.MeteoriteTimer;
import com.thomasbisson.meteroriteescape.other.MyPreference;
import com.thomasbisson.meteroriteescape.other.UtilsUpdate;
import com.thomasbisson.meteroriteescape.sprites.Meteorite;
import com.thomasbisson.meteroriteescape.sprites.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * The class where we play
 * Created by Thomas on 22/02/2017.
 */

public class PlayScreen implements Screen, InputProcessor {

    private final int STARTINGNBMETEORITE = 3;

    private final int STARTINGSPAWNFREQUENCY = 200;
    private final int MINUSSPAWFREQUENCYBYLEVEL = 30;
    private final int MINISPAWNFREQUENCY = 30;

    private MeteoriteEscape game;

    private OrthographicCamera cam;
    private Viewport gamePort;
    private Sprite background;

    private TextureAtlas atlas;

    private boolean leftInput = false;
    private boolean rightInput = false;

    private ArrayList<Meteorite> meteors;
    private int nbMaxMeteorite;
    private int spawFrequency;
    private Player player;
    private MeteoriteTimer meteoriteTimer;

    private HUD hud;

    public PlayScreen(MeteoriteEscape game){
        this.game = game;

        MyPreference.playBackMusic();

        atlas = new TextureAtlas("animationplayer2.pack");
        //The background
        background = new Sprite(new Texture(Gdx.files.internal("bg2.png")));
        background.setPosition(0,0);
        background.setSize(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT);
        //the player
        player = new Player(this,100,0);
        //the meteor
        nbMaxMeteorite = STARTINGNBMETEORITE;
        spawFrequency = STARTINGSPAWNFREQUENCY;
        meteors = new ArrayList<Meteorite>();

        //The camera and viewport
        cam = new OrthographicCamera();
        gamePort = new FitViewport(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT,cam);
        gamePort.apply();

        //Timer
        meteoriteTimer = new MeteoriteTimer();

        //hud
        hud = new HUD(game.getSpritBatch(),gamePort, game, this);

        //The input
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(hud.getStage());
        Gdx.input.setInputProcessor(multiplexer);

        //Tell the camera to be at the center of the screen
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        background.draw(game.batch);
        player.draw(game.batch);
        for(int i=0; i<meteors.size(); i++)
            meteors.get(i).draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        //hud.getStage().act();
        hud.getStage().draw();
    }

    private void update(float delta) {
        if (player.isDead())
            gameOver();
        //create a meteorite if random is OK and timer exceeded
        UtilsUpdate.meteoriteSpawn(meteors,nbMaxMeteorite, spawFrequency, meteoriteTimer);

        int previousLevel = hud.getLevel();
        //update meteorite position and check if they are out of the screen
        UtilsUpdate.updateMeteoriteAndCheckOutOfScreen(delta, meteors, hud);

        //if the level increased then we update the difficilty
        if(previousLevel != hud.getLevel())
            updateDifficulty();

        //Check if player colide with meteor and put him in State "dead" if true
        UtilsUpdate.checkIfCollisionPlayerWithMeteorite(meteors,player);

        //update player
        player.update(delta);
        cam.update();
        //if the user touch the screen without getting off his hand
        if(rightInput)
            player.turnRight();
        if(leftInput)
            player.turnLeft();
        player.getBounds().setPosition(player.getX(), player.getY());
    }

    private void updateDifficulty(){
        nbMaxMeteorite = STARTINGNBMETEORITE + hud.getLevel();
        spawFrequency = spawFrequency - MINUSSPAWFREQUENCYBYLEVEL;
        if(spawFrequency < MINISPAWNFREQUENCY)
            spawFrequency = MINISPAWNFREQUENCY;
        if(meteoriteTimer.getMILLISECONDMINI() < meteoriteTimer.getMilliSecondMax() )
            meteoriteTimer.setMilliSecondMax(meteoriteTimer.getMilliSecondMax() - meteoriteTimer.getMINUSMILLISECONDBYLEVEL());
        else
            meteoriteTimer.setMilliSecondMax(meteoriteTimer.getMILLISECONDMINI());
    }

    private void gameOver(){
        MyPreference.stopBackMusic();
        MyPreference.playHitSound();
        MyPreference.vibrate();
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }
        game.setScreen(new EndScreen(game, hud.getScore(), hud.getLevel()));
    }

    public void refreshHUDPlayScreen() {
        hud.setPlayScreen(this);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //TODO bien réglé le touché de l'écran
        if (screenX < Gdx.graphics.getWidth()/2){
            leftInput = true;
            rightInput = false;
            player.setRunningRight(false);
        }
        else {
            rightInput = true;
            leftInput = false;
            player.setRunningRight(true);
        }
        player.updateState(Player.State.RUNNING);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX < Gdx.graphics.getWidth()/2)
            leftInput = false;
        else
            rightInput = false;
        player.updateState(Player.State.STANDING);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public TextureAtlas getAtlas() { return atlas; }
    public void setRightInput(boolean bool) { rightInput = bool; }
    public void setLeftInput(boolean bool)  { leftInput = bool; }
    public Player getPlayer() { return player; }
    public HUD getHud() { return hud; }

    @Override
    public void dispose() { }
}
