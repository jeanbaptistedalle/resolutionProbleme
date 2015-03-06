package resolution.probleme;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoteurResolution {

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

	public enum HeuristiqueEnum {
		FF("First Find"), MIN("Min First"), MAX("Max First"), RAND("Random");

		private String label;

		private HeuristiqueEnum(final String label) {
			this.label = label;
		}

		public static List<HeuristiqueEnum> getAll() {
			final List<HeuristiqueEnum> list = new ArrayList<HeuristiqueEnum>();
			list.add(FF);
			list.add(MIN);
			list.add(MAX);
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

	public MoteurResolution(Integer size, final ResolutionEnum resolution,
			final HeuristiqueEnum heuristique) {
		if (size < 4) {
			size = 4;
		}
		this.nReine = new NReine(size, false);
		this.size = size;
		this.resolution = resolution;
		this.heuristique = heuristique;
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

	public NReine rechercheLocale() {
		NReine best = nReine;
		best.generate();
		int nbErrors = best.getErrors().size();
		double currentTemp = 100f / size;
		float cptTour = 0;
		while (!best.isValid()) {
			NReine test = best.getNeighbour();
			int testError = test.getErrors().size();
			double diffError = testError - nbErrors;
			if (diffError < 0) {
				best = test;
				nbErrors = testError;
			} else {
				if (Math.round(Math.random() * 1) < Math.exp(-diffError / currentTemp)) {
					best = test;
					nbErrors = testError;
				}
			}
			cptTour++;
			currentTemp = 100f / cptTour;
		}
		return best;
	}

	public NReine backtracking() {
		return backtracking(false);
	}

	public NReine backtracking(final boolean AC) {
		nReine.clear();
		List<List<Integer>> domain;

		if (AC) {
			domain = AC3();
		} else {
			domain = generateDomain();
		}
		return backtracking(0, domain);
	}

	private NReine backtracking(final int nthQueen, final List<List<Integer>> domain) {
		if (nReine.allQueenPlaced()) {
			return nReine;
		} else {
			sortByComparator(domain.get(nthQueen));
			for (final Integer y : domain.get(nthQueen)) {
				if (nReine.isSafe(nthQueen, y)) {
					nReine.addQueen(nthQueen, y);
					final NReine retour = backtracking(nthQueen + 1, domain);
					if (retour != null) {
						return retour;
					}
					nReine.removeQueen(nthQueen);
				}
			}
		}
		return null;
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

	public NReine forwardChecking() {
		return forwardChecking(false);
	}

	public NReine forwardChecking(final boolean AC) {
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

	public NReine forwardChecking(final List<List<Integer>> domain, final Integer nthQueen) {
		if (nReine.allQueenPlaced()) {
			return nReine;
		} else {
			sortByComparator(domain.get(nthQueen));
			for (final Integer y : domain.get(nthQueen)) {
				nReine.addQueen(nthQueen, y);
				final List<List<Integer>> reduction = reduceDomain(domain, nthQueen, y);
				final NReine retour = forwardChecking(domain, nthQueen + 1);
				if (retour != null) {
					return retour;
				}
				nReine.removeQueen(nthQueen);
				restaureDomain(domain, reduction);
			}
		}
		return null;
	}

	private List<List<Integer>> reduceDomain(final List<List<Integer>> domain, final int x,
			final int y) {
		final List<List<Integer>> reduction = new ArrayList<List<Integer>>();
		for (int i = 0; i < size; i++) {
			reduction.add(new ArrayList<Integer>());
		}
		for (int x2 = 0; x2 < size; x2++) {
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

	public NReine execute(final StringBuilder stringBuilder) {
		long timeStart = System.currentTimeMillis();
		final NReine result;
		if (size > 30 && !(resolution == ResolutionEnum.RL || heuristique == HeuristiqueEnum.RAND)) {
			stringBuilder.append("\t");
			return null;
		}
		switch (resolution) {
		case RL:
			result = rechercheLocale();
			break;
		case BT:
			result = backtracking();
			break;
		case BT_AC:
			result = backtracking(true);
			break;
		case FC:
			result = forwardChecking();
			break;
		case FC_AC:
			result = forwardChecking(true);
			break;
		default:
			stringBuilder.append("\t");
			return null;
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
		stringBuilder.append("\t");
		return result;
	}

	public static void main(String[] args) {
		try {
			final PrintWriter writer = new PrintWriter("moteurResolution.dat", "UTF-8");
			final List<Integer> testedSizes = new ArrayList<Integer>();
			for (int i = 4; i <= 200; i++) {
				testedSizes.add(i);
			}
			for (final Integer size : testedSizes) {
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(size);
				stringBuilder.append("\t");
				System.out.println("Moteur de résolution pour le problème " + size + "-queens : ");
				for (final ResolutionEnum resolution : ResolutionEnum.getAll()) {
					if (resolution == ResolutionEnum.RL) {
						final MoteurResolution moteurResolution = new MoteurResolution(size,
								resolution, null);
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
}