package Fachkonzept;

public class Meisterschaft extends Austragungsart{

	private static final long serialVersionUID = 1L;

	
	
	public Meisterschaft(int gespieltBisTag,int comparatorNr) {
		super(gespieltBisTag, comparatorNr);

	}

	public Meisterschaft(int gespieltBisTag,int anzahlSpieltage,int comparatorNr) {
		super(gespieltBisTag,anzahlSpieltage, comparatorNr);

	}
	
}
