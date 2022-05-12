package Tests;

import org.junit.jupiter.api.Test;

import datasets.DataSetLoader;
import model.Model;
import model.ModelParallel;
import model.Particle;

/**
 * This class is in charge of testing each models efficiency using the system
 * time
 * 
 * @author leonmenzies
 *
 */
public class TestEffeciency {
	final int STEPS = 5000;
	final int WARMUPSTEPS = 200;

	/**
	 * This method runs the warmup and then records the time between taking warmUp
	 * amount of steps
	 * 
	 * @param m - The model being tested
	 * @param warmUp - The number of warmup cycles
	 * @param runs - The number of run cycles
	 * @return - The time taken to run the model
	 */
	public long calcTime(Model m, int warmUp, int runs) {
		System.gc();
		for (int i = 0; i < warmUp; i++) {
			m.step();
		}
		long time0 = System.currentTimeMillis();
		for (int i = 0; i < runs; i++) {
			m.step();
		}
		long time1 = System.currentTimeMillis();
		return time1 - time0;
	}

	/**
	 * This method is in charge of calling the calcTime method and displaying the
	 * resutls
	 * 
	 * @param name - The name of the model for printing
	 * @param m    - The model being tested
	 */
	public void effeciencyTime(String name, Model m) {

		long time = calcTime(m, WARMUPSTEPS, STEPS);

		System.out.println(name + " takes " + time / 1000d + " seconds");
	}

	@Test
	public void testWarmup() {
		Model m = DataSetLoader.getRegularGrid(100, 800, 40);
		Model m1 = DataSetLoader.getRegularGrid(100, 800, 40);

		effeciencyTime("Test warmup", m);
		effeciencyTime("Test warmup", m1);
	}

	@Test
	public void testRegularGrid() {
		Model m = DataSetLoader.getRegularGrid(100, 800, 40);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("RegularGrid Model", m);
		effeciencyTime("RegularGrid ModeParallel", m1);
	}

	@Test
	public void testRandmonRotatingGrid1() {
		Model m = DataSetLoader.getRandomRotatingGrid(0.02d, 100, 800, 40);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Random Rotating Grid 1 Model", m);
		effeciencyTime("Random Rotating Grid 1 ModeParallel", m1);
	}

	@Test
	public void testRandmonRotatingGrid2() {
		Model m = DataSetLoader.getRandomRotatingGrid(0.02d, 100, 800, 30);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Random Rotating Grid 2 Model", m);
		effeciencyTime("Random Rotating Grid 2 ModeParallel", m1);
	}

	@Test
	public void testElaborate1() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Elaborate 1 Model", m);
		effeciencyTime("Elaborate 1 ModeParallel", m1);
	}

	@Test
	public void testElaborate2() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99005);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Elaborate 2 Model", m);
		effeciencyTime("Elaborate 2 ModeParallel", m1);
	}

	@Test
	public void testElaborate3() {
		Model m = DataSetLoader.getElaborate(200, 700, 2, 0.99008);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Elaborate 3 Model", m);
		effeciencyTime("Elaborate 3 ModeParallel", m1);
	}

	@Test
	public void testRandomSet1() {
		Model m = DataSetLoader.getRandomSet(100, 800, 1000);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Random Set 1 Model", m);
		effeciencyTime("Random Set 1 ModeParallel", m1);
	}

	@Test
	public void testRandomSet2() {
		Model m = DataSetLoader.getRandomSet(100, 800, 100);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Random Set 2 Model", m);
		effeciencyTime("Random Set 2 ModeParallel", m1);
	}

	@Test
	public void testRandomGrid() {
		Model m = DataSetLoader.getRandomGrid(100, 800, 30);

		ModelParallel m1 = new ModelParallel();
		for (Particle p : m.p) {
			m1.p.add(p.clone());
		}

		effeciencyTime("Random Grid Model", m);
		effeciencyTime("Random Grid ModeParallel", m1);
	}
}
