package dev.blakeismywaifu.mcdnd.Stats;

public class Stat {

	public enum StatName {
		STRENGTH("STR"),
		DEXTERITY("DEX"),
		CONSITUTION("CON"),
		INTELLIGENCE("INT"),
		WISDOM("WIS"),
		CHARISMA("CHA");

		public final String shortHand;

		StatName(String shortHand) {
			this.shortHand = shortHand;
		}
	}
}
