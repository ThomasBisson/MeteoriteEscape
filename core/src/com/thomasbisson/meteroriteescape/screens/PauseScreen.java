package com.thomasbisson.meteroriteescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasbisson.meteroriteescape.MeteoriteEscape;

/**
 * Created by Thomas on 27/02/2017.
 */

public class PauseScreen implements Screen{
    private MeteoriteEscape game;
    private PlayScreen playScreen;
    private PauseScreen pauseScreen;

    private OrthographicCamera cam;
    private Viewport viewPort;
    private Sprite background;

    private Stage stage;
    private Table tableButton;
    private Skin skin;
    private TextureAtlas atlas;
    private BitmapFont font;
    private LabelStyle labelStyle;
    private Label lScoreTitle, lScore, lPersonalizedMessage;
    private TextButton bResume, bNewGame, bMenu, bOption , bExit;


    public PauseScreen(MeteoriteEscape game, PlayScreen playScreen){
        this.game = game;
        this.playScreen = playScreen;

        //The background
        background = new Sprite(new Texture(Gdx.files.internal("bg2.png")));
        background.setPosition(0,0);
        background.setSize(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT);

        //The camera and viewport
        cam = new OrthographicCamera();
        viewPort = new FitViewport(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT,cam);
        viewPort.apply();

        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
        this.pauseScreen=this;
    }
    @Override
    public void show() {
        stage = new Stage(viewPort, game.getSpritBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("skullbutton.pack");
        skin = new Skin(atlas);

        tableButton = new Table(skin);
        tableButton.center();
        tableButton.setFillParent(true);

        //TODO creat a font with freetype and stop using BitMapFont
        font = new BitmapFont();//Gdx.files.internal("font2.fnt"), false);
        font.getData().setScale(4);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("skullbutton.up");
        textButtonStyle.down = skin.getDrawable("skullbutton.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;

        bResume = new TextButton("RESUME", textButtonStyle);
        bResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                InputMultiplexer multiplexer = new InputMultiplexer();
                multiplexer.addProcessor(playScreen);
                multiplexer.addProcessor(playScreen.getHud().getStage());
                Gdx.input.setInputProcessor(multiplexer);
                game.setScreen(playScreen);
            }
        });
        bNewGame = new TextButton("NEW GAME", textButtonStyle);
        bNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });
        bOption = new TextButton("OPTION", textButtonStyle);
        bOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionScreen(game,pauseScreen));
            }
        });
        bMenu = new TextButton("MENU", textButtonStyle);
        bMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        bExit = new TextButton("EXIT", textButtonStyle);
        bExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        tableButton.add(bResume).width(80);
        tableButton.row();
        tableButton.add().height(35);
        tableButton.row();
        tableButton.add(bNewGame).width(80);
        tableButton.row();
        tableButton.add().height(35);
        tableButton.row();
        tableButton.add(bOption).width(80);
        tableButton.row();
        tableButton.add().height(35);
        tableButton.row();
        tableButton.add(bMenu).width(80);
        tableButton.row();
        tableButton.add().height(35);
        tableButton.row();
        tableButton.add(bExit).width(80);

        stage.addActor(tableButton);

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
    public void dispose() {}
}
