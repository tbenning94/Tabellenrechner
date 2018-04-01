package Fachkonzept;

import java.util.ArrayList;

public class AlgorithmusBranchAndBound {
	private Team[] tmpVorherigeTabelle;
	private ArrayList<Team> tmpAusstehendeSpieleAusw;
	private ArrayList<Team> tmpAusstehendeSpieleHeim;

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
	private void backtracking(int aktuelleMoeglichkeit, int tiefe, ArrayList<Team> ausstehendeSpieleHeim,
			ArrayList<Team> ausstehendeSpieleAusw, Team[] vorherigeTabelle) {
		initBnB(aktuelleMoeglichkeit, ausstehendeSpieleHeim, ausstehendeSpieleAusw, vorherigeTabelle);
		initBnB2();
		int merkeMaxTP = this.maxTP;
		int merkeMinTP = this.minTP;
		boolean abbruch1=pruefeAbbruchbedingung1();
		boolean abbruch2=pruefeAbbruchbedingung2();
		
		if(abbruch1==true||abbruch2==true){
			//TODO
		}
		rekursionStarten();

	}

	public void initBnB(int aktuelleMoeglichkeit, ArrayList<Team> ausstehendeSpieleHeim,
			ArrayList<Team> ausstehendeSpieleAusw, Team[] vorherigeTabelle) {
		tmpVorherigeTabelle = new Team[vorherigeTabelle.length];
		for (int i = 0; i < vorherigeTabelle.length; i++) {
			tmpVorherigeTabelle[i] = new Team(vorherigeTabelle[i]);
		}
		tmpAusstehendeSpieleHeim = new ArrayList<Team>();
		for (int i = 0; i < ausstehendeSpieleHeim.size(); i++) {
			for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
				if (((Team) ausstehendeSpieleHeim.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
					tmpAusstehendeSpieleHeim.add(tmpVorherigeTabelle[j]);
				}
			}
		}
		tmpAusstehendeSpieleAusw = new ArrayList<Team>();
		for (int i = 0; i < ausstehendeSpieleAusw.size(); i++) {
			for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
				if (((Team) ausstehendeSpieleAusw.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
					tmpAusstehendeSpieleAusw.add(tmpVorherigeTabelle[j]);
				}
			}
		}
		if (aktuelleMoeglichkeit == 1) {
			((Team) tmpAusstehendeSpieleHeim.get(0)).sieg();
			((Team) tmpAusstehendeSpieleAusw.get(0)).niederlage();
		}
		if (aktuelleMoeglichkeit == 2) {
			((Team) tmpAusstehendeSpieleHeim.get(0)).unentschieden();
			((Team) tmpAusstehendeSpieleAusw.get(0)).unentschieden();
		}
		if (aktuelleMoeglichkeit == 3) {
			((Team) tmpAusstehendeSpieleHeim.get(0)).niederlage();
			((Team) tmpAusstehendeSpieleAusw.get(0)).sieg();
		}
		tmpAusstehendeSpieleHeim.remove(0);
		tmpAusstehendeSpieleAusw.remove(0);
	}

	public void initBnB2() {
		if (this.MAX) {
			erzeugeMengenTabelleMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);

			erzeugeMengenMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
		} else {
			int anzahlU = this.U.size();
			erzeugeMengenTabelleMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
			if (anzahlU < this.U.size()) {
				int NtmpMinTP = verbessereSchrankeMIN(tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw,
						tmpVorherigeTabelle);
				this.S = (this.minTP - NtmpMinTP);
			}
			erzeugeMengenMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
		}
	}
	
	public void pruefeAbbruchbedingung1(){
		if (this.maxTP == this.minTP) {
			if (this.MAX) {
				if (this.maxTP < this.max) {
					this.max = this.maxTP;
				}
			} else {
				if (this.maxTP > this.min) {
					this.min = this.maxTP;
				}
				if(minTP==min){
					//abbruch=true;
					//TODO ?
					//System.out.println("kompletter abbruch welcher schon längst überfällig war");
					//return;
				}
				if (this.tmpMinTP == this.min) {
					this.abbruch = true;
					System.out.println("kompletter abbruch..tmpMinTP==min");
					return;
				}
			}
			return;
		}
	}
	
	public void pruefeAbbruchbedingung2(){
		if (this.MAX) {
			if(maxTP>=max)
			{
				return;
			}
		} else {
			if (this.minTP - this.S <= this.min) {
				return;
			}
			if (this.minTP <= this.min) {
				System.out.println("abbruch 1: " + this.min);

				return;
			}
		}
	}
	
	public void rekursionStarten(){
		this.S = 0;
		for (int i = 1; i <= 3; i++) {
			if (tmpAusstehendeSpieleHeim.size() > 0) {
				if (this.abbruch) {
					return;
				}
				backtracking(i, tiefe + 1, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw, tmpVorherigeTabelle);
				this.maxTP = merkeMaxTP;
				this.minTP = merkeMinTP;
				
				erzeugeMengen(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);

			} else {
				System.out.println("ERROR ERROR ERROR ERROR ERROR");
			}
		}
	}
}
