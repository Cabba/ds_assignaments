package it.unipr.aotlab.ds.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.fusesource.hawtbuf.ByteArrayInputStream;

/**
 *
 * The {@code DataReceiver} class provides an implementation of a UDP receiver.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class ObjectReceiver
{
  private static final String ADDRESS = "230.0.0.1";
  private static final int DPORT      = 4446;
  private static final int SIZE       = 1024;

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

      Object o = toObject(packet.getData());

      if (o instanceof Message)
      {
        Message m = (Message) o;

        System.out.println(
            "Receiver received: " + m.geContent() + " from " + m.geName());
      }

      socket.close();
    }
    catch (IOException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  private Object toObject(byte[] b) throws IOException, ClassNotFoundException
  {
    ObjectInputStream s =
        new ObjectInputStream (new ByteArrayInputStream(b));

    Object o = s.readObject();
    s.close();

    return o;
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
    new ObjectReceiver().receive();
  }
}
