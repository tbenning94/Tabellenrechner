package Fachkonzept;

public abstract class SportartImplementierung implements Sportart{

	private static final long serialVersionUID = 1L;
	private final Zaehlweise zaehlweise;
	private final String url;
	private String lokalUrl;
	
	public SportartImplementierung(Zaehlweise zaehlweise,String url)
	{
		this.zaehlweise=zaehlweise;
		this.url=url;
	}

	public Zaehlweise getZaehlweise() {
		return this.zaehlweise;
	}

	public String getURL() {
		return this.url;
	}

	public String getLokalURL() {
		String path=System.getProperty("user.dir")+"/"+"src/Ergebnisse/";
		lokalUrl="file:///"+path;
		return this.lokalUrl;
	}
}
