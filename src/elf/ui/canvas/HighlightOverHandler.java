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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import elf.ui.meta.Data;

/**
 * Over-handler highlighting the item under.
 * @author casse
 *
 */
public class HighlightOverHandler implements Handler, MouseMotionListener, MouseListener {
	protected Data<Item> data;
	protected Canvas canvas;
	
	/**
	 * Create the highlight over handler.
	 * @param data		Data item.
	 */
	public HighlightOverHandler(Data<Item> data) {
		this.data = data;
	}
	
	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	@Override
	public void uninstall() {
		canvas.removeMouseMotionListener(this);
		canvas.removeMouseListener(this);
		canvas = null;
		onLeave();
		data.set(null);
	}

	/**
	 * Called when an item is left.
	 */
	protected void onLeave() {
		if(data.get() != null) {
			data.get().setFlags(data.get().getFlags() & ~Item.OVER);
			data.set(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
		// already on the same ?
		if(data.get() != null) {
			if(data.get().isInside(event.getX(), event.getY()))
				return;
			else
				onLeave();
		}
		
		// look for a new one
		data.set(canvas.findItemAt(event.getX(), event.getY()));
		if(data.get() != null && data.get().isInteractive())
			data.get().setFlags(data.get().getFlags() | Item.OVER);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		onLeave();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
