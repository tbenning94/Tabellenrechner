package Fachkonzept;

public class Handball extends SportartImplementierung{

	private static final long serialVersionUID = 1L;

	public Handball() {
		super(new ZaehlweisemitMinuspunkte(2, 1, 0, 0, 1, 2), "http://www.openligadb.de/api/getmatchdata/hbl/");
	}

}
