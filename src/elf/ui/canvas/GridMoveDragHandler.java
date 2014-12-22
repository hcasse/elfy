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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import elf.ui.meta.CollectionVar;

/**
 * Drag handler moving the selection according to a predefined grid.
 * @author casse
 */
public abstract class GridMoveDragHandler implements Handler, MouseListener, MouseMotionListener {
	Canvas canvas;
	int off_x, off_y, last_x, last_y;
	CollectionVar<Item> selection;
	
	/**
	 * Build the handler.
	 * @param selection		Selection to work with.
	 */
	public GridMoveDragHandler(CollectionVar<Item> selection) {
		this.selection = selection;
	}
	
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
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	@Override
	public void uninstall() {
		canvas.removeMouseListener(this);
		canvas.removeMouseMotionListener(this);
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
	public void mouseDragged(MouseEvent event) {
		if(selection.isEmpty())
			return;
		
		// compute the new grid position
		int nx = alignX(event.getX() + off_x);
		int ny = alignY(event.getY() + off_y);
		if(nx == last_x && ny == last_y)
			return;	
		
		// fix the items if required
		for(Item item: selection)
			item.move(nx - last_x, ny - last_y);
		
		// record the new position
		last_x = nx;
		last_y = ny;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
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
		if(selection.isEmpty() || event.getButton() != MouseEvent.BUTTON1)
			return;
		
		// compute minimal handle of selection
		last_x = Integer.MAX_VALUE;
		last_y = Integer.MAX_VALUE;
		for(Item item: selection) {
			Point ih = item.getHandle();
			last_x = Math.min(last_x, ih.x);
			last_y = Math.min(last_y, ih.y);
		}
		
		// compute the offset relative to the mouse position
		off_x = last_x - event.getX();
		off_y = last_y - event.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
