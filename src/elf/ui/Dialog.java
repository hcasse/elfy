/*
 * Elfy library
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
package elf.ui;

import elf.ui.meta.Action;

/**
 * Minimal interface of dialog.
 * @author casse
 */
public interface Dialog extends Container {


	/**
	 * Add an action to the dialog button bar.
	 * @param action	Added action.
	 */
	void add(Action action);
	
	/**
	 * Add OK button to the dialog button bar.
	 */
	void addOk();
	
	/**
	 * Add Cancel button to the dialog button bar.
	 */
	void addCancel();
	
	/**
	 * Run the dialog.
	 * @return	True if the Ok button is pushed, false else.
	 */
	boolean run();

}
