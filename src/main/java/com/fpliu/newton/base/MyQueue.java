package com.fpliu.newton.base;

import java.util.LinkedList;

/**
 * 用LinkedList实现的队列
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class MyQueue<E> {

	private LinkedList<E> elements = new LinkedList<E>();
	
	public boolean add(E e) {
		return elements.add(e);
	}

	public boolean offer(E e) {
		return elements.offer(e);
	}

	public E remove() {
		return elements.remove();
	}

	public E poll() {
		return elements.poll();
	}

	public E element() {
		return elements.element();
	}

	public E peek() {
		return elements.peek();
	}

}
