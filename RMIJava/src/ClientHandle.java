import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientHandle extends Remote {

	public void receiveMessage(String nickname, String message) throws RemoteException;

	public ChatClient getChatClient() throws RemoteException;

}
