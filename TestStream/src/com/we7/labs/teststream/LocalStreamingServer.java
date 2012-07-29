package com.we7.labs.teststream;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;

public class LocalStreamingServer extends Thread {
    
    /**
     * Handler class for handling requests coming in through the serversoket
     * 
     * @param socket
     */
  private class SocketHandler implements Runnable {

    private final Socket mSocket;
    //private DefaultHttpServerConnection mHttpConnection;

    public SocketHandler(Socket socket) {

      super();
      mSocket = socket;
      
      /*
       * // Set up a HTTP protocol processor BasicHttpProcessor httpProcessor = new BasicHttpProcessor(); httpProcessor.addInterceptor(new ResponseServer()); httpProcessor.addInterceptor(new ResponseContent());
       * httpProcessor.addInterceptor(new ResponseConnControl());
       * 
       * // Set up a HTTP service DefaultHttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory(); DefaultConnectionReuseStrategy connReuseStrategy = new DefaultConnectionReuseStrategy(); HttpService mHttpService = new
       * HttpService(httpProcessor, connReuseStrategy, httpResponseFactory);
       * 
       * mHttpConnection = new DefaultHttpServerConnection();
       * 
       * try { mHttpConnection.bind(socket, mHttpService.getParams()); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
       */

    }

    

    @Override
    public void run() {

      DataOutputStream dos = null;

      try {

        dos = new DataOutputStream(mSocket.getOutputStream());

      } catch (IOException ioe) {
        
        ioe.printStackTrace();
        
      }

      BufferedInputStream bis = null;
      try {

          bis = new BufferedInputStream(new FileInputStream(mFile));

      } catch (FileNotFoundException fnfe) {
        
        fnfe.printStackTrace();
        
      }

      byte[] dataBuffer = new byte[4096];
      int n;

      try {

        while ((n = bis.read(dataBuffer)) != -1) {
          
          dos.write(dataBuffer, 0, n);
          
        }

      } catch (IOException ioe) {

        ioe.printStackTrace();
        
      }
      
      try {
          bis.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

  }

  public static final int PORT = 8080;
  
  private Handler mHandler;

  private ServerSocket mServerSocket;

  private boolean isRunning;

  private File mFile;

  public LocalStreamingServer() {

    mHandler = new Handler();

    start();

  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setFileToStream(File file) {

      mFile = file;

    }
  
  @Override
  public void destroy() {

      super.destroy();

      if (isRunning){
          this.interrupt();
      }
      
      closeSocket();

  }

  /**
   * If a server socket exists close it and set to null
   */
  private void closeSocket() {

    if (mServerSocket != null) {
        
      try {
        mServerSocket.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      mServerSocket = null;

    }

  }

  @Override
  public synchronized void run() {

      // close any existing socket
      closeSocket();

    // Create new server socket
    try {

      mServerSocket = new ServerSocket();

      InetSocketAddress address = new InetSocketAddress(PORT);
      mServerSocket.bind(address);

    } catch (IOException e) {

      // TODO Auto-generated catch block
      e.printStackTrace();

    }

    // wait for incoming requests
    while (!Thread.interrupted() && !mServerSocket.isClosed()) {

        
      isRunning = true;
        
      try {

          // Block until a request comes in
        final Socket newSocket = mServerSocket.accept();
        
        // handle request
        mHandler.post(new SocketHandler(newSocket));

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    
    isRunning = false;

  }

  @Override
  public synchronized void start() {
    // TODO Auto-generated method stub
    try{
        super.start();
    } catch (IllegalThreadStateException itse){
        itse.printStackTrace();
    }
  }

}
