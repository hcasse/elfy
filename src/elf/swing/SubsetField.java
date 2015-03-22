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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import elf.ui.AbstractDisplayer;
import elf.ui.Displayer;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Entity;
import elf.util.EnumIterator;

/**
 * Swing implementation of subset field.
 * @author casse
 *
 * @param <T>	Type of values.
 */
public class SubsetField<T> extends Field implements elf.ui.SubsetField<T>, ItemListener {
	private CollectionVar<T> set;
	private CollectionVar<T> subset = new CollectionVar<T>(new Vector<T>());
	private Displayer<T> displayer = new AbstractDisplayer<T>();
	private Hashtable<T, JCheckBox> cbox = new Hashtable<T, JCheckBox>();
	private Hashtable<JCheckBox, T> value = new Hashtable<JCheckBox, T>();
	private Box box;
	private JScrollPane spane;
	private boolean do_break;
	
	public SubsetField(CollectionVar<T> set) {
		this.set = set;
	}
	
	@Override
	public Entity getEntity() {
		return set;
	}

	@Override
	public CollectionVar<T> getSubset() {
		return subset;
	}

	@Override
	public void setSubset(CollectionVar<T> subset) {
		subset.getCollection().clear();
		subset.getCollection().addAll(this.subset.getCollection());
		this.subset = subset;
	}

	@Override
	public void setDisplayer(Displayer<T> displayer) {
		this.displayer = displayer;
	}

	/**
	 * Add a value.
	 * @param v		Added value.
	 */
	private void addValue(T v) {
		JCheckBox b = new JCheckBox(displayer.asString(v));
		cbox.put(v,  b);
		value.put(b,  v);
		box.add(b);
		b.setSelected(subset.contains(v));
		b.addItemListener(this);
	}
	
	/**
	 * Remove a value.
	 * @param v		Value to remove.
	 */
	private void removeValue(T v) {
		JCheckBox b = cbox.get(v);
		cbox.remove(v);
		value.remove(b);
		box.remove(b);
	}
	
	/**
	 * Initialize the maps.
	 */
	private void init() {
		cbox.clear();
		value.clear();
		for(T v: set.getCollection())
			addValue(v);
	}
	
	/**
	 * Set a value in the subset.
	 * @param v		Set value.
	 */
	private void setValue(T v) {
		do_break = true;
		JCheckBox b = cbox.get(v);
		b.setSelected(true);
		do_break = false;		
	}
	
	/**
	 * Clear a value from subset.
	 * @param v		Value to clear.
	 */
	private void clearValue(T v) {
		do_break = true;
		JCheckBox b = cbox.get(v);
		b.setSelected(false);
		do_break = false;							
	}
	
	/**
	 * Clear all values from the subset.
	 */
	private void clearAll() {
		do_break = true;
		for(JCheckBox b: new EnumIterator<JCheckBox>(cbox.elements()))
			b.setSelected(false);
		do_break = false;		
	}
	
	/**
	 * Update UI depending on a new subset.
	 */
	private void updateAll() {
		do_break = true;
		for(JCheckBox b: new EnumIterator<JCheckBox>(cbox.elements())) {
			T v = value.get(b);
			b.setSelected(subset.contains(v));
		}
		do_break = false;				
	}
	
	@Override
	public JComponent getComponent(View view) {
		if(spane == null) {
			box = Box.createVerticalBox();
			init();
			spane = new JScrollPane(box);
			spane.setPreferredSize(FILL);
			spane.setMaximumSize(FILL);

			// listener for subset
			subset.addListener(new CollectionVar.Listener<T>() {
				@Override public void onAdd(T item) { setValue(item); }
				@Override public void onRemove(T item) { clearValue(item); }
				@Override public void onClear() { clearAll(); }
				@Override public void onChange() { updateAll(); }
			});
			
			// listener for set
			set.addListener(new CollectionVar.Listener<T>() {
				@Override public void onAdd(T item) { addValue(item); }
				@Override public void onRemove(T item) { removeValue(item); }
				@Override public void onClear() { onChange(); }
				@Override public void onChange() { subset.clear(); box.removeAll(); init(); }
			});
		}
		return spane;
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if(do_break)
			return;
		JCheckBox c = (JCheckBox)event.getItemSelectable();
		T v = value.get(c);
		if(c.isSelected())
			subset.add(v);
		else
			subset.remove(v);
	}

	@Override
	public void dispose() {
		super.dispose();
		box = null;
		spane = null;
		cbox.clear();
		value.clear();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setValidity(boolean validity) {
	}

}
