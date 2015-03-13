package resolution.probleme;

import static org.junit.Assert.*;

import org.junit.Test;

public class NReineTest {

	@Test
	public void createIdentityNQueen() {
		int size = 6;
		final NReine nReine = new NReine(size);
		nReine.generate();
		for (int i = 0; i < size; i++) {
			assertEquals((Integer) i, nReine.get(i));
		}
		assertFalse(nReine.isValid());
	}

	@Test
	public void createClearNQueen() {
		int size = 6;
		final NReine nReine = new NReine(size);
		for (int i = 0; i < size; i++) {
			assertNull(nReine.get(i));
		}
		assertFalse(nReine.isValid());
	}

	@Test
	public void validateNQueen() {
		int size = 4;
		final NReine nReine = new NReine(size);
		nReine.addQueen(0, 1);
		nReine.addQueen(1, 3);
		nReine.addQueen(2, 0);
		nReine.addQueen(3, 2);
		assertTrue(nReine.isValid());
	}

	@Test
	public void invalidateVerticallyNQueen() {
		int size = 4;
		final NReine nReine = new NReine(size);
		nReine.addQueen(0, 1);
		nReine.addQueen(1, 3);
		nReine.addQueen(2, 1);
		nReine.addQueen(3, 2);
		assertFalse(nReine.isValid());
	}

	@Test
	public void invalidateDiagonalNQueen() {
		int size = 4;
		final NReine nReine = new NReine(size);
		nReine.addQueen(0, 1);
		nReine.addQueen(1, 0);
		nReine.addQueen(2, 3);
		nReine.addQueen(3, 2);
		assertFalse(nReine.isValid());
	}

}
