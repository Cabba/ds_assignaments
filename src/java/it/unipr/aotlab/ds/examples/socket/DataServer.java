package it.unipr.aotlab.ds.examples.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * The {@code DataServer} class provides an implementation of a TCP server.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class DataServer
{
  private static final int SPORT = 4444;

  /**
   * Manages a request and sends a reply.
   *
  **/
  public void reply()
  {
    try
    {
      ServerSocket server = new ServerSocket(SPORT);

      Socket client = server.accept();

      BufferedReader is  =
          new BufferedReader(new InputStreamReader(client.getInputStream()));

      DataOutputStream os = new DataOutputStream(client.getOutputStream());

      System.out.println("Server received: " + is.readLine());
      os.writeBytes("Hello\n");

      client.close();
      server.close();
    }
    catch (IOException e)
    {
        e.printStackTrace();
    }
  }

  /**
   * Runs the server.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    new DataServer().reply();
  }
}
