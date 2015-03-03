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
		rechercheLocale();
	}

	public NReine rechercheLocale() {
		NReine best = (NReine) nReine.clone();
		int nbErrors = best.getErrors().size();
		double currentTemp = temperature;
		float cptTour = 0;
		while (!best.isValid()) {
			// System.out.println(best);
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

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		final MoteurResolution moteurResolution = new MoteurResolution(200);
		long timeEnd = System.currentTimeMillis();
		long elapsedTime = timeEnd - timeStart;
		System.out.println(elapsedTime+"ms");
	}
}
