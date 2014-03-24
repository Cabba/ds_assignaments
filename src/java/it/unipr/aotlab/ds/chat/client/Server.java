package it.unipr.aotlab.ds.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

  private ServerSocket m_incoming_conn;

  private List<String> m_user_list;

  public Server() {
    m_user_list = new ArrayList<String>();
  }

  private void startup() {
    System.out.println("SERVER RUNNING.");

    try {
      m_incoming_conn = new ServerSocket(K.CONNECTION_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void listenConnection() {
    try {
      Socket client_socket = m_incoming_conn.accept();
      ObjectInputStream is = new ObjectInputStream(
          client_socket.getInputStream());
      ObjectOutputStream os = new ObjectOutputStream(
          client_socket.getOutputStream());

      Object o = is.readObject();
      System.out.println("Received connection request.");

      String name = new String();
      boolean accepted = false;
      
      if (o instanceof JoinChatMsg) {
        JoinChatMsg msg = (JoinChatMsg) o;
        name = msg.getUserName();

        if (addUser(msg.getUserName())) {
          System.out.println("Added user in user pool.");
          accepted = true;
        } else {
          System.out.println("Username '" + name + "' currently in use.");
        }
        
        // Send multicast replies
      }

      JoinChatMsg msg = new JoinChatMsg(name, accepted);

      os.writeObject(msg);
      os.flush();

      client_socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private boolean addUser(String user_name) {
    if (m_user_list.contains(user_name)) {
      return false;
    } else {
      m_user_list.add(user_name);
      return true;
    }
  }

  public void start() {
    startup();
    // Incoming connection thread
    new Thread(new Runnable() {
      public void run() {
        System.out.println("Incoming connection thread running.");
        while (true) {
          System.out.println("Wait for connection ...");
          listenConnection();
        }
      }
    }).start();
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }

}
