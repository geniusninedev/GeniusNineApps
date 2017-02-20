package com.geniusnine.android.geniusnineapps;

/**
 * Created by AndriodDev8 on 20-02-2017.
 */
import android.app.Application;
public class GlobalData extends Application {
    private  int mainActivityCounter = 0;

    public int getMainActivityCounter() {
        return mainActivityCounter;
    }

    public void setMainActivityCounter(int mainActivityCounter) {
        this.mainActivityCounter = mainActivityCounter;
    }
}
