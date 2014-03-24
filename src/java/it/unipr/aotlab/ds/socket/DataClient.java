package it.unipr.aotlab.ds.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * The {@code DataClient} class provides an implementation of a TCP client.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class DataClient
{
  private static final int SPORT    = 4444;
  private static final String SHOST = "localhost";

  /**
   * Sends a request.
   *
  **/
  public void send()
  {
    try
    {
      Socket client = new Socket(SHOST, SPORT);

      BufferedReader is  =
          new BufferedReader(new InputStreamReader(client.getInputStream()));

      DataOutputStream os = new DataOutputStream(client.getOutputStream());

      os.writeBytes("Hello\n");

      System.out.println("Client received: " + is.readLine());

      client.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Runs the client.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    new DataClient().send();
  }
}

