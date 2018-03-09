package GUI;

import java.util.LinkedList;

import Fachkonzept.Koordinator;
import Fachkonzept.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;


public class ControllerUebersichtSpieltage {

	
	@FXML
	private Button buttonZ,buttonV;
	@FXML
	private Label labelSpieltag;
	@FXML
	private ListView<String> listViewSpiele;
	@FXML
	private ListView<String> listViewErg;

	
	private int anzahlTeams;
	private int spieltag;
	private ObservableList<String> list1 = FXCollections.observableArrayList();
	private ObservableList<String> list2 = FXCollections.observableArrayList();
	
	private final Koordinator k = Koordinator.getKoordinator();
	
	public ControllerUebersichtSpieltage() {
		anzahlTeams=k.getAktiveLiga().getTeams().length;
	}
	@FXML
	protected void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteUebersichtSpieltage;
		spieltag =  k.getAktiveLiga().getAustragungsart().getGespieltBisTag();
		labelSpieltag.setText("Spieltag: " + (spieltag) );
		zeigeSpieltag(spieltag - 1); 
	}
	
	private void zeigeSpieltag(int tag) {
		
			list1.clear();
			list2.clear();
			listViewSpiele.getItems().clear();
			listViewErg.getItems().clear();
		

		LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
		
		

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
						list1.add(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName());
						list2.add(" ("	+ alleSpiele.get(i).getToreSpieltag(spieltag) + " : "+ alleSpiele.get(i + 1).getToreSpieltag(spieltag) + ")");
						listViewSpiele.setItems(list1);
						listViewErg.setItems(list2);
						
					}catch(NullPointerException e)
					{
						list1.add("Spiel konnte nicht gefunden werden");
						list2.add("");
						listViewSpiele.setItems(list1);
						listViewErg.setItems(list2);
					}

				} else {
					list1.add(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName());	
					list2.add("");
					listViewSpiele.setItems(list1);
					listViewErg.setItems(list2);
				}
			}
		}
	}
	
	@FXML
	protected void buttonVor() {
		spieltag++;
		if (spieltag >= k.getAktiveLiga().getAustragungsart().getAnzahlSpieltage()){
			spieltag =k.getAktiveLiga().getAustragungsart().getAnzahlSpieltage();
		}
			
		setzeSpieltag();
		

	}
	
	@FXML
	private void buttonZurueck() {
		spieltag--;
		if (spieltag <= 0)
			spieltag = 1;
		setzeSpieltag();
	}
	
	protected void setzeSpieltag() {
		labelSpieltag.setText("Spieltag: " + spieltag);
		zeigeSpieltag(spieltag - 1);
	}
	
	
}
