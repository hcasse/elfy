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
public interface Subject {

	/**
	 * Interface implemented by entities that may observe the entity.
	 * @author casse
	 */
	interface Observer {
		
		/**
		 * Called each time a change arises on the current observable.
		 */
		void update();

	}
	
	/**
	 * Add an observer.
	 * @param observer	Added observer.
	 */
	void add(Observer observer);
	
	/**
	 * Remove an observer.
	 * @param observer	Removed observer.
	 */
	void remove(Observer observer);

}
