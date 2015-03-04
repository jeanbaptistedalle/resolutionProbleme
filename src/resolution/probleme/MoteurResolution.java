package resolution.probleme;

public class MoteurResolution {

	private NReine nReine;
	private int size;
	private double temperature;

	public MoteurResolution(final Integer size) {
		this.nReine = new NReine(size, false);
		this.size = size;
		this.temperature = 100f / size;
		// Solution pour size = 4
		// nReine.addQueen(1, 2);
		// nReine.addQueen(2, 4);
		// nReine.addQueen(3, 1);
		// nReine.addQueen(4, 3);
		// System.out.println(nReine);
		// System.out.println(nReine.isValid());
	}

	public void reset() {
		nReine.reset();
	}
	
	public void clear(){
		nReine.clear();
	}

	public NReine rechercheLocale() {
		NReine best = (NReine) nReine.clone();
		int nbErrors = best.getErrors().size();
		double currentTemp = temperature;
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
		return backtracking(nReine, 1);
	}

	public NReine backtracking(final NReine nReine, final int nthQueen) {
		if (nReine.allQueenPlaced()) {
			return nReine;
		} else {
			for (int y = 1; y <= size; y++) {
				if (nReine.isSafe(nthQueen, y)) {
					nReine.addQueen(nthQueen, y);
					final NReine retour = backtracking(nReine, nthQueen + 1);
					if(retour != null){
						return retour;
					}
					nReine.removeQueen(nthQueen);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		long timeStart;
		long timeEnd;
		long elapsedTime;
		MoteurResolution moteurResolution = new MoteurResolution(4);

		// timeStart = System.currentTimeMillis();
		// System.out.println(moteurResolution.rechercheLocale());
		// timeEnd = System.currentTimeMillis();
		// elapsedTime = timeEnd - timeStart;
		// System.out.println("Resolution par recherche locale avec recuit simulé effectuée en "
		// + elapsedTime + "ms");

		timeStart = System.currentTimeMillis();
		moteurResolution.clear();
		System.out.println(moteurResolution.backtracking());
		timeEnd = System.currentTimeMillis();
		elapsedTime = timeEnd - timeStart;
		System.out.println("Resolution par backtracking effectuée en "
				+ elapsedTime + "ms");
	}
}
