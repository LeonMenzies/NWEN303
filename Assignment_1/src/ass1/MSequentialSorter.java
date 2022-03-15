package ass1;

import java.util.ArrayList;
import java.util.List;

public class MSequentialSorter implements Sorter {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {

		int size = list.size();

		if (size < 2) {
			return list;
		}

		List<T> left = sort(list.subList(0, (size + 1) / 2));
		List<T> right = sort(list.subList((size + 1) / 2, size));

		return merge(left, right);
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
		
		if(i < left.size()) {toReturn.addAll(left.subList(i, left.size()));}
		if(j < left.size()) {toReturn.addAll(right.subList(j, right.size()));}
		


		return toReturn;
	}
}