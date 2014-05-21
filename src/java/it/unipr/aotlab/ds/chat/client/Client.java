package it.unipr.aotlab.ds.chat.client;

import it.unipr.aotlab.ds.chat.rmi.ClientRMIImpl;
import it.unipr.aotlab.ds.chat.socket.ClientSockImpl;
import it.unipr.aotlab.ds.chat.jms.ClientJMSImpl;;

public class Client {

  public static void main(String[] args) {
    try{
      // Decomment the next line for socket implementation of the chat
      // ChatGui cg = new ChatGui(new ChatClientSockImpl());
      
      ChatGui cg = new ChatGui( new ClientRMIImpl() );
      cg.listen();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
