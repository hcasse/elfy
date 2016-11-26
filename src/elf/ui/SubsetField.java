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
package elf.ui;

import elf.ui.meta.CollectionVar;

/**
 * This field allows to select a subset of a set of values. 
 * @author casse
 */
public interface SubsetField<T> extends Field {

	/**
	 * Get subset.
	 * @return	Selected.
	 */
	CollectionVar<T> getSubset();
	
	/**
	 * Change the result subset.
	 * @param subset	New subset.
	 */
	void setSubset(CollectionVar<T> subset);
	
	/**
	 * Set the current displayer.
	 * @param displayer		Displayer.
	 */
	void setDisplayer(Displayer<T> displayer);
	
	/**
	 * Set the set to take a subset from (reset the current subset).
	 * @param set	New set.
	 */
	void setSet(CollectionVar<T> set);
	
	/**
	 * Get the current set of values.
	 * @return	Current set of values.
	 */
	CollectionVar<T> getSet();
}
