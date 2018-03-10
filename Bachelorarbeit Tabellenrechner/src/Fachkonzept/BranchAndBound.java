package Fachkonzept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;	
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

/**
 * Die Klasse AlgorithmusMAX_MIN berechnet für ein Team den Maximalen und/oder
 * Minimalen zu erreichenden Platz für die noch auszustehenden Spieltage.
 * 
 * @author T.Benning
 *
 */
public class BranchAndBound implements Informant {
	private Koordinator k;
	private int maxPZ, minPZ;
	private int maxTP, minTP;
	private int max, min;
	private int tmpMinTP;
	private int S = 0;
	private boolean abbruch = false;
	private ArrayList<Team> ausstehendeNamenHeim;
	private ArrayList<Team> ausstehendeNamenAusw;
	private ArrayList<Team> O, M, U;
	private ArrayList<Team> merke;
	private int hallo = 0;
	private ArrayList<Integer> merkeSpielausgang;
	private ArrayList<Team> merkeGegner;
	private ArrayList<Integer> xxx;
	private ArrayList<Team> nachbesserung;
	private ArrayList<Team> test1;
	private ArrayList<Team> test2;
	private Team[] test3;
	private int uMax1 = 0;
	private int uMax2 = 0;
	private int uMax3 = 0;
	private int uMax4 = 0;
	private int uMax5 = 0;
	private int uMax6 = 0;
	private int uMax7 = 0;
	private int uMax8 = 0;
	private int uMax9 = 0;
	private int uMax10 = 0;
	private int uMax11 = 0;
	private int uMax12 = 0;
	private int uMax13 = 0;
	private int uMax14 = 0;
	private int uMax15 = 0;
	private int uMax16 = 0;
	private int uMax17 = 0;
	private int uMax18 = 0;

	private int uSMax1 = 0;
	private int uSMax2 = 0;
	private int uSMax3 = 0;
	private int uSMax4 = 0;
	private int uSMax5 = 0;
	private int uSMax6 = 0;
	private int uSMax7 = 0;
	private int uSMax8 = 0;
	private int uSMax9 = 0;
	private int uSMax10 = 0;
	private int uSMax11 = 0;
	private int uSMax12 = 0;

	private int uBMax = 0;
	private int uCMax = 0;
	private int uDMax = 0;
	private int uEMax = 0;
	private int uFMax = 0;
	private int uGMax = 0;

	private int uMin1 = 0;
	private int uMin2 = 0;
	private int uMin3 = 0;
	private int uMin4 = 0;
	private int uMin5 = 0;
	private int uMin6 = 0;
	private int uMin7 = 0;
	private int uMin8 = 0;
	private int uMin9 = 0;
	private int uMin10 = 0;
	private int uMin11 = 0;
	private int uMin12 = 0;
	private int uMin13 = 0;
	private int uMin14 = 0;
	private int uMin15 = 0;
	private int uMin16 = 0;
	private int uMin17 = 0;
	private int uMin18 = 0;

	private int uSMin1 = 0;
	private int uSMin2 = 0;
	private int uSMin3 = 0;
	private int uSMin4 = 0;
	private int uSMin5 = 0;
	private int uSMin6 = 0;
	private int uSMin7 = 0;
	private int uSMin8 = 0;
	private int uSMin9 = 0;
	private int uSMin10 = 0;
	private int uSMin11 = 0;
	private int uSMin12 = 0;

	private int uBMin = 0;
	private int uCMin = 0;
	private int uDMin = 0;
	private int uEMin = 0;
	private int uFMin = 0;
	private int uGMin = 0;

	private int abc = 0;
	private int abcdef = 0;
	private int abcdefghi = 0;
	private int rek = 0;
	private int nMin = 0;
	private int abc2 = 0;
	private int abcdef2 = 0;
	private int rek2 = 0;
	public Task<Long> task;
	public Thread berechnungen;

	private int oberGrenze; // die Grenze nach oben, die angibt welche Teams
							// nicht weiter betrachtet werden müssen, z.B da
							// diese zu viele Punkte haben
	private int unterGrenze; // die Grenze nach unten, die angibt welche Teams
								// nicht weiter betrachtet werden müssen
	private int durchlauf = 0; // wird benötigt um zu prüfen wie viele tage
								// schon in betracht gezogen wurden
	private int anzahlUebrigerSpieltage;
	private int anzahlTeams;
	private long dauer; // dauer für eine berechnung MAX MIN
	private boolean MAX; // um zu prüfen ob es sich um einen Algorithmus MAX
							// oder Algorithmus MIN handelt um so zum einen
							// richtig zu sortieren und den richtigen Wert zu
							// speichern
	private boolean inBearbeitung = false; // prüfen ob bereits ein MAX MIN für
											// ein Team berechnet wird

	private Liga liga;
	private Long[] dauerListe; // Eine Liste die die Zeiten für die Berechnungen
								// enthält
	private Team[][] ausstehendCpy; // Eine Kopie der noch auszustehenden
									// Spiele. Es wird eine Kopie benötigt, da
									// wir das Original noch unverändert
									// benötigen wenn mehrere MAX_MIN
									// berechnungen durchgeführt werden. So kann
									// einfach die Kopie für die berechnungen
									// verwendet werden
	private Team team;

	private ArrayList<Integer> moeglichkeiten; // Hier werden die möglichkeiten
												// in einer ArrayList
												// hinereinander gemerkt um sie
												// so einfach ab zu arbeiten.

	private static ArrayList<Integer> tipps = new ArrayList<Integer>();// Wenn
																		// spiele
																		// getippt
																		// wurden,
																		// werden
																		// diese
																		// Tipps
																		// hier
																		// gespeichert.
																		// (ein
																		// wert
																		// von
																		// -1
																		// bedeutet
																		// nicht
																		// getippt)

	private ArrayList<Beobachter> beobachterListe = new ArrayList<Beobachter>(); // eine
																					// Liste
																					// die
																					// alle
																					// zu
																					// informierenden
																					// Objekte
																					// enthält
	private LinkedList<Team> warteListe = new LinkedList<Team>(); // Eine Liste
																	// die Teams
																	// aufnimmt
																	// welche
																	// als
																	// nächstes
																	// das MAX
																	// MIN
																	// berechnet
																	// werden
																	// soll

	/**
	 * Startet den Algorithmus zur Berechnung des Maximalen und Minimalen zu
	 * erreichbare platzierung
	 * 
	 * @param t
	 *            das Team für welches der Maximale und Minimale Wert berechnet
	 *            werden soll
	 */
	public void starteAlgorithmen(Team t) {

		// Die berechnung wird in einem eigenen Task abgearbeitet,
		// so blockiert die GUI nicht und der Ladebalken kann stehts
		// aktualisiert werden
		task = new Task<Long>() {

			// wird beim starten des Tasks ausgeführt
			@Override
			public Long call() {
				long anfang = System.currentTimeMillis();// startzeit des
															// Algorithmus
				initialisiereAlgorithmus(t);

				// führe die Berechnungen so oft aus wie es noch übrige
				// Spieltage gibt,
				// da man von jedem Spieltag
				// das zu erreichende Maximum bestimmen möchte. Hierbei ist zu
				// beachten
				// das erst der Spieltag "n" betrachtet wird dann "n-1",
				// "n-2"...
				int kk = 0;
				int a = 0;
				for (int tag = anzahlUebrigerSpieltage - 1; tag >= 0; tag--) {

					System.out.println("----------tag: " + (34 - kk) + "-----------------");
					kk++;


					berechneMAX(tag);
					
					updateProgress(++a, anzahlUebrigerSpieltage * 2);

					berechneMIN(tag);
					

					durchlauf++;
					updateProgress(++a, anzahlUebrigerSpieltage * 2);
				}
				System.out.println("Anzahl: " + hallo);
				liga.ermittelPlatzierung(liga.getTeams(), Liga.SORTIERUNG_NORMAL, null);
				long ende = System.currentTimeMillis() - anfang; // dauer des
																	// algorithmus
				return ende;
			}
		};

		// der Ladebalken wird mit dem Task "verbunden"
		ProgressBar bar = Koordinator.getKoordinator().getProgressBar();
		bar.progressProperty().bind(task.progressProperty());

		// wenn der Task fertig abgearbeitet ist
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent wse) {
				// dann wird die dauer gesetzt und alle Beobachter darüber
				// informiert
				dauer = task.getValue();
				dauerListe[t.getPlatzierung() - 1] = dauer;// Die dauer wird in
															// die Liste
															// hinzugefügt an
															// der Stelle wo
															// sich das Team
															// auch befindet (-1
															// da platzierung
															// nicht bei 0
															// anfängt)
				if (!beobachterListe.isEmpty()) {
					notifyAlleBeobachter(team);
				}
				bar.progressProperty().unbind();
				bar.setProgress(0.0);

				inBearbeitung = false;
				if (!warteListe.isEmpty()) {
					starteAlgorithmen(warteListe.removeLast());
				} else {
					ausgabeErmittelt();
				}

			}
		});

		task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				task = null;
				berechnungen = null;
				bar.progressProperty().unbind();
				bar.setProgress(0.0);
				warteListe.clear();
				inBearbeitung = false;

				dauerListe[t.getPlatzierung() - 1] = (long) -1;// Die dauer wird
																// in die Liste
																// hinzugefügt
																// an der Stelle
																// wo sich das
																// Team auch
																// befindet (-1
																// da
																// platzierung
																// nicht bei 0
																// anfängt)

				for (int i = anzahlUebrigerSpieltage - durchlauf; i >= 0; i--) {
					t.setMaxPlatzSpieltag(-100);
					t.setMinPlatzSpieltag(-100);
				}
				for (Beobachter b : beobachterListe) {
					b.updateCancelled(t);
				}
			}
		});

		// Wenn noch keine Berechnung durchgeführt wird, dann..
		if (!inBearbeitung) {
			inBearbeitung = true;
			for (Beobachter b : beobachterListe) {
				b.updateAnfang(t);
			}
			// .. Ein Thread wird erstellt der den Task abarbeitet
			// dieser wird dann gestartet
			berechnungen = new Thread(task);
			berechnungen.start();
		} else {
			// ansonsten wird das Team in die WarteListe aufgenommen
			warteListe.addFirst(t);
		}
	}

	/**
	 * Initialisiert den Algorithmus
	 * 
	 * @param t
	 *            das Team welches der Maximum und Minimum berechnet werden soll
	 */
	private void initialisiereAlgorithmus(Team t) {
		k = Koordinator.getKoordinator();
		if (dauerListe == null) {
			this.dauerListe = new Long[k.getAktiveLiga().getTeams().length];
		}
		this.team = t;
		this.durchlauf = 0;
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

	private void berechneMAX(int tag)
	  {
	    this.MAX = true;
	    this.liga.ermittelPlatzierung(this.k.getAktiveLiga().getTeams(), 2, this.team);
	    this.max = this.team.getPlatzierung();
	    this.maxPZ = (this.team.getPunkte() + Zaehlweise.PUNKTE_S * (tag + 1));
	    this.minPZ = this.team.getPunkte();
	    
	    pruefeSchrankeMAX();
	    erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());
	    
	    Team[] tmpTabelle = erstelleInitialeTabelle();
	    
	    erstelleWerteMAX(this.k.getAktiveLiga().getTeams(), tag + 1);
	    int aaMax = this.max;
	    int aaMaxTP = this.maxTP;
	    
	    ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList();
	    for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
	      tmpAusstehendeNamenHeim.add((Team)this.ausstehendeNamenHeim.get(i));
	    }
	    ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList();
	    for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
	      tmpAusstehendeNamenAusw.add((Team)this.ausstehendeNamenAusw.get(i));
	    }
	    if (this.maxTP == this.minTP)
	    {
	      this.abc2 += 1;
	      this.team.setMaxPlatzSpieltag(this.maxTP);
	      return;
	    }
	    erzeugeMengenTabelleMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    if (this.max == this.maxTP)
	    {
	      this.abcdef2 += 1;
	      ermittel(0, aaMaxTP, 0, this.max);
	      this.team.setMaxPlatzSpieltag(this.max);
	      return;
	    }
	    int tmpMaxTP = verbessereSchrankeMAX(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    if (tmpMaxTP == this.max)
	    {
	      this.abcdef2 += 1;
	      
	      ermittel(0, aaMaxTP, 0, this.max);
	      this.team.setMaxPlatzSpieltag(this.max);
	      return;
	    }
	    erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    if (tmpMaxTP == this.max)
	    {
	      this.abcdef2 += 1;
	      ermittel(0, aaMaxTP, 0, this.max);
	      
	      this.team.setMaxPlatzSpieltag(this.max);
	      return;
	    }
	    if (this.maxTP == this.minTP)
	    {
	      if (this.maxTP < this.max) {
	        this.max = this.maxTP;
	      }
	    }
	    else if (this.maxTP < this.max)
	    {
	      System.out.println("Starten der Rekursion MAX");
	      erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      int merkeMaxTP = this.maxTP;
	      int merkeMinTP = this.minTP;
	      backtracking(1, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	      this.maxTP = merkeMaxTP;
	      this.minTP = merkeMinTP;
	      erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      backtracking(2, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	      this.maxTP = merkeMaxTP;
	      this.minTP = merkeMinTP;
	      erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      backtracking(3, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	    }
	    this.rek2 += 1;
	    ermittel(0, aaMaxTP, 0, this.max);
	    
	    this.team.setMaxPlatzSpieltag(this.max);
	  }
	  
	  private void berechneMIN(int tag)
	  {
	    this.MAX = false;
	    this.liga.ermittelPlatzierung(this.k.getAktiveLiga().getTeams(), 3, this.team);
	    this.min = this.team.getPlatzierung();
	    this.maxPZ = this.team.getPunkte();
	    this.minPZ = (this.team.getPunkte() - Zaehlweise.PUNKTE_S * (tag + 1));
	    
	    pruefeSchrankeMIN();
	    erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());
	    
	    Team[] tmpTabelle = erstelleInitialeTabelle();
	    
	    erstelleWerteMIN(this.k.getAktiveLiga().getTeams(), tag + 1);
	    int aaMin = this.min;
	    int aaMinTP = this.minTP;
	    
	    ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList();
	    for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
	      tmpAusstehendeNamenHeim.add((Team)this.ausstehendeNamenHeim.get(i));
	    }
	    ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList();
	    for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
	      tmpAusstehendeNamenAusw.add((Team)this.ausstehendeNamenAusw.get(i));
	    }
	    if (this.maxTP == this.minTP)
	    {
	      this.abc += 1;
	      this.team.setMinPlatzSpieltag(this.minTP);
	      return;
	    }
	    erzeugeMengenTabelleMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    
	    test(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	    tmpAusstehendeNamenHeim = this.test1;
	    tmpAusstehendeNamenAusw = this.test2;
	    
	    erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    if (this.min == this.minTP)
	    {
	      this.abcdef += 1;
	      
	      ermittel(0, aaMinTP, 0, this.min);
	      this.team.setMinPlatzSpieltag(this.min);
	      
	      return;
	    }
	    this.tmpMinTP = verbessereSchrankeMIN(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	    if (this.tmpMinTP == this.min)
	    {
	      this.abcdef += 1;
	      
	      ermittel(0, aaMinTP, 0, this.min);
	      this.team.setMinPlatzSpieltag(this.min);
	      return;
	    }
	    int altMin = this.min;
	    int altMinTP = this.minTP;
	    int altMaxTP = this.maxTP;
	    int altMinPZ = this.minPZ;
	    int altMaxPZ = this.maxPZ;
	    erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(this.k.getAktiveLiga().getTeams(), tag);
	    if (altMin < this.min) {
	      this.nMin += 1;
	    }
	    if (this.nachbesserung != null) {
	      this.nachbesserung.clear();
	    }
	    this.minTP = altMinTP;
	    this.maxTP = altMaxTP;
	    this.minPZ = altMinPZ;
	    this.maxPZ = altMaxPZ;
	    
	    this.min = Math.max(altMin, this.min);
	    if (this.tmpMinTP == this.min)
	    {
	      this.abcdefghi += 1;
	      ermittel(0, aaMinTP, 0, this.min);
	      
	      this.team.setMinPlatzSpieltag(this.min);
	      return;
	    }
	    this.S = (this.minTP - this.tmpMinTP);
	    
	    erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	    
	    int altMinalt = this.min;
	    if (this.maxTP == this.minTP)
	    {
	      if (this.maxTP > this.min) {
	        this.min = this.maxTP;
	      }
	    }
	    else if (this.minTP > this.min)
	    {
	      System.out.println("Starte mit der Rekursion in MIN");
	      erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      int merkeMaxTP = this.maxTP;
	      int merkeMinTP = this.minTP;
	      backtracking(1, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	      this.maxTP = merkeMaxTP;
	      this.minTP = merkeMinTP;
	      erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      backtracking(2, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	      this.maxTP = merkeMaxTP;
	      this.minTP = merkeMinTP;
	      erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      backtracking(3, 1, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, tmpTabelle);
	    }
	    System.out.println("altes Min: " + altMinalt + "  neues Min: " + this.min);
	    this.rek += 1;
	    
	    ermittel(0, aaMinTP, 0, this.min);
	    this.team.setMinPlatzSpieltag(this.min);
	  }

	  private void erzeugeInitialeMengen(Team[] tmpTeam)
	  {
	    this.O = new ArrayList<Team>();
	    this.M = new ArrayList<Team>();
	    this.U = new ArrayList<Team>();
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (this.MAX)
	      {
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
	      else if (tmpTeam[i].getPunkte() >= this.maxPZ) {
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

	/**
	 * Prüft die Schranken für das Maximum und setzt dementsprechend die
	 * Möglichkeiten
	 */
	  private void pruefeSchrankeMAX()
	  {
	    this.unterGrenze = this.minPZ;
	    this.oberGrenze = this.maxPZ;
	    
	    this.moeglichkeiten = new ArrayList();
	    this.ausstehendeNamenHeim = new ArrayList();
	    this.ausstehendeNamenAusw = new ArrayList();
	    this.merke = new ArrayList();
	    this.merkeSpielausgang = new ArrayList();
	    this.merkeGegner = new ArrayList();
	    for (int x = 0; x < this.anzahlUebrigerSpieltage - this.durchlauf; x++) {
	      for (int i = 0; i < this.ausstehendCpy[x].length; i += 2)
	      {
	        this.merke.add(this.ausstehendCpy[x][i]);
	        this.merke.add(this.ausstehendCpy[x][(i + 1)]);
	        if ((!tipps.isEmpty()) && (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() != -1))
	        {
	          if (this.ausstehendCpy[x][i].getName().equals(this.team.getName()))
	          {
	            if (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() == 2)
	            {
	              this.maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
	              this.minPZ += Zaehlweise.PUNKTE_U;
	            }
	            if (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() == 3)
	            {
	              this.maxPZ -= Zaehlweise.PUNKTE_S;
	              this.minPZ -= Zaehlweise.PUNKTE_S;
	            }
	          }
	          else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName()))
	          {
	            if (((Integer)tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == 3)
	            {
	              this.maxPZ -= Zaehlweise.PUNKTE_S;
	              this.minPZ -= Zaehlweise.PUNKTE_S;
	            }
	            if (((Integer)tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == 2)
	            {
	              this.maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
	              this.minPZ += Zaehlweise.PUNKTE_U;
	            }
	          }
	          this.unterGrenze = this.minPZ;
	          this.oberGrenze = this.maxPZ;
	          this.moeglichkeiten.add((Integer)tipps.get(i + x * this.anzahlTeams));
	          this.moeglichkeiten.add((Integer)tipps.get(i + 1 + x * this.anzahlTeams));
	        }
	        else if (this.ausstehendCpy[x][i].getName().equals(this.team.getName()))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(1));
	          this.moeglichkeiten.add(Integer.valueOf(3));
	        }
	        else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName()))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(3));
	          this.moeglichkeiten.add(Integer.valueOf(1));
	        }
	        else if ((this.ausstehendCpy[x][i].getPunkte() > this.oberGrenze) || 
	          (this.ausstehendCpy[x][i].getPunkte() <= this.unterGrenze))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(1));
	          this.moeglichkeiten.add(Integer.valueOf(3));
	        }
	        else if ((this.ausstehendCpy[x][(i + 1)].getPunkte() > this.oberGrenze) || 
	          (this.ausstehendCpy[x][(i + 1)].getPunkte() <= this.unterGrenze))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(3));
	          this.moeglichkeiten.add(Integer.valueOf(1));
	        }
	        else
	        {
	          this.ausstehendeNamenHeim.add(this.ausstehendCpy[x][i]);
	          this.ausstehendeNamenAusw.add(this.ausstehendCpy[x][(i + 1)]);
	          this.moeglichkeiten.add(Integer.valueOf(-1));
	          this.moeglichkeiten.add(Integer.valueOf(-1));
	        }
	      }
	    }
	  }

	  private void pruefeSchrankeMIN()
	  {
	    this.unterGrenze = this.minPZ;
	    this.oberGrenze = this.maxPZ;
	    
	    this.moeglichkeiten = new ArrayList();
	    this.ausstehendeNamenHeim = new ArrayList();
	    this.ausstehendeNamenAusw = new ArrayList();
	    for (int x = 0; x < this.anzahlUebrigerSpieltage - this.durchlauf; x++) {
	      for (int i = 0; i < this.ausstehendCpy[x].length; i += 2) {
	        if ((!tipps.isEmpty()) && (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() != -1))
	        {
	          if (this.ausstehendCpy[x][i].getName().equals(this.team.getName()))
	          {
	            if (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() == 1)
	            {
	              this.minPZ += Zaehlweise.PUNKTE_S;
	              this.maxPZ += Zaehlweise.PUNKTE_S;
	            }
	            if (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() == 2)
	            {
	              this.minPZ += Zaehlweise.PUNKTE_U;
	              this.maxPZ += Zaehlweise.PUNKTE_U;
	            }
	          }
	          else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName()))
	          {
	            if (((Integer)tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == 1)
	            {
	              this.minPZ += Zaehlweise.PUNKTE_S;
	              this.maxPZ += Zaehlweise.PUNKTE_S;
	            }
	            if (((Integer)tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == 2)
	            {
	              this.minPZ += Zaehlweise.PUNKTE_U;
	              this.maxPZ += Zaehlweise.PUNKTE_U;
	            }
	          }
	          this.unterGrenze = this.minPZ;
	          this.oberGrenze = this.maxPZ;
	          this.moeglichkeiten.add((Integer)tipps.get(i + x * this.anzahlTeams));
	          this.moeglichkeiten.add((Integer)tipps.get(i + 1 + x * this.anzahlTeams));
	        }
	        else if (this.ausstehendCpy[x][i].getName().equals(this.team.getName()))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(3));
	          this.moeglichkeiten.add(Integer.valueOf(1));
	        }
	        else if (this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName()))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(1));
	          this.moeglichkeiten.add(Integer.valueOf(3));
	        }
	        else if ((this.ausstehendCpy[x][i].getPunkte() >= this.oberGrenze) || 
	          (this.ausstehendCpy[x][i].getPunkte() < this.unterGrenze))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(3));
	          this.moeglichkeiten.add(Integer.valueOf(1));
	        }
	        else if ((this.ausstehendCpy[x][(i + 1)].getPunkte() >= this.oberGrenze) || 
	          (this.ausstehendCpy[x][(i + 1)].getPunkte() < this.unterGrenze))
	        {
	          this.moeglichkeiten.add(Integer.valueOf(1));
	          this.moeglichkeiten.add(Integer.valueOf(3));
	        }
	        else
	        {
	          this.ausstehendeNamenHeim.add(this.ausstehendCpy[x][i]);
	          this.ausstehendeNamenAusw.add(this.ausstehendCpy[x][(i + 1)]);
	          this.moeglichkeiten.add(Integer.valueOf(-1));
	          this.moeglichkeiten.add(Integer.valueOf(-1));
	        }
	      }
	    }
	  }

	  private Team[] erstelleInitialeTabelle()
	  {
	    Team[] tmpTeam = null;
	    int index2 = 0;
	    
	    tmpTeam = new Team[this.liga.getTeams().length];
	    for (int i = 0; i < this.liga.getTeams().length; i++) {
	      tmpTeam[i] = new Team(this.liga.getTeams()[i]);
	    }
	    for (int j = 0; j < this.moeglichkeiten.size(); j++)
	    {
	      int index = 0;
	      if ((j % this.liga.getTeams().length == 0) && (j != 0)) {
	        index2++;
	      }
	      for (int k = 0; k < tmpTeam.length; k++) {
	        if (this.ausstehendCpy[index2][(j % this.liga.getTeams().length)].getName().equals(tmpTeam[k].getName())) {
	          index = k;
	        }
	      }
	      if (((Integer)this.moeglichkeiten.get(j)).intValue() == 1) {
	        tmpTeam[index].sieg();
	      }
	      if (((Integer)this.moeglichkeiten.get(j)).intValue() == 2) {
	        tmpTeam[index].unentschieden();
	      }
	      if (((Integer)this.moeglichkeiten.get(j)).intValue() == 3) {
	        tmpTeam[index].niederlage();
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    return tmpTeam;
	  }

	  private void erstelleInitialeLoesungVarianteB(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    for (int i = 0; i < t.length; i++) {
	      for (int j = 0; j < l1.size(); j++)
	      {
	        if (tmpTeam[i].getName().equals(((Team)l1.get(j)).getName())) {
	          tmpTeam[i].unentschieden();
	        }
	        if (tmpTeam[i].getName().equals(((Team)l2.get(j)).getName())) {
	          tmpTeam[i].unentschieden();
	        }
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteC(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    int durchschnitt = 0;
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++)
	    {
	      tmpTeam[i] = new Team(t[i]);
	      for (int j = 0; j < l1.size(); j++)
	      {
	        if (tmpTeam[i].getName().equals(((Team)l1.get(j)).getName())) {
	          durchschnitt += tmpTeam[i].getPunkte();
	        }
	        if (tmpTeam[i].getName().equals(((Team)l2.get(j)).getName())) {
	          durchschnitt += tmpTeam[i].getPunkte();
	        }
	      }
	    }
	    if (l1.size() > 0) {
	      durchschnitt /= l1.size() * 2;
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < heim.size(); i++) {
	      if (this.MAX)
	      {
	        if ((((Team)heim.get(i)).getPunkte() > durchschnitt) && (((Team)ausw.get(i)).getPunkte() > durchschnitt))
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	        }
	        else if ((((Team)heim.get(i)).getPunkte() > durchschnitt) && (((Team)ausw.get(i)).getPunkte() <= durchschnitt))
	        {
	          ((Team)heim.get(i)).niederlage();
	          ((Team)ausw.get(i)).sieg();
	        }
	        else if ((((Team)heim.get(i)).getPunkte() <= durchschnitt) && (((Team)ausw.get(i)).getPunkte() > durchschnitt))
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	        }
	        else if ((((Team)heim.get(i)).getPunkte() <= durchschnitt) && (((Team)ausw.get(i)).getPunkte() <= durchschnitt))
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	        }
	      }
	      else if ((((Team)heim.get(i)).getPunkte() >= durchschnitt) && (((Team)ausw.get(i)).getPunkte() >= durchschnitt))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	      }
	      else if ((((Team)heim.get(i)).getPunkte() > durchschnitt) && (((Team)ausw.get(i)).getPunkte() <= durchschnitt))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	      }
	      else if ((((Team)heim.get(i)).getPunkte() < durchschnitt) && (((Team)ausw.get(i)).getPunkte() >= durchschnitt))
	      {
	        ((Team)heim.get(i)).niederlage();
	        ((Team)ausw.get(i)).sieg();
	      }
	      else if ((((Team)heim.get(i)).getPunkte() < durchschnitt) && (((Team)ausw.get(i)).getPunkte() < durchschnitt))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteD(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    int durchschnitt = 0;
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++)
	    {
	      tmpTeam[i] = new Team(t[i]);
	      for (int j = 0; j < l1.size(); j++)
	      {
	        if (tmpTeam[i].getName().equals(((Team)l1.get(j)).getName())) {
	          durchschnitt += tmpTeam[i].getPunkte();
	        }
	        if (tmpTeam[i].getName().equals(((Team)l2.get(j)).getName())) {
	          durchschnitt += tmpTeam[i].getPunkte();
	        }
	      }
	    }
	    if (l1.size() > 0) {
	      durchschnitt /= l1.size() * 2;
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team)heim.get(i), heim, ausw);
	      int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team)ausw.get(i), heim, ausw);
	      if (this.MAX)
	      {
	        if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim > ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw)
	        {
	          ((Team)heim.get(i)).niederlage();
	          ((Team)ausw.get(i)).sieg();
	        }
	        else if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim < ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	        }
	        else
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	        }
	      }
	      else if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim > ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw)
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	      }
	      else if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim < ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw)
	      {
	        ((Team)heim.get(i)).niederlage();
	        ((Team)ausw.get(i)).sieg();
	      }
	      else
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteE(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team)heim.get(i), heim, ausw);
	      int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team)ausw.get(i), heim, ausw);
	      if (this.MAX)
	      {
	        if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw > 3)
	        {
	          ((Team)heim.get(i)).niederlage();
	          ((Team)ausw.get(i)).sieg();
	        }
	        else if (((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageAusw > 3)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	        }
	        else
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	        }
	      }
	      else if (((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageAusw < 3)
	      {
	        ((Team)ausw.get(i)).niederlage();
	        ((Team)heim.get(i)).sieg();
	      }
	      else if (((Team)ausw.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team)heim.get(i)).getPunkte() + 3 * offeneSpieltageAusw < 3)
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	      }
	      else
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteMAX_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    int px = this.maxPZ;
	    
	    ArrayList<Integer> mins = new ArrayList();
	    ArrayList<Integer> maxs = new ArrayList();
	    for (int i = 0; i < t.length; i++)
	    {
	      int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
	      mins.add(Integer.valueOf(t[i].getPunkte()));
	      maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int minPY = 0;int maxPY = 0;int minPZ = 0;int maxPZ = 0;
	      int y = -1;
	      int z = -1;
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)heim.get(i)).getName().equals(t[j].getName()))
	        {
	          minPY = ((Integer)mins.get(j)).intValue();
	          maxPY = ((Integer)maxs.get(j)).intValue();
	          y = j;
	        }
	        else if (((Team)ausw.get(i)).getName().equals(t[j].getName()))
	        {
	          minPZ = ((Integer)mins.get(j)).intValue();
	          maxPZ = ((Integer)maxs.get(j)).intValue();
	          z = j;
	        }
	      }
	      if ((minPY == px) && (minPZ == px))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if (((minPY == px - 1) || (minPY == px - 2)) && (minPZ >= px))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if (((minPZ == px - 1) || (minPZ == px - 2)) && (minPY >= px))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteMIN_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team)heim.get(i), heim, ausw);
	      int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team)ausw.get(i), heim, ausw);
	      if ((((Team)heim.get(i)).getPunkte() == this.maxPZ - 1) && (((Team)ausw.get(i)).getPunkte() == this.maxPZ - 1))
	      {
	        ((Team)ausw.get(i)).unentschieden();
	        ((Team)heim.get(i)).unentschieden();
	      }
	      else if (((Team)heim.get(i)).getPunkte() < ((Team)ausw.get(i)).getPunkte())
	      {
	        ((Team)ausw.get(i)).niederlage();
	        ((Team)heim.get(i)).sieg();
	      }
	      else if (((Team)ausw.get(i)).getPunkte() < ((Team)heim.get(i)).getPunkte())
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	      }
	      else if (offeneSpieltageHeim < offeneSpieltageAusw)
	      {
	        ((Team)ausw.get(i)).niederlage();
	        ((Team)heim.get(i)).sieg();
	      }
	      else if (offeneSpieltageAusw < offeneSpieltageHeim)
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	      }
	      else
	      {
	        ((Team)ausw.get(i)).niederlage();
	        ((Team)heim.get(i)).sieg();
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteMIN_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2, boolean nachb)
	  {
	    Team[] tmpTeam = null;
	    
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    int px = this.team.getPunkte();
	    
	    ArrayList<Integer> mins = new ArrayList();
	    ArrayList<Integer> maxs = new ArrayList();
	    for (int i = 0; i < t.length; i++)
	    {
	      int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
	      mins.add(Integer.valueOf(t[i].getPunkte()));
	      maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int minPY = 0;int maxPY = 0;int minPZ = 0;int maxPZ = 0;
	      int y = -1;
	      int z = -1;
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)heim.get(i)).getName().equals(t[j].getName()))
	        {
	          minPY = ((Integer)mins.get(j)).intValue();
	          maxPY = ((Integer)maxs.get(j)).intValue();
	          y = j;
	        }
	        else if (((Team)ausw.get(i)).getName().equals(t[j].getName()))
	        {
	          minPZ = ((Integer)mins.get(j)).intValue();
	          maxPZ = ((Integer)maxs.get(j)).intValue();
	          z = j;
	        }
	      }
	      if ((minPY >= px) || (minPZ >= px))
	      {
	        if (minPY >= minPZ)
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	      }
	      else if ((maxPY < px) && (maxPZ < px))
	      {
	        if (minPY >= minPZ)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	      }
	      else if ((maxPY < px) && (maxPZ >= px))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if ((maxPZ < px) && (maxPY >= px))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if ((maxPY == px + 2) && (maxPZ == px + 2))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	      else if ((maxPY >= px) && (maxPY <= px + 2) && (maxPZ >= px) && (maxPZ <= px + 2))
	      {
	        if (minPY >= minPZ)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	      }
	      else if ((maxPY == px + 2) && (maxPZ > px + 2))
	      {
	        if (minPY == px - 1)
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	        }
	        else if (minPY < minPZ)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	        }
	      }
	      else if ((maxPZ == px + 2) && (maxPY > px + 2))
	      {
	        if (minPZ == px - 1)
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	        }
	        else if (minPZ < minPY)
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)heim.get(i)).unentschieden();
	          ((Team)ausw.get(i)).unentschieden();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	        }
	      }
	      else if (((maxPY == px) || (maxPY == px + 1)) && (maxPZ > px + 2))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if ((maxPZ == px + 2) && (maxPY > px + 2))
	      {
	        if (minPY * maxPY < minPZ * maxPZ)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	        else if (minPZ * maxPZ < minPY * maxPY)
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)ausw.get(i)).unentschieden();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	        }
	      }
	      else if (((maxPZ == px) || (maxPZ == px + 1)) && (maxPY > px + 2))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if (minPY < minPZ)
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if (minPZ < minPY)
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if (minPY == minPZ)
	      {
	        if (maxPY <= maxPZ)
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	    if (!nachb)
	    {
	      if (this.nachbesserung != null) {
	        this.nachbesserung.clear();
	      }
	      for (int i = tmpTeam.length - 1; i >= 0; i--) {
	        if (tmpTeam[i].getPunkte() < this.team.getPunkte())
	        {
	          if (this.nachbesserung == null) {
	            this.nachbesserung = new ArrayList();
	          }
	          this.nachbesserung.add(tmpTeam[i]);
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteMAX_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    Team[] tmpTeam = null;
	    
	    tmpTeam = new Team[t.length];
	    for (int i = 0; i < t.length; i++) {
	      tmpTeam[i] = new Team(t[i]);
	    }
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(tmpTeam[j].getName())) {
	          heim.add(tmpTeam[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < tmpTeam.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(tmpTeam[j].getName())) {
	          ausw.add(tmpTeam[j]);
	        }
	      }
	    }
	    int px = this.maxPZ;
	    
	    ArrayList<Integer> mins = new ArrayList();
	    ArrayList<Integer> maxs = new ArrayList();
	    for (int i = 0; i < t.length; i++)
	    {
	      int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
	      mins.add(Integer.valueOf(t[i].getPunkte()));
	      maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
	    }
	    for (int i = 0; i < heim.size(); i++)
	    {
	      int minPY = 0;int maxPY = 0;int minPZ = 0;int maxPZ = 0;
	      int y = -1;
	      int z = -1;
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)heim.get(i)).getName().equals(t[j].getName()))
	        {
	          minPY = ((Integer)mins.get(j)).intValue();
	          maxPY = ((Integer)maxs.get(j)).intValue();
	          y = j;
	        }
	        else if (((Team)ausw.get(i)).getName().equals(t[j].getName()))
	        {
	          minPZ = ((Integer)mins.get(j)).intValue();
	          maxPZ = ((Integer)maxs.get(j)).intValue();
	          z = j;
	        }
	      }
	      if ((minPY >= px) || (minPZ >= px))
	      {
	        if (minPY < minPZ)
	        {
	          ((Team)ausw.get(i)).sieg();
	          ((Team)heim.get(i)).niederlage();
	          mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	          maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	        }
	        else
	        {
	          ((Team)heim.get(i)).sieg();
	          ((Team)ausw.get(i)).niederlage();
	          mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	          maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	        }
	      }
	      else if ((maxPY < px) && (maxPZ < px))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	      else if ((maxPY <= px) && (maxPZ > px))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if ((maxPZ <= px) && (maxPY > px))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if ((maxPY == px - 2) && (maxPZ == px - 2))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	      else if ((maxPY <= px) && (maxPY <= px - 2) && (maxPZ <= px) && (maxPZ <= px - 2))
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	      else if ((maxPY == px - 2) && (maxPZ > px - 2))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if ((maxPZ == px - 2) && (maxPY > px - 2))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if ((maxPY == px) && (maxPZ > px - 2))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if ((maxPY == px - 2) && (maxPZ > px - 2))
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else if ((maxPZ == px) && (maxPY > px - 2))
	      {
	        ((Team)ausw.get(i)).sieg();
	        ((Team)heim.get(i)).niederlage();
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 3));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 3));
	      }
	      else if (minPY < minPZ)
	      {
	        ((Team)heim.get(i)).sieg();
	        ((Team)ausw.get(i)).niederlage();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 3));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 3));
	      }
	      else
	      {
	        ((Team)heim.get(i)).unentschieden();
	        ((Team)ausw.get(i)).unentschieden();
	        mins.set(y, Integer.valueOf(((Integer)mins.get(y)).intValue() + 1));
	        maxs.set(y, Integer.valueOf(((Integer)maxs.get(y)).intValue() - 2));
	        mins.set(z, Integer.valueOf(((Integer)mins.get(z)).intValue() + 1));
	        maxs.set(z, Integer.valueOf(((Integer)maxs.get(z)).intValue() - 2));
	      }
	    }
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    for (int i = 0; i < tmpTeam.length; i++) {
	      if (tmpTeam[i].getName().equals(this.team.getName())) {
	        if (this.MAX) {
	          this.max = tmpTeam[i].getPlatzierung();
	        } else {
	          this.min = tmpTeam[i].getPlatzierung();
	        }
	      }
	    }
	  }

	  private void erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(Team[] t, int tag)
	  {
	    int durchgang = 0;
	    int tmpMin = 0;
	    if (this.nachbesserung == null) {
	      return;
	    }
	    for (int b = 0; b < this.nachbesserung.size(); b++)
	    {
	      this.xxx = new ArrayList();
	      for (int a = t.length - 1; a >= 0; a--) {
	        for (int c = 0; c <= durchgang; c++) {
	          if (t[a].getName().equals(((Team)this.nachbesserung.get(c)).getName())) {
	            for (int x = 0; x < this.anzahlUebrigerSpieltage - this.durchlauf; x++) {
	              for (int i = 0; i < this.ausstehendCpy[x].length; i += 2) {
	                if ((this.ausstehendCpy[x][i].getName().equals(t[a].getName())) && (((Integer)tipps.get(i + x * this.anzahlTeams)).intValue() == -1))
	                {
	                  if (!this.ausstehendCpy[x][(i + 1)].getName().equals(this.team.getName()))
	                  {
	                    tipps.set(i + x * this.anzahlTeams, Integer.valueOf(3));
	                    tipps.set(i + 1 + x * this.anzahlTeams, Integer.valueOf(1));
	                    this.xxx.add(Integer.valueOf(i + x * this.anzahlTeams));
	                    this.xxx.add(Integer.valueOf(i + 1 + x * this.anzahlTeams));
	                  }
	                }
	                else if ((this.ausstehendCpy[x][(i + 1)].getName().equals(t[a].getName())) && (((Integer)tipps.get(i + 1 + x * this.anzahlTeams)).intValue() == -1)) {
	                  if (!this.ausstehendCpy[x][i].getName().equals(this.team.getName()))
	                  {
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
	      pruefeSchrankeMIN();
	      
	      erzeugeInitialeMengen(this.k.getAktiveLiga().getTeams());
	      
	      Team[] tmpTabelle = erstelleInitialeTabelle();
	      
	      erstelleWerteMIN(this.k.getAktiveLiga().getTeams(), tag + 1);
	      
	      ArrayList<Team> tmpAusstehendeNamenHeim = new ArrayList();
	      for (int i = 0; i < this.ausstehendeNamenHeim.size(); i++) {
	        tmpAusstehendeNamenHeim.add((Team)this.ausstehendeNamenHeim.get(i));
	      }
	      ArrayList<Team> tmpAusstehendeNamenAusw = new ArrayList();
	      for (int i = 0; i < this.ausstehendeNamenAusw.size(); i++) {
	        tmpAusstehendeNamenAusw.add((Team)this.ausstehendeNamenAusw.get(i));
	      }
	      erzeugeMengenTabelleMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
	      
	      erstelleInitialeLoesungVarianteMIN_G_Komplex(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw, true);
	      for (int i = 0; i < this.xxx.size(); i++) {
	        tipps.set(((Integer)this.xxx.get(i)).intValue(), Integer.valueOf(-1));
	      }
	      tmpMin = Math.max(tmpMin, this.min);
	      durchgang++;
	    }
	    this.min = tmpMin;
	  }

	  private void erstelleInitialeLoesungAlle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    if (this.MAX)
	    {
	      int altmax = this.max;
	      erstelleInitialeLoesungVarianteB(t, l1, l2);
	      int tmpMaxB = this.max;
	      
	      erstelleInitialeLoesungVarianteC(t, l1, l2);
	      int tmpMaxC = this.max;
	      
	      erstelleInitialeLoesungVarianteD(t, l1, l2);
	      int tmpMaxD = this.max;
	      
	      erstelleInitialeLoesungVarianteE(t, l1, l2);
	      int tmpMaxE = this.max;
	      
	      erstelleInitialeLoesungVarianteMAX_F(t, l1, l2);
	      int tmpMaxF = this.max;
	      
	      erstelleInitialeLoesungVarianteMAX_G_Komplex(t, l1, l2);
	      int tmpMaxG = this.max;
	      
	      int tmpMax = Math.min(tmpMaxB, tmpMaxC);
	      tmpMax = Math.min(tmpMax, tmpMaxD);
	      tmpMax = Math.min(tmpMax, tmpMaxE);
	      tmpMax = Math.min(tmpMax, tmpMaxF);
	      tmpMax = Math.min(tmpMax, tmpMaxG);
	      this.max = tmpMax;
	      if ((tmpMaxB != tmpMaxC) || (tmpMaxC != tmpMaxD) || (tmpMaxD != tmpMaxE) || (tmpMaxE != tmpMaxF) || (tmpMaxF != tmpMaxG)) {
	        if (altmax != this.max)
	        {
	          if (tmpMaxB == this.max) {
	            this.uBMax += 1;
	          }
	          if (tmpMaxC == this.max) {
	            this.uCMax += 1;
	          }
	          if (tmpMaxD == this.max) {
	            this.uDMax += 1;
	          }
	          if (tmpMaxE == this.max) {
	            this.uEMax += 1;
	          }
	          if (tmpMaxF == this.max) {
	            this.uFMax += 1;
	          }
	          if (tmpMaxG == this.max) {
	            this.uGMax += 1;
	          }
	        }
	      }
	    }
	    else
	    {
	      int altmin = this.min;
	      erstelleInitialeLoesungVarianteB(t, l1, l2);
	      int tmpMinB = this.min;
	      
	      erstelleInitialeLoesungVarianteC(t, l1, l2);
	      int tmpMinC = this.min;
	      
	      erstelleInitialeLoesungVarianteD(t, l1, l2);
	      int tmpMinD = this.min;
	      
	      erstelleInitialeLoesungVarianteE(t, l1, l2);
	      int tmpMinE = this.min;
	      
	      erstelleInitialeLoesungVarianteMIN_F(t, l1, l2);
	      int tmpMinF = this.min;
	      
	      erstelleInitialeLoesungVarianteMIN_G_Komplex(t, l1, l2, false);
	      int tmpMinG = this.min;
	      
	      int tmpMin = Math.max(tmpMinB, tmpMinC);
	      tmpMin = Math.max(tmpMin, tmpMinD);
	      tmpMin = Math.max(tmpMin, tmpMinE);
	      tmpMin = Math.max(tmpMin, tmpMinF);
	      tmpMin = Math.max(tmpMin, tmpMinG);
	      
	      this.min = tmpMin;
	      if ((tmpMinB != tmpMinC) || (tmpMinC != tmpMinD) || (tmpMinD != tmpMinE) || (tmpMinE != tmpMinF) || (tmpMinF != tmpMinG)) {
	        if (altmin != this.min)
	        {
	          if (tmpMinB == this.min) {
	            this.uBMin += 1;
	          }
	          if (tmpMinC == this.min) {
	            this.uCMin += 1;
	          }
	          if (tmpMinD == this.min) {
	            this.uDMin += 1;
	          }
	          if (tmpMinE == this.min) {
	            this.uEMin += 1;
	          }
	          if (tmpMinF == this.min) {
	            this.uFMin += 1;
	          }
	          if (tmpMinG == this.min) {
	            this.uGMin += 1;
	          }
	        }
	      }
	    }
	  }

	  private void erstelleWerteMAX(Team[] tmpTeam, int anzahlUebrigerTage)
	  {
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    this.minTP = this.team.getPlatzierung();
	    this.maxTP = 1;
	    for (int i = 0; i < tmpTeam.length; i++)
	    {
	      if (tmpTeam[i].getPunkte() <= this.maxPZ) {
	        break;
	      }
	      this.maxTP += 1;
	    }
	  }

	  private void erstelleWerteMIN(Team[] tmpTeam, int anzahlUebrigerTage)
	  {
	    if (this.MAX) {
	      this.liga.ermittelPlatzierung(tmpTeam, 2, this.team);
	    } else {
	      this.liga.ermittelPlatzierung(tmpTeam, 3, this.team);
	    }
	    this.maxTP = this.team.getPlatzierung();
	    this.minTP = tmpTeam.length;
	    for (int i = tmpTeam.length - 1; i >= 0; i--)
	    {
	      if (tmpTeam[i].getPunkte() >= this.minPZ) {
	        break;
	      }
	      this.minTP -= 1;
	    }
	  }


	  private void erzeugeMengenTabelleMAX(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int anzahlSpiele = 0;
	    for (int i = this.O.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.O.get(i)).getName().equals(t[j].getName())) {
	          this.O.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.U.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.U.get(i)).getName().equals(t[j].getName())) {
	          this.U.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.M.get(i)).getName().equals(t[j].getName())) {
	          this.M.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--)
	    {
	      anzahlSpiele = ausstehendeAnzahlVonSpielen((Team)this.M.get(i), l1, l2);
	      if (((Team)this.M.get(i)).getPunkte() > this.maxPZ)
	      {
	        if (anzahlSpiele > 0)
	        {
	          for (int j = l1.size() - 1; j >= 0; j--) {
	            if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).sieg();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).niederlage();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	            else if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).niederlage();
	                }
	              }
	              ((Team)this.M.get(i)).sieg();
	              
	              l1.remove(j);
	              l2.remove(j);
	            }
	          }
	          this.O.add((Team)this.M.remove(i));
	          this.maxTP += 1;
	          erzeugeMengenTabelleMAX(t, l1, l2);
	          break;
	        }
	        this.O.add((Team)this.M.remove(i));
	        this.maxTP += 1;
	      }
	      else if (((Team)this.M.get(i)).getPunkte() <= this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele)
	      {
	        if (anzahlSpiele > 0)
	        {
	          for (int j = l1.size() - 1; j >= 0; j--) {
	            if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).sieg();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).niederlage();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	            else if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).sieg();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).niederlage();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	          }
	          this.U.add((Team)this.M.remove(i));
	          this.minTP -= 1;
	          erzeugeMengenTabelleMAX(t, l1, l2);
	          break;
	        }
	        this.U.add((Team)this.M.remove(i));
	        this.minTP -= 1;
	      }
	    }
	  }

	  private void erzeugeMengenTabelleMIN(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int anzahlSpiele = 0;
	    for (int i = this.O.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.O.get(i)).getName().equals(t[j].getName())) {
	          this.O.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.U.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.U.get(i)).getName().equals(t[j].getName())) {
	          this.U.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.M.get(i)).getName().equals(t[j].getName())) {
	          this.M.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--)
	    {
	      anzahlSpiele = ausstehendeAnzahlVonSpielen((Team)this.M.get(i), l1, l2);
	      if (((Team)this.M.get(i)).getPunkte() >= this.maxPZ)
	      {
	        if (anzahlSpiele > 0)
	        {
	          for (int j = l1.size() - 1; j >= 0; j--) {
	            if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).niederlage();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	            else if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              ((Team)this.M.get(i)).niederlage();
	              l1.remove(j);
	              l2.remove(j);
	            }
	          }
	          this.O.add((Team)this.M.remove(i));
	          this.maxTP += 1;
	          erzeugeMengenTabelleMIN(t, l1, l2);
	          break;
	        }
	        this.O.add((Team)this.M.remove(i));
	        this.maxTP += 1;
	      }
	      else if (((Team)this.M.get(i)).getPunkte() < this.maxPZ - Zaehlweise.PUNKTE_S * anzahlSpiele)
	      {
	        if (anzahlSpiele > 0)
	        {
	          for (int j = l1.size() - 1; j >= 0; j--) {
	            if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              ((Team)this.M.get(i)).niederlage();
	              l1.remove(j);
	              l2.remove(j);
	            }
	            else if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).niederlage();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	          }
	          this.U.add((Team)this.M.remove(i));
	          this.minTP -= 1;
	          erzeugeMengenTabelleMIN(t, l1, l2);
	          break;
	        }
	        this.U.add((Team)this.M.remove(i));
	        this.minTP -= 1;
	      }
	    }
	  }

	  private void erzeugeMengenMAX(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    this.O = new ArrayList();
	    this.M = new ArrayList();
	    this.U = new ArrayList();
	    int anzahlSpiele = 0;
	    for (int i = 0; i < tmpTeam.length; i++)
	    {
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
	  
	  private void erzeugeMengenMIN(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    this.O = new ArrayList();
	    this.M = new ArrayList();
	    this.U = new ArrayList();
	    int anzahlSpiele = 0;
	    for (int i = 0; i < tmpTeam.length; i++)
	    {
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

	  private int verbessereNaivMAX(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int tmpMaxTP = this.maxTP;
	    ArrayList<String> raus = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      if ((!raus.contains(((Team)l1.get(i)).getName())) && (!raus.contains(((Team)l2.get(i)).getName()))) {
	        if (((((Team)l1.get(i)).getPunkte() == this.maxPZ) && (((Team)l2.get(i)).getPunkte() == this.maxPZ)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.maxPZ) && (((Team)l2.get(i)).getPunkte() == this.maxPZ - 1)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.maxPZ - 1) && (((Team)l2.get(i)).getPunkte() == this.maxPZ)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.maxPZ) && (((Team)l2.get(i)).getPunkte() == this.maxPZ - 2)) || (
	          (((Team)l1.get(i)).getPunkte() == this.maxPZ - 2) && (((Team)l2.get(i)).getPunkte() == this.maxPZ)))
	        {
	          tmpMaxTP++;
	          raus.add(((Team)l1.get(i)).getName());
	          raus.add(((Team)l2.get(i)).getName());
	        }
	      }
	    }
	    return tmpMaxTP;
	  }

	  private int verbessereNaivMIN(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int tmpMinTP = this.minTP;
	    ArrayList<String> raus = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      if ((!raus.contains(((Team)l1.get(i)).getName())) && (!raus.contains(((Team)l2.get(i)).getName()))) {
	        if (((((Team)l1.get(i)).getPunkte() == this.minPZ) && (((Team)l2.get(i)).getPunkte() == this.minPZ)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ + 1) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 1)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 1)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ + 1) && (((Team)l2.get(i)).getPunkte() == this.minPZ)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ + 2) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 2)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ + 3) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 3)) || 
	          ((((Team)l1.get(i)).getPunkte() == this.minPZ + 3) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 2)) || (
	          (((Team)l1.get(i)).getPunkte() == this.minPZ + 2) && (((Team)l2.get(i)).getPunkte() == this.minPZ + 3)))
	        {
	          tmpMinTP--;
	          raus.add(((Team)l1.get(i)).getName());
	          raus.add(((Team)l2.get(i)).getName());
	        }
	      }
	    }
	    return tmpMinTP;
	  }

	  private int verbessereSchrankeMINVarianteB(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t)
	  {
	    int px = this.team.getPunkte();
	    int siege = 0;
	    int unentschieden = 0;
	    for (int i = 0; i < this.M.size(); i++)
	    {
	      int diff = px - ((Team)this.M.get(i)).getPunkte();
	      while (diff >= 0) {
	        if ((diff - 3 >= 0) || (diff - 2 >= 0))
	        {
	          siege++;
	          diff -= 3;
	        }
	        else
	        {
	          if (diff - 1 < 0) {
	            break;
	          }
	          unentschieden++;
	          diff--;
	        }
	      }
	    }
	    ArrayList<Integer> offeneSiegeProMannschaft = new ArrayList();
	    double wert = siege + unentschieden / 2.0D;
	    for (int i = 0; i < t.length; i++)
	    {
	      int test = px - t[i].getPunkte();
	      test = Math.round(test / 3.0F);
	      
	      offeneSiegeProMannschaft.add(Integer.valueOf(test));
	    }
	    Collections.sort(offeneSiegeProMannschaft);
	    if (l1.size() < wert)
	    {
	      int abzug = 1;
	      for (int i = offeneSiegeProMannschaft.size() - 1; i >= 0; i--) {
	        if (l1.size() < wert - ((Integer)offeneSiegeProMannschaft.get(i)).intValue())
	        {
	          abzug++;
	          wert -= ((Integer)offeneSiegeProMannschaft.get(i)).intValue();
	        }
	        else
	        {
	          return this.minTP - abzug;
	        }
	      }
	      return this.minTP - 1;
	    }
	    return this.minTP;
	  }

	  private int verbessereSchrankeMINVarianteC(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int px = this.team.getPunkte();
	    for (int i = 0; i < l1.size(); i++)
	    {
	      int anzahlSpieltageY = ausstehendeAnzahlVonSpielen((Team)l1.get(i), l1, l2);
	      int anzahlSpieltageZ = ausstehendeAnzahlVonSpielen((Team)l2.get(i), l1, l2);
	      
	      int maxPY = ((Team)l1.get(i)).getPunkte() + 3 * anzahlSpieltageY;
	      int maxPZ = ((Team)l2.get(i)).getPunkte() + 3 * anzahlSpieltageZ;
	      if ((maxPY <= px + 2) && (maxPZ <= px + 2)) {
	        if ((maxPY == maxPZ) && 
	          (maxPY != px + 2)) {
	          return this.minTP - 1;
	        }
	      }
	    }
	    return this.minTP;
	  }

	  private int verbessereSchrankeMIN(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t)
	  {
	    int a = verbessereNaivMIN(l1, l2);
	    int b = verbessereSchrankeMINVarianteB(l1, l2, t);
	    int c = verbessereSchrankeMINVarianteC(l1, l2);
	    
	    int tmpMin = Math.min(this.minTP, a);
	    tmpMin = Math.min(tmpMin, b);
	    tmpMin = Math.min(tmpMin, c);
	    
	    return tmpMin;
	  }

	  private int verbessereSchrankeMAXVarianteB(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int punkte = l1.size() * 2;
	    
	    int summe = 0;
	    for (int i = 0; i < this.M.size(); i++) {
	      summe += this.maxPZ - ((Team)this.M.get(i)).getPunkte();
	    }
	    if (summe < punkte) {
	      return this.maxTP + 1;
	    }
	    return this.maxTP;
	  }

	  private int verbessereSchrankeMAX(ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int a = verbessereNaivMAX(l1, l2);
	    int b = verbessereSchrankeMAXVarianteB(l1, l2);
	    
	    int tmp = Math.max(this.maxTP, a);
	    tmp = Math.max(tmp, b);
	    return tmp;
	  }

	  private void test(ArrayList<Team> l1, ArrayList<Team> l2, Team[] t1)
	  {
	    ArrayList<Team> heim = new ArrayList();
	    ArrayList<Team> ausw = new ArrayList();
	    for (int i = 0; i < l1.size(); i++) {
	      for (int j = 0; j < t1.length; j++) {
	        if (((Team)l1.get(i)).getName().equals(t1[j].getName())) {
	          heim.add(t1[j]);
	        }
	      }
	    }
	    for (int i = 0; i < l2.size(); i++) {
	      for (int j = 0; j < t1.length; j++) {
	        if (((Team)l2.get(i)).getName().equals(t1[j].getName())) {
	          ausw.add(t1[j]);
	        }
	      }
	    }
	    int offeneSpiele = l1.size();
	    int px = this.team.getPunkte();
	    int siege = 0;
	    int unentschieden = 0;
	    for (int i = 0; i < this.M.size(); i++)
	    {
	      int diff = px - ((Team)this.M.get(i)).getPunkte();
	      while (diff >= 0) {
	        if ((diff - 3 >= 0) || (diff - 2 >= 0))
	        {
	          siege++;
	          diff -= 3;
	        }
	        else
	        {
	          if (diff - 1 < 0) {
	            break;
	          }
	          unentschieden++;
	          diff--;
	        }
	      }
	    }
	    double wert = siege + unentschieden / 2.0D;
	    while (wert > offeneSpiele)
	    {
	      this.liga.ermittelPlatzierung(t1, 3, this.team);
	      erzeugeMengenMIN(t1, heim, ausw);
	      Team m = (Team)this.M.get(this.M.size() - 1);
	      for (int j = heim.size() - 1; j >= 0; j--) {
	        if (((Team)heim.get(j)).getName().equals(m.getName()))
	        {
	          ((Team)heim.get(j)).niederlage();
	          ((Team)ausw.get(j)).sieg();
	          heim.remove(j);
	          ausw.remove(j);
	        }
	        else if (((Team)ausw.get(j)).getName().equals(m.getName()))
	        {
	          ((Team)heim.get(j)).sieg();
	          ((Team)ausw.get(j)).niederlage();
	          heim.remove(j);
	          ausw.remove(j);
	        }
	      }
	      this.U.add((Team)this.M.remove(this.M.size() - 1));
	      this.minTP -= 1;
	      
	      testR(t1, heim, ausw);
	      offeneSpiele = heim.size();
	      siege = 0;
	      unentschieden = 0;
	      for (int i = 0; i < this.M.size(); i++)
	      {
	        int diff = px - ((Team)this.M.get(i)).getPunkte();
	        while (diff >= 0) {
	          if ((diff - 3 >= 0) || (diff - 2 >= 0))
	          {
	            siege++;
	            diff -= 3;
	          }
	          else
	          {
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
	    this.liga.ermittelPlatzierung(t1, 3, this.team);
	    
	    this.test1 = heim;
	    this.test2 = ausw;
	  }

	  private void testR(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	  {
	    int anzahlSpiele = 0;
	    for (int i = this.O.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.O.get(i)).getName().equals(t[j].getName())) {
	          this.O.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.U.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.U.get(i)).getName().equals(t[j].getName())) {
	          this.U.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--) {
	      for (int j = 0; j < t.length; j++) {
	        if (((Team)this.M.get(i)).getName().equals(t[j].getName())) {
	          this.M.set(i, t[j]);
	        }
	      }
	    }
	    for (int i = this.M.size() - 1; i >= 0; i--)
	    {
	      anzahlSpiele = ausstehendeAnzahlVonSpielen((Team)this.M.get(i), l1, l2);
	      if (((Team)this.M.get(i)).getPunkte() >= this.maxPZ)
	      {
	        if (anzahlSpiele > 0)
	        {
	          for (int j = l1.size() - 1; j >= 0; j--) {
	            if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              ((Team)this.M.get(i)).niederlage();
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              l1.remove(j);
	              l2.remove(j);
	            }
	            else if (((Team)l2.get(j)).getName().equals(((Team)this.M.get(i)).getName()))
	            {
	              for (int k = 0; k < this.M.size(); k++) {
	                if (((Team)l1.get(j)).getName().equals(((Team)this.M.get(k)).getName())) {
	                  ((Team)this.M.get(k)).sieg();
	                }
	              }
	              ((Team)this.M.get(i)).niederlage();
	              l1.remove(j);
	              l2.remove(j);
	            }
	          }
	          this.O.add((Team)this.M.remove(i));
	          this.maxTP += 1;
	          erzeugeMengenTabelleMIN(t, l1, l2);
	          break;
	        }
	        this.O.add((Team)this.M.remove(i));
	        this.maxTP += 1;
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
	  private void backtracking(int aktuelleMoeglichkeit, int tiefe, ArrayList<Team> ausstehendeSpieleHeim, ArrayList<Team> ausstehendeSpieleAusw, Team[] vorherigeTabelle)
	  {
	    this.hallo += 1;
	    
	    Team[] tmpVorherigeTabelle = new Team[vorherigeTabelle.length];
	    for (int i = 0; i < vorherigeTabelle.length; i++) {
	      tmpVorherigeTabelle[i] = new Team(vorherigeTabelle[i]);
	    }
	    ArrayList<Team> tmpAusstehendeSpieleHeim = new ArrayList();
	    for (int i = 0; i < ausstehendeSpieleHeim.size(); i++) {
	      for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
	        if (((Team)ausstehendeSpieleHeim.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
	          tmpAusstehendeSpieleHeim.add(tmpVorherigeTabelle[j]);
	        }
	      }
	    }
	    ArrayList<Team> tmpAusstehendeSpieleAusw = new ArrayList();
	    for (int i = 0; i < ausstehendeSpieleAusw.size(); i++) {
	      for (int j = 0; j < tmpVorherigeTabelle.length; j++) {
	        if (((Team)ausstehendeSpieleAusw.get(i)).getName().equals(tmpVorherigeTabelle[j].getName())) {
	          tmpAusstehendeSpieleAusw.add(tmpVorherigeTabelle[j]);
	        }
	      }
	    }
	    if (aktuelleMoeglichkeit == 1)
	    {
	      ((Team)tmpAusstehendeSpieleHeim.get(0)).sieg();
	      ((Team)tmpAusstehendeSpieleAusw.get(0)).niederlage();
	    }
	    if (aktuelleMoeglichkeit == 2)
	    {
	      ((Team)tmpAusstehendeSpieleHeim.get(0)).unentschieden();
	      ((Team)tmpAusstehendeSpieleAusw.get(0)).unentschieden();
	    }
	    if (aktuelleMoeglichkeit == 3)
	    {
	      ((Team)tmpAusstehendeSpieleHeim.get(0)).niederlage();
	      ((Team)tmpAusstehendeSpieleAusw.get(0)).sieg();
	    }
	    tmpAusstehendeSpieleHeim.remove(0);
	    tmpAusstehendeSpieleAusw.remove(0);
	    if (this.MAX)
	    {
	      erzeugeMengenTabelleMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	      
	      erzeugeMengenMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	    }
	    else
	    {
	      int anzahlU = this.U.size();
	      erzeugeMengenTabelleMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	      if (anzahlU < this.U.size())
	      {
	        int NtmpMinTP = verbessereSchrankeMIN(tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw, tmpVorherigeTabelle);
	        this.S = (this.minTP - NtmpMinTP);
	      }
	      erzeugeMengenMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	    }
	    int merkeMaxTP = this.maxTP;
	    int merkeMinTP = this.minTP;
	    if (this.maxTP == this.minTP)
	    {
	      if (this.MAX)
	      {
	        if (this.maxTP < this.max) {
	          this.max = this.maxTP;
	        }
	      }
	      else
	      {
	        if (this.maxTP > this.min) {
	          this.min = this.maxTP;
	        }
	        if (this.tmpMinTP == this.min)
	        {
	          this.abbruch = true;
	          System.out.println("kompletter abbruch..tmpMinTP==min");
	          return;
	        }
	      }
	      return;
	    }
	    if (this.MAX)
	    {
	      if (this.maxTP < this.max) {}
	    }
	    else
	    {
	      if (this.minTP - this.S <= this.min) {
	        return;
	      }
	      if (this.minTP <= this.min)
	      {
	        System.out.println("abbruch 1: " + this.min);
	        
	        return;
	      }
	    }
	    this.S = 0;
	    for (int i = 1; i <= 3; i++) {
	      if (tmpAusstehendeSpieleHeim.size() > 0)
	      {
	        if (this.abbruch) {
	          return;
	        }
	        backtracking(i, tiefe + 1, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw, tmpVorherigeTabelle);
	        this.maxTP = merkeMaxTP;
	        this.minTP = merkeMinTP;
	        if (this.MAX) {
	          erzeugeMengenMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	        } else {
	          erzeugeMengenMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
	        }
	      }
	      else
	      {
	        System.out.println("ERROR ERROR ERROR ERROR ERROR");
	      }
	    }
	  }

	  private int ausstehendeAnzahlVonSpielen(Team t, ArrayList<Team> l1, ArrayList<Team> l2)
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

	/**
	 * 
	 * @return gibt die Dauer in ms zurück die der Algorithmus gebraucht hat
	 */
	public long getDauer() {
		return this.dauer;
	}

	private void ausgabeWerte() {
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

	  private void ausgabeTabelle(Team[] t0)
	  {
	    ComparatorChain.t = this.team;
	    if (this.MAX) {
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

	private void ermittel(int altesMax, int alteSchranke, int neuesMax, int neueSchranke) {
		if (MAX) {
			switch (altesMax - neuesMax) {
			case 1:
				uMax1++;
				break;
			case 2:
				uMax2++;
				break;
			case 3:
				uMax3++;
				break;
			case 4:
				uMax4++;
				break;
			case 5:
				uMax5++;
				break;
			case 6:
				uMax6++;
				break;
			case 7:
				uMax7++;
				break;
			case 8:
				uMax8++;
				break;
			case 9:
				uMax9++;
				break;
			case 10:
				uMax10++;
				break;
			case 11:
				uMax11++;
				break;
			case 12:
				uMax12++;
				break;
			case 13:
				uMax13++;
				break;
			case 14:
				uMax14++;
				break;
			case 15:
				uMax15++;
				break;
			case 16:
				uMax16++;
				break;
			case 17:
				uMax17++;
				break;
			case 18:
				uMax18++;
				break;
			}

			switch (neueSchranke - alteSchranke) {
			case 1:
				uSMax1++;
				break;
			case 2:
				uSMax2++;
				break;
			case 3:
				uSMax3++;
				break;
			case 4:
				uSMax4++;
				break;
			case 5:
				uSMax5++;
				break;
			case 6:
				uSMax6++;
				break;
			case 7:
				uSMax7++;
				break;
			case 8:
				uSMax8++;
				break;
			case 9:
				uSMax9++;
				break;
			case 10:
				uSMax10++;
				break;
			case 11:
				uSMax11++;
				break;
			case 12:
				uSMax12++;
				break;
			}
		} else {
			switch (neuesMax - altesMax) {
			case 1:
				uMin1++;
				break;
			case 2:
				uMin2++;
				break;
			case 3:
				uMin3++;
				break;
			case 4:
				uMin4++;
				break;
			case 5:
				uMin5++;
				break;
			case 6:
				uMin6++;
				break;
			case 7:
				uMin7++;
				break;
			case 8:
				uMin8++;
				break;
			case 9:
				uMin9++;
				break;
			case 10:
				uMin10++;
				break;
			case 11:
				uMin11++;
				break;
			case 12:
				uMin12++;
				break;
			case 13:
				uMin13++;
				break;
			case 14:
				uMin14++;
				break;
			case 15:
				uMin15++;
				break;
			case 16:
				uMin16++;
				break;
			case 17:
				uMin17++;
				break;
			case 18:
				uMin18++;
				break;
			}

			switch (alteSchranke - neueSchranke) {
			case 1:
				uSMin1++;
				break;
			case 2:
				uSMin2++;
				break;
			case 3:
				uSMin3++;
				break;
			case 4:
				uSMin4++;
				break;
			case 5:
				uSMin5++;
				break;
			case 6:
				uSMin6++;
				break;
			case 7:
				uSMin7++;
				break;
			case 8:
				uSMin8++;
				break;
			case 9:
				uSMin9++;
				break;
			case 10:
				uSMin10++;
				break;
			case 11:
				uSMin11++;
				break;
			case 12:
				uSMin12++;
				break;
			}
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
				tipps.add(-1);
				tipps.add(-1);
			} else {
				if (t[i] == t[i + 1]) {
					tipps.add(2);
					tipps.add(2);
				}
			}
			if (t[i] > t[i + 1]) {
				tipps.add(1);
				tipps.add(3);
			}
			if (t[i] < t[i + 1]) {
				tipps.add(3);
				tipps.add(1);
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

	/**
	 * 
	 * @return gibt die Liste aller benötigten Zeiten zurück
	 */
	public Long[] getDauerListe() {
		return this.dauerListe;
	}

	/**
	 * @param beobachter
	 *            fügt den Beobachter in die beobachterListe hinzu
	 */
	@Override
	public void addBeobachter(Beobachter beobachter) {
		this.beobachterListe.add(beobachter);

	}

	/**
	 * 
	 * Benachrichtigt alle Beobachter dass sich diese aktualisieren sollen
	 */
	@Override
	public void notifyAlleBeobachter(Team team) {
		for (Beobachter b : beobachterListe) {
			b.update(team);
		}
	}

	@Override
	public void removeBeobachter(Beobachter beobachter) {
		beobachterListe.remove(beobachter);
	}

	public Task<Long> getTask() {
		return this.task;
	}

}
