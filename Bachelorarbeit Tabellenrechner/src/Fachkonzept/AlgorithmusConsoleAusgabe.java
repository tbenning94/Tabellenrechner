package Fachkonzept;

import java.util.ArrayList;

public class AlgorithmusConsoleAusgabe {


	private void ausgabeWerte(int max,int min) {
		System.out.println("Erstellte Werte: ");
		System.out.println("maxPZ: " + maxPZ + "   maxTP: " + maxTP);
		System.out.println("minPZ: " + minPZ + "   minTP: " + minTP);
		System.out.println("MAX: " + max + "    MIN: " + min);

		System.out.println();
	}

	private void ausgabeMengen() {
		System.out.println("Die Menge M");
		for (int i = 0; i < M.size(); i++) {
			System.out.println(M.get(i).getName());
		}

		System.out.println("O: " + O.size());
		for (int i = 0; i < O.size(); i++) {
			System.out.println(O.get(i).getName());
		}

		System.out.println("U: " + U.size());
		for (int i = 0; i < U.size(); i++) {
			System.out.println(U.get(i).getName());
		}
	}

	  private void ausgabeTabelle(Team[] t0,boolean isMaxCalculation)
	  {
	    ComparatorChain.aktuellesTeam = this.team;
	    if (isMaxCalculation) {
	      this.liga.ermittelPlatzierung(t0, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(t0, 3, this.team);
	    }
	    System.out.printf("%s ", new Object[] { "Platz" });
	    System.out.printf("%-20s", new Object[] { "     Team" });
	    System.out.printf("%s  ", new Object[] { "  Punkte" });
	    System.out.printf("%s%n", new Object[] { "  Gespielt" });
	    for (int i = 0; i < t0.length; i++)
	    {
	      System.out.printf("%4d  ", new Object[] { Integer.valueOf(t0[i].getPlatzierung()) });
	      System.out.printf("%-25s  ", new Object[] { t0[i].getName() });
	      System.out.printf("%2d   ", new Object[] { Integer.valueOf(t0[i].getPunkte()) });
	      System.out.printf("%2d/34 %n", new Object[] { Integer.valueOf(t0[i].getSiege() + t0[i].getNiederlagen() + t0[i].getUnentschieden()) });
	    }
	    System.out.println();
	  }

	  private void ausgabeAusstehendeTeams(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    System.out.println("Ausstehende Teams:");
	    for (int i = 0; i < l1.size(); i++)
	    {
	      System.out.printf("%25s  vs.  ", new Object[] { ((Team)l1.get(i)).getName() });
	      System.out.printf("%-25s  %n", new Object[] { ((Team)l2.get(i)).getName() });
	    }
	  }

	private void ausgabeMoeglichkeiten() {
		System.out.println("Ausgabe Möglichkeiten");
		for (int i = 0; i < moeglichkeiten.size(); i++) {
			System.out.print(moeglichkeiten.get(i) + " ");
		}
		System.out.println();
	}

	private void ausgabeTipps() {
		System.out.println("Ausgabe Tipps");
		for (int i = 0; i < tipps.size(); i++) {
			System.out.print(tipps.get(i) + " ");
		}
		System.out.println("");
	}

	
	private void zeigeSpieleAn(int tag) {
		int x = anzahlUebrigerSpieltage - tag - 1;
		// System.out.println("Beste Platzierung nach Spieltag:
		// "+(liga.getAustragungsart().getAnzahlSpieltage()-x));
		ausgabeMoeglichkeiten();
		// System.out.println(moeglichkeiten.size()+" "+merke.size());
		int j = 0;
		for (int i = 0; i < moeglichkeiten.size(); i = i + 2) {
			if (moeglichkeiten.get(i) == 1) {
				System.out.println(merke.get(i).getName() + "(w) vs " + merke.get(i + 1).getName());
			}
			if (moeglichkeiten.get(i) == 2) {
				System.out.println(merke.get(i).getName() + "(w) vs (w)" + merke.get(i + 1).getName());
			}
			if (moeglichkeiten.get(i) == 3) {
				System.out.println(merke.get(i).getName() + " vs (w)" + merke.get(i + 1).getName());
			}
			if (moeglichkeiten.get(i) == -1) {
				j++;
				System.out.println(merke.get(i).getName() + "(-1) vs (-1)" + merke.get(i + 1).getName());
			}
		}
		System.out.println("j: " + j + "   merkeSpielausgänge.size: " + merkeSpielausgang.size());
		for (int i = 0; i < merkeGegner.size(); i = i + 2) {
			if (merkeSpielausgang.get(i) == 1) {
				System.out.println(merkeGegner.get(i).getName() + "(w)  vs.  " + merkeGegner.get(i).getName());
			}
			if (merkeSpielausgang.get(i) == 2) {
				System.out.println(merkeGegner.get(i).getName() + "(w)  vs.  (w)" + merkeGegner.get(i).getName());
			}
			if (merkeSpielausgang.get(i) == 3) {
				System.out.println(merkeGegner.get(i).getName() + "  vs.  (w)" + merkeGegner.get(i).getName());
			}

		}
	}


	  private void ausgabeErmittelt()
	  {
	    System.out.println("abc=max: " + this.abc2);
	    System.out.println("init=max: " + this.abcdef2);
	    System.out.println("rek=max: " + this.rek2);
	    System.out.println("verbesserut hat x mal geholhen: " + this.nMin);
	    
	    System.out.println("abc=min: " + this.abc);
	    System.out.println("init=min: " + this.abcdef);
	    System.out.println("nachb=min: " + this.abcdefghi);
	    System.out.println("rek=min: " + this.rek);
	    System.out.println("verbesserut hat x mal geholhen: " + this.nMin);
	  }

	
}
