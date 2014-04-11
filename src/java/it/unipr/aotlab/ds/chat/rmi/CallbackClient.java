package it.unipr.aotlab.ds.chat.rmi;

import it.unipr.aotlab.ds.chat.command.Command;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CallbackClient extends UnicastRemoteObject implements ICallbackClient{
  
  private static final long serialVersionUID = 1L;
  private final BlockingQueue<Command> m_messages;

  public CallbackClient() throws RemoteException{
    super();
    m_messages = new LinkedBlockingQueue<Command>();
  }
  
  @Override
  public void pushMessage(Command message) throws InterruptedException, RemoteException{
    m_messages.put(message);
  }
  
  public Command popMessage() throws InterruptedException{
      return m_messages.take();
  }

}
