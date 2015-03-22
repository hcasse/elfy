/*
 * ElfCore library
 * Copyright (c) 2015 - Hugues Cassé <hugues.casse@laposte.net>
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

/**
 * Common interface for piece application whose update is observable.
 * @author casse
 */
public interface Listenable {

	/**
	 * Interface implemented by entities that may observe the entity.
	 * @author casse
	 */
	interface Listener {
		
		/**
		 * Called each time a change arises on the current observable.
		 * @param obs	Changed observer.	
		 */
		void update(Listenable obs);
	}
	
	/**
	 * Add a listener.
	 * @param listener	Added listener.
	 */
	void add(Listener listener);
	
	/**
	 * Remove a listener.
	 * @param listener	Removed listener.
	 */
	void remove(Listener listener);
	
}
