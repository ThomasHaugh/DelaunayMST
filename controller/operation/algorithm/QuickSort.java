package d_minSpanTree.controller.operation.algorithm;

import java.util.ArrayList;

public class QuickSort implements Sorter {
	private SortBehavior sortBehavior;
	private ArrayList<Object> toSort;

	public QuickSort(ArrayList<Object> list, SortBehavior behavior) {
		toSort = list;
		sortBehavior = behavior;
	}

	public void sort() {
		quicksort(0, toSort.size() - 1);
	}

	private int q;

	private void quicksort(int p, int r) {
		if (p < r) {
			q = partition(p, r);
			quicksort(p, q - 1);
			quicksort(q + 1, r);
		}
	}

	private int i;
	private int j;

	private int partition(int p, int r) {
		i = p;
		for (j = p; j < r; j++) {
			if (sortBehavior.compare(toSort.get(j), toSort.get(r))) {
				exchange(i, j);
				i++;
			}
		}
		exchange(i, r);
		return i;
	}

	private void exchange(int index1, int index2) {
		Object o = toSort.get(index1);
		toSort.set(index1, toSort.get(index2));
		toSort.set(index2, o);
	}

}
