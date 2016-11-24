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

/**
 * Interface provided by the parent item.
 * @author casse
 */
public interface ParentItem extends Item {

	/**
	 * Called when the child item has moved.
	 * @param item		Moved item.
	 * @param dx		X offset.
	 * @param dy		Y offset.
	 */
	void moved(Item item, int dx, int dy);
	
	/**
	 * Called when the item display need to be refreshed.
	 * @param item		Item to refresh.
	 * @param x			X position.
	 * @param y			Y position.
	 * @param w			Width.
	 * @param h			height.
	 */
	void refresh(Item item, int x, int y, int w, int h);
}
