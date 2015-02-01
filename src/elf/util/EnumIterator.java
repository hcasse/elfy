/*
 * Elfy library
 * Copyright (c) 2012 - Hugues Cassé <hugues.casse@laposte.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
