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


	private int durchlauf = 0; // wird benötigt um zu prüfen wie viele tage
								// schon in betracht gezogen wurden
	private int anzahlUebrigerSpieltage;




	public Task<Long> task;
	public Thread berechnungen;

	

	private long dauer; // dauer für eine berechnung MAX MIN
	
	private boolean inBearbeitung = false; // prüfen ob bereits ein MAX MIN für
											// ein Team berechnet wird

	
	private Long[] dauerListe; // Eine Liste die die Zeiten für die Berechnungen
								// enthält

	private Team team;

	

	

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
	

				// führe die Berechnungen so oft aus wie es noch übrige
				// Spieltage gibt,
				// da man von jedem Spieltag
				// das zu erreichende Maximum bestimmen möchte. Hierbei ist zu
				// beachten
				// das erst der Spieltag "n" betrachtet wird dann "n-1",
				// "n-2"...
				int kk = 0;
				int a = 0;
				Liga liga=Koordinator.getKoordinator().getAktiveLiga();
				team=t;
				if (dauerListe == null) {
					dauerListe = new Long[liga.getTeams().length];
				}
				durchlauf=0;
				anzahlUebrigerSpieltage=liga.getAnzahlAusstehend() / liga.getTeams().length;
				for (int tag = anzahlUebrigerSpieltage - 1; tag >= 0; tag--) {

					System.out.println("----------tag: " + (34 - kk) + "-----------------");
					kk++;


					new AlgorithmusMax(t).berechneMAX(tag,false,durchlauf);
					
					updateProgress(++a, anzahlUebrigerSpieltage * 2);

					new AlgorithmusMin(t).berechneMIN(tag,false,durchlauf);
					

					durchlauf++;
					updateProgress(++a, anzahlUebrigerSpieltage * 2);
				}
				liga.ermittelPlatzierung(liga.getTeams(), Liga.SORTIERUNG_NORMAL, null);
				long ende = System.currentTimeMillis() - anfang; // dauer des
																	// algorithmus
				return ende;
			}
		};

		// der Ladebalken wird mit dem Task "verbunden"
		ProgressBar bar = Koordinator.getKoordinator().getProgressBar();
		if(bar!=null){
			bar.progressProperty().bind(task.progressProperty());			
		}

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
					System.out.println("ausgabeErmittelt() sollte aufgerufen werden?");
					//ausgabeErmittelt();
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
	
	public void testOhneInitLoesung(Team t){
		durchlauf=0;
		Liga liga=Koordinator.getKoordinator().getAktiveLiga();
		anzahlUebrigerSpieltage=liga.getAnzahlAusstehend() / liga.getTeams().length;
		for (int tag = anzahlUebrigerSpieltage - 1; tag >= 0; tag--) {
			new AlgorithmusMax(t).berechneMAX(tag,true,durchlauf);
			new AlgorithmusMin(t).berechneMIN(tag,true,durchlauf);			
			durchlauf++;
		}
	}
	
	public void testMitInitLoesung(Team t){
		durchlauf=0;
		Liga liga=Koordinator.getKoordinator().getAktiveLiga();
		anzahlUebrigerSpieltage=liga.getAnzahlAusstehend() / liga.getTeams().length;
		for (int tag = anzahlUebrigerSpieltage - 1; tag >= 0; tag--) {
			new AlgorithmusMax(t).berechneMAX(tag,false,durchlauf);
			new AlgorithmusMin(t).berechneMIN(tag,false,durchlauf);			
			durchlauf++;
		}
	}


	
	

	/**
	 * 
	 * @return gibt die Dauer in ms zurück die der Algorithmus gebraucht hat
	 */
	public long getDauer() {
		return this.dauer;
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
