package Fachkonzept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.sound.midi.Synthesizer;

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
	public static int auswahlMax=0,auswahlMin=0;
	private Koordinator k;
	private int maxPZ,minPZ;
	private int maxTP,minTP;
	private int max,min;
	private int tmpMinTP;
	private int S=0;
	private boolean abbruch=false;
	private ArrayList<Team> ausstehendeNamenHeim;
	private ArrayList<Team> ausstehendeNamenAusw;
	private ArrayList<Team> O,M,U;
	private ArrayList<Team> merke;
	private int hallo=0;
	private ArrayList<Integer> merkeSpielausgang;
	private ArrayList<Team> merkeGegner;
	private ArrayList<Integer> xxx;
	private ArrayList<Team> nachbesserung;
	private ArrayList<Team> test1;
	private ArrayList<Team> test2;
	private Team[] test3;
	private int uMax1=0;
	private int uMax2=0;
	private int uMax3=0;
	private int uMax4=0;
	private int uMax5=0;
	private int uMax6=0;
	private int uMax7=0;
	private int uMax8=0;
	private int uMax9=0;
	private int uMax10=0;
	private int uMax11=0;
	private int uMax12=0;
	private int uMax13=0;
	private int uMax14=0;
	private int uMax15=0;
	private int uMax16=0;
	private int uMax17=0;
	private int uMax18=0;
	
	private int uSMax1=0;
	private int uSMax2=0;
	private int uSMax3=0;
	private int uSMax4=0;
	private int uSMax5=0;
	private int uSMax6=0;
	private int uSMax7=0;
	private int uSMax8=0;
	private int uSMax9=0;
	private int uSMax10=0;
	private int uSMax11=0;
	private int uSMax12=0;
	
	private int uBMax=0;
	private int uCMax=0;
	private int uDMax=0;
	private int uEMax=0;
	private int uFMax=0;
	private int uGMax=0;
	
	private int uMin1=0;
	private int uMin2=0;
	private int uMin3=0;
	private int uMin4=0;
	private int uMin5=0;
	private int uMin6=0;
	private int uMin7=0;
	private int uMin8=0;
	private int uMin9=0;
	private int uMin10=0;
	private int uMin11=0;
	private int uMin12=0;
	private int uMin13=0;
	private int uMin14=0;
	private int uMin15=0;
	private int uMin16=0;
	private int uMin17=0;
	private int uMin18=0;
	
	private int uSMin1=0;
	private int uSMin2=0;
	private int uSMin3=0;
	private int uSMin4=0;
	private int uSMin5=0;
	private int uSMin6=0;
	private int uSMin7=0;
	private int uSMin8=0;
	private int uSMin9=0;
	private int uSMin10=0;
	private int uSMin11=0;
	private int uSMin12=0;
	
	private int uBMin=0;
	private int uCMin=0;
	private int uDMin=0;
	private int uEMin=0;
	private int uFMin=0;
	private int uGMin=0;
	
	private int abc=0;
	private int abcdef=0;
	private int abcdefghi=0;
	private int rek=0;
	private int nMin=0;
	private int abc2=0;
	private int abcdef2=0;
	private int rek2=0;
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
				int kk=0;
				int a=0;
				for (int tag = anzahlUebrigerSpieltage - 1; tag >= 0; tag--) {

					System.out.println("----------tag: "+(34-kk)+"-----------------");
					kk++;

						if(auswahlMax==0)
						{
							//updateProgress(0, 1);
							berechneMAX(tag);				
						}else{
							if(auswahlMax==1)
							{
								berechneMAX_MIN(tag);
								for(int i=0; i<xxx.size();i++)
								{
									tipps.set(xxx.get(i), -1);
								}
							}
						}					
						updateProgress(++a, anzahlUebrigerSpieltage*2);
						if(auswahlMin==0)
						{
							//updateProgress(7, 8);
							berechneMIN(tag);
							abbruch=false;
							
						}else{
							if(auswahlMin==1)
							{
								berechneMIN_MAX(tag);
								for(int i=0; i<xxx.size();i++)
								{
									tipps.set(xxx.get(i), -1);
								}
							}
						}
					
					
					
					durchlauf++;
					updateProgress(++a, anzahlUebrigerSpieltage*2);
				}
				System.out.println("Anzahl: "+hallo);
				liga.ermittelPlatzierung(liga.getTeams(),Liga.SORTIERUNG_NORMAL,null);
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
				dauerListe[t.getPlatzierung() - 1] = dauer;//Die dauer wird in die Liste hinzugefügt an der Stelle wo sich das Team auch befindet (-1 da platzierung nicht bei 0 anfängt)
				if (!beobachterListe.isEmpty()) {
					notifyAlleBeobachter(team);
				}
				bar.progressProperty().unbind();
				bar.setProgress(0.0);
				
				inBearbeitung = false;
				if (!warteListe.isEmpty()) {
					starteAlgorithmen(warteListe.removeLast());
				}else{
					ausgabeErmittelt();
				}

			}
		});
		
		task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				task=null;
				berechnungen=null;
				bar.progressProperty().unbind();
				bar.setProgress(0.0);
				warteListe.clear();
				inBearbeitung=false;
				
				dauerListe[t.getPlatzierung() - 1] = (long) -1;//Die dauer wird in die Liste hinzugefügt an der Stelle wo sich das Team auch befindet (-1 da platzierung nicht bei 0 anfängt)
				
				for(int i=anzahlUebrigerSpieltage-durchlauf;i>=0;i--)
				{
					t.setMaxPlatzSpieltag(-100);
					t.setMinPlatzSpieltag(-100);
				}
				for (Beobachter b : beobachterListe) {
					b.updateCancelled(t);
				}
			}
		});
		
		
		
		//Wenn noch keine Berechnung durchgeführt wird, dann..
		if (!inBearbeitung) {
			inBearbeitung = true;
			for (Beobachter b : beobachterListe) {
				b.updateAnfang(t);
			}
			//.. Ein Thread wird erstellt der den Task abarbeitet
			// dieser wird dann gestartet
			berechnungen = new Thread(task);
			berechnungen.start();
		} else {
			//ansonsten wird das Team in die WarteListe aufgenommen
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
		MAX=true;
		liga.ermittelPlatzierung(k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MAX,team);
		max=team.getPlatzierung();
		maxPZ=team.getPunkte()+(Zaehlweise.PUNKTE_S*(tag+1));
		minPZ=team.getPunkte();
		
		
		pruefeSchrankeMAX();
		erzeugeInitialeMengen(k.getAktiveLiga().getTeams());
		//ausgabeMengen();
		//ausgabeMoeglichkeiten();
		Team[] tmpTabelle=erstelleInitialeTabelle();
		//ausgabeTabelle(tmpTabelle);
		
		erstelleWerteMAX(k.getAktiveLiga().getTeams(),tag+1);
		int aaMax=max;
		int aaMaxTP=maxTP;
		//ausgabeWerte();


		ArrayList<Team> tmpAusstehendeNamenHeim=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenHeim.size();i++)
		{
			tmpAusstehendeNamenHeim.add(ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenAusw.size();i++)
		{
			tmpAusstehendeNamenAusw.add(ausstehendeNamenAusw.get(i));
		}

		if(maxTP==minTP)
		{
			abc2++;
			team.setMaxPlatzSpieltag(maxTP);
			return;
		}
		
		//ausgabeAusstehendeTeams(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		erzeugeMengenTabelleMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		erzeugeMengenMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//ausgabeWerte();
		//ausgabeTabelle(tmpTabelle);
		//System.out.println("max: "+max);
		if(max==maxTP)
		{
			//System.out.println("Abbruch bei max ist richtig");
			//TODO ein ermittel vergessen
			abcdef2++;
			ermittel(0, aaMaxTP, 0, max);
			team.setMaxPlatzSpieltag(max);
			return;
		}
		//ausgabeTabelle(tmpTabelle);
		int tmpMaxTP=verbessereSchrankeMAX(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if(tmpMaxTP==max)
		{
			abcdef2++;
			//System.out.println("Abbruch bei verbesserten max ist richtig");
			//ermittel(aaMax, aaMaxTP, max, aaMaxTP);
			ermittel(0, aaMaxTP, 0, max);
			team.setMaxPlatzSpieltag(max);
			return;
		}
		//System.out.println("verbesserte Schranke: "+tmpMaxTP);
		erzeugeMengenMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		
		
		//ermittel(aaMax, aaMaxTP, max, tmpMaxTP);
		


		if(tmpMaxTP==max)
		{
			abcdef2++;
			ermittel(0, aaMaxTP, 0, max);
			//System.out.println("Abbruch bei verbesserten max und nachbesserung ist richtig");
			team.setMaxPlatzSpieltag(max);
			return;
		}
		//ausgabeMengen();
		//ausgabeWerte();
		if(maxTP==minTP)
		{
			if(maxTP<max)
			{
				max=maxTP;
			}	
		}else{
			if(maxTP>=max)
			{
				//System.out.println("init.... maxTP>max kein Branchen mehr");
			}else{
				System.out.println("Starten der Rekursion MAX");
				erzeugeMengenMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
				int merkeMaxTP=maxTP;
				int merkeMinTP=minTP;
				backtracking(1,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(2,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(3,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
			}
		}
		rek2++;
		ermittel(0, aaMaxTP, 0, max);
		//System.out.println("setzten des max wertes: "+max);
		team.setMaxPlatzSpieltag(max);
	}
	


	private void berechneMIN(int tag)
	{
		MAX=false;
		liga.ermittelPlatzierung(k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MIN,team);
		min=team.getPlatzierung();
		maxPZ=team.getPunkte();
		minPZ=team.getPunkte()-(Zaehlweise.PUNKTE_S*(tag+1));


		//ausgabeTabelle(k.getAktiveLiga().getTeams());
		pruefeSchrankeMIN();
		erzeugeInitialeMengen(k.getAktiveLiga().getTeams());
		//ausgabeMengen();
		//ausgabeMoeglichkeiten();
		Team[] tmpTabelle=erstelleInitialeTabelle();
		//ausgabeTabelle(tmpTabelle);
		
		erstelleWerteMIN(k.getAktiveLiga().getTeams(),tag+1);
		int aaMin=min;
		int aaMinTP=minTP;
		//ausgabeWerte();
		
		
		ArrayList<Team> tmpAusstehendeNamenHeim=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenHeim.size();i++)
		{
			tmpAusstehendeNamenHeim.add(ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenAusw.size();i++)
		{
			tmpAusstehendeNamenAusw.add(ausstehendeNamenAusw.get(i));
		}

		if(maxTP==minTP)
		{
			//System.out.println("Abbruch bei min ist vllt richtig");
			abc++;
			team.setMinPlatzSpieltag(minTP);
			return;
		}
		erzeugeMengenTabelleMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		//erzeugeMengenMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		//ausgabeTabelle(tmpTabelle);
		//ausgabeAusstehendeTeams(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//System.out.println("vor Test min="+min);
		test(tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
		tmpAusstehendeNamenHeim=test1;
		tmpAusstehendeNamenAusw=test2;
		//System.out.println("übrig bleiben: "+tmpAusstehendeNamenHeim.size());
		//System.out.println("Nach test min="+min);
		erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//System.out.println("nach init: "+min);
		

		
		//ausgabeMengen();
		//ausgabeTabelle(tmpTabelle);
		//System.out.println("min: "+min);
		
		if(min==minTP)
		{
			abcdef++;
			//ermittel(aaMin, aaMinTP, min, aaMinTP);
			ermittel(0, aaMinTP, 0, min);
			team.setMinPlatzSpieltag(min);
			//System.out.println("Abbruch bei min ist richtig");
			return;
		}
		//ausgabeMengen();
		//ausgabeWerte();
		
		tmpMinTP=verbessereSchrankeMIN(tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
		if(tmpMinTP==min)
		{
			abcdef++;
			//ermittel(aaMin, aaMinTP, min, tmpMinTP);
			ermittel(0, aaMinTP, 0, min);
			team.setMinPlatzSpieltag(min);
			return;
		}
		//System.out.println("verbesserte Schranke: "+tmpMinTP);
		//System.out.println("test beginnt: ");
		int altMin=min;
		int altMinTP=minTP;
		int altMaxTP=maxTP;
		int altMinPZ=minPZ;
		int altMaxPZ=maxPZ;
		erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(k.getAktiveLiga().getTeams(),tag);
		//System.out.println("Nachbesserung liefer: "+min);
		//ermittel(aaMin, aaMinTP, min, tmpMinTP);
		if(altMin<min)nMin++;
		if(nachbesserung!=null){
			nachbesserung.clear();
		}
		
		minTP=altMinTP;
		maxTP=altMaxTP;
		minPZ=altMinPZ;
		maxPZ=altMaxPZ;

		//System.out.println("test beendet");
		min=Math.max(altMin,min);
		if(tmpMinTP==min)
		{
			abcdefghi++;
			ermittel(0, aaMinTP, 0, min);
			//System.out.println("Abbruch bei verbesserten min ist richtig");
			team.setMinPlatzSpieltag(min);
			return;
		}
		S=minTP-tmpMinTP;
		//System.out.println("S beträgt vor der Rekursion: "+S+"    tmpMinTP: "+tmpMinTP+"   -  minTP: "+minTP);
		//System.out.println("vorgauckeln min =15 mit "+tmpAusstehendeNamenHeim.size());
		//min=15;
		//System.out.println("Anzahl ausstehnde Teams: "+tmpAusstehendeNamenHeim.size());
		erzeugeMengenMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		//ausgabeAusstehendeTeams(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//ausgabeMengen();

		int altMinalt=min;
		if(maxTP==minTP)
		{
			if(maxTP>min)
			{
				min=maxTP;
			}	
		}else{
			if(minTP<=min)
			{
				//System.out.println("init.... minTP<min kein Branchen mehr");
			}else{
				
				System.out.println("Starte mit der Rekursion in MIN");
				erzeugeMengenMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
				int merkeMaxTP=maxTP;
				int merkeMinTP=minTP;
				backtracking(1,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(2,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(3,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
			}
		}
		System.out.println("altes Min: "+altMinalt+"  neues Min: "+min);
		rek++;
		//System.out.println("Der Wert "+min+" wird gesetzt");
		ermittel(0, aaMinTP, 0, min);
		team.setMinPlatzSpieltag(min);
	}
	
	private void berechneMAX_MIN(int tag)
	{
		xxx=new ArrayList<Integer>();
		//System.out.println("Berechne Max_Min");
		if(tipps.isEmpty())
		{
			int j = 0;
			for(int i=0;i<liga.getAnzahlAusstehend()/2;i=i+2)
			{
				if (i % anzahlTeams == 0 && i != 0) {
					j++;
				}
				
				if(ausstehendCpy[j][i % anzahlTeams].getName().equals(team.getName())){
					tipps.add(3);
					tipps.add(1);
				}else{
					if(ausstehendCpy[j][(i % anzahlTeams)+1].getName().equals(team.getName())){
						tipps.add(1);
						tipps.add(3);
					}else{
						tipps.add(-1);
						tipps.add(-1);
					}
				}				
			}
		}else{
			for (int x = 0; x < anzahlUebrigerSpieltage - durchlauf; x++) {
				for (int i = 0; i < ausstehendCpy[x].length; i = i + 2) {
					if (ausstehendCpy[x][i].getName().equals(team.getName())&&tipps.get(i + (x * anzahlTeams)) == -1) {
						tipps.set(i + (x * anzahlTeams), 3);
						tipps.set(i + 1 + (x * anzahlTeams), 1);
						xxx.add(i + (x * anzahlTeams));
						xxx.add(i + 1 + (x * anzahlTeams));
					}else{
						if (ausstehendCpy[x][i+1].getName().equals(team.getName())&&tipps.get(i +1 + (x * anzahlTeams)) == -1) {
							tipps.set(i + (x * anzahlTeams), 1);
							tipps.set(i + 1 + (x * anzahlTeams), 3);
							xxx.add(i + (x * anzahlTeams));
							xxx.add(i + 1 + (x * anzahlTeams));
						}
					}
				}
			}
		}
		//ausgabeTipps();
		
		
		
		MAX=true;
		liga.ermittelPlatzierung(k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MAX,team);
		max=team.getPlatzierung();
		maxPZ=team.getPunkte()+(Zaehlweise.PUNKTE_S*(tag+1));
		minPZ=team.getPunkte();
		
		
		pruefeSchrankeMAX();
		erzeugeInitialeMengen(k.getAktiveLiga().getTeams());
		//ausgabeMengen();
		//ausgabeMoeglichkeiten();
		Team[] tmpTabelle=erstelleInitialeTabelle();
		//ausgabeTabelle(tmpTabelle);
		
		erstelleWerteMAX_MIN(k.getAktiveLiga().getTeams(),tag+1);
		//ausgabeWerte();


		ArrayList<Team> tmpAusstehendeNamenHeim=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenHeim.size();i++)
		{
			tmpAusstehendeNamenHeim.add(ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenAusw.size();i++)
		{
			tmpAusstehendeNamenAusw.add(ausstehendeNamenAusw.get(i));
		}
		if(maxTP==minTP)
		{
			team.setMaxPlatzSpieltag(maxTP);
			return;
		}
		
		//ausgabeAusstehendeTeams(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		erzeugeMengenTabelleMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//ausgabeWerte();
		//System.out.println("max: "+max);
		if(max==maxTP)
		{
			//System.out.println("Abbruch bei max ist richtig");
			team.setMaxPlatzSpieltag(max);
			return;
		}

		int tmpMaxTP=verbessereSchrankeMAX(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		if(tmpMaxTP==max)
		{
			team.setMaxPlatzSpieltag(max);
			return;
		}
		
		//ausgabeMengen();
		//ausgabeWerte();
		if(maxTP==minTP)
		{
			if(maxTP<max)
			{
				max=maxTP;
			}	
		}else{
			if(maxTP>=max)
			{
				System.out.println("init.... maxTP>max kein Branchen mehr");
			}else{
				erzeugeMengenMAX(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
				int merkeMaxTP=maxTP;
				int merkeMinTP=minTP;
				backtracking(1,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(2,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMAX(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(3,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
			}
		}

		team.setMaxPlatzSpieltag(max);
	}
	
	private void berechneMIN_MAX(int tag)
	{
		xxx=new ArrayList<Integer>();
		//System.out.println("BerechneMin_MAX");
		if(tipps.isEmpty())
		{
			int j = 0;
			for(int i=0;i<liga.getAnzahlAusstehend()/2;i=i+2)
			{
				if (i % anzahlTeams == 0 && i != 0) {
					j++;
				}
				
				if(ausstehendCpy[j][i % anzahlTeams].getName().equals(team.getName())){
					tipps.add(1);
					tipps.add(3);
				}else{
					if(ausstehendCpy[j][(i % anzahlTeams)+1].getName().equals(team.getName())){
						tipps.add(3);
						tipps.add(1);
					}else{
						tipps.add(-1);
						tipps.add(-1);
					}
				}				
			}
		}else{
			for (int x = 0; x < anzahlUebrigerSpieltage - durchlauf; x++) {
				for (int i = 0; i < ausstehendCpy[x].length; i = i + 2) {
					if (ausstehendCpy[x][i].getName().equals(team.getName())&&tipps.get(i + (x * anzahlTeams)) == -1) {
						tipps.set(i + (x * anzahlTeams), 1);
						tipps.set(i + 1 + (x * anzahlTeams),3);
						xxx.add(i + (x * anzahlTeams));
						xxx.add(i + 1 + (x * anzahlTeams));
					}else{
						if (ausstehendCpy[x][i+1].getName().equals(team.getName())&&tipps.get(i +1 + (x * anzahlTeams)) == -1) {
							tipps.set(i + (x * anzahlTeams), 3);
							tipps.set(i + 1 + (x * anzahlTeams), 1);
							xxx.add(i + (x * anzahlTeams));
							xxx.add(i + 1 + (x * anzahlTeams));
						}
					}
				}
			}
		}
		//ausgabeTipps();
		
		MAX=false;
		liga.ermittelPlatzierung(k.getAktiveLiga().getTeams(), Liga.SORTIERUNG_MIN,team);
		min=team.getPlatzierung();
		maxPZ=team.getPunkte();
		minPZ=team.getPunkte()-(Zaehlweise.PUNKTE_S*(tag+1));

		pruefeSchrankeMIN();
		erzeugeInitialeMengen(k.getAktiveLiga().getTeams());
		//ausgabeMengen();
		//ausgabeMoeglichkeiten();
		Team[] tmpTabelle=erstelleInitialeTabelle();
		//ausgabeTabelle(tmpTabelle);
		
		erstelleWerteMIN_MAX(k.getAktiveLiga().getTeams(),tag+1);
		//ausgabeWerte();
		
		
		ArrayList<Team> tmpAusstehendeNamenHeim=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenHeim.size();i++)
		{
			tmpAusstehendeNamenHeim.add(ausstehendeNamenHeim.get(i));
		}
		ArrayList<Team> tmpAusstehendeNamenAusw=new ArrayList<Team>();
		for(int i=0;i<ausstehendeNamenAusw.size();i++)
		{
			tmpAusstehendeNamenAusw.add(ausstehendeNamenAusw.get(i));
		}


		if(maxTP==minTP)
		{
			//System.out.println("Abbruch bei min ist vllt richtig");
			team.setMinPlatzSpieltag(minTP);
			return;
		}
		
		erzeugeMengenTabelleMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		test(tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
		tmpAusstehendeNamenHeim=test1;
		tmpAusstehendeNamenAusw=test2;
		erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//System.out.println("nach init: ");
		if(min==minTP)
		{
			//ermittel(aaMin, aaMinTP, min, aaMinTP);
			team.setMinPlatzSpieltag(min);
			//System.out.println("Abbruch bei min ist richtig");
			return;
		}
		tmpMinTP=verbessereSchrankeMIN(tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
		if(tmpMinTP==min)
		{
			//ermittel(aaMin, aaMinTP, min, tmpMinTP);
			team.setMinPlatzSpieltag(min);
			return;
		}
		//System.out.println("verbesserte Schranke: "+tmpMinTP);
		//System.out.println("test beginnt: ");
		int altMin=min;
		int altMinTP=minTP;
		int altMaxTP=maxTP;
		int altMinPZ=minPZ;
		int altMaxPZ=maxPZ;
		erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(k.getAktiveLiga().getTeams(),tag);
		//System.out.println("Nachbesserung liefer: "+min);
		//ermittel(aaMin, aaMinTP, min, tmpMinTP);
		
		if(altMin<min)nMin++;
		nachbesserung.clear();
		minTP=altMinTP;
		maxTP=altMaxTP;
		minPZ=altMinPZ;
		maxPZ=altMaxPZ;

		//System.out.println("test beendet");
		min=Math.max(altMin,min);
		
		if(tmpMinTP==min)
		{
			//System.out.println("Abbruch bei verbesserten min ist richtig");
			team.setMinPlatzSpieltag(min);
			return;
		}
		S=minTP-tmpMinTP;
		//System.out.println("S beträgt vor der Rekursion: "+S+"    tmpMinTP: "+tmpMinTP+"   -  minTP: "+minTP);
		//System.out.println("vorgauckeln min =15 mit "+tmpAusstehendeNamenHeim.size());
		//min=15;
		//System.out.println("Anzahl ausstehnde Teams: "+tmpAusstehendeNamenHeim.size());
		erzeugeMengenMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
		//ausgabeAusstehendeTeams(tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
		//ausgabeMengen();
		
		if(min==minTP)
		{
			team.setMinPlatzSpieltag(min);
			//System.out.println("Abbruch bei min ist richtig");
			return;
		}
		//ausgabeMengen();
		//ausgabeWerte();
		if(maxTP==minTP)
		{
			if(maxTP>min)
			{
				min=maxTP;
			}	
		}else{
			if(minTP<=min)
			{
				System.out.println("init.... minTP<min kein Branchen mehr");
			}else{
				System.out.println("Starte mit der Rekursion in MIN");
				erzeugeMengenMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
				int merkeMaxTP=maxTP;
				int merkeMinTP=minTP;
				backtracking(1,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(2,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				erzeugeMengenMIN(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
				backtracking(3,1,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw,tmpTabelle);
			}
		}
	
		//System.out.println("Der Wert "+min+" wird gesetzt");
		team.setMinPlatzSpieltag(min);
	}

	private void erzeugeInitialeMengen(Team[] tmpTeam)
	{
		O = new ArrayList<Team>();
		M = new ArrayList<Team>();
		U = new ArrayList<Team>();
	
		for(int i=0; i<tmpTeam.length; i++)
		{
			
			if(MAX)
			{
				if(tmpTeam[i].getPunkte()>maxPZ)
				{
					O.add(tmpTeam[i]);
					
				}else{
					if(!tmpTeam[i].getName().equals(team.getName()))
					{
						if(tmpTeam[i].getPunkte()<=minPZ)
						{
							U.add(tmpTeam[i]);	
						}else{
							M.add(tmpTeam[i]);
						}
					}
				}
			}else{
				if(tmpTeam[i].getPunkte()>=maxPZ)
				{
					O.add(tmpTeam[i]);
					
				}else{
					if(!tmpTeam[i].getName().equals(team.getName()))
					{
						if(tmpTeam[i].getPunkte()<minPZ)
						{
							U.add(tmpTeam[i]);	
						}else{
							M.add(tmpTeam[i]);
						}
					}
				}
			}
		}
	}

	

	/**
	 * Prüft die Schranken für das Maximum und setzt dementsprechend die
	 * Möglichkeiten
	 */
	private void pruefeSchrankeMAX() {
	
		unterGrenze=minPZ;
		oberGrenze=maxPZ;
		
		moeglichkeiten = new ArrayList<Integer>();
		ausstehendeNamenHeim = new ArrayList<Team>();
		ausstehendeNamenAusw = new ArrayList<Team>();
		merke= new ArrayList<Team>();
		merkeSpielausgang = new ArrayList<Integer>();
		merkeGegner= new ArrayList<Team>();
		// durchlaufe alle übrigen Spieltage
		for (int x = 0; x < anzahlUebrigerSpieltage - durchlauf; x++) {
			// durchlaufe alle Teams an diesen Spieltag
			// hierbei wird erst auf Heimspiel dann auf Auswärtsspiel geprüft,
			// deswegen wird i am ende um 2 schritte erhöht
			for (int i = 0; i < ausstehendCpy[x].length; i = i + 2) {
				merke.add(ausstehendCpy[x][i]);
				merke.add(ausstehendCpy[x][i+1]);
				// wenn tipps gesetzt wurden, werden diese als erstes betrachtet
				// (-1 bedeutet nicht gesetzt)
				// es wird zuerst auf Heimspiel geprüft
				if (!tipps.isEmpty() && tipps.get(i + (x * anzahlTeams)) != -1) {
					if (ausstehendCpy[x][i].getName().equals(team.getName())) {
						
						if (tipps.get(i + (x * anzahlTeams)) == 2) {
							maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
							minPZ += Zaehlweise.PUNKTE_U;
						}
						
						if (tipps.get(i + (x * anzahlTeams)) == 3) {
							maxPZ -= Zaehlweise.PUNKTE_S;
							minPZ -=  Zaehlweise.PUNKTE_S;
						}
					} else {

						if (ausstehendCpy[x][i + 1].getName().equals(team.getName())) {

							if (tipps.get(i + 1 + (x * anzahlTeams)) == 3) {
								maxPZ -= Zaehlweise.PUNKTE_S;
								minPZ -= Zaehlweise.PUNKTE_S;
							}

							if (tipps.get(i + 1 + (x * anzahlTeams)) == 2) {
								maxPZ -= Zaehlweise.PUNKTE_S - Zaehlweise.PUNKTE_U;
								minPZ += Zaehlweise.PUNKTE_U;
							}
							
						}
					}
					unterGrenze=minPZ;
					oberGrenze=maxPZ;
					moeglichkeiten.add(tipps.get(i + (x * anzahlTeams)));
					moeglichkeiten.add(tipps.get(i + 1 + (x * anzahlTeams)));
				} else {
					// wenn nicht getippt wurde dann..
					// Wenn das das ausgewählte team ist soll natürlich gewinnen
					// (Heimspiel)
					if (ausstehendCpy[x][i].getName().equals(team.getName())) {
						moeglichkeiten.add(1);
						moeglichkeiten.add(3);
					} else {
						// Wenn das das ausgewählte team ist soll natürlich
						// gewinnen (Auswärtsspiel)
						if (ausstehendCpy[x][i + 1].getName().equals(team.getName())) {
							moeglichkeiten.add(3);
							moeglichkeiten.add(1);
						} else {
							// Wenn es die obergrenze überschreitet oder die
							// untergrenze unterschreitet
							// lasse es immer gewinnen (Heimspiel)
							if (ausstehendCpy[x][i].getPunkte() > oberGrenze
									|| ausstehendCpy[x][i].getPunkte() <= unterGrenze) {
								moeglichkeiten.add(1);
								moeglichkeiten.add(3);
							} else {
								// Wenn es die obergrenze überschreitet oder die
								// untergrenze unterschreitet
								// lasse es immer gewinnen (Auswärtsspiel)
								if (ausstehendCpy[x][i + 1].getPunkte() > oberGrenze
										|| ausstehendCpy[x][i + 1].getPunkte() <= unterGrenze) {
									moeglichkeiten.add(3);
									moeglichkeiten.add(1);
								} else {
									
									ausstehendeNamenHeim.add(ausstehendCpy[x][i]);
									ausstehendeNamenAusw.add(ausstehendCpy[x][i+1]);
									moeglichkeiten.add(-1);
									moeglichkeiten.add(-1);		
								}
							}
						}
					}
				}
			}
		}
	}

	private void pruefeSchrankeMIN() {
		unterGrenze=minPZ;
		oberGrenze=maxPZ;
		
		moeglichkeiten = new ArrayList<Integer>();
		ausstehendeNamenHeim = new ArrayList<Team>();
		ausstehendeNamenAusw = new ArrayList<Team>();
	
		// durchlaufe alle übrigen Spieltage
		for (int x = 0; x < anzahlUebrigerSpieltage - durchlauf; x++) {
			// durchlaufe alle Teams an diesen Spieltag
			// hierbei wird erst auf Heimspiel dann auf Auswärtsspiel geprüft,
			// deswegen wird i am ende um 2 schritte erhöht
			for (int i = 0; i < ausstehendCpy[x].length; i = i + 2) {
				// wenn tipps gesetzt wurden, werden diese als erstes betrachtet
				// (-1 bedeutet nicht gesetzt)
				// es wird zuerst auf Heimspiel geprüft
				if (!tipps.isEmpty() && tipps.get(i + (x * anzahlTeams)) != -1) {
					if (ausstehendCpy[x][i].getName().equals(team.getName())) {
						// wurde gewonnen getippt? dann verändere die obere und
						// untere Grenze denn das ausgewählte Team kann ja nun
						// nicht mehr die minimale anzahl von Punkten erreichen
						if (tipps.get(i + (x * anzahlTeams)) == 1) {
							minPZ += Zaehlweise.PUNKTE_S;
							maxPZ += Zaehlweise.PUNKTE_S;
						}
						// wurde unentschieden getippt? dann verändere die obere
						// und untere Grenze denn das ausgewählte Team kann ja
						// nun
						// nicht mehr die minimale anzahl von Punkten erreichen
						if (tipps.get(i + (x * anzahlTeams)) == 2) {
							minPZ += Zaehlweise.PUNKTE_U;
							maxPZ += Zaehlweise.PUNKTE_U ;
						}
					} else {
						if (ausstehendCpy[x][i + 1].getName().equals(team.getName())) {
							// wurde gewonnen getippt? dann verändere die obere
							// und untere Grenze denn das ausgewählte Team kann
							// ja nun
							// nicht mehr die minimale anzahl von Punkten
							// erreichen
							if (tipps.get(i + 1 + (x * anzahlTeams)) == 1) {
								minPZ += Zaehlweise.PUNKTE_S;
								maxPZ += Zaehlweise.PUNKTE_S;
							}
							// wurde unentschieden getippt? dann verändere die
							// obere und untere Grenze denn das ausgewählte Team
							// kann ja nun
							// nicht mehr die minimale anzahl von Punkten
							// erreichen
							if (tipps.get(i + 1 + (x * anzahlTeams)) == 2) {
								minPZ += Zaehlweise.PUNKTE_U;
								maxPZ += Zaehlweise.PUNKTE_U ;
							}
						}
					}
					unterGrenze=minPZ;
					oberGrenze=maxPZ;
					moeglichkeiten.add(tipps.get(i + (x * anzahlTeams)));
					moeglichkeiten.add(tipps.get(i + 1 + (x * anzahlTeams)));
				} else {
	
					// wenn nicht getippt wurde dann..
					// Wenn das das ausgewählte team ist soll natürlich gewinnen
					// (Heimspiel)
					if (ausstehendCpy[x][i].getName().equals(team.getName())) {
						moeglichkeiten.add(3);
						moeglichkeiten.add(1);
					} else {
						if (ausstehendCpy[x][i + 1].getName().equals(team.getName())) {
							moeglichkeiten.add(1);
							moeglichkeiten.add(3);
						} else {
							if (ausstehendCpy[x][i].getPunkte() >= oberGrenze
									|| ausstehendCpy[x][i].getPunkte() < unterGrenze) {
								moeglichkeiten.add(3);
								moeglichkeiten.add(1);
							} else {
								if (ausstehendCpy[x][i + 1].getPunkte() >= oberGrenze
										|| ausstehendCpy[x][i + 1].getPunkte() < unterGrenze) {
									moeglichkeiten.add(1);
									moeglichkeiten.add(3);
								} else {
									ausstehendeNamenHeim.add(ausstehendCpy[x][i]);
									ausstehendeNamenAusw.add(ausstehendCpy[x][i+1]);
									moeglichkeiten.add(-1);
									moeglichkeiten.add(-1);		
								}
							}
						}
					}
				}
			}
		}
	}
	
	

	private Team[] erstelleInitialeTabelle()
	{
		Team[] tmpTeam=null;
		int index2 = 0;
		
		tmpTeam = new Team[liga.getTeams().length];
		for (int i = 0; i < liga.getTeams().length; i++) {
			tmpTeam[i] = new Team(liga.getTeams()[i]);
		}
		
		for (int j = 0; j < moeglichkeiten.size(); j++) {
			int index = 0;
			if (j % liga.getTeams().length == 0 && j != 0) {
				index2++;
			}
			for (int k = 0; k < tmpTeam.length; k++) {
				if (ausstehendCpy[index2][j % liga.getTeams().length].getName().equals(tmpTeam[k].getName())) {
					index = k;
				}
			}
			
			if (moeglichkeiten.get(j) == 1) {
				tmpTeam[index].sieg();
			}
	
			if (moeglichkeiten.get(j) == 2) {
				tmpTeam[index].unentschieden();
			}
			if (moeglichkeiten.get(j) == 3) {
				tmpTeam[index].niederlage();
			}
		}

		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		
		return tmpTeam;
	}

	private void erstelleInitialeLoesungVarianteB(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		Team[] tmpTeam=null;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		for(int i=0; i<t.length;i++)
		{
			for(int j=0; j<l1.size(); j++)
			{
				if(tmpTeam[i].getName().equals(l1.get(j).getName()))
				{
					tmpTeam[i].unentschieden();
				}
				if(tmpTeam[i].getName().equals(l2.get(j).getName()))
				{
					tmpTeam[i].unentschieden();
				}
			}
		}
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
					
				}else{
					min=tmpTeam[i].getPlatzierung();					
				}			
			}
		}
	}
	private void erstelleInitialeLoesungVarianteC(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		Team[] tmpTeam=null;
		int durchschnitt=0;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
			for(int j=0; j<l1.size();j++)
			{
				if(tmpTeam[i].getName().equals(l1.get(j).getName()))
				{
					durchschnitt+=tmpTeam[i].getPunkte();
				}
				if(tmpTeam[i].getName().equals(l2.get(j).getName()))
				{
					durchschnitt+=tmpTeam[i].getPunkte();
				}
			}
		}
		
		if(l1.size()>0)
		{
			durchschnitt=durchschnitt/(l1.size()*2);
		}
		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		for(int i=0; i<heim.size(); i++)
		{
			if(MAX)
			{
				if(heim.get(i).getPunkte()>durchschnitt&&ausw.get(i).getPunkte()>durchschnitt)
				{
					heim.get(i).unentschieden();
					ausw.get(i).unentschieden();
				}else{
					if(heim.get(i).getPunkte()>durchschnitt&&ausw.get(i).getPunkte()<=durchschnitt)
					{
						heim.get(i).niederlage();
						ausw.get(i).sieg();
					}else{
						if(heim.get(i).getPunkte()<=durchschnitt&&ausw.get(i).getPunkte()>durchschnitt)
						{
							heim.get(i).sieg();
							ausw.get(i).niederlage();
						}else{
							if(heim.get(i).getPunkte()<=durchschnitt&&ausw.get(i).getPunkte()<=durchschnitt)
							{
								heim.get(i).unentschieden();
								ausw.get(i).unentschieden();
							}else{
								//System.out.println("-------------------------->hier<--------------");
								//System.out.println(heim.get(i).getPunkte()+"   "+ausw.get(i).getPunkte());
							}
						}
					}
				}
			}else{
				if(heim.get(i).getPunkte()>=durchschnitt&&ausw.get(i).getPunkte()>=durchschnitt)
				{
					heim.get(i).unentschieden();
					ausw.get(i).unentschieden();
				}else{
					if(heim.get(i).getPunkte()>durchschnitt&&ausw.get(i).getPunkte()<=durchschnitt)
					{
						heim.get(i).sieg();
						ausw.get(i).niederlage();
						//ausw.get(i).sieg();
						//heim.get(i).niederlage();
					}else{
						if(heim.get(i).getPunkte()<durchschnitt&&ausw.get(i).getPunkte()>=durchschnitt)
						{
							//heim.get(i).sieg();
							//ausw.get(i).niederlage();
							heim.get(i).niederlage();
							ausw.get(i).sieg();
						}else{
							if(heim.get(i).getPunkte()<durchschnitt&&ausw.get(i).getPunkte()<durchschnitt)
							{
								heim.get(i).unentschieden();
								ausw.get(i).unentschieden();
							}else{
								//System.out.println("-------------------------->hier<--------------");
								//System.out.println(heim.get(i).getPunkte()+"   "+ausw.get(i).getPunkte());
							}
						}
					}
				}
			}

		}
		//System.out.println("neu: ");
		//ausgabeAusstehendeTeams(heim, ausw);
		//System.out.println("Durchschnitt: "+durchschnitt);
		
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
	}
	private void erstelleInitialeLoesungVarianteD(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		Team[] tmpTeam=null;
		int durchschnitt=0;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
			for(int j=0; j<l1.size();j++)
			{
				if(tmpTeam[i].getName().equals(l1.get(j).getName()))
				{
					durchschnitt+=tmpTeam[i].getPunkte();
				}
				if(tmpTeam[i].getName().equals(l2.get(j).getName()))
				{
					durchschnitt+=tmpTeam[i].getPunkte();
				}
			}
		}
		
		if(l1.size()>0)
		{
			durchschnitt=durchschnitt/(l1.size()*2);
		}
		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		for(int i=0; i<heim.size(); i++)
		{
			int offeneSpieltageHeim=ausstehendeAnzahlVonSpielen(heim.get(i), heim, ausw);
			int offeneSpieltageAusw=ausstehendeAnzahlVonSpielen(ausw.get(i), heim, ausw);
			if(MAX)
			{
				if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)>ausw.get(i).getPunkte()+(3*offeneSpieltageAusw))
				{
					heim.get(i).niederlage();
					ausw.get(i).sieg();
				}else{
					if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)<ausw.get(i).getPunkte()+(3*offeneSpieltageAusw))
					{
						heim.get(i).sieg();
						ausw.get(i).niederlage();
					}else{
						heim.get(i).unentschieden();
						ausw.get(i).unentschieden();
					}
				}
			}else{
				if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)>ausw.get(i).getPunkte()+(3*offeneSpieltageAusw))
				{
					heim.get(i).sieg();
					ausw.get(i).niederlage();
				}else{
					if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)<ausw.get(i).getPunkte()+(3*offeneSpieltageAusw))
					{
						heim.get(i).niederlage();
						ausw.get(i).sieg();
					}else{
						heim.get(i).unentschieden();
						ausw.get(i).unentschieden();
					}
				}
			}
		}
		//System.out.println("neu: ");
		//ausgabeAusstehendeTeams(heim, ausw);
		//System.out.println("Durchschnitt: "+durchschnitt);
		
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
	}
	
	private void erstelleInitialeLoesungVarianteE(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2){
		Team[] tmpTeam=null;
		
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		

		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		for(int i=0; i<heim.size(); i++)
		{
			int offeneSpieltageHeim=ausstehendeAnzahlVonSpielen(heim.get(i), heim, ausw);
			int offeneSpieltageAusw=ausstehendeAnzahlVonSpielen(ausw.get(i), heim, ausw);
			if(MAX)
			{
				if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)-ausw.get(i).getPunkte()+(3*offeneSpieltageAusw)>3)
				{
					heim.get(i).niederlage();
					ausw.get(i).sieg();
				}else{
					if(ausw.get(i).getPunkte()+(3*offeneSpieltageHeim)-heim.get(i).getPunkte()+(3*offeneSpieltageAusw)>3)
					{
						heim.get(i).sieg();
						ausw.get(i).niederlage();
					}else{
						heim.get(i).unentschieden();
						ausw.get(i).unentschieden();
					}
				}
			}else{
				if(heim.get(i).getPunkte()+(3*offeneSpieltageHeim)-ausw.get(i).getPunkte()+(3*offeneSpieltageAusw)<3)
				{
					ausw.get(i).niederlage();
					heim.get(i).sieg();
				}else{
					if(ausw.get(i).getPunkte()+(3*offeneSpieltageHeim)-heim.get(i).getPunkte()+(3*offeneSpieltageAusw)<3)
					{
						ausw.get(i).sieg();
						heim.get(i).niederlage();
					}else{
						heim.get(i).unentschieden();
						ausw.get(i).unentschieden();
					}
				}
			}
		}

		
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
	}
	
	private void erstelleInitialeLoesungVarianteMAX_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2){
		Team[] tmpTeam=null;
		
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		

		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		int px=maxPZ;
		//System.out.println("P(x): "+px);
		ArrayList<Integer> mins=new ArrayList<Integer>();
		ArrayList<Integer> maxs=new ArrayList<Integer>();

		for(int i=0; i<t.length;i++)
		{
			int anzahlSpieltageY=ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(t[i].getPunkte());
			maxs.add(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY));
		
			//System.out.println("test: "+maxs.get(i));
			//System.out.println("Diff von: "+t[i].getName()+" "+t[i].getPunkte()+"-"+(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY)));
		}
		for(int i=0; i<heim.size();i++)
		{
			int minPY = 0,maxPY=0,minPZ=0,maxPZ=0;
			int y=-1;
			int z=-1;
			for(int j=0; j<t.length;j++)
			{
				if(heim.get(i).getName().equals(t[j].getName())){
					minPY = mins.get(j);
					maxPY = maxs.get(j);
					y=j;
				}else{
					if(ausw.get(i).getName().equals(t[j].getName())){
						minPZ = mins.get(j);
						maxPZ = maxs.get(j);
						z=j;
					}
				}
			}
			

			
			//System.out.print("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
		
			
			if(minPY==px && minPZ==px)
			{
				heim.get(i).sieg();
				ausw.get(i).niederlage();
				mins.set(y, mins.get(y)+3);
				maxs.set(z, maxs.get(z)-3);
			}else{
				if((minPY==px-1||minPY==px-2)&&minPZ>=px){
					ausw.get(i).sieg();
					heim.get(i).niederlage();
					mins.set(z, mins.get(z)+3);
					maxs.set(y, maxs.get(y)-3);
				}else{
					if((minPZ==px-1||minPZ==px-2)&&minPY>=px){
						heim.get(i).sieg();
						ausw.get(i).niederlage();
						mins.set(y, mins.get(y)+3);
						maxs.set(z, maxs.get(z)-3);
					}else{
						heim.get(i).unentschieden();
						ausw.get(i).unentschieden();
						mins.set(y, mins.get(y)+1);
						maxs.set(y, maxs.get(y)-2);
						mins.set(z, mins.get(z)+1);
						maxs.set(z, maxs.get(z)-2);
					}
				}
			}
			//System.out.println("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
			//System.out.println();
		}
		
		//System.out.println("Ende: "+min);
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
		//System.out.println("nach dem komplexen Initialisierungsvorgang sieht die Tabelle wie folgt aus:");
		//ausgabeTabelle(tmpTeam);	
	}
	
	private void erstelleInitialeLoesungVarianteMIN_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2){
		Team[] tmpTeam=null;
		
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		

		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		for(int i=0; i<heim.size(); i++)
		{
			int offeneSpieltageHeim=ausstehendeAnzahlVonSpielen(heim.get(i), heim, ausw);
			int offeneSpieltageAusw=ausstehendeAnzahlVonSpielen(ausw.get(i), heim, ausw);

			if(heim.get(i).getPunkte()==maxPZ-1&&ausw.get(i).getPunkte()==maxPZ-1){
				ausw.get(i).unentschieden();
				heim.get(i).unentschieden();
			}else{
				if(heim.get(i).getPunkte()<ausw.get(i).getPunkte())
				{
					ausw.get(i).niederlage();
					heim.get(i).sieg();
				}else{
					if(ausw.get(i).getPunkte()<heim.get(i).getPunkte())
					{
						ausw.get(i).sieg();
						heim.get(i).niederlage();
					}else{
						if(offeneSpieltageHeim<offeneSpieltageAusw){
							ausw.get(i).niederlage();
							heim.get(i).sieg();
						}else{
							if(offeneSpieltageAusw<offeneSpieltageHeim)
							{
								ausw.get(i).sieg();
								heim.get(i).niederlage();
							}else{
								ausw.get(i).niederlage();
								heim.get(i).sieg();
							}
						}						
					}
				}
			}
		}
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
	}
	
	private void erstelleInitialeLoesungVarianteMIN_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,boolean nachb){
		Team[] tmpTeam=null;
		
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		

		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		int px=team.getPunkte();
		//System.out.println("P(x): "+px);
		ArrayList<Integer> mins=new ArrayList<Integer>();
		ArrayList<Integer> maxs=new ArrayList<Integer>();

		for(int i=0; i<t.length;i++)
		{
			int anzahlSpieltageY=ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(t[i].getPunkte());
			maxs.add(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY));
		
			//System.out.println("test: "+maxs.get(i));
			//System.out.println("Diff von: "+t[i].getName()+" "+t[i].getPunkte()+"-"+(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY)));
		}
		for(int i=0; i<heim.size();i++)
		{
			int minPY = 0,maxPY=0,minPZ=0,maxPZ=0;
			int y=-1;
			int z=-1;
			for(int j=0; j<t.length;j++)
			{
				if(heim.get(i).getName().equals(t[j].getName())){
					minPY = mins.get(j);
					maxPY = maxs.get(j);
					y=j;
				}else{
					if(ausw.get(i).getName().equals(t[j].getName())){
						minPZ = mins.get(j);
						maxPZ = maxs.get(j);
						z=j;
					}
				}
			}
			

			
			//System.out.print("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
		
			
			
			if(minPY>=px||minPZ>=px){
				//System.out.println("Fall 1");
				if(minPY>=minPZ){
					ausw.get(i).sieg();
					heim.get(i).niederlage();
					mins.set(z, mins.get(z)+3);
					maxs.set(y, maxs.get(y)-3);
				}else{
					heim.get(i).sieg();
					ausw.get(i).niederlage();
					mins.set(y, mins.get(y)+3);
					maxs.set(z, maxs.get(z)-3);
				}
			}else{
				if(maxPY<px && maxPZ<px){
					//System.out.println("Fall 2");
					if(minPY>=minPZ){
						heim.get(i).sieg();
						ausw.get(i).niederlage();
						mins.set(y, mins.get(y)+3);
						maxs.set(z, maxs.get(z)-3);
					}else{
						ausw.get(i).sieg();
						heim.get(i).niederlage();
						mins.set(z, mins.get(z)+3);
						maxs.set(y, maxs.get(y)-3);
					}
				}else{
					if(maxPY<px && maxPZ >= px){
						//System.out.println("Fall 3");
						ausw.get(i).sieg();
						heim.get(i).niederlage();
						mins.set(z, mins.get(z)+3);
						maxs.set(y, maxs.get(y)-3);
					}else{
						if(maxPZ<px && maxPY>=px){
							//System.out.println("Fall 4");
							heim.get(i).sieg();
							ausw.get(i).niederlage();
							mins.set(y, mins.get(y)+3);
							maxs.set(z, maxs.get(z)-3);
						}else{
							if(maxPY==px+2 && maxPZ==px+2){
								//System.out.println("Fall 5");
								heim.get(i).unentschieden();
								ausw.get(i).unentschieden();
								mins.set(y, mins.get(y)+1);
								maxs.set(y, maxs.get(y)-2);
								mins.set(z, mins.get(z)+1);
								maxs.set(z, maxs.get(z)-2);
							}else{
								if((maxPY>=px&&maxPY<=px+2) && (maxPZ>=px&&maxPZ<=px+2))
								{
									//System.out.println("Fall 6");
									if(minPY>=minPZ){
										heim.get(i).sieg();
										ausw.get(i).niederlage();
										mins.set(y, mins.get(y)+3);
										maxs.set(z, maxs.get(z)-3);
									}else{
										ausw.get(i).sieg();
										heim.get(i).niederlage();
										mins.set(z, mins.get(z)+3);
										maxs.set(y, maxs.get(y)-3);
									}
								}else{
									if(maxPY==px+2 && maxPZ>px+2)
									{
										//System.out.println("Fall 7");
										if(minPY==px-1){
											heim.get(i).unentschieden();
											ausw.get(i).unentschieden();
											mins.set(y, mins.get(y)+1);
											maxs.set(y, maxs.get(y)-2);
											mins.set(z, mins.get(z)+1);
											maxs.set(z, maxs.get(z)-2);
										}else{
											if(minPY<minPZ){
												heim.get(i).sieg();
												ausw.get(i).niederlage();
												mins.set(y, mins.get(y)+3);
												maxs.set(z, maxs.get(z)-3);
											}else{
												heim.get(i).unentschieden();
												ausw.get(i).unentschieden();
												mins.set(y, mins.get(y)+1);
												maxs.set(y, maxs.get(y)-2);
												mins.set(z, mins.get(z)+1);
												maxs.set(z, maxs.get(z)-2);
											}
										}
									}else{
										if(maxPZ==px+2 && maxPY>px+2){
											//System.out.print("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
											//System.out.println("Fall 7.5");
											if(minPZ==px-1){
												heim.get(i).unentschieden();
												ausw.get(i).unentschieden();
												mins.set(y, mins.get(y)+1);
												maxs.set(y, maxs.get(y)-2);
												mins.set(z, mins.get(z)+1);
												maxs.set(z, maxs.get(z)-2);
											}else{
												if(minPZ<minPY){
													ausw.get(i).sieg();
													heim.get(i).niederlage();
													mins.set(z, mins.get(z)+3);
													maxs.set(y, maxs.get(y)-3);
												}else{
													heim.get(i).unentschieden();
													ausw.get(i).unentschieden();
													mins.set(y, mins.get(y)+1);
													maxs.set(y, maxs.get(y)-2);
													mins.set(z, mins.get(z)+1);
													maxs.set(z, maxs.get(z)-2);
												}
											}
										}else{
											if((maxPY==px || maxPY==px+1)&& maxPZ>px+2)
											{
												//System.out.println("Fall 8");
												heim.get(i).sieg();
												ausw.get(i).niederlage();
												mins.set(y, mins.get(y)+3);
												maxs.set(z, maxs.get(z)-3);
											}else{
												if(maxPZ==px+2 && maxPY>px+2)
												{
													//System.out.println("Fall 9");
													/*heim.get(i).unentschieden();
													ausw.get(i).unentschieden();
													mins.set(y, mins.get(y)+1);
													maxs.set(y, maxs.get(y)-2);
													mins.set(z, mins.get(z)+1);
													maxs.set(z, maxs.get(z)-2);*/
													if(minPY*maxPY<minPZ*maxPZ){
														heim.get(i).sieg();
														ausw.get(i).niederlage();
														mins.set(y, mins.get(y)+3);
														maxs.set(z, maxs.get(z)-3);
													}else{
														if(minPZ*maxPZ<minPY*maxPY){
															ausw.get(i).sieg();
															heim.get(i).niederlage();
															mins.set(z, mins.get(z)+3);
															maxs.set(y, maxs.get(y)-3);
														}else{
															ausw.get(i).unentschieden();
															mins.set(y, mins.get(y)+1);
															maxs.set(y, maxs.get(y)-2);
															mins.set(z, mins.get(z)+1);
															maxs.set(z, maxs.get(z)-2);
														}
													}
												}else{
													if((maxPZ==px || maxPZ==px+1)&& maxPY>px+2)
													{
														//System.out.println("Fall 10");
														ausw.get(i).sieg();
														heim.get(i).niederlage();
														mins.set(z, mins.get(z)+3);
														maxs.set(y, maxs.get(y)-3);
													}else{
														if(minPY < minPZ){
															//System.out.println("Fall 11");
															heim.get(i).sieg();
															ausw.get(i).niederlage();
															mins.set(y, mins.get(y)+3);
															maxs.set(z, maxs.get(z)-3);
														}else{
															if(minPZ<minPY){
																//System.out.println("Fall 12");
																ausw.get(i).sieg();
																heim.get(i).niederlage();
																mins.set(z, mins.get(z)+3);
																maxs.set(y, maxs.get(y)-3);
															}else{
																if(minPY==minPZ){
																	//System.out.println("Fall 13");
																	if(maxPY<=maxPZ)
																	{
																		heim.get(i).sieg();
																		ausw.get(i).niederlage();
																		mins.set(y, mins.get(y)+3);
																		maxs.set(z, maxs.get(z)-3);
																	}else{
																		ausw.get(i).sieg();
																		heim.get(i).niederlage();
																		mins.set(z, mins.get(z)+3);
																		maxs.set(y, maxs.get(y)-3);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}														
			}	
			//System.out.println("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
			//System.out.println();
		}
		
		//System.out.println("Ende: "+min);
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
		//System.out.println("nach dem komplexen Initialisierungsvorgang sieht die Tabelle wie folgt aus:");
		//ausgabeTabelle(tmpTeam);
		if(!nachb){
			if(nachbesserung!=null)nachbesserung.clear();
			for(int i=tmpTeam.length-1;i>=0;i--)
			{
				if(tmpTeam[i].getPunkte()<team.getPunkte())
				{
					if(nachbesserung==null)nachbesserung=new ArrayList<Team>();
					nachbesserung.add(tmpTeam[i]);
				}
			}
		}	
	}
	
	private void erstelleInitialeLoesungVarianteMAX_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2){
		Team[] tmpTeam=null;
		
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		

		
		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l1.get(i).getName().equals(tmpTeam[j].getName()))
				{
					heim.add(tmpTeam[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<tmpTeam.length; j++)
			{
				if(l2.get(i).getName().equals(tmpTeam[j].getName()))
				{
					ausw.add(tmpTeam[j]);
				}
			}
		}
		
		int px=maxPZ;
		//System.out.println("P(x): "+px);
		ArrayList<Integer> mins=new ArrayList<Integer>();
		ArrayList<Integer> maxs=new ArrayList<Integer>();

		for(int i=0; i<t.length;i++)
		{
			int anzahlSpieltageY=ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(t[i].getPunkte());
			maxs.add(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY));
		
			//System.out.println("test: "+maxs.get(i));
			//System.out.println("Diff von: "+t[i].getName()+" "+t[i].getPunkte()+"-"+(t[i].getPunkte()+(Zaehlweise.PUNKTE_S*anzahlSpieltageY)));
		}
		for(int i=0; i<heim.size();i++)
		{
			int minPY = 0,maxPY=0,minPZ=0,maxPZ=0;
			int y=-1;
			int z=-1;
			for(int j=0; j<t.length;j++)
			{
				if(heim.get(i).getName().equals(t[j].getName())){
					minPY = mins.get(j);
					maxPY = maxs.get(j);
					y=j;
				}else{
					if(ausw.get(i).getName().equals(t[j].getName())){
						minPZ = mins.get(j);
						maxPZ = maxs.get(j);
						z=j;
					}
				}
			}
			

			
			//System.out.print("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
		
			
			
			if(minPY>=px||minPZ>=px){
				//System.out.println("Fall 1");
				if(minPY<minPZ){
					ausw.get(i).sieg();
					heim.get(i).niederlage();
					mins.set(z, mins.get(z)+3);
					maxs.set(y, maxs.get(y)-3);
				}else{
					heim.get(i).sieg();
					ausw.get(i).niederlage();
					mins.set(y, mins.get(y)+3);
					maxs.set(z, maxs.get(z)-3);
				}
			}else{
				if(maxPY<px && maxPZ<px){
					//System.out.println("Fall 2");
					heim.get(i).unentschieden();
					ausw.get(i).unentschieden();
					mins.set(y, mins.get(y)+1);
					maxs.set(y, maxs.get(y)-2);
					mins.set(z, mins.get(z)+1);
					maxs.set(z, maxs.get(z)-2);
				}else{
					if(maxPY<=px && maxPZ > px){
						//System.out.println("Fall 3");
						ausw.get(i).sieg();
						heim.get(i).niederlage();
						mins.set(z, mins.get(z)+3);
						maxs.set(y, maxs.get(y)-3);
					}else{
						if(maxPZ<=px && maxPY>px){
							//System.out.println("Fall 4");
							heim.get(i).sieg();
							ausw.get(i).niederlage();
							mins.set(y, mins.get(y)+3);
							maxs.set(z, maxs.get(z)-3);
						}else{
							if(maxPY==px-2 && maxPZ==px-2){
								//System.out.println("Fall 5");
								heim.get(i).unentschieden();
								ausw.get(i).unentschieden();
								mins.set(y, mins.get(y)+1);
								maxs.set(y, maxs.get(y)-2);
								mins.set(z, mins.get(z)+1);
								maxs.set(z, maxs.get(z)-2);
							}else{
								if((maxPY<=px&&maxPY<=px-2) && (maxPZ<=px&&maxPZ<=px-2))
								{
									heim.get(i).unentschieden();
									ausw.get(i).unentschieden();
									mins.set(y, mins.get(y)+1);
									maxs.set(y, maxs.get(y)-2);
									mins.set(z, mins.get(z)+1);
									maxs.set(z, maxs.get(z)-2);
								}else{
									if(maxPY==px-2 && maxPZ>px-2)
									{
										ausw.get(i).sieg();
										heim.get(i).niederlage();
										mins.set(z, mins.get(z)+3);
										maxs.set(y, maxs.get(y)-3);
									}else{
										if(maxPZ==px-2 && maxPY>px-2){
											ausw.get(i).sieg();
											heim.get(i).niederlage();
											mins.set(z, mins.get(z)+3);
											maxs.set(y, maxs.get(y)-3);
										}else{
											if((maxPY==px )&& maxPZ>px-2)
											{
												//System.out.println("Fall 8");
												heim.get(i).sieg();
												ausw.get(i).niederlage();
												mins.set(y, mins.get(y)+3);
												maxs.set(z, maxs.get(z)-3);
											}else{
												if(maxPY==px-2 && maxPZ>px-2)
												{
													//System.out.println("Fall 9");
													heim.get(i).sieg();
													ausw.get(i).niederlage();
													mins.set(y, mins.get(y)+3);
													maxs.set(z, maxs.get(z)-3);
												}else{
													if((maxPZ==px )&& maxPY>px-2)
													{
														//System.out.println("Fall 10");
														ausw.get(i).sieg();
														heim.get(i).niederlage();
														mins.set(z, mins.get(z)+3);
														maxs.set(y, maxs.get(y)-3);
													}else{
														if(minPY < minPZ){
															//System.out.println("Fall 11");
															heim.get(i).sieg();
															ausw.get(i).niederlage();
															mins.set(y, mins.get(y)+3);
															maxs.set(z, maxs.get(z)-3);
														}else{			
															heim.get(i).unentschieden();
															ausw.get(i).unentschieden();
															mins.set(y, mins.get(y)+1);
															maxs.set(y, maxs.get(y)-2);
															mins.set(z, mins.get(z)+1);
															maxs.set(z, maxs.get(z)-2);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}														
			}	
			//System.out.println("Y:"+heim.get(i).getName()+" minPY: "+mins.get(y)+" maxPY: "+maxs.get(y)+"  vs. "+"Z:"+ausw.get(i).getName()+" minPZ: "+mins.get(z)+" maxPZ: "+maxs.get(z)+"  ");
			//System.out.println();
		}
		
		//System.out.println("Ende: "+min);
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
		
		for(int i=0; i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getName().equals(team.getName()))
			{
				if(MAX){
					max=tmpTeam[i].getPlatzierung();
				}else{
					min=tmpTeam[i].getPlatzierung();
				}			
			}
		}
		//System.out.println("nach dem komplexen Initialisierungsvorgang sieht die Tabelle wie folgt aus:");
		//ausgabeTabelle(tmpTeam);

	}
	private void erstelleInitialeLoesungVarianteMIN_I_Nachbesserung(Team[] t,int tag){
		int durchgang=0;
		int tmpMin=0;
		if(nachbesserung==null) return;
		
		//System.out.println("1 "+nachbesserung+ "  größe: "+nachbesserung.size());
		for(int b=0;b<nachbesserung.size();b++)
		{	
			xxx=new ArrayList<Integer>();
			//System.out.println("2 "+xxx);
			for(int a=t.length-1; a>=0; a--)
			{		
				//System.out.println("3 "+t);
				
				for(int c=0; c<=durchgang;c++)
				{
					//System.out.println("5 ");
					//System.out.println(t[a].getName()+"   "+nachbesserung.get(c).getName());
					if(t[a].getName().equals(nachbesserung.get(c).getName())){						
						for (int x = 0; x < anzahlUebrigerSpieltage - durchlauf; x++) {
							//System.out.println("5.0.1");
							for (int i = 0; i < ausstehendCpy[x].length; i = i + 2) {
								///System.out.println("5.0.2");
								//System.out.println(ausstehendCpy[x][i].getName()+"  "+t[a].getName());
								//System.out.println(tipps+" länge: "+tipps.size());
								//System.out.println(i + (x * anzahlTeams));
								//System.out.println(tipps.isEmpty());
								//System.out.println(tipps.get(i + (x * anzahlTeams)));
							
								if (ausstehendCpy[x][i].getName().equals(t[a].getName())&&tipps.get(i + (x * anzahlTeams)) == -1) {
									//System.out.println("5.0.3");
									if(!ausstehendCpy[x][i+1].getName().equals(team.getName())){
										//System.out.println("5.1");
										tipps.set(i + (x * anzahlTeams), 3);
										tipps.set(i + 1 + (x * anzahlTeams), 1);
										xxx.add(i + (x * anzahlTeams));
										xxx.add(i + 1 + (x * anzahlTeams));
									}
								}else{
									//System.out.println("5.0.4");
									if (ausstehendCpy[x][i+1].getName().equals(t[a].getName())&&tipps.get(i +1 + (x * anzahlTeams)) == -1) {
										//System.out.println("5.0.5");
										if(!ausstehendCpy[x][i].getName().equals(team.getName())){
											//System.out.println("5.2");
											tipps.set(i + (x * anzahlTeams), 1);
											tipps.set(i + 1 + (x * anzahlTeams), 3);
											xxx.add(i + (x * anzahlTeams));
											xxx.add(i + 1 + (x * anzahlTeams));
										}
									}
								}
							}
						}
					}
				}
			}
			//System.out.println("6");
			pruefeSchrankeMIN();
			//System.out.println("7");
			erzeugeInitialeMengen(k.getAktiveLiga().getTeams());
			//System.out.println("8");
			Team[] tmpTabelle=erstelleInitialeTabelle();
			//System.out.println("9");
			erstelleWerteMIN(k.getAktiveLiga().getTeams(),tag+1);
			//System.out.println("10");
			ArrayList<Team> tmpAusstehendeNamenHeim=new ArrayList<Team>();
			for(int i=0;i<ausstehendeNamenHeim.size();i++)
			{
				tmpAusstehendeNamenHeim.add(ausstehendeNamenHeim.get(i));
			}
			ArrayList<Team> tmpAusstehendeNamenAusw=new ArrayList<Team>();
			for(int i=0;i<ausstehendeNamenAusw.size();i++)
			{
				tmpAusstehendeNamenAusw.add(ausstehendeNamenAusw.get(i));
			}
			//System.out.println("11");
			erzeugeMengenTabelleMIN(tmpTabelle,tmpAusstehendeNamenHeim,tmpAusstehendeNamenAusw);
			//erstelleInitialeLoesungAlle(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw);
			//System.out.println("12");
			erstelleInitialeLoesungVarianteMIN_G_Komplex(tmpTabelle, tmpAusstehendeNamenHeim, tmpAusstehendeNamenAusw,true);
			//System.out.println("nachbesserung liefert: "+min);				
			
						
			for(int i=0; i<xxx.size();i++)
			{
				tipps.set(xxx.get(i), -1);
			}
			tmpMin=Math.max(tmpMin, min);
			durchgang++;
		}
		min=tmpMin;
	}
	
	

	private void erstelleInitialeLoesungAlle(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		if(MAX)
		{
			int altmax=max;
			erstelleInitialeLoesungVarianteB(t, l1, l2);
			int tmpMaxB=max;
			
			erstelleInitialeLoesungVarianteC(t, l1, l2);
			int tmpMaxC=max;
			
			erstelleInitialeLoesungVarianteD(t, l1, l2);
			int tmpMaxD=max;
			
			erstelleInitialeLoesungVarianteE(t, l1, l2);
			int tmpMaxE=max;
			
			erstelleInitialeLoesungVarianteMAX_F(t, l1, l2);
			int tmpMaxF=max;
			
			erstelleInitialeLoesungVarianteMAX_G_Komplex(t, l1, l2);
			int tmpMaxG=max;
			
			//System.out.println("MAX   b: "+tmpMaxB+"  c: "+tmpMaxC+"  d: "+tmpMaxD+" e: "+tmpMaxE+" f: "+tmpMaxF+" g: "+tmpMaxG);
			int tmpMax=Math.min(tmpMaxB, tmpMaxC);
			tmpMax=Math.min(tmpMax, tmpMaxD);
			tmpMax=Math.min(tmpMax, tmpMaxE);
			tmpMax=Math.min(tmpMax, tmpMaxF);
			tmpMax=Math.min(tmpMax, tmpMaxG);
			max=tmpMax;
			
			
			
			if(tmpMaxB==tmpMaxC&&tmpMaxC==tmpMaxD&&tmpMaxD==tmpMaxE&&tmpMaxE==tmpMaxF&&tmpMaxF==tmpMaxG){
				
			}else{
				if(altmax!=max){
					if(tmpMaxB==max){
						uBMax++;
					}
					if(tmpMaxC==max){
						uCMax++;
					}
					if(tmpMaxD==max){
						uDMax++;
					}
					if(tmpMaxE==max){
						uEMax++;
					}
					if(tmpMaxF==max){
						uFMax++;
					}
					if(tmpMaxG==max){
						uGMax++;
					}
				}
				
			}
		}else{
			int altmin=min;
			erstelleInitialeLoesungVarianteB(t, l1, l2);
			int tmpMinB=min;
			
			erstelleInitialeLoesungVarianteC(t, l1, l2);
			int tmpMinC=min;
			
			erstelleInitialeLoesungVarianteD(t, l1, l2);
			int tmpMinD=min;
			
			erstelleInitialeLoesungVarianteE(t, l1, l2);
			int tmpMinE=min;
			
			erstelleInitialeLoesungVarianteMIN_F(t, l1, l2);
			int tmpMinF=min;
			
			erstelleInitialeLoesungVarianteMIN_G_Komplex(t, l1, l2,false);
			int tmpMinG=min;
			

			int tmpMin=Math.max(tmpMinB, tmpMinC);
			tmpMin=Math.max(tmpMin, tmpMinD);
			tmpMin=Math.max(tmpMin, tmpMinE);
			tmpMin=Math.max(tmpMin, tmpMinF);
			tmpMin=Math.max(tmpMin, tmpMinG);
			//tmpMin=Math.max(tmpMin, tmpMinH);
			//System.out.println("MIN b: "+tmpMinB+"  c: "+tmpMinC+"  d: "+tmpMinD+" e: "+tmpMinE+" f: "+tmpMinF+" g: "+tmpMinG);
			//ausgabeWerte();
			min=tmpMin;
			if(tmpMinB==tmpMinC&&tmpMinC==tmpMinD&&tmpMinD==tmpMinE&&tmpMinE==tmpMinF&&tmpMinF==tmpMinG){
				
			}else{
				if(altmin!=min){
					if(tmpMinB==min){
						uBMin++;
					}
					if(tmpMinC==min){
						uCMin++;
					}
					if(tmpMinD==min){
						uDMin++;
					}
					if(tmpMinE==min){
						uEMin++;
					}
					if(tmpMinF==min){
						uFMin++;
					}
					if(tmpMinG==min){
						uGMin++;
					}
				}
				
			}
		}
		
		
		
		
	}
	private void erstelleWerteMAX(Team[] tmpTeam,int anzahlUebrigerTage)
	{
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}

		minTP=team.getPlatzierung();
		maxTP=1;
		for(int i=0;i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getPunkte()>maxPZ)
			{
				maxTP++;
			}else{
				break;
			}
		}
	}

	private void erstelleWerteMIN(Team[] tmpTeam,int anzahlUebrigerTage)
	{
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
			maxTP=team.getPlatzierung();
			minTP=tmpTeam.length;
			for(int i=tmpTeam.length-1;i>=0;i--)
			{
				if(tmpTeam[i].getPunkte()<minPZ)
				{
					minTP--;
				}else{
					break;
				}
			}
	}
	
	private void erstelleWerteMIN_MAX(Team[] tmpTeam,int anzahlUebrigerTage)
	{
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}

		minTP=team.getPlatzierung();
		maxTP=1;
		for(int i=0;i<tmpTeam.length;i++)
		{
			if(tmpTeam[i].getPunkte()>=maxPZ)
			{
				maxTP++;
			}else{
				break;
			}
		}
	}
	
	private void erstelleWerteMAX_MIN(Team[] tmpTeam,int anzahlUebrigerTage)
	{
		if(MAX)
		{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN,team);
		}
			maxTP=team.getPlatzierung();
			minTP=tmpTeam.length;
			for(int i=tmpTeam.length-1;i>=0;i--)
			{
				if(tmpTeam[i].getPunkte()<=minPZ)
				{
					minTP--;
				}else{
					break;
				}
			}
	}

	private void erzeugeMengenTabelleMAX(Team[] t, ArrayList<Team> l1,ArrayList<Team> l2)
	{	
		//System.out.println("hallo");
		int anzahlSpiele=0;
		for(int i=O.size()-1; i>=0; i--)
		{
			for(int j=0; j<t.length;j++)
			{
				if(O.get(i).getName().equals(t[j].getName()))
				{
					O.set(i, t[j]);
				}
			}
		}
		for(int i=U.size()-1; i>=0; i--)
		{
			for(int j=0; j<t.length;j++)
			{
				if(U.get(i).getName().equals(t[j].getName()))
				{
					U.set(i, t[j]);
				}
			}
		}
		for(int i=M.size()-1; i>=0; i--)
		{
			for(int j=0;j<t.length;j++)
			{
				if(M.get(i).getName().equals(t[j].getName()))
				{
					M.set(i, t[j]);
				}
			}
		}
		//System.out.println("Die Menge M in erzeugeMengenTabelleMAX: "+M.size());
		for(int i=M.size()-1; i>=0; i--)
		{
			anzahlSpiele=ausstehendeAnzahlVonSpielen(M.get(i), l1,l2);
			if(M.get(i).getPunkte()>maxPZ)
			{		
				if(anzahlSpiele>0)
				{
					for(int j=l1.size()-1;j>=0;j--)
					{	
						if(l1.get(j).getName().equals(M.get(i).getName()))
						{
							M.get(i).sieg();
							for(int k=0; k<M.size();k++)
							{
								if(l2.get(j).getName().equals(M.get(k).getName()))
								{
									M.get(k).niederlage();
								}
							}	

							l1.remove(j);
							l2.remove(j);
						}else{
							if(l2.get(j).getName().equals(M.get(i).getName()))
							{
								for(int k=0; k<M.size();k++)
								{
									if(l1.get(j).getName().equals(M.get(k).getName()))
									{
										M.get(k).niederlage();
									}
								}	
								
								M.get(i).sieg();

								l1.remove(j);
								l2.remove(j);
							}
						}
					}
					O.add(M.remove(i));
					maxTP++;
					erzeugeMengenTabelleMAX(t, l1,l2);
					break;
				}
				O.add(M.remove(i));
				maxTP++;
			}else{
				if(M.get(i).getPunkte()<=maxPZ-(Zaehlweise.PUNKTE_S*anzahlSpiele))
				{
					//System.out.println("Mannschaft: "+M.get(i).getName()+" "+M.get(i).getPunkte()+" "+anzahlSpiele);
					//Team[] test = new Team[M.size()];
					//ausgabeTabelle(M.toArray(test));
					
					//ausgabeAusstehendeTeams(l1, l2);
					if(anzahlSpiele>0)
					{
						for(int j=l1.size()-1;j>=0;j--)
						{
							if(l1.get(j).getName().equals(M.get(i).getName()))
							{
								M.get(i).sieg();
								for(int k=0; k<M.size();k++)
								{
									if(l2.get(j).getName().equals(M.get(k).getName()))
									{
										M.get(k).niederlage();
									}
								}		

								l1.remove(j);
								l2.remove(j);
							}else{
								if(l2.get(j).getName().equals(M.get(i).getName()))
								{
									M.get(i).sieg();
									for(int k=0; k<M.size();k++)
									{
										if(l1.get(j).getName().equals(M.get(k).getName()))
										{
											M.get(k).niederlage();
										}
									}	

									l1.remove(j);
									l2.remove(j);
								}
							}
						}
						U.add(M.remove(i));
						minTP--;
						erzeugeMengenTabelleMAX(t, l1,l2);
						break;
					}
					U.add(M.remove(i));
					minTP--;
				}
			}
		}
	}

	private void erzeugeMengenTabelleMIN(Team[] t, ArrayList<Team> l1,ArrayList<Team> l2)
	{	
		//System.out.println("hallo");
		int anzahlSpiele=0;
		for(int i=O.size()-1; i>=0; i--)
		{
			for(int j=0; j<t.length;j++)
			{
				if(O.get(i).getName().equals(t[j].getName()))
				{
					O.set(i, t[j]);
				}
			}
		}
		for(int i=U.size()-1; i>=0; i--)
		{
			for(int j=0; j<t.length;j++)
			{
				if(U.get(i).getName().equals(t[j].getName()))
				{
					U.set(i, t[j]);
				}
			}
		}
		for(int i=M.size()-1; i>=0; i--)
		{
			for(int j=0;j<t.length;j++)
			{
				if(M.get(i).getName().equals(t[j].getName()))
				{
					M.set(i, t[j]);
				}
			}
		}
		//System.out.println("Die Menge M in erzeugeMengenTabelleMAX: "+M.size());
		for(int i=M.size()-1; i>=0; i--)
		{
			anzahlSpiele=ausstehendeAnzahlVonSpielen(M.get(i), l1,l2);
			if(M.get(i).getPunkte()>=maxPZ)
			{		
				if(anzahlSpiele>0)
				{
					for(int j=l1.size()-1;j>=0;j--)
					{
						if(l1.get(j).getName().equals(M.get(i).getName()))
						{
							M.get(i).niederlage();
							for(int k=0; k<M.size();k++)
							{
								if(l2.get(j).getName().equals(M.get(k).getName()))
								{
									M.get(k).sieg();
								}
							}	
							l1.remove(j);
							l2.remove(j);
						}else{
							if(l2.get(j).getName().equals(M.get(i).getName()))
							{
								for(int k=0; k<M.size();k++)
								{
									if(l1.get(j).getName().equals(M.get(k).getName()))
									{
										M.get(k).sieg();
									}
								}									
								M.get(i).niederlage();
								l1.remove(j);
								l2.remove(j);
							}
						}
					}
					O.add(M.remove(i));
					maxTP++;
					erzeugeMengenTabelleMIN(t, l1,l2);
					break;
				}
				O.add(M.remove(i));
				maxTP++;
			}else{
				if(M.get(i).getPunkte()<maxPZ-(Zaehlweise.PUNKTE_S*anzahlSpiele))
				{
					if(anzahlSpiele>0)
					{
						for(int j=l1.size()-1;j>=0;j--)
						{
							if(l1.get(j).getName().equals(M.get(i).getName()))
							{
								for(int k=0; k<M.size();k++)
								{
									if(l2.get(j).getName().equals(M.get(k).getName()))
									{
										M.get(k).sieg();
									}
								}		
								M.get(i).niederlage();
								l1.remove(j);
								l2.remove(j);
							}else{
								if(l2.get(j).getName().equals(M.get(i).getName()))
								{
									M.get(i).niederlage();
									for(int k=0; k<M.size();k++)
									{
										if(l1.get(j).getName().equals(M.get(k).getName()))
										{
											M.get(k).sieg();
										}
									}		
									l1.remove(j);
									l2.remove(j);
								}
							}
						}
						U.add(M.remove(i));
						minTP--;
						erzeugeMengenTabelleMIN(t, l1,l2);
						break;
					}
					U.add(M.remove(i));
					minTP--;
				}
			}
		}
	}

	private void erzeugeMengenMAX(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		O = new ArrayList<Team>();
		M = new ArrayList<Team>();
		U = new ArrayList<Team>();
		int anzahlSpiele=0;
		for(int i=0; i<tmpTeam.length; i++)
		{
			anzahlSpiele=ausstehendeAnzahlVonSpielen(tmpTeam[i], l1,l2);
			if(tmpTeam[i].getPunkte()>maxPZ)
			{
				O.add(tmpTeam[i]);
				
			}else{
				if(tmpTeam[i].getPunkte()<=maxPZ-(Zaehlweise.PUNKTE_S*anzahlSpiele))
				{
					U.add(tmpTeam[i]);	
				}else{
					M.add(tmpTeam[i]);
				}
			}
		}
	}

	private void erzeugeMengenMIN(Team[] tmpTeam, ArrayList<Team> l1, ArrayList<Team> l2)
	{
		O = new ArrayList<Team>();
		M = new ArrayList<Team>();
		U = new ArrayList<Team>();
		int anzahlSpiele=0;
		for(int i=0; i<tmpTeam.length; i++)
		{
			anzahlSpiele=ausstehendeAnzahlVonSpielen(tmpTeam[i], l1,l2);
			if(tmpTeam[i].getPunkte()>=maxPZ)
			{
				O.add(tmpTeam[i]);
				
			}else{
				if(tmpTeam[i].getPunkte()<maxPZ-(Zaehlweise.PUNKTE_S*anzahlSpiele))
				{
					U.add(tmpTeam[i]);	
				}else{
					M.add(tmpTeam[i]);
				}
			}
		}
	}

	private int verbessereNaivMAX(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		
		int tmpMaxTP=maxTP;
		ArrayList<String> raus=new ArrayList<String>();
		for(int i=0; i<l1.size(); i++)
		{
			if(raus.contains(l1.get(i).getName())||raus.contains(l2.get(i).getName()))
			{
				
			}else{
				if((l1.get(i).getPunkte()==maxPZ&&l2.get(i).getPunkte()==maxPZ)
					||(l1.get(i).getPunkte()==maxPZ&&l2.get(i).getPunkte()==maxPZ-1)
					||(l1.get(i).getPunkte()==maxPZ-1&&l2.get(i).getPunkte()==maxPZ)
					||(l1.get(i).getPunkte()==maxPZ&&l2.get(i).getPunkte()==maxPZ-2)
					||(l1.get(i).getPunkte()==maxPZ-2&&l2.get(i).getPunkte()==maxPZ))
				{
	
					tmpMaxTP++;
					raus.add(l1.get(i).getName());
					raus.add(l2.get(i).getName());
				}
			}		
		}
		return tmpMaxTP;
	}
	
	private int verbessereNaivMIN(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		
		int tmpMinTP=minTP;
		ArrayList<String> raus=new ArrayList<String>();
		for(int i=0; i<l1.size(); i++)
		{
			if(raus.contains(l1.get(i).getName())||raus.contains(l2.get(i).getName()))
			{
				
			}else{
				if((l1.get(i).getPunkte()==minPZ&&l2.get(i).getPunkte()==minPZ)
					||(l1.get(i).getPunkte()==minPZ+1&&l2.get(i).getPunkte()==minPZ+1)
					||(l1.get(i).getPunkte()==minPZ&&l2.get(i).getPunkte()==minPZ+1)
					||(l1.get(i).getPunkte()==minPZ+1&&l2.get(i).getPunkte()==minPZ)
					||(l1.get(i).getPunkte()==minPZ+2&&l2.get(i).getPunkte()==minPZ+2)
					||(l1.get(i).getPunkte()==minPZ+3&&l2.get(i).getPunkte()==minPZ+3)
					||(l1.get(i).getPunkte()==minPZ+3&&l2.get(i).getPunkte()==minPZ+2)
					||(l1.get(i).getPunkte()==minPZ+2&&l2.get(i).getPunkte()==minPZ+3))
				{
	
					tmpMinTP--;
					raus.add(l1.get(i).getName());
					raus.add(l2.get(i).getName());
				}
			}		
		}
		return tmpMinTP;
	}
	private int verbessereSchrankeMINVarianteB(ArrayList<Team> l1,ArrayList<Team>l2, Team[] t)
	{
		//TODO in arbeit schreiben wurde 2x modifizert
		int px=team.getPunkte();
		int siege=0;
		int unentschieden=0;
		for(int i=0; i<M.size();i++)
		{
			int diff=px-M.get(i).getPunkte();
			while(diff>=0)
			{
				if(diff-3>=0||diff-2>=0)
				{
					siege++;
					diff-=3;
				}else{
					if(diff-1>=0)
					{
						
						unentschieden++;
						diff-=1;
					}else{
						break;
					}
				}
			}
		}
		//System.out.println("siege: "+siege+" untentschieden: "+unentschieden);
		//System.out.println("offene Spiele: "+l1.size());
		ArrayList<Integer> offeneSiegeProMannschaft=new ArrayList<Integer>();
		double wert=siege+(unentschieden/2.0);
		for(int i=0;i<t.length;i++)
		{
			int test=px-(t[i].getPunkte());
				test=Math.round(test/3.0f);
			
			offeneSiegeProMannschaft.add(test);
		}
		Collections.sort(offeneSiegeProMannschaft);
		//System.out.println(offeneSiegeProMannschaft);
		
		if(l1.size()<wert)
		{
			int abzug=1;
			for(int i=offeneSiegeProMannschaft.size()-1;i>=0;i--)
			{
				//System.out.println("wert: "+wert+" - x:"+offeneSiegeProMannschaft.get(i)+" = "+(wert-offeneSiegeProMannschaft.get(i)));
				if(l1.size()<wert-offeneSiegeProMannschaft.get(i)){
					abzug++;
					wert-= offeneSiegeProMannschaft.get(i);
				}else{
					//System.out.println("Gib zurück: "+(minTP-abzug)+"  der abzug beträgt:"+abzug);
					return minTP-abzug;
				}
			}
			return minTP-1;
		}else{
			return minTP;
		}		
	}
	
	private int verbessereSchrankeMINVarianteC(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		int px=team.getPunkte();
		for(int i=0;i<l1.size();i++)
		{
			int anzahlSpieltageY=ausstehendeAnzahlVonSpielen(l1.get(i), l1, l2);
			int anzahlSpieltageZ=ausstehendeAnzahlVonSpielen(l2.get(i), l1, l2);
			
			int maxPY = l1.get(i).getPunkte()+(3*anzahlSpieltageY);
			int maxPZ = l2.get(i).getPunkte()+(3*anzahlSpieltageZ);
			if((maxPY<=px+2&&maxPZ<=px+2))
			{
				if((maxPY==maxPZ)){
					if(!(maxPY==px+2)){
						return minTP-1;
					}
				}
			}
		}
	
		return minTP;
	}
	
	private int verbessereSchrankeMIN(ArrayList<Team> l1,ArrayList<Team> l2, Team[] t)
	{
		int a=verbessereNaivMIN(l1,l2);
		int b=verbessereSchrankeMINVarianteB(l1,l2,t);
		int c=verbessereSchrankeMINVarianteC(l1, l2);
		//System.out.println("a: "+a+"  b: "+b+"  c: "+c);
		int tmpMin=Math.min(minTP, a);
		tmpMin=Math.min(tmpMin,b);
		tmpMin=Math.min(tmpMin, c);
		
		return tmpMin;	
	}
	
	private int verbessereSchrankeMAXVarianteB(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		int punkte=l1.size()*2;
		
		int summe=0;
		for(int i=0; i<M.size();i++)
		{
			summe +=maxPZ-M.get(i).getPunkte();
		}
		
		if(summe<punkte){
			return maxTP+1;
		}else{
			return maxTP;
		}
	}

	
	private int verbessereSchrankeMAX(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		int a= verbessereNaivMAX(l1, l2);
		int b= verbessereSchrankeMAXVarianteB(l1, l2);
		//System.out.println("a: "+a+"  b: "+b);
		int tmp=Math.max(maxTP,a);
		tmp=Math.max(tmp, b);
		return tmp;
	}
	
	
	private void test(ArrayList<Team> l1, ArrayList<Team> l2,Team[] t1){

		ArrayList<Team> heim=new ArrayList<Team>();
		ArrayList<Team> ausw=new ArrayList<Team>();

		for(int i=0; i<l1.size(); i++)
		{
			for(int j=0; j<t1.length; j++)
			{
				if(l1.get(i).getName().equals(t1[j].getName()))
				{
					heim.add(t1[j]);
				}
			}
		}
		for(int i=0; i<l2.size(); i++)
		{
			for(int j=0; j<t1.length; j++)
			{
				if(l2.get(i).getName().equals(t1[j].getName()))
				{
					ausw.add(t1[j]);
				}
			}
		}
		int offeneSpiele=l1.size();
		int px=team.getPunkte();
		int siege=0;
		int unentschieden=0;
		for(int i=0; i<M.size();i++)
		{
			int diff=px-M.get(i).getPunkte();
			while(diff>=0)
			{
				if(diff-3>=0||diff-2>=0)
				{
					siege++;
					diff-=3;
				}else{
					if(diff-1>=0)
					{
						
						unentschieden++;
						diff-=1;
					}else{
						break;
					}
				}
			}
		}

		double wert=siege+(unentschieden/2.0);
		
		while(wert>offeneSpiele){
			liga.ermittelPlatzierung(t1, Liga.SORTIERUNG_MIN,team);
			erzeugeMengenMIN(t1, heim, ausw);
			Team m=M.get(M.size()-1);
			//ausgabeTabelle(t1);
			//System.out.println("ausgewählt: "+m.getName());
			for(int j=heim.size()-1;j>=0;j--)
			{
				if(heim.get(j).getName().equals(m.getName()))
				{
					heim.get(j).niederlage();
					ausw.get(j).sieg();	
					heim.remove(j);
					ausw.remove(j);
				}else{
					if(ausw.get(j).getName().equals(m.getName()))
					{
						heim.get(j).sieg();
						ausw.get(j).niederlage();
						heim.remove(j);
						ausw.remove(j);
					}
				}
			}
			//System.out.println("fehler?");
			U.add(M.remove(M.size()-1));
			minTP--;
			//System.out.println("starte testR");
			//erzeugeMengenTabelleMIN(t1, heim, ausw);
			testR(t1,heim,ausw);
			offeneSpiele=heim.size();
			siege=0;
			unentschieden=0;
			for(int i=0; i<M.size();i++)
			{
				int diff=px-M.get(i).getPunkte();
				while(diff>=0)
				{
					if(diff-3>=0||diff-2>=0)
					{
						siege++;
						diff-=3;
					}else{
						if(diff-1>=0)
						{
							
							unentschieden++;
							diff-=1;
						}else{
							break;
						}
					}
				}
			}
			
			wert=siege+(unentschieden/2.0);
			//System.out.println("wert: "+wert+">  offeneSpiele: "+offeneSpiele );
		}
		

		liga.ermittelPlatzierung(t1, Liga.SORTIERUNG_MIN,team);

		test1=heim;
		test2=ausw;
	}
	
	private void testR(Team[] t, ArrayList<Team> l1,ArrayList<Team> l2){
		//System.out.println("hallo");
				int anzahlSpiele=0;
				for(int i=O.size()-1; i>=0; i--)
				{
					for(int j=0; j<t.length;j++)
					{
						if(O.get(i).getName().equals(t[j].getName()))
						{
							O.set(i, t[j]);
						}
					}
				}
				for(int i=U.size()-1; i>=0; i--)
				{
					for(int j=0; j<t.length;j++)
					{
						if(U.get(i).getName().equals(t[j].getName()))
						{
							U.set(i, t[j]);
						}
					}
				}
				for(int i=M.size()-1; i>=0; i--)
				{
					for(int j=0;j<t.length;j++)
					{
						if(M.get(i).getName().equals(t[j].getName()))
						{
							M.set(i, t[j]);
						}
					}
				}
				//System.out.println("Die Menge M in erzeugeMengenTabelleMAX: "+M.size());
				for(int i=M.size()-1; i>=0; i--)
				{
					anzahlSpiele=ausstehendeAnzahlVonSpielen(M.get(i), l1,l2);
					if(M.get(i).getPunkte()>=maxPZ)
					{		
						if(anzahlSpiele>0)
						{
							for(int j=l1.size()-1;j>=0;j--)
							{
								if(l1.get(j).getName().equals(M.get(i).getName()))
								{
									M.get(i).niederlage();
									for(int k=0; k<M.size();k++)
									{
										if(l2.get(j).getName().equals(M.get(k).getName()))
										{
											M.get(k).sieg();
										}
									}	
									l1.remove(j);
									l2.remove(j);
								}else{
									if(l2.get(j).getName().equals(M.get(i).getName()))
									{
										for(int k=0; k<M.size();k++)
										{
											if(l1.get(j).getName().equals(M.get(k).getName()))
											{
												M.get(k).sieg();
											}
										}									
										M.get(i).niederlage();
										l1.remove(j);
										l2.remove(j);
									}
								}
							}
							O.add(M.remove(i));
							maxTP++;
							erzeugeMengenTabelleMIN(t, l1,l2);
							break;
						}
						O.add(M.remove(i));
						maxTP++;
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
	private void backtracking(int aktuelleMoeglichkeit,int tiefe, ArrayList<Team> ausstehendeSpieleHeim,ArrayList<Team> ausstehendeSpieleAusw, Team[] vorherigeTabelle) {
		hallo++;
		//System.out.println("Das Backtracking wird gestartet: "+tiefe+"."+aktuelleMoeglichkeit+"      "+hallo++);
		//ausgabeWerte();
		Team[] tmpVorherigeTabelle=new Team[vorherigeTabelle.length];
		for(int i=0; i<vorherigeTabelle.length; i++)
		{
			tmpVorherigeTabelle[i]=new Team(vorherigeTabelle[i]);
		}
		ArrayList<Team> tmpAusstehendeSpieleHeim = new ArrayList<Team>();
		for(int i=0; i<ausstehendeSpieleHeim.size();i++)
		{
			for(int j=0; j<tmpVorherigeTabelle.length;j++)
			{
				if(ausstehendeSpieleHeim.get(i).getName().equals(tmpVorherigeTabelle[j].getName()))
				{
					tmpAusstehendeSpieleHeim.add(tmpVorherigeTabelle[j]);
				}
			}		
		}
		ArrayList<Team> tmpAusstehendeSpieleAusw = new ArrayList<Team>();
		for(int i=0; i<ausstehendeSpieleAusw.size();i++)
		{
			for(int j=0; j<tmpVorherigeTabelle.length;j++)
			{
				if(ausstehendeSpieleAusw.get(i).getName().equals(tmpVorherigeTabelle[j].getName()))
				{
					tmpAusstehendeSpieleAusw.add(tmpVorherigeTabelle[j]);
				}
			}		
		}
	
		//ausgabeAusstehendeTeams(tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
		//ausgabeTabelle(tmpVorherigeTabelle);
		//ausgabeWerte();
		
		if(aktuelleMoeglichkeit==1)
		{
			tmpAusstehendeSpieleHeim.get(0).sieg();
			tmpAusstehendeSpieleAusw.get(0).niederlage();
		}
		if(aktuelleMoeglichkeit==2)
		{
			tmpAusstehendeSpieleHeim.get(0).unentschieden();
			tmpAusstehendeSpieleAusw.get(0).unentschieden();
		}
		if(aktuelleMoeglichkeit==3)
		{
			tmpAusstehendeSpieleHeim.get(0).niederlage();
			tmpAusstehendeSpieleAusw.get(0).sieg();
		}
		
		tmpAusstehendeSpieleHeim.remove(0);
		tmpAusstehendeSpieleAusw.remove(0);
	
		if(MAX)
		{
			//System.out.println("erzeugeMengenTabelleMax");
			erzeugeMengenTabelleMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw);
			
			erzeugeMengenMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw);
			
			//ausgabeTabelle(tmpVorherigeTabelle);
			//ausgabeWerte();
			//ausgabeMengen();
		}else{
			//System.out.println("erzeugeMengenTabelleMIN");
			
			int anzahlU=U.size();
			erzeugeMengenTabelleMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw);
			if(anzahlU<U.size()){
				int NtmpMinTP=verbessereSchrankeMIN(tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw,tmpVorherigeTabelle);
				S=minTP-NtmpMinTP;
				//System.out.println("S: "+S+" minTP: "+minTP+" tmpMinTP: "+tmpMinTP+" NtmpMinTP: "+NtmpMinTP);
			}
			erzeugeMengenMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw);
		}
		
		
		int merkeMaxTP=maxTP;
		int merkeMinTP=minTP;
		

		if(maxTP==minTP)
		{
			if(MAX)
			{
				if(maxTP<max)
				{
					max=maxTP;
				}
			}else{
				if(maxTP>min)
				{
					min=maxTP;
				}
				if(minTP==min){
					//abbruch=true;
					//TODO ?
					//System.out.println("kompletter abbruch welcher schon längst überfällig war");
					//return;
				}
				if(tmpMinTP==min){
					abbruch=true;
					System.out.println("kompletter abbruch..tmpMinTP==min");				
					return;
				}
			}
			return;			
		}
		if(MAX)
		{
			if(maxTP>=max)
			{
				return;
			}
		}else{
			if(minTP-S<=min){
				//System.out.println("minTP-S<=min");
				return;
			}
			if(minTP<=min)
			{
				System.out.println("abbruch 1: "+min);
				//System.out.println("minTP<=min");
				return;
			}

		}
		S=0;
		for (int i = 1; i <= 3; i++) {
			if(tmpAusstehendeSpieleHeim.size()>0){
				//System.out.println("Werte bevor nächste Rekursion startet: ");
				//System.out.println("maxTP: "+maxTP+" minTP: "+minTP+" max: "+max);
				if(abbruch) return;
				backtracking(i,tiefe+1,tmpAusstehendeSpieleHeim,tmpAusstehendeSpieleAusw,tmpVorherigeTabelle);
				maxTP=merkeMaxTP;
				minTP=merkeMinTP;
				if(MAX)
				{
					erzeugeMengenMAX(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
				}else{
					erzeugeMengenMIN(tmpVorherigeTabelle, tmpAusstehendeSpieleHeim, tmpAusstehendeSpieleAusw);
				}
				
			}else{
				System.out.println("ERROR ERROR ERROR ERROR ERROR");
			}
		}
	}

	private int ausstehendeAnzahlVonSpielen(Team t,ArrayList<Team> l1,ArrayList<Team> l2)
	{
		int anzahl=0;
		for(int i=0;i<l1.size();i++)
		{
			if(l1.get(i).getName().equals(t.getName()))
			{
				anzahl++;
			}
		}
		for(int i=0;i<l2.size();i++)
		{
			if(l2.get(i).getName().equals(t.getName()))
			{
				anzahl++;
			}
		}
		return anzahl;
	}
	private void zeigeSpieleAn(int tag)
	{
		int x=anzahlUebrigerSpieltage-tag-1;
		//System.out.println("Beste Platzierung nach Spieltag: "+(liga.getAustragungsart().getAnzahlSpieltage()-x));
		ausgabeMoeglichkeiten();
		//System.out.println(moeglichkeiten.size()+"   "+merke.size());
		int j=0;
		for(int i=0; i<moeglichkeiten.size();i=i+2)
		{
			if(moeglichkeiten.get(i)==1){
				System.out.println(merke.get(i).getName()+"(w) vs "+merke.get(i+1).getName());
			}
			if(moeglichkeiten.get(i)==2){
				System.out.println(merke.get(i).getName()+"(w) vs (w)"+merke.get(i+1).getName());
			}
			if(moeglichkeiten.get(i)==3){
				System.out.println(merke.get(i).getName()+" vs (w)"+merke.get(i+1).getName());
			}
			if(moeglichkeiten.get(i)==-1)
			{
				j++;
				System.out.println(merke.get(i).getName()+"(-1) vs (-1)"+merke.get(i+1).getName());
			}			
		}
		System.out.println("j: "+j+"   merkeSpielausgänge.size: "+merkeSpielausgang.size());
		for(int i=0; i<merkeGegner.size();i=i+2)
		{
			if(merkeSpielausgang.get(i)==1)
			{
				System.out.println(merkeGegner.get(i).getName()+"(w)  vs.  "+merkeGegner.get(i).getName());
			}
			if(merkeSpielausgang.get(i)==2)
			{
				System.out.println(merkeGegner.get(i).getName()+"(w)  vs.  (w)"+merkeGegner.get(i).getName());
			}
			if(merkeSpielausgang.get(i)==3)
			{
				System.out.println(merkeGegner.get(i).getName()+"  vs.  (w)"+merkeGegner.get(i).getName());
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
	
	private void ausgabeWerte()
	{
		System.out.println("Erstellte Werte: ");
		System.out.println("maxPZ: "+maxPZ+"   maxTP: "+maxTP);
		System.out.println("minPZ: "+minPZ+"   minTP: "+minTP);
		System.out.println("MAX: "+max+"    MIN: "+min);
		
		System.out.println();
	}
	
	private void ausgabeMengen()
	{
		System.out.println("Die Menge M");
		for(int i=0; i<M.size();i++)
		{
			System.out.println(M.get(i).getName());
		}
		
		System.out.println("O: "+O.size());
		for(int i=0; i<O.size();i++)
		{
			System.out.println(O.get(i).getName());
		}
		
		System.out.println("U: "+U.size());
		for(int i=0;i<U.size();i++)
		{
			System.out.println(U.get(i).getName());
		}
	}
	private void ausgabeTabelle(Team[] t0)
	{
		ComparatorChain.t=team;
		if(MAX)
		{
			liga.ermittelPlatzierung(t0, Liga.SORTIERUNG_MAX,team);
		}else{
			liga.ermittelPlatzierung(t0, Liga.SORTIERUNG_MIN,team);
		}
		System.out.printf("%s ","Platz");
		System.out.printf("%-20s", "     Team");
		System.out.printf("%s  ","  Punkte");
		System.out.printf("%s%n", "  Gespielt");
		for(int i=0; i<t0.length; i++)
		{

			System.out.printf("%4d  ", t0[i].getPlatzierung());
			System.out.printf("%-25s  ", t0[i].getName());
			System.out.printf("%2d   ", t0[i].getPunkte());
			System.out.printf("%2d/34 %n", (t0[i].getSiege()+t0[i].getNiederlagen()+t0[i].getUnentschieden()));

			//System.out.println(t0[i].getPlatzierung()+"  "+t0[i].getName()+"   "+t0[i].getPunkte()+"   "+(t0[i].getSiege()+t0[i].getNiederlagen()+t0[i].getUnentschieden())+"/34                    sieg: "+t0[i].getSiege()+"   unentschieden: "+t0[i].getUnentschieden()+"   niederlage: "+t0[i].getNiederlagen());
		}
		System.out.println();
	}
	private void ausgabeAusstehendeTeams(ArrayList<Team> l1, ArrayList<Team> l2)
	{
		System.out.println("Ausstehende Teams:");
		for(int i=0; i<l1.size(); i++)
		{
			System.out.printf("%25s  vs.  ", l1.get(i).getName());
			System.out.printf("%-25s  %n", l2.get(i).getName());
			
			//System.out.println(l1.get(i).getName()+"  vs.  "+l2.get(i).getName());
		}
	}
	
	private void ausgabeMoeglichkeiten()
	{
		System.out.println("Ausgabe Möglichkeiten");
		for(int i=0; i<moeglichkeiten.size(); i++)
		{
			System.out.print(moeglichkeiten.get(i)+" ");
		}
		System.out.println();
	}
	
	private void ausgabeTipps()
	{
		System.out.println("Ausgabe Tipps");
		for(int i=0; i<tipps.size();i++)
		{
			System.out.print(tipps.get(i)+" ");
		}
		System.out.println("");
	}
	private void ermittel(int altesMax, int alteSchranke, int neuesMax, int neueSchranke)
	{
		if(MAX){
			switch(altesMax-neuesMax){
				case 1: uMax1++; break;
				case 2: uMax2++; break;
				case 3: uMax3++; break;
				case 4: uMax4++; break;
				case 5: uMax5++; break;
				case 6: uMax6++; break;
				case 7: uMax7++; break;
				case 8: uMax8++; break;
				case 9: uMax9++; break;
				case 10: uMax10++; break;
				case 11: uMax11++; break;
				case 12: uMax12++; break;
				case 13: uMax13++; break;
				case 14: uMax14++; break;
				case 15: uMax15++; break;
				case 16: uMax16++; break;
				case 17: uMax17++; break;
				case 18: uMax18++; break;
			}
		
			switch(neueSchranke-alteSchranke){
				case 1: uSMax1++; break;
				case 2: uSMax2++; break;
				case 3: uSMax3++; break;
				case 4: uSMax4++; break;
				case 5: uSMax5++; break;
				case 6: uSMax6++; break;
				case 7: uSMax7++; break;
				case 8: uSMax8++; break;
				case 9: uSMax9++; break;
				case 10: uSMax10++; break;
				case 11: uSMax11++; break;
				case 12: uSMax12++; break;
			}
		}else{
			switch(neuesMax-altesMax){
				case 1: uMin1++; break;
				case 2: uMin2++; break;
				case 3: uMin3++; break;
				case 4: uMin4++; break;
				case 5: uMin5++; break;
				case 6: uMin6++; break;
				case 7: uMin7++; break;
				case 8: uMin8++; break;
				case 9: uMin9++; break;
				case 10: uMin10++; break;
				case 11: uMin11++; break;
				case 12: uMin12++; break;
				case 13: uMin13++; break;
				case 14: uMin14++; break;
				case 15: uMin15++; break;
				case 16: uMin16++; break;
				case 17: uMin17++; break;
				case 18: uMin18++; break;
			}
	
			switch(alteSchranke-neueSchranke){
				case 1: uSMin1++; break;
				case 2: uSMin2++; break;
				case 3: uSMin3++; break;
				case 4: uSMin4++; break;
				case 5: uSMin5++; break;
				case 6: uSMin6++; break;
				case 7: uSMin7++; break;
				case 8: uSMin8++; break;
				case 9: uSMin9++; break;
				case 10: uSMin10++; break;
				case 11: uSMin11++; break;
				case 12: uSMin12++; break;
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
	private void ausgabeErmittelt(){
		//System.out.println("MAX:");
		
		//System.out.println(" "+uMax1+" "+uMax2+" "+uMax3+" "+uMax4+" "+uMax5+" "+uMax6+" "+uMax7+" "+uMax8+" "+uMax9+" "+uMax10+" "+uMax11+" "+uMax12+" "+uMax13+" "+uMax14+" "+uMax15+" "+uMax16+" "+uMax17+" "+uMax18);
		//System.out.println(" "+uSMax1+" "+uSMax2+" "+uSMax3+" "+uSMax4+" "+uSMax5+" "+uSMax6+" "+uSMax7+" "+uSMax8+" "+uSMax9+" "+uSMax10+" "+uSMax11+" "+uSMax12);
		//System.out.println("B: "+uBMax+" C: "+uCMax+" D: "+uDMax+" E: "+uEMax+" F: "+uFMax+" G: "+uGMax);
		System.out.println("abc=max: "+abc2);
		System.out.println("init=max: "+abcdef2);
		System.out.println("rek=max: "+rek2	);
		System.out.println("verbesserut hat x mal geholhen: "+nMin);
		//System.out.println("MIN:");
		//System.out.println(" "+uMin1+" "+uMin2+" "+uMin3+" "+uMin4+" "+uMin5+" "+uMin6+" "+uMin7+" "+uMin8+" "+uMin9+" "+uMin10+" "+uMin11+" "+uMin12+" "+uMin13+" "+uMin14+" "+uMin15+" "+uMin16+" "+uMin17+" "+uMin18);
		//System.out.println(" "+uSMin1+" "+uSMin2+" "+uSMin3+" "+uSMin4+" "+uSMin5+" "+uSMin6+" "+uSMin7+" "+uSMin8+" "+uSMin9+" "+uSMin10+" "+uSMin11+" "+uSMin12);
		//System.out.println("B: "+uBMin+" C: "+uCMin+" D: "+uDMin+" E: "+uEMin+" F: "+uFMin+" G: "+uGMin);
		System.out.println("abc=min: "+abc);
		System.out.println("init=min: "+abcdef);
		System.out.println("nachb=min: "+abcdefghi);
		System.out.println("rek=min: "+rek	);
		System.out.println("verbesserut hat x mal geholhen: "+nMin);
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
	
	public Task<Long> getTask()
	{
		return this.task;
	}
	
}
