package resolution.probleme;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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

	private NReine nReine;
	private int size;

	public MoteurResolution(Integer size) {
		if (size < 4) {
			size = 4;
		}
		this.nReine = new NReine(size, false);
		this.size = size;
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

	public NReine backtracking(final boolean ACFirst) {
		nReine.clear();
		if (ACFirst) {
			final List<List<Integer>> domain = AC3();
			return backtracking(0, domain);
		} else {
			return backtracking(0, generateDomain());
		}
	}

	private NReine backtracking(final int nthQueen, final List<List<Integer>> domain) {
		if (nReine.allQueenPlaced()) {
			return nReine;
		} else {
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
		if (AC) {
			final List<List<Integer>> domain = AC3();
			return forwardChecking(domain, 0);
		} else {
			final List<List<Integer>> domain = new ArrayList<List<Integer>>();
			final List<Integer> list = new ArrayList<Integer>();
			for (int y = 0; y < size; y++) {
				list.add(y);
			}
			for (int x = 0; x < size; x++) {
				domain.add(x, new ArrayList<Integer>(list));
			}
			return forwardChecking(domain, 0);
		}
	}

	public NReine forwardChecking(final List<List<Integer>> domain, final Integer nthQueen) {
		if (nReine.allQueenPlaced()) {
			return nReine;
		} else {
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

	public NReine execute(final ResolutionEnum resolution, final StringBuilder stringBuilder) {
		long timeStart = System.currentTimeMillis();
		final NReine result;
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
			return null;
		}
		long timeEnd = System.currentTimeMillis();
		long elapsedTime = timeEnd - timeStart;
		System.out.println("\tResolution par " + resolution.getLabel() + " obtenue en "
				+ elapsedTime + "ms");
		stringBuilder.append(elapsedTime);
		stringBuilder.append("\t");
		return result;
	}

	public static void main(String[] args) {
		try {
			final PrintWriter writer = new PrintWriter("moteurResolution.dat", "UTF-8");

			final List<Integer> testedSizes = new ArrayList<Integer>();
			for (int i = 4; i <= 30; i++) {
				testedSizes.add(i);
			}
			final StringBuilder stringBuilder = new StringBuilder();
			for (final Integer size : testedSizes) {
				stringBuilder.append(size);
				stringBuilder.append("\t");
				MoteurResolution moteurResolution = new MoteurResolution(size);
				System.out.println("Moteur de résolution pour le problème " + size + "-queens : ");
				for (final ResolutionEnum resolution : ResolutionEnum.getAll()) {
					moteurResolution.execute(resolution, stringBuilder);
				}
				stringBuilder.append(System.getProperty("line.separator"));
			}
			writer.write(stringBuilder.toString());
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}