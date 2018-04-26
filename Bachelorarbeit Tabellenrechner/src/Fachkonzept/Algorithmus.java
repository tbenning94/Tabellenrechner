package Fachkonzept;

import java.util.ArrayList;

public abstract class Algorithmus {
	public static int errorCounter=0;
	public static int SIEG=1;
	public static int UNENTSCHIEDEN=2;
	public static int NIEDERLAGE=3;
	public static int NICHT_GESETZT=-1;
	
	protected Liga liga;
	protected Team team;
	protected Koordinator k;
	protected int anzahlTeams;
	protected int maxPZ, minPZ;
	protected int maxTP, minTP;

	protected int anzahlUebrigerSpieltage;
	protected int oberGrenze; // die Grenze nach oben, die angibt welche Teams
							// nicht weiter betrachtet werden müssen, z.B da
							// diese zu viele Punkte haben
	protected int unterGrenze; // die Grenze nach unten, die angibt welche Teams
							// nicht weiter betrachtet werden müssen
	protected InitialeLoesung init;
	protected ArrayList<Team> O;
	protected ArrayList<Team> M;
	protected ArrayList<Team> U;
	protected static ArrayList<Integer> tipps = new ArrayList<Integer>();// Wenn spiele getippt wurden, werden diese Tipps hier gespeichert.
																		// (ein wert von -1 bedeutet nicht getippt)
	protected ArrayList<Team> merke; //eine aneinanderreihung der mannschaften die noch gegeneinander spielen müssen
	protected ArrayList<Integer> merkeSpielausgang;
	protected ArrayList<Team> merkeGegner;
	
	protected ArrayList<Team> ausstehendeNamenHeim;
	protected ArrayList<Team> ausstehendeNamenAusw;
	protected ArrayList<Integer> moeglichkeiten; // Hier werden die möglichkeiten
												// in einer ArrayList
												// hinereinander gemerkt um sie
												// so einfach ab zu arbeiten.
	protected Team[][] ausstehendCpy; // Eine Kopie der noch auszustehenden
									// Spiele. Es wird eine Kopie benötigt, da
									// wir das Original noch unverändert
									// benötigen wenn mehrere MAX_MIN
									// berechnungen durchgeführt werden. So kann
									// einfach die Kopie für die berechnungen
									// verwendet werden

	
	//TODO RENAME!
	protected int abc = 0;
	protected int abcdef = 0;
	protected int abcdefghi = 0;
	protected int rek = 0;
	protected int nMin = 0;
	protected int abc2 = 0;
	protected int abcdef2 = 0;
	protected int rek2 = 0;
	
	//Für das BnB-Verfahren
	protected boolean kompletterAbbruch;
	protected int S;
	
	
	abstract void erzeugeInitialeMengen(Team[] tmpTeam);
	abstract void erstelleInitialeLoesungAlle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2);
	abstract boolean moeglichkeitGefunden(int offenerSpieltagIndex, int teamIndex); 
	abstract void pruefeTippsAufAktivesTeam(int durchlauf);
	abstract void setzeMinMaxTP(Team[] tmpTeam);
	abstract void erzeugeMengenTabelle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2);
	abstract void erzeugeMengen(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2);
	
	
	//Für das BnB-Verfahren
	abstract void setzeNeueMengenForBnB(Team[] tmpVorherigeTabelle,ArrayList<Team> tmpAusstehendeSpieleHeim, ArrayList<Team> tmpAusstehendeSpieleAusw);
	abstract boolean pruefeAbbruchbedingung1();
	abstract boolean pruefeAbbruchbedingung2();
	
	
	public Algorithmus(Team team){
		initialisiereAlgorithmus(team);
		init=new InitialeLoesung(liga, this.team);
	}

	protected Team[] erstelleInitialeTabelle() {
		Team[] tmpTeam = null;
		int index2 = 0;

		tmpTeam = new Team[this.liga.getTeams().length];
		
		for (int i = 0; i < this.liga.getTeams().length; i++) {
			tmpTeam[i] = new Team(this.liga.getTeams()[i]);
		}
		
		//Die Möglichkeiten werden gesetzt
		for (int j = 0; j < this.moeglichkeiten.size(); j++) {
			int index = 0;
			if ((j % this.liga.getTeams().length == 0) && (j != 0)) {
				index2++;
			}
			for (int k = 0; k < tmpTeam.length; k++) {
				if (this.ausstehendCpy[index2][(j % this.liga.getTeams().length)].getName()
						.equals(tmpTeam[k].getName())) {
					index = k;
				}
			}
			if (((Integer) this.moeglichkeiten.get(j)).intValue() == SIEG) {
				tmpTeam[index].sieg();
			}
			if (((Integer) this.moeglichkeiten.get(j)).intValue() == UNENTSCHIEDEN) {
				tmpTeam[index].unentschieden();
			}
			if (((Integer) this.moeglichkeiten.get(j)).intValue() == NIEDERLAGE) {
				tmpTeam[index].niederlage();
			}
		}
		//tmpTeam sollte nach der rückgabe sortiert werden
		return tmpTeam;
	}
	
	protected void pruefeSchranke(int durchlauf) {
		this.unterGrenze = this.minPZ;
		this.oberGrenze = this.maxPZ;

		this.moeglichkeiten = new ArrayList<Integer>();
		this.ausstehendeNamenHeim = new ArrayList<Team>();
		this.ausstehendeNamenAusw = new ArrayList<Team>();
		
		if(!tipps.isEmpty()){
			pruefeTippsAufAktivesTeam(durchlauf);
			// die grenzen ändern sich sobald sich die punktzahl der aktiven
			// Mannschaft verändert
			this.unterGrenze = this.minPZ;
			this.oberGrenze = this.maxPZ;
		}
		
		//Gehe alle offenen Spieltage durch
		for (int x = 0; x < this.anzahlUebrigerSpieltage - durchlauf; x++) {
			//für jede Mannschaft (überspringe jede zweite da zwei immer gegeneinander spielen)
			for (int i = 0; i < this.ausstehendCpy[x].length; i += 2) {
				//wenn ein spiel getippt wurde
				if ((!tipps.isEmpty()) && (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() != -1)) {
					// Das Ergebnis wurde getippt und daraus geholt und dieser wert gesetzt
					int ergebnisHeim = (Integer) tipps.get(i + x * this.anzahlTeams);
					int ergebnisAusw = (Integer) tipps.get(i + 1 + x * this.anzahlTeams);
					setzeMoeglichkeiten(ergebnisHeim, ergebnisAusw);
				} else if (!moeglichkeitGefunden(x,i)){
					
					//Das spiel kann noch nicht gesetzt werden
					this.ausstehendeNamenHeim.add(this.ausstehendCpy[x][i]);
					this.ausstehendeNamenAusw.add(this.ausstehendCpy[x][(i + 1)]);
					setzeMoeglichkeiten(NICHT_GESETZT, NICHT_GESETZT);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param ergebnisHeim -  das Ergebnis der Heimmannschaft
	 * @param ergebnisAusw - das Ergebnis der Auswärtsmannschaft
	 */
	protected void setzeMoeglichkeiten(int ergebnisHeim,int ergebnisAusw){
		this.moeglichkeiten.add(ergebnisHeim);
		this.moeglichkeiten.add(ergebnisAusw);
	}
	
	  protected int ausstehendeAnzahlVonSpielen(Team t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int anzahl = 0;
	    for (int i = 0; i < l1.size(); i++) {
	      if (((Team)l1.get(i)).getName().equals(t.getName())) {
	        anzahl++;
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      if (((Team)l2.get(i)).getName().equals(t.getName())) {
	        anzahl++;
	      }
	    }
	    return anzahl;
	  }
	  
	  /**
		 * Initialisiert den Algorithmus
		 * 
		 * @param t
		 *            das Team welches der Maximum und Minimum berechnet werden soll
		 */
		private void initialisiereAlgorithmus(Team t) {
			k = Koordinator.getKoordinator();
			this.team = t;
			this.liga = k.getAktiveLiga();
			this.anzahlTeams = liga.getTeams().length;
			this.anzahlUebrigerSpieltage = liga.getAnzahlAusstehend() / liga.getTeams().length;
			this.ausstehendCpy = new Team[anzahlUebrigerSpieltage][anzahlTeams];

			// An dieser Stelle werden die Ausstehenden Spiele neu erstellt um so
			// das original nicht zu verändern.
			// j ist der ausstehende Spieltag und i das Team an diesem Tag
			int j = 0;
			for (int i = 0; i < liga.getAnzahlAusstehend(); i++) {
				// Der Spieltag j wird hochgesetzt sobald i einmal alle Teams
				// durchlaufen hat
				if (i % anzahlTeams == 0 && i != 0) {
					j++;
				}
				tipps.add(-1);
				ausstehendCpy[j][i % anzahlTeams] = new Team(
						liga.getAlleSpielPaarungen().get(i + liga.getAnzahlGespielt()));
			}
		}
		
		/**
		 * Die Tipps werden gesetzt
		 * 
		 * @param t
		 *            die Tipps in Form von zahlen. (-1=nicht getippt, 1=Sieg,
		 *            2=Unentschieden, 3=Niederlage (Aus sicht der Heimmanschaft))
		 */
		public static void setTipps(int[] t) {
			tipps = new ArrayList<Integer>();
			for (int i = 0; i < t.length; i = i + 2) {
				if (t[i] == -1) {
					tipps.add(NICHT_GESETZT);
					tipps.add(NICHT_GESETZT);
				} else {
					if (t[i] == t[i + 1]) {
						tipps.add(UNENTSCHIEDEN);
						tipps.add(UNENTSCHIEDEN);
					}
				}
				if (t[i] > t[i + 1]) {
					tipps.add(SIEG);
					tipps.add(NIEDERLAGE);
				}
				if (t[i] < t[i + 1]) {
					tipps.add(NIEDERLAGE);
					tipps.add(SIEG);
				}

			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		 * hier werden alle Möglichkeiten generiert und in eine Liste gesteckt
		 * 
		 * @param aktuelleMoeglichkeit
		 *            die Möglichkeit die als nächstes hinzugefügt werden soll
		 * @param tiefe
		 *            die Tiefe an der man sich grade befindet
		 * @param eineMoeglichkeit
		 *            die Liste einer Möglichkeit
		 */
		protected void backtracking(int aktuelleMoeglichkeit, int tiefe, ArrayList<Team> ausstehendeSpieleHeim,
				ArrayList<Team> ausstehendeSpieleAusw, Team[] vorherigeTabelle) {
			
			Team[] tmpVorherigeTabelle=copyVorherigeTabelle(vorherigeTabelle);
			ArrayList<Team> tmpAusstehendeSpieleHeim=copyAusstehendeSpieleHeim(ausstehendeSpieleHeim, tmpVorherigeTabelle);
			ArrayList<Team> tmpAusstehendeSpieleAusw=copyAusstehendeSpieleAusw(ausstehendeSpieleAusw, tmpVorherigeTabelle);
			setMoeglichkeit(aktuelleMoeglichkeit, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
			setzeNeueMengenForBnB(tmpVorherigeTabelle,tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw);
			
			int merkeMaxTP = this.maxTP;
			int merkeMinTP = this.minTP;

			boolean abbruch1=pruefeAbbruchbedingung1();
			boolean abbruch2=pruefeAbbruchbedingung2();
			
			//nur wenn kein abbruch berechnet wurde, wird die rekursion gestartet
			if(abbruch1==false&&abbruch2==false){
				rekursionStarten(merkeMaxTP,merkeMinTP,tiefe,tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw,vorherigeTabelle);	
			}

		}
		protected void aktuallisiereTeamsInMengen(Team[] t) {
			for (int i = this.O.size() - 1; i >= 0; i--) {
				for (int j = 0; j < t.length; j++) {
					if (((Team) this.O.get(i)).getName().equals(t[j].getName())) {
						this.O.set(i, t[j]);
					}
				}
			}
			for (int i = this.U.size() - 1; i >= 0; i--) {
				for (int j = 0; j < t.length; j++) {
					if (((Team) this.U.get(i)).getName().equals(t[j].getName())) {
						this.U.set(i, t[j]);
					}
				}
			}
			for (int i = this.M.size() - 1; i >= 0; i--) {
				for (int j = 0; j < t.length; j++) {
					if (((Team) this.M.get(i)).getName().equals(t[j].getName())) {
						this.M.set(i, t[j]);
					}
				}
			}
		}
		
		private Team[] copyVorherigeTabelle(Team[] vorherigeTabelle){
			Team[] tmpVorherigeTabelle = new Team[vorherigeTabelle.length];
			for (int i = 0; i < vorherigeTabelle.length; i++) {
				tmpVorherigeTabelle[i] = new Team(vorherigeTabelle[i]);
			}
			return tmpVorherigeTabelle;
		}
		
		private ArrayList<Team> copyAusstehendeSpieleHeim(ArrayList<Team> ausstehendeSpieleHeim,Team[] tmpVorherigeTabelle){
			ArrayList<Team> tmpAusstehendeSpieleHeim = new ArrayList<Team>();
			for (int i = 0; i < ausstehendeSpieleHeim.size(); i++) {
				for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
					if (((Team) ausstehendeSpieleHeim.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
						tmpAusstehendeSpieleHeim.add(tmpVorherigeTabelle[j]);
					}
				}
			}
			return tmpAusstehendeSpieleHeim;
		}
		
		private ArrayList<Team> copyAusstehendeSpieleAusw(ArrayList<Team> ausstehendeSpieleAusw,Team[] tmpVorherigeTabelle){
			ArrayList<Team> tmpAusstehendeSpieleAusw = new ArrayList<Team>();
			for (int i = 0; i < ausstehendeSpieleAusw.size(); i++) {
				for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
					if (((Team) ausstehendeSpieleAusw.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
						tmpAusstehendeSpieleAusw.add(tmpVorherigeTabelle[j]);
					}
				}
			}
			return tmpAusstehendeSpieleAusw;
		}
		
		private void setMoeglichkeit(int aktuelleMoeglichkeit,ArrayList<Team> tmpAusstehendeSpieleHeim,ArrayList<Team> tmpAusstehendeSpieleAusw){
			if (aktuelleMoeglichkeit == SIEG) {
				((Team) tmpAusstehendeSpieleHeim.get(0)).sieg();
				((Team) tmpAusstehendeSpieleAusw.get(0)).niederlage();
			}
			if (aktuelleMoeglichkeit == UNENTSCHIEDEN) {
				((Team) tmpAusstehendeSpieleHeim.get(0)).unentschieden();
				((Team) tmpAusstehendeSpieleAusw.get(0)).unentschieden();
			}
			if (aktuelleMoeglichkeit == NIEDERLAGE) {
				((Team) tmpAusstehendeSpieleHeim.get(0)).niederlage();
				((Team) tmpAusstehendeSpieleAusw.get(0)).sieg();
			}
			tmpAusstehendeSpieleHeim.remove(0);
			tmpAusstehendeSpieleAusw.remove(0);
		}
		
		private void rekursionStarten(int merkeMaxTP, int merkeMinTP,int tiefe,ArrayList<Team> tmpAusstehendeSpieleHeim,ArrayList<Team> tmpAusstehendeSpieleAusw,Team[] tmpVorherigeTabelle){
			this.S = 0;
			for (int i = 1; i <= 3; i++) {
				if (tmpAusstehendeSpieleHeim.size() > 0) {
					if (this.kompletterAbbruch) {
						return;
					}
					backtracking(i, tiefe + 1, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw, tmpVorherigeTabelle);
					this.maxTP = merkeMaxTP;
					this.minTP = merkeMinTP;
					
					erzeugeMengen(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);

				} else {
					System.out.println("ERROR ERROR ERROR ERROR ERROR");
					errorCounter++;
				}
			}
		}
}
