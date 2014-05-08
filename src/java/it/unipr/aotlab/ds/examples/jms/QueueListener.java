package it.unipr.aotlab.ds.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 
 * Class providing two implementation of a listener based receiver.
 * 
 * @author Agostino Poggi - AOT Lab - DII - University of Parma
 * 
**/

public class QueueListener implements MessageListener
{
  private final static String BROKER_URL   = "tcp://localhost:61616";
  private final static String BROKER_PROPS = "persistent=false&useJmx=false";
  private final static String QUEUE_NAME   = "queue";

  private ActiveMQConnection connection = null;

  public QueueListener()
  {
    try
    {
      BrokerService broker = BrokerFactory.createBroker(
          "broker:(" + BROKER_URL + ")?" + BROKER_PROPS);

      broker.start();

      ActiveMQConnectionFactory cf =
        new ActiveMQConnectionFactory(QueueListener.BROKER_URL);
      connection = (ActiveMQConnection) cf.createConnection();
      
      connection.start();
      
      QueueSession session =
        connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue queue         = session.createQueue(QueueListener.QUEUE_NAME);

      MessageConsumer consumer = session.createConsumer(queue);

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
    new QueueListener();
  }
}