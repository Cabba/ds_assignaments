package it.unipr.aotlab.ds.chat.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientSubscriber extends UnicastRemoteObject implements IClientSubscriber {

  Map<String, ICallbackClient> m_clients;
  
  protected ClientSubscriber() throws RemoteException {
    super();
    m_clients = new ConcurrentHashMap<String, ICallbackClient>();
  }
  
  @Override
  public boolean subscribe(final String name, final ICallbackClient client) throws RemoteException {
    if( m_clients.containsKey(name) ) return false;
    m_clients.put(name, client);
    return true;
  }
  
  public void unsubscribe(final String name) throws RemoteException {
    m_clients.remove(name);
  }
  
  public ICallbackClient get(String name){
    return m_clients.get(name);
  }
  
  public List<ICallbackClient> getClients(){
    return new ArrayList<ICallbackClient>(m_clients.values());
  }

}
