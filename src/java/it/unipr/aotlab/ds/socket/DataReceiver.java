package it.unipr.aotlab.ds.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * The {@code DataReceiver} class provides an implementation of a UDP receiver.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class DataReceiver
{
  private static final String ADDRESS = "230.0.0.1";
  private static final int DPORT      = 4446;
  private static final int SIZE       = 256;

  /**
   * Receives a message.
   *
  **/
  public void receive()
  {
    try
    {
      MulticastSocket socket = new MulticastSocket(DPORT);
      socket.joinGroup(InetAddress.getByName(ADDRESS));

      byte[] buf = new byte[SIZE];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);

      socket.receive(packet);

      System.out.println("Receiver received: " + new String(packet.getData()));

      socket.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Runs the receiver.
   *
   * @param v  the arguments.
   *
   * It does not need arguments.
   *
  **/
  public static void main(final String[] v)
  {
    new DataReceiver().receive();
  }
}
