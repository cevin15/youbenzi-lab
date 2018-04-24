package com.youbenzi.sort;

import java.util.Arrays;

public class Sort {

	/**
	 * 插入排序
	 * 
	 * 对于位置p，将p 上的元素向左移动，直到它在前p + 1 个元素中的正确位置被找到。然后继续p + 1 位置上元素的操作。
	 * 
	 * @param elements
	 */
	public static <T extends Comparable<? super T>> T[] insertionSort(T[] elements) {
		return insertionSort(elements, 0, elements.length - 1);
	}
	
	/**
	 * 插入排序，只对left - right 区间进行排序操作
	 * @param elements
	 * @param left
	 * @param right
	 * @return
	 */
	private static <T extends Comparable<? super T>> T[] insertionSort(T[] elements, int left, int right) {
		int j;

		for (int p = left + 1; p <= right; p++) {
			T tmp = elements[p]; // 使用临时变量存储当前需要调整位置的元素，避免后续的交换（交换开销比这种策略大）。
			for (j = p; j > left && tmp.compareTo(elements[j - 1]) < 0; j--) {
				elements[j] = elements[j - 1];
			}
			elements[j] = tmp;
		}
		return elements;
	}
	
	/**
	 * 希尔排序
	 * 
	 * 希尔排序使用一个序列h1，h2，h3，h4，……，称为增量数列。只要h1 = 1，任何增量数列都是可行的，不过好的增量数列性能更强。
	 * 假设序列的某个值为k，一趟k 排序的作用，就是对k 个独立的子数组（i、i + k、i + 2k、…… 构成一个子数组）执行一次插入排序。
	 * 用增量数列倒序进行k 排序，一直到k = 1，排序完成。
	 * 
	 * @param elements
	 */
	public static <T extends Comparable<? super T>> T[] shellSort(T[] elements) {
		int j;
		// 这里的增量数列用的是希尔（Shell）增量，即f(k) = length, f(k-1) = f(k)/2，直到1.
		// 希尔增量效率其实不好，目前实践中最好的序列是{1, 5, 19, 41, 109, …}，该序列的项或者是（9*4的i次方 - 9*2的i次方 +
		// 1）的形式，或者是（4的i次方 - 3*2的i次方 + 1）
		for (int gap = elements.length / 2; gap > 0; gap = gap / 2) {
			for (int i = gap; i < elements.length; i++) { // 后续子数列使用插入排序
				T tmp = elements[i];
				for (j = i; j >= gap && tmp.compareTo(elements[j - gap]) < 0; j = j - gap) {
					elements[j] = elements[j - gap];
				}
				elements[j] = tmp;
			}
		}
		return elements;
	}

	/**
	 * 找出目标节点的左儿子
	 * 
	 * @param i
	 *            目标节点
	 * @return 目标节点左儿子的位置
	 */
	private static int leftChild(int i) {
		return 2 * i + 1;
	}

	/**
	 * 下滤，给i 节点找到正确的位置
	 * 
	 * @param elements
	 *            目标数组
	 * @param i
	 *            下滤的节点位置
	 * @param n
	 *            二叉堆在目标数组中的长度，剩下的位置存储着被移除的根节点
	 */
	private static <T extends Comparable<? super T>> void percDown(T[] elements, int i, int n) {
		int child;
		T tmp;
		for (tmp = elements[i]; leftChild(i) < n; i = child) {
			// 下滤节点的左儿子
			child = leftChild(i);
			// 如果左儿子不是最后一个节点，比较左右两个儿子的大小，确定值大的儿子
			if (child != n - 1 && elements[child].compareTo(elements[child + 1]) < 0) {
				child++;
			}
			// 如果目标节点比值大的儿子要小，交换节点
			if (tmp.compareTo(elements[child]) < 0) {
				elements[i] = elements[child];
			} else {
				break;
			}
		}
		elements[i] = tmp;
	}

	/**
	 * 交换两个节点在数组中的位置
	 */
	private static <T extends Comparable<? super T>> void swapReferences(T[] elements, int source, int target) {
		T tmp = elements[source];
		elements[source] = elements[target];
		elements[target] = tmp;
	}

	public static <T extends Comparable<? super T>> T[] heapSort(T[] elements) {
		// 把无序的数组构建为一个二叉堆：这个上文说过，从最后一个父亲节点开始，往前一个个下滤即可。
		for (int i = elements.length / 2; i >= 0; i--) {
			percDown(elements, i, elements.length);
		}

		for (int i = elements.length - 1; i > 0; i--) {
			// 根节点和二叉堆末节点交换位置，相当于移除根节点
			swapReferences(elements, 0, i);
			// 为交换后的根节点寻找一个合适的节点；上一步操作之后，此时的堆通常不符合堆序性
			percDown(elements, 0, i);
		}

		return elements;
	}

	
	private static <T extends Comparable<? super T>> void mergeSort(T[] elements, T[] tmpArray, int left, int right) {
		if (left >= right) {	//如果子数组只有一个元素了，跳出递归。
			return;
		}
		int center = (left + right) / 2;
		mergeSort(elements, tmpArray, left, center);
		mergeSort(elements, tmpArray, center + 1, right);
		merge(elements, tmpArray, left, center + 1, right);
	}
	
	private static <T extends Comparable<? super T>> void merge(T[] elements, T[] tmpArray, int leftPos, int rightPos,
			int rightEnd) {
		int leftEnd = rightEnd - 1;
		int tmpPos = leftPos;
		int numElements = rightEnd - leftPos + 1;

		//左右两个数组都还有数据
		while (leftPos <= leftEnd && rightPos <= rightEnd) {
			//比较左右数组两个数值哪个小，小的填充到临时数组
			if (elements[leftPos].compareTo(elements[rightPos]) <= 0) {
				tmpArray[tmpPos++] = elements[leftPos++];
			} else {
				tmpArray[tmpPos++] = elements[rightPos++];
			}
		}
		//左数组还有数据
		while (leftPos <= leftEnd) {
			tmpArray[tmpPos++] = elements[leftPos++];
		}
		//右数组还有数据
		while (rightPos <= rightEnd) {
			tmpArray[tmpPos++] = elements[rightPos++];
		}
		//把已排好序的临时数组数据填充到原数组中
		for (int i = 0; i < numElements; i++, rightEnd--) {
			elements[rightEnd] = tmpArray[rightEnd];
		}
	}

	/**
	 * 归并排序
	 * @param elements
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> void mergeSort(T[] elements) {
		T[] tmpArray = (T[])new Comparable[elements.length];
		mergeSort(elements, tmpArray, 0, elements.length - 1);
	}

	private static final int CUTOFF = 20;
	/**
	 * 三数中值分割法，选取枢纽元
	 */
	private static <T extends Comparable<? super T>> T median3(T[] elements, int left, int right) {
		int center = (left + right) / 2;
		
		if (elements[center].compareTo(elements[left]) < 0) {
			swapReferences(elements, left, center);
		}
		if (elements[right].compareTo(elements[left]) < 0) {
			swapReferences(elements, left, right);
		}
		if (elements[right].compareTo(elements[center]) < 0) {
			swapReferences(elements, right, center);
		}
		
		swapReferences(elements, center, right - 1);
		return elements[right - 1];
	}
	
	private static <T extends Comparable<? super T>> T[] quickSort(T[] elements, int left, int right) {
		// 小数组不使用快速排序，因为效率并不比插入排序高
		if (left + CUTOFF > right) {
			insertionSort(elements, left, right);
		} else {
			T pivot = median3(elements, left, right);
			
			int i = left, j = right - 1;
			for(;;) {
				//寻找左侧比枢纽元大的值，遇到大的值跳出while
				while (elements[++i].compareTo(pivot) < 0) {}
				//寻找右侧比枢纽元小的值，遇到小的值跳出while
				while (elements[--j].compareTo(pivot) > 0) {}
				//如果i 还在j 的左侧，交换两个位置上的值
				if (i < j) {
					swapReferences(elements, i, j);
				} else {	//i 与j 已经交错，跳出for
					break;
				}
			}
			//位置i 与枢纽元的值交换位置
			swapReferences(elements, i, right - 1);
			
			//对左右两侧的小数组继续递归调用快速排序
			quickSort(elements, left, i - 1);
			quickSort(elements, i + 1, right);
		}
		
		return elements;
	}
	
	/**
	 * 快速排序
	 * @param elements
	 * @return
	 */
	public static <T extends Comparable<? super T>> T[] quickSort(T[] elements) {
		return quickSort(elements, 0, elements.length - 1);
	}
	
	public static void main(String[] args) {
		System.out.println(
				Arrays.toString(quickSort(new Integer[] { 21, 53, 32, 123, 856, 43, 32, 674, 342, 6728, 1237, 42 })));
		System.out.println(new Integer(3).compareTo(new Integer(4)));
	}

}
