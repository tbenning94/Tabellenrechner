package Fachkonzept;

public class Fu�ball extends SportartImplementierung{


	public Fu�ball(String ligaID) {
		this("https://www.openligadb.de/api/getmatchdata/",ligaID);
	}
	
	public Fu�ball(String url,String ligaID) {
		super(new Zaehlweise(3, 1, 0), url+ligaID+"/");
	}

}
