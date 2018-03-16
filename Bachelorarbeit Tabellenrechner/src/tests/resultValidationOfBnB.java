package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import Fachkonzept.Koordinator;
import Fachkonzept.Team;

public class resultValidationOfBnB {
	//erster Wert Ergebnis von Spieltag 29, dann 30...
	//aus der Saison 2015/16
	Integer[] bayernMuenchenMax = {-1,-1,-1,-1,-1};
	Integer[] bayernMuenchenMin = {-2,-2,-2,-2,-1};	
	
	Integer[] bvbMax = {-1,-1,-1,-1,-2};
	Integer[] bvbMin = {-2,-2,-2,-2,-2};
	
	Integer[] bayerLeverkusenMax={-3,-3,-3,-3,-3};
	Integer[] bayerLeverkusenMin={-12,-9,-7,-3,-3};
	
	Integer[] borussiaMoenchengladbachMax={-3,-3,-3,-4,-4};
	Integer[] borussiaMoenchengladbachMin={-14,-14,-10,-7,-6};
	
	Integer[] mainzMax={-3,-3,-3,-4,-4};
	Integer[] mainzMin={-14,-14,-11,-9,-7};
	
	Integer[] hertaBSCMax={-3,-3,-3,-4,-4};
	Integer[] hertaBSCMin={-11,-10,-9,-7,-7};
	
	Integer[] schalkeMax={-3,-3,-3,-4,-4};
	Integer[] schalkeMin={-14,-14,-11,-7,-7};
	
	Integer[] wolfsburgMax={-3,-4,-5,-8,-8};
	Integer[] wolfsburgMin={-17,-17,-16,-16,-10};
	
	Integer[] koelnMax={-4,-4,-4,-7,-8};
	Integer[] koelnMin={-17,-17,-15,-14,-10};
	
	Integer[] ingolstadtMax={-3,-4,-4,-7,-8};
	Integer[] ingolstadtMin={-17,-17,-15,-15,-13};
	
	Integer[] hsvMax={-4,-5,-6,-8,-10};
	Integer[] hsvMin={-17,-17,-17,-16,-15};
	
	Integer[] augsburgMax={-5,-5,-6,-8,-10};
	Integer[] augsburgMin={-18,-18,-17,-17,-15};
	
	Integer[] darmstadtMax={-5,-5,-8,-8,-10};
	Integer[] darmstadtMin={-18,-17,-17,-17,-15};
	
	Integer[] hoffenheimMax={-5,-5,-8,-8,-10};
	Integer[] hoffenheimMin={-18,-17,-17,-17,-15};
	
	Integer[] frankfurtMax={-8,-8,-11,-11,-11};
	Integer[] frankfurtMin={-18,-18,-18,-17,-17};
	
	Integer[] bremenMax={-8,-8,-8,-9,-12};
	Integer[] bremenMin={-18,-18,-18,-17,-17};
	
	Integer[] stuttgartMax={-4,-5,-8,-11,-16};
	Integer[] stuttgartMin={-18,-18,-17,-17,-17};
	
	Integer[] hannoverMax={-12,-15,-17,-18,-18};
	Integer[] hannoverMin={-18,-18,-18,-18,-18};
	
	Koordinator k;
	
	@Test
	public void testBnBOhneInitLoesung() throws IOException{
		bnb(false);
	}
	
	@Test
	public void testBnBMitInitLoesung() throws IOException{
		bnb(true);
	}
	
	public void bnb(boolean mitInitLoesung) throws IOException {
		k=Koordinator.getKoordinator();
		
		for(int i=0; i<=4; i++){
			k.setLiga("1. Fußballbundesliga", 2015, 29+i);
			for(Team team:k.getAktiveLiga().getTeams()){
				if(mitInitLoesung){
					k.getAlgorithmus().testMitInitLoesung(team);					
				}else{
					k.getAlgorithmus().testOhneInitLoesung(team);
				}
				
				switch(team.getName()){
				case "Bayern München":proof(i,team,bayernMuenchenMax,bayernMuenchenMin);
				break;
				case "Borussia Dortmund": proof(i,team,bvbMax,bvbMin);
				break;
				case "Bayer 04 Leverkusen": proof(i,team,bayerLeverkusenMax,bayerLeverkusenMin);
					break;
				case "Borussia Mönchengladbach":proof(i,team,borussiaMoenchengladbachMax,borussiaMoenchengladbachMin);
					break;
				case "1. FSV Mainz 05":proof(i,team,mainzMax,mainzMin);
					break;
				case "Hertha BSC":proof(i,team,hertaBSCMax,hertaBSCMin);
					break;
				case "FC Schalke 04":proof(i,team,schalkeMax,schalkeMin);
					break;
				case "VfL Wolfsburg":proof(i,team,wolfsburgMax,wolfsburgMin);
					break;
				case "1. FC Köln":proof(i,team,koelnMax,koelnMin);
					break;
				case "FC Ingolstadt 04":proof(i,team,ingolstadtMax,ingolstadtMin);
					break;
				case "Hamburger SV":proof(i,team,hsvMax,hsvMin);
					break;
				case "FC Augsburg":proof(i,team,augsburgMax,augsburgMin);
					break;
				case "SV Darmstadt 98":proof(i,team,darmstadtMax,darmstadtMin);
					break;
				case "TSG 1899 Hoffenheim":proof(i,team,hoffenheimMax,hoffenheimMin);
					break;
				case "Eintracht Frankfurt":proof(i,team,frankfurtMax,frankfurtMin);
					break;
				case "Werder Bremen":proof(i,team,bremenMax,bremenMin);
					break;
				case "VfB Stuttgart":proof(i,team,stuttgartMax,stuttgartMin);
					break;
				case "Hannover 96":proof(i,team,hannoverMax,hannoverMin);
					break;
				default: fail("Team "+team.getName()+" not found");
				}
			}
		}
		//fail("Not yet implemented");
	} 
	
	public void proof(int i,Team team,Object[] max,Object[] min){
		assertEquals("MAX "+team.getName(), max[i], team.getMaxPlatzSpieltag().getLast());
		assertEquals("MIN "+team.getName(), min[i], team.getMinPlatzSpieltag().getLast());
	}
}
