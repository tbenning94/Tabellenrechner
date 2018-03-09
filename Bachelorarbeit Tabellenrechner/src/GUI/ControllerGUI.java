package GUI;

import java.io.IOException;

import Fachkonzept.Koordinator;
import Fachkonzept.Meisterschaft;
import Fachkonzept.Team;
import Fachkonzept.Beobachter;
import Fachkonzept.Europameisterschaft;
import Fachkonzept.Gruppenphase;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Die Klasse ControllerGUI ist das Hauptfenster der Anwendung und steuert die
 * Interaktion mit dem Benutzer
 * 
 * @author T.Benning
 *
 */
public class ControllerGUI extends Application implements Beobachter {
	@FXML
	private MenuBar oben;
	@FXML
	private Pane mitte;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label labelText, labelZeit,labelAktuell,labelArt;

	private Pane controllerGraphenansichtPane;
	private ControllerGraphenansicht cg;
	private final Koordinator k = Koordinator.getKoordinator();
	private int alleTeams = -1;
	private boolean berechneAlleTeams = false;
	private int dauerAlleTeams;
	
	public static int aktiveSeite =0;
	public static final int seiteLigaErstellen = 1;
	public static final int seiteStartseite = 2;
	public static final int seiteTabelle = 3;
	public static final int seiteGraphenansicht = 4;
	public static final int seiteUebersichtSpieltage = 5;
	
	
	/**
	 * Wird von der Main beim Starten aufgerufen, muss Implementiert werden
	 */
	@Override
	public void start(Stage primaryStage) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI.fxml"));

		fxmlLoader.setController(new ControllerGUI());
		Pane root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bachelorarbeit - Tabellenrechner zur Vorhersage von Tabellenplätzen im Sport");
		primaryStage.getIcons().add(new Image("Images/Icon_FHDortmund.png"));
		primaryStage.show();
	}

	/**
	 * Wird aufgerufen sobald die fxml datei geladen wird
	 */
	@FXML
	private void initialize() {
		k.getAlgorithmus().addBeobachter(this);
		k.setMitte(mitte);
		k.setOben(oben);
		k.setProgressBar(progressBar);
		loadStartseiteFXML();
		// Am Anfang kann man noch nicht alle Menüpunkte auswählen
		oben.getMenus().get(0).setDisable(true);
		oben.getMenus().get(1).setDisable(true);
	}

	/**
	 * Ein text über die Projektarbeit der angezeigt wird
	 */
	@FXML
	private void ueberDieProjektarbeit() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Bachelorarbeit");
		alert.setHeaderText("Über die Bachelorarbeit");
		String text= "Ziel dieser Bachelorarbeit: \n"+
					" • Tabellenrechner für die Fußballbundesliga.\n"+
					" • Maximalen Platz nach Spieltag X berechnen.\n"+
					" • Minimalen Platz nach Spieltag X berechnen.\n"+ 
					" • Berechnung soll möglichst effizient sein.\n"+
					" • Ergebnis in eine aussagekräftige Grafik anzeigen.\n"+
					" • Spiele selber tippen: Einfluss auf Berechnungen nehmen.\n"+
					"      o Unwahrscheinliche Szenarien somit ausschließen, oder\n"+
					"      o Unwahrscheinliche Szenarien extra erzeugen.\n"+
					" • 4 Werte zu berechnen";
		alert.setContentText(text);
		alert.showAndWait();
	}

	@FXML
	private void hilfeAktuelleSeite()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Hilfe");
		String text=null;
		if(aktiveSeite==seiteStartseite)
		{
			alert.setHeaderText("Hilfe zur Startseite");
			text= "• Auf der Startseite können Sie sich eine Sportart aussuchen und dazu die vorhandene Saison und den Spieltag auswählen. Bestätigen wird über den gleichnamigen Button und dann werden die Informationen geladen\n"+
				  "• Sie können auch über die Schaltfläche \"Erstelle Liga\" eine eigene Liga erstellen \n"+
				  "• Selbst erstellte Ligen können bearbeitet werden über die Schaltfläche \"Bearbeite Liga\".\n"+
				  "• Selbst erstellte Ligen können durch drücken der Taste \"Entf\" gelöscht werden. Dabei werden alle Daten unwiederruflich gelöscht.";
		}
		if(aktiveSeite==seiteLigaErstellen)
		{
			alert.setHeaderText("Hilfe zur Liga erstellen");
			text= "• Eine Liga wird in 3 Schritte erstellt: \n"+
				  "1. Auswählen ob es sich um eine Meisterschaft (Fußballbundesliga) oder ein Gruppenspiel handelt(Weltmeisterschaft). Ausfüllen aller Felder und Eintragen der Mannschaften erforderlich. Dann auf \"weiter\" klicken um zu Schritt 2 zu gelangen. Sollte etwas falsch eingetragen sein bekommen Sie eine Meldung mit dem entsprechenden Fehler \n"+
				  "2. In Schritt 2 müssen Sie für die jeweiligen Spieltage oder Gruppen die Mannschaften eintragen. Dies geschieht per Drag and Drop indem man die Mannschaft bei angeklickter Maustaste in das entsprechende Feld zieht und dann los lässt. Sollte hierbei etwas schief laufen kann man über den großen Button den Spieltag nochmal neu erstellen. Es spielen immer die Mannschaften gegeneinander die auch nebeneinander aufgelistet sind. Mithilfe der Pfeil Tasten auf der Tastatur lässt sich die Position der Teams innerhalb der Liste noch ändern. Bei einer Meisterschafft können Sie auch bereits Ergebnisse in die beiden Felder Rechts eintragen. Wenn Sie alles eingetragen haben müssen sie weiter klicken um zu Schritt 3 zu gelangen\n"+
				  "3. In Schritt 3 werden angaben zur Punktevergabe und ggf. Sortierung gemacht. Wenn Sie mit ihren einstellungen zufrieden sind, bestätigen Sie diese und dann gelangen Sie zurück zur Startseite, wo Sie ihre neu erstellte Liga auswählen können.";
		}
		if(aktiveSeite==seiteTabelle)
		{
			alert.setHeaderText("Hilfe zur Tabelle");
			text= "• Die angezeigte Tabelle kann durch Klicken auf die Spalten anders sortiert werden. Außerdem kann durch ziehen der Spalten diese Vertauscht werden, wenn eine andere Ansicht lieber gewünscht ist.  \n";		
		}
		if(aktiveSeite==seiteGraphenansicht)
		{
			alert.setHeaderText("Hilfe zur Graphenansicht");
			text= "•   In der Graphenansicht sehen Sie auf der Linken Seite einen kurzen Überblick der Mannschaften und ihrer Platzierung. Durch klicken auf die Mannschaft wird deren Maximaler und Minimaler Tabellenplatz ermittelt und im Graphen angezeigt. Durch erneutes klicken derselben Mannschaft werden die Linien wieder entfernt. \n"+
			"• Man kann anstatt des Maximalen Wertes auch den Max-Min Wert ermitteln, das bedeutet die Mannschaft selber spielt maximal aber die anderen mannschaften spielen so das die aktuelle Mannschaft trotzdem nur den Minimalen platz erreichen kann. Genau umgekehrt funktioniert dies auch für die Auswahl zwischen Minimum und Min-Max. \n"+
					"• Ganz Rechts befindet sich alte Ergebnisse der letzten Spieltage und man kann für noch nicht gespielte Spiele Ergebnisse tippen, um so Einfluss auf die berechnungen zu nehmen.";		
		}
		if(aktiveSeite==seiteUebersichtSpieltage)
		{
			alert.setHeaderText("Hilfe zur Übersicht der Spieltage");
			text= " Hier finden Sie eine Übersicht der Spieltage. Mit den Button Vor und Zurück können Sie die Spieltage wechseln";		
		}
		if(aktiveSeite==0)
		{
			alert.setHeaderText("Hilfe");
			text= "• Es ist zurzeit keine Hilfe vorhanden  \n";		
		}
		
		alert.setContentText(text);
		alert.showAndWait();
	}
	/**
	 * Läd die Aktuelle Tabelle
	 */
	@FXML
	private void aktuelleTabelle() {
		entferneVorherigenInhalt();

		FXMLLoader fxmlLoader=null;
		if(k.getLiga().getAustragungsart() instanceof Meisterschaft)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("Tabelle.fxml"));
			fxmlLoader.setController(new ControllerTabelle());
	
		}
		if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("TabelleGruppenphase.fxml"));
			fxmlLoader.setController(new ControllerTabelleGruppenphase());
		}


		Pane p = null;
		try {
			p = fxmlLoader.load();
		} catch (IOException ex) {
			System.out.println("Fehler beim Laden der Tabelle.fxml");
			ex.printStackTrace();
		}
		

		p.setTranslateX(225);

		mitte.getChildren().add(p);
	}
	
	@FXML
	private void uebersichtSpieltage()
	{
		entferneVorherigenInhalt();

		FXMLLoader fxmlLoader=null;
		if(k.getLiga().getAustragungsart() instanceof Meisterschaft)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("UebersichtSpieltage.fxml"));
			fxmlLoader.setController(new ControllerUebersichtSpieltage());
	
		}
		if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("UebersichtSpieltageGruppenphase.fxml"));
			fxmlLoader.setController(new ControllerUebersichtSpieltageGruppenphase());
		}


		Pane p = null;
		try {
			p = fxmlLoader.load();
		} catch (IOException ex) {
			System.out.println("Fehler beim Laden der Tabelle.fxml");
			ex.printStackTrace();
		}
		

		p.setTranslateX(225.0);

		mitte.getChildren().add(p);
	}

	/**
	 * läd die Graphenansicht
	 */
	@FXML
	private void graphenansicht() {
		entferneVorherigenInhalt();
		
		FXMLLoader fxmlLoader=null;
		if(k.getLiga().getAustragungsart() instanceof Meisterschaft)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("Graphenansicht.fxml"));
			fxmlLoader.setController(cg = new ControllerGraphenansicht());
		}
		if(k.getLiga().getAustragungsart() instanceof Europameisterschaft)
		{
			fxmlLoader = new FXMLLoader(getClass().getResource("GraphenansichtGruppen.fxml"));
			fxmlLoader.setController(cg = new ControllerGraphenansichtGruppen());
		}
		
		controllerGraphenansichtPane = null;
		try {
			controllerGraphenansichtPane = fxmlLoader.load();
		} catch (IOException ex) {
			System.out.println("Fehler beim Laden der Graphenansicht.fxml");
			ex.printStackTrace();
		}
		
		mitte.getChildren().addAll(controllerGraphenansichtPane);
	}
	
	@FXML
	private void stoppeAktuelleMessung()
	{
		System.out.println("Stopp 1");
		if(k.getAlgorithmus()!=null)
		{
			System.out.println("Stopp 2");
			if(k.getAlgorithmus().berechnungen!=null)
			{
				System.out.println("Stopp 3");
				alleTeams=-1;
				berechneAlleTeams=false;
				k.getAlgorithmus().task.cancel();
				//k.getAlgorithmus().berechnungen.stop();
				System.out.println("Stopp 4");
				
			}
		}
	}

	/**
	 * geht zurück zur Startseite
	 */
	@FXML
	private void zurStartseite() {
		// dabei werden einige Elemente wieder deaktiviert
		
		stoppeAktuelleMessung();
		oben.getMenus().get(0).setDisable(true);
		oben.getMenus().get(1).setDisable(true);
		entferneVorherigenInhalt();
		labelText.setText("");
		labelZeit.setText("");
		labelAktuell.setText("");
		if(k.getAlgorithmus().getTask()!=null)
		{
			k.getAlgorithmus().getTask().cancel();
		}
		loadStartseiteFXML();
	}

	/**
	 * ruft funktion auf zum Berechnen aller Maximums und Minimums
	 */
	@FXML
	public void berechneMaximumMinimumAlleTeams() {
		if (alleTeams == -1) {
			entferneVorherigenInhalt();
			dauerAlleTeams=0;
			berechneAlleTeams = true;
			alleTeams = 1;
			k.getAlgorithmus().starteAlgorithmen(k.getAktiveLiga().getTeams()[alleTeams]);
		} else {
			k.getAlgorithmus().starteAlgorithmen(k.getAktiveLiga().getTeams()[alleTeams]);
		
		}
	}
	

	/**
	 * läd die Startseite
	 */
	public void loadStartseiteFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
		fxmlLoader.setController(new ControllerStartseite(this));
		Pane p = null;
		try {
			p = fxmlLoader.load();
		} catch (IOException ex) {
			System.out.println("Fehler beim Laden der Startseite.fxml");
			ex.printStackTrace();
		}
		p.setTranslateX(225);
		mitte.getChildren().add(p);
	}

	/**
	 * entfernt den vorherigen Inhalt aus der Pane in der Mitte
	 * 
	 */
	public void entferneVorherigenInhalt() {
		// wichtig dabei ist von hinten nach vorne zu löschen!
		for (int i = mitte.getChildren().size(); i > 0; i--) {			
			//wenn es sich dabei um die Graphenansicht handelt, wird diese als Beobachter
			//entfernt und auf null gesetzt
			if(mitte.getChildren().get(i-1)==controllerGraphenansichtPane)
			{
				cg.removeBeobachter();
				cg=null;
			}
			mitte.getChildren().remove(i - 1);
		}
	}

	/**
	 * MAIN
	 * 
	 * @param args
	 *            MAIN
	 */
	public static void main(String[] args) {

		launch(args);
	}

	/**
	 * @param t das Team welches Informationen zum aktualisieren enthält
	 */
	@Override
	public void update(Team t) {
		labelAktuell.setText("");
		labelText.setText("Zuletzt: " + t.getName());
		zeitUmrechner(k.getAlgorithmus().getDauerListe()[t.getPlatzierung()-1] );
		if (berechneAlleTeams) {
			dauerAlleTeams+=k.getAlgorithmus().getDauer();
			alleTeams++;
			if((alleTeams)%k.getAktiveLiga().getTeams().length==0)//wenn alle Teams berechnet wurden
			{
				labelAktuell.setText("");
				labelText.setText("Alle Teams fertig!   ");
				zeitUmrechner(dauerAlleTeams);
				alleTeams=-1;
				berechneAlleTeams=false;
				return ;				
			}else{
			berechneMaximumMinimumAlleTeams();
			}
		}
	}

	/**
	 * @param t das Team, das informationen zur aktualisierung enthält
	 */
	@Override
	public void updateAnfang(Team t) {
		labelAktuell.setText("Berechnet wird: "+t.getName());		
	}
	
	/**
	 * Berechnet anhand der milisekunden in sekunden und minuten um.
	 * @param ms die milisekunden die umgerechnet werden sollen
	 */
	private void zeitUmrechner(long ms)
	{
		int minute=(int)(ms/1000)/60;
		int sekunde=(int) (ms/1000);
		if(minute >= 1)
		{
			if(sekunde%60<10){
				labelZeit.setText("   Benötigte Zeit: " + minute + ",0"+ sekunde%60+" Minuten");
			}else{
				labelZeit.setText("   Benötigte Zeit: " + minute + ","+ sekunde%60+" Minuten");
			}
		}else {
			if(sekunde >= 1)
			{
				labelZeit.setText("   Benötigte Zeit: " + sekunde + ","+ ms%1000+" Sekunden");
			} else{
				labelZeit.setText("   Benötigte Zeit: " + ms + " ms");
			}
		}		
	}

	@Override
	public void updateCancelled(Team t) {
		labelAktuell.setText("Abgebrochen wurde: "+t.getName());
		
	}


}
