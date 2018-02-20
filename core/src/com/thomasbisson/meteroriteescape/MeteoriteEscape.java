package com.thomasbisson.meteroriteescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thomasbisson.meteroriteescape.other.MyPreference;
import com.thomasbisson.meteroriteescape.screens.MenuScreen;

public class MeteoriteEscape extends Game {

	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 800;
	public SpriteBatch batch;

	//TODO mettre la cam et le viewport ici, ca évite de les recréés tout le temps
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getSpritBatch() { return batch; }

	@Override
	public void dispose () {
		batch.dispose();
	}
}
