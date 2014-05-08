package it.unipr.aotlab.ds.examples.jms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * Class providing two implementation of a publisher.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
**/

public class Publisher
{
  private final static String BROKER_URL   = "tcp://localhost:61616";
  private final static String TOPIC_NAME   = "queue";

  public void publish(final int n)
  {
    ActiveMQConnection connection = null;

    try
    {
      ActiveMQConnectionFactory cf =
        new ActiveMQConnectionFactory(Publisher.BROKER_URL);
      connection = (ActiveMQConnection) cf.createConnection();
      
      connection.start();
      
      TopicSession session =
        connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      Topic topic = session.createTopic(TOPIC_NAME);
      TopicPublisher publisher = session.createPublisher(topic);
      TextMessage message = session.createTextMessage();

      for (int i = 0; i < n; i++) {
        message.setText("This is message " + (i + 1));
        publisher.publish(message);
      }

      publisher.publish(session.createMessage());
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
    new Publisher().publish(3);
  }
}