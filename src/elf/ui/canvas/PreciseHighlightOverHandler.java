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

import elf.ui.meta.Var;

/**
 * Over-handler highlighting the item under with re-calculation of what is under according to depth.
 * @author casse
 */
public class PreciseHighlightOverHandler extends HighlightOverHandler {
	
	public PreciseHighlightOverHandler(Var<Item> data) {
		super(data);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
		// already on the same ?
		if(data.get() != null) {
			Item new_item = data.get().getParent().findItemAt(event.getX(), event.getY());
			if(new_item == data.get())
				return;
			else {
				onLeave();
				if(data.get() != null) {
					data.set(new_item);
					data.get().setFlags(data.get().getFlags() | Item.OVER);
				}
				return;
			}
		}
		
		// look for a new one
		data.set(canvas.findItemAt(event.getX(), event.getY()));
		if(data.get() != null && data.get().isInteractive())
			data.get().setFlags(data.get().getFlags() | Item.OVER);
	}

}
