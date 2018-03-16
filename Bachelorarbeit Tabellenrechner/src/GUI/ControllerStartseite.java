package GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Optional;

import Datenhaltung.DatenzugriffJSON;
import Fachkonzept.Gruppenphase;
import Fachkonzept.Koordinator;
import Fachkonzept.Liga;
import Fachkonzept.Meisterschaft;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class ControllerStartseite{
	@FXML
	public ListView<String> sportarten;
	@FXML
	public ChoiceBox<Integer> saison,spieltag;
	
	private Koordinator k=Koordinator.getKoordinator();
	private ControllerGUI cGUI;
	private ObservableList<Integer> tag;
	private ObservableList<String> sport;
	private ObservableList<Integer> jahr;
	private static File fileEigeneSporarten=new File("eigeneSportarten.ser");
	
	public ControllerStartseite(ControllerGUI cGUI)
	{
		this.cGUI=cGUI;

	}
	
	@FXML
	public void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteStartseite;
		sportarten.setOnKeyPressed(onKeyPressedEventHandler);
		
		sport = FXCollections.observableArrayList("1. Fußballbundesliga","2. Fußballbundesliga");
		
		sportarten.setItems(sport);
	
		sportarten.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//if(newValue.equals("1. Fußballbundesliga"))
				//{
					spieltag.setDisable(false);
					jahr= FXCollections.observableArrayList(2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,2016,2017);
					tag= FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34);
					saison.setItems(jahr);
					saison.getSelectionModel().select(11);
					spieltag.setItems(tag);
	        		spieltag.getSelectionModel().selectLast();
				//} 			
			}	
		});
		//http://docs.oracle.com/javafx/2/ui_controls/ChoiceBoxSample.java.html
		saison.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Number>() 
		{
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, Number value, Number new_value) 
            {
            	if(!sportarten.getSelectionModel().getSelectedItem().equals("1. Fußballbundesliga")&&
            	   !sportarten.getSelectionModel().getSelectedItem().equals("2. Fußballbundesliga")) return;
            	if(new_value==null) return;

            	if(new_value.intValue()==2017)
        		{
            		int letzterSpieltag=-1;
            		if(sportarten.getSelectionModel().getSelectedItem().equals("1. Fußballbundesliga"))
            		{
            			letzterSpieltag=DatenzugriffJSON.aktuellerSpieltag("bl1");
            		}
        			if(sportarten.getSelectionModel().getSelectedItem().equals("2. Fußballbundesliga"))
        			{
        				letzterSpieltag=DatenzugriffJSON.aktuellerSpieltag("bl2");
        			}
        			if(letzterSpieltag==-1)
        			{
        				Alert alert = new Alert(AlertType.ERROR);
        				alert.setTitle("Error");
        				alert.setHeaderText("ERROR");
        				alert.setContentText("Aktueller Spieltag konnte nicht ermittelt werden. Überprüfen Sie Ihre Internetverbindung!");
        				alert.showAndWait();
        				saison.getSelectionModel().select(11);
        			}else {
        				int t=letzterSpieltag;
        				if(t==0){
							tag= FXCollections.observableArrayList(1);
						}
						if(t==1){
							tag= FXCollections.observableArrayList(t);
						}
						if(t==2){
							tag= FXCollections.observableArrayList(t-1,t);
						}
						if(t==3){
							tag= FXCollections.observableArrayList(t-2,t-1,t);
						}
						if(t==4){
							tag= FXCollections.observableArrayList(t-3,t-2,t-1,t);
						}
						if(t==5){
							tag= FXCollections.observableArrayList(t-4,t-3,t-2,t-1,t);
						}
						if(t==6){
							tag= FXCollections.observableArrayList(t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==7){
							tag= FXCollections.observableArrayList(t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==8){
							tag= FXCollections.observableArrayList(t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==9){
							tag= FXCollections.observableArrayList(t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==10){
							tag= FXCollections.observableArrayList(t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==11){
							tag= FXCollections.observableArrayList(t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==12){
							tag= FXCollections.observableArrayList(t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==13){
							tag= FXCollections.observableArrayList(t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==14){
							tag= FXCollections.observableArrayList(t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==15){
							tag= FXCollections.observableArrayList(t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==16){
							tag= FXCollections.observableArrayList(t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==17){
							tag= FXCollections.observableArrayList(t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==18){
							tag= FXCollections.observableArrayList(t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==19){
							tag= FXCollections.observableArrayList(t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==20){
							tag= FXCollections.observableArrayList(t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==21){
							tag= FXCollections.observableArrayList(t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==22){
							tag= FXCollections.observableArrayList(t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==23){
							tag= FXCollections.observableArrayList(t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==24){
							tag= FXCollections.observableArrayList(t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==25){
							tag= FXCollections.observableArrayList(t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==26){
							tag= FXCollections.observableArrayList(t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==27){
							tag= FXCollections.observableArrayList(t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==28){
							tag= FXCollections.observableArrayList(t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==29){
							tag= FXCollections.observableArrayList(t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==30){
							tag= FXCollections.observableArrayList(t-29,t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==31){
							tag= FXCollections.observableArrayList(t-30,t-29,t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==32){
							tag= FXCollections.observableArrayList(t-31,t-30,t-29,t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==33){
							tag= FXCollections.observableArrayList(t-32,t-31,t-30,t-29,t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
						if(t==34){
							tag= FXCollections.observableArrayList(t-33,t-32,t-31,t-30,t-29,t-28,t-27,t-26,t-25,t-24,t-23,t-22,t-21,t-20,t-19,t-18,t-17,t-16,t-15,t-14,t-13,t-12,t-11,t-10,t-9,t-8,t-7,t-6,t-5,t-4,t-3,t-2,t-1,t);
						}
        			}    			
        		}
        		spieltag.setItems(tag);
        		spieltag.getSelectionModel().selectLast();
            }	
		});

	}
	
	@FXML
	public void auswaehlen()
	{
		boolean error=false;
		if(sportarten.getSelectionModel().getSelectedItem()==null ||
			(int)saison.getSelectionModel().getSelectedItem()==0) return;
		           
        try {
    		if(sportarten.getSelectionModel().getSelectedItem().equals("1. Fußballbundesliga")||
    		   sportarten.getSelectionModel().getSelectedItem().equals("2. Fußballbundesliga"))
    		{
    			if(sportarten.getSelectionModel().getSelectedItem().equals("1. Fußballbundesliga")){
        			k.setLiga("1. Fußballbundesliga",
    						(int) saison.getSelectionModel().getSelectedItem(),
    						(int) spieltag.getSelectionModel().getSelectedItem());
    			}
    			if(sportarten.getSelectionModel().getSelectedItem().equals("2. Fußballbundesliga")){
        			k.setLiga("2. Fußballbundesliga",
    						(int) saison.getSelectionModel().getSelectedItem(),
    						(int) spieltag.getSelectionModel().getSelectedItem());
    			}
    		}
		} catch (IOException e) {
			e.printStackTrace();
			error=true;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ERROR");
			alert.setContentText("Daten konnten weder lokal geladen noch aus dem Internet bezogen werden. Überprüfen Sie Ihre Internetverbindung! Ggf. benötigen Sie ein SSL-Zertifikat von openligadb.");

			alert.showAndWait();
		}
        if(!error) 
        {
    		for(int i=k.getMitte().getChildren().size();i>0;i--)
    		{
    			k.getMitte().getChildren().remove(i-1);
    		}
    		aktuelleTabelle();
            k.getOben().getMenus().get(0).setDisable(false);
            k.getOben().getMenus().get(1).setDisable(false);
        }
	}
	
	private void aktuelleTabelle() {
		

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

		k.getMitte().getChildren().add(p);
	}
	
	
	EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>(){
		@Override
		public void handle(KeyEvent key) {
			if(key.getCode()==KeyCode.DELETE)
			{
				if(sportarten.getSelectionModel().getSelectedItem().equals("1. Fußballbundesliga")||
				   sportarten.getSelectionModel().getSelectedItem().equals("1. Handballbundesliga"))
				{
					return;
				}else{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Bestätigen");
					String text= "Es werden alle dazugehörigen Ordner und Dateien gelöscht. Sicher?";
					alert.setContentText(text);
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){		
						String ligaName=sportarten.getSelectionModel().getSelectedItem();
						String path=System.getProperty("user.dir")+"/"+"src/Ergebnisse/"+ligaName;
						File ordner=new File(path);
						File ordnerSaison=new File(path+"/"+saison.getSelectionModel().getSelectedItem());
						for(int i=ordner.listFiles().length-1;i>=0;i--)
						{
							for(int j=ordnerSaison.listFiles().length-1; j>=0; j--)
							{
								ordnerSaison.listFiles()[j].delete();
							}
							ordner.listFiles()[i].delete();
						}
						if(!ordner.delete())
						{
							return;
						}
						
						sportarten.getItems().remove(sportarten.getSelectionModel().getSelectedItem());	
					} else {
					   return;
					}
				
				}
			}
		}	
	};
}
