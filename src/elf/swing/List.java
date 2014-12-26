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
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import elf.ui.AbstractDisplayer;
import elf.ui.Displayer;
import elf.ui.meta.SingleVar;
import elf.ui.meta.CollectionVar;

/**
 * List component.
 * @author casse
 */
public class List<T> implements elf.ui.List<T>, Component {
	private SingleVar<T> select = new SingleVar<T>();
	private CollectionVar<T> coll = new CollectionVar<T>(new Vector<T>());
	private Displayer<T> display = new AbstractDisplayer<T>();
	private final Model model = new Model();
	private JList<T> jlist = new JList<T>();
	private JScrollPane spane = new JScrollPane(jlist, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	/**
	 * Build a list.
	 */
	public List() {
		coll.addListener(model);
		jlist.setModel(model);
		
		// handlers for selection
		jlist.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent arg0)
				{ select.set(jlist.getSelectedValue()); }
		});
		jlist.getModel().addListDataListener(new ListDataListener() {
			@Override public void contentsChanged(ListDataEvent event)
				{ select.set(jlist.getSelectedValue()); }
			@Override public void intervalAdded(ListDataEvent arg0)
				{ select.set(jlist.getSelectedValue()); }
			@Override public void intervalRemoved(ListDataEvent arg0)
				{ select.set(jlist.getSelectedValue()); }
		});
		
		// add displayer
		jlist.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				java.awt.Component r = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				@SuppressWarnings("unchecked")
				T object = (T)value;
				setText(display.asString(object));
				return r;
			}
		});

		// other configuration
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		spane.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		spane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
	}
	
	/**
	 * Remove the component.
	 */
	public void dispose() {
		coll.removeListener(model);
	}
	
	@Override
	public SingleVar<T> getSelector() {
		return select;
	}

	@Override
	public void setSelector(SingleVar<T> select) {
		select.set(this.select.get());
		this.select = select;
	}

	@Override
	public CollectionVar<T> getCollection() {
		return coll;
	}

	@Override
	public void setCollection(CollectionVar<T> coll) {
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
			return coll.size();
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
		}

		@Override
		public void onChange() {
			update();
			this.fireContentsChanged(this, 0, array.size() - 1);			
		}
	}

	@Override
	public JComponent getComponent() {
		return spane;
	}

}
