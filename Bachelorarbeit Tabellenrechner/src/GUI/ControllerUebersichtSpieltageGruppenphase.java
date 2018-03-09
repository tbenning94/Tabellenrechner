package GUI;

import Fachkonzept.ComparatorChain;
import Fachkonzept.Gruppenphase;
import Fachkonzept.Koordinator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerUebersichtSpieltageGruppenphase extends ControllerUebersichtSpieltage{

	private int gruppe;
	private char gruppeChar;
	private int anzahlGruppen;
	private final Koordinator k = Koordinator.getKoordinator();
	
	@FXML
	private Label labelGruppe;
	@FXML
	private Button buttonVor, buttonZurueck;
	
	protected void initialize(){
		super.initialize();
		buttonZurueck.setDisable(true);
		gruppe=0;
		gruppeChar='A';
		anzahlGruppen=((Gruppenphase) k.getLiga().getAustragungsart()).getAnzahlGruppen();
		buttonVor();
	}
	
	@FXML
	private void vor()
	{
		gruppe++;
		if (gruppe >= anzahlGruppen)
			gruppe = anzahlGruppen;
		gruppeChar++;
		labelGruppe.setText("Gruppe: "+gruppeChar);
		k.setzeAktiveLiga(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe]);
		ComparatorChain.gruppe=gruppe;
		if(gruppe==anzahlGruppen-1)
		{
			buttonVor.setDisable(true);
			buttonZurueck.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurueck.setDisable(false);
		}
		setzeSpieltag();
	}
	
	@FXML
	private void zurueck()
	{
		gruppe--;
		if (gruppe < 0)
			gruppe = 0;
		gruppeChar--;
		labelGruppe.setText("Gruppe: "+gruppeChar);
		k.setzeAktiveLiga(((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe]);
		ComparatorChain.gruppe=gruppe;
		if(gruppe==0)
		{
			buttonZurueck.setDisable(true);
			buttonVor.setDisable(false);
		}else{
			buttonVor.setDisable(false);
			buttonZurueck.setDisable(false);
		}
		setzeSpieltag();
	}
	
}
