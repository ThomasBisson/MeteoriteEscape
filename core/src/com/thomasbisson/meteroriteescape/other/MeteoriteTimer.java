package com.thomasbisson.meteroriteescape.other;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Thomas on 23/02/2017.
 */

public class MeteoriteTimer {

    private final int MINUSMILLISECONDBYLEVEL = 3;
    private final int MILLISECONDMINI = 2;
    private int milliSecondPassedSinceLastMeteorite;
    private int milliSecondMax;
    private Timer timer;
    private TimerTask ts;

    public MeteoriteTimer() {
        //equals 2 secondes
        milliSecondMax = 20;
        timer = new Timer();
        ts = new TimerTask() {
            @Override
            public void run() { milliSecondPassedSinceLastMeteorite++; }
        };
        timer.scheduleAtFixedRate(ts,100,100);
    }

    public boolean isTimeMaxExceeded() {
        if(milliSecondPassedSinceLastMeteorite >= milliSecondMax) {
            milliSecondPassedSinceLastMeteorite = 0;
            return true;
        }
        return false;
    }

    public int  getMilliSecondMax()         { return milliSecondMax;          }
    public void setMilliSecondMax(int n)    { milliSecondMax = n;             }
    public int getMINUSMILLISECONDBYLEVEL() { return MINUSMILLISECONDBYLEVEL; }
    public int getMILLISECONDMINI()         { return MILLISECONDMINI;         }
}
