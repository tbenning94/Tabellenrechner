package Fachkonzept;

import java.io.Serializable;

public class Zaehlweise implements Serializable{

	private static final long serialVersionUID = 1L;
	public static int PUNKTE_S; //wie viele Punkte es für einen Sieg gibt
	public static int PUNKTE_U; //wie viele Punkte es für ein Unentschieden gibt
	public static int PUNKTE_N; //wie viele Punkte es für eine Niederlage gibt
	
	private int S;
	private int U;
	private int N;
	
	public Zaehlweise(int punkteS, int punkteU, int punkteN)
	{
		PUNKTE_S=punkteS;
		PUNKTE_U=punkteU;
		PUNKTE_N=punkteN;
		S=punkteS;
		U=punkteU;
		N=punkteN;
	}
	
	public Zaehlweise(Zaehlweise z)
	{
		PUNKTE_S=z.S;
		PUNKTE_U=z.U;
		PUNKTE_N=z.N;
	}
}
