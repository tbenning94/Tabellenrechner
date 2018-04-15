package Fachkonzept;

public class Fuﬂball extends SportartImplementierung{


	public Fuﬂball(String ligaID) {
		this("https://www.openligadb.de/api/getmatchdata/",ligaID);
	}
	
	public Fuﬂball(String url,String ligaID) {
		super(new Zaehlweise(3, 1, 0), url+ligaID+"/");
	}

}
