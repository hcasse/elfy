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
 * A bar that displays progress of an action.
 * @author casse
 */
public interface ProgressBar extends Field {
	
	/**
	 * Change minimum value.
	 * @param min	Minimum value.
	 */
	void setMin(int min);
	
	/**
	 * change maximum value.
	 * @param max	Maximum value.
	 */
	void setMax(int max);
	
	/**
	 * Set the text displayed in the bar.
	 * The text may contain a %d that will be replaced by the current
	 * percentage of bar.
	 * @param format	Format string in bar.
	 */
	void setText(String format);
}
