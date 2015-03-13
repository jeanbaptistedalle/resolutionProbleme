package resolution.probleme;

import org.junit.Test;

import resolution.probleme.MoteurResolution.HeuristiqueEnum;
import resolution.probleme.MoteurResolution.ResolutionEnum;
import static org.junit.Assert.*;

public class MoteurResolutionTest {

	@Test
	public void testResolution() {
		for (ResolutionEnum resolution : ResolutionEnum.getAll()) {
			for (HeuristiqueEnum heuristique : HeuristiqueEnum.getAll()) {
				rechercheLocalTest(resolution, heuristique);
			}
		}
	}

	public void rechercheLocalTest(final ResolutionEnum resolution,
			final HeuristiqueEnum heuristique) {
		final MoteurResolution mr = new MoteurResolution(10, resolution, heuristique);
		switch (resolution) {
		case RL:
			mr.rechercheLocale();
			break;
		case BT:
			mr.backtracking();
			break;
		case BT_AC:
			mr.backtracking(true);
			break;
		case FC:
			mr.forwardChecking();
			break;
		case FC_AC:
			mr.forwardChecking(true);
			break;
		default:
			return;
		}
		final NReine nReine = mr.getnReine();
		assertNotNull(nReine);
		// On s'assure une fois le traitement terminé en refaisant le calcul que
		// les nReines renvoyés sont bien exactes
		nReine.searchAllErrors();
		assertTrue(nReine.isValid());
	}
}
