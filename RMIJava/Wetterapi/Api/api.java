package Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class api {
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

	public void readCSV(ArrayList<List> list) {
		try {
			java.io.BufferedReader FileReader = new java.io.BufferedReader(new java.io.FileReader(new java.io.File("WetterID.csv")));

			String zeile = "";

			while (null != (zeile = FileReader.readLine())) {
				String[] split = zeile.split(";");
				List temp = new List(split[0], split[1]);
				list.add(temp);
			}
			FileReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
