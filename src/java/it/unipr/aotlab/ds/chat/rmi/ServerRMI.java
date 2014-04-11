package it.unipr.aotlab.ds.chat.rmi;

import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Leave;
import it.unipr.aotlab.ds.chat.command.Send;
import it.unipr.aotlab.ds.chat.socket.ServerSock;

import java.util.ArrayList;
import java.util.List;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {

	private List<Command> m_history;

	private ClientSubscriber m_subscriber;
	private CallbackServer m_server;

	public void start() {
		try {
			System.out.println("[SERVER] Initialization ...");
			m_history = new ArrayList<Command>();
			m_subscriber = new ClientSubscriber();
			m_server = new CallbackServer();

			// Register the server and the subscriber
			System.out.println("[SERVER] Registering the server and the client subscriber.");
			Registry reg = LocateRegistry.createRegistry(1099);
			reg.bind("server", (ICallbackServer) m_server);
			reg.bind("client_subscriber", (IClientSubscriber) m_subscriber);

			// Server loop
			while (true) {
				System.out.println("[SERVER] Wait for some message...");
				// Wait until a message is pushed into the server callback class
				Command o = m_server.popMessage();
				System.out.println("[SERVER] Message received.");

				// Join message
				if (o instanceof Join) {
					Join msg = (Join) o;
					System.out.println("[SERVER] Received a join message: [" + msg.toString() + "].");
					// Push the history in the client list
					System.out.print("[SERVER] Sending history to the client ... ");
					ICallbackClient cli = m_subscriber.get(msg.getName());
					for (int i = 0; i < m_history.size(); ++i) {
						cli.pushMessage(m_history.get(i));
					}
					System.out.print("ok.\n");
					
					System.out.print("[SERVER] Broadcasting the new message ... ");
					broadcast(msg);
					System.out.print("ok.\n");
				}

				if (o instanceof Send) {
					Send msg = (Send) o;
					System.out.println("[SERVER] Received a send message: [" + msg.toString() + "].");
					System.out.print("[SERVER] Broadcasting message ... ");
					broadcast(msg);
					System.out.print("ok.\n");
				}
				
				if(o instanceof Leave){
					Leave msg = (Leave)o;
					System.out.println("[SERVER] Received a leave message: [" + msg.toString() +"].");
					System.out.print("[SERVER] Removing '" + msg.getName()+ "' from the subscribed clients ... ");
					m_subscriber.unsubscribe(msg.getName());
					System.out.print("ok.\n");
					broadcast(msg);
				}
				
				// Add the message in the history
				m_history.add(o);
			}

		} catch (RemoteException | InterruptedException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	private void broadcast(Command command) {
		List<ICallbackClient> m_clients = m_subscriber.getClients();
		for (int i = 0; i < m_clients.size(); ++i) {
			try {
				m_clients.get(i).pushMessage(command);
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ServerRMI server = new ServerRMI();
		server.start();
	}

}
