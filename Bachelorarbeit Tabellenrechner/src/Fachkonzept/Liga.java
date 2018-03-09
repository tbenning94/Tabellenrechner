package Fachkonzept;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Liga implements Serializable{

	private static final long serialVersionUID = 1L;
	private final int jahr;			//das jahr(saison) in dem gespielt wird
	private final String liga;
	private final Sportart sportart;
	private final Austragungsart austragungsart;
	private int anzahlGespielt;
	private int anzahlAusstehend;
	private LinkedList<Team> alleSpielPaarungen; //!=teams
	private Team teams[];
	
	//Flags die angeben wie Sortiert werden soll
	public static final int SORTIERUNG_NORMAL = 1;
	public static final int SORTIERUNG_MAX = 2;
	public static final int SORTIERUNG_MIN = 3;
	
	public Liga(String liga, int jahr, Sportart sportart, Austragungsart austragungsart)
	{
		this.alleSpielPaarungen=new LinkedList<Team>();
		this.liga=liga;
		this.jahr=jahr;
		this.sportart=sportart;
		this.austragungsart=austragungsart;
	}
	
	/**
	 * Gibt das Jahr/Saison zurück in dem gespielt wird
	 * @return das Jahr/Saion
	 */
	public int getJahr()
	{
		return this.jahr;
	}
	public String getLiga() {
		return liga;
	}

	public Sportart getSportart() {
		return sportart;
	}

	public Austragungsart getAustragungsart() {
		return austragungsart;
	}
	/**
	 * Liefert alle Teams in Sortierter Reihenfolge zurück. 
	 * @return Alle Teams die Spielen 
	 */
	public Team[] getTeams()
	{
		return teams;
	}
	

	/**
	 * Fügt ein Team in die Liste der Teams hinzu
	 * @param teamnamen Das Team, welches hinzugefügt werden soll
	 */
	public void addTeams(LinkedList<String> teamnamen)
	{

		if(teams==null) teams=new Team[teamnamen.size()];
		for(int i=0;i<teamnamen.size();i++)
		{
			teams[i]=new Team(teamnamen.get(i));
		}
	}
	
	/**
	 * Ermittelt die Sortierung, welche sich nach dem flag Sortierung richtet
	 * @param t Die Teams die sortiert werden sollen
	 * @param sortierung die Sortierung(1 SortierungNormal, 2 SortierungMax, 3SortierungMin)
	 */
	public void ermittelPlatzierung(Team[] t,int sortierung,Team team)
	{	
		List<Team> listeSortieren=Arrays.asList(t);
		
		if(sortierung==SORTIERUNG_NORMAL)
		{
			ComparatorChain.t=null;
			Collections.sort( listeSortieren, getAustragungsart().getComparatorChain() );
			//Collections.sort( listeSortieren, new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.TORDIFFERENZ_COMPARATOR,ComparatorChain.ERZIELTE_TORE_COMPARATOR ) );
		}
		if(sortierung==SORTIERUNG_MAX)
		{
			ComparatorChain.t=team;
			Collections.sort( listeSortieren, new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.MAX_COMPARATOR ) );
		}
		if(sortierung==SORTIERUNG_MIN)
		{
			ComparatorChain.t=team;
			Collections.sort( listeSortieren, new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.MIN_COMPARATOR ) );
		}
		
		for(int i=0; i<listeSortieren.size();i++)
		{
			listeSortieren.get(i).setPlatzierung(i+1);
		}
	}
	

	
	
	/**
	 * Liefert alle Spielpaarungen zurück. Hierbei ist zu beachten, dass ein ungrades Team gegen ein grades Team antritt
	 * also 0vs1, 2vs3, 4vs5...
	 * @return Liefert alle Spielpaarungen zurück
	 */
	public LinkedList<Team> getAlleSpielPaarungen()
	{
		return this.alleSpielPaarungen;
	}
	/**
	 * 
	 * @param s fügt die noch zu spielenden spiele hinzu
	 */
	public void addAusstehendeSpiele(LinkedList<String> s)
	{
		for(int i=0; i<s.size();i++)
		{
			for(int j=0;j<teams.length;j++)
			{
				if(s.get(i).equals(teams[j].getName()))
				{					
					this.alleSpielPaarungen.add(new Team(teams[j]));
					this.anzahlAusstehend++;
				}
			}
		}
	}
	
	/**
	 * @param t fügt alle bereits ausgetragenen Spiele hinzu
	 */
	public void addAusgetrageneSpiele(Team[][] t)
	{
		for(int i=0; i<t.length;i++)
		{
			for(int j=0;j<t[i].length;j++)
			{
				alleSpielPaarungen.add(t[i][j]);
				anzahlGespielt++;
			}
			
		}
	}
	

	


	/**
	 * 
	 * @return Anzahl der Gespielten Spiele
	 */
	public int getAnzahlGespielt()
	{
		return this.anzahlGespielt;
	}
	/**
	 * 
	 * @return Anzahl der Ausstehenden Spiele
	 */
	public int getAnzahlAusstehend()
	{
		return this.anzahlAusstehend;
	}

}
