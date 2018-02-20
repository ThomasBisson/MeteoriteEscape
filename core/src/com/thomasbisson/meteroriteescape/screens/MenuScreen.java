package com.thomasbisson.meteroriteescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasbisson.meteroriteescape.MeteoriteEscape;

/**
 * Created by Thomas on 21/02/2017.
 */

public class MenuScreen implements Screen, InputProcessor {

    private MeteoriteEscape game;
    private MenuScreen menuScreen;

    private OrthographicCamera cam;
    private Viewport viewPort;
    private Sprite background;

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private BitmapFont font;
    private TextButton buttonPlay, buttonOption , buttonExit;

    public MenuScreen(MeteoriteEscape game){
        this.game = game;
        background = new Sprite(new Texture(Gdx.files.internal("bg2.png")));
        background.setPosition(0,0);
        background.setSize(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT);
        cam = new OrthographicCamera();
        viewPort = new FitViewport(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT,cam);
        viewPort.apply();

        Gdx.input.setInputProcessor(this);
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
        this.menuScreen = this;
    }

    @Override
    public void show() {
        stage = new Stage(viewPort, game.getSpritBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("skullbutton.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.center();
        table.setFillParent(true);

        //TODO creat a font with freetype and stop using BitMapFont
        font = new BitmapFont();//Gdx.files.internal("font2.fnt"), false);
        font.getData().setScale(4);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("skullbutton.up");
        textButtonStyle.down = skin.getDrawable("skullbutton.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;

        buttonPlay = new TextButton("PLAY",   textButtonStyle);
        buttonPlay.addListener(new ClickListener() {
           @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
           }
        });
        buttonOption = new TextButton("OPTION", textButtonStyle);
        buttonOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionScreen(game, menuScreen));
            }
        });
        buttonExit = new TextButton("EXIT",   textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add().height(300);
        table.row();
        table.add(buttonPlay).width(80);
        table.row();
        table.add().height(35);
        table.row();
        table.add(buttonOption).width(80);
        table.row();
        table.add().height(35);
        table.row();
        table.add(buttonExit).width(80);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width,height);
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

    @Override
    public void dispose() {
        background.getTexture().dispose();
    }
}
