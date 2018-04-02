package Fachkonzept;

import java.util.ArrayList;

public class AlgorithmusMax extends Algorithmus {

	private int max;

	public AlgorithmusMax(Team team) {
		super(team);
	}

	public void berechneMAX(int tag, boolean test,int durchlauf) {
		this.liga.ermittelPlatzierung(this.k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MAX, this.team);
		this.max = this.team.getPlatzierung();
		this.maxPZ = (this.team.getPunkte() + Zaehlweise.PUNKTE_S * (tag + 1));
		this.minPZ = this.team.getPunkte();

		pruefeSchranke(durchlauf);
		erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());

		Team[] tmpTabelle = erstelleInitialeTabelle();

		//TODO überprüfen welche tabelle hier rein kann
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
		
		if (!test) {
			erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		}
		//Wenn eine Initiallösung den besten TP gefunden hat, kann abgebrochen werden
		if (this.max == this.maxTP) {
			this.team.setMaxPlatzSpieltag(this.max);
			return;
		}

		//überprüfen ob maxTP überhaupt erreicht werden kann
		int tmpMaxTP = ermittelVerbessertenMaxTP(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if (tmpMaxTP == this.max) {
			this.team.setMaxPlatzSpieltag(this.max);
			return;
		}

		//Wenn es keine weiteren Spiele mehr gibt, wurde eine Lösung gefunden
		if (this.maxTP == this.minTP) {
			if (this.maxTP < this.max) {
				this.max = this.maxTP;
			}
		} else if (this.maxTP < this.max) {
			System.out.println("Starten der Rekursion MAX");
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
		this.team.setMaxPlatzSpieltag(this.max);
	}

	@Override
	protected void erzeugeInitialeMengen(Team[] tmpTeam) {
		this.O = new ArrayList<Team>();
		this.M = new ArrayList<Team>();
		this.U = new ArrayList<Team>();
		for (int i = 0; i < tmpTeam.length; i++) {
			if (tmpTeam[i].getPunkte() > this.maxPZ) {
				this.O.add(tmpTeam[i]);
			} else if (!tmpTeam[i].getName().equals(this.team.getName())) {
				if (tmpTeam[i].getPunkte() <= this.minPZ) {
					this.U.add(tmpTeam[i]);
				} else {
					this.M.add(tmpTeam[i]);
				}
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
			if (tmpTeam[i].getPunkte() > this.maxPZ) {
				this.O.add(tmpTeam[i]);
			} else if (tmpTeam[i].getPunkte() <= this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele) {
				this.U.add(tmpTeam[i]);
			} else {
				this.M.add(tmpTeam[i]);
			}
		}
	}

	@Override
	protected void erstelleInitialeLoesungAlle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2) {
		int tmpMaxB = init.erstelleInitialeLoesungVarianteB(t, l1, l2, true);

		int tmpMaxC = init.erstelleInitialeLoesungVarianteC(t, l1, l2, true);

		int tmpMaxD = init.erstelleInitialeLoesungVarianteD(t, l1, l2, true);

		int tmpMaxE = init.erstelleInitialeLoesungVarianteE(t, l1, l2, true);

		int tmpMaxF = init.erstelleInitialeLoesungVarianteMAX_F(t, l1, l2, this.maxPZ);

		int tmpMaxG = init.erstelleInitialeLoesungVarianteMAX_G_Komplex(t, l1, l2, this.maxPZ);

		int tmpMax = Math.min(tmpMaxB, tmpMaxC);
		tmpMax = Math.min(tmpMax, tmpMaxD);
		tmpMax = Math.min(tmpMax, tmpMaxE);
		tmpMax = Math.min(tmpMax, tmpMaxF);
		tmpMax = Math.min(tmpMax, tmpMaxG);
		this.max = tmpMax;
	}

	@Override
	protected boolean moeglichkeitGefunden(int x, int i){
		boolean moeglichkeitGefunden=true;
		if (this.ausstehendCpy[x][i].getName().equals(this.team.getName())) {
			setzeMoeglichkeiten(SIEG, NIEDERLAGE);
		} else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName())) {
			setzeMoeglichkeiten(NIEDERLAGE, SIEG);
		} else if ((this.ausstehendCpy[x][i].getPunkte() > this.oberGrenze)
				|| (this.ausstehendCpy[x][i].getPunkte() <= this.unterGrenze)) {
			setzeMoeglichkeiten(SIEG, NIEDERLAGE);
		} else if ((this.ausstehendCpy[x][(i + 1)].getPunkte() > this.oberGrenze)
				|| (this.ausstehendCpy[x][(i + 1)].getPunkte() <= this.unterGrenze)) {
			setzeMoeglichkeiten(NIEDERLAGE, SIEG);
		}else{
			moeglichkeitGefunden=false;
		}
		return moeglichkeitGefunden;
	}
	
	@Override
	protected void pruefeSchrankeWennGetippt(int x, int i) {
		//Wenn die aktive Mannschaft die Heimmanschaft ist
		if (this.ausstehendCpy[x][i].getName().equals(this.team.getName())) {
			if (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() == UNENTSCHIEDEN) {
				this.maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
				this.minPZ += Zaehlweise.PUNKTE_U;
			}
			if (((Integer) tipps.get(i + x * this.anzahlTeams)).intValue() == NIEDERLAGE) {
				this.maxPZ -= Zaehlweise.PUNKTE_S;
				this.minPZ -= Zaehlweise.PUNKTE_S;
			}
		} else 
			//Wenn die aktive Mannschaft die Auswärstmannschaft ist
			if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName())) {
			if (((Integer) tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == NIEDERLAGE) {
				this.maxPZ -= Zaehlweise.PUNKTE_S;
				this.minPZ -= Zaehlweise.PUNKTE_S;
			}
			if (((Integer) tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == UNENTSCHIEDEN) {
				this.maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
				this.minPZ += Zaehlweise.PUNKTE_U;
			}
		}
		//Das Ergebnis wurde getippt und daraus geholt und dieser wert gesetzt
		int ergebnisHeim=(Integer) tipps.get(i + x * this.anzahlTeams);
		int ergebnisAusw=(Integer) tipps.get(i + 1 + x * this.anzahlTeams);
		setzeMoeglichkeiten(ergebnisHeim, ergebnisAusw);
		
		//die grenzen ändern sich sobald sich die punktzahl der aktiven Mannschaft verändert
		this.unterGrenze = this.minPZ;
		this.oberGrenze = this.maxPZ;
	}
	


	@Override
	protected void setzeMinMaxTP(Team[] tmpTeam) {
		this.liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX, this.team);
		this.minTP = this.team.getPlatzierung();
		this.maxTP = 1;
		for (int i = 0; i < tmpTeam.length; i++) {
			if (tmpTeam[i].getPunkte() <= this.maxPZ) {
				break;
			}
			this.maxTP += 1;
		}
	}

	@Override
	protected void erzeugeMengenTabelle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2) {
		int anzahlSpiele = 0;
		aktuallisiereTeamsInMengen(t);
		for (int i = this.M.size() - 1; i >= 0; i--) {
			anzahlSpiele = ausstehendeAnzahlVonSpielen((Team) this.M.get(i), l1, l2);
			if (((Team) this.M.get(i)).getPunkte() > this.maxPZ) {
				if (anzahlSpiele > 0) {
					for (int j = l1.size() - 1; j >= 0; j--) {
						if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).sieg();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).niederlage();
								}
							}
							l1.remove(j);
							l2.remove(j);
						} else if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).niederlage();
								}
							}
							((Team) this.M.get(i)).sieg();
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
			} else if (((Team) this.M.get(i)).getPunkte() <= this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele) {
				if (anzahlSpiele > 0) {
					for (int j = l1.size() - 1; j >= 0; j--) {
						if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).sieg();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).niederlage();
								}
							}
							l1.remove(j);
							l2.remove(j);
						} else if (((Team) l2.get(j)).getName().equals(((Team) this.M.get(i)).getName())) {
							((Team) this.M.get(i)).sieg();
							for (int k = 0; k < this.M.size(); k++) {
								if (((Team) l1.get(j)).getName().equals(((Team) this.M.get(k)).getName())) {
									((Team) this.M.get(k)).niederlage();
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



/**
 * Bei der Variante A zur Verbesserung der Schranken geht es darum, eine Spielpaarung zu finden, welche garantiert, 
 * dass mindestens eine Mannschaft mehr (bei maxTP) oder weniger (bei minTP) Punkte bekommt, als die aktuelle Mannschaft. 
 * Zuerst werden dafür temporäre Schranken tmpMaxTP und tmpMinTP erstellt, welche den Wert von maxTP bzw. minTP bekommen. 
 * Sobald dann eine der Bedingungen aus Tabelle 3 zutrifft, wird tmpMaxTP inkrementiert oder tmpMinTP dekrementiert. Dabei ist zu 
 * beachten, dass wenn einmal eine Bedingung für eine Mannschaft zutraf, diese Mannschaft danach nicht weiter betrachtet wird.
 * 
 * Tabellenrechner zur Vorhersage von Tabellenplätzen im Sport, S.15
 */
	protected int ermittelVerbessertenMaxTP_VarianteA(ArrayList<Team> l1, ArrayList<Team> l2) {
		int tmpMaxTP = this.maxTP;
		ArrayList<String> raus = new ArrayList<String>();
		for (int i = 0; i < l1.size(); i++) {
			if ((!raus.contains(((Team) l1.get(i)).getName())) && (!raus.contains(((Team) l2.get(i)).getName()))) {
				if (((((Team) l1.get(i)).getPunkte() == this.maxPZ) && (((Team) l2.get(i)).getPunkte() == this.maxPZ))
						|| ((((Team) l1.get(i)).getPunkte() == this.maxPZ)
								&& (((Team) l2.get(i)).getPunkte() == this.maxPZ - 1))
						|| ((((Team) l1.get(i)).getPunkte() == this.maxPZ - 1)
								&& (((Team) l2.get(i)).getPunkte() == this.maxPZ))
						|| ((((Team) l1.get(i)).getPunkte() == this.maxPZ)
								&& (((Team) l2.get(i)).getPunkte() == this.maxPZ - 2))
						|| ((((Team) l1.get(i)).getPunkte() == this.maxPZ - 2)
								&& (((Team) l2.get(i)).getPunkte() == this.maxPZ))) {
					tmpMaxTP++;
					raus.add(((Team) l1.get(i)).getName());
					raus.add(((Team) l2.get(i)).getName());
				}
			}
		}
		return tmpMaxTP;
	}

	/**
	 * In der Variante B wird berechnet, wie viele Punkte eine Mannschaft aus M maximal noch holen dürfen, damit keiner die 
	 * ausgewählte Mannschaft überholt. Die Mannschaften aus M bekommen mindestens die 
	 * Anzahl der noch offenen Spiele * 2 (wegen Unentschieden) Punkte: 𝑚𝑖𝑛𝑃=|𝑜𝑓𝑓𝑒𝑛𝑒𝑆𝑝𝑖𝑒𝑙𝑒|∙2 
	 * Dann wird für jede Mannschaft m die Differenz seiner Punkte P(m) und der maximalen Punkte der 
	 * aktuellen Mannschaft maxPZ berechnet und aufsummiert. |M| ist dabei die Anzahl der Mannschaften 
	 * in der Menge M. 𝑆=Σ𝑚𝑎𝑥𝑃𝑍−𝑃(𝑚)     𝑚=1 
	 * Zum Schluss wird dann geprüft, ob diese Summe (S) kleiner ist als die Punktzahl (minP). 
	 * Wenn dies der Fall ist, dann muss mindestens eine Mannschaft aus M die aktuelle Mannschaft 
	 * übertreffen und die temporäre Schranke tmpMaxTP würde somit inkrementiert werden. 
	 * 𝑆<𝑚𝑖𝑛𝑃 ⟹𝑡𝑚𝑝𝑀𝑎𝑥𝑇𝑃++
	 * 
	 * Tabellenrechner zur Vorhersage von Tabellenplätzen im Sport, S.16
	 */
	private int ermittelVerbessertenMaxTP_VarianteB(ArrayList<Team> l1, ArrayList<Team> l2) {
		int punkte = l1.size() * 2;

		int summe = 0;
		for (int i = 0; i < this.M.size(); i++) {
			summe += this.maxPZ - ((Team) this.M.get(i)).getPunkte();
		}
		if (summe < punkte) {
			return this.maxTP + 1;
		}
		return this.maxTP;
	}

	private int ermittelVerbessertenMaxTP(ArrayList<Team> l1, ArrayList<Team> l2) {
		int a = ermittelVerbessertenMaxTP_VarianteA(l1, l2);
		int b = ermittelVerbessertenMaxTP_VarianteB(l1, l2);

		int tmp = Math.max(this.maxTP, a);
		tmp = Math.max(tmp, b);
		return tmp;
	}

	@Override
	void setzeNeueMengenForBnB(Team[] tmpVorherigeTabelle,ArrayList<Team> tmpAusstehendeSpieleHeim, ArrayList<Team> tmpAusstehendeSpieleAusw) {
		erzeugeMengenTabelle(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
		erzeugeMengen(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	}

	@Override
	boolean pruefeAbbruchbedingung1() {
		if (this.maxTP == this.minTP) {
			if (this.maxTP < this.max) {
				this.max = this.maxTP;
			}
			return true;
		}
		return false;
	}

	@Override
	boolean pruefeAbbruchbedingung2() {
		if (maxTP >= max) {
			return true;
		}
		return false;
	}
}
