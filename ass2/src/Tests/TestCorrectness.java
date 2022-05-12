package Tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import datasets.DataSetLoader;
import model.Model;
import model.ModelParallel;
import model.Particle;

/**
 * This class is in charge of checking the models do not produce different
 * results
 * 
 * @author leonmenzies
 *
 */
public class TestCorrectness {

	final int STEPS = 1000;

	/**
	 * This method runs each model for STEPS amount of iterations and then checking
	 * the Models remain the same
	 * 
	 * @param m1 - The first model to test
	 * @param m2 - The second model to test
	 * @return - A boolean value if the test passed or failed
	 */
	public boolean runAndCompare(Model m1, ModelParallel m2) {
		for (int i = 0; i < STEPS; i++) {
			m1.step();
			m2.step();
		}

		for (int i = 0; i < m1.p.size(); i++) {
			if (!m1.p.get(i).equals(m2.p.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This test case checks that if a single particle is removed from one of the
	 * models the test will node pass as the two Models will differ
	 */
	@Test
	public void testFalseCase() {
		Model m = DataSetLoader.getRegularGrid(100, 800, 40);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		m1.p.remove(m1.p.size() / 2);

		assertFalse(runAndCompare(m, m1));
	}

	@Test
	public void testRegularGrid() {
		Model m = DataSetLoader.getRegularGrid(100, 800, 40);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testRandmonRotatingGrid1() {
		Model m = DataSetLoader.getRandomRotatingGrid(0.02d, 100, 800, 40);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testRandmonRotatingGrid2() {
		Model m = DataSetLoader.getRandomRotatingGrid(0.02d, 100, 800, 30);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testElaborate1() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testElaborate2() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99005);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testElaborate3() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99008);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testRandomSet1() {
		Model m = DataSetLoader.getRandomSet(100, 800, 1000);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testRandomSet2() {
		Model m = DataSetLoader.getRandomSet(100, 800, 100);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}

	@Test
	public void testRandomGrid() {
		Model m = DataSetLoader.getRandomGrid(100, 800, 30);
		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		assertTrue(runAndCompare(m, m1));
	}
}