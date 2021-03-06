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

/**
 * Status bar with timed messages and scroll bar.
 * @author casse
 */
public interface StatusBar extends Component, Monitor {
	public static final int
		WAIT = 5,
		FOREVER = Integer.MAX_VALUE;

	/**
	 * Set the message display delay.
	 * @param delay		Delay in seconds.
	 */
	public void setDelay(int delay);
	
	/**
	 * Clear the displayed message.
	 */
	public void clear();
	
	/**
	 * Display the given message (for usual time).
	 * @param message		Message to display.
	 */
	public void set(String message);
	
	/**
	 * Add a text information.
	 * @param format	Format of text.
	 * @param align		One of Component.LEFT or Component.RIGHT.
	 */
	public TextInfo addTextInfo(String format, int align);
}
