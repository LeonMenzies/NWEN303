package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Benefit:
 * The benefit of introducing Futures into the merge sort algorithm is speed. For smaller problems the cost of using parallelism 
 * is to much due to the large overheads of setting up threads and workers to do each task. When the problems become bigger
 * thats where the futures shine. Compared to MSequentialSorter Futures are able to submit the tasks (Split and merge) to a
 * work stealing pool. This allows multiple tasks to be performed at a single time reducing merge sort times by more than half.
 * Futures have checked exceptions when can sometimes be preferred.
 * 
 * Learned:
 * The main take away from this was setting up a work stealing pool. I spent lots of time using running into the problem where 
 * there was not enough workers to complete all the task causing the program to freeze. I finally figure out that a work stealing 
 * pool allows other workers who are waiting to complete tasks in the pool.
 * 
 */
public class MParallelSorter1 implements Sorter {

	public static final ExecutorService pool = Executors.newWorkStealingPool();

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {

		int size = list.size();

		// If the list is smaller than 20 sort sequentially
		if (size < 20) {
			return new MSequentialSorter().sort(list);
		}

		Future<List<T>> left = pool.submit(() -> sort(list.subList(0, (size + 1) / 2)));
		List<T> right = sort(list.subList((size + 1) / 2, size));

		return merge(get(left), right);

	}

	public <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right) {

		ArrayList<T> toReturn = new ArrayList<>();

		int i = 0, j = 0;

		while (i < left.size() && j < right.size()) {
			if (left.get(i).compareTo(right.get(j)) < 0) {
				toReturn.add(left.get(i++));
			} else {
				toReturn.add(right.get(j++));
			}
		}

		if (i < left.size()) {
			toReturn.addAll(left.subList(i, left.size()));
		}
		if (j < left.size()) {
			toReturn.addAll(right.subList(j, right.size()));
		}

		return toReturn;
	}

	public static <T> T get(Future<T> f) {
		try {
			return f.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new Error(e);
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
			if (t instanceof RuntimeException rt) {
				throw rt;
			}
			if (t instanceof Error et) {
				throw et;
			}
			throw new Error("Unexpected Checked Exception", t);
		}
	}
}