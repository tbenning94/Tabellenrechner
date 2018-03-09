package Fachkonzept;

import java.io.Serializable;

public interface Sportart extends Serializable {
	Zaehlweise getZaehlweise();
	String getURL();
	String getLokalURL();
}
