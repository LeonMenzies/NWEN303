package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MParallelSorter1 implements Sorter {

	public static final ExecutorService pool = Executors.newFixedThreadPool(100);
	
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list){
		

		// If the size is less the 20 use a sequential algorithm
		if (list.size() < 20) {
			return new MSequentialSorter().sort(list);
		}
		
		int size = list.size();

		if (size < 2) {
			return list;
		}
		
		Future<List<T>> left = pool.submit(() -> sort(list.subList(0, (size + 1) / 2)));
		Future<List<T>> right = pool.submit(() -> sort(list.subList((size + 1) / 2, size)));
				
		

		try {
			return merge(left.get(), right.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			return null;
		}

		

//		List<T> left = sort(list.subList(0, (size + 1) / 2));
//		List<T> right = sort(list.subList((size + 1) / 2, size));
//		
//		List<Future<List<T>>> mergeResults = new ArrayList<>();
//
//		mergeResults.add(pool.submit(() -> merge(left, right)));
//		
//		try {
//			return mergeResults.get(0).get();
//
//		} catch (Throwable e) {
//			return null;
//		}
//		
		
		
	}
	
	
	public <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right) throws InterruptedException, ExecutionException {
		
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