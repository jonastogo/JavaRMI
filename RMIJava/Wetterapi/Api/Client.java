package Api;

import java.io.IOException;
import java.util.ArrayList;

public class Client {

	private ArrayList<List>	list	= new ArrayList<List>();
	private String			id		= "";
	private api				read;
	private String			source;
	private String			sourceLines[];

	public static void main(String[] args) {
		Client client = new Client();
		client.read = new api();
		client.loadList();
		client.id = client.getIdFromCity("Herford");
		try {
			client.source = client.read.getUrlSource("http://api.openweathermap.org/data/2.5/forecast/city?id=" + client.id + "&APPID=722920868a0a0266c859a174da690bc1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String formatedSource = client.source.replaceAll("list\\\":\\[", "list\":\\[\n").replaceAll("00\\\"\\},", "00\"}\n");
		client.sourceLines = formatedSource.split("\\n");
		for (int i = 1; i < 9; i++) {
			String time = client.sourceLines[i].substring((client.sourceLines[i].length() - 10), (client.sourceLines[i].length() - 5));
			System.out.print("\nTime: " + time);
			int a = client.sourceLines[i].indexOf("temp_min");
			try {
				double temp_min = Double.parseDouble(client.sourceLines[i].substring((a + 10), (a + 16)));
				temp_min -= 273.15;
				System.out.printf(" -> Min Temp: %.2f°C", temp_min);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int b = client.sourceLines[i].indexOf("temp_max");
			try {
				double temp_max = Double.parseDouble(client.sourceLines[i].substring((b + 10), (b + 16)));
				temp_max -= 273.15;
				System.out.printf(" -> Max Temp: %.2f°C", temp_max);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean loadList() {
		read.readCSV(list);
		// for (List l : list) {
		// System.out.println("Stadt: " + l.getName() + " -> ID: " + l.getId());
		// }
		return false;

	}

	public String getIdFromCity(String city) {
		for (List l : list) {
			if (l.getName().equalsIgnoreCase(city)) {
				System.out.println("Found: " + l.getId());
				return l.getId();
			}
		}
		return null;
	}
}
