package it.unipr.aotlab.ds.chat.jms;

import java.util.ArrayList;
import java.util.List;

import it.unipr.aotlab.ds.chat.K;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Leave;
import it.unipr.aotlab.ds.chat.command.Send;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class ServerJMS {

	ActiveMQConnection m_connection;

	// Queue stuff
	QueueSession m_qSession;
	Queue m_queue;
	QueueReceiver m_receiver;

	// Topic stuff
	TopicSession m_tSession;
	Topic m_topic;
	TopicPublisher m_pub;

	// History
	List<Command> m_histo;
	// Connected clients
	List<String> m_clients;

	public ServerJMS() {
		m_histo = new ArrayList<Command>();
		m_clients = new ArrayList<String>();
	}

	// The server is a receiver (for client connections) and a publisher (for
	// char messages)
	public void run() {
		try {
			setupConnections();

			while (true) {
				System.out.println("[SERVER] Wait for messages...");
				// Send message to server
				ObjectMessage msg = (ObjectMessage) m_receiver.receive();

				if (msg.getObject() instanceof Join) {
					Join m = (Join) msg.getObject();
					System.out.println("Received a Join message from " + m.getName());
					// Broadcast only if the client is not in the list
					if (!m_clients.contains(m.getName()))
						broadcast(m);
					sendClientResponse(m.getName());
				}
				if (msg.getObject() instanceof Send) {
					Send m = (Send) msg.getObject();
					System.out.println("Received a Send message from " + m.getName());
					broadcast(m);
				}
				if (msg.getObject() instanceof Leave) {
					Leave m = (Leave) msg.getObject();
					System.out.println("Received a Leave message from " + m.getName());
					// Remove the client and add the message in the history
					m_clients.remove(m.getName());
					m_histo.add(m);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupConnections() throws Exception {
		BrokerService broker = BrokerFactory.createBroker("broker:(" + K.BROKER_URL + ")?" + K.BROKER_PROPS);

		broker.start();

		// Create a connection, for send messages, with the server
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(K.BROKER_URL);
		m_connection = (ActiveMQConnection) cf.createConnection();

		m_connection.start();

		// Setup the queue for receiving messages from clients
		m_qSession = m_connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		m_queue = m_qSession.createQueue(K.QUEUE_NAME);
		m_receiver = m_qSession.createReceiver(m_queue);

		// Setup the topic for publish messages
		m_tSession = m_connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		m_topic = m_tSession.createTopic(K.TOPIC_NAME);
		m_pub = m_tSession.createPublisher(m_topic);
	}

	private void broadcast(Command msg) throws Exception {
		// Add message to the global history
		m_histo.add(msg);

		// Broadcast message
		ObjectMessage obj = m_tSession.createObjectMessage(msg);
		m_pub.publish(obj);
	}

	private void sendClientResponse(String clientName) throws Exception {
		Queue responseQ = m_qSession.createQueue(clientName);
		QueueSender rec = m_qSession.createSender(responseQ);
		// Check client name
		if (m_clients.contains(clientName)) {
			System.out.println("[SERVER] Sending negative response to the client.");
			ObjectMessage msg = m_qSession.createObjectMessage(new JoinResponse(false));
			rec.send(msg);
		} else {
			System.out.println("[SERVER] Sending positive response to the client.");
			m_clients.add(clientName);
			// If no client is founded add the client and send the history
			ObjectMessage msg = m_qSession.createObjectMessage(new JoinResponse(true, K.TOPIC_NAME, m_histo));
			rec.send(msg);
		}
	}

	public static void main(String[] args) {
		ServerJMS server = new ServerJMS();

		server.run();
	}

}
