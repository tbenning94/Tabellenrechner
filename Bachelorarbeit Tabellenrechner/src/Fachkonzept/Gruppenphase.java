package Fachkonzept;

public class Gruppenphase extends Austragungsart
{
	private static final long serialVersionUID = 1L;
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
