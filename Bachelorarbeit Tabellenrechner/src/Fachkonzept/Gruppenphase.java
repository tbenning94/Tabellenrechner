package Fachkonzept;

public class Gruppenphase extends Austragungsart
{
	private final int anzahlGruppen;
	private final Liga[] gruppen;
	
	public Gruppenphase(int anzahlGruppen, int gespieltBisTag, int comparatorNr) {
		super(gespieltBisTag,comparatorNr);
		this.anzahlGruppen=anzahlGruppen;
		this.gruppen=new Liga[anzahlGruppen];
	}
	
	public int getAnzahlGruppen() {
		return anzahlGruppen;
	}

	public Liga[] getGruppen() {
		return gruppen;
	}
}
