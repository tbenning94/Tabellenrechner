package Fachkonzept;

public class Fu�ball extends SportartImplementierung{

	private static final long serialVersionUID = 1L;

	public Fu�ball() {
		super(new Zaehlweise(3, 1, 0), "http://www.openligadb.de/api/getmatchdata/bl1/");
	}

}
