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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Item as a group of items.
 * @author casse
 */
public class Group implements ParentItem {
	ParentItem parent = NullItem.NULL;
	int depth;
	Rectangle bounds;
	LinkedList<Item> items = new LinkedList<Item>();
	
	/**
	 * Get the items in the group.
	 * @return		Items.
	 */
	public Iterable<Item> getItems() {
		return items;
	}
	
	/**
	 * Add an item to the group.
	 * @param item		Added item.
	 */
	public void add(Item item) {
		
		// sort in ascending way
		ListIterator<Item> it = items.listIterator();
		while(it.hasNext()) {
			Item iit = it.next();
			if(iit.getDepth() > item.getDepth()) {
				it.previous();
				break;
			}
		}
		it.add(item);

		// connect the parent link
		item.setParent(this);
		if(bounds != null) {
			Rectangle ibounds = item.getBounds(); 
			bounds.add(ibounds);
			refresh(this, ibounds.x, ibounds.y, ibounds.width, ibounds.height);
		}
	}
	
	/**
	 * Remove item from the group.
	 * @param item		Item to remove.
	 */
	public void remove(Item item) {
		items.remove(item);
		bounds = null;
	}
	
	@Override
	public int getFlags() {
		return 0;
	}

	@Override
	public void setFlags(int flags) {
	}

	@Override
	public void display(Graphics2D g) {
        for(Item item: items)
        	if(g.getClipBounds().intersects(item.getBounds()))
        		item.display(g);
	}

	@Override
	public void move(int dx, int dy) {
		getBounds();
		bounds.x += dx;
		bounds.y += dy;
		for(Item item: items)
			item.move(dx, dy);
		getParent().moved(this, dx, dy);
	}

	@Override
	public boolean isSelectable(Collection<Item> selection) {
		return false;
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
		return bounds.contains(x, y);
	}

	/*@Override
	public void onMouseEvent(MouseEvent event) {
	}*/

	@Override
	public Item findItemAt(int x, int y) {
		if(!bounds.contains(x, y))
			return null;
		Iterator<Item> it = items.descendingIterator();
		while(it.hasNext()) {
			Item found = it.next().findItemAt(x, y);
			if(found != null)
				return found;
		}
		return null;
	}

	@Override
	public Rectangle getBounds() {
		if(bounds == null) {
			bounds = new Rectangle();
			for(Item item: items)
				bounds.add(item.getBounds());
		}
		return bounds;
	}

	@Override
	public boolean isInteractive() {
		return false;
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
	public void moved(Item item, int dx, int dy) {
		Rectangle ibounds = item.getBounds();
		if(bounds != null)
			bounds.add(ibounds);
		refresh(item, ibounds.x - dx, ibounds.y - dy, ibounds.width, ibounds.height);
		refresh(item, ibounds.x, ibounds.y, ibounds.width, ibounds.height);
	}

	@Override
	public void refresh(Item item, int x, int y, int w, int h) {
		parent.refresh(item, x, y, w, h);
	}

	/**
	 * change the bounds of the group.
	 * @param x		New X.
	 * @param y		New Y.
	 * @param w		New width.
	 * @param h		New height.
	 */
	protected void setBounds(int x, int y, int w, int h) {
		if(bounds == null)
			bounds = new Rectangle(x, y, w, h);
		else
			bounds.setBounds(x, y, w, h);
	}

	/**
	 * Move the bounds of the group.
	 * @param dx	X offset.
	 * @param dy	Y offset.
	 */
	protected void moveBounds(int dx, int dy) {
		bounds.x += dx;
		bounds.y += dy;
	}

	@Override
	public Point getHandle() {
		return getBounds().getLocation();
	}
}
