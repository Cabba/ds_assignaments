package it.unipr.aotlab.ds.server;

import it.unipr.aotlab.ds.chat.K;
import it.unipr.aotlab.ds.chat.Utils;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Send;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

  // CONNECTION
  private ServerSocket m_incoming_conn;
  private MulticastSocket m_multicastConn;
  private InetAddress m_multicastGroup;

  // STATE
  private List<String> m_user_list;

  public Server() {
    m_user_list = new ArrayList<String>();
  }

  private void startup() {
    System.out.println("SERVER RUNNING.");

    try {
      // Open TCP connection
      m_incoming_conn = new ServerSocket(K.TCP_SERVER_PORT);
      System.out.println("Server timeout: " + m_incoming_conn.getSoTimeout());
      // Open UDP socket
      m_multicastConn = new MulticastSocket(K.UDP_SERVER_PORT);
      m_multicastGroup = InetAddress.getByName(K.ADDRESS_UDP);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void listen() {
    try {
      Socket client_socket = m_incoming_conn.accept();
      //client_socket.setKeepAlive(true);
      System.out.println("Received connection request");

      ObjectInputStream is = new ObjectInputStream(
          client_socket.getInputStream());
      
      System.out.println("Reading object ...");
      Object o = is.readObject();
      System.out.println("Object readed.");
      
      String name = new String();
      boolean accepted = false;

      // Check message type
      if (o instanceof Join) {
        Join msg = (Join) o;
        name = msg.getName();

        if (addUser(msg.getName())) {
          System.out.println("Added user in user pool.");
          accepted = true;
        } else {
          System.out.println("Username '" + name + "' currently in use.");
        }
      }
      if (o instanceof Send) {
        // Reply in multicast group
        Send msg = (Send) o;
        System.out.println("Received Send message (" + msg.getName() + ", "
            + msg.getMessage() + ") ..");
        multicast(msg);
        System.out.println("Broadcasted message.");
      }

      if (accepted) {
        ObjectOutputStream os = new ObjectOutputStream(
            client_socket.getOutputStream());
        Join msg = new Join(name, accepted);
        os.writeObject(msg);
        os.flush();
        multicast(msg);
      }

      client_socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void multicast(Command com) {
    try {
      byte[] buffer = null;
      if(com instanceof Send)
        buffer = Utils.toByteArray((Send)com);
      if(com instanceof Join)
        buffer = Utils.toByteArray((Join)com);
      
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
          m_multicastGroup, K.UDP_CLIENT_PORT);
      m_multicastConn.send(packet);
    } catch (IOException e) {
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
   // new Thread(new Runnable() {
   //   public void run() {
        System.out.println("Incoming connection thread running.");
        while (true) {
          System.out.println("Thread iteration ...");
          listen();
        }
  //    }
  //  }).start();
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }

}
