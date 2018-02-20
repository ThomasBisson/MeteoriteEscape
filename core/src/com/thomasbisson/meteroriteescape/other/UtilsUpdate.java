package com.thomasbisson.meteroriteescape.other;

import com.thomasbisson.meteroriteescape.MeteoriteEscape;
import com.thomasbisson.meteroriteescape.hud.HUD;
import com.thomasbisson.meteroriteescape.sprites.Meteorite;
import com.thomasbisson.meteroriteescape.sprites.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 05/06/2017.
 */

public class UtilsUpdate {

    //Check if player colide with meteor and put him in State "dead" if true
    public static void checkIfCollisionPlayerWithMeteorite(ArrayList<Meteorite> meteors, Player player) {
        for(int i=0; i<meteors.size(); i++){
            if(player.getBounds().overlaps(meteors.get(i).getBounds())){
                player.updateState(Player.State.DEAD);
                return;
            }

        }
    }

    //add a meteorite in function of a timer and the meteorites already present
    public static void meteoriteSpawn(ArrayList<Meteorite> meteors, int nbMaxMeteorite, int spawFrequency, MeteoriteTimer meteoriteTimer){
        Random meteorSpawnAndPosition = new Random();
        if (meteors.size() != nbMaxMeteorite) {
            if (meteorSpawnAndPosition.nextInt(spawFrequency) == 0 || meteoriteTimer.isTimeMaxExceeded())
                meteors.add(new Meteorite(meteorSpawnAndPosition.nextInt(MeteoriteEscape.V_WIDTH - 64), MeteoriteEscape.V_HEIGHT));
        }
    }

    //update meteorite and check if a meteorite is out of the screen, if true, he dispose and remove him from the arraylist
    public static void updateMeteoriteAndCheckOutOfScreen(float delta, ArrayList<Meteorite> meteors, HUD hud){
        for (int i = 0; i < meteors.size(); i++) {
            meteors.get(i).update(delta);
            meteors.get(i).getBounds().setPosition(meteors.get(i).getX(), meteors.get(i).getY());
            if (meteors.get(i).getPosition().y < 0-64) {
                hud.setScore(hud.getScore()+1);
                hud.update();
                meteors.get(i).dispose();
                meteors.remove(i);
                i--;
            }
        }
    }
}
