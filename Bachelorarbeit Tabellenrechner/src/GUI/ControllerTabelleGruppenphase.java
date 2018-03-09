package GUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Fachkonzept.ComparatorChain;
import Fachkonzept.Gruppenphase;
import Fachkonzept.Koordinator;
import Fachkonzept.Team;
import Fachkonzept.ZaehlweisemitMinuspunkte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class ControllerTabelleGruppenphase {
	private final Koordinator k=Koordinator.getKoordinator();
	@FXML
	TableView<Team> tabelle;
	@FXML
	TableColumn<Team, String> colTeam, colPunkte;
	@FXML
	TableColumn<Team, Integer> colPlatz, colDifferenz, colSiege, colUnent, colNiederlage;
	@FXML
	Label labelSpieltag;
	@FXML
	Button buttonZurueck,buttonVor;
	
	private int gruppe;
	private char gruppeChar;
	private int anzahlGruppen;
	@FXML
	public void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteTabelle;
		buttonZurueck.setDisable(true);
		anzahlGruppen=((Gruppenphase) k.getLiga().getAustragungsart()).getAnzahlGruppen();
		gruppe=0;
		gruppeChar='A';
		labelSpieltag.setText("Gruppe: "+gruppeChar);
		k.setzeAktiveLiga(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[0]);
		setColumnsValue(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[0].getTeams());
		setColumnsStyle();
	}

	/**
	 * setzt die Werte der Teams in die Tabelle
	 * @param teams die Teams die in der Tabelle angezeigt werden sollen
	 * @return die Tabellenansicht
	 */
	public TableView<Team> setColumnsValue(Team[] teams) {
		ObservableList<Team> t = FXCollections.observableArrayList();
		List<Team> listeSortieren=Arrays.asList(teams);
		
		ComparatorChain.gruppe=this.gruppe;
		Collections.sort( listeSortieren, k.getLiga().getAustragungsart().getComparatorChain() );
		for (int i = 0; i < listeSortieren.size(); i++) {
			t.add(listeSortieren.get(i));
		}
		colPlatz.setCellValueFactory(new PropertyValueFactory<Team, Integer>("platzierung"));
		colTeam.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
		if(k.getAktiveLiga().getSportart().getZaehlweise() instanceof ZaehlweisemitMinuspunkte)
		{
			colPunkte.setCellValueFactory(new PropertyValueFactory<Team, String>("punkte_ggPunkte"));
		}else{
			colPunkte.setCellValueFactory(new PropertyValueFactory<Team, String>("punkteS"));
		}
		colDifferenz.setCellValueFactory(new PropertyValueFactory<Team, Integer>("differenz"));
		colSiege.setCellValueFactory(new PropertyValueFactory<Team, Integer>("siege"));
		colUnent.setCellValueFactory(new PropertyValueFactory<Team, Integer>("unentschieden"));
		colNiederlage.setCellValueFactory(new PropertyValueFactory<Team, Integer>("niederlagen"));
		tabelle.setItems(t);
		return tabelle;
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
		            }
		        }
		    };
		});
		
		colPunkte.setCellFactory(column -> {
		    return new TableCell<Team, String>() {
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            } else {
		            	setText(item+"");
		            	 setTextFill(Color.BLACK);
		                 setStyle( "-fx-alignment: center");          
		            }
		        }
		    };
		});
		colDifferenz.setCellFactory(column -> {
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
		                 setStyle("-fx-alignment: center");
		            }
		        }
		    };
		});
		colNiederlage.setCellFactory(column -> {
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
		                 setStyle("-fx-alignment: center");
		            }
		        }
		    };
		});
		colSiege.setCellFactory(column -> {
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
		                 setStyle("-fx-alignment: center");
		            }
		        }
		    };
		});
		colUnent.setCellFactory(column -> {
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
		                 setStyle("-fx-alignment: center");
		            }
		        }
		    };
		});
	}
	@FXML
	private void vor()
	{
		
		if (gruppe+1 >= anzahlGruppen){
			gruppe = anzahlGruppen;
		}else{
			gruppe++;
			gruppeChar++;
		}
			
		
		labelSpieltag.setText("Gruppe: "+gruppeChar);
		k.setzeAktiveLiga(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe]);
		setColumnsValue(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getTeams());
		if(gruppe==anzahlGruppen-1)
		{
			buttonVor.setDisable(true);
			buttonZurueck.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurueck.setDisable(false);
		}
	}
	
	@FXML
	private void zurueck()
	{
		gruppe--;
		if (gruppe < 0)
			gruppe = 0;
		gruppeChar--;
		labelSpieltag.setText("Gruppe: "+gruppeChar);
		k.setzeAktiveLiga(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe]);
		setColumnsValue(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getTeams());
		if(gruppe==0)
		{
			buttonZurueck.setDisable(true);
			buttonVor.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurueck.setDisable(false);
		}
	}
}
