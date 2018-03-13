package Fachkonzept;

public class Fuﬂball extends SportartImplementierung{

	private static final long serialVersionUID = 1L;

	public Fuﬂball(int ligaNr) {
		super(new Zaehlweise(3, 1, 0), "https://www.openligadb.de/api/getmatchdata/bl"+ligaNr+"/");
	}

}
