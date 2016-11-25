/*
 * ElfCore library
 * Copyright (c) 2016 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.ui;

import java.util.LinkedList;

import elf.data.Pair;
import elf.ui.UI.Color;

/**
 * Descriptor of style a-la CSS.
 * @author casse
 */
public class Style {
	public static final Style DEFAULT = new Style();
	
	// identifiers
	public static final int
		NOBE		= 0,
		FONT_SIZE	= 1,	// type FontSize
		COLOR		= 2;	// Color
		
	
	public static final int
		MEDIUM		= 0,
		XX_SMALL	= 1,
		X_SMALL		= 2,
		SMALL		= 3,
		LARGE		= 4,
		X_LARGE		= 5,
		XX_LARGE	= 6,
		SMALLER		= 7,
		LARGER		= 8,
		LENGTH_PX	= 9,
		LENGTH_CM	= 11,
		LENGTH_EM	= 12,
		LENGTH_PT	= 13,
		PERCENT		= 14;
	
	// local fields
	private LinkedList<Listener> listeners;
	private LinkedList<Pair<Integer, Object>> items;
	private Style parent;
	
	/**
	 * No parent style constructor.
	 */
	public Style() {
	}
	
	/**
	 * Style constructor with parent.
	 * @param parent	Parent style.
	 */
	public Style(Style parent) {
		this.parent = parent;
	}
	
	/**
	 * Constructor with parent and list of item.
	 * @param parent	Parent style or null.
	 * @param def		List of pairs(Intger key, Object value).
	 */
	public Style(Style parent, Object... items) {
		this.parent = parent;
		setItems(items);
	}
	
	/**
	 * Get the parent style if any.
	 * @return
	 */
	public final Style getParent() {
		return parent;
	}
	
	/**
	 * Add a new listener.
	 * @param listener	Added listener.
	 */
	public final void addListener(Listener listener) {
		if(listeners == null)
			listeners = new LinkedList<Listener>();
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener.
	 * @param listener	Removed listener.
	 */
	public final void removeListener(Listener listener) {
		if(listeners != null) {
			listeners.remove(listener);
			if(listeners.isEmpty())
				listeners = null;
		}
	}
	
	/**
	 * Fire listeners for an update event.
	 * @param items
	 */
	private final void fireUpdate(int[] items) {
		if(listeners != null)
			for(Listener listener: listeners)
				listener.onUpdate(items);
	}
	
	/**
	 * Find a list item by its key.
	 * @param key	Looked key.
	 * @param def	Default value.
	 * @return		Found style or default value.
	 */
	public final Object get(int key, Object def) {
		if(items == null)
			return null;
		for(Pair<Integer, Object> item: items)
			if(item.fst == key)
				return item.snd;
		if(parent != null)
			return parent.get(key, def);
		else
			return null;
	}
	
	/**
	 * Find a list item by its key.
	 * @param key	Looked key.
	 * @param def	Default value.
	 * @return		Found style or null.
	 */
	public final Object get(int key) {
		return get(key, null);
	}
	
	/**
	 * Set a style item without listener invocation.
	 * @param key	Key to set.
	 * @param val	Value to set.
	 */
	private final void setRaw(int key, Object val) {
		if(items == null)
			items = new LinkedList<Pair<Integer, Object>>();
		for(Pair<Integer, Object> item: items)
			if(item.fst == key) {
				item.snd = val;
				return;
			}
		items.add(new Pair<Integer, Object>(key, val));
	}
	
	/**
	 * Set a property of the style.
	 * @param key	Key to set.
	 * @param val	Value to set.
	 */
	public final void set(int key, Object val) {
		setRaw(key, val);
		fireUpdate(new int[] { key });
	}
	
	/**
	 * Set a set of preoperties.
	 * @param items		Pair of (Integer, Object) values.
	 */
	public final void setItems(Object... items) {
		int[] keys = new int[items.length];
		for(int i = 0; i < keys.length; i += 2) {
			int key = (Integer)items[i];
			setRaw(key, items[i + 1]);
			keys[i / 2] = key;
		}
		fireUpdate(keys);
	}
	
	/**
	 * Get the font size item.
	 * @return	Return font size item or null.
	 */
	public FontSize getFontSize() {
		return (FontSize)get(FONT_SIZE);
	}
	
	/**
	 * Change the font size.
	 * @param fs	Font size to change.
	 */
	public void setFontSize(FontSize fs) {
		set(FONT_SIZE, fs);
	}
	
	/**
	 * Get the text color item.
	 * @return	Color item.
	 */
	public Color getColor() {
		return (Color)get(COLOR);
	}
	
	/**
	 * Get the text color item.
	 * @param color	Text color item.
	 */
	public void setColor(Color color) {
		set(COLOR, color);
	}

	/**
	 * Listener for style update.
	 * @author casse
	 */
	public interface Listener {
		
		/**
		 * Called when one style is changed.
		 * @param items		List of changed items.
		 */
		void onUpdate(int[] items);
		
	};
	
	/**
	 * Descriptor for font size.
	 * @author casse
	 */
	public static class FontSize {
		private int type;
		private float value;
		
		public FontSize(int type) {
			this.type = type;
		}
		
		public FontSize(int type, double value) {
			this.type = type;
			this.value = (float)value;
		}
		
		public final int getType() {
			return type;
		}
		
		public final float getValue() {
			return value;
		}
	};
	

}
