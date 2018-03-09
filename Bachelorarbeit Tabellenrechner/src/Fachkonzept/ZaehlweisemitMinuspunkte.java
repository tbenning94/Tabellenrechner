package Fachkonzept;

public class ZaehlweisemitMinuspunkte extends Zaehlweise{

	private static final long serialVersionUID = 1L;
	public static int PUNKTE_S_MINUS=0;
	public static int PUNKTE_U_MINUS=0;
	public static int PUNKTE_N_MINUS=0;
	
	public ZaehlweisemitMinuspunkte(int punkteS, int punkteU, int punkteN, int punkteS_Minus, int punkteU_Minus, int punkteN_Minus)
	{
		super(punkteS,punkteU,punkteN);
		PUNKTE_S_MINUS=punkteS_Minus;
		PUNKTE_N_MINUS=punkteN_Minus;
		PUNKTE_U_MINUS=punkteU_Minus;
	}

	
}
