package it.unipr.aotlab.ds.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 
 * Class providing two implementation of a listener based subscriber.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
**/

public class TopicListener implements MessageListener
{
  private final static String BROKER_URL   = "tcp://localhost:61616";
  private final static String BROKER_PROPS = "persistent=false&useJmx=false";
  private final static String TOPIC_NAME   = "queue";

  private ActiveMQConnection connection = null;

  public TopicListener()
  {
    try
    {
      BrokerService broker = BrokerFactory.createBroker(
          "broker:(" + BROKER_URL + ")?" + BROKER_PROPS);

      broker.start();

      ActiveMQConnectionFactory cf =
        new ActiveMQConnectionFactory(TopicListener.BROKER_URL);
      connection = (ActiveMQConnection) cf.createConnection();
      
      connection.start();
      
      TopicSession session =
        connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      Topic topic = session.createTopic(TOPIC_NAME);

      MessageConsumer consumer = session.createConsumer(topic);

      consumer.setMessageListener(this);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void onMessage(Message m)
  {
    if (m instanceof TextMessage)
    {
      try
      {
        System.out.println("Message: " + ((TextMessage) m).getText());
      }
      catch (JMSException e)
      {
        e.printStackTrace();
      }
    }
    else if (connection != null)
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

  public static void main(String[] args)
  {
    new TopicListener();
  }
}