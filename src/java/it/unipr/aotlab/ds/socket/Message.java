package it.unipr.aotlab.ds.socket;

import java.io.Serializable;

/**
 *
 * The {@code Message} class defines a message.
 *
**/

public final class Message implements Serializable
{
  private static final long serialVersionUID = 1L;

  // Sender.
  private transient final Object sender;
  // Sender name.
  private final String name;
  // Message.
  private final String content;

  /**
   * Class constructor.
   *
   * @param c  the actor qualified class name.
   * @param v  the actor initialization arguments.
   *
  **/
  public Message(final Object s, final String n, final String c)
  {
    this.sender  = s;
    this.name    = n;
    this.content = c;
  }

  /**
   * Gets the sender.
   *
   * @return the sender.
   *
  **/
  public Object getSender()
  {
    return this.sender;
  }

  /**
   * Gets the sender name.
   *
   * @return the name.
   *
  **/
  public String geName()
  {
    return this.name;
  }

  /**
   * Gets the message content.
   *
   * @return the content.
   *
  **/
  public String geContent()
  {
    return this.content;
  }
}
