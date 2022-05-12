package ass1;

import java.util.ArrayList;
import java.util.List;

/*
 * Benefit:
 * Compared to the other 3 merge sort methods implemented in this assignment, MSequential is very simple and easy to implement.
 * A big benefit of this is it very easy to see the flow of the program, which makes bug fixing easy during implementation.
 * It is also very little code compared to the others with the majority taken up by the merge which is the same for every merge
 * sort method.
 * 
 * Learned:
 * From implementing this method of merge sort I learned how to successfully use recursion to divide a list and then write a merge
 * method that merges two lists with the final result being in order.
 * 
 */
public class MSequentialSorter implements Sorter {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {

		int size = list.size();

		if (size < 2) {
			return list;
		}

		return merge(sort(list.subList(0, (size + 1) / 2)), sort(list.subList((size + 1) / 2, size)));
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