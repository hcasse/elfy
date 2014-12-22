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

import java.util.Vector;

/**
 * A data that may be observed.
 * @author H. Cassé <casse@irit.fr>
 *
 * @param <T>	Stored data.
 */
public class Var<T> {
	Vector<Listener<T>> listeners = new Vector<Listener<T>>();
	T value;
	
	/**
	 * Build the data with default value.
	 */
	public Var() { }
	
	/**
	 * Build the data with the given value.
	 * @param value	Set value.
	 */
	public Var(T value) { this.value = value; }
	
	/**
	 * Get the value of the data.
	 * @return	Data value.
	 */
	public final T get() { return value; }
	
	/**
	 * Set the value of the data.
	 * @param value		Set value.
	 */
	public final void set(T value) {
		this.value = value;
		for(Listener<T> listener : listeners)
			listener.change(this);
	}
	
	/**
	 * Add the given listener.
	 * @param listener	Listener to add.
	 */
	public void addListener(Listener<T> listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove the given listener.
	 * @param listener	Removed listener.
	 */
	public void removeListener(Listener<T> listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Listener to record change in the data.
	 */
	public interface Listener<T> {
		
		/**
		 * Called each time the data has been changed.
		 * @param data	Changed data.
		 */
		public void change(Var<T> data);
	}
}
