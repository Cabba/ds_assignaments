package it.unipr.aotlab.ds.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * The {@code DataSender} class provides an implementation of a UDP sender.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class DataSender
{
  private static final int SPORT      = 4444;
  private static final String ADDRESS = "230.0.0.1";
  private static final int DPORT      = 4446;

  /**
   * Sends a message.
   *
  **/
  public void send()
  {
    try
    {
      DatagramSocket socket = new DatagramSocket(SPORT);
      InetAddress group = InetAddress.getByName(ADDRESS);

      String s = "Hello\n";
      byte[] buf = s.getBytes();
      DatagramPacket packet =
              new DatagramPacket(buf, buf.length, group, DPORT);

      socket.send(packet);

      socket.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Runs the sender.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    new DataSender().send();
  }
}
