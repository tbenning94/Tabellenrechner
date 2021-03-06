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
	 * Gibt den Koordinator zur�ck
	 * 
	 * @return liefert den Kooordinator zur�ck
	 */
	public static Koordinator getKoordinator() {
		if (k == null) {
			k = new Koordinator();		
		}
		return k;
	}
	
	public static void setKoordinatorToNull(){
		k=null;
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
	 * Liefert die mitte zur�ck um so z.b elemente hinzuzuf�gen
	 * 
	 * @return Das Pane welches sich in der Mitte befindet
	 */
	public Pane getMitte() {
		return this.mitte;
	}

	/**
	 * setzt die Men�bar
	 * 
	 * @param oben
	 *            Die Men�Bar die gesetzt werden soll
	 */
	public void setOben(MenuBar oben) {
		this.oben = oben;
	}

	/**
	 * Liefert die Men�bar zur�ck um so von anderen Klassen drauf zugreifen zu
	 * k�nnen
	 * 
	 * @return die Men�bar die sich oben befindet
	 */
	public MenuBar getOben() {
		return this.oben;
	}

	/**
	 * Setzt die Sportart mit den angegebenen Daten
	 * 
	 * @param sportart
	 *            um welche Sporart es sich handelt, z.B Fu�ball
	 * @param saison
	 *            die saison welche betrachtet werden soll
	 * @param bisSpieltag
	 *            der Spieltag bis zu dem Gespielt werden soll
	 * @throws IOException 
	 */
	public void setLiga(String sportart, int saison,int bisSpieltag) throws IOException {
		switch (sportart) {
			case Liga.ersteFu�ballBundesliga:
				this.liga = new Liga(sportart, saison, new Fu�ball("bl1"), new Meisterschaft(bisSpieltag,ComparatorChain.CC_FU�BALL_MEISTERSCHAFT));
				this.aktiveGruppenLiga=liga;
				break;
			case Liga.zweiteFu�ballBundesliga:
				this.liga = new Liga(sportart, saison, new Fu�ball("bl2"), new Meisterschaft(bisSpieltag,ComparatorChain.CC_FU�BALL_MEISTERSCHAFT));
				this.aktiveGruppenLiga=liga;
				break;
		}
		
		if(this.liga!=null)
		{
			erstelleTeams();
		}	
	}
	
	public void setLiga(String sportart, int saison,int bisSpieltag,String url) throws IOException {
		ISportart sport;
		switch (sportart) {
			case "1. Fu�ballbundesliga":
				if(url==null){
					sport= new Fu�ball("bl1");
				}else{
					sport= new Fu�ball(url,"bl1");
				}
				this.liga = new Liga("Fu�ball", saison, sport, new Meisterschaft(bisSpieltag,ComparatorChain.CC_FU�BALL_MEISTERSCHAFT));
				this.aktiveGruppenLiga=liga;
				break;
			case "2. Fu�ballbundesliga":
				if(url==null){
					sport= new Fu�ball("bl2");
				}else{
					sport= new Fu�ball(url,"bl2");
				}
				this.liga = new Liga("Fu�ball", saison, sport, new Meisterschaft(bisSpieltag,ComparatorChain.CC_FU�BALL_MEISTERSCHAFT));
				this.aktiveGruppenLiga=liga;
				break;
		}
		
		if(this.liga!=null)
		{
			erstelleTeams();
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
	 * @return liefert den Algorithmus zur Berechnung zur�ck, wird haupts�chlich verwendet
	 * um beobachter im Algorithmus hinzuzuf�gen
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

		ISportart sportart=liga.getSportart();
		//String lokalURL = sportart.getLokalURL()+this.liga.getLiga()+"/";
		daten = new DatenzugriffJSON(sportart.getURL());


		liga.addTeams(daten.ermittelTeams(liga.getJahr()));
		liga.getAustragungsart().setAnzahlSpieltage((liga.getTeams().length-1)*2);
		liga.addAusgetrageneSpiele(
				daten.berechneTabelle(liga.getJahr(), ((Meisterschaft) liga.getAustragungsart()).getGespieltBisTag(), liga.getTeams()));

		Liga.ermittelPlatzierungNormal(liga.getTeams(), liga.getAustragungsart());
		liga.addAusstehendeSpiele(
				daten.ermittelAusstehendeSpiele(liga.getJahr(), ((Meisterschaft) liga.getAustragungsart()).getGespieltBisTag() + 1, (liga.getTeams().length-1)*2));
	}
	
	private void erstelleTeamsGruppen() throws IOException {
		ISportart sportart=liga.getSportart();
		daten = new DatenzugriffJSON(sportart.getURL());//, sportart.getLokalURL()+k.getLiga().getLiga()+"/");
		String gruppe = null;

		for(int i=1; i<=((Gruppenphase) liga.getAustragungsart()).getAnzahlGruppen();i++)
		{
			Liga tmp=null;
			tmp=new Liga(liga.getLiga(),liga.getJahr(),liga.getSportart(),new Meisterschaft(liga.getAustragungsart().getGespieltBisTag(),liga.getAustragungsart().getComparatorNr()));
			
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

			Liga.ermittelPlatzierungNormal(tmp.getTeams(), tmp.getAustragungsart());
			tmp.addAusstehendeSpiele(
					daten.ermittelAusstehendeSpiele(tmp.getJahr(), tmp.getAustragungsart().getGespieltBisTag() + 1, (tmp.getTeams().length-1)*2,gruppe));
			((Gruppenphase) liga.getAustragungsart()).getGruppen()[i-1]=tmp;
		}
		aktiveGruppenLiga=((Gruppenphase) liga.getAustragungsart()).getGruppen()[0];
	}
}
