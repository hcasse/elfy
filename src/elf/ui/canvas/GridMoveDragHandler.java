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

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Drag handler moving the selection according to a predefined grid.
 * @author casse
 */
public abstract class GridMoveDragHandler implements DragHandler {
	Canvas canvas;
	int off_x, off_y, last_x, last_y;
	
	/**
	 * Get the grid size on X axis.
	 * @return		X grid size.
	 */
	public abstract int getGridX();
	
	/**
	 * Get the grid size on Y axis.
	 * @return		Y grid size.
	 */
	public abstract int getGridY();
	
	@Override
	public void install(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void uninstall() {
		canvas = null;
	}

	/**
	 * Align a coordinate according to X axis.
	 * @param x		Coordinate to align.
	 * @return		Aligned coordinate.
	 */
	public final int alignX(int x) {
		int gx = getGridX();
		return (x + gx/2) / gx * gx;
	}
	
	/**
	 * Align a coordinate according to Y axis.
	 * @param y		Coordinate to align.
	 * @return		Aligned coordinate.
	 */
	public final int alignY(int y) {
		int gy = getGridY();
		return (y + gy/2) / gy * gy;
	}
	
	@Override
	public void onBegin(MouseEvent event) {
		System.out.println("DEBUG: drag");
		if(canvas.getSelection().isEmpty())
			return;
		
		// compute minimal handle of selection
		last_x = Integer.MAX_VALUE;
		last_y = Integer.MAX_VALUE;
		for(Item item: canvas.getSelection()) {
			Point ih = item.getHandle();
			last_x = Math.min(last_x, ih.x);
			last_y = Math.min(last_y, ih.y);
		}
		
		// compute the offset relative to the mouse position
		off_x = last_x - event.getX();
		off_y = last_y - event.getY();
		System.out.println("DEBUG: off_x, off_y = " + off_x + ", " + off_y);
		System.out.println("DEBUG: last_x, last_y = " + last_x + ", " + last_y);
	}

	@Override
	public void onDrag(MouseEvent event) {
		if(canvas.getSelection().isEmpty())
			return;
		
		// compute the new grid position
		int nx = alignX(event.getX() + off_x);
		int ny = alignY(event.getY() + off_y);
		System.out.println("DEBUG: mouse = " + event.getX() + ", " + event.getY());
		System.out.println("DEBUG: nx, ny = " + nx + ", " + ny);
		if(nx == last_x && ny == last_y)
			return;	
		
		// fix the items if required
		for(Item item: canvas.getSelection())
			item.move(nx - last_x, ny - last_y);
		
		// record the new position
		last_x = nx;
		last_y = ny;
	}

	@Override
	public void onEnd(MouseEvent event) {
	}

}
