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
package elf.swing;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import elf.ui.AbstractDisplayer;
import elf.ui.Displayer;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Var;

/**
 * List component.
 * @author casse
 */
public class List<T> extends Component implements elf.ui.List<T> {
	private Handler<T> handler;
	private CollectionVar<T> coll;
	private Displayer<T> display = new AbstractDisplayer<T>();
	private Model model;
	private JList<T> jlist;
	private JScrollPane spane;
	private View view;

	/**
	 * Build a list.
	 */
	public List() {
		coll = new CollectionVar<T>(new Vector<T>());
	}
	
	/**
	 * Build a list.
	 * @param collection	Collection of the list.
	 */
	public List(CollectionVar<T> collection) {
		this.coll = collection;
	}
	
	/**
	 * Remove the component.
	 */
	public void dispose() {
		super.dispose();
		coll.removeListener(model);
		jlist = null;
		spane = null;
		model = null;
	}
	
	@Override
	public Var<T> getSelector() {
		return handler.getselector();
	}

	@Override
	public void setSelector(Var<T> select) {
		if(jlist != null)
			handler.cleanup(jlist);
		handler = new SingleHandler(select);
		if(jlist != null)
			handler.setup(jlist);
	}

	@Override
	public CollectionVar<T> getCollection() {
		return coll;
	}

	@Override
	public void setCollection(CollectionVar<T> coll) {
		handler.reset();
		coll.removeListener(model);
		this.coll = coll;
		coll.addListener(model);
		model.update();
	}

	@Override
	public Displayer<T> getDisplayer() {
		return display;
	}

	@Override
	public void setDisplayer(Displayer<T> display) {
		this.display = display;
		if(jlist != null)
			jlist.repaint();
	}

	/**
	 * Internal model.
	 * @author casse
	 */
	private class Model extends AbstractListModel<T> implements CollectionVar.Listener<T> {
		private static final long serialVersionUID = 1L;
		ArrayList<T> array = new ArrayList<T>();

		public Model() {
			update();
		}
		
		private void update() {
			array.clear();
			array.addAll(coll.getCollection());
		}
		
		@Override
		public T getElementAt(int index) {
			if(index < array.size())
				return array.get(index);
			else
				return null;
		}

		@Override
		public int getSize() {
			return array.size();
		}

		@Override
		public void onAdd(T item) {
			update();
			this.fireContentsChanged(this, 0, array.size() - 1);
		}

		@Override
		public void onRemove(T item) {
			update();
			this.fireContentsChanged(this, 0, array.size() - 1);
		}

		@Override
		public void onClear() {
			update();
			this.fireContentsChanged(this, 0, array.size() - 1);
			handler.reset();
		}

		@Override
		public void onChange() {
			update();
			this.fireContentsChanged(this, 0, array.size() - 1);			
			handler.recompute();
		}
	}

	@Override
	public JComponent getComponent(View view) {
		if(spane == null) {
			this.view = view;
			jlist = new JList<T>();
			handler.setup(jlist);
			model = new Model();
			coll.addListener(model);
			jlist.setModel(model);
			
			// add displayer
			jlist.setCellRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					java.awt.Component r = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					@SuppressWarnings("unchecked")
					T object = (T)value;
					elf.ui.Icon icon = display.getIcon(object);
					if(icon != null)
						this.setIcon(List.this.view.getIcon(icon).get(elf.swing.Icon.NORMAL, elf.swing.Icon.TEXTUAL));
					setText(display.asString(object));
					return r;
				}
			});

			// other configuration
			spane = new JScrollPane(jlist, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			spane.setPreferredSize(jlist.getPreferredSize());
			spane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		}
		return spane;
	}

	@Override
	public void setSelector(CollectionVar<T> select) {
		if(jlist != null)
			handler.cleanup(jlist);
		handler = new MultiHandler(select);
		if(jlist != null)
			handler.setup(jlist);
	}

	@Override
	public CollectionVar<T> getMultiSelector() {
		return handler.getMultiSelector();
	}
	
	private interface Handler<T> {
		
		void setup(JList<T> list);
		
		void cleanup(JList<T> list);
		
		Var<T> getselector();
		
		CollectionVar<T> getMultiSelector();

		void reset();

		void recompute();

	};
	
	private class SingleHandler implements Handler<T>, ListSelectionListener {
		Var<T> select;

		SingleHandler(Var<T> select) {
			this.select = select;
		}
		
		@Override
		public Var<T> getselector() {
			return select;
		}

		@Override
		public CollectionVar<T> getMultiSelector() {
			return null;
		}

		@Override
		public void setup(JList<T> list) {
			list.addListSelectionListener(this);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		@Override
		public void cleanup(JList<T> list) {
			list.removeListSelectionListener(this);			
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			select.set(jlist.getSelectedValue());
		}

		@Override
		public void reset() {
			select.set(null);
		}

		@Override
		public void recompute() {
			T value = jlist.getSelectedValue();
			if(value != select.get())
				select.set(value);
		}
		
	}
	
	private class MultiHandler implements Handler<T>, ListSelectionListener {
		private CollectionVar<T> select;
		
		public MultiHandler(CollectionVar<T> select) {
			this.select = select;
		}
		
		@Override
		public Var<T> getselector() {
			return null;
		}

		@Override
		public CollectionVar<T> getMultiSelector() {
			return select;
		}

		@Override
		public void setup(JList<T> list) {
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			list.addListSelectionListener(this);
		}

		@Override
		public void cleanup(JList<T> list) {
			list.removeListSelectionListener(this);
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
		}

		@Override
		public void reset() {
			select.clear();
		}

		@Override
		public void recompute() {
			Collection<T> selected = jlist.getSelectedValuesList();
			
			// compute to delete
			Vector<T> to_delete = new Vector<T>(select.getCollection());
			to_delete.removeAll(selected);
			for(T value: to_delete)
				select.remove(value);
			
			// compute to add
			selected.removeAll(select.getCollection());
			for(T value: selected)
				select.remove(value);
		}
		
	}

}
