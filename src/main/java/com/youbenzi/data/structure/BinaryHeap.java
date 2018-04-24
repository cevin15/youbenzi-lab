package com.youbenzi.data.structure;

/**
 * 二叉堆（优先队列的实现方式之一）
 * 
 * @author yangyingqiang
 * @time Apr 19, 2018 3:14:24 PM
 * @param <T>
 */
public class BinaryHeap<T extends Comparable<? super T>> {

	/** 数据 **/
	T[] elements;
	/** 堆内的数据大小 **/
	int currentSize;

	/**扩容因子**/
	static final float LOAD_FACTOR = 0.75f;
	
	@SuppressWarnings("unchecked")
	public BinaryHeap(T[] items) {
		currentSize = items.length;
		//初始化一个较大的数组
		elements = (T[]) new Comparable[(int)(currentSize * (1 + LOAD_FACTOR))];	

		int i = 1;
		for (T t : items) {
			elements[i++] = t;
		}
		buildHeap();
	}

	private void buildHeap() {
		// current/2 找到最后一个父节点，然后往回找父节点
		for (int i = currentSize / 2; i > 0; i--) {
			percolateDown(i);	//对该节点进行下滤操作
		}
	}

	public void insert(T x) {
		if (currentSize == elements.length) {
			// 扩容
		}
		int hole = ++currentSize;
		// 1. 如果位置来到了根，则停止，新元素插入为止为根
		// 2. 如果x 比父亲的值要大，则x 找到其位置
		// 3. 如果1、2都不满足，交换x 跟其父亲的位置，继续往上找
		for (; hole > 1 && x.compareTo(elements[hole / 2]) < 0; hole = hole / 2) {
			elements[hole] = elements[hole / 2];
		}
		elements[hole] = x;
	}

	public T deleteMin() {
		// 按照我们的数据结构，第一个元素保存在数组的第二个位置，所以elements 长度为2才是有值
		if (elements.length == 1) {
			return null;
		}
		// 找出根节点上的值
		T minItem = elements[1];
		// 暂时存储最后一个节点的值
		elements[1] = elements[currentSize--];
		// 下滤
		percolateDown(1);

		return minItem;
	}

	/**
	 * 下滤
	 * 
	 * @param hole
	 *            需要执行下滤的节点
	 */
	private void percolateDown(int hole) {
		int child;
		T tmp = elements[hole];
		// 1. 如果节点已无子节点，停止
		// 2. 否则，如果未找到合适的节点，继续往下一个子节点查找
		for (; hole * 2 <= currentSize; hole = child) {
			// 节点的左儿子
			child = hole * 2;
			// 1. child != currentSize 检查该节点是否有右儿子
			// 2. 如果有右儿子，则检查左儿子跟右儿子哪个小
			if (child != currentSize && elements[child + 1].compareTo(elements[child]) < 0) {
				child++;
			}
			// 将值较小的儿子与父节点进行比较，如果儿子较小，则儿子上移。否则，找到hole 的位置。
			if (elements[child].compareTo(tmp) < 0) {
				elements[hole] = elements[child];
			} else {
				break;
			}
		}
		elements[hole] = tmp;
	}
	
	public static void main(String[] args) {
		int i = 5;
		System.out.println(++i);
		System.out.println(i);
		System.out.println(i++);
		System.out.println(i);
	}
}
