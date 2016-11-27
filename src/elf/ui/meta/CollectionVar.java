/*
 * ElfCore library
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
package elf.ui.meta;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Data collection providing model-observer pattern.
 * @author casse
 */
public class CollectionVar<T> extends AbstractEntity implements Iterable<T> {
	Collection<T> coll;
	LinkedList<Listener<T>> listeners = new LinkedList<Listener<T>>();
	
	/**
	 * Empty collection variable.
	 */
	public CollectionVar() {
		coll = new Vector<T>();
	}
	
	/**
	 * Build a data collection.
	 * @param coll		Contained collection.
	 */
	public CollectionVar(Collection<T> coll) {
		this.coll = coll;
	}
	
	/**
	 * Get the contained collection.
	 * @return		Contained collection.
	 */
	public Collection<T> getCollection() {
		return coll;
	}
	
	/**
	 * change the collection.
	 * @param coll	Changed collection.
	 */
	public void setCollection(Collection<T> coll) {
		this.coll = coll;
		for(Listener<T> listener: listeners)
			listener.onChange();
	}

	/**
	 * Add a listener.
	 * @param listener		Added listener.
	 */
	public void addListener(Listener<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener.
	 * @param listener		Removed listener.
	 */
	public void removeListener(Listener<T> listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Add an item.
	 * @param item	Added item.
	 */
	public void add(T item) {
		coll.add(item);
		for(Listener<T> listener: listeners)
			listener.onAdd(item);
	}
	
	/**
	 * Remove an item.
	 * @param item		Removed item.
	 */
	public void remove(T item) {
		coll.remove(item);
		for(Listener<T> listener: listeners)
			listener.onRemove(item);
	}
	
	/**
	 * Remove a collection of values.
	 * @param items		Removed values.
	 */
	public void remove(Collection<T> items) {
		for(T item: items)
			remove(item);
	}
	
	/**
	 * Clear the content of the collection.
	 */
	public void clear() {
		coll.clear();
		for(Listener<T> listener: listeners)
			listener.onClear();		
	}
	
	
	/**
	 * Listener for the data.
	 * @author casse
	 *
	 * @param <T>	Type of items in the collection.
	 */
	public interface Listener<T> {
		
		/**
		 * Called each time an item is added.
		 * @param item		Added item.
		 */
		void onAdd(T item);
		
		/**
		 * Called each time an item is removed.
		 * @param item		Removed item.
		 */
		void onRemove(T item);
		
		/**
		 * Called each time the collection is cleared.
		 */
		void onClear();
		
		/**
		 * Called when the collection is changed.
		 */
		void onChange();
	}

	/**
	 * Test if the collection contains the given item.
	 * @param item		Tested item.
	 * @return			True if item is contained, false else.
	 */
	public boolean contains(T item) {
		return coll.contains(item);
	}

	/**
	 * Test if the collections is empty.
	 * @return	True if it is empty, false else.
	 */
	public boolean isEmpty() {
		return coll.isEmpty();
	}

	/**
	 * Get iterator on the collection.
	 * @return		Iterator.
	 */
	public Iterator<T> iterator() {
		return coll.iterator();
	}

	/**
	 * Get the size of the collection.
	 * @return		Collection size.
	 */
	public int size() {
		return coll.size();
	}

}
