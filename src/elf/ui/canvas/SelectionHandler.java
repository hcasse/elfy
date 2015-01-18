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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import elf.ui.meta.Var;
import elf.ui.meta.CollectionVar;

/**
 * Select handler managing simply the canvas selection.
 * @author casse
 */
public class SelectionHandler implements Handler, MouseListener {
	Var<Item> current;
	CollectionVar<Item> selection;
	Canvas canvas;
	
	/**
	 * Build a selection handler.
	 * @param current		Current item.
	 * @param selection		Selection collection.
	 */
	public SelectionHandler(Var<Item> current, CollectionVar<Item> selection) {
		this.current = current;
		this.selection = selection;
	}
	
	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
	}

	@Override
	public void uninstall() {
		canvas.removeMouseListener(this);
		canvas = null;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		
		// check button
		if(event.getButton() != MouseEvent.BUTTON1)
			return;
			
		// single selection
		if((event.getModifiers() & MouseEvent.SHIFT_MASK) == 0) {
			for(Item item: selection)
				item.setFlags(item.getFlags() & ~Item.SELECTED);
			selection.clear();
		}
		
		// no item ?
		if(current.get() == null)
			return;
		
		// add the item
		Item item = current.get();
		if(item.isSelectable(selection.getCollection())
		&& !selection.contains(item)) {
			selection.add(item);
			item.setFlags(item.getFlags() | Item.SELECTED);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
