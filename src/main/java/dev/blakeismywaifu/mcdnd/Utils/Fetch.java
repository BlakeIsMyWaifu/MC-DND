package dev.blakeismywaifu.mcdnd.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Fetch {

	private final String uri;

	public Fetch(String uri) {
		this.uri = uri;
	}

	private String getRawJSON() {
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(this.uri);
			InputStreamReader streamReader = new InputStreamReader(url.openStream());
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line).append("\n");
			}
			bufferedReader.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
		return content.toString();
	}

	public JSONObject getData() {
		String rawJson = getRawJSON();

		JSONObject json = new JSONObject(rawJson);
		boolean status = json.getBoolean("success");

		if (status) {
			Console.success("API Success from url " + this.uri);
		} else {
			Console.error("API Failed from url " + this.uri);
		}

		return json.getJSONObject("data");
	}
}
