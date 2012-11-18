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
 * Drag handler moving the selection.
 * @author casse
 */
public class MoveDragHandler implements DragHandler {
	Canvas canvas;
	int last_x, last_y;
	
	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void uninstall() {
		canvas = null;
	}

	@Override
	public void onBegin(MouseEvent event) {
		last_x = event.getX();
		last_y = event.getY();
	}

	@Override
	public void onDrag(MouseEvent event) {
		if(!canvas.getSelection().isEmpty())
			for(Item item: canvas.getSelection()) {
				canvas.repaint(item.getDisplayRect());
				item.move(event.getX() - last_x, event.getY() - last_y);
				canvas.repaint(item.getDisplayRect());
			}
		last_x = event.getX();
		last_y = event.getY();
	}

	@Override
	public void onEnd(MouseEvent event) {
	}

}
