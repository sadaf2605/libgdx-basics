package com.me.mygdxgame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainActivity extends AndroidApplication {
    @Override
        public void onCreate (Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                initialize(new StarAssault(), false);               
        }
}