/*
 * Elfy library
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

/**
 * A UI component.
 * @author casse
 */
public interface Component {
	public static final int
		LEFT = 0,
		CENTER = 1,
		RIGHT = 2,
		SPREAD = 3,
		VERTICAL = 4,
		HORIZONTAL = 5,
		TOP = 6,
		BOTTOM = 7;

	/**
	 * Get the monitor of the component.
	 * @return	Component monitor.
	 */
	public Monitor getMonitor();
	
	/**
	 * Set the style of the text field.
	 * @param style		Style set.
	 */
	public void setStyle(Style style);
	
	/**
	 * Called to let the component dispose its resources.
	 */
	public void dispose();
	
}
