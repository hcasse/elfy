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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import elf.ui.Monitor;
import elf.ui.Style;
import elf.ui.Style.FontSize;
import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * Interface for objects producing a Swing component.
 * @author casse
 */
public abstract class Component implements elf.ui.Component, elf.ui.Style.Listener {
	static final Dimension
		ZERO = new Dimension(0, 0),
		HFILL = new Dimension(Short.MAX_VALUE, 0),
		VFILL = new Dimension(0, Short.MAX_VALUE),
		FILL = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	Parent parent;
	Style style;
	
	/**
	 * Get the parent of the component.
	 * @return	Component container.
	 */
	public Parent getParent() {
		return parent;
	}
	
	/**
	 * Get the matching Swing component.
	 * @param view TODO
	 * @return	Swing Component.
	 */
	public abstract JComponent getComponent(View view);

	/**
	 * Initialize button for an entity.
	 * @param component		Button.
	 * @param entity		Current entity.
	 */
	protected void prepareEntity(AbstractButton component, Entity entity) {
		if(entity.getHelp() != null)
			component.setToolTipText(entity.getHelp());
		if(entity.getMnemonic() != 0)
			component.setMnemonic(entity.getMnemonic());
	}
	
	/**
	 * Prepare the component.
	 * @param component		Current component.
	 * @param action		Current action.
	 */
	protected void prepareButton(AbstractButton component, Action action) {
		prepareEntity(component, action);
		component.setEnabled(action.isEnabled());
	}
	
	/**
	 * Prepare the component before SWING component creation.
	 * @param view	Current view.
	 */
	protected void prepare(View view) {
		if(style != null)
			style.addListener(this);
	}
	
	@Override
	public void dispose() {
		if(style != null)
			style.removeListener(this);
	}
	
	/**
	 * Add a border around the component for debugging purpose.
	 * @param component		Component to put border around.
	 * @param color			Color of the border.
	 */
	protected void debugBorder(JComponent component, Color color) {
		component.setBorder(BorderFactory.createLineBorder(color, 4));
	}

	@Override
	public Monitor getMonitor() {
		return getParent().getMonitor();
	}

	/**
	 * Get the owner view.
	 * @return	Parent view.
	 */
	public View getView() {
		return getParent().getView();
	}

	@Override
	public void onUpdate(int[] items) {
	}

	@Override
	public void setStyle(Style style) {
		this.style = style;
	}
	
	/**
	 * Compute the font size matching the font style.
	 * @param component	Container component.
	 * @param font		Initial font.
	 * @return			Matching font.
	 */
	protected java.awt.Font getFontStyle(java.awt.Component component, java.awt.Font font) {
		
		// select the size
		int size = font.getSize();
		FontSize fs = style.getFontSize();
		if(fs == null)
			return font;
		switch(fs.getType()) {
		case Style.MEDIUM:
			return font;
		case Style.XX_SMALL:
			size = (int)(size * 0.4);
			break;
		case Style.X_SMALL:
			size = (int)(size * 0.6);
			break;
		case Style.SMALL:
			size = (int)(size * 0.8);
			break;
		case Style.LARGE:
			size = (int)(size * 1.2);
			break;
		case Style.X_LARGE:
			size = (int)(size * 1.6);
			break;
		case Style.XX_LARGE:
			size = (int)(size * 2.0);
			break;
		case Style.SMALLER:
			size = (int)(size * 0.8);
			break;
		case Style.LARGER:
			size = (int)(size * 1.2);
			break;
		case Style.LENGTH_PX:
			// TODO
			break;
		case Style.LENGTH_CM:
			// TODO
			break;
		case Style.LENGTH_EM:
			// TODO
			break;
		case Style.LENGTH_PT:
			size = (int)fs.getValue();
		case Style.PERCENT:
			size = (int)(size * fs.getValue() / 100);
			break;
		default:
			return font;
		}
		
		// allocate the font
		return font.deriveFont((float)size);
	}

	public java.awt.Color getColor(java.awt.Color color) {
		elf.swing.UI.Color scolor = (elf.swing.UI.Color)style.getColor();
		if(scolor == null)
			return color;
		else
			return scolor.getColor();
	}

	/**
	 * This method is called to let the component take the focus
	 * if it wants.
	 * @return	True if the focus is taken, false else.
	 */
	public boolean takeFocus() {
		return false;
	}
	
	/**
	 * Display components composing a Swing UI (for debugging purpose).
	 * @param component		Component to display.
	 * @param indent		Current indentation.
	 */
	public static void display(java.awt.Component component, int indent) {
		
		// display current item
		for(int i = 0; i < indent; i++)
			System.out.print("  ");
		System.out.println("" + component);
		
		if(component instanceof javax.swing.Box || component instanceof javax.swing.JPanel) {
			java.awt.Container c = (java.awt.Container)component;
			for(int i = 0; i < c.getComponentCount(); i++)
				display(c.getComponent(i), indent + 1);
		}
		
		else if(component instanceof javax.swing.JScrollPane)
			display(((javax.swing.JScrollPane)component).getViewport().getView(), indent + 1);
		
	}
	
}
