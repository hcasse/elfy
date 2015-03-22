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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import elf.ui.Displayer;
import elf.ui.Monitor;
import elf.ui.meta.Action;
import elf.ui.meta.Entity;
import elf.ui.meta.Factory;

/**
 * Swing version of view.
 * @author casse
 */
public class View extends Container implements elf.ui.View {
	private Entity entity;
	private JFrame frame;
	private Action close_action = Action.QUIT;
	private Monitor monitor;
	private UI ui;
	private Factory factory = Factory.DEF;
	
	public View(UI ui, Entity entity) {
		this.ui = ui;
		this.entity = entity;
	}
	
	/**
	 * Get the viewer factory.
	 * @return	Viewer factory.
	 */
	public Factory getFactory() {
		return factory;
	}
	
	/**
	 * Get the current JFrame.
	 * @return	Current JFrame.
	 */
	public JFrame getFrame() {
		if(frame == null) {
			frame = new JFrame(entity.getLabel());
			
			// prepare the sub-components
			for(Component component: getComponents())
				frame.getContentPane().add(component.getComponent(this));
			
			// prepare the frame
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new WindowAdapter() {
				@Override public void windowClosing(WindowEvent e) { close_action.run(); }
			});
			frame.setSize(500, 300);
		}
		return frame;
	}
	
	@Override
	public void dispose() {
		frame = null;
	}

	@Override
	public void show() {
		getFrame().setVisible(true);
	}

	@Override
	public void hide() {
		if(frame != null)
			getFrame().setVisible(false);
	}

	@Override
	public void setCloseAction(Action action) {
		close_action = action;
	}

	@Override
	public JComponent getComponent(View view) {
		return null;
	}

	@Override
	public Monitor getMonitor() {
		if(monitor == null)
			monitor = new elf.swing.Monitor(this);
		return monitor;
	}

	@Override
	public boolean showConfirmDialog(String message, String title) {
		return JOptionPane.showConfirmDialog(frame, message, title,  JOptionPane.YES_NO_OPTION) == 0;
	}

	@Override
	public <T> SelectionDialog<T> makeSelectionDialog(String message, String title, Collection<T> values) {
		return new SelectionDialog<T>(message, title, values);
	}

	/**
	 * Get parent UI.
	 * @return	Parent UI.
	 */
	public UI getUI() {
		return ui;
	}
	
	/**
	 * Get swing icon.
	 * @param icon	UI icon.
	 * @return	Swing icon.
	 */
	public Icon getIcon(elf.ui.Icon icon) {
		return ui.getIcon(icon);
	}

	@Override
	public elf.ui.GenericDialog makeDialog(Entity entity) {
		return new Dialog(this, entity);
	}

	@Override
	public View getView() {
		return this;
	}

	/**
	 * Implementation of selection dialog.
	 * @author casse
	 *
	 * @param <T>	Type of values.
	 */
	private class SelectionDialog<T> implements elf.ui.SelectionDialog<T> {
		// TODO Make it in its own class and remove ListDialog.
		private String title, message, action;
		private Vector<T> collection = new Vector<T>();
		private Displayer<T> displayer = new Displayer<T>() {
			@Override public String asString(T value) { return value.toString(); }
			@Override public elf.ui.Icon getIcon(T value) { return null; }
		};
		private T init;
		
		public SelectionDialog(String message, String title, Collection<T> collection) {
			this.message = message;
			this.title = title;
			this.collection.addAll(collection);
		}
		
		@Override
		public T show() {
			int i = -1;
			if(init != null)
				i = collection.indexOf(init);
			ListDialog<T> dialog = new ListDialog<T>(getFrame(), message, title, collection, i, action) {
				private static final long serialVersionUID = 1L;
				@Override protected String asString(T object) { return displayer.asString(object); }			
			};
			return dialog.run();
		}

		@Override
		public void setAction(String name) {
			this.action = name;
		}

		@Override
		public void setDisplayer(Displayer<T> displayer) {
			this.displayer = displayer;
		}

		@Override
		public void setValues(Collection<T> collection) {
			this.collection.clear();
			this.collection.addAll(collection);
		}

		@Override
		public void setInitial(T value) {
			init = value;
		}
		
	}
	
}
