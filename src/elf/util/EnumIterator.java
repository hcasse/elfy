package elf.util;

import java.util.Enumeration;

/**
 * Implements an iterator from an enumerator.
 * @author casse
 */
public class EnumIterator<T> implements Iterable<T> {
	Enumeration<T> e;
	
	public EnumIterator(Enumeration<T> e) {
		this.e = e;
	}
	
	@Override
	public java.util.Iterator<T> iterator() {
		return new Iterator();
	}

	private class Iterator implements java.util.Iterator<T> {

		@Override
		public boolean hasNext() {
			return e.hasMoreElements();
		}

		@Override
		public T next() {
			return e.nextElement();
		}

		@Override
		public void remove() {
		}
		
	}
}
