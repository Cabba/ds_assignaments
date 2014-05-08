package it.unipr.aotlab.ds.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 
 * Class providing two implementation of a receiver.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
**/

public class Receiver
{
  private final static String BROKER_URL    = "tcp://localhost:61616";
  private final static String BROKER_PROPS = "persistent=false&useJmx=false";
  private final static String QUEUE_NAME   = "queue";

  public void receive()
  {
    ActiveMQConnection connection = null;
    try
    {
      BrokerService broker = BrokerFactory.createBroker(
          "broker:(" + BROKER_URL + ")?" + BROKER_PROPS);

      broker.start();

      ActiveMQConnectionFactory cf =
        new ActiveMQConnectionFactory(Receiver.BROKER_URL);
      connection = (ActiveMQConnection) cf.createConnection();
      
      connection.start();
      
      QueueSession session =
        connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue queue         = session.createQueue(Receiver.QUEUE_NAME);
      QueueReceiver receiver  = session.createReceiver(queue);

      while (true)
      {
        Message message = receiver.receive();

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
    new Receiver().receive();
  }
}