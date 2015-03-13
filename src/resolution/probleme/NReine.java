package resolution.probleme;

import java.util.Arrays;
import java.util.Random;

public class NReine {

	private Integer[] reines;
	private Integer errorCount;
	private Integer size;

	public NReine(final Integer size) {
		this.size = size;
		reines = new Integer[size];
		clear();
	}

	public void generate() {
		int cpt = 0;
		for (int i = 0; i < size; i++) {
			reines[i] = cpt;
			cpt++;
		}
		searchAllErrors();
	}

	public void clear() {
		Arrays.fill(reines, null);
		errorCount = null;
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

	public Integer get(final Integer x) {
		return reines[x];
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
		if(errorCount == null){
			searchAllErrors();
		}
		return errorCount == 0;
	}

	private void updateError(int x1) {
		for (int x2 = 0; x2 < size; x2++) {
			if (x1 != x2) {
				if (verticalCheck(x1, x2) || diagonalCheck(x1, x2)) {
					addError(x1, x2);
				}
			}
		}
	}

	public void searchAllErrors() {
		// Le test horizontal est inutile car, de par la représentation du
		// tableau, il ne peut y avoir qu'une reine par ligne
		errorCount = 0;
		for (int x1 = 0; x1 < size; x1++) {
			for (int x2 = x1 + 1; x2 < size; x2++) {
				if (verticalCheck(x1, x2) || diagonalCheck(x1, x2)) {
					addError(x1, x2);
				}
			}
		}
		return;
	}

	private boolean inConflict(int x1, int x2) {
		return diagonalCheck(x1, x2) || verticalCheck(x1, x2);
	}

	private boolean diagonalCheck(int x1, int x2) {
		if (reines[x1] != null && reines[x2] != null) {
			if (Math.abs(reines[x1] - reines[x2]) == Math.abs(x1 - x2)) {
				return true;
			}
		}
		return false;
	}

	private boolean verticalCheck(int x1, int x2) {
		if (reines[x1] == reines[x2]) {
			return true;
		}
		return false;
	}

	private void addError(int x1, int x2) {
		if (inConflict(x1, x2)) {
			errorCount++;
		}
	}

	private void removeError(int x1, int x2) {
		if (inConflict(x1, x2)) {
			errorCount--;
		}
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

	public Integer[] getReines() {
		return reines;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public NReine getNeighbour() {
		final NReine neighBour = clone();
		final Random rand = new Random();
		int x = rand.nextInt(size);
		int y = -1;
		while (y == -1 || neighBour.contains(x, y)) {
			y = rand.nextInt(size);
		}
		removeErrors(neighBour, x);
		neighBour.reines[x] = y;
		neighBour.updateError(x);
		return neighBour;
	}

	private void removeErrors(final NReine neighBour, int x) {
		for (int x2 = 0; x2 < size; x2++) {
			if (x != x2) {
				neighBour.removeError(x, x2);
			}
		}
	}

	protected NReine clone() {
		final NReine clone = new NReine(this.size);
		clone.errorCount = this.errorCount;
		clone.reines = Arrays.copyOf(this.reines, this.size);
		return clone;
	}
}
