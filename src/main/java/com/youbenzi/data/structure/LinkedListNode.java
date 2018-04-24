package com.youbenzi.data.structure;

/**
 * 双向链表
 * @author yangyingqiang
 * @time Apr 18, 2018 10:33:33 AM
 */
public class LinkedListNode<T> {

	/**数据**/
	T element;
	/**上一个节点**/
	LinkedListNode<T> prev;
	/**下一个节点**/
	LinkedListNode<T> next;
}
