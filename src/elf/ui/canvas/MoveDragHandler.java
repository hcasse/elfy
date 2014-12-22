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

import elf.ui.meta.CollectionVar;

/**
 * Drag handler moving the selection.
 * @author casse
 */
public class MoveDragHandler implements Handler, MouseListener, MouseMotionListener {
	Canvas canvas;
	int last_x, last_y;
	CollectionVar<Item> selection;
	
	/**
	 * Build a moving drag handler.
	 * @param selection		Current selection.
	 */
	public MoveDragHandler(CollectionVar<Item> selection) {
		this.selection = selection;
	}
	
	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	@Override
	public void uninstall() {
		canvas = null;
		canvas.removeMouseListener(this);
		canvas.removeMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		last_x = event.getX();
		last_y = event.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if(!selection.isEmpty())
			for(Item item: selection) {
				int dx = event.getX() - last_x, dy = event.getY() - last_y;
				if(item.acceptsMove(dx, dy))
					item.move(dx, dy);
			}
		last_x = event.getX();
		last_y = event.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

}
