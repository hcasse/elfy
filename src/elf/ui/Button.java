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
 * A button in the UI.
 * @author casse
 */
public interface Button extends Component {
	public static final int
		STYLE_TEXT = 0,			/** Simple text */
		STYLE_ICON_TEXT = 1,	/** Icon (left) and text */
		STYLE_ICON = 2,			/** Icon only (text size) */
		STYLE_TOOL = 3,			/** Icon only (tool bar size) */
		STYLE_TOOL_TEXT = 4;	/** Icon (tool bar size) and, below, small text */

	/**
	 * Get the current action.
	 * @return	Current action.
	 */
	Action getAction();
	
}
