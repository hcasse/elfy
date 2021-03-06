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

/**
 * Interface used to display an object.
 * @author casse
 *
 */
public interface Displayer<T> {

	/**
	 * Get a string display of the value.
	 * @param value		Value to display.
	 * @return			String of the value.
	 */
	public String asString(T value);
	
	/**
	 * Get the icon associated with the value.
	 * @param value		Value.
	 * @return			Associated icon or null.
	 */
	public Icon getIcon(T value);
	
}
