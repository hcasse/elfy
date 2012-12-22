/*
 * ElfUI library
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

/**
 * Over-handler highlighting the item under.
 * @author casse
 *
 */
public class HighlightOverHandler implements OverHandler {
	Canvas canvas;
	Item item;
	
	@Override
	public void onMove(MouseEvent event) {
		
		// already on the same ?
		if(item != null) {
			if(item.isInside(event.getX(), event.getY()))
				return;
			else
				onLeave();
		}
		
		// look for a new one
		item = canvas.findItemAt(event.getX(), event.getY());
		if(item != null && item.isInteractive())
			item.setFlags(item.getFlags() | Item.OVER);
	}

	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
		item = null;
	}

	@Override
	public void uninstall() {
		canvas = null;
		onLeave();
		item = null;
	}

	@Override
	public void onLeave() {
		if(item != null) {
			item.setFlags(item.getFlags() & ~Item.OVER);
			item = null;
		}
	}

}
