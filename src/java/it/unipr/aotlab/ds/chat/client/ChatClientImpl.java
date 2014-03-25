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

import it.unipr.aotlab.ds.chat.K;
import it.unipr.aotlab.ds.chat.Utils;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Send;

public class ChatClientImpl implements ChatClient{
  
  private Socket m_connectionSocket;
  private MulticastSocket m_multicastSocket;
  
  
  @Override
  public Command receive(){
    System.out.println("Receive ...");
    final byte[] buffer = new byte[K.SIZE];
    DatagramPacket data = new DatagramPacket(buffer, buffer.length);
    Object o = null;
    try {
    m_multicastSocket.receive(data);
    o = Utils.toObject(data.getData());
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
    return (Command)o;
  }

  @Override
  public boolean join(String n){
    boolean accepted = false;
    
    try{
      m_connectionSocket = new Socket(K.ADDRESS_TCP, K.TCP_SERVER_PORT);

      ObjectOutputStream os = new ObjectOutputStream(
          m_connectionSocket.getOutputStream());

      // Sending join message to the server ...
      Join join = new Join(n);
      System.out.println("Sending message to server.");
      os.writeObject(join);
      os.flush();

      ObjectInputStream is = new ObjectInputStream(
          m_connectionSocket.getInputStream());
      // ... and wait for the reply.
      Object o = is.readObject();
      System.out.println("Server has replied.");

      // Check server reply
      if (o instanceof Join) {
        Join msg = (Join) o;
        accepted = msg.isAccepted();
      }
      m_connectionSocket.close();

      // Check the server reply ...
      if(accepted){
        System.out.println("Connected");
        // ... join the multicast group
        m_multicastSocket = new MulticastSocket(K.UDP_CLIENT_PORT);
        m_multicastSocket.joinGroup(InetAddress.getByName(K.ADDRESS_UDP));
      }else{
        System.out.println("Connection refused");
      }
      
    }catch(IOException e){
      e.printStackTrace();
    }catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return accepted;
  }

  @Override
  public void send(String n, String m) { 
    try {
      
      m_connectionSocket = new Socket(K.ADDRESS_TCP, K.TCP_SERVER_PORT);
      ObjectOutputStream os = new ObjectOutputStream(
          m_connectionSocket.getOutputStream());
      // Send message to the server ...
      Send send = new Send(n, m);
      System.out.println("Sending message (" + n + ", " + m + ") to server.");
      os.writeObject(send);
      os.flush();
      m_connectionSocket.close();
      os.close();
      System.out.println("Message sent.");
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void leave(String n) {
  
  }



}
