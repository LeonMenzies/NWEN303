package ass1;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/*
 * Pros of naive testing:
 * - Using the System.currentTimeMillis is a good way to tests in real time as it gives end to end response time.
 *   This is useful in our case as the change in algorithm runs times is greatly effective when using parallelism
 *   even when the same amount of operations is occurring.  
 * - It is actually quite accurate as long is you run enough iterations.
 * 
 * Cons of naive testing:
 * - A warm up is required which can take a while and is recommenced to run for about 10,000 iterations. 
 *   On large algorithms this can take a long time.
 * - Due to the the fact you need to do lots of warm up runs and and test runs it can take quite a while
 *   to test the performance.
 * 
 * Alternative implementation:
 * - One alternative way to measure the algorithms efficiency would be counting each operation that occurs. this
 *   may not be so using when comparing algorithms that using parallelism.
 * - Using System.nanoTime() will give a more accurate measurement of elapsed time but will also come with the 
 *   negatives of warm up times and expensive run times.
 * - JProfiler could also be used with its time profiling ability's to measure the performance of the algorithms.
 * 
 */
public class TestPerformance {
	/**
	 * This method starts by running java's garbage collection to make memory which is occupied 
	 * by unused objects available for quick reuse. It then runs through a warmup loop specified
	 * by a warmUp int which warms up the Just-In-Time compiler. It then takes the current system
	 * time and runs the merge sort before taking the system time again and returning the difference
	 * in time as a result
	 * 
	 * @param r - A runnable object in this case the iteration of the 2d array which will be run on a thread
	 * @param warmUp - amount of iterations for the warmup run
	 * @param runs - the amount of runs to be timed
	 * @return - the time taken to do all the runs
	 */
	long timeOf(Runnable r, int warmUp, int runs) {
		System.gc();
		for (int i = 0; i < warmUp; i++) {
			r.run();
		}
		long time0 = System.currentTimeMillis();
		for (int i = 0; i < runs; i++) {
			r.run();
		}
		long time1 = System.currentTimeMillis();
		return time1 - time0;
	}

	/**
	 * Using the given sorter algorithm and the 2d array of datasets this method calls timeOf with
	 * a for each loop (A runnable) that is iterating the array of arrays. timeOf returns a time long
	 * which is then printed to display how long the algorithm took in seconds
	 * 
	 * @param <T> - The custom type which allows for any object type to be used
	 * @param s - The merge sort algorithm which is an implementation of the sorter interface
	 * @param name - The name of the sorter algorithm being used for printing purposes
	 * @param dataset - The 2d array of datasets to be tested on
	 */
	<T extends Comparable<? super T>> void msg(Sorter s, String name, T[][] dataset) {
		long time = timeOf(() -> {
			for (T[] l : dataset) {
				s.sort(Arrays.asList(l));
			}
		}, 20000, 200);// realistically 20.000 to make the JIT do his job..
		System.out.println(name + " sort takes " + time / 1000d + " seconds");
	}

	/**
	 * This method runs every merge sort algorithm on all dataset types
	 * @param <T> - The custom type which allows for any object type to be used
	 * @param dataset - The 2d array of datasets to be run through each merge sort algorithm
	 */
	<T extends Comparable<? super T>> void msgAll(T[][] dataset) {
		// msg(new ISequentialSorter(),"Sequential
		// insertion",TestBigInteger.dataset);//so slow
		// uncomment the former line to include performance of ISequentialSorter
		msg(new MSequentialSorter(), "Sequential merge sort", dataset);
		msg(new MParallelSorter1(), "Parallel merge sort (futures)", dataset);
		msg(new MParallelSorter2(), "Parallel merge sort (completablefutures)", dataset);
		msg(new MParallelSorter3(), "Parallel merge sort (forkJoin)", dataset);
	}

	/**
	 * Method for testing the merge sort algorithm on big integers
	 */
	@Test
	void testBigInteger() {
		System.out.println("On the data type BigInteger");
		msgAll(TestBigInteger.dataset);
	}

	/**
	 * Method for testing the merge sort algorithm on floats
	 */
	@Test
	void testFloat() {
		System.out.println("On the data type Float");
		msgAll(TestFloat.dataset);
	}

	/**
	 * Method for testing the merge sort algorithm the custom point type
	 */
	@Test
	void testPoint() {
		System.out.println("On the data type Point");
		msgAll(TestPoint.dataset);
	}

	/**
	 * Method for testing the merge sort algorithm the custom object type I made which represents a water bottle that is comparable by the volume
	 */
	@Test
	void testWaterBottle() {
		System.out.println("On the data type WaterBottle");
		msgAll(TestPoint.dataset);
	}
}
/*
 * With the model solutions, on a lab machine (2019) we may get those results:
 * On the data type Float Sequential merge sort sort takes 1.178 seconds
 * Parallel merge sort (futures) sort takes 0.609 seconds Parallel merge sort
 * (completablefutures) sort takes 0.403 seconds Parallel merge sort (forkJoin)
 * sort takes 0.363 seconds On the data type Point Sequential merge sort sort
 * takes 1.373 seconds Parallel merge sort (futures) sort takes 0.754 seconds
 * Parallel merge sort (completablefutures) sort takes 0.541 seconds Parallel
 * merge sort (forkJoin) sort takes 0.48 seconds On the data type BigInteger
 * Sequential merge sort sort takes 1.339 seconds Parallel merge sort (futures)
 * sort takes 0.702 seconds Parallel merge sort (completablefutures) sort takes
 * 0.452 seconds Parallel merge sort (forkJoin) sort takes 0.492 seconds
 * 
 * On another lab machine in 2021 we get those results: //they must have
 * optimized sequential execution quite a bit! On the data type Float Sequential
 * merge sort sort takes 0.635 seconds Parallel merge sort (futures) sort takes
 * 0.475 seconds //with a smart trick we may get 0.241 instead Parallel merge
 * sort (completablefutures) sort takes 0.25 seconds Parallel merge sort
 * (forkJoin) sort takes 0.253 seconds On the data type Point Sequential merge
 * sort sort takes 0.76 seconds //with a smart trick we may get 0.296 instead
 * Parallel merge sort (futures) sort takes 0.505 seconds Parallel merge sort
 * (completablefutures) sort takes 0.279 seconds Parallel merge sort (forkJoin)
 * sort takes 0.296 seconds On the data type BigInteger Sequential merge sort
 * sort takes 0.871 seconds Parallel merge sort (futures) sort takes 0.574
 * seconds //with a smart trick we may get 0.372 instead Parallel merge sort
 * (completablefutures) sort takes 0.354 seconds Parallel merge sort (forkJoin)
 * sort takes 0.338 seconds
 * 
 */