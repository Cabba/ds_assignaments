package it.unipr.aotlab.ds.chat.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.unipr.aotlab.ds.chat.client.ChatClient;
import it.unipr.aotlab.ds.chat.command.Command;
import it.unipr.aotlab.ds.chat.command.Join;
import it.unipr.aotlab.ds.chat.command.Leave;
import it.unipr.aotlab.ds.chat.command.Send;

public class ClientRMIImpl implements ChatClient {

	CallbackClient m_clientRemote;
	IClientSubscriber m_subscriber;
	ICallbackServer m_server;

	@Override
	public Command receive() {
		Command msg = null;
		try {
			// wait until there are messages - blocking call
			msg = m_clientRemote.popMessage();
			System.out.println("[CLIENT] Message received.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public boolean join(String n) {
		try {
			Registry reg = LocateRegistry.getRegistry();
			m_clientRemote = new CallbackClient();
			// Register the client class into the client subscriber
			m_subscriber = (IClientSubscriber) reg.lookup("client_subscriber");
			System.out
					.println("[CLIENT] Registering the client in the client subscriber.");
			boolean success = m_subscriber.subscribe(n,
					(ICallbackClient) m_clientRemote);

			if (success == false){
				System.out.println("[CLIENT] A problem is occurred, sorry.");
				return false;
			}

			System.out
					.print("[CLIENT] Sending a join request to the server ...");
			m_server = (ICallbackServer) reg.lookup("server");
			m_server.pushMessage(new Join(n));
			System.out.print("ok.\n");

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void send(String n, String m) {
		try {
			m_server.pushMessage(new Send(n, m));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	@Override
	public void leave(String n) {
		try {
			m_server.pushMessage(new Leave(n));
		} catch (RemoteException e ) {
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}
