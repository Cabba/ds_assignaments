package it.unipr.aotlab.ds.chat.client;

import it.unipr.aotlab.ds.chat.rmi.ChatClientRMIImpl;
import it.unipr.aotlab.ds.chat.socket.ChatClientSockImpl;

public class Client {

  public static void main(String[] args) {
    try{
      // Decomment the next line for socket implementation of the chat
      // ChatGui cg = new ChatGui(new ChatClientSockImpl());
      
      ChatGui cg = new ChatGui( new ChatClientRMIImpl() );
      cg.listen();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
