package it.unipr.aotlab.ds.chat.rmi;

import it.unipr.aotlab.ds.chat.command.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICallbackServer extends Remote{

  /**
   * This method send a message to the server. 
   * The server send the message at all the client subscribed onto the client subscriber.
   */
  public void pushMessage(Command command) throws InterruptedException, RemoteException;

}
