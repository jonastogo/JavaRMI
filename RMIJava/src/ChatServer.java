import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

	public ChatSession subscribeUser(String nickname, ClientHandle handle) throws RemoteException;

	public boolean unsubscribeUser(ClientHandle handle) throws RemoteException;

}
