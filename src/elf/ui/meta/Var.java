/*
 * ElfCore library
 * Copyright (c) 2014 - Hugues Cassé <hugues.casse@laposte.net>
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
 * Common interface to variables.
 * @author casse
 */
public class Var<T> extends AbstractEntity {
	private Accessor<T> accessor;
	private Vector<Listener<T>> listeners = new Vector<Listener<T>>();

	/**
	 * Fast variable construction.
	 * @param value		Value of the variable.
	 * @return			Built variable.
	 */
	public static <T> Var<T> make(T value) {
		return new Var<T>(value);
	}
	
	/**
	 * Build an empty variable.
	 */
	public Var() {
		this.accessor = new Accessor.Store<T>();
	}
	
	/**
	 * Build a variable storing a single value.
	 * @param value		Stored value.
	 */
	public Var(T value) {
		this.accessor = new Accessor.Store<T>(value);
	}
	
	/**
	 * Build a variable from an accessor.
	 * @param accessor	Accessor to use.
	 */
	public Var(Accessor<T> accessor) {
		this.accessor = accessor;
	}
	
	protected void fireChange() {
		for(Listener<T> listener : listeners)
			listener.change(this);		
	}
	
	/**
	 * Change the accessor.
	 * @param accessor	New accessor.
	 */
	public void setAccessor(Accessor<T> accessor) {
		this.accessor = accessor;
		fireChange();
	}

	/**
	 * Get the value of the data.
	 * @return	Data value.
	 */
	public T get() {
		return accessor.get();
	}
	
	/**
	 * Set the value of the data.
	 * @param value		Set value.
	 */
	public void set(T value) {
		accessor.set(value);
		fireChange();
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
