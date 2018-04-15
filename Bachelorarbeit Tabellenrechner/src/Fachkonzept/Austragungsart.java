package Fachkonzept;

public abstract class Austragungsart {
	private final int gespieltBisTag;// Der Spieltag bis zu dem gespielt wurde
	private int anzahlSpieltage;
	private int comparatorNr;
	private transient ComparatorChain<Team> cc;

	public Austragungsart(int gespieltBisTag, int comparatorNr) {
		this.gespieltBisTag = gespieltBisTag;
		this.comparatorNr = comparatorNr;
		setComparatorChain(comparatorNr);
	}

	public Austragungsart(int gespieltBisTag, int anzahlSpieltage, int comparatorNr) {
		this.gespieltBisTag = gespieltBisTag;
		this.comparatorNr = comparatorNr;
		this.anzahlSpieltage = anzahlSpieltage;
		setComparatorChain(comparatorNr);
	}

	/**
	 * Gibt den Spieltag zur¸ck bis zu dem einschlieﬂlich Gespielt wurde
	 * 
	 * @return Tag des zuletzt gespielten Spiels
	 */
	public int getGespieltBisTag() {
		return this.gespieltBisTag;
	}

	public int getAnzahlSpieltage() {
		return anzahlSpieltage;
	}

	public void setAnzahlSpieltage(int anzahlSpieltage) {
		this.anzahlSpieltage = anzahlSpieltage;
	}

	public ComparatorChain<Team> getComparatorChain() {
		return cc;
	}

	public void setComparatorChain(int comparatorNr) {
		this.comparatorNr = comparatorNr;
		switch (comparatorNr) {
		// Sortierung: Fuﬂball EM
		case 1:
			cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_PUNKTE_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_TORDIFFERENZ_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_ANZAHL_TORE_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_ANZAHL_AUSW_TORE_COMPARATOR);
			break;
		// "Sortierung: Fuﬂballbundesliga"
		case 2:
			cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.TORDIFFERENZ_COMPARATOR,
					ComparatorChain.ERZIELTE_TORE_COMPARATOR);
			break;
		// "Sortierung: Handballbundesliga/EM"
		case 3:
			cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_PUNKTE_COMPARATOR,
					ComparatorChain.DIREKTER_VERGLEICH_TORDIFFERENZ_COMPARATOR);
			break;
		// "Sortierung default"
		default:
			cc = new ComparatorChain<Team>(ComparatorChain.PUNKTE_COMPARATOR, ComparatorChain.TORDIFFERENZ_COMPARATOR);
			break;
		}

	}

	public int getComparatorNr() {
		return this.comparatorNr;
	}

}
