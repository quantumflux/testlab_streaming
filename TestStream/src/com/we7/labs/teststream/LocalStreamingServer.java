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
import java.net.SocketAddress;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseServer;

import android.os.Handler;
import android.util.Log;

public class LocalStreamingServer extends Thread {

  private class SocketHandler implements Runnable {

    private final Socket mSocket;
    private DefaultHttpServerConnection mHttpConnection;
    private File mFile;

    public SocketHandler(Socket socket) {

      super();
      this.mSocket = socket;

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

    public void setFileToStream(File file) {

      mFile = file;

    }

    @Override
    public void run() {

      DataOutputStream dos = null;

      try {

        dos = new DataOutputStream(mSocket.getOutputStream());

      } catch (IOException ioe) {
        
        ioe.printStackTrace();
        
      }

      BufferedInputStream inputStream = null;
      try {

        inputStream = new BufferedInputStream(new FileInputStream(mFile));

      } catch (FileNotFoundException fnfe) {
        
        fnfe.printStackTrace();
        
      }

      byte[] dataBuffer = new byte[4096];
      int n;

      try {

        while ((n = inputStream.read(dataBuffer)) != -1) {
          
          dos.write(dataBuffer, 0, n);
          
        }

      } catch (IOException ioe) {

        ioe.printStackTrace();
        
      }
    }

  }

  private Handler mHandler;

  private ServerSocket mServerSocket;

  private boolean isRunning;

  public LocalStreamingServer() {

    mHandler = new Handler();

  }

  public boolean isRunning() {
    return isRunning;
  }

  @Override
  public void destroy() {
    super.destroy();

    closeSocket();

  }

  /**
   * If a server socket exists close it and set to null
   */
  private void closeSocket() {

    if (mServerSocket != null && !mServerSocket.isClosed()) {

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
  public void run() {

    closeSocket();

    try {

      mServerSocket = new ServerSocket();

      InetSocketAddress address = new InetSocketAddress(8080);
      mServerSocket.bind(address);

    } catch (IOException e) {

      // TODO Auto-generated catch block
      e.printStackTrace();

    }

    while (!Thread.interrupted() && !mServerSocket.isClosed()) {

      try {

        final Socket newSocket = mServerSocket.accept();
        mHandler.post(new SocketHandler(newSocket));

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }

  }

  @Override
  public synchronized void start() {
    // TODO Auto-generated method stub
    super.start();
  }

}
