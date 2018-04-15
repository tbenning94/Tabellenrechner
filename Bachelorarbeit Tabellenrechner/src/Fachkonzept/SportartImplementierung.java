package Fachkonzept;

public abstract class SportartImplementierung implements ISportart{

	private final Zaehlweise zaehlweise;
	private final String url;
	
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
}
