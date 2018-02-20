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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasbisson.meteroriteescape.MeteoriteEscape;
import com.thomasbisson.meteroriteescape.other.MyPreference;

/**
 * Created by Thomas on 06/06/2017.
 */

public class OptionScreen implements Screen {

    private MeteoriteEscape game;
    private Screen screen;

    private OrthographicCamera cam;
    private Viewport viewPort;
    private Sprite background;

    private Stage stage;
    private Table tableButton;
    private Skin skin;
    private TextureAtlas atlas;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private Label lScoreTitle, lScore, lPersonalizedMessage;
    private TextButton bResume, bNewGame, bMenu, bOption , bExit;


    public OptionScreen(MeteoriteEscape game, Screen screen){
        this.game = game;
        this.screen = screen;

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

        TextButton bExit = new TextButton("OK",textButtonStyle);
        bExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MyPreference.getPreferences().flush();
                game.setScreen(screen);
            }
        });

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOff = skin.getDrawable("skullbutton.up");
        checkBoxStyle.checkboxOn = skin.getDrawable("skullbutton.down");
        //checkBoxStyle.checked = skin.getDrawable("skullbutton.down");
        checkBoxStyle.pressedOffsetX = 1;
        checkBoxStyle.pressedOffsetY = -1;
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = Color.RED;

        final CheckBox isSoundOn = new CheckBox("sound",checkBoxStyle);
        isSoundOn.setChecked(MyPreference.getPreferences().getBoolean("sound", false));
        isSoundOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(isSoundOn.isChecked()) {
                    MyPreference.getPreferences().putBoolean("sound", true);
                    MyPreference.playBackMusic();
                } else {
                    MyPreference.getPreferences().putBoolean("sound", false);
                    MyPreference.stopBackMusic();
                }
            }
        });

        final CheckBox isVibrateOn = new CheckBox("vibration",checkBoxStyle);
        isVibrateOn.setChecked(MyPreference.getPreferences().getBoolean("vibration", false));
        isVibrateOn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(isVibrateOn.isChecked()){
                    MyPreference.getPreferences().putBoolean("vibrate", true);
                } else {
                    MyPreference.getPreferences().putBoolean("vibrate", false);
                }
            }
        });

        tableButton.add().height(300);
        tableButton.row();
        tableButton.add(isSoundOn).width(80);
        tableButton.row();
        tableButton.add().height(35);
        tableButton.row();
        tableButton.add(isVibrateOn).width(80);
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
