package com.we7.labs.teststream;

import java.io.File;
import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;

public class LocalStreamPlayer implements OnPreparedListener {

    
    
  private static MediaPlayer sMediaPlayer;
  private static LocalStreamingServer sLocalStreamingServer;
  
  public static final File BASE = Environment.getExternalStorageDirectory();
  
  public LocalStreamPlayer(){
   
    if (sMediaPlayer == null){
      
      sMediaPlayer = new MediaPlayer();
      
    }
    
    if (sLocalStreamingServer == null){
      
        sLocalStreamingServer = new LocalStreamingServer();
        
    } 
        
        
    if (!sLocalStreamingServer.isRunning()){
      
        sLocalStreamingServer.start();
      
    }

  }
  
  public void playTrack(String track){
      
      File trackFile = new File(Environment.getExternalStorageDirectory(), "/streamtest/" + track);
      
      if (trackFile.canRead()){
          
          sMediaPlayer.reset();
          
          while (!sLocalStreamingServer.isRunning()){
              
          }
          
          sLocalStreamingServer.setFileToStream(trackFile);
          
        try {
            sMediaPlayer.setDataSource("http://127.0.0.1:" + LocalStreamingServer.PORT + "/?track=" + track);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
          sMediaPlayer.prepareAsync();
          sMediaPlayer.setOnPreparedListener(this);
          
      }
      
  }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
  
  
}
