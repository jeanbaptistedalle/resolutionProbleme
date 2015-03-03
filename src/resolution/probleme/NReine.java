package resolution.probleme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NReine {

	private Integer[] reines;

	private Integer size;

	public NReine(final Integer size, final boolean generate) {
		this.size = size;
		reines = new Integer[size + 1];
		if (generate) {
			int cpt = 1;
			for (int i = 1; i <= size; i++) {
				reines[i] = cpt;
				cpt++;
			}
		}
	}

	public NReine(final Integer size) {
		this(size, true);
	}

	public boolean contains(final Integer x, final Integer y) {
		return reines[x] != null && reines[x] == y;
	}

	public void addQueen(final Integer x, final Integer y) {
		reines[x] = y;
	}

	public boolean isValid() {
		return getErrors().size() == 0;
	}

	public List<Integer> getErrors() {
		// Le test horizontal est inutile car, de par la représentation du
		// tablea, il ne peut y avoir qu'une reine par ligne
		final List<Integer> errors = new ArrayList<Integer>();
		for (int x1 = 1; x1 <= size; x1++) {
			for (int x2 = 1; x2 <= size; x2++) {
				if (x1 != x2) {
				// Test vertical
					if (reines[x1] == reines[x2]) {
						errors.add(x1);
					}
					// Test diagonal
					if (reines[x1] != null && reines[x2] != null) {
						if (Math.abs(reines[x1] - reines[x2]) == Math.abs(x1 - x2)) {
							errors.add(x1);
						}
						if (Math.abs(reines[x2] - reines[x1]) == Math.abs(x2 - x1)) {
							errors.add(x1);
						}
					}
				}
			}
		}
		return errors;
	}

	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int x = 1; x <= size; x++) {
			if (reines[x] != null) {
				for (int y = 1; y <= size; y++) {
					if (reines[x] == y) {
						stringBuilder.append("o");
					} else {
						stringBuilder.append("-");
					}
				}
			} else {
				for (int y = 1; y <= size; y++) {
					stringBuilder.append("-");
				}
			}
			stringBuilder.append(System.getProperty("line.separator"));
		}
		return stringBuilder.toString();
	}

	public NReine getNeighbour() {
		final NReine neighBour = clone();
		final Random rand = new Random();
		int x = 0;
		while (x == 0) {
			x = rand.nextInt(size) + 1;
		}
		int y = 0;
		while (y == 0 || contains(x, y)) {
			y = rand.nextInt(size) + 1;
		}
		neighBour.addQueen(x, y);
		return neighBour;
	}

	protected NReine clone() {
		final NReine clone = new NReine(this.size);
		for (int x = 1; x <= size; x++) {
			if (reines[x] != null) {
				clone.addQueen(x, reines[x]);
			}
		}
		return clone;
	}
}
