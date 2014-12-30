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

import java.util.Collection;

/**
 * Dialog allowing to select a value through a list of values.
 * @author casse
 * <T>	Type of values.
 */
public interface SelectionDialog<T> {

	/**
	 * Show the dialog and let the user select a value or cancel it.
	 * @return		Selected value or null (if cancelled).
	 */
	T show();
	
	/**
	 * Set the action button name.
	 * @param name	Button action name.
	 */
	void setAction(String name);
	
	/**
	 * Set the displayer of values.
	 * @param dissplayer	Displayer of values.
	 */
	void setDisplayer(Displayer<T> displayer);
	
	/**
	 * Collection of values.
	 * @param collection	Collection of values.
	 */
	void setValues(Collection<T> collection);
	
	/**
	 * Set the initially selected value.
	 * @param value		Initial value.
	 */
	void setInitial(T value);
}
