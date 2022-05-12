package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/*
 * Benefit:
 * The benefit of of changing to completable futures over normal futures is the ability to merge the workers task as
 * the program runs and collect all the results at the end with a single join() call. This prevents any blocking from 
 * happening during execution. They are also much faster than the sequential sort algorithm and don't require you to 
 * create any new classes like the ForJoin method.
 * 
 * Learned:
 * Something I learning from this was the ability to use the thenCombine method that is available in the completablefutures
 * library that allows to workers to merge and the information they hold can be used in a method call. This was specifically
 * used for the merge() method after the list had been split to size < 2 
 * 
 */
public class MParallelSorter2 implements Sorter {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		return split(list).join();
	}

	public <T extends Comparable<? super T>> CompletableFuture<List<T>> split(List<T> list) {

		int size = list.size();

		// If the list is smaller than 20 sort sequentially
		if (size < 20) {
			return CompletableFuture.supplyAsync(() -> new MSequentialSorter().sort(list));
		}

		CompletableFuture<List<T>> left = split(list.subList(0, (size + 1) / 2));
		CompletableFuture<List<T>> right = split(list.subList((size + 1) / 2, size));

		return left.thenCombine(right, (l, r) -> merge(l, r));
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