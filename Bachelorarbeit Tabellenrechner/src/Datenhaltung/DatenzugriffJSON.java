package Datenhaltung;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import Fachkonzept.Team;

/**
 * Die Klasse DatenzugriffJSON verarbeitet die Daten einer HTML seite mithilfe
 * der javax.json-1.0.4.jar
 * 
 * @author T.Benning
 *
 */
public class DatenzugriffJSON {

	private String url;

	/**
	 * 
	 * @param url
	 *            Die Url der Internetseite oder Lokal von wo die Daten
	 *            abgerufen werden können
	 */
	public DatenzugriffJSON(String url) {
		this.url = url;
	}

	/**
	 * ermittelt die Teams die in einer Saison spielen
	 * 
	 * @param saison
	 *            die Saison aus der die Teams ermittelt werden sollen
	 * @return Die Teamnamen in einer Liste
	 * @throws IOException
	 */
	public final LinkedList<String> ermittelTeams(int saison) throws IOException {
		return ermittelTeams(saison, null);
	}

	public final LinkedList<String> ermittelTeams(int saison, String gruppe) throws IOException {
		LinkedList<String> teamnamen = new LinkedList<String>();
		URL url = null;
		InputStream is = null;
		String tmp;
		// if (gruppe == null) {
		// tmp = this.lokalURL + "" + saison + "/" + 1;
		// } else {
		// gruppe.toUpperCase();
		// tmp = this.lokalURL + "" + saison + "/" + gruppe + 1;
		// }

		try {
			String tmpURL = this.url + "" + saison + "/" + 1;
			System.out.println(tmpURL);
			url = new URL(tmpURL);
			is = url.openStream();
		} catch (IOException e1) {
			throw e1;
		}

		try {
			JsonParser parser = Json.createParser(is);
			System.out.println("Daten erfolgreich heruntergeladen!");
			while (parser.hasNext()) {
				Event e = parser.next();
				if (e == Event.KEY_NAME) {
					if (parser.getString().equals("TeamName")) {
						parser.next();
						teamnamen.add(parser.getString());
					}
				}
			}
			return teamnamen;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param saison
	 *            die Saison die ausgewählt wurde
	 * @param bisSpiel
	 *            die Tabelle bis zu diesem Spieltag berechnen
	 * @param team
	 *            die ermittelten Teams um die ergebnisse zu setzten
	 * @return die ausgetragenen Spiele am Spieltag
	 */
	public final Team[][] berechneTabelle(int saison, int bisSpiel, Team[] team, String gruppe) {
		// String pfad = lokalURL + "" + saison;
		Team[][] alleSpiele = new Team[bisSpiel][team.length];
		System.out.println("bisSpiel: " + bisSpiel + "   team.lenght: " + team.length);
		for (int spieltag = 1; spieltag <= bisSpiel; spieltag++) {
			// String tmp;
			// if (gruppe == null) {
			// tmp = pfad + "/" + spieltag;
			// } else {
			// gruppe.toUpperCase();
			// tmp = pfad + "/" + gruppe + spieltag;
			// }
			URL url = null;
			InputStream is = null;
			Team t1 = null;
			Team t2 = null;

			try {
				String tmpURL = this.url + "" + saison + "/" + spieltag;
				System.out.println("Berechne Spieltag: " + tmpURL);
				url = new URL(tmpURL);
				is = url.openStream();
				System.out.println("Daten erfolgreich heruntergeladen!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			JsonReader rdr = Json.createReader(is);
			JsonArray spiele = rdr.readArray();
			int x = 0;
			for (JsonObject spiel : spiele.getValuesAs(JsonObject.class)) {
				for (int i = 0; i < team.length; i++) {
					if (team[i].getName().equals(spiel.getJsonObject("Team1").getString("TeamName"))) {
						t1 = team[i];

					}
					if (team[i].getName().equals(spiel.getJsonObject("Team2").getString("TeamName"))) {
						t2 = team[i];
					}
				}

				JsonArray tore = spiel.getJsonArray("MatchResults");

				for (JsonObject a : tore.getValuesAs(JsonObject.class)) {

					if (a.getString("ResultName").equals("Endergebnis")) {
						int torTeam1 = a.getInt("PointsTeam1");
						int torTeam2 = a.getInt("PointsTeam2");
						t1.setTore(torTeam1);
						t1.setGgtore(torTeam2);
						t2.setTore(torTeam2);
						t2.setGgtore(torTeam1);
						if (torTeam1 == torTeam2) {
							t1.unentschieden();
							t2.unentschieden();
						}

						if (torTeam1 < torTeam2) {
							t1.niederlage();
							t2.sieg();
						}

						if (torTeam1 > torTeam2) {
							t1.sieg();
							t2.niederlage();
						}

						if (x < team.length) {
							alleSpiele[spieltag - 1][x] = new Team(t1);
							alleSpiele[spieltag - 1][x + 1] = new Team(t2);
						}
						x += 2;

					}
				}
			}

		} // ende For schleife
		return alleSpiele;
	}// Ende Methode berechneTabelle

	public final Team[][] berechneTabelle(int saison, int bisSpiel, Team[] team) {
		return berechneTabelle(saison, bisSpiel, team, null);
	}

	/**
	 * 
	 * @param saison
	 *            Die Saison in der gespielt wird
	 * @param von
	 *            ab welchen Tag die ausstehenden spiele ermittelt werden sollen
	 * @return die Namen der Teams in der Reihenfolge wie diese noch ausstehend
	 *         sind
	 */
	public final LinkedList<String> ermittelAusstehendeSpiele(int saison, int von, int bis, String gruppe) {
//		String pfad = lokalURL + "" + saison;
		LinkedList<String> ausstehend = new LinkedList<String>();
		for (int spieltag = von; spieltag <= bis; spieltag++) {
			String tmp;
//			if (gruppe == null) {
//				tmp = pfad + "/" + spieltag;
//			} else {
//				gruppe.toUpperCase();
//				tmp = pfad + "/" + gruppe + spieltag;
//			}
			URL url = null;
			InputStream is = null;
			
				try {
					String tmpURL = this.url + "" + saison + "/" + spieltag;
					System.out.println("Ermittel noch zu spielen: " + tmpURL);
					url = new URL(tmpURL);
					is = url.openStream();
					System.out.println("Daten erfolgreich heruntergeladen!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			
			JsonReader rdr = Json.createReader(is);
			JsonArray spiele = rdr.readArray();
			for (JsonObject spiel : spiele.getValuesAs(JsonObject.class)) {
				ausstehend.add(spiel.getJsonObject("Team1").getString("TeamName"));
				ausstehend.add(spiel.getJsonObject("Team2").getString("TeamName"));
			}
		}
		return ausstehend;
	}

	public final LinkedList<String> ermittelAusstehendeSpiele(int saison, int von, int bis) {
		return ermittelAusstehendeSpiele(saison, von, bis, null);
	}

	/**
	 * liefert den aktuellen Spieltag der aktuellen Saison zurück zurzeit nur
	 * von Fußball
	 * 
	 * @return der aktuelle Spieltag der aktuellen Saison (nur Fußball)
	 */
	public static final int aktuellerSpieltag(String shortcut) {
		int aktuellerSpieltag = 0;
		URL url = null;
		InputStream is = null;
		String tmp = "https://www.openligadb.de/api/getcurrentgroup/" + shortcut;
		try {
			url = new URL(tmp);
			is = url.openStream();
		} catch (IOException e) {
			return -1;
		}
		JsonParser parser = Json.createParser(is);
		while (parser.hasNext()) {
			Event e = parser.next();
			if (e == Event.KEY_NAME) {
				if (parser.getString().equals("GroupOrderID")) {
					parser.next();
					aktuellerSpieltag = (parser.getInt());
				}
			}
		}
		return aktuellerSpieltag;
	}
}
