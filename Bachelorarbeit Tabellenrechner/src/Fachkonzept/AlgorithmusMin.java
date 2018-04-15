package Fachkonzept;

import java.util.ArrayList;
import java.util.Collections;

public class AlgorithmusMin extends Algorithmus {

	private int min;
	private int tmpMinTP;
	private int S = 0;
	private ArrayList<Team> nachbesserung;
	private ArrayList<Integer> xxx;
	private ArrayList<Team> test1;
	private ArrayList<Team> test2;

	public AlgorithmusMin(Team team) {
		super(team);
	}

	public void berechneMIN(int tag, boolean test, int durchlauf) {

		Liga.ermittelPlatzierungMaxOderMin(this.k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MIN, this.team);
		this.min = this.team.getPlatzierung();
		this.maxPZ = this.team.getPunkte();
		this.minPZ = (this.team.getPunkte() - Zaehlweise.PUNKTE_S * (tag + 1));

		pruefeSchranke(durchlauf);
		erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());

		Team[] tmpTabelle = erstelleInitialeTabelle();

		setzeMinMaxTP(this.k.getAktiveLiga().getTeams());

		ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList<Team>();
		for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
			tmpAusstehendeNamenHeim.add((Team) this.ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList<Team>();
		for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
			tmpAusstehendeNamenAusw.add((Team) this.ausstehendeNamenAusw.get(i));
		}

		erzeugeMengenTabelle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);

		test(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
		tmpAusstehendeNamenHeim = this.test1;
		tmpAusstehendeNamenAusw = this.test2;

		if (!test) {
			erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		}
		// Wenn eine Initiallösung den besten TP gefunden hat, kann abgebrochen
		// werden
		if (this.min == this.minTP) {
			this.team.setMinPlatzSpieltag(this.min);
			return;
		}

		// überprüfen ob minTP überhaupt erreicht werden kann
		this.tmpMinTP = ermittelVerbessertenMinTP(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
		if (this.tmpMinTP == this.min) {
			this.team.setMinPlatzSpieltag(this.min);
			return;
		}

		if (!test) {
			nachbesserung(tag, durchlauf);
		}

		if (this.tmpMinTP == this.min) {
			this.team.setMinPlatzSpieltag(this.min);
			return;
		}
		this.S = (this.minTP - this.tmpMinTP);

		erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);

		int altMinalt = this.min;
		if (this.maxTP == this.minTP) {
			if (this.maxTP > this.min) {
				this.min = this.maxTP;
			}
		} else if (this.minTP > this.min) {
			System.out.println("Starte mit der Rekursion in MIN");
			erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
			int merkeMaxTP = this.maxTP;
			int merkeMinTP = this.minTP;
			backtracking(1, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
			this.maxTP = merkeMaxTP;
			this.minTP = merkeMinTP;
			erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
			backtracking(2, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
			this.maxTP = merkeMaxTP;
			this.minTP = merkeMinTP;
			erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
			backtracking(3, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
		}
		System.out.println("altes Min: " + altMinalt + "  neues Min: " + this.min);
		this.rek += 1;

		this.team.setMinPlatzSpieltag(this.min);
	}

	private void nachbesserung(int tag, int durchlauf) {
		int altMin = this.min;
		int altMinTP = this.minTP;
		int altMaxTP = this.maxTP;
		int altMinPZ = this.minPZ;
		erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(this.k.getAktiveLiga().getTeams(), tag, durchlauf);
		if (this.nachbesserung != null) {
			this.nachbesserung.clear();
		}
		this.minTP = altMinTP;
		this.maxTP = altMaxTP;
		this.minPZ = altMinPZ;

		this.min = Math.max(altMin, this.min);
	}

	@Override
	protected void erzeugeInitialeMengen(Team[] tmpTeam) {
		this.O = new ArrayList<Team>();
		this.M = new ArrayList<Team>();
		this.U = new ArrayList<Team>();
		for (int i = 0; i < tmpTeam.length; i++) {
			if (tmpTeam[i].getPunkte() >= this.maxPZ) {
				this.O.add(tmpTeam[i]);
			} else if (!tmpTeam[i].getName().equals(this.team.getName())) {
				if (tmpTeam[i].getPunkte() < this.minPZ) {
					this.U.add(tmpTeam[i]);
				} else {
					this.M.add(tmpTeam[i]);
				}
			}
		}
	}

	@Override
	protected boolean moeglichkeitGefunden(int x, int i) {
		boolean moeglichkeitGefunden = true;
		if (this.ausstehendCpy[x][i].getName().equals(this.team.getName())) {
			setzeMoeglichkeiten(NIEDERLAGE, SIEG);
		} else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName())) {
			setzeMoeglichkeiten(SIEG, NIEDERLAGE);
		} else if ((this.ausstehendCpy[x][i].getPunkte() >= this.oberGrenze)
				|| (this.ausstehendCpy[x][i].getPunkte() < this.unterGrenze)) {
			setzeMoeglichkeiten(NIEDERLAGE, SIEG);
		} else if ((this.ausstehendCpy[x][(i + 1)].getPunkte() >= this.oberGrenze)
				|| (this.ausstehendCpy[x][(i + 1)].getPunkte() < this.unterGrenze)) {
			setzeMoeglichkeiten(SIEG, NIEDERLAGE);
		} else {
			moeglichkeitGefunden = false;
		}
		return moeglichkeitGefunden;
	}

	@Override
	protected void pruefeSchrankeWennGetippt(int x, int i) {
		if (this.ausstehendCpy[x][i].getName().equals(this.team.getName())) {
			if (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() == SIEG) {
				this.minPZ += Zaehlweise.PUNKTE_S;
				this.maxPZ += Zaehlweise.PUNKTE_S;
			}
			if (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() == UNENTSCHIEDEN) {
				this.minPZ += Zaehlweise.PUNKTE_U;
				this.maxPZ += Zaehlweise.PUNKTE_U;
			}
		} else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName())) {
			if (((Integer) tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == SIEG) {
				this.minPZ += Zaehlweise.PUNKTE_S;
				this.maxPZ += Zaehlweise.PUNKTE_S;
			}
			if (((Integer) tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == UNENTSCHIEDEN) {
				this.minPZ += Zaehlweise.PUNKTE_U;
				this.maxPZ += Zaehlweise.PUNKTE_U;
			}
		}
		// Das Ergebnis wurde getippt und daraus geholt und dieser wert gesetzt
		int ergebnisHeim = (Integer) tipps.get(i + x * this.anzahlTeams);
		int ergebnisAusw = (Integer) tipps.get(i + 1 + x * this.anzahlTeams);
		setzeMoeglichkeiten(ergebnisHeim, ergebnisAusw);

		// die grenzen ändern sich sobald sich die punktzahl der aktiven
		// Mannschaft verändert
		this.unterGrenze = this.minPZ;
		this.oberGrenze = this.maxPZ;
	}

	private void erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(Team[] t, int tag, int durchlauf) {
		int durchgang = 0;
		int tmpMin = 0;
		if (this.nachbesserung == null) {
			return;
		}
		for (int b = 0; b < this.nachbesserung.size(); b++) {
			this.xxx = new ArrayList<Integer>();
			for (int a = t.length - 1; a >= 0; a--) {
				for (int c = 0; c <= durchgang; c++) {
					if (t[a].getName().equals(((Team) this.nachbesserung.get(c)).getName())) {
						for (int x = 0; x < this.anzahlUebrigerSpieltage - durchlauf; x++) {
							for (int i = 0; i < this.ausstehendCpy[x].length; i += 2) {
								if ((this.ausstehendCpy[x][i].getName().equals(t[a].getName()))
										&& (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() == -1)) {
									if (!this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName())) {
										tipps.set(i + x * this.anzahlTeams, Integer.valueOf(3));
										tipps.set(i + 1 + x * this.anzahlTeams, Integer.valueOf(1));
										this.xxx.add(Integer.valueOf(i + x * this.anzahlTeams));
										this.xxx.add(Integer.valueOf(i + 1 + x * this.anzahlTeams));
									}
								} else if ((this.ausstehendCpy[x][(i + 1)].getName().equals(t[a].getName()))
										&& (((Integer) tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == -1)) {
									if (!this.ausstehendCpy[x][i].getName().equals(this.team.getName())) {
										tipps.set(i + x * this.anzahlTeams, Integer.valueOf(1));
										tipps.set(i + 1 + x * this.anzahlTeams, Integer.valueOf(3));
										this.xxx.add(Integer.valueOf(i + x * this.anzahlTeams));
										this.xxx.add(Integer.valueOf(i + 1 + x * this.anzahlTeams));
									}
								}
							}
						}
					}
				}
			}
			pruefeSchranke(durchlauf);

			erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());

			Team[] tmpTabelle = erstelleInitialeTabelle();

			setzeMinMaxTP(this.k.getAktiveLiga().getTeams());

			ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList<Team>();
			for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
				tmpAusstehendeNamenHeim.add((Team) this.ausstehendeNamenHeim.get(i));
			}
			ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList<Team>();
			for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
				tmpAusstehendeNamenAusw.add((Team) this.ausstehendeNamenAusw.get(i));
			}
			erzeugeMengenTabelle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);

			this.min = init.erstelleInitialeLoesungVarianteMIN_G_Komplex(tmpTabelle, tmpAusstehendeNamenHeim,
					tmpAusstehendeNamenAusw, true, nachbesserung);
			for (int i = 0; i < this.xxx.size(); i++) {
				tipps.set(((Integer) this.xxx.get(i)).intValue(), Integer.valueOf(-1));
			}
			tmpMin = Math.max(tmpMin, this.min);
			durchgang++;
		}
		this.min = tmpMin;
	}

	@Override
	protected void erstelleInitialeLoesungAlle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2) {

		int tmpMinB = init.erstelleInitialeLoesungVarianteB(t, l1, l2, false);

		int tmpMinC = init.erstelleInitialeLoesungVarianteC(t, l1, l2, false);

		int tmpMinD = init.erstelleInitialeLoesungVarianteD(t, l1, l2, false);

		int tmpMinE = init.erstelleInitialeLoesungVarianteE(t, l1, l2, false);

		int tmpMinF = init.erstelleInitialeLoesungVarianteMIN_F(t, l1, l2, this.maxPZ);

		int tmpMinG = init.erstelleInitialeLoesungVarianteMIN_G_Komplex(t, l1, l2, false, nachbesserung);

		int tmpMin = Math.max(tmpMinB, tmpMinC);
		tmpMin = Math.max(tmpMin, tmpMinD);
		tmpMin = Math.max(tmpMin, tmpMinE);
		tmpMin = Math.max(tmpMin, tmpMinF);
		tmpMin = Math.max(tmpMin, tmpMinG);

		this.min = tmpMin;
	}

	@Override
	protected void setzeMinMaxTP(Team[] tmpTeam) {

		Liga.ermittelPlatzierungMaxOderMin(tmpTeam, Liga.SORTIERUNG_MIN, this.team);

		this.maxTP = this.team.getPlatzierung();
		this.minTP = tmpTeam.length;
		for (int i = tmpTeam.length - 1; i >= 0; i--) {
			if (tmpTeam[i].getPunkte() >= this.minPZ) {
				break;
			}
			this.minTP -= 1;
		}
	}

	@Override
	protected void erzeugeMengenTabelle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2) {
		int anzahlSpiele = 0;
		aktuallisiereTeamsInMengen(t);
		for (int i = this.M.size() - 1; i >= 0; i--) {
			anzahlSpiele = ausstehendeAnzahlVonSpielen((Team) this.M.get(i), l1, l2);
			if (((Team) this.M.get(i)).getPunkte() >= this.maxPZ) {
				if (anzahlSpiele > 0) {
					for (int j = l1.size() - 1; j >= 0; j--) {
						if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).niederlage();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							l1.remove(j);
							l2.remove(j);
						} else if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							((Team) this.M.get(i)).niederlage();
							l1.remove(j);
							l2.remove(j);
						}
					}
					this.O.add((Team) this.M.remove(i));
					this.maxTP += 1;
					erzeugeMengenTabelle(t, l1, l2);
					break;
				}
				this.O.add((Team) this.M.remove(i));
				this.maxTP += 1;
			} else if (((Team) this.M.get(i)).getPunkte() < this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele) {
				if (anzahlSpiele > 0) {
					for (int j = l1.size() - 1; j >= 0; j--) {
						if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							((Team) this.M.get(i)).niederlage();
							l1.remove(j);
							l2.remove(j);
						} else if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).niederlage();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							l1.remove(j);
							l2.remove(j);
						}
					}
					this.U.add((Team) this.M.remove(i));
					this.minTP -= 1;
					erzeugeMengenTabelle(t, l1, l2);
					break;
				}
				this.U.add((Team) this.M.remove(i));
				this.minTP -= 1;
			}
		}
	}

	@Override
	protected void erzeugeMengen(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2) {
		this.O = new ArrayList<Team>();
		this.M = new ArrayList<Team>();
		this.U = new ArrayList<Team>();
		int anzahlSpiele = 0;
		for (int i = 0; i < tmpTeam.length; i++) {
			anzahlSpiele = ausstehendeAnzahlVonSpielen(tmpTeam[i], l1, l2);
			if (tmpTeam[i].getPunkte() >= this.maxPZ) {
				this.O.add(tmpTeam[i]);
			} else if (tmpTeam[i].getPunkte() < this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele) {
				this.U.add(tmpTeam[i]);
			} else {
				this.M.add(tmpTeam[i]);
			}
		}
	}

	/**
	 * Bei der Variante A zur Verbesserung der Schranken geht es darum, eine
	 * Spielpaarung zu finden, welche garantiert, dass mindestens eine
	 * Mannschaft mehr (bei maxTP) oder weniger (bei minTP) Punkte bekommt, als
	 * die aktuelle Mannschaft. Zuerst werden dafür temporäre Schranken tmpMaxTP
	 * und tmpMinTP erstellt, welche den Wert von maxTP bzw. minTP bekommen.
	 * Sobald dann eine der Bedingungen aus Tabelle 3 zutrifft, wird tmpMaxTP
	 * inkrementiert oder tmpMinTP dekrementiert. Dabei ist zu beachten, dass
	 * wenn einmal eine Bedingung für eine Mannschaft zutraf, diese Mannschaft
	 * danach nicht weiter betrachtet wird.
	 * 
	 * Tabellenrechner zur Vorhersage von Tabellenplätzen im Sport, S.15
	 */
	protected int ermittelVerbessertenMinTP_VarianteA(ArrayList<Team> l1, ArrayList<Team> l2) {
		int tmpMinTP = this.minTP;
		ArrayList<String> raus = new ArrayList<String>();
		for (int i = 0; i < l1.size(); i++) {
			if ((!raus.contains(((Team) l1.get(i)).getName())) && (!raus.contains(((Team) l2.get(i)).getName()))) {
				if (((((Team) l1.get(i)).getPunkte() == this.minPZ) && (((Team) l2.get(i)).getPunkte() == this.minPZ))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 1)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 1))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 1))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 1)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 2)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 2))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 3)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 3))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 3)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 2))
						|| ((((Team) l1.get(i)).getPunkte() == this.minPZ + 2)
								&& (((Team) l2.get(i)).getPunkte() == this.minPZ + 3))) {
					tmpMinTP--;
					raus.add(((Team) l1.get(i)).getName());
					raus.add(((Team) l2.get(i)).getName());
				}
			}
		}
		return tmpMinTP;
	}

	/**
	 * Hier geht es darum, zu berechnen, wie viele Siege und unentschieden
	 * mindestens benötigt werden, damit alle Mannschaften aus der Menge M die
	 * aktuelle Mannschaft überholen. Dafür wird die Differenz (diff) zwischen
	 * der aktuellen Punktzahl der aktuellen Mannschaft P(x) und der Punkte der
	 * Mannschaft P(m) gebildet. 𝑑𝑖𝑓𝑓=𝑃(𝑥)−𝑃(𝑚) Aus dieser Differenz
	 * wird geprüft, wie viele Siege und unentschieden die Mannschaft m
	 * benötigen würde, um mindestens gleich viele Punkte wie P(x) zu bekommen.
	 * 
	 * Beispiel: Wird angenommen, dass noch 64 Spiele offen sind, dann könnte
	 * die Summe aller gezählten Siege und Unentschieden bei 74 Siegen und 3
	 * Unentschieden liegen. Weiter gilt, dass zwei Unentschieden wie ein Sieg
	 * gewertet werden. Somit gilt, dass ein Unentschieden einen Wert von 0,5
	 * hat. Summiert man dann die Siege und Unentschieden auf, so kommt man auf
	 * einen Wert von 75,5. Dieser Wert wird als Spielsumme (S) bezeichnet.
	 * 𝑆=|𝑆𝑖𝑒𝑔𝑒|+|𝑈𝑛𝑒𝑛𝑡𝑠𝑐ℎ𝑖𝑒𝑑𝑒𝑛|2 Das heißt, dass man
	 * mindestens 75,5 Spiele benötigt, selbst wenn die Spielpaarungen ideal
	 * ausgerichtet wären. Da es aber nur 64 offene Spiele gibt, kann minTP gar
	 * nicht erreicht werden.
	 * 
	 * Tabellenrechner zur Vorhersage von Tabellenplätzen im Sport, S.16f.
	 */
	private int ermittelVerbessertenMinTP_VarianteB(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t) {
		int px = this.team.getPunkte();
		int siege = 0;
		int unentschieden = 0;
		for (int i = 0; i < this.M.size(); i++) {
			int diff = px - ((Team) this.M.get(i)).getPunkte();
			while (diff >= 0) {
				if ((diff - 3 >= 0) || (diff - 2 >= 0)) {
					siege++;
					diff -= 3;
				} else {
					if (diff - 1 < 0) {
						break;
					}
					unentschieden++;
					diff--;
				}
			}
		}
		ArrayList<Integer> offeneSiegeProMannschaft = new ArrayList<Integer>();
		double wert = siege + unentschieden / 2.0D;
		for (int i = 0; i < t.length; i++) {
			int test = px - t[i].getPunkte();
			test = Math.round(test / 3.0F);

			offeneSiegeProMannschaft.add(Integer.valueOf(test));
		}
		Collections.sort(offeneSiegeProMannschaft);
		if (l1.size() < wert) {
			int abzug = 1;
			for (int i = offeneSiegeProMannschaft.size() - 1; i >= 0; i--) {
				if (l1.size() < wert - ((Integer) offeneSiegeProMannschaft.get(i)).intValue()) {
					abzug++;
					wert -= ((Integer) offeneSiegeProMannschaft.get(i)).intValue();
				} else {
					return this.minTP - abzug;
				}
			}
			return this.minTP - 1;
		}
		return this.minTP;
	}

	private int ermittelVerbessertenMinTP_VarianteC(ArrayList<Team> l1, ArrayList<Team> l2) {
		int px = this.team.getPunkte();
		for (int i = 0; i < l1.size(); i++) {
			int anzahlSpieltageY = ausstehendeAnzahlVonSpielen((Team) l1.get(i), l1, l2);
			int anzahlSpieltageZ = ausstehendeAnzahlVonSpielen((Team) l2.get(i), l1, l2);

			int maxPY = ((Team) l1.get(i)).getPunkte() + 3 * anzahlSpieltageY;
			int maxPZ = ((Team) l2.get(i)).getPunkte() + 3 * anzahlSpieltageZ;
			if ((maxPY <= px + 2) && (maxPZ <= px + 2)) {
				if ((maxPY == maxPZ) && (maxPY != px + 2)) {
					return this.minTP - 1;
				}
			}
		}
		return this.minTP;
	}

	private int ermittelVerbessertenMinTP(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t) {
		int a = ermittelVerbessertenMinTP_VarianteA(l1, l2);
		int b = ermittelVerbessertenMinTP_VarianteB(l1, l2, t);
		int c = ermittelVerbessertenMinTP_VarianteC(l1, l2);

		int tmpMin = Math.min(this.minTP, a);
		tmpMin = Math.min(tmpMin, b);
		tmpMin = Math.min(tmpMin, c);

		return tmpMin;
	}

	// TODO RECHECK AND RENAME
	private void test(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t1) {
		ArrayList<Team> heim = new ArrayList();
		ArrayList<Team> ausw = new ArrayList();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < t1.length; j++) {
				if (((Team) l1.get(i)).getName().equals(t1[j].getName())) {
					heim.add(t1[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < t1.length; j++) {
				if (((Team) l2.get(i)).getName().equals(t1[j].getName())) {
					ausw.add(t1[j]);
				}
			}
		}
		int offeneSpiele = l1.size();
		int px = this.team.getPunkte();
		int siege = 0;
		int unentschieden = 0;
		for (int i = 0; i < this.M.size(); i++) {
			int diff = px - ((Team) this.M.get(i)).getPunkte();
			while (diff >= 0) {
				if ((diff - 3 >= 0) || (diff - 2 >= 0)) {
					siege++;
					diff -= 3;
				} else {
					if (diff - 1 < 0) {
						break;
					}
					unentschieden++;
					diff--;
				}
			}
		}
		double wert = siege + unentschieden / 2.0D;
		while (wert > offeneSpiele) {
			Liga.ermittelPlatzierungMaxOderMin(t1, Liga.SORTIERUNG_MIN, this.team);
			erzeugeMengen(t1, heim, ausw);
			Team m = (Team) this.M.get(this.M.size() - 1);
			for (int j = heim.size() - 1; j >= 0; j--) {
				if (((Team) heim.get(j)).getName().equals(m.getName())) {
					((Team) heim.get(j)).niederlage();
					((Team) ausw.get(j)).sieg();
					heim.remove(j);
					ausw.remove(j);
				} else if (((Team) ausw.get(j)).getName().equals(m.getName())) {
					((Team) heim.get(j)).sieg();
					((Team) ausw.get(j)).niederlage();
					heim.remove(j);
					ausw.remove(j);
				}
			}
			this.U.add((Team) this.M.remove(this.M.size() - 1));
			this.minTP -= 1;

			testR(t1, heim, ausw);
			offeneSpiele = heim.size();
			siege = 0;
			unentschieden = 0;
			for (int i = 0; i < this.M.size(); i++) {
				int diff = px - ((Team) this.M.get(i)).getPunkte();
				while (diff >= 0) {
					if ((diff - 3 >= 0) || (diff - 2 >= 0)) {
						siege++;
						diff -= 3;
					} else {
						if (diff - 1 < 0) {
							break;
						}
						unentschieden++;
						diff--;
					}
				}
			}
			wert = siege + unentschieden / 2.0D;
		}
		Liga.ermittelPlatzierungMaxOderMin(t1, Liga.SORTIERUNG_MIN, this.team);

		this.test1 = heim;
		this.test2 = ausw;
	}

	private void testR(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2) {
		int anzahlSpiele = 0;
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
		for (int i = this.M.size() - 1; i >= 0; i--) {
			anzahlSpiele = ausstehendeAnzahlVonSpielen((Team) this.M.get(i), l1, l2);
			if (((Team) this.M.get(i)).getPunkte() >= this.maxPZ) {
				if (anzahlSpiele > 0) {
					for (int j = l1.size() - 1; j >= 0; j--) {
						if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).niederlage();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							l1.remove(j);
							l2.remove(j);
						} else if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).sieg();
								}
							}
							((Team) this.M.get(i)).niederlage();
							l1.remove(j);
							l2.remove(j);
						}
					}
					this.O.add((Team) this.M.remove(i));
					this.maxTP += 1;
					erzeugeMengenTabelle(t, l1, l2);
					break;
				}
				this.O.add((Team) this.M.remove(i));
				this.maxTP += 1;
			}
		}
	}

	@Override
	void setzeNeueMengenForBnB(Team[] tmpVorherigeTabelle, ArrayList<Team> tmpAusstehendeSpieleHeim,
			ArrayList<Team> tmpAusstehendeSpieleAusw) {
		int anzahlU = this.U.size();
		erzeugeMengenTabelle(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
		if (anzahlU < this.U.size()) {
			int NtmpMinTP = ermittelVerbessertenMinTP(tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw,
					tmpVorherigeTabelle);
			this.S = (this.minTP - NtmpMinTP);
		}
		erzeugeMengen(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	}

	@Override
	boolean pruefeAbbruchbedingung1() {
		if (this.maxTP == this.minTP) {
			if (this.maxTP > this.min) {
				this.min = this.maxTP;
			}
			if (minTP == min) {
				// abbruch=true;
				// TODO ?
				// System.out.println("kompletter abbruch welcher schon längst
				// überfällig war");
				// return;
			}
			if (this.tmpMinTP == this.min) {
				this.kompletterAbbruch = true;
				System.out.println("kompletter abbruch..tmpMinTP==min");
			}
			return true;
		}
		return false;
	}

	@Override
	boolean pruefeAbbruchbedingung2() {
		if (this.minTP - this.S <= this.min) {
			return true;
		}
		if (this.minTP <= this.min) {
			System.out.println("abbruch 1: " + this.min);

			return true;
		}
		return false;
	}
}
