import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

	private static final long	serialVersionUID	= 1L;

	ArrayList<ChatSession>		sessions			= new ArrayList<ChatSession>();

	public ChatServerImpl() throws RemoteException {}

	public ChatSession subscribeUser(String nickname, ClientHandle handle) throws RemoteException {
		for (int i = 0; i < sessions.size(); i++) {
			if (sessions.get(i).getClientHandle().getChatClient().getUsername().equals(nickname)) {
				return null;
			}
		}
		System.out.println("create session: " + nickname);
		ChatSession s = new ChatSessionImpl(this, nickname, handle);
		sessions.add(s);
		System.out.println("\n");
		return s;
	}

	public void postMessage(String message, ChatSessionImpl s) {
		ChatSessionImpl tmp;
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			System.out.println("send Message to: " + tmp.getNickname());
			try {
				tmp.getClientHandle().receiveMessage(s.getNickname(), message);
			} catch (RemoteException ex) {
				System.out.println("unabled to contact client " + s.getNickname());
				System.out.println("removing.");
				removeSession(tmp);
				i--;
			}
		}
		System.out.println("\n");
	}

	public boolean removeSession(ChatSession session) {
		return sessions.remove(session);
	}

	public boolean unsubscribeUser(ClientHandle handle) throws RemoteException {
		for (ChatSession cs : sessions) {
			try {
				if (cs.getClientHandle().getChatClient().getUsername().equals(handle.getChatClient().getUsername())) {
					System.out.println(handle.getChatClient().getUsername() + " was removed!\n");
					return sessions.remove(cs);
				}
			} catch (RemoteException | NullPointerException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public static void main(String args[]) throws RemoteException {
		java.rmi.registry.LocateRegistry.createRegistry(1099);
		try {
			Naming.rebind("chat-server", new ChatServerImpl());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
