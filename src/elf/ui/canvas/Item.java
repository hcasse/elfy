/*
 * ElfSim tool
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
package elf.ui.canvas;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Displayed item.
 * @author casse
 */
public interface Item {
	public static final int
		OVER 		= 0x0001,
		SELECTED 	= 0x0002; 
	
	/**
	 * Called to display the item.
	 * @param g				Graphics to use.
	 * @param flags			Combination of OVER and SELECTED.
	 */
	void display(Graphics2D g, int flags);
	
	/**
	 * Get the display rectangle, typically for refresh.
	 * @return		Display rectangle.
	 */
	Rectangle getDisplayRect();
	
	/**
	 * Move the item with the given offset.
	 * @param dx		X offset.
	 * @param dy		Y offset.
	 */
	void move(int dx, int dy);
	
	/**
	 * Ask the item whether it can be selected.
	 * @param selection		Current selection.
	 * @return				True if it can be selected, false else.
	 */
	boolean acceptsSelect(Collection<Item> selection);
	
	/**
	 * Ask the whether it can be moved.
	 * @param dx			X offset.
	 * @param dy			Y offset
	 * @return				True if it can be moved, false else.
	 */
	boolean acceptsMove(int dx, int dy);
	
	/**
	 * Get the depth of the item.
	 * @return		Item depth.
	 */
	int getDepth();
	
	/**
	 * Test if the given point is inside the item.
	 * @param x		X coord.
	 * @param y		Y coord.
	 * @return		True if it is inside, false else.
	 */
	boolean isInside(int x, int y);
	
	/**
	 * Called when a mouse event arise on the item.
	 * @param event		Mouse event.
	 */
	void onMouseEvent(MouseEvent event);

}