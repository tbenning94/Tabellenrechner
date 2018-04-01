package Fachkonzept;

import java.util.ArrayList;

public class InitialeLoesung {
	private Liga liga;
	private Team team;

	public InitialeLoesung(Liga liga, Team team) {
		this.liga = liga;
		this.team = team;
	}

	protected int erstelleInitialeLoesungVarianteB(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			boolean isMaxCalculation) {
		Team[] tmpTeam = null;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < l1.size(); j++) {
				if (tmpTeam[i].getName().equals(((Team) l1.get(j)).getName())) {
					tmpTeam[i].unentschieden();
				}
				if (tmpTeam[i].getName().equals(((Team) l2.get(j)).getName())) {
					tmpTeam[i].unentschieden();
				}
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, isMaxCalculation);
	}

	protected int erstelleInitialeLoesungVarianteC(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			boolean isMaxCalculation) {
		Team[] tmpTeam = null;
		int durchschnitt = 0;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
			for (int j = 0; j < l1.size(); j++) {
				if (tmpTeam[i].getName().equals(((Team) l1.get(j)).getName())) {
					durchschnitt += tmpTeam[i].getPunkte();
				}
				if (tmpTeam[i].getName().equals(((Team) l2.get(j)).getName())) {
					durchschnitt += tmpTeam[i].getPunkte();
				}
			}
		}
		if (l1.size() > 0) {
			durchschnitt /= l1.size() * 2;
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < heim.size(); i++) {
			if (isMaxCalculation) {
				if ((((Team) heim.get(i)).getPunkte() > durchschnitt)
						&& (((Team) ausw.get(i)).getPunkte() > durchschnitt)) {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
				} else if ((((Team) heim.get(i)).getPunkte() > durchschnitt)
						&& (((Team) ausw.get(i)).getPunkte() <= durchschnitt)) {
					((Team) heim.get(i)).niederlage();
					((Team) ausw.get(i)).sieg();
				} else if ((((Team) heim.get(i)).getPunkte() <= durchschnitt)
						&& (((Team) ausw.get(i)).getPunkte() > durchschnitt)) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
				} else if ((((Team) heim.get(i)).getPunkte() <= durchschnitt)
						&& (((Team) ausw.get(i)).getPunkte() <= durchschnitt)) {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
				}
			} else if ((((Team) heim.get(i)).getPunkte() >= durchschnitt)
					&& (((Team) ausw.get(i)).getPunkte() >= durchschnitt)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
			} else if ((((Team) heim.get(i)).getPunkte() > durchschnitt)
					&& (((Team) ausw.get(i)).getPunkte() <= durchschnitt)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
			} else if ((((Team) heim.get(i)).getPunkte() < durchschnitt)
					&& (((Team) ausw.get(i)).getPunkte() >= durchschnitt)) {
				((Team) heim.get(i)).niederlage();
				((Team) ausw.get(i)).sieg();
			} else if ((((Team) heim.get(i)).getPunkte() < durchschnitt)
					&& (((Team) ausw.get(i)).getPunkte() < durchschnitt)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, isMaxCalculation);
	}

	protected int erstelleInitialeLoesungVarianteD(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			boolean isMaxCalculation) {
		Team[] tmpTeam = null;
		int durchschnitt = 0;
		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
			for (int j = 0; j < l1.size(); j++) {
				if (tmpTeam[i].getName().equals(((Team) l1.get(j)).getName())) {
					durchschnitt += tmpTeam[i].getPunkte();
				}
				if (tmpTeam[i].getName().equals(((Team) l2.get(j)).getName())) {
					durchschnitt += tmpTeam[i].getPunkte();
				}
			}
		}
		if (l1.size() > 0) {
			durchschnitt /= l1.size() * 2;
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < heim.size(); i++) {
			int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team) heim.get(i), heim, ausw);
			int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team) ausw.get(i), heim, ausw);
			if (isMaxCalculation) {
				if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim > ((Team) ausw.get(i)).getPunkte()
						+ 3 * offeneSpieltageAusw) {
					((Team) heim.get(i)).niederlage();
					((Team) ausw.get(i)).sieg();
				} else if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim < ((Team) ausw.get(i)).getPunkte()
						+ 3 * offeneSpieltageAusw) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
				} else {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
				}
			} else if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim > ((Team) ausw.get(i)).getPunkte()
					+ 3 * offeneSpieltageAusw) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
			} else if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim < ((Team) ausw.get(i)).getPunkte()
					+ 3 * offeneSpieltageAusw) {
				((Team) heim.get(i)).niederlage();
				((Team) ausw.get(i)).sieg();
			} else {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, isMaxCalculation);
	}

	protected int erstelleInitialeLoesungVarianteE(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			boolean isMaxCalculation) {
		Team[] tmpTeam = null;

		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < heim.size(); i++) {
			int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team) heim.get(i), heim, ausw);
			int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team) ausw.get(i), heim, ausw);
			if (isMaxCalculation) {
				if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team) ausw.get(i)).getPunkte()
						+ 3 * offeneSpieltageAusw > 3) {
					((Team) heim.get(i)).niederlage();
					((Team) ausw.get(i)).sieg();
				} else if (((Team) ausw.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team) heim.get(i)).getPunkte()
						+ 3 * offeneSpieltageAusw > 3) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
				} else {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
				}
			} else if (((Team) heim.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team) ausw.get(i)).getPunkte()
					+ 3 * offeneSpieltageAusw < 3) {
				((Team) ausw.get(i)).niederlage();
				((Team) heim.get(i)).sieg();
			} else if (((Team) ausw.get(i)).getPunkte() + 3 * offeneSpieltageHeim - ((Team) heim.get(i)).getPunkte()
					+ 3 * offeneSpieltageAusw < 3) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
			} else {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, isMaxCalculation);
	}

	protected int erstelleInitialeLoesungVarianteMAX_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			int thisMaxPZ) {
		Team[] tmpTeam = null;

		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		int px = thisMaxPZ;

		ArrayList<Integer> mins = new ArrayList<Integer>();
		ArrayList<Integer> maxs = new ArrayList<Integer>();
		for (int i = 0; i < t.length; i++) {
			int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(Integer.valueOf(t[i].getPunkte()));
			maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
		}
		for (int i = 0; i < heim.size(); i++) {
			int minPY = 0;
			int maxPY = 0;
			int minPZ = 0;
			int maxPZ = 0;
			int y = -1;
			int z = -1;
			for (int j = 0; j < t.length; j++) {
				if (((Team) heim.get(i)).getName().equals(t[j].getName())) {
					minPY = ((Integer) mins.get(j)).intValue();
					maxPY = ((Integer) maxs.get(j)).intValue();
					y = j;
				} else if (((Team) ausw.get(i)).getName().equals(t[j].getName())) {
					minPZ = ((Integer) mins.get(j)).intValue();
					maxPZ = ((Integer) maxs.get(j)).intValue();
					z = j;
				}
			}
			if ((minPY == px) && (minPZ == px)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if (((minPY == px - 1) || (minPY == px - 2)) && (minPZ >= px)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if (((minPZ == px - 1) || (minPZ == px - 2)) && (minPY >= px)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, true);
	}

	protected int erstelleInitialeLoesungVarianteMIN_F(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2, int maxPZ) {
		Team[] tmpTeam = null;

		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < heim.size(); i++) {
			int offeneSpieltageHeim = ausstehendeAnzahlVonSpielen((Team) heim.get(i), heim, ausw);
			int offeneSpieltageAusw = ausstehendeAnzahlVonSpielen((Team) ausw.get(i), heim, ausw);
			if ((((Team) heim.get(i)).getPunkte() == maxPZ - 1) && (((Team) ausw.get(i)).getPunkte() == maxPZ - 1)) {
				((Team) ausw.get(i)).unentschieden();
				((Team) heim.get(i)).unentschieden();
			} else if (((Team) heim.get(i)).getPunkte() < ((Team) ausw.get(i)).getPunkte()) {
				((Team) ausw.get(i)).niederlage();
				((Team) heim.get(i)).sieg();
			} else if (((Team) ausw.get(i)).getPunkte() < ((Team) heim.get(i)).getPunkte()) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
			} else if (offeneSpieltageHeim < offeneSpieltageAusw) {
				((Team) ausw.get(i)).niederlage();
				((Team) heim.get(i)).sieg();
			} else if (offeneSpieltageAusw < offeneSpieltageHeim) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
			} else {
				((Team) ausw.get(i)).niederlage();
				((Team) heim.get(i)).sieg();
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, false);
	}

	protected int erstelleInitialeLoesungVarianteMIN_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			boolean nachb, ArrayList<Team> nachbesserung) {
		Team[] tmpTeam = null;

		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		int px = this.team.getPunkte();

		ArrayList<Integer> mins = new ArrayList<Integer>();
		ArrayList<Integer> maxs = new ArrayList<Integer>();
		for (int i = 0; i < t.length; i++) {
			int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(Integer.valueOf(t[i].getPunkte()));
			maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
		}
		for (int i = 0; i < heim.size(); i++) {
			int minPY = 0;
			int maxPY = 0;
			int minPZ = 0;
			int maxPZ = 0;
			int y = -1;
			int z = -1;
			for (int j = 0; j < t.length; j++) {
				if (((Team) heim.get(i)).getName().equals(t[j].getName())) {
					minPY = ((Integer) mins.get(j)).intValue();
					maxPY = ((Integer) maxs.get(j)).intValue();
					y = j;
				} else if (((Team) ausw.get(i)).getName().equals(t[j].getName())) {
					minPZ = ((Integer) mins.get(j)).intValue();
					maxPZ = ((Integer) maxs.get(j)).intValue();
					z = j;
				}
			}
			if ((minPY >= px) || (minPZ >= px)) {
				if (minPY >= minPZ) {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				} else {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				}
			} else if ((maxPY < px) && (maxPZ < px)) {
				if (minPY >= minPZ) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				} else {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				}
			} else if ((maxPY < px) && (maxPZ >= px)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if ((maxPZ < px) && (maxPY >= px)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if ((maxPY == px + 2) && (maxPZ == px + 2)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			} else if ((maxPY >= px) && (maxPY <= px + 2) && (maxPZ >= px) && (maxPZ <= px + 2)) {
				if (minPY >= minPZ) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				} else {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				}
			} else if ((maxPY == px + 2) && (maxPZ > px + 2)) {
				if (minPY == px - 1) {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
				} else if (minPY < minPZ) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				} else {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
				}
			} else if ((maxPZ == px + 2) && (maxPY > px + 2)) {
				if (minPZ == px - 1) {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
				} else if (minPZ < minPY) {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				} else {
					((Team) heim.get(i)).unentschieden();
					((Team) ausw.get(i)).unentschieden();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
				}
			} else if (((maxPY == px) || (maxPY == px + 1)) && (maxPZ > px + 2)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if ((maxPZ == px + 2) && (maxPY > px + 2)) {
				if (minPY * maxPY < minPZ * maxPZ) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				} else if (minPZ * maxPZ < minPY * maxPY) {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				} else {
					((Team) ausw.get(i)).unentschieden();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
				}
			} else if (((maxPZ == px) || (maxPZ == px + 1)) && (maxPY > px + 2)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if (minPY < minPZ) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if (minPZ < minPY) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if (minPY == minPZ) {
				if (maxPY <= maxPZ) {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				} else {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				}
			}
		}

		if (!nachb) {
			if (nachbesserung != null) {
				nachbesserung.clear();
			}
			for (int i = tmpTeam.length - 1; i >= 0; i--) {
				if (tmpTeam[i].getPunkte() < this.team.getPunkte()) {
					if (nachbesserung == null) {
						nachbesserung = new ArrayList<Team>();
					}
					nachbesserung.add(tmpTeam[i]);
				}
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, false);
	}

	protected int erstelleInitialeLoesungVarianteMAX_G_Komplex(Team[] t, ArrayList<Team> l1, ArrayList<Team> l2,
			int thisMaxPZ) {
		Team[] tmpTeam = null;

		tmpTeam = new Team[t.length];
		for (int i = 0; i < t.length; i++) {
			tmpTeam[i] = new Team(t[i]);
		}
		ArrayList<Team> heim = new ArrayList<Team>();
		ArrayList<Team> ausw = new ArrayList<Team>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l1.get(i)).getName().equals(tmpTeam[j].getName())) {
					heim.add(tmpTeam[j]);
				}
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			for (int j = 0; j < tmpTeam.length; j++) {
				if (((Team) l2.get(i)).getName().equals(tmpTeam[j].getName())) {
					ausw.add(tmpTeam[j]);
				}
			}
		}
		int px = thisMaxPZ;

		ArrayList<Integer> mins = new ArrayList<Integer>();
		ArrayList<Integer> maxs = new ArrayList<Integer>();
		for (int i = 0; i < t.length; i++) {
			int anzahlSpieltageY = ausstehendeAnzahlVonSpielen(t[i], heim, ausw);
			mins.add(Integer.valueOf(t[i].getPunkte()));
			maxs.add(Integer.valueOf(t[i].getPunkte() + Zaehlweise.PUNKTE_S * anzahlSpieltageY));
		}
		for (int i = 0; i < heim.size(); i++) {
			int minPY = 0;
			int maxPY = 0;
			int minPZ = 0;
			int maxPZ = 0;
			int y = -1;
			int z = -1;
			for (int j = 0; j < t.length; j++) {
				if (((Team) heim.get(i)).getName().equals(t[j].getName())) {
					minPY = ((Integer) mins.get(j)).intValue();
					maxPY = ((Integer) maxs.get(j)).intValue();
					y = j;
				} else if (((Team) ausw.get(i)).getName().equals(t[j].getName())) {
					minPZ = ((Integer) mins.get(j)).intValue();
					maxPZ = ((Integer) maxs.get(j)).intValue();
					z = j;
				}
			}
			if ((minPY >= px) || (minPZ >= px)) {
				if (minPY < minPZ) {
					((Team) ausw.get(i)).sieg();
					((Team) heim.get(i)).niederlage();
					mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
					maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
				} else {
					((Team) heim.get(i)).sieg();
					((Team) ausw.get(i)).niederlage();
					mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
					maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
				}
			} else if ((maxPY < px) && (maxPZ < px)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			} else if ((maxPY <= px) && (maxPZ > px)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if ((maxPZ <= px) && (maxPY > px)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if ((maxPY == px - 2) && (maxPZ == px - 2)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			} else if ((maxPY <= px) && (maxPY <= px - 2) && (maxPZ <= px) && (maxPZ <= px - 2)) {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			} else if ((maxPY == px - 2) && (maxPZ > px - 2)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if ((maxPZ == px - 2) && (maxPY > px - 2)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if ((maxPY == px) && (maxPZ > px - 2)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if ((maxPY == px - 2) && (maxPZ > px - 2)) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else if ((maxPZ == px) && (maxPY > px - 2)) {
				((Team) ausw.get(i)).sieg();
				((Team) heim.get(i)).niederlage();
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 3));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 3));
			} else if (minPY < minPZ) {
				((Team) heim.get(i)).sieg();
				((Team) ausw.get(i)).niederlage();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 3));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 3));
			} else {
				((Team) heim.get(i)).unentschieden();
				((Team) ausw.get(i)).unentschieden();
				mins.set(y, Integer.valueOf(((Integer) mins.get(y)).intValue() + 1));
				maxs.set(y, Integer.valueOf(((Integer) maxs.get(y)).intValue() - 2));
				mins.set(z, Integer.valueOf(((Integer) mins.get(z)).intValue() + 1));
				maxs.set(z, Integer.valueOf(((Integer) maxs.get(z)).intValue() - 2));
			}
		}
		return ermittelPlatzierungAndGetResult(tmpTeam, true);
	}

	private int ermittelPlatzierungAndGetResult(Team[] tmpTeam, boolean isMaxCalculation) {
		if (isMaxCalculation) {
			this.liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MAX, this.team);
		} else {
			this.liga.ermittelPlatzierung(tmpTeam, Liga.SORTIERUNG_MIN, this.team);
		}
		for (int i = 0; i < tmpTeam.length; i++) {
			if (tmpTeam[i].getName().equals(this.team.getName())) {
				if (isMaxCalculation) {
					return tmpTeam[i].getPlatzierung();
				} else {
					return tmpTeam[i].getPlatzierung();
				}
			}
		}
		return team.getPlatzierung();
	}

	private int ausstehendeAnzahlVonSpielen(Team t, ArrayList<Team> l1, ArrayList<Team> l2) {
		int anzahl = 0;
		for (int i = 0; i < l1.size(); i++) {
			if (((Team) l1.get(i)).getName().equals(t.getName())) {
				anzahl++;
			}
		}
		for (int i = 0; i < l2.size(); i++) {
			if (((Team) l2.get(i)).getName().equals(t.getName())) {
				anzahl++;
			}
		}
		return anzahl;
	}
}
