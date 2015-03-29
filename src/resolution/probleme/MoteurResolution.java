package resolution.probleme;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoteurResolution {

	private static final String SEPARATOR = ":";

	/**
	 * Cette enumeration contient les différentes méthodes de résolution
	 * implémentées
	 */
	public enum ResolutionEnum {
		RL("Recherche locale"), BT("Back Tracking"), BT_AC("AC puis Back Tracking"), FC(
				"Forward Checking"), FC_AC("AC puis Forward Checking");

		private String label;

		private ResolutionEnum(final String label) {
			this.label = label;
		}

		public static List<ResolutionEnum> getAll() {
			final List<ResolutionEnum> list = new ArrayList<ResolutionEnum>();
			list.add(RL);
			list.add(BT);
			list.add(BT_AC);
			list.add(FC);
			list.add(FC_AC);
			return list;
		}

		public String getLabel() {
			return this.label;
		}
	}

	/**
	 * Cette énumération contient les différentes heuristiques implémentées
	 */
	public enum HeuristiqueEnum {
		FF("First Find"), MIN("Min First"), MAX("Max First"), MIN_DOMAIN_SIZE(
				"Min Domain Size First"), RAND("Random");

		private String label;

		private HeuristiqueEnum(final String label) {
			this.label = label;
		}

		public static List<HeuristiqueEnum> getAll() {
			final List<HeuristiqueEnum> list = new ArrayList<HeuristiqueEnum>();
			list.add(FF);
			list.add(MIN);
			list.add(MAX);
			list.add(MIN_DOMAIN_SIZE);
			list.add(RAND);
			return list;
		}

		public String getLabel() {
			return this.label;
		}
	}

	private class MinComparator implements Comparator<Integer> {
		public int compare(Integer arg0, Integer arg1) {
			if (arg0 > arg1) {
				return 1;
			} else if (arg1 > arg0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private class MaxComparator implements Comparator<Integer> {
		public int compare(Integer arg0, Integer arg1) {
			if (arg0 > arg1) {
				return -1;
			} else if (arg1 > arg0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private NReine nReine;
	private int size;
	private ResolutionEnum resolution;
	private HeuristiqueEnum heuristique;
	private Comparator<Integer> comparator;

	public MoteurResolution(Integer size, final ResolutionEnum resolution) {
		this(size, resolution, null);
	}

	public MoteurResolution(Integer size, final ResolutionEnum resolution,
			final HeuristiqueEnum heuristique) {
		if (size < 4) {
			size = 4;
		}
		this.nReine = new NReine(size);
		this.size = size;
		this.resolution = resolution;
		this.heuristique = heuristique;
		/*
		 * Suivant l'heuristique choisie, on utilise un comparator qui permettra
		 * de trier les valeurs
		 */
		if (heuristique != null) {
			switch (heuristique) {
			case MAX:
				comparator = new MaxComparator();
				break;
			case MIN:
				comparator = new MinComparator();
				break;
			case RAND:
				comparator = null;
				break;
			default:
				comparator = null;
				break;
			}
		}
	}

	/**
	 * Cette méthode permet de générer un domaine complet : chaque reine de 1 à
	 * n pour être placé sur les colonnes 1 à n.
	 * 
	 * @return
	 */
	public List<List<Integer>> generateDomain() {
		final List<List<Integer>> domain = new ArrayList<List<Integer>>();
		for (int i = 0; i < size; i++) {
			final List<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < size; j++) {
				list.add(j);
			}
			domain.add(list);
		}
		return domain;
	}

	/**
	 * Cette méthode permet de générer les arcs que l'on va tester dans la
	 * méthode AC3
	 * 
	 * @return
	 */
	public List<Arc> generateWorkList() {
		final List<Arc> workList = new ArrayList<Arc>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i != j) {
					workList.add(new Arc(i, j));
				}
			}
		}
		return workList;
	}

	public void sortByComparator(final List<Integer> listToSort) {
		if (comparator != null) {
			Collections.sort(listToSort, comparator);
		} else {
			if (heuristique == HeuristiqueEnum.RAND) {
				Collections.shuffle(listToSort);
			}
		}
	}

	public Integer getMinDomainSizeQueen(final List<List<Integer>> domain) {
		Integer min = null;
		for (int i = 0; i < size; i++) {
			if (nReine.get(i) == null) {
				if (min == null) {
					min = i;
				} else if (domain.get(i).size() < domain.get(min).size()) {
					min = i;
				}
			}
		}
		return min;
	}

	public void rechercheLocale() {
		NReine best = nReine;
		best.generate();
		int nbErrors = best.getErrorCount();
		double currentTemp = 100f;
		float cptTour = 0;
		/*
		 * En temps normal, on aurait du mettre une condition d'arrêt lié à la
		 * temperature, dans le cas où il n'existerait pas de solution, mais le
		 * problème des N-Reines à toujours une solution, on peux donc dérouler
		 * l'algorithme jusqu'à avoir une solution.
		 */
		while (nbErrors != 0) {
			NReine test = best.getNeighbour();
			int testError = test.getErrorCount();
			double diffError = testError - nbErrors;
			if (diffError < 0) {
				best = test;
				nbErrors = testError;
			} else {
				/*
				 * Si la solution voisine est moins interessante, on a une
				 * probabilité de plus en plus faible de tout de même l'accepter
				 */
				if (Math.round(Math.random()) < Math.exp(-diffError / currentTemp)) {
					best = test;
					nbErrors = testError;
				}
			}
			cptTour++;
			currentTemp = 100f / cptTour;
		}
		nReine = best;
	}

	public boolean backtracking() {
		return backtracking(false);
	}

	public boolean backtracking(final boolean AC) {
		nReine.clear();
		List<List<Integer>> domain;

		if (AC) {
			domain = AC3();
		} else {
			domain = generateDomain();
		}
		return backtracking(0, domain);
	}

	private boolean backtracking(final Integer nthQueen, final List<List<Integer>> domain) {
		if (nReine.allQueenPlaced()) {
			return true;
		} else {
			sortByComparator(domain.get(nthQueen));
			for (final Integer y : domain.get(nthQueen)) {
				if (nReine.isSafe(nthQueen, y)) {
					nReine.addQueen(nthQueen, y);
					if (heuristique == HeuristiqueEnum.MIN_DOMAIN_SIZE) {
						final Integer nextQueen = getMinDomainSizeQueen(domain);
						if (backtracking(nextQueen, domain)) {
							return true;
						}
					} else {
						if (backtracking(nthQueen + 1, domain)) {
							return true;
						}
					}
					nReine.removeQueen(nthQueen);
				}
			}
		}
		return false;
	}

	public List<List<Integer>> AC3() {
		return AC3(generateDomain());
	}

	public List<List<Integer>> AC3(final List<List<Integer>> domain) {
		final List<Arc> workList = generateWorkList();
		while (!workList.isEmpty()) {
			final Arc arc = workList.remove(0);
			if (arcReduce(arc, domain)) {
				if (domain.get(arc.x).isEmpty()) {
					return null;
				} else {
					for (final Integer z : domain.get(arc.x)) {
						if (z != arc.y) {
							final Arc a = new Arc(z, arc.x);
							if (!workList.contains(a)) {
								workList.add(new Arc(z, arc.x));
							}
						}
					}
				}
			}
		}
		return domain;
	}

	public boolean arcReduce(final Arc arc, final List<List<Integer>> domain) {
		boolean change = false;
		final List<Integer> newDomain = new ArrayList<Integer>(domain.get(arc.x));
		for (final Integer vx : domain.get(arc.x)) {
			boolean find = false;
			for (final Integer vy : domain.get(arc.y)) {
				if (NReine.checkConstraint(arc.x, vx, arc.y, vy)) {
					find = true;
					break;
				}
			}
			if (!find) {
				newDomain.remove(vx);
				change = true;
			}
		}
		domain.set(arc.x, newDomain);
		return change;
	}

	public boolean forwardChecking() {
		return forwardChecking(false);
	}

	public boolean forwardChecking(final boolean AC) {
		nReine.clear();
		List<List<Integer>> domain;
		if (AC) {
			domain = AC3();
		} else {
			domain = new ArrayList<List<Integer>>();
			final List<Integer> list = new ArrayList<Integer>();
			for (int y = 0; y < size; y++) {
				list.add(y);
			}
			for (int x = 0; x < size; x++) {
				domain.add(x, new ArrayList<Integer>(list));
			}
		}
		return forwardChecking(domain, 0);
	}

	public boolean forwardChecking(final List<List<Integer>> domain, final Integer nthQueen) {
		if (nReine.allQueenPlaced()) {
			return true;
		} else {
			sortByComparator(domain.get(nthQueen));
			for (final Integer y : domain.get(nthQueen)) {
				nReine.addQueen(nthQueen, y);
				final List<List<Integer>> reduction = reduceDomain(domain, nthQueen, y);
				if (heuristique == HeuristiqueEnum.MIN_DOMAIN_SIZE) {
					if (forwardChecking(domain, getMinDomainSizeQueen(domain))) {
						return true;
					}
				} else {
					if (forwardChecking(domain, nthQueen + 1)) {
						return true;
					}
				}
				nReine.removeQueen(nthQueen);
				restaureDomain(domain, reduction);
			}
		}
		return false;
	}

	private List<List<Integer>> reduceDomain(final List<List<Integer>> domain, final int x,
			final int y) {
		final List<List<Integer>> reduction = new ArrayList<List<Integer>>();
		for (int x2 = 0; x2 < size; x2++) {
			reduction.add(new ArrayList<Integer>());
			final List<Integer> listX2 = new ArrayList<Integer>();
			for (final Integer y2 : domain.get(x2)) {
				if (!NReine.checkConstraint(x, y, x2, y2)) {
					reduction.get(x2).add(y2);
				} else {
					listX2.add(y2);
				}
			}
			domain.set(x2, listX2);
		}
		return reduction;
	}

	private void restaureDomain(final List<List<Integer>> domain, final List<List<Integer>> restaure) {
		for (int x = 0; x < size; x++) {
			for (final Integer y : restaure.get(x)) {
				domain.get(x).add(y);
			}
		}
	}

	public void execute() {
		long timeStart = System.currentTimeMillis();
		switch (resolution) {
		case RL:
			rechercheLocale();
			break;
		case BT:
			backtracking();
			break;
		case BT_AC:
			backtracking(true);
			break;
		case FC:
			forwardChecking();
			break;
		case FC_AC:
			forwardChecking(true);
			break;
		default:
			return;
		}
		long timeEnd = System.currentTimeMillis();
		long elapsedTime = timeEnd - timeStart;
		System.out.println("Résolution effectuée en " + elapsedTime + " ms");
		NReine solution = getNReine();
		System.out.println(solution);
	}

	/**
	 * Cette methode permet de lancer le moteur de résolution pour la méthode
	 * selectionnée, puis d'inserer le resultat dans le stringbuilder qui
	 * servira à sauvegarder les données dans un fichier
	 * 
	 * @param stringBuilder
	 */
	public void execute(final StringBuilder stringBuilder) {
		/*
		 * Les différentes méthodes de résolution et heuristiques n'ayant pas
		 * les mêmes performances, on ne lance la résolution que dans certains
		 * cas.
		 * 
		 * Pour les différentes méthodes de programmation par contrainte, il est
		 * possible de calculer jusqu'à 31 et jusqu'à 90 avec les heuristiques
		 * random et min domain size.
		 * 
		 * Pour la recherche locale, on pourra aller jusqu'à l'instance 7000 en
		 * un temps correct.
		 */
		if (size > 31 && size <= 90) {
			if (resolution != ResolutionEnum.FC && resolution != ResolutionEnum.FC_AC
					&& resolution != ResolutionEnum.RL) {
				stringBuilder.append(SEPARATOR);
				return;
			} else {
				if (heuristique != HeuristiqueEnum.MIN_DOMAIN_SIZE
						&& heuristique != HeuristiqueEnum.RAND) {
					stringBuilder.append(SEPARATOR);
					return;
				}
			}
		} else if (size > 90) {
			if (resolution != ResolutionEnum.RL) {
				stringBuilder.append(SEPARATOR);
				return;
			}
		}
		long timeStart = System.currentTimeMillis();
		switch (resolution) {
		case RL:
			rechercheLocale();
			break;
		case BT:
			backtracking();
			break;
		case BT_AC:
			backtracking(true);
			break;
		case FC:
			forwardChecking();
			break;
		case FC_AC:
			forwardChecking(true);
			break;
		default:
			stringBuilder.append(SEPARATOR);
			return;
		}
		long timeEnd = System.currentTimeMillis();
		long elapsedTime = timeEnd - timeStart;
		StringBuilder stringBuilder2 = new StringBuilder();
		stringBuilder2.append("\tResolution par '");
		stringBuilder2.append(resolution.getLabel());
		if (heuristique != null) {
			stringBuilder2.append("' et heuristique '");
			stringBuilder2.append(heuristique.getLabel());
		}
		stringBuilder2.append("' obtenue en ");
		stringBuilder2.append(elapsedTime);
		stringBuilder2.append("ms");
		System.out.println(stringBuilder2.toString());
		stringBuilder.append(elapsedTime);
		stringBuilder.append(":");
	}

	public NReine getNReine() {
		return nReine;
	}

	/**
	 * Cette méthode peut être appelée dans le main et permet de lancer la
	 * résolution du problème de N-Reines avec les différentes heuristiques et
	 * méthode de résolution. Les resultats sont ensuite stockés dans le fichier
	 * moteurResolution.dat qui pourra ensuite être réutilisé par gnuplot
	 * 
	 * ATTENTION !! Ce traitement risque de prendre du temps et écrasera
	 * l'ancien fichier moteurResolution.dat. Si il n'arrive pas à son terme,
	 * l'ancien fichier sera simplement vidé.
	 */
	public static void genererFichierResultat() {
		try {
			final PrintWriter writer = new PrintWriter("moteurResolution.dat", "UTF-8");
			final List<Integer> testedSizes = new ArrayList<Integer>();
			for (int i = 4; i <= 90; i++) {
				testedSizes.add(i);
			}
			for (int i = 91; i <= 7000; i += 50) {
				testedSizes.add(i);
			}
			for (final Integer size : testedSizes) {
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(size);
				stringBuilder.append(SEPARATOR);
				System.out.println("Moteur de résolution pour le problème " + size + "-queens : ");
				for (final ResolutionEnum resolution : ResolutionEnum.getAll()) {
					if (resolution == ResolutionEnum.RL) {
						/*
						 * Les heuristiques ne concernent pas la recherche
						 * locale
						 */
						final MoteurResolution moteurResolution = new MoteurResolution(size,
								resolution);
						moteurResolution.execute(stringBuilder);
					} else {
						for (final HeuristiqueEnum heuristique : HeuristiqueEnum.getAll()) {
							final MoteurResolution moteurResolution = new MoteurResolution(size,
									resolution, heuristique);
							moteurResolution.execute(stringBuilder);
						}
					}
				}
				stringBuilder.append(System.getProperty("line.separator"));
				writer.write(stringBuilder.toString());
			}
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ce second main permet de ne lancer qu'une seule méthode de résolution.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final MoteurResolution moteurResolution = new MoteurResolution(20, ResolutionEnum.BT,
				HeuristiqueEnum.RAND);
		moteurResolution.execute();
		// Decommenter la ligne suivante pour lancer la récolte des données pour gnuplot
		// genererFichierResultat();
	}
}