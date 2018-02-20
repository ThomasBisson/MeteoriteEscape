package com.thomasbisson.meteroriteescape.screens;

import com.badlogic.gdx.Gdx;
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

public class EndScreen implements Screen {

    private MeteoriteEscape game;

    private OrthographicCamera cam;
    private Viewport viewPort;
    private Sprite background;

    private Stage stage;
    private Table table;
    private Skin skin;
    private TextureAtlas atlas;
    private BitmapFont font;
    private LabelStyle labelStyle;
    private Label lScoreTitle, lScore, lPersonalizedMessage;
    private TextButton bNewGame, bMenu, bExit;

    private int score;
    private int level;
    private String[] arrayPersonalizedMessage = { "less than 10cm", "Loser", "Scumbag", "Beginner", "Warrior", "Advanced warrior", "BADASS"};

    public EndScreen(MeteoriteEscape game, int score, int level){
        this.game = game;
        this.score = score;
        this.level = level;

        //The background
        background = new Sprite(new Texture(Gdx.files.internal("bg2.png")));
        background.setPosition(0,0);
        background.setSize(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT);

        //The camera and viewport
        cam = new OrthographicCamera();
        viewPort = new FitViewport(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT,cam);
        viewPort.apply();

        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
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

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("skullbutton.up");
        textButtonStyle.down = skin.getDrawable("skullbutton.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;

        bNewGame = new TextButton("NEW GAME",   textButtonStyle);
        bNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });
        bMenu = new TextButton("MENU", textButtonStyle);
        bMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        bExit = new TextButton("EXIT",   textButtonStyle);
        bExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        labelStyle = new Label.LabelStyle(font, Color.RED);
        lScoreTitle = new Label("SCORE", labelStyle);
        lScore = new Label(String.format("%05d", score),labelStyle);
        lPersonalizedMessage = new Label(arrayPersonalizedMessage[level], labelStyle);



        table.add(lScoreTitle);
        table.row();
        table.add(lScore);
        table.row();
        table.add().height(70);
        table.row();
        table.add(lPersonalizedMessage);
        table.row();
        table.add().height(70);
        table.row();
        table.add(bNewGame).width(80);
        table.row();
        table.add().height(35);
        table.row();
        table.add(bMenu).width(80);
        table.row();
        table.add().height(35);
        table.row();
        table.add(bExit).width(80);

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
    public void dispose() {}
}
