package it.unipr.aotlab.ds.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * The {@code DataServer} class provides an implementation of a TCP server.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class ObjectServer
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

      ObjectInputStream is =
          new ObjectInputStream(client.getInputStream());

      Object o = is.readObject();

      if (o instanceof Message)
      {
        Message m = (Message) o;

        System.out.println(
            "Server received: " + m.geContent() + " from " + m.geName());
      }

      ObjectOutputStream os =
          new ObjectOutputStream(client.getOutputStream());

      os.writeObject(new Message(this, "Server", "hello"));
      os.flush();

      client.close();
      server.close();
    }
    catch (IOException | ClassNotFoundException e)
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
    new ObjectServer().reply();
  }
}
