package com.youbenzi.data.structure;

/**
 * 队列：FIFO 先进先出
 * @author yangyingqiang
 * @time Apr 18, 2018 10:18:42 AM
 */
public class Queue {

	/**数据**/
	Object[] elements;
	/**队列头**/
	int front;
	/**队列尾**/
	int back;
	/**当前队列数据长度**/
	int currentSize;
}
