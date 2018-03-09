package Fachkonzept;

import java.io.IOException;

import Datenhaltung.DatenzugriffJSON;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

/**
 * Der Koordinator dient als Schnittstelle zwischen GUI interaktion, Logik und
 * Datenhaltung
 * 
 * @author T.Benning
 *
 */
public class Koordinator {

	private Liga liga;
	private MenuBar oben;
	private Pane mitte;
	private static Koordinator k = null;
	private DatenzugriffJSON daten;
	private ProgressBar progressBar;
	private BranchAndBound algorithmus;
	private Liga aktiveGruppenLiga;


	/**
	 * Gibt den Koordinator zurück
	 * 
	 * @return liefert den Kooordinator zurück
	 */
	public static Koordinator getKoordinator() {
		if (k == null) {
			k = new Koordinator();		
		}
		return k;
	}
	
	/**
	 * Konstruktor
	 */
	private Koordinator()
	{
		algorithmus=new BranchAndBound();
	}

	/**
	 * setzt das Pane als Mitte
	 * 
	 * @param mitte
	 *            Das Pane welches in die Mitte gesetzt wird
	 */
	public void setMitte(Pane mitte) {
		this.mitte = mitte;
	}

	/**
	 * 
	 * @param progressBar der Ladebalken der sich aktualisieren muss
	 */
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	/**
	 * 
	 * @return der Ladebalken der sich aktualisieren muss
	 */
	public ProgressBar getProgressBar() {
		return this.progressBar;
	}

	/**
	 * Liefert die mitte zurück um so z.b elemente hinzuzufügen
	 * 
	 * @return Das Pane welches sich in der Mitte befindet
	 */
	public Pane getMitte() {
		return this.mitte;
	}

	/**
	 * setzt die Menübar
	 * 
	 * @param oben
	 *            Die MenüBar die gesetzt werden soll
	 */
	public void setOben(MenuBar oben) {
		this.oben = oben;
	}

	/**
	 * Liefert die Menübar zurück um so von anderen Klassen drauf zugreifen zu
	 * können
	 * 
	 * @return die Menübar die sich oben befindet
	 */
	public MenuBar getOben() {
		return this.oben;
	}

	/**
	 * Setzt die Sportart mit den angegebenen Daten
	 * 
	 * @param sportart
	 *            um welche Sporart es sich handelt, z.B Fußball
	 * @param saison
	 *            die saison welche betrachtet werden soll
	 * @param bisSpieltag
	 *            der Spieltag bis zu dem Gespielt werden soll
	 * @throws IOException 
	 */
	public void setLiga(String sportart, int saison,int bisSpieltag) throws IOException {
		switch (sportart.toLowerCase()) {
		case "fußball":
			this.liga = new Liga("Fußball", saison, new Fußball(), new Meisterschaft(bisSpieltag,ComparatorChain.CC_FUßBALL_MEISTERSCHAFT));
			this.aktiveGruppenLiga=liga;
			break;
		case "handball":
			this.liga=new Liga("Handball",saison, new Handball(),new Meisterschaft(bisSpieltag, ComparatorChain.CC_HANDBALL_MEISTERSCHAFT));
			this.aktiveGruppenLiga=liga;
			break;
		}
		
		if(this.liga!=null)
		{
			erstelleTeams();
		}	
	}
	
	public void setLiga(Liga liga) throws IOException
	{
		
		new Zaehlweise(liga.getSportart().getZaehlweise());
		this.liga=liga;
		this.aktiveGruppenLiga=liga;
		System.out.println("hier2---------------------------: "+liga.getAustragungsart().getGespieltBisTag());
		if(this.liga.getAustragungsart() instanceof Meisterschaft)
		{
			liga.getAustragungsart().setComparatorChain(liga.getAustragungsart().getComparatorNr());
			erstelleTeams();
		}	
		if(liga.getAustragungsart() instanceof Europameisterschaft)
		{
			liga.getAustragungsart().setComparatorChain(liga.getAustragungsart().getComparatorNr());
			erstelleTeamsGruppen();
		}
	}
	
	public void setzeAktiveLiga(Liga liga)
	{
		this.aktiveGruppenLiga=liga;
	}
	
	public Liga getAktiveLiga()
	{
		return this.aktiveGruppenLiga;
	}


	public Liga getLiga() {
		return this.liga;
	}

	/**
	 * 
	 * @return liefert den Algorithmus zur Berechnung zurück, wird hauptsächlich verwendet
	 * um beobachter im Algorithmus hinzuzufügen
	 * 
	 */
	public BranchAndBound getAlgorithmus()
	{
		return this.algorithmus;
	}


	/**
	 * erstellt die Teams und stellt die Tabelle bis zum Spieltag her
	 * @throws IOException 
	 */
	private void erstelleTeams() throws IOException {

		Sportart sportart=liga.getSportart();
		daten = new DatenzugriffJSON(sportart.getURL(), sportart.getLokalURL()+k.getLiga().getLiga()+"/");


		liga.addTeams(daten.ermittelTeams(liga.getJahr()));
		liga.getAustragungsart().setAnzahlSpieltage((liga.getTeams().length-1)*2);
		liga.addAusgetrageneSpiele(
				daten.berechneTabelle(liga.getJahr(), ((Meisterschaft) liga.getAustragungsart()).getGespieltBisTag(), liga.getTeams()));

		liga.ermittelPlatzierung(liga.getTeams(), Liga.SORTIERUNG_NORMAL,null);
		liga.addAusstehendeSpiele(
				daten.ermittelAusstehendeSpiele(liga.getJahr(), ((Meisterschaft) liga.getAustragungsart()).getGespieltBisTag() + 1, (liga.getTeams().length-1)*2));
	}
	
	private void erstelleTeamsGruppen() throws IOException {
		Sportart sportart=liga.getSportart();
		daten = new DatenzugriffJSON(sportart.getURL(), sportart.getLokalURL()+k.getLiga().getLiga()+"/");
		String gruppe = null;

		for(int i=1; i<=((Gruppenphase) liga.getAustragungsart()).getAnzahlGruppen();i++)
		{
			//TODO zurück ändern
			Liga tmp=null;
			if(i==1&&liga.getJahr()==2008)
			{
				tmp=new Liga(liga.getLiga(),liga.getJahr(),liga.getSportart(),new Meisterschaft(3,liga.getAustragungsart().getComparatorNr()));
			}else{
				tmp=new Liga(liga.getLiga(),liga.getJahr(),liga.getSportart(),new Meisterschaft(liga.getAustragungsart().getGespieltBisTag(),liga.getAustragungsart().getComparatorNr()));
			}
			
			//2.Liga tmp=new Liga(liga.getLiga(),liga.getJahr(),liga.getSportart(),new Gruppenphase(6));
			//1.Liga tmp=new Liga(liga.getLiga(),liga.getJahr(),liga.getSportart(),liga.getAustragungsart());
			switch(i){
				case 1: gruppe="A"; break;
				case 2: gruppe="B"; break;
				case 3: gruppe="C"; break;
				case 4: gruppe="D"; break;
				case 5: gruppe="E"; break;
				case 6: gruppe="F"; break;
				case 7: gruppe="G"; break;
				case 8: gruppe="H"; break;
				case 9: gruppe="I"; break;
				case 10: gruppe="J"; break;
				case 11: gruppe="K"; break;
				case 12: gruppe="L"; break;
				case 13: gruppe="M"; break;
				case 14: gruppe="N"; break;
				case 15: gruppe="O"; break;
				case 16: gruppe="P"; break;
				case 17: gruppe="Q"; break;
			}
			tmp.addTeams(daten.ermittelTeams(tmp.getJahr(),gruppe));
			tmp.getAustragungsart().setAnzahlSpieltage((tmp.getTeams().length-1)*2);
			tmp.addAusgetrageneSpiele(
					daten.berechneTabelle(tmp.getJahr(),  tmp.getAustragungsart().getGespieltBisTag(), tmp.getTeams(),gruppe));

			tmp.ermittelPlatzierung(tmp.getTeams(), Liga.SORTIERUNG_NORMAL,null);
			tmp.addAusstehendeSpiele(
					daten.ermittelAusstehendeSpiele(tmp.getJahr(), tmp.getAustragungsart().getGespieltBisTag() + 1, (tmp.getTeams().length-1)*2,gruppe));
			((Gruppenphase) liga.getAustragungsart()).getGruppen()[i-1]=tmp;
		}
		aktiveGruppenLiga=((Gruppenphase) liga.getAustragungsart()).getGruppen()[0];
	}
}
