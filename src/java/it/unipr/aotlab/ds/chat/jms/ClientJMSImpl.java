package it.unipr.aotlab.ds.chat.jms;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import it.unipr.aotlab.ds.chat.K;
import it.unipr.aotlab.ds.chat.client.ChatClient;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;

public class ClientJMSImpl implements ChatClient {

	ActiveMQConnection m_connection;
	
	// Queue stuff
	QueueSession m_qSession;
	Queue m_queue;
	QueueSender m_sender;
	

	@Override
	public Command receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean join(String n) {
		try {
			// Create a connection, for send messages, with the server 
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
					K.BROKER_URL);
			m_connection = (ActiveMQConnection) cf.createConnection();

			m_connection.start();
			
			// Setup the connection
			m_qSession = m_connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			m_queue = m_qSession.createQueue(K.QUEUE_NAME);
			m_sender = m_qSession.createSender(m_queue);
			
			System.out.println("[CLIENT] Creating message ...");
			// Create join message
			ObjectMessage oMsg = m_qSession.createObjectMessage(new Join(n));
			
			System.out.print("[CLIENT] Sending message ...");
			// Send message to server
			m_sender.send(oMsg);
			System.out.print(" ok.\n");

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void send(String n, String m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(String n) {
		// TODO Auto-generated method stub

	}

}
