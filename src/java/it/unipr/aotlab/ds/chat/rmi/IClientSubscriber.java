package it.unipr.aotlab.ds.chat.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientSubscriber extends Remote {

  boolean subscribe(final String name, final ICallbackClient client) throws RemoteException;

  //void unsubscribe(final ICallbackClient client) throws RemoteException;

  void unsubscribe(final String client) throws RemoteException;
}
