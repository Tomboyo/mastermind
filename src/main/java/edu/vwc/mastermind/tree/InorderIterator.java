package edu.vwc.mastermind.tree;

import java.util.Iterator;
import java.util.Stack;

public class InorderIterator<E extends Iterable<E>> implements Iterator<E> {

	private E next;	
	private Iterator<E> current;
	private Stack<Iterator<E>> stack;
	
	public InorderIterator(E it) {
		next = it;
		current = it.iterator();
		stack = new Stack<>();
	}
	
	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public E next() {
		E tmp = next;
		
		while (!current.hasNext() && !stack.empty()) {
			current = stack.pop();
		}
		
		if (current.hasNext()) {
			next = current.next();
			stack.push(current);
			current = next.iterator();
		} else {
			next = null;
		}
		
		return tmp;
	}

}
