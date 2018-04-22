package GUI;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Fachkonzept.Koordinator;
import Fachkonzept.Liga;
import Fachkonzept.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ControllerTabelle {
	private final Koordinator k=Koordinator.getKoordinator();
	@FXML
	TableView<Team> tabelle;
	@FXML
	TableColumn<Team, String> colTeam,colPunkte;
	@FXML
	TableColumn<Team, Integer> colPlatz, colDifferenz, colSiege, colUnent, colNiederlage;
	@FXML
	Label labelSpieltag;
	@FXML
	Pane paneLegende;
	@FXML
	FlowPane vBoxLegende;
	

	@FXML
	public void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteTabelle;
		labelSpieltag.setText("Tabelle nach Spieltag "+ k.getLiga().getAustragungsart().getGespieltBisTag());
		
		setColumnsValue();
		setColumnsStyle();
	}

	private void setColumnsStyle()
	{
		if(k.getAktiveLiga().getLiga().equals(Liga.ersteFußballBundesliga))
        {
		   	 vBoxLegende.getChildren().add(new Label("Legende: "));
		   	 Label l5= new Label("Champions League Teilnahme");		   
		   	 l5.setStyle("-fx-background-color: limegreen;");
		   	 Label l4= new Label("Champions League Quali.");		   
		   	 l4.setStyle("-fx-background-color: lime;");
		   	 Label l3= new Label("Europa League Teilnahme");		   
		   	 l3.setStyle("-fx-background-color: DeepSkyBlue;");
		   	 Label l1= new Label("Relegationsplatz");		   
		   	 l1.setStyle("-fx-background-color: khaki;");
		   	 Label l2= new Label("Abstiegsplatz");		   
		   	 l2.setStyle("-fx-background-color: salmon;");
		   	 vBoxLegende.getChildren().add(l5);
		   	 vBoxLegende.getChildren().add(l4);
		   	 vBoxLegende.getChildren().add(l3);
		   	 vBoxLegende.getChildren().add(l1);
		   	 vBoxLegende.getChildren().add(l2);
        }
		if(k.getAktiveLiga().getLiga().equals(Liga.zweiteFußballBundesliga))
        {
		   	 vBoxLegende.getChildren().add(new Label("Legende: "));
		   	 Label l5= new Label("Aufstiegsplatz");		   
		   	 l5.setStyle("-fx-background-color: limegreen;");
		   	 Label l4= new Label("Aufstieg Relegation");		   
		   	 l4.setStyle("-fx-background-color: lime;");		   	 
		   	 Label l1= new Label("Abstieg Relegation");		   
		   	 l1.setStyle("-fx-background-color: khaki;");
		   	 Label l2= new Label("Abstiegsplatz");		   
		   	 l2.setStyle("-fx-background-color: salmon;");
		   	 vBoxLegende.getChildren().add(l5);
		   	 vBoxLegende.getChildren().add(l4);
		   	 vBoxLegende.getChildren().add(l1);
		   	 vBoxLegende.getChildren().add(l2);
        }
		if(k.getAktiveLiga().getLiga().equals("Handball"))
        {
		   	 vBoxLegende.getChildren().add(new Label("Legende: "));
		   	 Label l3= new Label("Qualifikation für die EHF Champions League");		   
		   	 l3.setStyle("-fx-background-color: limegreen;");
		   	 Label l1= new Label("Qualifikation für den EHF-Cup");		   
		   	 l1.setStyle("-fx-background-color: lime;");
		   	 Label l2= new Label("Abstiegsplatz");		   
		   	 l2.setStyle("-fx-background-color: salmon;");
		   	 vBoxLegende.getChildren().add(l3);
		   	 vBoxLegende.getChildren().add(l1);
		   	 vBoxLegende.getChildren().add(l2);
        }
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
		                 if(k.getAktiveLiga().getLiga().equals(Liga.ersteFußballBundesliga))
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
		                 if(k.getAktiveLiga().getLiga().equals(Liga.zweiteFußballBundesliga))
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
	/**
	 * setzt die Werte der Teams in die Tabelle
	 * @param teams die Teams die in der Tabelle angezeigt werden sollen
	 * @return die Tabellenansicht
	 */
	public TableView<Team> setColumnsValue() {
		Team[] teams=k.getLiga().getTeams();
		ObservableList<Team> t = FXCollections.observableArrayList();

		List<Team> listeSortieren=Arrays.asList(teams);
		Collections.sort( listeSortieren, k.getLiga().getAustragungsart().getComparatorChain() );
		for (int i = 0; i < listeSortieren.size(); i++) {
			t.add(listeSortieren.get(i));
		}
		colPlatz.setCellValueFactory(new PropertyValueFactory<Team, Integer>("platzierung"));
		colTeam.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
		colPunkte.setCellValueFactory(new PropertyValueFactory<Team, String>("punkteS"));	
		colDifferenz.setCellValueFactory(new PropertyValueFactory<Team, Integer>("differenz"));
		colSiege.setCellValueFactory(new PropertyValueFactory<Team, Integer>("siege"));
		colUnent.setCellValueFactory(new PropertyValueFactory<Team, Integer>("unentschieden"));
		colNiederlage.setCellValueFactory(new PropertyValueFactory<Team, Integer>("niederlagen"));
		tabelle.setItems(t);
		return tabelle;
	}
}
