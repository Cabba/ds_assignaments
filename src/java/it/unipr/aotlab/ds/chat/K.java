package it.unipr.aotlab.ds.chat;

public class K {

	//////////
	/// SOCKET
	//////////
	public static final int SIZE = 256;

	public static final int UDP_CLIENT_PORT = 44444;
	public static final int TCP_SERVER_PORT = 44446;
	public static final int UDP_SERVER_PORT = 44447;

	public static final String ADDRESS_TCP = "127.0.0.1";

	// Multicast IP range has to be in: 224.0.0.0 to 239.255.255.255, inclusive.
	public static final String ADDRESS_UDP = "239.100.100.1";

	///////
	/// JMS
	///////
	public final static String BROKER_URL = "tcp://localhost:61616";
	public final static String BROKER_PROPS = "persistent=false&useJmx=false";
	public final static String QUEUE_NAME  = "queue";
	public final static String TOPIC_NAME   = "queue";
}
