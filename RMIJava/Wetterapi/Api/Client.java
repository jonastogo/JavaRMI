package Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Client {

	private String	source;
	private String	sourceLines[];
	private String	city;
	private String	cityID;
	private int		cityZIP;
	private String	countryID;

	public static void main(String[] args) {
		Client client = new Client();
		System.out.println("Eingabe  z.B.: Herford     oder   32051,de           oder   Herford,de");
		System.out.println("Eingabeformat: Stadtname   oder   PLZ,Länderkürzel   oder   Stadtname,Länderkürzel");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("\nCity:  ");
			client.city = br.readLine();
			if (client.city.contains(",")) {
				try {
					client.cityZIP = Integer.parseInt(client.city.substring(0, client.city.indexOf(",")));
				} catch (NumberFormatException nfe) {
					client.cityID = client.city.substring(0, client.city.indexOf(","));
					client.cityZIP = 0;
				}
				client.countryID = client.city.substring(client.city.indexOf(",") + 1);
				System.out.println("ZIP: " + client.cityZIP + " Country: " + client.countryID);
			} else {
				client.countryID = "";
				client.cityZIP = 0;
				client.cityID = client.city;
			}
		} catch (IOException e) {
			System.out.println("Sie haben eventuell keine gültige Stadt eingegeben!");
			return;
		}

		try {
			if (client.cityZIP == 0 && client.countryID.equals("")) {
				if (client.cityID.contains(" ")) {
					client.cityID = client.cityID.replaceAll("\\s", "%20");
				}
				client.source = client.getUrlSource("http://api.openweathermap.org/data/2.5/forecast/city?q=" + client.cityID + "&APPID=722920868a0a0266c859a174da690bc1");
			} else {
				if (client.cityZIP == 0 && !client.countryID.equals("")) {
					if (client.cityID.contains(" ")) {
						client.cityID = client.cityID.replaceAll("\\s", "%20");
					}
					System.out.println("http://api.openweathermap.org/data/2.5/forecast/city?q=" + client.cityID + "," + client.countryID + "&APPID=722920868a0a0266c859a174da690bc1");
					client.source = client.getUrlSource("http://api.openweathermap.org/data/2.5/forecast/city?q=" + client.cityID + "," + client.countryID + "&APPID=722920868a0a0266c859a174da690bc1");
				} else {
					client.source = client.getUrlSource("http://api.openweathermap.org/data/2.5/forecast/city?zip=" + client.cityZIP + "," + client.countryID + "&APPID=722920868a0a0266c859a174da690bc1");
				}
			}
		} catch (IOException e) {
			System.out.println("Sie haben eine ungültige Eingabe gemacht!");
			return;
		}

		String formatedSource = client.source.replaceAll("list\\\":\\[", "list\":\\[\n").replaceAll("00\\\"\\},", "00\"}\n");
		client.sourceLines = formatedSource.split("\\n");
		int cityIndex = client.sourceLines[0].indexOf("\"name\":\"");
		String cityID = client.sourceLines[0].substring((cityIndex + 8), client.sourceLines[0].substring(cityIndex + 8).indexOf("\"") + cityIndex + 8);
		System.out.print(cityID);
		int country = client.sourceLines[0].indexOf("country");
		String countryID = client.sourceLines[0].substring((country + 10), client.sourceLines[0].substring(country + 10).indexOf("\"") + country + 10);
		System.out.println(" " + countryID);
		for (int i = 1; i < 9; i++) {
			String time = client.sourceLines[i].substring((client.sourceLines[i].length() - 10), (client.sourceLines[i].length() - 5));
			System.out.print("\nTime: " + time);
			int a = client.sourceLines[i].indexOf("temp_min");
			try {
				double temp_min = Double.parseDouble(client.sourceLines[i].substring((a + 10), (a + 15)));
				temp_min -= 273.15;
				System.out.printf(" -> Min Temp: %.1f°C", temp_min);
			} catch (NumberFormatException e) {
				System.out.println("Formatting failed!");
				return;
			}
			int b = client.sourceLines[i].indexOf("temp_max");
			try {
				double temp_max = Double.parseDouble(client.sourceLines[i].substring((b + 10), (b + 15)));
				temp_max -= 273.15;
				System.out.printf(" -> Max Temp: %.1f°C", temp_max);
			} catch (NumberFormatException e) {
				System.out.println("Formatting failed!");
				return;
			}
		}
	}

	public String getUrlSource(String url) throws IOException {
		URL link = new URL(url);
		URLConnection yc = link.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			a.append(inputLine);
		}
		in.close();

		return a.toString();
	}
}
