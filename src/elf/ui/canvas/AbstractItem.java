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
package elf.ui.canvas;

import java.awt.Rectangle;
import java.util.Collection;

/**
 * Abstract item provides common basic facilities of an item
 * (selectable, draggable and interactive).
 * @author casse
 */
public abstract class AbstractItem implements Item {
	int flags;
	protected int depth;
	ParentItem parent = NullItem.NULL;
	
	@Override
	public void setFlags(int flags) {
		if(flags != this.flags) {
			this.flags = flags;
			Rectangle bounds = getBounds();
			getParent().refresh(this, bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	@Override
	public boolean isInteractive() {
		return true;
	}

	@Override
	public boolean isSelectable(Collection<Item> selection) {
		return true;
	}

	@Override
	public boolean acceptsMove(int dx, int dy) {
		return true;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public boolean isInside(int x, int y) {
		return getBounds().contains(x, y);
	}

	@Override
	public Item findItemAt(int x, int y) {
		if(getBounds().contains(x, y))
			return this;
		else
			return null;
	}

	@Override
	public int getFlags() {
		return flags;
	}

	@Override
	public void setParent(ParentItem parent) {
		this.parent = parent;
	}

	@Override
	public ParentItem getParent() {
		return parent;
	}

	@Override
	public void move(int dx, int dy) {
		Rectangle bounds = getBounds();
		bounds.x += dx;
		bounds.y += dy;
		getParent().moved(this, dx, dy);
	}

}
