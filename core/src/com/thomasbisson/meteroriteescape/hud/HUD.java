package com.thomasbisson.meteroriteescape.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasbisson.meteroriteescape.MeteoriteEscape;
import com.thomasbisson.meteroriteescape.screens.PauseScreen;
import com.thomasbisson.meteroriteescape.screens.PlayScreen;
import com.thomasbisson.meteroriteescape.sprites.Player;

/**
 * Created by Thomas on 23/02/2017.
 */

public class HUD {

    private final int METEORITEBYLEVEL = 10;

    private MeteoriteEscape game;
    private PlayScreen playScreen;
    private Viewport vp;
    private SpriteBatch sb;

    private Stage stage;
    private Table tableScore, tablePauseButton;
    private TextureAtlas atlas;
    private Skin skin;

    private int score;
    private int level;

    private LabelStyle labelStyle;
    private BitmapFont font;
    private Label lScoreTitle;
    private Label lScore;
    private Label lLevelTitle;
    private Label lLevel;

    private Button bPause;

    public HUD(SpriteBatch sb, Viewport viewport, final MeteoriteEscape game, final PlayScreen playScreen_){
        this.sb = sb;
        vp = new FitViewport(MeteoriteEscape.V_WIDTH,MeteoriteEscape.V_HEIGHT,new OrthographicCamera());
        vp.apply();
        this.game = game;
        this.playScreen = playScreen_;

        stage = new Stage(vp,sb);

        atlas = new TextureAtlas("buttonpause.pack");
        skin = new Skin(atlas);

        //Table with the score and the label
        tableScore = new Table();
        tableScore.top();
        tableScore.setSize(MeteoriteEscape.V_WIDTH,155);
        tableScore.setPosition(0,650);
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm.setColor(Color.BLACK);
        pm.fill();
        tableScore.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm))));

        font = new BitmapFont();
        font.getData().setScale(4);
        labelStyle = new LabelStyle(font, Color.WHITE);

        score = 0;
        level = 0;
        lScoreTitle = new Label("Score", labelStyle);
        lScore = new Label(String.format("%05d", score),labelStyle);
        lLevelTitle = new Label("Level", labelStyle);
        lLevel = new Label(String.format("%02d", level),labelStyle);

        tableScore.add(lLevelTitle).expandX();
        tableScore.add(lScoreTitle).expandX();
        tableScore.row();
        tableScore.add(lLevel);
        tableScore.add(lScore);

        //Table with the pause button
        tablePauseButton = new Table(skin);
        tablePauseButton.top().right();
        tablePauseButton.setSize(MeteoriteEscape.V_WIDTH,40);
        tablePauseButton.setPosition(0,605);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = skin.getDrawable("bouttonpause.up");
        buttonStyle.down = skin.getDrawable("bouttonpause.down");
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.pressedOffsetY = -1;

        bPause = new Button(buttonStyle);
        bPause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playScreen.refreshHUDPlayScreen();
                playScreen.setRightInput(false);
                playScreen.setLeftInput(false);
                playScreen.getPlayer().setRunningRight(false);
                playScreen.getPlayer().updateState(Player.State.STANDING);
                game.setScreen(new PauseScreen(game, playScreen));
                return false;
            }
        });

        tablePauseButton.add(bPause).height(50).width(50);

        //add the tables to the stage
        stage.addActor(tableScore);
        stage.addActor(tablePauseButton);
    }

    public void setPlayScreen(PlayScreen playScreen) { this.playScreen = playScreen; }

    public Stage getStage()         { return stage;       }
    public int getScore()           { return score;       }
    public void setScore(int score) { this.score = score; }
    public int getLevel()           { return level;       }
    public void update(){
        int newLevel = score/METEORITEBYLEVEL;
        if(newLevel != level)
            level = newLevel;
        lLevel.setText(String.format("%02d", level));
        lScore.setText(String.format("%05d", score));
    }
}
