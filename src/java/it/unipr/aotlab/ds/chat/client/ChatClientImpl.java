package it.unipr.aotlab.ds.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;

import it.unipr.aotlab.ds.chat.command.Command;

public class ChatClientImpl implements ChatClient{
  
  private Socket m_connection_socket;
  private MulticastSocket m_comunication_socket;
  
  private InetAddress addr;
  
  
  @Override
  public Command receive() {
    return null;
  }

  @Override
  public boolean join(String n){
    boolean accepted = false;
    
    try{
    m_connection_socket = new Socket(K.ADDRESS, K.CONNECTION_PORT);
    
    ObjectOutputStream os = new ObjectOutputStream(m_connection_socket.getOutputStream());
    ObjectInputStream is = new ObjectInputStream(m_connection_socket.getInputStream());
    
    JoinChatMsg join = new JoinChatMsg(n);
    System.out.println("Sending message to server.");
    os.writeObject(join);
    os.flush();
    
    Object o = is.readObject();
    System.out.println("Server has replied.");
    
    if(o instanceof JoinChatMsg){
      JoinChatMsg msg = (JoinChatMsg)o;
      accepted = msg.requestAccepted();
    }
    m_connection_socket.close();
    
    }catch(IOException e){
      e.printStackTrace();
    }catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    if(accepted)
      System.out.println("Connected");
    else 
      System.out.println("Connection refused");
    
    return accepted;
  }

  @Override
  public void send(String n, String m) {    
  }

  @Override
  public void leave(String n) {
  
  }



}
