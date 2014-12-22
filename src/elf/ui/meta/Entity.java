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
package elf.ui.meta;

import elf.ui.Icon;

/**
 * An entity groups together facilities to identify and display an UI item.
 * @author casse
 */
public interface Entity {
	/**
	 * Get the label of the action.
	 * @return	Action label.
	 */
	public String getLabel();
	
	/**
	 * Get the help string for the action.
	 * @return	Help string.
	 */
	public String getHelp();
	
	/**
	 * Get the mnemonic of the action.
	 * @return	Mnemonic (or 0 if no mnemonic).
	 */
	public int getMnemonic();
	
	/**
	 * Get the control-character of the action.
	 * @return	Control character.
	 */
	public int getControl();

	/**
	 * Get an icon for the action.
	 * @return	Matching icon.
	 */
	public Icon getIcon();
	
}
