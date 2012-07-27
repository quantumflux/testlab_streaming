package com.we7.labs.teststream;

import java.net.ServerSocket;

import android.media.MediaPlayer;

public class LocalStreamPlayer {

  private static MediaPlayer sMediaPlayer;
  private LocalStreamingServer sLocalStreamingServer;
  

  
  public LocalStreamPlayer(){
   
    if (sMediaPlayer == null){
      
      sMediaPlayer = new MediaPlayer();
      
    }
    
    if (sLocalStreamingServer == null){
      
    } else if (!sLocalStreamingServer.isRunning()){
      
      
      
    }
    
    m
    
  }
  
  
}
