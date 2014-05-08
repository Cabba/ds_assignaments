package it.unipr.aotlab.ds.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 
 * Class providing two implementation of the chat client
 * for socket based communication.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
**/

public class Subscriber
{
  private final static String BROKER_URL   = "tcp://localhost:61616";
  private final static String BROKER_PROPS = "persistent=false&useJmx=false";
  private final static String TOPIC_NAME   = "queue";

  public void receive()
  {
    ActiveMQConnection connection = null;

    try
    {
      BrokerService broker = BrokerFactory.createBroker(
          "broker:(" + BROKER_URL + ")?" + BROKER_PROPS);

      broker.start();

      ActiveMQConnectionFactory cf =
        new ActiveMQConnectionFactory(Subscriber.BROKER_URL);
      connection = (ActiveMQConnection) cf.createConnection();
      
      connection.start();
      
      TopicSession session =
        connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      Topic topic = session.createTopic(TOPIC_NAME);
      TopicSubscriber subscriber = session.createSubscriber(topic);

      while (true)
      {
        Message message = subscriber.receive();

        if (message instanceof TextMessage)
          System.out.println("Message: " + ((TextMessage) message).getText());
        else break;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally {
      if (connection != null)
      {
        try
        {
          connection.close();
        }
        catch (JMSException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args)
  {
    new Subscriber().receive();
  }
}