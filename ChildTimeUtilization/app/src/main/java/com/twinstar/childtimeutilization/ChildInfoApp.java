package com.twinstar.childtimeutilization;

/**
 * Created by JOE on 3/24/2016.
 */

import android.app.Application;


import java.text.NumberFormat;
import java.text.ParsePosition;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class ChildInfoApp extends Application {
    private static ChildInfoApp instance;

    private String DELIMITER = "ZZZ";
    private Integer nCurrentId = 0;

    public static ChildInfoApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public void setCurrentId(Integer i) {
        synchronized (nCurrentId) {
            nCurrentId = i;
        }
    }

    public Integer getCurrentId() {
        synchronized (nCurrentId) {
            return nCurrentId;
        }
    }
    public String getDelimiter() {
        synchronized (DELIMITER) {
            return DELIMITER;
        }
    }

}
