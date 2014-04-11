package it.unipr.aotlab.ds.chat.rmi;

import it.unipr.aotlab.ds.chat.command.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICallbackClient extends Remote{

  /**
   * This method push a message into the client for processing.
   */
  public void pushMessage(Command message) throws InterruptedException, RemoteException;
  
}
