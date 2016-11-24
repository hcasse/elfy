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
package elf.swing.canvas;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
	 * Set the current parent item.
	 * @param parent		Parent item.
	 */
	void setParent(ParentItem parent);
	
	/**
	 * Get the parent item.
	 * @return		Parent item.
	 */
	ParentItem getParent();
	
	/**
	 * Get the flags of the item.
	 * @return		Flags.
	 */
	int getFlags();
	
	/**
	 * Set the flags of the item.
	 * @param flags		Item flags.
	 */
	void setFlags(int flags);
	
	/**
	 * Called to display the item.
	 * @param g				Graphics to use.
	 */
	void display(Graphics2D g);
	
	/**
	 * Move the item with the given offset.
	 * @param dx		X offset.
	 * @param dy		Y offset.
	 */
	void move(int dx, int dy);
	
	/**
	 * Test if the item supports interaction with the mouse.
	 * @return		True if it is interactive, false else.
	 */
	boolean isInteractive();
	
	/**
	 * Ask the item whether it can be selected.
	 * @param selection		Current selection.
	 * @return				True if it can be selected, false else.
	 */
	boolean isSelectable(Collection<Item> selection);
	
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
	//void onMouseEvent(MouseEvent event);

	/**
	 * Get the item under the given point.
	 * @param x		Point X.
	 * @param y		Point Y.
	 * @return		Item under the given point or null.
	 */
	Item findItemAt(int x, int y);
	
	/**
	 * Get the bounds of the item.
	 * @return	Item bounds.
	 */
	Rectangle getBounds();
	
	/**
	 * Get the handle for the given item.
	 * @return		Handle.
	 */
	Point getHandle();
}
