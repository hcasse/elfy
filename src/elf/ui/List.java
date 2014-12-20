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
package elf.ui;

import elf.ui.meta.Data;
import elf.ui.meta.DataCollection;

/**
 * Abstract object for a list. 
 * @author casse
 */
public interface List<T> {

	/**
	 * Get the current selector.
	 * @return	Current selector.
	 */
	public Data<T> getSelector();

	/**
	 * Set the selector data.
	 * @param select	Selector data.
	 */
	public void setSelector(Data<T> select);

	/**
	 * Get the current collection.
	 * @return	Current collection.
	 */
	public DataCollection<T> getCollection();

	/**
	 * Set the collection.
	 * @param coll	New collection.
	 */
	public void setCollection(DataCollection<T> coll);

	/**
	 * Get the current displayer.
	 * @return	Current displayer.
	 */
	public Displayer<T> getDisplayer();

	/**
	 * Set the displayer.
	 * @param display	New displayer.
	 */
	public void setDisplayer(Displayer<T> display);

}
