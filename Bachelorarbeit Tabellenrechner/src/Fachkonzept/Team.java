package Fachkonzept;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 
 * @author T.Benning
 *
 */
public class Team
{
	private final String name;
	private int punkte;
	private int tore;
	private int ggtore;//gegentore
	private int differenz;//zwischen tore und ggtore
	private int siege;
	private int unentschieden;
	private int niederlagen;
	private int platzierung;

	private LinkedList<Integer> maxPlatzSpieltag= new LinkedList<Integer>();//Die Maximalen Pl�tze die noch erreicht werden k�nnen
	private LinkedList<Integer> minPlatzSpieltag=new LinkedList<Integer>();//Die Minimalen Pl�tze die noch erreicht werden k�nnen
	private LinkedList<Integer> toreSpieltag= new LinkedList<Integer>();//Tore an Spieltag x
	private LinkedList<Integer> ggtoreSpieltag=new LinkedList<Integer>();//ggTore an Spieltag x

	
	/**
	 * Konstruktor f�r die erste Erstellung eines Teams wird der Name Ben�tigt
	 * @param name der Name des Teams
	 */
	public Team(String name)
	{
		this.name=name;
	}
	
	/**
	 * "KopierKonstruktor" der mithilfe eines Teams alle Attribute kopiert und damit ein neues Team erstellt
	 * wird meistens f�r tempor�re berechnungen eines Teams erstellt um das Original nicht zu ver�ndern
	 * @param t das Team welches Kopiert werden soll
	 */
	public Team(Team t)
	{
		this.name=t.getName();
		this.punkte=t.getPunkte();
		this.tore=t.getTore();
		this.ggtore=t.getGgtore();
		this.siege=t.getSiege();
		this.niederlagen=t.getNiederlagen();
		this.unentschieden=t.getUnentschieden();
		this.toreSpieltag=t.getToreSpieltag();
		this.ggtoreSpieltag=t.getGgtoreSpieltag();
		this.maxPlatzSpieltag=t.getMaxPlatzSpieltag();
		this.minPlatzSpieltag=t.getMinPlatzSpieltag();
	}

	/**
	 * 
	 * @return gibt die Platzierung des Teams zur�ck
	 */
	public int getPlatzierung()
	{
		return this.platzierung;
	}
	/**
	 * Setzt die Platzierung
	 * @param platzierung die erreichte platzierung
	 */
	public void setPlatzierung(int platzierung)
	{
		this.platzierung=platzierung;
	}
	

	
	/**
	 * 
	 * @return liefert die Punkte zur�ck
	 */
	public int getPunkte() {
		return punkte;
	}
	

	/**
	 * 
	 * @return liefert die geschossenen Tore zur�ck
	 */
	public int getTore() {
		return tore;
	}

	/**
	 * 
	 * @param tore addiert die neuen geschossenen Tore zu den alten hinzu
	 */
	public void setTore(int tore) {
		this.tore += tore;
		setToreSpieltag(tore);
		setDifferenz();
	}

	public String getPunkteS()
	{
		String x=""+punkte;
		return x;
	}
	/**
	 * 
	 * @return liefert die gegentore zur�ck
	 */
	public int getGgtore() {
		return ggtore;
	}

	/**
	 * addiet die gegentore zu den vorhanden toren hinzu
	 * @param ggtore die gegentore die hinzuaddert werden sollen
	 */
	public void setGgtore(int ggtore) {
		this.ggtore += ggtore;
		setGgtoreSpieltag(ggtore);
		setDifferenz();
	}
	
	/**
	 * berechnet die Differenz zwischen tore und gegentore
	 */
	public void setDifferenz()
	{
		this.differenz=tore-ggtore;
	}
	/**
	 * @return liefert die differenz zur�ck
	 */
	public int getDifferenz()
	{
		return this.differenz;
	}

	/**
	 * 
	 * @return liefert die anzahl der Siege zur�ck
	 */
	public int getSiege() {
		return siege;
	}

	/**
	 * setzt einen Sieg und addiert dies zu den Punkten
	 */
	public void sieg() {
		this.siege++;
		punkte+=Zaehlweise.PUNKTE_S;
	}

	/**
	 * 
	 * @return liefert die anzahl von Unentschiedenen Spielen zur�ck
	 */
	public int getUnentschieden() {
		return unentschieden;
	}

	/**
	 * setzt ein unentschieden und addiert dies zu den Punkten
	 */
	public void unentschieden() {
		this.unentschieden++;
		punkte+=Zaehlweise.PUNKTE_U;
	}

	/**
	 * 
	 * @return liefert die Anzahl der Niederlagen zur�ck
	 */
	public int getNiederlagen() {
		return niederlagen;
	}

	/**
	 * setzt eine Niederlage und addiert dies zu den Punkten (nur n�tig falls das 
	 * verlieren ebenfalls punkte gibt oder sogar punkte abgezogen werden)
	 */
	public void niederlage() {
		this.niederlagen++;
		punkte+=Zaehlweise.PUNKTE_N;
	}
	
	/**
	 * 
	 * @return liefert den Namen zur�ck
	 */
	public String getName()
	{
		return name;
	}
	

	/**
	 * 
	 * @param tore setzt die Tore am Spieltag 
	 */
	public void setToreSpieltag(int tore)
	{
		this.toreSpieltag.add(tore);
	}
	
	/**
	 * 
	 * @param spieltag Der Spieltag von den man die Tore m�chte
	 * @return liefert die Tore am Spieltag spieltag zur�ck
	 */
	public int getToreSpieltag(int spieltag)
	{
		int tore;
		try{
			tore=this.toreSpieltag.get(spieltag);
			return tore;
		}catch(IndexOutOfBoundsException e)
		{
			return -1;
		}

	}

	/**
	 * Nur f�r das Kopieren n�tig
	 * @return Die liste welche die Tore an Spieltag x enth�lt
	 */
	private LinkedList<Integer> getToreSpieltag()
	{
		return this.toreSpieltag;
	}
	
	/**
	 * 
	 * @param tore setzt die Tore am Spieltag
	 */
	public void setGgtoreSpieltag(int tore)
	{
		this.ggtoreSpieltag.add(tore);
	}
	
	/**
	 * 
	 * @param spieltag Der Spieltag von den man die gegenTore m�chte
	 * @return liefert die gegenTore am Spieltag spieltag zur�ck
	 */
	public int getGgtoreSpieltag(int spieltag)
	{
		return this.ggtoreSpieltag.get(spieltag);
	}
	/**
	 * Nur f�r das Kopieren n�tig
	 * @return Die liste welche die gegenTore an Spieltag x enth�lt
	 */
	private LinkedList<Integer> getGgtoreSpieltag()
	{
		return this.ggtoreSpieltag;
	}
	/**
	 * 
	 * @param platz setzt den MaximalenPlatz
	 */
	public void setMaxPlatzSpieltag(int platz) {
		this.maxPlatzSpieltag.addFirst(platz*(-1));
	}
	
	/**
	 * 
	 * @return liefert die liste mit den Maximalen Spieltagen zur�ck
	 */
	public LinkedList<Integer> getMaxPlatzSpieltag()
	{
		return this.maxPlatzSpieltag;
	}
	
	/**
	 * 
	 * @param platz setzt den Minimalen Platz
	 */
	public void setMinPlatzSpieltag(int platz) {
		this.minPlatzSpieltag.addFirst(platz*(-1));
	}

	/**
	 * 
	 * @return  liefert die liste mit den Minimalen Spieltagen zur�ck
	 */
	public LinkedList<Integer> getMinPlatzSpieltag() {
		return this.minPlatzSpieltag;
	}
	
}
