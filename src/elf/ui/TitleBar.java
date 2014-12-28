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

import elf.ui.meta.Action;

/**
 * Interface for title bar. A title bar is the title itself,
 * possible several icons to the left, several icons to the right
 * and a menu button.
 * @author casse
 */
public interface TitleBar extends Component {

	/**
	 * Add action for left icon.
	 * @param action	Icon action.
	 */
	void addLeft(Action action);
	
	/**
	 * Add action for right icon.
	 * @param action	Icon action.
	 */
	void addRight(Action action);
	
	/**
	 * Add action for a menu.
	 * @param action	Menu action.
	 */
	void addMenu(Action action);
	
	/**
	 * Get the title.
	 * @return	Title.
	 */
	String getTitle();
	
	/**
	 * Set the title.
	 * @param title		Title.
	 */
	void setTitle(String title);
}
