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

import Fachkonzept.ComparatorChain;
import Fachkonzept.Europameisterschaft;
import Fachkonzept.Gruppenphase;
import Fachkonzept.Liga;
import Fachkonzept.Meisterschaft;
import Fachkonzept.SportartNeu;
import Fachkonzept.Zaehlweise;
import Fachkonzept.ZaehlweisemitMinuspunkte;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class ControllerLigaErstellen {
	@FXML
	private Pane panePunktevergabe, paneEigenePunktevergabe, paneMinuspunkte, paneSpieltage,paneGenerator,paneSchritt1,paneTipps;
	@FXML
	private FlowPane flowPaneLabels;
	@FXML
	private TextField tfLigaName, tfSaison, tfTeamName, tfSieg, tfNiederlage, tfUnentschieden, tfSieg_M, tfNiederlage_M,
			tfUnentschieden_M,tfAnzahlGruppenmitglieder;
	@FXML
	private ListView<String> listViewTeams, colTeam1, colTeam2;
	@FXML
	private Button buttonEntfernen,buttonZurück,buttonVor,buttonTeamsLoeschen;
	@FXML
	private Label labelAnzahlTeams, labelSpieltag,labelAnzahlGruppenmitglieder;
	@FXML
	private ChoiceBox<String> choiceBoxPunkte,choiceBoxMeisterschafft,choiceBoxSortierung;

	private ObservableList<String> teams = FXCollections.observableArrayList();
	private ObservableList<String> list1 = FXCollections.observableArrayList();
	private ObservableList<String> list2 = FXCollections.observableArrayList();

	private String ligaName;
	private int spieltag;
	private int anzahlSpieltage;
	private int anzahlTeams;
	private int saison;
	private int anzahlGruppenmitglieder;
	private double alteKoorMausX, alteKoorMausY;
	private double alteKoorObjX, alteKoorObjY;
	private File ordner,ordnerSaison, informationen;
	private LinkedList<Label> labels=new LinkedList<Label>();
	private LinkedList<TextField> tfTipps;
	private LinkedList<Integer> tipps=new LinkedList<Integer>();
	private LinkedList<ObservableList<String>> spielpaarungenHeim=new LinkedList<ObservableList<String>>();
	private LinkedList<ObservableList<String>> spielpaarungenAusw=new LinkedList<ObservableList<String>>();
	private ControllerGUI cGUI;
	//private Koordinator k=Koordinator.getKoordinator();
	private Liga liga=null;
	
	public ControllerLigaErstellen(ControllerGUI cGUI)
	{
		this.cGUI=cGUI;

	}
	public ControllerLigaErstellen(ControllerGUI cGUI, Liga liga)
	{
		this.cGUI=cGUI;
		this.liga=liga;
	}
	private void bearbeitungsmodus()
	{
		tfLigaName.setText(liga.getLiga());
		tfSaison.setText(""+liga.getJahr());
		ladeInfomationen(System.getProperty("user.dir")+"/"+"src/Ergebnisse/"+liga.getLiga()+"/"+tfSaison.getText());
		if(liga.getAustragungsart() instanceof Gruppenphase)
		{
			choiceBoxMeisterschafft.getSelectionModel().select(1);
			int x=0;
			for(int i=0; i<spielpaarungenHeim.size();i++)
			{
				for(int j=0; j<spielpaarungenHeim.get(i).size();j++)
				{
					tfTeamName.setText(spielpaarungenHeim.get(i).get(j));
					addTeam();
					x++;
				}
			}
			colTeam1.setItems(spielpaarungenHeim.get(0));
			int y=x/((Gruppenphase)liga.getAustragungsart()).getAnzahlGruppen();
			tfAnzahlGruppenmitglieder.setText(""+y);
		}
		
		if(liga.getAustragungsart() instanceof Meisterschaft)
		{
			choiceBoxMeisterschafft.getSelectionModel().select(0);
			for(int i=0; i<spielpaarungenHeim.get(0).size();i++)
			{
				tfTeamName.setText(spielpaarungenHeim.get(0).get(i));
				addTeam();
				tfTeamName.setText(spielpaarungenAusw.get(0).get(i));
				addTeam();
			}

			colTeam1.setItems(spielpaarungenHeim.get(0));
			colTeam2.setItems(spielpaarungenAusw.get(0));
		}
		
		weiterZuSchritt2();
		holeTipps();
	}
	@FXML
	public void initialize() {
		ControllerGUI.aktiveSeite=ControllerGUI.seiteLigaErstellen;
		spieltag=1;
		
		labelAnzahlGruppenmitglieder.setVisible(false);
		tfAnzahlGruppenmitglieder.setVisible(false);
		
		choiceBoxMeisterschafft.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("Meisterschaft"))
				{
					labelAnzahlGruppenmitglieder.setVisible(false);
					tfAnzahlGruppenmitglieder.setVisible(false);
				}else{
					labelAnzahlGruppenmitglieder.setVisible(true);
					tfAnzahlGruppenmitglieder.setVisible(true);
				}
			}
		});
		
		buttonEntfernen.setDisable(true);
		paneMinuspunkte.setDisable(true);

		paneGenerator.setVisible(false);	
		panePunktevergabe.setVisible(false);
		ObservableList<String> vorgabePunkte = FXCollections.observableArrayList("Fußball", "Handball");
		choiceBoxPunkte.setItems(vorgabePunkte);
		choiceBoxPunkte.getSelectionModel().select(0);
		
		ObservableList<String> vorgabe = FXCollections.observableArrayList("Meisterschaft", "Gruppenphasen (EM/WM/CL)");
		choiceBoxMeisterschafft.setItems(vorgabe);
		choiceBoxMeisterschafft.getSelectionModel().select(0);
		
		if(liga!=null)
		{
			bearbeitungsmodus();
		}
	}

	private void erstelleTippfelder()
	{
		tfTipps=new LinkedList<TextField>();
		for (int i = 0; i < anzahlTeams*anzahlSpieltage; i++) {
			tipps.add(-1);
		}
		for(int i=0; i<anzahlTeams;i++)
		{
			tfTipps.add(new TextField());
		}
		
		int abstand=0;
		// aktuallisiert alte textfelder, wenn diese erneut angezeigt werden
		// z.B durch das klicken von Vor oder Zurück
		for (int i = 0; i < tfTipps.size(); i = i + 2) {
			//tfTipps.get(i).setTranslateX(250);
			tfTipps.get(i).setTranslateY(abstand);
			tfTipps.get(i).setMaxWidth(30);

			tfTipps.get(i + 1).setTranslateX(40);
			tfTipps.get(i + 1).setTranslateY(abstand);
			tfTipps.get(i + 1).setMaxWidth(30);

			abstand += 30;
		}
		paneTipps.getChildren().addAll(tfTipps);
	}
	
	private void holeTipps()
	{
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)return;
		for(int i=0;i<tfTipps.size();i++)
		{
			tfTipps.get(i).setText("");
		}
		
		int x=spieltag-1;
		for (int i = 0; i < tfTipps.size(); i = i + 2) {
			if (tipps.get(i + (x * anzahlTeams))!= -1) {
				tfTipps.get(i).setText("" + tipps.get(i + (x * anzahlTeams)));
			}
			if (tipps.get(i + 1 + (x * anzahlTeams)) != -1) {
				tfTipps.get(i + 1).setText("" + tipps.get(i + 1 + (x * anzahlTeams)));
			}
		}
	}
	
	private void setzeTipps()
	{
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)return;
		int tag=spieltag-1;
		for (int i = 0; i < tfTipps.size(); i++) {
			int tmp;
			// wenn eine zahl eingegeben wurde wird diese zu den tipps
			// hinzugefügt,
			// ansonsten wird das feld wieder "leer" gesetzt
			try {
				tmp = Integer.parseInt(tfTipps.get(i).getText());
				tipps.set(i + (tag * anzahlTeams),tmp);
			} catch (NumberFormatException e) {
				tfTipps.get(i).setText("");
				tipps.set(i + (tag * anzahlTeams),-1);
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
	
	@FXML
	private void weiterZuSchritt2() {
		if(tfLigaName.getText().equals("")||tfLigaName.getText().equals(" "))
		{
			fehlermeldung("Kein Liga Name eingegeben");
			return;
		}
		if(tfSaison.getText().equals("")||tfSaison.getText().equals(" "))
		{
			fehlermeldung("Bitte Saison eingeben");
			return;
		}
		try {
			saison=Integer.parseInt(tfSaison.getText());
		} catch (NumberFormatException e) {
			fehlermeldung("Saison als Jahreszahl angeben, wann die Saison beginnt");
			return;
		}
		if(listViewTeams.getItems().size()==0)
		{
			fehlermeldung("Keine Teams eingegeben");
			return;
		}
		if(listViewTeams.getItems().size()==1)
		{
			fehlermeldung("Es wird mehr als eine Mannschaft benötigt");
			return;
		}
		if(listViewTeams.getItems().size()%2!=0)
		{
			fehlermeldung("Grade Anzahl von Teams eingeben");
			return;
		}
		//if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1&&listViewTeams.getItems().size()%4!=0)
		//{
		//	fehlermeldung("Eine Gruppen besteht immer aus 4 Mannschaften. Die Anzahl der mannschaften muss also durch 4 teilbar sein");
		//	return;
		//}
		
		for(int i=0; i<ControllerStartseite.eigeneLigen.size();i++)
		{
			if(tfLigaName.getText().equals(ControllerStartseite.eigeneLigen.get(i).getLiga()))
			{
				if(liga==null)
				{
					
					fehlermeldung("Liga bereits vorhanden, bitte anderen Liganamen eingeben");
					return;
				}else{
					if(!tfLigaName.getText().equals(liga.getLiga()))
					{
						fehlermeldung("Liga bereits vorhanden, bitte anderen Liganamen eingeben");
						return;
					}
				}
			}
		}
		
		ligaName=tfLigaName.getText();
		anzahlTeams=teams.size();
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
		{
			colTeam1.setOnKeyPressed(onKeyPressedEventHandler);
			colTeam2.setOnKeyPressed(onKeyPressedEventHandler);
			anzahlSpieltage=(anzahlTeams-1)*2;	
			colTeam2.setDisable(false);
			colTeam2.setVisible(true);
			spieltageSelberGenerieren();
			if(tfTipps==null)erstelleTippfelder();
			if(spieltag==1){
				buttonZurück.setDisable(true);
			}	
		}
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
		{
			if(tfAnzahlGruppenmitglieder.getText().equals("")||tfSaison.getText().equals(" "))
			{
				fehlermeldung("Bitte Anzahl der Gruppenmitglieder eingeben");
				return;
			}
			try {
				anzahlGruppenmitglieder=Integer.parseInt(tfAnzahlGruppenmitglieder.getText());
			} catch (NumberFormatException e) {
				fehlermeldung("Anzahl Gruppenmitglieder als Zahl angeben");
				return;
			}
			//TODO verbessern bzw entfernen
			if(anzahlGruppenmitglieder!=4)
			{
				fehlermeldung("Zurzeit ist es nur Möglich eine Gruppe bestehend aus 4 Mannschaften zu erstellen");
				return;
			}
			if(listViewTeams.getItems().size()%4!=0){
				fehlermeldung("Zurzeit ist es nur Möglich eine Gruppe bestehend aus 4 Mannschaften zu erstellen, daher muss die Anzahl der Mannschaften durch 4 teilbar sein");
				return;
			}
			if(tfTipps!=null)
			{
				for(int i=tfTipps.size()-1;i>=0; i--)
				{
					paneTipps.getChildren().remove(tfTipps.get(i));
					tfTipps.remove(i);
				}		
			}
			System.out.println("anzahlSpieltage: "+anzahlSpieltage+"   anzahlTeams: "+anzahlTeams+"   anzahlGruppenmitglieder: "+anzahlGruppenmitglieder);
			anzahlSpieltage=anzahlTeams/anzahlGruppenmitglieder;	
			setLabelText();
			spieltageSelberGenerieren();
			labelsErstellen(listViewTeams.getItems());
			colTeam2.setDisable(true);
			colTeam2.setVisible(false);
			if(spieltag==1){
				buttonZurück.setDisable(true);
			}	
		}

		
		
		paneGenerator.setVisible(true);
		paneGenerator.setDisable(false);
		paneSchritt1.setDisable(true);

	}
	@FXML
	private void weiterZuSchritt3() {
		if(spieltag==anzahlSpieltage)
		{
			buttonVor();
		}
		paneGenerator.setDisable(true);
		panePunktevergabe.setVisible(true);
		panePunktevergabe.setDisable(false);
		paneEigenePunktevergabe.setDisable(true);
		setzeTipps();
		ObservableList<String> sortierung=null;
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
		{
			sortierung = FXCollections.observableArrayList("Fußballbundesliga", "Handballbundesliga");
		}
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
		{
			sortierung = FXCollections.observableArrayList("Fußball EM", "Handball EM");
		}
		choiceBoxSortierung.setItems(sortierung);
		choiceBoxSortierung.getSelectionModel().select(0);
	}

	@FXML
	private void zurueckZuSchritt1()
	{
		paneGenerator.setDisable(true);
		paneSchritt1.setDisable(false);
	}
	
	@FXML
	private void zurueckZuSchritt2()
	{
		paneGenerator.setDisable(false);
		panePunktevergabe.setDisable(true);
	}
	
	@FXML
	private void zurueckZurStartseite()
	{
		cGUI.entferneVorherigenInhalt();
		cGUI.loadStartseiteFXML();
	}
	
	@FXML
	private void addTeam() {
		String teamName = tfTeamName.getText();

		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).equals(teamName)) {
				fehlermeldung("Name bereis vorhanden");
				return;
			}
			if (teamName.equals("")) {
				fehlermeldung("Kein Name eingegeben");
				return;
			}
		}
		teams.add(teamName);
		listViewTeams.setItems(teams);
		tfTeamName.setText("");
		labelAnzahlTeams.setText("" + teams.size());
	}

	@FXML
	private void deleteTeam() {
		for (int i = 0; i < teams.size(); i++) {
			if (listViewTeams.getSelectionModel().getSelectedItem().equals(teams.get(i))) {
				teams.remove(i);
				buttonEntfernen.setDisable(true);
				labelAnzahlTeams.setText("" + teams.size());
			}
		}

	}

	@FXML
	private void auswahl() {
		if (listViewTeams.getSelectionModel().getSelectedItem() != null) {
			buttonEntfernen.setDisable(false);
		}
	}

	@FXML
	private void punktevergabeVorgegeben() {
		choiceBoxPunkte.setDisable(false);
		paneEigenePunktevergabe.setDisable(true);
	}

	@FXML
	private void punktevergabeEigene() {
		choiceBoxPunkte.setDisable(true);
		paneEigenePunktevergabe.setDisable(false);
	}

	@FXML
	private void minuspunkte() {
		if (paneMinuspunkte.isDisable()) {
			paneMinuspunkte.setDisable(false);
		} else {
			paneMinuspunkte.setDisable(true);
		}
	}

	@FXML
	private void bestaetigen() {
		String path=System.getProperty("user.dir")+"/"+"src/Ergebnisse/"+ligaName;
		String lokalPath="file:///"+path+"/";
		Liga tmpLiga = null;
		int sieg, unentschieden, niederlage = 0;
		int siegM, unentschiedenM, niederlageM = 0;
		int bisSpieltag=0;
		int spieltag=0;
		int counter=0;
		
		for(int i=0; i<tipps.size(); i++)
		{
			if(tipps.get(i)==-1)
			{
				counter++;
			}
			if(i%anzahlTeams==0&&i!=0)
			{
				spieltag++;
			}
			if(counter>=anzahlTeams)
			{
				bisSpieltag=spieltag;
				break;
			}
		}
		System.out.println("hier---------------------------: "+bisSpieltag);
		
		if(paneEigenePunktevergabe.isDisable())
		{
			String sportart=choiceBoxPunkte.getSelectionModel().getSelectedItem();
			if(sportart.equals("Fußball"))
			{
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
				{
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new Zaehlweise(3, 1, 0)),new Meisterschaft(bisSpieltag,anzahlSpieltage,ComparatorChain.CC_FUßBALL_MEISTERSCHAFT));
				}
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
				{
					//tmpLiga=new Liga(ligaName, saison,new SportartNeu(new Zaehlweise(3, 1, 0),lokalPath),new Gruppenphase(anzahlSpieltage));
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new Zaehlweise(3, 1, 0)),new Europameisterschaft(anzahlSpieltage,bisSpieltag,ComparatorChain.CC_FUßBALL_EM));
				}
			}
			if(sportart.equals("Handball")){
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
				{
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new ZaehlweisemitMinuspunkte(2, 1, 0, 0, 1, 2)),new Meisterschaft(bisSpieltag,anzahlSpieltage,ComparatorChain.CC_HANDBALL_MEISTERSCHAFT));
				}
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
				{
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new ZaehlweisemitMinuspunkte(2, 1, 0, 0, 1, 2)),new Europameisterschaft(anzahlSpieltage,bisSpieltag,ComparatorChain.CC_HANDBALL_EM));
				}
			}
		}
		if (!paneEigenePunktevergabe.isDisable()) {
			int sortierung=0;
			try {
				sieg = Integer.parseInt(tfSieg.getText());
				unentschieden = Integer.parseInt(tfUnentschieden.getText());
				niederlage = Integer.parseInt(tfNiederlage.getText());
				
				switch(choiceBoxSortierung.getSelectionModel().getSelectedItem())
				{
					case "Fußballbundesliga" : sortierung = ComparatorChain.CC_FUßBALL_MEISTERSCHAFT;
					case "Fußball EM": sortierung = ComparatorChain.CC_FUßBALL_EM;
					case "Handballbundesliga": sortierung = ComparatorChain.CC_HANDBALL_MEISTERSCHAFT;
					case "Handball EM": sortierung = ComparatorChain.CC_HANDBALL_EM;
				}
				System.out.println(sortierung);
			} catch (NumberFormatException e) {
				fehlermeldung("Nur Zahlen eingeben");
				return;
			}
			if (!paneMinuspunkte.isDisable()) {
				try {
					siegM = Integer.parseInt(tfSieg_M.getText());
					unentschiedenM = Integer.parseInt(tfUnentschieden_M.getText());
					niederlageM = Integer.parseInt(tfNiederlage_M.getText());
					if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
					{
						tmpLiga=new Liga(ligaName, saison,new SportartNeu(new ZaehlweisemitMinuspunkte(sieg, unentschieden, niederlage,siegM,unentschiedenM,niederlageM)),new Meisterschaft(bisSpieltag,anzahlSpieltage,sortierung));
					}
					if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
					{
						tmpLiga=new Liga(ligaName, saison,new SportartNeu(new ZaehlweisemitMinuspunkte(sieg, unentschieden, niederlage,siegM,unentschiedenM,niederlageM)),new Europameisterschaft(anzahlSpieltage,bisSpieltag,sortierung));
					}
				} catch (NumberFormatException e) {
					fehlermeldung("Nur Zahlen eingeben");
					return;
				}
			}else{
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
				{
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new Zaehlweise(sieg, unentschieden, niederlage)),new Meisterschaft(bisSpieltag,anzahlSpieltage,sortierung));
				}
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
				{
					tmpLiga=new Liga(ligaName, saison,new SportartNeu(new Zaehlweise(sieg,unentschieden,niederlage)),new Europameisterschaft(anzahlSpieltage,bisSpieltag,sortierung));
				}
			}						
		}
		boolean loeschen=false;
		try{	
			if(liga!=null&&liga.getLiga().equals(tfLigaName.getText()))
			{
				loeschen=loescheOrdner(path);
				if(!loeschen){
					return;
				}
			}
			ordner = new File(path); 
			ordnerSaison=new File(path+"/"+saison);
			if(!ordner.mkdir())
			{
				fehlermeldung("Ordner konnte nicht erstellt werden");
				return;
			}
			if(!ordnerSaison.mkdir())
			{
				fehlermeldung("Ordner konnte nicht erstellt werden");
				return;
			}
			
			JSONDateienErzeugen(path+"/"+saison);
		}catch(Exception e)
		{
			e.printStackTrace();
			loescheOrdner(path);
			fehlermeldung("Überprüfen Sie ihre Spielpaarungen in Schritt 2");
			return;
		}
		speicherInformationen(path+"/"+saison);
		if(loeschen){
			for(int i=0; i<ControllerStartseite.eigeneLigen.size(); i++)
			{
				if(ControllerStartseite.eigeneLigen.get(i).getLiga().equals(tmpLiga.getLiga()))
				{
					ControllerStartseite.eigeneLigen.remove(i);
				}
			}
			System.out.println("hier");
			ControllerStartseite.eigeneLigen.add(tmpLiga);
			ControllerStartseite.eigeneSportartenSpeichern();
			zurueckZurStartseite();
		}else{
			ControllerStartseite.eigeneLigen.add(tmpLiga);
			ControllerStartseite.eigeneSportartenSpeichern();
			zurueckZurStartseite();
		}

	}

	private boolean loescheOrdner(String path)
	{
			File ordner=new File(path);
			File ordnerSaison=new File(path+"/"+saison);
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
				return false;
			}
			return true;
	}
	private void spieltageSelberGenerieren() {
		paneSpieltage.setDisable(false);
		labels.clear();
		for(int i=flowPaneLabels.getChildren().size();i>2;i--)
		{
			flowPaneLabels.getChildren().remove(i-1);
		}
		pruefenUndLabelsErstellen();
	}
	
	private void labelsErstellen(ObservableList<String> liste)
	{
		for(int i=0; i< liste.size();i++)
		{
			Label tmp=new Label(liste.get(i));

			tmp.setOnMouseDragged(onMouseDraggedEventHandler);
			tmp.setOnMousePressed(onMousePressedEventHandler);
			tmp.setOnMouseReleased(onMouseReleasedEventHandler);
			
			labels.add(tmp);
		}
		
		for(int i=0; i<labels.size(); i++)
		{
			flowPaneLabels.getChildren().add(labels.get(i));
		}
	}
	
	private void fehlermeldung(String meldung) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("ERROR");
		alert.setContentText(meldung);
		alert.showAndWait();
	}

	/**
	 * Der eventHanlder regaiert auf MouseEvents, hier speziell auf das ziehen
	 * mit der Mause
	 */
	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
            //Neue Korrdinaten ermitteln. mit alten und neuen Koordinaten und dann abziehen
            double offsetX = t.getSceneX() - alteKoorMausX;
            double offsetY = t.getSceneY() - alteKoorMausY;

            //Alte und neue Verschiebung zusammen rechnen
            double newTranslateX = alteKoorObjX + offsetX;
            double newTranslateY = alteKoorObjY + offsetY;
            
            ((Label) (t.getSource())).setTranslateX(newTranslateX);
            ((Label) (t.getSource())).setTranslateY(newTranslateY);
		}
	};

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			//Mauskoordinaten speichern
			alteKoorMausX=t.getSceneX();
			alteKoorMausY=t.getSceneY();
			
			//Objektkoordinaten speichern
			alteKoorObjX=((Label) (t.getSource())).getTranslateX();
			alteKoorObjY=((Label) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t) {
			Bounds tmp=((Label) (t.getSource())).getBoundsInParent();
			if(colTeam1.getBoundsInParent().intersects(tmp))
			{	

				list1.add(((Label) (t.getSource())).getText());	
				colTeam1.setItems(list1);

				flowPaneLabels.getChildren().remove((Label) (t.getSource()));
				labels.remove((Label) (t.getSource()));
			}
			if(colTeam2.getBoundsInParent().intersects(tmp))
			{	
				list2.add(((Label) (t.getSource())).getText());	
				colTeam2.setItems(list2);
				flowPaneLabels.getChildren().remove((Label) (t.getSource()));
				labels.remove((Label) (t.getSource()));
			}
		}
	};
	
	EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>(){
		@Override
		public void handle(KeyEvent key) {
			ListView<String> tmp = null;
			ListView<String> tmp2 = null;

			if(colTeam1.isFocused())
			{
				tmp=colTeam1;
				tmp2=colTeam2;
			}
			if(colTeam2.isFocused())
			{
				tmp=colTeam2;
				tmp2=colTeam1;
			}
			if(tmp==null||tmp.getSelectionModel().isEmpty()) return;
			
			if(key.getCode()==KeyCode.DOWN)
			{
				if(tmp.getSelectionModel().getSelectedIndex()+1==tmp.getItems().size())
				{
					return;
				}
				int index=tmp.getSelectionModel().getSelectedIndex();
				String s= tmp.getSelectionModel().getSelectedItem();
				tmp.getItems().remove(index);
				tmp.getItems().add(index+1, s);
				tmp.getSelectionModel().clearSelection();
				tmp.getSelectionModel().select(index);
			}
			if(key.getCode()==KeyCode.UP)
			{
				if(tmp.getSelectionModel().getSelectedIndex()==0)
				{
					return;
				}
				int index=tmp.getSelectionModel().getSelectedIndex();
				String s= tmp.getSelectionModel().getSelectedItem();
				tmp.getItems().remove(index);
				tmp.getItems().add(index-1, s);
				tmp.getSelectionModel().clearSelection();
				tmp.getSelectionModel().select(index);
			}
			if(key.getCode()==KeyCode.RIGHT)
			{
				if(colTeam1.isFocused())
				{
					int index=tmp.getSelectionModel().getSelectedIndex();
					String s= tmp.getSelectionModel().getSelectedItem();
					tmp.getItems().remove(index);
					tmp2.getItems().add(s);
					tmp.getSelectionModel().clearSelection();
					tmp2.getSelectionModel().select(tmp2.getItems().size()-1);
				}
			}
			if(key.getCode()==KeyCode.LEFT)
			{
				if(colTeam2.isFocused())
				{
					int index=tmp.getSelectionModel().getSelectedIndex();
					String s= tmp.getSelectionModel().getSelectedItem();
					tmp.getItems().remove(index);
					tmp2.getItems().add(s);
					tmp.getSelectionModel().clearSelection();
					tmp2.getSelectionModel().select(tmp2.getItems().size()-1);
				}
			}
		}	
	};
	
	
	private void xxx()
	{
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
		{
			labels.clear();
			for(int i=flowPaneLabels.getChildren().size();i>2;i--)
			{
				flowPaneLabels.getChildren().remove(i-1);
			}
		}

		
		if(spieltag-1==spielpaarungenHeim.size())
		{
			spielpaarungenHeim.add(spieltag-1, copyItem(colTeam1.getItems()));	
		}else{
			spielpaarungenHeim.set(spieltag-1, copyItem(colTeam1.getItems()));
		}
		if(spieltag-1==spielpaarungenAusw.size())
		{
			spielpaarungenAusw.add(spieltag-1, copyItem(colTeam2.getItems()));
		}else{
			spielpaarungenAusw.set(spieltag-1, copyItem(colTeam2.getItems()));
		}	
		
		setzeTipps();
	}
	
	private void yyy()
	{
		colTeam1.getItems().clear();
		colTeam2.getItems().clear();
		list1.clear();
		list2.clear();
		
		if(pruefenUndLabelsErstellen())
		{
			colTeam1.setItems(spielpaarungenHeim.get(spieltag-1));
			colTeam2.setItems(spielpaarungenAusw.get(spieltag-1));
		}

		holeTipps();
	}
	private boolean pruefenUndLabelsErstellen()
	{
		if(spieltag-1==spielpaarungenHeim.size()&&spieltag-1==spielpaarungenAusw.size())
		{
			if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0){
				labelsErstellen(listViewTeams.getItems());
			}
			return false;
		}else {

			ObservableList<String> liste = FXCollections.observableArrayList();
			for(int i=0; i<listViewTeams.getItems().size();i++)
			{
				String tmp=listViewTeams.getItems().get(i);
				liste.add(tmp);
			}
			for(int i=0; i<spielpaarungenHeim.get(spieltag-1).size();i++)
			{
				for(int j=liste.size()-1; j>=0;j--)
				{
					if(spielpaarungenHeim.get(spieltag-1).get(i).equals(liste.get(j)))
					{
						String tmp=liste.get(j);
						list1.add(tmp);
						liste.remove(j);
					}
				}
			}
			for(int i=0; i<spielpaarungenAusw.get(spieltag-1).size();i++)
			{
				for(int j=liste.size()-1; j>=0;j--)
				{
					if(spielpaarungenAusw.get(spieltag-1).get(i).equals(liste.get(j)))
					{
						String tmp=liste.get(j);
						list2.add(tmp);
						liste.remove(j);
					}
				}
			}
			if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
			{
				labelsErstellen(liste);
			}
			return true;
		}
	}
	@FXML
	private void buttonVor()
	{
		xxx();
		spieltag++;
		
		if (spieltag > anzahlSpieltage)
			spieltag = anzahlSpieltage;
		setLabelText();
		if(spieltag==anzahlSpieltage)
		{
			buttonVor.setDisable(true);
			buttonZurück.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurück.setDisable(false);
		}
		yyy();
	}
	

	@FXML
	private void buttonZurueck()
	{
		xxx();
		spieltag--;
		if (spieltag <= 0)
			spieltag = 1;
		setLabelText();
		if(spieltag==1)
		{
			buttonZurück.setDisable(true);
			buttonVor.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurück.setDisable(false);
		}
		yyy();
	}
	
	private void setLabelText()
	{
		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==1)
		{
			switch(spieltag)
			{
			case 1:labelSpieltag.setText("Gruppe A"); break;
			case 2:labelSpieltag.setText("Gruppe B"); break;
			case 3:labelSpieltag.setText("Gruppe C"); break;
			case 4:labelSpieltag.setText("Gruppe D"); break;
			case 5:labelSpieltag.setText("Gruppe E"); break;
			case 6:labelSpieltag.setText("Gruppe F"); break;
			case 7:labelSpieltag.setText("Gruppe G"); break;
			case 8:labelSpieltag.setText("Gruppe H"); break;
			case 9:labelSpieltag.setText("Gruppe I"); break;
			case 10:labelSpieltag.setText("Gruppe J"); break;
			case 11:labelSpieltag.setText("Gruppe K"); break;
			case 12:labelSpieltag.setText("Gruppe L"); break;
			case 13:labelSpieltag.setText("Gruppe M"); break;
			case 14:labelSpieltag.setText("Gruppe N"); break;
			case 15:labelSpieltag.setText("Gruppe O"); break;
			case 16:labelSpieltag.setText("Gruppe P"); break;
			case 17:labelSpieltag.setText("Gruppe Q"); break;
			case 18:labelSpieltag.setText("Gruppe R"); break;
			case 19:labelSpieltag.setText("Gruppe S"); break;
			case 20:labelSpieltag.setText("Gruppe T"); break;			
			}
		}else{
			labelSpieltag.setText("Spieltag: "+spieltag);
		}
	}
	
	private ObservableList<String> copyItem(ObservableList<String> alteWerte)
	{
		ObservableList<String> cpy = FXCollections.observableArrayList();
		for(int i=0; i<alteWerte.size();i++)
		{
			String tmp=alteWerte.get(i);
			cpy.add(tmp);
		}
		return cpy;
	}
	@FXML
	private void teamsLoeschen()
	{
		labels.clear();
		colTeam1.getItems().clear();
		colTeam2.getItems().clear();
		for(int i=flowPaneLabels.getChildren().size()-1; i>=2; i--)
		{
			flowPaneLabels.getChildren().remove(i);
		}
		
		labelsErstellen(listViewTeams.getItems());
	}
	private void JSONDateienErzeugen(String path) throws IOException
	{
		for(int i=1; i<=anzahlSpieltage;i++)
		{
			FileOutputStream fop = null;
			File file;

			try {

				String anfang = "[";
				String ende="]";
				String spielpaarungen="";
				if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()==0)
				{
					String dateiname=""+(i);
					file = new File(path+"/"+dateiname);
					fop = new FileOutputStream(file);

					if (!file.exists()) {
						file.createNewFile();
					}
					int z=0;
					for(int j=0; j<anzahlTeams/2;j++)
					{
						
						String spiel;
						if(j+1==anzahlTeams/2)
						{
							spiel="   {\"Team1\":{\"TeamName\":\""+spielpaarungenHeim.get(i-1).get(j)+"\"},"
									 + "\"Team2\":{\"TeamName\":\""+spielpaarungenAusw.get(i-1).get(j)+"\"}"
									 + ",\"MatchResults\":[{\"ResultName\":\"Endergebnis\",\"PointsTeam1\":"+ tipps.get(z + ((i-1) * anzahlTeams)) +",\"PointsTeam2\":"+ tipps.get(z+1 + ((i-1) * anzahlTeams)) +"}]}";
						}else{
							spiel="   {\"Team1\":{\"TeamName\":\""+spielpaarungenHeim.get(i-1).get(j)+"\"},"
									 + "\"Team2\":{\"TeamName\":\""+spielpaarungenAusw.get(i-1).get(j)+"\"}"
									 + ",\"MatchResults\":[{\"ResultName\":\"Endergebnis\",\"PointsTeam1\":"+ tipps.get(z + ((i-1) * anzahlTeams)) +",\"PointsTeam2\":"+ tipps.get(z+1 + ((i-1) * anzahlTeams)) +"}]},";
						}
						spielpaarungen=spielpaarungen+spiel;	
						z+=2;
					}
					anfang=anfang+spielpaarungen+ende;
					byte[] bytesArray = anfang.getBytes();
					fop.write(bytesArray);
					fop.flush();
					fop.close();
				}else{
						for(int j=1; j<=(anzahlGruppenmitglieder-1)*2;j++)
						{
							String dateiname=null;//=""+(j);
							String spielpaarungenG="";
							String anfangG = "[";
							switch(i)
							{
							case 1: dateiname="A"+j; break;
							case 2: dateiname="B"+j; break;
							case 3: dateiname="C"+j; break;
							case 4: dateiname="D"+j; break;
							case 5: dateiname="E"+j; break;
							case 6: dateiname="F"+j; break;
							case 7: dateiname="G"+j; break;
							case 8: dateiname="H"+j; break;
							case 9: dateiname="I"+j; break;
							case 10: dateiname="J"+j; break;
							case 11: dateiname="K"+j; break;
							case 12: dateiname="L"+j; break;
							}
							
							file = new File(path+"/"+dateiname);
							fop = new FileOutputStream(file);

							if (!file.exists()) {
								file.createNewFile();
							}
							String spiel1, spiel2;
							String team1 = null,team2 =null,team3=null,team4=null;
							if(j==1)
							{
								//TODO
								team1=spielpaarungenHeim.get(i-1).get(0);
								team2=spielpaarungenHeim.get(i-1).get(1);
								team3=spielpaarungenHeim.get(i-1).get(2);
								team4=spielpaarungenHeim.get(i-1).get(3);
							}
							if(j==2)
							{
								team1=spielpaarungenHeim.get(i-1).get(0);
								team2=spielpaarungenHeim.get(i-1).get(2);
								team3=spielpaarungenHeim.get(i-1).get(1);
								team4=spielpaarungenHeim.get(i-1).get(3);
							}
							if(j==3)
							{
								team1=spielpaarungenHeim.get(i-1).get(0);
								team2=spielpaarungenHeim.get(i-1).get(3);
								team3=spielpaarungenHeim.get(i-1).get(1);
								team4=spielpaarungenHeim.get(i-1).get(2);
							}
							if(j==4)
							{
								team1=spielpaarungenHeim.get(i-1).get(1);
								team2=spielpaarungenHeim.get(i-1).get(0);
								team3=spielpaarungenHeim.get(i-1).get(3);
								team4=spielpaarungenHeim.get(i-1).get(2);
							}
							if(j==5)
							{
								team1=spielpaarungenHeim.get(i-1).get(2);
								team2=spielpaarungenHeim.get(i-1).get(0);
								team3=spielpaarungenHeim.get(i-1).get(3);
								team4=spielpaarungenHeim.get(i-1).get(1);
							}
							if(j==6)
							{
								team1=spielpaarungenHeim.get(i-1).get(3);
								team2=spielpaarungenHeim.get(i-1).get(0);
								team3=spielpaarungenHeim.get(i-1).get(2);
								team4=spielpaarungenHeim.get(i-1).get(1);
							}
							
								spiel1="   {\"Team1\":{\"TeamName\":\""+team1+"\"},"
										 + "\"Team2\":{\"TeamName\":\""+team2+"\"}"
										 + ",\"MatchResults\":[]},";
								spiel2="   {\"Team1\":{\"TeamName\":\""+team3+"\"},"
										 + "\"Team2\":{\"TeamName\":\""+team4+"\"}"
										 + ",\"MatchResults\":[]}";
							
							spielpaarungenG=spielpaarungenG+spiel1+spiel2;
							anfangG=anfangG+spielpaarungenG+ende;
							byte[] bytesArray = anfangG.getBytes();
							fop.write(bytesArray);
							fop.flush();
							fop.close();
							
						}		
				}
			
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fop != null) {
						fop.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void speicherInformationen(String path)
	{
		LinkedList<LinkedList<String>> heim= new LinkedList<LinkedList<String>>();
		
		for(int i=0; i<spielpaarungenHeim.size();i++)
		{
			LinkedList<String> tmp= new LinkedList<String>();
			for(int j=0; j<spielpaarungenHeim.get(i).size(); j++)
			{
				tmp.add(spielpaarungenHeim.get(i).get(j));
			}
			heim.add(tmp);
		}
		informationen=new File(path+"/"+"infoHeim.ser");
		try {
			informationen.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		OutputStream fos = null;
		try
		{
		  fos = new FileOutputStream(informationen);
		  ObjectOutputStream o = new ObjectOutputStream( fos );
		  o.writeObject(heim);
		}
		catch ( IOException e ) { System.err.println( e ); }
		finally { try { fos.close(); } catch ( Exception e ) { e.printStackTrace(); } }
		
		
		
		LinkedList<LinkedList<String>> ausw= new LinkedList<LinkedList<String>>();
		
		for(int i=0; i<spielpaarungenAusw.size();i++)
		{
			LinkedList<String> tmp= new LinkedList<String>();
			for(int j=0; j<spielpaarungenAusw.get(i).size(); j++)
			{
				tmp.add(spielpaarungenAusw.get(i).get(j));
			}
			ausw.add(tmp);
		}
		informationen=new File(path+"/"+"infoAusw.ser");
		try {
			informationen.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try
		{
		  fos = new FileOutputStream(informationen);
		  ObjectOutputStream o = new ObjectOutputStream( fos );
		  o.writeObject(ausw);
		}
		catch ( IOException e ) { System.err.println( e ); }
		finally { try { fos.close(); } catch ( Exception e ) { e.printStackTrace(); } }
		
		
		informationen=new File(path+"/"+"infoTipps.ser");
		try {
			informationen.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try
		{
		  fos = new FileOutputStream(informationen);
		  ObjectOutputStream o = new ObjectOutputStream( fos );
		  o.writeObject(tipps);
		}
		catch ( IOException e ) { System.err.println( e ); }
		finally { try { fos.close(); } catch ( Exception e ) { e.printStackTrace(); } }
		
		
	}
	
	private void ladeInfomationen(String path)
	{
		informationen=new File(path+"/"+"infoHeim.ser");
		LinkedList<LinkedList<String>> heim= new LinkedList<LinkedList<String>>();
		InputStream fis = null;
		try
		{
		  fis = new FileInputStream(informationen);

		  ObjectInputStream o = new ObjectInputStream( fis );
		  heim = (LinkedList<LinkedList<String>>) o.readObject();
		}
		catch ( IOException e ) { System.err.println( e ); }
		catch ( ClassNotFoundException e ) { System.err.println( e ); }
		finally { try { fis.close(); } catch ( Exception e ) { } }
		
		
		for(int i=0;i<heim.size();i++)
		{
			ObservableList<String> tmp=FXCollections.observableArrayList();
			for(int j=0; j<heim.get(i).size();j++)
			{
				tmp.add(heim.get(i).get(j));
			}
			spielpaarungenHeim.add(tmp);
		}

		if(choiceBoxMeisterschafft.getSelectionModel().getSelectedIndex()!=0)
		{
			return;
		}
		informationen=new File(path+"/"+"infoAusw.ser");
		LinkedList<LinkedList<String>> ausw= new LinkedList<LinkedList<String>>();
		try
		{
		  fis = new FileInputStream(informationen);

		  ObjectInputStream o = new ObjectInputStream( fis );
		  ausw = (LinkedList<LinkedList<String>>) o.readObject();
		}
		catch ( IOException e ) { System.err.println( e ); }
		catch ( ClassNotFoundException e ) { System.err.println( e ); }
		finally { try { fis.close(); } catch ( Exception e ) { } }
		
		
		for(int i=0;i<ausw.size();i++)
		{
			ObservableList<String> tmp=FXCollections.observableArrayList();
			for(int j=0; j<ausw.get(i).size();j++)
			{
				tmp.add(ausw.get(i).get(j));
			}
			spielpaarungenAusw.add(tmp);
		}
		
		informationen=new File(path+"/"+"infoTipps.ser");
		try
		{
		  fis = new FileInputStream(informationen);

		  ObjectInputStream o = new ObjectInputStream( fis );
		  tipps = (LinkedList<Integer>) o.readObject();
		}
		catch ( IOException e ) { System.err.println( e ); }
		catch ( ClassNotFoundException e ) { System.err.println( e ); }
		finally { try { fis.close(); } catch ( Exception e ) { } }
		
	}
}
