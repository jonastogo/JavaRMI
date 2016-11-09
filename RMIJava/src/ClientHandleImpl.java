import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandleImpl extends UnicastRemoteObject implements ClientHandle {

	private static final long	serialVersionUID	= 1L;

	ChatClient					client;

	public ClientHandleImpl(ChatClient client) throws RemoteException {
		this.client = client;
	}

	public ClientHandleImpl() throws RemoteException {}

	public void receiveMessage(String nickname, String message) {
		client.receiveMessage(nickname, message);
	}

	public ChatClient getChatClient() {
		return client;
	}

}
