/*
 * ElfCore library
 * Copyright (c) 2014 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.swing;

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import elf.ui.meta.Action;

/**
 * A fixed position tool bar made of icons. 
 * @author casse
 */
public class ActionBar extends Component implements elf.ui.ActionBar {
	private LinkedList<Action> actions = new LinkedList<Action>();
	private int
		align = LEFT,
		axis = HORIZONTAL,
		pad = 4,
		margin = 1,
		style = Button.STYLE_ICON_TEXT;
	private Box box;
	private LinkedList<Button> buttons = new LinkedList<Button>();
	private static final int
		NONE = 0,
		PAD = 1,
		GLUE = 2;
	private static final int[] left = {
		NONE,
		GLUE,
		GLUE,
		GLUE
	};
	private static final int[] center = {
		PAD,
		PAD,
		PAD,
		GLUE
	};
	private static final int[] right = {
		GLUE,
		GLUE,
		NONE,
		GLUE
	};

	/**
	 * Add the given seperation.
	 * @param type	Type of separation.
	 * @param box	Box to add to.
	 */
	private void addSeparation(int type, Box box) {
		switch(type) {
		case NONE: 	break;
		case PAD:	box.add(Box.createHorizontalStrut(pad)); break;
		case GLUE:	box.add(Box.createGlue()); break;
		}
	}
	
	@Override
	public JComponent getComponent() {
		if(box == null) {
			
			// build the box
			if(axis == HORIZONTAL) {
				box = Box.createHorizontalBox();
				box.setMaximumSize(new Dimension(Short.MAX_VALUE, 10));
			}
			else {
				box = Box.createVerticalBox();
				box.setMaximumSize(new Dimension(10, Short.MAX_VALUE));
			}
			box.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
			
			// create the actions
			addSeparation(left[align], box);
			boolean first = true;
			for(Action action: actions) {
				if(first)
					first = false;
				else
					addSeparation(center[align], box);
				Button button = new Button(action, style);
				buttons.add(button);
				JComponent component = button.getComponent();
				if(axis == VERTICAL)
					component.setMaximumSize(new Dimension(Short.MAX_VALUE, 10));
				box.add(component);
			}
			addSeparation(right[align], box);
		}
		return box;
	}

	/**
	 * Get the alignment.
	 * @return	Alignment (one of LEFT, CENTER or RIGHT).
	 */
	public int getAlign() {
		return align;
	}

	@Override
	public void setAlignment(int alignment) {
		this.align = alignment;
	}


	/**
	 * Set the axis.
	 * @param axis	Axis (one of HORIZONTAL or VERTICAL).
	 */
	public void setAxis(int axis) {
		this.axis = axis;
	}

	@Override
	public void setPadding(int width) {
		this.pad = width;
	}

	/**
	 * Set the margin (around the bar).
	 * @param margin	Margin size.
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	@Override
	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	public void add(Action action) {
		actions.add(action);
	}

	@Override
	public void dispose() {
		super.dispose();
		for(Button button: buttons)
			button.dispose();
		box = null;
	}

}
