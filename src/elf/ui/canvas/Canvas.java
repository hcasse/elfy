/*
 * ElfSim tool
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JComponent;

/**
 * Represents with vector display.
 * @author casse
 */
public class Canvas extends JComponent implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	Dimension log_dim;
	LinkedList<Item> items = new LinkedList<Item>();
	Item over = null;
	Vector<Item> selection = new Vector<Item>();
	int last_x, last_y;
	
	/**
	 * Build a default canvas.
	 */
	public Canvas() {
		log_dim = new Dimension(1024, 1024);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	/**
	 * Build a canvas with the given dimension.
	 * @param w		Width.
	 * @param h		Height.
	 */
	public Canvas(int w, int h) {
		log_dim = new Dimension(w, h);
	}
	
	/**
	 * Add an item.
	 * @param item		Added item.
	 */
	public void add(Item item) {
		items.add(item);
		this.repaint(item.getDisplayRect());
	}
	
	/**
	 * Remove an item.
	 * @param item		Removed item.
	 */
	public void remove(Item item) {
		items.remove(item);
		this.repaint(item.getDisplayRect());		
	}

	@Override
	public Dimension getPreferredSize() {
        return log_dim;
    }

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(Item item: items)
        	if(g.getClipBounds().intersects(item.getDisplayRect()))
        		item.display(g2, (over == item ? Item.OVER : 0) | (selection.contains(item) ? Item.SELECTED : 0));
    }

	@Override
	public void mouseDragged(MouseEvent event) {
		if(!selection.isEmpty())
			for(Item item: selection) {
				repaint(item.getDisplayRect());
				item.move(event.getX() - last_x, event.getY() - last_y);
				repaint(item.getDisplayRect());
			}
		last_x = event.getX();
		last_y = event.getY();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
		// already on the same ?
		if(over != null) {
			if(over.isInside(event.getX(), event.getY()))
				return;
			else {
				this.repaint(over.getDisplayRect());
				over = null;
			}
		}
		
		// look for a new one
		for(Item item: items)
			if(item.isInside(event.getX(), event.getY())) {
				over = item;
				this.repaint(over.getDisplayRect());
			}
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
		
		// last press
		last_x = event.getX();
		last_y = event.getY();
		
		// single selection
		if(!event.isShiftDown()) {
			for(Item item: selection)
				repaint(item.getDisplayRect());
			selection.clear();
		}
		
		// add the item
		if(over != null && !selection.contains(over)) {
			selection.add(over);
			repaint(over.getDisplayRect());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	} 
	
}
