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
package elf.swing.canvas;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;

/**
 * Implements a default item without any effect.
 * @author casse
 */
public class NullItem implements ParentItem {
	public static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);
	public static final NullItem NULL = new NullItem();
	
	@Override
	public void display(Graphics2D g) {
	}

	@Override
	public void move(int dx, int dy) {
	}

	@Override
	public boolean isSelectable(Collection<Item> selection) {
		return false;
	}

	@Override
	public boolean acceptsMove(int dx, int dy) {
		return false;
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public boolean isInside(int x, int y) {
		return false;
	}

	/*@Override
	public void onMouseEvent(MouseEvent event) {
	}*/

	@Override
	public int getFlags() {
		return 0;
	}

	@Override
	public void setFlags(int flags) {
	}

	@Override
	public Item findItemAt(int x, int y) {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return EMPTY;
	}

	@Override
	public boolean isInteractive() {
		return false;
	}

	@Override
	public void setParent(ParentItem parent) {
	}

	@Override
	public ParentItem getParent() {
		return null;
	}

	@Override
	public void moved(Item item, int dx, int dy) {
	}

	@Override
	public void refresh(Item item, int x, int y, int w, int h) {
	}

	@Override
	public Point getHandle() {
		return EMPTY.getLocation();
	}

}
