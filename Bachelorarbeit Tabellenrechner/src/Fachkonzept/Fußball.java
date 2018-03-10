package Fachkonzept;

public class Fuﬂball extends SportartImplementierung{

	private static final long serialVersionUID = 1L;

	public Fuﬂball() {
		super(new Zaehlweise(3, 1, 0), "http://www.openligadb.de/api/getmatchdata/bl1/");
	}

}
