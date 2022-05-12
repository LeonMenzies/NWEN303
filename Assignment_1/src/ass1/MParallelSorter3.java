package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/*
 * Benefit:
 * The benefit of a the forkJoin method is it is easier to see what is happening through the use of a separate class. 
 * All the functionality is done in one place and to run the merge sort you simply use the ForkjoinPool.invoke and pass 
 * in a new object of the class you created with the list as a parameter. Compared to the solutions that just use futures
 * ForkJoin uses the structure of streams which is good for recursive tasks.
 * 
 * 
 * Learned:
 * I learned how to create a recursive class that assigns new tasks to a ForkJoinPool through the use of the super methods.
 * I also learned how to override and create a compute method that runs the splitting and the merging will invoking new objects 
 * On the main pool.
 * 
 */
public class MParallelSorter3 implements Sorter {

	static final ForkJoinPool mainPool = new ForkJoinPool();

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		return mainPool.invoke(new MergeSort<>(list));
	}
}

class MergeSort<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

	final List<T> list;

	public MergeSort(List<T> list) {
		this.list = list;
	}

	@Override
	protected List<T> compute() {

		int size = this.list.size();
		if (size < 2) {
			return list;
		}

		MergeSort<T> left = new MergeSort<>(list.subList(0, (size + 1) / 2));
		MergeSort<T> right = new MergeSort<>(list.subList((size + 1) / 2, size));

		invokeAll(left, right);

		return merge(left.join(), right.join());
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
}