package dev.blakeismywaifu.mcdnd.Data.Books;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Feats extends BookBase {

	public Feats(JSONObject json) {
		super("Feats & Traits", json);
	}

	@Override
	protected List<Category> generateContent() {
		List<Category> categories = new LinkedList<>(classFeaturesContents());
		categories.add(racialTraitsCategory());
		categories.add(featsCategory());
		return categories;
	}

	private List<Category> classFeaturesContents() {
		List<Category> out = new LinkedList<>();

		JSONArray classes = this.json.getJSONArray("classes");
		for (Object dndClassObject : classes) {
			JSONObject dndClass = (JSONObject) dndClassObject;

			String className = dndClass.getJSONObject("definition").getString("name");

			JSONArray classFeatures = dndClass.getJSONArray("classFeatures");
			List<String[]> contents = new LinkedList<>();
			classFeatures.forEach(feature -> contents.add(getContent(feature)));

			Category category = new Category(className + " Features", contents);
			out.add(category);
		}

		return out;
	}

	private Category racialTraitsCategory() {
		List<String[]> contents = new LinkedList<>();

		JSONObject race = this.json.getJSONObject("race");
		JSONArray racialTraits = race.getJSONArray("racialTraits");
		racialTraits.forEach(trait -> contents.add(getContent(trait)));

		return new Category("Racial Traits", contents);
	}

	private Category featsCategory() {
		List<String[]> contents = new LinkedList<>();

		JSONArray feats = this.json.getJSONArray("feats");
		feats.forEach(feat -> contents.add(getContent(feat)));

		return new Category("Feats", contents);
	}

	private String[] getContent(Object parentObject) {
		JSONObject parentJson = (JSONObject) parentObject;
		JSONObject definition = parentJson.getJSONObject("definition");
		if (!definition.isNull("hideInSheet")) {
			if (definition.getBoolean("hideInSheet")) return new String[]{};
		}
		String name = definition.getString("name");
		// TODO fallback to other descriptions
		// TODO parse weird inline calculations
		String description = definition.isNull("snippet") ? "" : definition.getString("snippet");
		return new String[]{name, description};
	}
}
