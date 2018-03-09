package Fachkonzept;

import java.io.Serializable;

public abstract class Austragungsart implements Serializable{
	private static final long serialVersionUID = 1L;
	private final int gespieltBisTag;//Der Spieltag bis zu dem gespielt wurde
	private int anzahlSpieltage;
	private int comparatorNr;
	private transient ComparatorChain<Team> cc;
	
	public Austragungsart(int gespieltBisTag,int comparatorNr)
	{
		this.gespieltBisTag=gespieltBisTag;
		this.comparatorNr=comparatorNr;
		setComparatorChain(comparatorNr);
	}
	
	public Austragungsart(int gespieltBisTag,int anzahlSpieltage, int comparatorNr)
	{
		this.gespieltBisTag=gespieltBisTag;
		this.comparatorNr=comparatorNr;
		this.anzahlSpieltage=anzahlSpieltage;
		setComparatorChain(comparatorNr);
	}
	
	/**
	 * Gibt den Spieltag zur¸ck bis zu dem einschlieﬂlich Gespielt wurde
	 * @return Tag des zuletzt gespielten Spiels
	 */
	public int getGespieltBisTag()
	{
		return this.gespieltBisTag;
	}

	
	
	public int getAnzahlSpieltage() {
		return anzahlSpieltage;
	}
	
	public void setAnzahlSpieltage(int anzahlSpieltage){
		this.anzahlSpieltage=anzahlSpieltage;
	}

	public ComparatorChain<Team> getComparatorChain() {
		return cc;
	}
	
	public void setComparatorChain(int comparatorNr){
		this.comparatorNr=comparatorNr;
		switch(comparatorNr){
		case 1: cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.DIREKTER_VERGLEICH_PUNKTE_COMPARATOR ,ComparatorChain.DIREKTER_VERGLEICH_TORDIFFERENZ_COMPARATOR, ComparatorChain.DIREKTER_VERGLEICH_ANZAHL_TORE_COMPARATOR, ComparatorChain.DIREKTER_VERGLEICH_ANZAHL_AUSW_TORE_COMPARATOR);System.out.println("Sortierung: Fuﬂball EM"); break;
		case 2: cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.TORDIFFERENZ_COMPARATOR, ComparatorChain.ERZIELTE_TORE_COMPARATOR); System.out.println("Sortierung: Fuﬂballbundesliga");break;
		case 3: cc = new ComparatorChain<Team> (ComparatorChain.PUNKTE_COMPARATOR,ComparatorChain.DIREKTER_VERGLEICH_PUNKTE_COMPARATOR,ComparatorChain.DIREKTER_VERGLEICH_TORDIFFERENZ_COMPARATOR); System.out.println("Sortierung: Handballbundesliga/EM");break;
		default:cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR,ComparatorChain.TORDIFFERENZ_COMPARATOR); System.out.println("Sortierung default");break;
		}
		
	}
	
	public int getComparatorNr()
	{
		return this.comparatorNr;
	}

}
