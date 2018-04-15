package GUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Fachkonzept.BranchAndBound;
import Fachkonzept.Koordinator;
import Fachkonzept.Team;
import Fachkonzept.Zaehlweise;
import Fachkonzept.Algorithmus;
import Fachkonzept.IBeobachter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * ControllerGraphenansicht steuert die Interaktion mit der Graphenansicht.
 * 
 * @author T.Benning
 *
 */
public class ControllerGraphenansicht implements IBeobachter {
	protected final Koordinator k = Koordinator.getKoordinator();
	private int spieltag;
	private int anzahlTeams;
	private LinkedList<Team> ausgewaehlt = new LinkedList<Team>(); // Die
																	// makierten
																	// Teams in
																	// der
																	// Tabelle
	private LinkedList<TextField> tfTipps;
	private int[] tipps;
	private boolean getippt=false;

	@FXML
	private TableView<Team> tabelle;
	@FXML
	private TableColumn<Team, String> colTeam;
	@FXML
	private TableColumn<Team, Integer> colPlatz;
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private NumberAxis xAxis,yAxis;
	@FXML
	private Label teamName, labelSpieltag;
	@FXML
	private Pane paneSpiele;
	@FXML
	public Button buttonZ,buttonV;


	/**
	 * Konstruktor
	 */
	public ControllerGraphenansicht() {
		anzahlTeams=k.getAktiveLiga().getTeams().length;
		tipps = new int[k.getAktiveLiga().getAnzahlAusstehend()]; // es können sie
																// viele tipps
																// erstellt
																// werden wie es
																// ausstehende
																// Spiele gibt
	}

	/**
	 * entfernt den Beobachter aus der Beobachterliste
	 */
	public void removeBeobachter()
	{
		k.getAlgorithmus().removeBeobachter(this);
	}
	/**
	 * Setzt die Werte in der Tabelle
	 * 
	 * @param teams
	 *            die Teams die in der Tabelle angezeigt werden sollen
	 * @return Die Tabellenansicht
	 */
	public TableView<Team> setColumnsValue(Team[] teams) {
		ObservableList<Team> t = FXCollections.observableArrayList();
		List<Team> listeSortieren=Arrays.asList(teams);
		
		Collections.sort( listeSortieren, k.getLiga().getAustragungsart().getComparatorChain() );
		
		for (int i = 0; i < listeSortieren.size(); i++) {
			t.add(listeSortieren.get(i));
		}
		colPlatz.setCellValueFactory(new PropertyValueFactory<Team, Integer>("platzierung"));
		colTeam.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
		tabelle.setItems(t);
		return tabelle;
	}

	/**
	 * wird aufgerufen sobald die FXML datei geladen wird
	 */
	@FXML
	protected void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteGraphenansicht;
		setColumnsStyle();
		setColumnsValue(k.getAktiveLiga().getTeams());

		k.getAlgorithmus().addBeobachter(this);
		tabelle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // erlaubt
																				// mehrfachauswahl
																				// in
																				// der
																				// tabelle
		colPlatz.setSortable(false);
		colTeam.setSortable(false);
		spieltag =  k.getAktiveLiga().getAustragungsart().getGespieltBisTag();// der letzte gespielte
														// tag
		xAxis.setLowerBound(spieltag);// der Graph soll an den Spieltag beginnen
										// wo das letzte spiel gespielt wurde
		xAxis.setUpperBound((anzahlTeams-1)*2); //maximale anzahl von Spieltagen
		//yAxis.setUpperBound(anzahlTeams*(-1));
		yAxis.setLowerBound(anzahlTeams*(-1));
		labelSpieltag.setText("Spiele am Tag " + (spieltag) + ":");
		zeigeSpieltag(spieltag - 1); 
		for (int i = 0; i < tfTipps.size(); i++) {
			tfTipps.get(i).setText("");
		}
		for (int i = 0; i < tipps.length; i++) {
			tipps[i] = -1;
		}
		Algorithmus.setTipps(tipps);
	}

	private void setColumnsStyle()
	{
		colPlatz.setCellFactory(column -> {
		    return new TableCell<Team, Integer>() {
		        @Override
		        protected void updateItem(Integer item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            } else {
		            	setText(item+"");
		            	 setTextFill(Color.BLACK);
		                 setStyle( "-fx-alignment: center");
		                 if(k.getAktiveLiga().getLiga().equals("Fußball"))
		                 {
		                	 switch(item)
			                 {
				                 case 1:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: limegreen;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 2:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: limegreen;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 3:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: limegreen;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 4:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: lime;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 5:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: DeepSkyBlue;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 6:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: DeepSkyBlue;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 16:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: khaki;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 17:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: salmon;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 18:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: salmon;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 default:setTextFill(Color.BLACK);
				                 		setStyle("-fx-alignment: center");
				                 		break;
			                 }
		                 }
		                 if(k.getAktiveLiga().getLiga().equals("Handball"))
		                 {
		                	 switch(item)
			                 {
				                 case 1:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: limegreen;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 2:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: limegreen;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 3:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: lime;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 4:setTextFill(Color.BLACK);
				                 		setStyle("-fx-background-color: lime;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 16:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: salmon;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 17:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: salmon;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 case 18:setTextFill(Color.WHITE);
				                 		setStyle("-fx-background-color: salmon;"
				                 				+ "-fx-alignment: center");
				                 		break;
				                 default:setTextFill(Color.BLACK);
				                 		setStyle("-fx-alignment: center");
				                 		break;
			                 }
		                 }
		            }
		        }
		    };
		});
	}
	
	/**
	 * berechnet Das minimum und maximum des teams, es sei denn es wurde schon
	 * berechnet dann wird die linie einfach angezeigt
	 * 
	 * @param t
	 *            das Team welches das Maximum und Minimum berechnet/angezeigt werden soll
	 */
	private void berechneMaxMin(Team t) {
		// "Durchlaufe" alle Teams
		for (int i = 0; i < k.getAktiveLiga().getTeams().length; i++) {
			// wenn das ausgewählte Team t dem original Team entspricht
			if (t.getName().equals(k.getAktiveLiga().getTeams()[i].getName())) {
				// dann wird geprüft ob bereits ein Maximum/Minimum berechnet
				// wurde oder ob getippt wurde

				if(t.getMaxPlatzSpieltag().isEmpty()||t.getMinPlatzSpieltag().isEmpty()||getippt)
				{
					k.getAlgorithmus().starteAlgorithmen(t);
				}else{
					k.getAlgorithmus().notifyAlleBeobachter(t);
				}
			}
		}
	}

	/**
	 * wird ausgeführt wenn man auf die Tabelle klickt
	 */
	@FXML
	private void onMouseClicked() {
		Team team = tabelle.getSelectionModel().getSelectedItem();
		if (team == null)
			return; // wenn nichts ausgewählt wurde dann abbrechen
		// "durchlaufe" alle bereits ausgewählten Teams
		for (int i = 0; i < ausgewaehlt.size(); i++) {
			
			// wenn das angeklickte team bereits ausgewählt ist, dann entferne
			// es aus der Graphik
			// und aus der auswahlliste
			if (ausgewaehlt.get(i).equals(team)) {
				for (int j = lineChart.getData().size() - 1; j >= 0; j--) {
					if(lineChart.getData().get(j).getName().contains(team.getName()))
					{
						lineChart.getData().remove(j);
					}
				}
				ausgewaehlt.remove(i);
				makieren();
				return;
			} // ende if
		} // ende for

		// ansonsten füge die Linien des Teams in die Grafik ein und füge es in
		// die Liste der ausgewählten
		// teams hinzu und makiere dies.
		berechneMaxMin(team);
		ausgewaehlt.add(team);
		makieren();
	}

	/**
	 * makiert alle ausgewählten teams
	 */
	private void makieren() {
		// zuerst werden alle makierungen aufgehoben
		tabelle.getSelectionModel().clearSelection();
		for (int i = 0; i < ausgewaehlt.size(); i++) {
			// um dann die ausgewählten wieder zu makieren
			// das wird so gemacht da die Methode ebenfalls aufgerufen wird,
			// wenn etwas wieder
			// angeklickt wurde und somit "De-Makiert" werden soll.
			tabelle.getSelectionModel().select(ausgewaehlt.get(i).getPlatzierung() - 1);
		}
	}

	/**
	 * wenn der Button Vor geklickt wird, wird der nächste Spieltag angezeigt
	 */
	@FXML
	protected void buttonVor() {
		tippsErstellen();
		spieltag++;
		if (spieltag >= k.getAktiveLiga().getAustragungsart().getAnzahlSpieltage()){
			spieltag =k.getAktiveLiga().getAustragungsart().getAnzahlSpieltage();
		}
			
		setzeSpieltag();
		

	}

	/**
	 * Wenn der Button Zurueck geklickt wird, wird der vorherige Spieltag
	 * angezeigt
	 */
	@FXML
	private void buttonZurueck() {
		tippsErstellen();
		spieltag--;
		if (spieltag <= 0)
			spieltag = 1;
		setzeSpieltag();
	}

	/**
	 * Wenn der Button Reset geklickt wird, werden die Textfelder "geleert"
	 */
	@FXML
	private void buttonReset() {
		for (int i = 0; i < tfTipps.size(); i++) {
			tfTipps.get(i).setText("");
		}
		for (int i = 0; i < tipps.length; i++) {
			tipps[i] = -1;
		}

		Algorithmus.setTipps(tipps);

		// es werden die alten werte in der grafik angezeigt
		if (!ausgewaehlt.isEmpty()) {
			for (int i = ausgewaehlt.size() * 2 - 1; i >= 0; i--) {
				lineChart.getData().remove(i);
			}
			for (int i = 0; i < ausgewaehlt.size(); i++) {
				berechneMaxMin(ausgewaehlt.get(i));
			}
		}
		
		getippt=false;
	}

	/**
	 * wenn der Button Tippen geklickt wird, werden die Tipps für die Berechnung
	 * gesetzt und und die Grafik aktualisiert
	 *
	 */
	@FXML
	private void buttonTippen() {
		getippt=true;
		tippsErstellen();
		Algorithmus.setTipps(tipps);
		if (!ausgewaehlt.isEmpty()) {
			for (int i = ausgewaehlt.size() * 2 - 1; i >= 0; i--) {
				lineChart.getData().remove(i);
			}
			for (int i = 0; i < ausgewaehlt.size(); i++) {
				berechneMaxMin(ausgewaehlt.get(i));
			}
		}
	}

	/**
	 * Die Tipps werden gesetzt und darauf geachtet das nur zahlen eingegeben
	 * werden
	 */
	private void tippsErstellen() {
		int tag = berechneTag();

		for (int i = 0; i < tfTipps.size(); i++) {
			int tmp;
			// wenn eine zahl eingegeben wurde wird diese zu den tipps
			// hinzugefügt,
			// ansonsten wird das feld wieder "leer" gesetzt
			try {
				tmp = Integer.parseInt(tfTipps.get(i).getText());
				tipps[i + (tag * anzahlTeams)] = tmp;
			} catch (NumberFormatException e) {
				tfTipps.get(i).setText("");
				tipps[i + (tag * anzahlTeams)] = -1;
			}
			// wenn nur einseitig getippt wurde, wird die andere seite 0 getippt
			if (!tfTipps.get(i).getText().equals("")) {
				if (i % 2 == 0) {
					if (tfTipps.get(i + 1).getText().equals("")) {
						tfTipps.get(i + 1).setText("" + 0);
					}
				}
				if (i % 2 != 0) {
					if (tfTipps.get(i - 1).getText().equals("")) {
						tfTipps.get(i - 1).setText("" + 0);
					}
				}
			}
		}
	}

	/**
	 * zeigt den Spieltag im Label an
	 */
	protected void setzeSpieltag() {
		labelSpieltag.setText("Spiele am Tag " + spieltag + ":");
		zeigeSpieltag(spieltag - 1);
	}

	/**
	 * 
	 * @param tag
	 *            der Tag der angezeigt werden soll. (Beachte, dass die Liste
	 *            bei 0 anfängt und somit muss bei der übergabe immer -1
	 *            abgezogen werden)
	 */
	private void zeigeSpieltag(int tag) {
		// entferne zuerst alle elemente
		for (int i = paneSpiele.getChildren().size(); i > 0; i--) {
			paneSpiele.getChildren().remove(i - 1);
		}

		LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
		LinkedList<Label> spiele = new LinkedList<Label>();
		tfTipps = new LinkedList<TextField>();
		int abstand = -20; // der abstand zwischen den Spielpaarungen (Labels)

		for (int i = 0; i < alleSpiele.size(); i = i + 2) {
			int spieltag = (int) i / k.getAktiveLiga().getTeams().length; // gucken
																		// an
																		// welchen
																		// spieltag
																		// man
																		// sich
																		// befindet

			// wenn es der gewollte spieltag ist dann zeige die Paarungen davon
			// an
			if (tag == spieltag) {
				// und wenn dieses spiel bereits ausgetragen ist, zeige die
				// Ergebnisse davon an
				if (spieltag < k.getAktiveLiga().getAnzahlGespielt() / anzahlTeams) {
					try{
						spiele.add(new Label(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName() + " ("
								+ alleSpiele.get(i).getToreSpieltag(spieltag) + " : "
								+ alleSpiele.get(i + 1).getToreSpieltag(spieltag) + ")"));
					}catch(NullPointerException e)
					{
						try{
							spiele.add(
									new Label(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName() + " "));
						}catch(NullPointerException e1)
						{
							spiele.add(new Label("Spiel konnte nicht gefunden werden"));
						}
						
					}

				} else {
					// anonsten erstelle die Textfelder zum tippen der Spiele
					spiele.add(
							new Label(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName() + " "));
					tfTipps.add(new TextField());
					tfTipps.add(new TextField());
				}
			}
		}

		// setzte die Labels an die richtige position
		for (int i = 0; i < spiele.size(); i++) {
			spiele.get(i).setFont(new Font(11));
			spiele.get(i).setTranslateY(abstand += 30);
		}

		int x = berechneTag();
		abstand = 10; // der Abstand für die Textfelder

		// aktuallisiert alte textfelder, wenn diese erneut angezeigt werden
		// z.B durch das klicken von Vor oder Zurück
		for (int i = 0; i < tfTipps.size(); i = i + 2) {
			if (tipps[i + (x * anzahlTeams)] != -1) {
				tfTipps.get(i).setText("" + tipps[i + (x * anzahlTeams)]);
			}
			if (tipps[i + 1 + (x * anzahlTeams)] != -1) {
				tfTipps.get(i + 1).setText("" + tipps[i + 1 + (x * anzahlTeams)]);
			}
			tfTipps.get(i).setTranslateX(250);
			tfTipps.get(i).setTranslateY(abstand);
			tfTipps.get(i).setMaxWidth(30);

			tfTipps.get(i + 1).setTranslateX(300);
			tfTipps.get(i + 1).setTranslateY(abstand);
			tfTipps.get(i + 1).setMaxWidth(30);

			abstand += 30;
		}

		paneSpiele.getChildren().addAll(spiele);
		paneSpiele.getChildren().addAll(tfTipps);
	}
	
	/**
	 * 
	 * @param team das Team welches die Naiven Maximum Platz angezeigt werden soll
	 */
	private int berechneNaivenWertMAX(Team team)
	{
		int anzahlUebrigerSpieltage = ((k.getAktiveLiga().getAnzahlAusstehend() + k.getAktiveLiga().getAnzahlGespielt())
				/ k.getAktiveLiga().getTeams().length)-  k.getAktiveLiga().getAustragungsart().getGespieltBisTag();
		int maxPunkteNaiv=team.getPunkte()+Zaehlweise.PUNKTE_S*anzahlUebrigerSpieltage;
		int maxPlatzNaiv=0;
		for(int i=0;i<k.getAktiveLiga().getTeams().length;i++)
		{
			if(maxPunkteNaiv>=k.getAktiveLiga().getTeams()[i].getPunkte())
			{
				maxPlatzNaiv=i+1;
				break;
			}
		}		
		return maxPlatzNaiv;
	}
	
	/**
	 * 
	 * @param team das Team welches die Naiven Minimum Platz angezeigt werden soll
	 */
	private int berechneNaivenWertMIN(Team team)
	{
		int anzahlUebrigerSpieltage = ((k.getAktiveLiga().getAnzahlAusstehend() + k.getAktiveLiga().getAnzahlGespielt())
				/ k.getAktiveLiga().getTeams().length)-  k.getAktiveLiga().getAustragungsart().getGespieltBisTag();
		
		int minPunkteNaiv=team.getPunkte()+Zaehlweise.PUNKTE_N*anzahlUebrigerSpieltage;
		int minPlatzNaiv=0;
		for(int i=k.getAktiveLiga().getTeams().length-1;i>=0;i--)
		{
			if(minPunkteNaiv<=k.getAktiveLiga().getTeams()[i].getPunkte()+Zaehlweise.PUNKTE_S*anzahlUebrigerSpieltage)
			{
				minPlatzNaiv=i+1;
				break;
			}
		}
		return minPlatzNaiv;
	}
	
	/**
	 * 
	 * @return
	 */
	private int berechneTag() {
		int anzahlSpieltage = (k.getAktiveLiga().getAnzahlAusstehend() + k.getAktiveLiga().getAnzahlGespielt())
				/ k.getAktiveLiga().getTeams().length;
		int anzahl1 = anzahlSpieltage - spieltag + 1;
		int anzahl2 = k.getAktiveLiga().getAnzahlAusstehend() / k.getAktiveLiga().getTeams().length;// 4
																								// konstant
		int test = anzahl2 - anzahl1;
		return test;
	}

	/**
	 * Erstellt die Linien für das Maximum und Minimum des Teams
	 * @param t das Team für welches die Linien erstellt werden sollen
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void erstelleLinie(Team t) {
		XYChart.Series seriesMax = new XYChart.Series();
		XYChart.Series seriesMin = new XYChart.Series();

		seriesMax.setName(t.getName() + " MAX Naiv("+berechneNaivenWertMAX(t)+")");

		seriesMin.setName(t.getName() + " MIN Naiv("+berechneNaivenWertMIN(t)+")");

		

		// "Durchlaufe" alle Teams
		for (int i = 0; i < k.getAktiveLiga().getTeams().length; i++) {
			// wenn das ausgewählte Team t dem original Team entspricht
			if (t.getName().equals(k.getAktiveLiga().getTeams()[i].getName())) {

				// Füge erstmal die Punkte des zu letz Gespielten tages hinzu
				seriesMax.getData().add(new XYChart.Data(  k.getAktiveLiga().getAustragungsart().getGespieltBisTag(), t.getPlatzierung()*(-1)));
				seriesMin.getData().add(new XYChart.Data( k.getAktiveLiga().getAustragungsart().getGespieltBisTag(), t.getPlatzierung()*(-1)));

				// Füge alle Punkte der Maximalen Spieltage hinzu
				for (int x = 0; x < k.getAktiveLiga().getTeams()[i].getMaxPlatzSpieltag().size(); x++) {
					if(!(k.getAktiveLiga().getTeams()[i].getMaxPlatzSpieltag().get(x)==100)){
						
						seriesMax.getData().add(new XYChart.Data( k.getAktiveLiga().getAustragungsart().getGespieltBisTag() + 1 + x,
								+k.getAktiveLiga().getTeams()[i].getMaxPlatzSpieltag().get(x)));
					}
				}
				// Füge alle Punkte der Minimalen Spieltage hinzu
				for (int x = 0; x < k.getAktiveLiga().getTeams()[i].getMinPlatzSpieltag().size(); x++) {
					if(!(k.getAktiveLiga().getTeams()[i].getMinPlatzSpieltag().get(x)==100)){
						seriesMin.getData().add(new XYChart.Data(  k.getAktiveLiga().getAustragungsart().getGespieltBisTag() + 1 + x,
								+k.getAktiveLiga().getTeams()[i].getMinPlatzSpieltag().get(x)));
					}
					
				}
			}
		}
		// füge die linien zur Graphik hinzu
		lineChart.getData().add(seriesMax);
		lineChart.getData().add(seriesMin);
	}

	/**
	 *@param t erstellt den Verlauf für das Team t
	 */
	@Override
	public void update(Team t) {
		erstelleLinie(t);
	}

	@Override
	public void updateAnfang(Team t) {
	}

	@Override
	public void updateCancelled(Team t) {
		//ausgewaehlt.remove(t);
		//makieren();
		erstelleLinie(t);
	}
}
