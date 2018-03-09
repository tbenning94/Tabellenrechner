package Fachkonzept;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * http://openbook.rheinwerk-verlag.de/javainsel/javainsel_08_001.html#dodtp04c98cfe-ca41-4780-b6ea-2a8d185203f2
 * 
 * 	
	 * Sortiert die Teams 
	 * http://s.bundesliga.de/assets/doc/1060000/1059631_original.pdf
	 * Bei    Punktgleichheit    in    der    Bundesliga    und    der    2.    Bundesliga    werden 
		nachstehende  Kriterien  in  der  aufgeführten  Reihenfolge  zur  Ermittlung  der 
		Platzierung herangezogen:
		1) die nach dem Subtraktionsverfahren ermittelte Tordifferenz
		2) Anzahl der erzielten Tore
		nachstehende regeln wurden nicht implementiert
		3)das Gesamtergebnis aus Hin-und Rückspiel im direkten Vergleich
		4)die Anzahl der auswärts erzielten Tore im direkten Vergleich
		5) die Anzahl aller auswärts erzielten Tore.
		6) Ist   auch   die   Anzahl   aller   auswärts   erzielten   Tore   gleich,   findet   ein 
		Entscheidungsspiel auf neutralem Platz statt. 
	 
 * @author T.Benning
 *
 */
public class ComparatorChain<E> implements Comparator<E> {
	private List<Comparator<E>> comparatorChain = new ArrayList<Comparator<E>>();
	private static Koordinator k;
	public final static int CC_FUßBALL_EM = 1;
	public final static int CC_FUßBALL_MEISTERSCHAFT = 2 ;
	public final static int CC_HANDBALL_MEISTERSCHAFT = 3;
	public final static int CC_HANDBALL_EM = 3; //TODO
	public static Team t;

	public static int gruppe;
	
	 @SafeVarargs  // ab Java 7
	  public ComparatorChain( Comparator<E>... comparators )
	  {
		 k=Koordinator.getKoordinator();
	    if ( comparators == null )
	      throw new IllegalArgumentException( "Argument is not allowed to be null" );

	    Collections.addAll( comparatorChain, comparators );
	  }	 
	 
	 public void addComparator( Comparator<E> comparator )
	  {
	    if ( comparator == null )
	      throw new IllegalArgumentException( "Argument is not allowed to be null" );

	    comparatorChain.add( comparator );
	  }
	 
	 @Override
	  public int compare( E o1, E o2 )
	  {
	    if ( comparatorChain.isEmpty() )
	      throw new UnsupportedOperationException(
	                  "Unable to compare without a Comparator in the chain" );

	    for ( Comparator<E> comparator : comparatorChain )
	    {
	      int order = comparator.compare( o1, o2 );
	      if ( order != 0 )
	        return order;
	    }

	    return 0;
	  }
	
	
	
	
	public final static Comparator<Team> PUNKTE_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			return t2.getPunkte()-t1.getPunkte();
		}
    };
    
    public final static Comparator<Team> TORDIFFERENZ_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			return t2.getDifferenz()-t1.getDifferenz();
		}
    };
    
    public final static Comparator<Team> ERZIELTE_TORE_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			return t2.getTore()-t1.getTore();
		}
    };
    
    public final static Comparator<Team> MAX_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			if(t==null)return 1;
			if(t.getName().equals(t1.getName()))
			{
				return -1;
			}else{
				if(t.getName().equals(t2.getName()))
				{
					return 1;
				}
			}
			return 1;
		}
    };
    
    public final static Comparator<Team> MIN_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			if(t==null)return 1;
			if(t.getName().equals(t1.getName()))
			{
				return 1;
			}else{
				if(t.getName().equals(t2.getName()))
				{
					return -1;
				}
			}
			return 1;
		}
    };
   
    public final static Comparator<Team> DIREKTER_VERGLEICH_PUNKTE_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
			int punkteDirekterVergleichTeam1 = 0;
			int punkteDirekterVergleichTeam2 = 0;
			for (int i = 0; i < alleSpiele.size(); i = i + 2) {
				if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
				{
					if(i>=((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getAnzahlGespielt()/2)
					{
						break;
					}
				}else{
					if(i>=k.getLiga().getAnzahlGespielt()/2){
						break;//TODO überprüfen
					}
				}
							
			
					int spieltag = (int) i / k.getAktiveLiga().getTeams().length; // gucken an welchen spieltag man sich befindet
	
					//System.out.println("i: "+i+"   spieltag: "+spieltag+"   alleSpiele.size(): "+alleSpiele.size());
					if(t1.getName().equals(alleSpiele.get(i).getName())&&t2.getName().equals(alleSpiele.get(i+1).getName()))
					{

						if(alleSpiele.get(i).getToreSpieltag(spieltag)>alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam1+=3;
						}
						if(alleSpiele.get(i).getToreSpieltag(spieltag)<alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam2+=3;
						}
						if(alleSpiele.get(i).getToreSpieltag(spieltag)==alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam1+=1;
							punkteDirekterVergleichTeam2+=1;
						}
						System.out.println("if1");
						System.out.println(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName() + " ("
								+ alleSpiele.get(i).getToreSpieltag(spieltag) + " : "
								+ alleSpiele.get(i + 1).getToreSpieltag(spieltag) + ")");
					}
					if(t2.getName().equals(alleSpiele.get(i).getName())&&t1.getName().equals(alleSpiele.get(i+1).getName()))
					{
						if(alleSpiele.get(i).getToreSpieltag(spieltag)>alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam2+=3;
						}
						if(alleSpiele.get(i).getToreSpieltag(spieltag)<alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam1+=3;
						}
						if(alleSpiele.get(i).getToreSpieltag(spieltag)==alleSpiele.get(i + 1).getToreSpieltag(spieltag))
						{
							punkteDirekterVergleichTeam1+=1;
							punkteDirekterVergleichTeam2+=1;
						}
						System.out.println("if2");
						System.out.println(alleSpiele.get(i).getName() + " vs. " + alleSpiele.get(i + 1).getName() + " ("
								+ alleSpiele.get(i).getToreSpieltag(spieltag) + " : "
								+ alleSpiele.get(i + 1).getToreSpieltag(spieltag) + ")");
					}
				}	
			
			System.out.println("punkteDV1: "+punkteDirekterVergleichTeam1+"   punkteDV2: "+punkteDirekterVergleichTeam2);
			if(punkteDirekterVergleichTeam1==0&&punkteDirekterVergleichTeam2==0)
			{
				return 0;
			}
			return punkteDirekterVergleichTeam2-punkteDirekterVergleichTeam1;
		}
    };
    
    public final static Comparator<Team> DIREKTER_VERGLEICH_ANZAHL_TORE_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
			int toreDirekterVergleichTeam1 = 0;
			int toreDirekterVergleichTeam2 = 0;
			for (int i = 0; i < alleSpiele.size(); i = i + 2) {

							
				if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
				{
					if(i>=((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getAnzahlGespielt()/2)
					{
						break;
					}
				}else{
					if(i>=k.getLiga().getAnzahlGespielt()/2){
						break;//TODO überprüfen
					}
				}
				
				{
					int spieltag = (int) i / k.getAktiveLiga().getTeams().length; // gucken an welchen spieltag man sich befindet
					
					if(t1.getName().equals(alleSpiele.get(i).getName())&&t2.getName().equals(alleSpiele.get(i+1).getName()))
					{
						toreDirekterVergleichTeam1+=alleSpiele.get(i).getToreSpieltag(spieltag);
						toreDirekterVergleichTeam2+=alleSpiele.get(i+1).getToreSpieltag(spieltag);						
					}
					if(t2.getName().equals(alleSpiele.get(i).getName())&&t1.getName().equals(alleSpiele.get(i+1).getName()))
					{
						toreDirekterVergleichTeam2+=alleSpiele.get(i).getToreSpieltag(spieltag);
						toreDirekterVergleichTeam1+=alleSpiele.get(i+1).getToreSpieltag(spieltag);						
					}
				}	
			}
			return toreDirekterVergleichTeam2-toreDirekterVergleichTeam1;
		}
    };
    
    public final static Comparator<Team> DIREKTER_VERGLEICH_TORDIFFERENZ_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
			int torDiffDirekterVergleichTeam1 = 0;
			int torDiffDirekterVergleichTeam2 = 0;
			for (int i = 0; i < alleSpiele.size(); i = i + 2) {

							
				if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
				{
					if(i>=((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getAnzahlGespielt()/2)
					{
						break;
					}
				}else{
					if(i>=k.getLiga().getAnzahlGespielt()/2){
						break;//TODO überprüfen
					}
				}
				
				{
					int spieltag = (int) i / k.getAktiveLiga().getTeams().length; // gucken an welchen spieltag man sich befindet
					
					if(t1.getName().equals(alleSpiele.get(i).getName())&&t2.getName().equals(alleSpiele.get(i+1).getName()))
					{
						int torDiff1=alleSpiele.get(i).getToreSpieltag(spieltag)-alleSpiele.get(i).getGgtoreSpieltag(spieltag);
						int torDiff2=alleSpiele.get(i+1).getToreSpieltag(spieltag)-alleSpiele.get(i+1).getGgtoreSpieltag(spieltag);
						torDiffDirekterVergleichTeam1+=torDiff1;
						torDiffDirekterVergleichTeam2+=torDiff2;						
					}
					if(t2.getName().equals(alleSpiele.get(i).getName())&&t1.getName().equals(alleSpiele.get(i+1).getName()))
					{
						int torDiff2=alleSpiele.get(i).getToreSpieltag(spieltag)-alleSpiele.get(i).getGgtoreSpieltag(spieltag);
						int torDiff1=alleSpiele.get(i+1).getToreSpieltag(spieltag)-alleSpiele.get(i+1).getGgtoreSpieltag(spieltag);
						torDiffDirekterVergleichTeam1+=torDiff1;
						torDiffDirekterVergleichTeam2+=torDiff2;	
					}
				}	
			}
			return torDiffDirekterVergleichTeam2-torDiffDirekterVergleichTeam1;
		}
    };
    
    public final static Comparator<Team> DIREKTER_VERGLEICH_ANZAHL_AUSW_TORE_COMPARATOR = new Comparator<Team>() {
		@Override
		public int compare(Team t1, Team t2) {
			LinkedList<Team> alleSpiele = k.getAktiveLiga().getAlleSpielPaarungen();
			int ausw_toreDirekterVergleichTeam1 = 0;
			int ausw_toreDirekterVergleichTeam2 = 0;
			for (int i = 0; i < alleSpiele.size(); i = i + 2) {

							
				if(k.getLiga().getAustragungsart() instanceof Gruppenphase)
				{
					if(i>=((Gruppenphase) k.getLiga().getAustragungsart()).getGruppen()[gruppe].getAnzahlGespielt()/2)
					{
						break;
					}
				}else{
					if(i>=k.getLiga().getAnzahlGespielt()/2){
						break;//TODO überprüfen
					}
				}
				{
					int spieltag = (int) i / k.getAktiveLiga().getTeams().length; // gucken an welchen spieltag man sich befindet
					
					if(t1.getName().equals(alleSpiele.get(i).getName())&&t2.getName().equals(alleSpiele.get(i+1).getName()))
					{
						ausw_toreDirekterVergleichTeam2=alleSpiele.get(i+1).getToreSpieltag(spieltag);
					}
					if(t2.getName().equals(alleSpiele.get(i).getName())&&t1.getName().equals(alleSpiele.get(i+1).getName()))
					{
						ausw_toreDirekterVergleichTeam1=alleSpiele.get(i).getToreSpieltag(spieltag);						
					}
				}	
			}
			return ausw_toreDirekterVergleichTeam2-ausw_toreDirekterVergleichTeam1;
		}
    };
}
