package dev.blakeismywaifu.mcdnd.Data.Books;

import org.javatuples.Pair;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Feats extends BookBase {

	public Feats(JSONObject json) {
		super("Feats & Traits", json);
	}

	@Override
	protected Map<String, List<@Nullable String>> generateContent() {
		Map<String, List<String>> books = new LinkedHashMap<>();
		classFeaturesContents().forEach(classFeatures -> {
			String title = classFeatures.getValue0() + " Features";
			books.put(title, classFeatures.getValue1());
		});
		books.put("Racial Traits", racialTraitsContents());
		books.put("Feats", featsContents());
		return books;
	}

	private List<Pair<String, List<@Nullable String>>> classFeaturesContents() {
		List<Pair<String, List<@Nullable String>>> out = new LinkedList<>();

		JSONArray classes = this.json.getJSONArray("classes");
		for (Object dndClassObject : classes) {
			JSONObject dndClass = (JSONObject) dndClassObject;

			String className = dndClass.getJSONObject("definition").getString("name");

			JSONArray classFeatures = dndClass.getJSONArray("classFeatures");
			List<@Nullable String> contents = new LinkedList<>();
			classFeatures.forEach(feature -> contents.add(getName(feature)));

			Pair<String, List<@Nullable String>> classData = new Pair<>(className, contents);
			out.add(classData);
		}

		return out;
	}

	private List<@Nullable String> racialTraitsContents() {
		List<@Nullable String> contents = new LinkedList<>();

		JSONObject race = this.json.getJSONObject("race");
		JSONArray racialTraits = race.getJSONArray("racialTraits");
		racialTraits.forEach(trait -> contents.add(getName(trait)));

		return contents;
	}

	private List<String> featsContents() {
		List<String> contents = new LinkedList<>();

		JSONArray feats = this.json.getJSONArray("feats");
		feats.forEach(feat -> contents.add(getName(feat)));

		return contents;
	}

	private @Nullable String getName(Object parentObject) {
		JSONObject parentJson = (JSONObject) parentObject;
		JSONObject definition = parentJson.getJSONObject("definition");
		if (!definition.isNull("hideInSheet")) {
			if (definition.getBoolean("hideInSheet")) return null;
		}
		return definition.getString("name");
	}
}
