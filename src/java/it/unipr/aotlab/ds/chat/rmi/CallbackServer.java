package it.unipr.aotlab.ds.chat.rmi;

import it.unipr.aotlab.ds.chat.command.Command;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// TODO
public class CallbackServer extends UnicastRemoteObject implements ICallbackServer {

  private static final long serialVersionUID = 1L;
  private final BlockingQueue<Command> m_messages;
  
  protected CallbackServer() throws RemoteException {
    super();
    m_messages = new LinkedBlockingQueue<Command>();
  }

  @Override
  public void pushMessage(Command command) throws InterruptedException, RemoteException {
    m_messages.put(command);
  }

  public Command popMessage() throws InterruptedException{
    return m_messages.take();
  }
}
