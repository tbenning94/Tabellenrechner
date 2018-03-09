package Fachkonzept;


/**
 * Das Interface Informant stellt sicher 
 * dass alle Nötigen Methoden implementiert werden müssen
 * @author T.Benning
 *
 */
public interface Informant {
	/**
	 * 
	 * @param beobachter der Beobachter der hinzugefügt werden soll
	 */
	public void addBeobachter(Beobachter beobachter);
	/**
	 * @param team alle Beobachter informieren
	 */
	public void notifyAlleBeobachter(Team team);
	
	/**
	 * 
	 * @param beobachter entfernt den Beobachter aus der liste
	 */
	public void removeBeobachter(Beobachter beobachter);
}
