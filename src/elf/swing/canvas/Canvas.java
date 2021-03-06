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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Comparator;

import javax.swing.JComponent;

/**
 * Represents with vector display.
 * @author casse
 */
public class Canvas extends JComponent {
	private static final long serialVersionUID = 1L;

	// state
	int min_w = 1024, min_h = 1024;
	Group group = new CanvasGroup();
	
	/**
	 * Build a default canvas.
	 */
	public Canvas() {
		group.getBounds().x = 0;
		group.getBounds().y = 0;
		group.getBounds().width = min_w;
		group.getBounds().height = min_h;
	}
	
	/**
	 * Change the over-handler.
	 * @param handler		New over-handler.
	 */
	/*public void setOverHandler(OverHandler handler) {
		over_handler.uninstall();
		over_handler = handler;
		over_handler.install(this);
	}*/
	
	/**
	 * Set the current drag handler.
	 * @param handler	New drag handler.
	 */
	/*public void setDragHandler(DragHandler handler) {
		drag_handler.uninstall();
		drag_handler = handler;
		drag_handler.install(this);
	}*/
	
	/**
	 * Build a canvas with the given dimension.
	 * @param w		Width.
	 * @param h		Height.
	 */
	public Canvas(int w, int h) {
		min_w = w;
		min_h = h;
	}
	
	/**
	 * Add an item.
	 * @param item		Added item.
	 */
	public void add(Item item) {
		group.add(item);
	}
	
	/**
	 * Remove an item.
	 * @param item		Removed item.
	 */
	public void remove(Item item) {
		group.remove(item);
	}

	/**
	 * Find the item at the given position.
	 * @param x		X position.
	 * @param y		Y position.
	 * @return		Found item or null.
	 */
	public Item findItemAt(int x, int y) {
		return group.findItemAt(x, y);
	}

	@Override
	public Dimension getPreferredSize() {
		Rectangle r = group.getBounds();
		return new Dimension(Math.max(min_w, r.width), Math.max(min_h, r.height));
	}

	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		group.display(g2);
    }

	static class ItemComparator implements Comparator<Item> {

		@Override
		public int compare(Item o1, Item o2) {
			return o2.getDepth() - o1.getDepth();
		}
		
		
	}
	
	/**
	 * Group for the canvas. Perform link between canvas items and swing.
	 * @author casse
	 *
	 */
	private class CanvasGroup extends Group {

		@Override
		public void refresh(Item item, int x, int y, int w, int h) {
			Canvas.this.repaint(x, y, w, h);
		}
		
	}

}
