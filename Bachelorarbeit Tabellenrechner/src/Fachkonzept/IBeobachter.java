package Fachkonzept;

import Fachkonzept.Team;

/**
 * Die Beobachter m�ssen auf Ereignisse reagieren/aktuallisieren
 * @author T.Benning
 *
 */
public interface IBeobachter {
	/**
	 * eine Aktualisierung die stattfindet sobald die Berechnung beendet ist
	 * @param t das Team welches informationen f�r die aktualisierung beinhaltet
	 */
	public void update(Team t);
	
	/**
	 * eine Aktualisierung die stattfindet bevor die Berechnung gestartet werden
	 * @param t das Team welches Informationen f�r die aktualisierung beinhaltet
	 */
	public void updateAnfang(Team t);
	
	public void updateCancelled(Team t);

}
