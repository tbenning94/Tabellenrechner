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

		Team[] tmpTabelle = erstelleInitialeTabelle(true);

		erstelleWerte(this.k.getAktiveLiga().getTeams(), tag + 1);

		ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList<Team>();
		for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
			tmpAusstehendeNamenHeim.add((Team) this.ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList<Team>();
		for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
			tmpAusstehendeNamenAusw.add((Team) this.ausstehendeNamenAusw.get(i));
		}
		if (this.maxTP == this.minTP) {
			this.abc2 += 1;
			this.team.setMaxPlatzSpieltag(this.maxTP);
			return;
		}
		erzeugeMengenTabelle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if (!test) {
			erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		}
		if (this.max == this.maxTP) {
			this.abcdef2 += 1;
			this.team.setMaxPlatzSpieltag(this.max);
			return;
		}
		int tmpMaxTP = verbessereSchrankeMAX(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if (tmpMaxTP == this.max) {
			this.abcdef2 += 1;

			this.team.setMaxPlatzSpieltag(this.max);
			return;
		}
		erzeugeMengen(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if (tmpMaxTP == this.max) {
			this.abcdef2 += 1;

			this.team.setMaxPlatzSpieltag(this.max);
			return;
		}
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
		this.rek2 += 1;

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
	protected void erstelleWerte(Team[] tmpTeam, int anzahlUebrigerTage) {
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
	protected int verbessereNaiv(ArrayList<Team> l1, ArrayList<Team> l2) {
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

	private int verbessereSchrankeMAXVarianteB(ArrayList<Team> l1, ArrayList<Team> l2) {
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

	private int verbessereSchrankeMAX(ArrayList<Team> l1, ArrayList<Team> l2) {
		int a = verbessereNaiv(l1, l2);
		int b = verbessereSchrankeMAXVarianteB(l1, l2);

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
