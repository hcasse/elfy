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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import elf.ui.meta.Action;

/**
 * A fixed position tool bar made of icons. 
 * @author casse
 */
public class ActionBar implements Component {
	public static final int
		ICON = 0,
		TEXT = 1,
		ICON_AND_TEXT = 2,
		ICON_OVER_TEXT = 3;
	private Factory factory;
	private Action[] actions;
	private int
		align = LEFT,
		axis = HORIZONTAL,
		pad = 4,
		margin = 1,
		style = ICON;
	private Box box;
	
	public ActionBar(Factory factory, Action[] actions) {
		this.factory = factory;
		this.actions = actions;
	}
	
	@Override
	public JComponent getComponent() {
		if(box == null) {
			
			// build the box
			if(axis == HORIZONTAL)
				box = Box.createHorizontalBox();
			else
				box = Box.createVerticalBox();
			box.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
			
			// create the actions
			if(align != LEFT)
				box.add(Box.createHorizontalGlue());
			boolean first = true;
			for(int i = 0; i < actions.length; i++) {
				if(first)
					first = false;
				else if(align == SPREAD)
					box.add(Box.createGlue());
				else
					box.add(Box.createHorizontalStrut(pad));
				JComponent button;
				if(style == ICON)
					button = factory.makeToolButton(actions[i]);
				else if(style == TEXT)
					button = factory.makeButton(actions[i]);
				else if(style == ICON_AND_TEXT)
					button = null;
				else
					button = null;
				box.add(button);
			}
			if(align != RIGHT)
				box.add(Box.createHorizontalGlue());
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

	/**
	 * Set the alignment.
	 * @param align		Alignment (one of LEFT, CENTER or RIGHT).
	 */
	public void setAlign(int align) {
		this.align = align;
	}

	/**
	 * Get the axis.
	 * @return	Axis (one of HORIZONTAL or VERTICAL).
	 */
	public int getAxis() {
		return axis;
	}

	/**
	 * Set the axis.
	 * @param axis	Axis (one of HORIZONTAL or VERTICAL).
	 */
	public void setAxis(int axis) {
		this.axis = axis;
	}

	/**
	 * Get the pad value (between icons).
	 * @return	Pad value.
	 */
	public int getPad() {
		return pad;
	}

	/**
	 * Set the pad value (between icons).
	 * @param pad	Pad value.
	 */
	public void setPad(int pad) {
		this.pad = pad;
	}

	/**
	 * Get the margin (around the bar).
	 * @return	Margin size.
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * Set the margin (around the bar).
	 * @param margin	Margin size.
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	public Action[] getActions() {
		return actions;
	}

	/**
	 * Get the style.
	 * @return	Style (one of ICON, TEXT, ICON_AND_TEXT, ICON_OVER_TEXT).
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * Set the style.
	 * @param style		Style (one of ICON, TEXT, ICON_AND_TEXT, ICON_OVER_TEXT).
	 */
	public void setStyle(int style) {
		this.style = style;
	}

}
