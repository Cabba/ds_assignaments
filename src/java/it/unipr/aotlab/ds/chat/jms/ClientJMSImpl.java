package it.unipr.aotlab.ds.chat.jms;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import it.unipr.aotlab.ds.chat.K;
import it.unipr.aotlab.ds.chat.client.ChatClient;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Leave;
import it.unipr.aotlab.ds.chat.command.Send;

public class ClientJMSImpl implements ChatClient {

	ActiveMQConnection m_connection;

	// Queue stuff
	QueueSession m_qSession;
	Queue m_queue;
	QueueSender m_sender;

	// Topic stuff
	TopicSession m_tSession;
	Topic m_topic;
	TopicSubscriber m_sub;

	// History
	List<Command> m_histo;

	@Override
	public Command receive() {
		// Process the history
		if (m_histo.size() != 0) {
			return m_histo.remove(0);
		}

		System.out.println("[CLIENT] Wait for some message ...");
		try {
			// Parse the message
			ObjectMessage msg = (ObjectMessage) m_sub.receive();
			if (msg.getObject() instanceof Command)
				return (Command) msg.getObject();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean join(String n) {
		try {
			// Create a connection, for send messages, with the server
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(K.BROKER_URL);
			m_connection = (ActiveMQConnection) cf.createConnection();

			m_connection.start();

			// Setup the connection
			m_qSession = m_connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			m_queue = m_qSession.createQueue(K.QUEUE_NAME);
			m_sender = m_qSession.createSender(m_queue);

			System.out.println("[CLIENT] Queue created, creating message ...");
			// Create join message
			ObjectMessage oMsg = m_qSession.createObjectMessage(new Join(n));

			System.out.print("[CLIENT] Sending message ...");
			// Send message to server
			m_sender.send(oMsg);
			System.out.print(" ok.\n");

			// The client wait for a response in a queue named like username
			Queue response = m_qSession.createQueue(n);
			QueueReceiver receiver = m_qSession.createReceiver(response);

			System.out.print("[CLIENT] Wait for a response ...");
			oMsg = (ObjectMessage) receiver.receive();
			System.out.print("received\n");
			
			if (oMsg.getObject() instanceof JoinResponse) {
				JoinResponse resp = (JoinResponse) oMsg.getObject();
				if (!resp.accepted()){
					System.out.println("[CLIENT] Connecton refused.");
					receiver.close();
					return false;
				}
				else {
					System.out.print("[CLIENT] Subscribing the topic ...");
					// Subscribe the topic and process the history
					m_tSession = m_connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
					m_topic = m_tSession.createTopic(resp.getTopicName());
					m_sub = m_tSession.createSubscriber(m_topic);
					m_histo = resp.getHistory();
					System.out.print("subscribed\n");
					receiver.close();
					return true;
				}
			} else
				return false;

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void send(String n, String m) {
		try {
			ObjectMessage msg = m_qSession.createObjectMessage(new Send(n, m));
			m_sender.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leave(String n) {
		try {
			ObjectMessage msg = m_qSession.createObjectMessage(new Leave(n));
			m_sender.send(msg);
			
			m_connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
