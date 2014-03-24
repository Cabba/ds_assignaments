package it.unipr.aotlab.ds.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * The {@code ObjectClient} class provides an implementation of a TCP client.
 *
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 *
**/

public class ObjectClient
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

      ObjectOutputStream os =
              new ObjectOutputStream(client.getOutputStream());
      
      Message m = new Message(this, "Client", "hello");

      os.writeObject(m);
      os.flush();

      ObjectInputStream is =
          new ObjectInputStream(client.getInputStream());

      Object o = is.readObject();

      if (o instanceof Message)
      {
        m = (Message) o;

        System.out.println(
            "Client received: " + m.geContent() + " from " + m.geName());
      }

      client.close();
    }
    catch (IOException | ClassNotFoundException e)
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
    new ObjectClient().send();
  }
}

