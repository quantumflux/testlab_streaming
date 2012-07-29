package com.we7.labs.teststream;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener{

    private Button mButtonPlay;
    private LocalStreamPlayer mLocalStreamPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void initialise(){
        
        findViewById(R.id.btn_play).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        
        if (v == findViewById(R.id.btn_play)){
            playTrack();
        }
        
    }
    
    private void playTrack(){
        
        if (mLocalStreamPlayer == null){
            
            mLocalStreamPlayer = new LocalStreamPlayer();
            
        }
        
        mLocalStreamPlayer.playTrack("Kalimba.mp3");
        
    }
    
}
