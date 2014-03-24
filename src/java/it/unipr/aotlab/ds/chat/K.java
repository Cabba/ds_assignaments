package it.unipr.aotlab.ds.chat;

public class K {

  public static final int SIZE = 1024;
  
  public static final int UDP_CLIENT_PORT = 44444;
  public static final int TCP_SERVER_PORT = 44446;
  public static final int UDP_SERVER_PORT = 44447;
  
  public static final String ADDRESS_TCP = "127.0.0.1";
  
  // Multicast IP range has to be in: 224.0.0.0 to 239.255.255.255, inclusive.
  public static final String ADDRESS_UDP = "239.100.100.1";

}
