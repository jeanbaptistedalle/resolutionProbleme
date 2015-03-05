package resolution.probleme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NReine {

	private Integer[] reines;

	private Integer size;

	public NReine(final Integer size, final boolean generate) {
		this.size = size;
		reines = new Integer[size];
		if (generate) {
			generate();
		}else{
			clear();
		}
	}

	public NReine(final Integer size) {
		this(size, true);
	}

	public void generate() {
		int cpt = 0;
		for (int i = 0; i < size; i++) {
			reines[i] = cpt;
			cpt++;
		}
	}

	public void clear() {
		Arrays.fill(reines, null);
	}

	public boolean contains(final Integer x, final Integer y) {
		return reines[x] != null && reines[x] == y;
	}

	public void addQueen(final Integer x, final Integer y) {
		reines[x] = y;
	}

	public void removeQueen(final Integer x) {
		reines[x] = null;
	}

	public boolean areQueenPlacedValid() {
		return getErrors().size() == 0;
	}

	public boolean allQueenPlaced() {
		for (int i = 0; i < size; i++) {
			if (reines[i] == null) {
				return false;
			}
		}
		return true;
	}

	public boolean isValid() {
		if (!allQueenPlaced()) {
			return false;
		}
		return getErrors().size() == 0;
	}

	public List<Integer> getErrors() {
		// Le test horizontal est inutile car, de par la représentation du
		// tablea, il ne peut y avoir qu'une reine par ligne
		final List<Integer> errors = new ArrayList<Integer>();
		for (int x1 = 0; x1 < size; x1++) {
			for (int x2 = 0; x2 < size; x2++) {
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
					}
				}
			}
		}
		return errors;
	}

	public static boolean checkConstraint(final int x1, final int y1, final int x2, final int y2) {
		if (x1 == x2) {
			return false;
		}
		if (y1 == y2) {
			return false;
		}
		// Test diagonal
		if (Math.abs(y1 - y2) == Math.abs(x1 - x2)) {
			return false;
		}
		return true;
	}

	public boolean isSafe(final int x, final int y) {
		for (int i = 0; i < size; i++) {
			if (reines[i] != null && reines[i] == y) {
				return false;
			}
			if (reines[i] != null) {
				if (Math.abs(y - reines[i]) == Math.abs(x - i)) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int x = 0; x < size; x++) {
			if (reines[x] != null) {
				for (int y = 0; y < size; y++) {
					if (reines[x] == y) {
						stringBuilder.append("o");
					} else {
						stringBuilder.append("-");
					}
				}
			} else {
				for (int y = 0; y < size; y++) {
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
		int x = rand.nextInt(size);
		int y = -1;
		while (y == -1 || contains(x, y)) {
			y = rand.nextInt(size);
		}
		neighBour.addQueen(x, y);
		return neighBour;
	}

	protected NReine clone() {
		final NReine clone = new NReine(this.size);
		for (int x = 0; x < size; x++) {
			if (reines[x] != null) {
				clone.addQueen(x, reines[x]);
			}
		}
		return clone;
	}
}
