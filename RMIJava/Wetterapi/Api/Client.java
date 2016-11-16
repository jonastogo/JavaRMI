package Api;

import java.util.ArrayList;

public class Client {

	public static void main(String[] args) {
		ArrayList<List> list = new ArrayList<List>();
		api read = new api();
		read.readCSV(list);
		for (List l : list) {
			System.out.println("Stadt: " + l.getName() + " -> ID: " + l.getId());
		}
	}
}
