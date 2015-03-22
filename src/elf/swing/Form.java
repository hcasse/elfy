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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.Transient;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import elf.ui.CheckBox;
import elf.ui.EnumField;
import elf.ui.SubsetField;
import elf.ui.TextField;
import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Entity;
import elf.ui.meta.EnumVar;
import elf.ui.meta.Factory;
import elf.ui.meta.Var;

/**
 * Swing implementation of a form.
 * @author casse
 */
public class Form extends Parent implements elf.ui.Form {
	private int style = STYLE_TWO_COLUMN,
				button_style = Button.STYLE_ICON_TEXT,
				button_alignment = LEFT;
	private int enter_mode = ENTER_NEXT_AND_SUBMIT;
	private Action main_action;
	private LinkedList<Action> actions = new LinkedList<Action>();
	private LinkedList<elf.swing.Field> fields = new LinkedList<elf.swing.Field>();
	private LinkedList<Label> labels = new LinkedList<Label>();
	private boolean visible = true;
	private JComponent component, first, last;
	private View view;
	private Factory factory;
	
	public Form(Factory factory) {
		this.factory = factory;
	}

	/**
	 * Make a two-column form.
	 * @return	Built form.
	 */
	private JComponent makeTwoColumn(View view) {
		
		// build the panel
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(100, 100));
		panel.setMaximumSize(new Dimension(100, 100));
		GridBagConstraints c = new GridBagConstraints();	
		c.ipadx = 4;
		c.ipady = 4;
		c.weightx = 0;
		c.weighty = 0.;
		c.insets = new Insets(2, 2, 2, 2);
		
		// build the fields
		EnterListener listener = null;
		if(enter_mode != ENTER_IGNORE)
			listener = new EnterListener();
		int i = 0;
		for(elf.swing.Field field: fields) {
			Label label = new Label(field, true);
			labels.add(label);
			c.gridx = 0;
			c.gridy = i;
			c.anchor = GridBagConstraints.NORTHEAST;
			panel.add(label, c);
			last = field.getComponent(view);
			c.gridx = 1;
			c.gridy = i;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.weightx = 1.;
			if(i == fields.size() - 1)
				c.weighty = 1.;
			panel.add(last, c);
			c.weightx = 0.;
			if(listener != null)
				last.addKeyListener(listener);
			if(first == null)
				first = last;
			i++;
		}
		debugBorder(panel, BLUE);
		return panel;
	}
	
	private JComponent makeVertical(View view) {
		
		// build the box
		javax.swing.Box box = javax.swing.Box.createVerticalBox();
		
		// build the fields
		EnterListener listener = null;
		if(enter_mode != ENTER_IGNORE)
			listener = new EnterListener();
		for(elf.swing.Field field: fields) {
			Label label = new Label(field, false);
			labels.add(label);
			label.setMaximumSize(HFILL);
			label.setAlignmentX(javax.swing.Box.LEFT_ALIGNMENT);
			box.add(label);
			JComponent component = field.getComponent(view);
			component.setAlignmentX(javax.swing.Box.LEFT_ALIGNMENT);
			box.add(component);
			if(listener != null)
				component.addKeyListener(listener);
			if(!field.isReadOnly())
				last = component;
			if(first == null && last != null)
				first = last;
		}		
		return box;
	}
	
	@Override
	public void addAction(Action action) {
		actions.addLast(action);
		if(main_action == null)
			main_action = action;
	}

	@Override
	public JComponent getComponent(View view) {
		if(component == null) {
			this.view = view;
			
			// build the form
			JComponent form;
			if(style == STYLE_VERTICAL)
				form = makeVertical(view);
			else
				form = makeTwoColumn(view);
			JScrollPane spane = new JScrollPane(form);
			spane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			spane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			spane.setViewportBorder(null);
			spane.getViewport().setOpaque(false);
			
			// build the buttons
			if(!visible)
				component = spane;
			else {
				javax.swing.Box box = javax.swing.Box.createVerticalBox();
				component = box;
				box.add(spane);
				ActionBar bar = new ActionBar();
				bar.setStyle(button_style);
				bar.setAlignment(button_alignment);
				for(Action action: actions)
					bar.add(action);
				box.add(bar.getComponent(view));				
			}
		}
		component.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		component.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		return component;
	}

	@Override
	public <T> TextField<T> addTextField(Var<T> var) {
		elf.swing.TextField<T> field = new elf.swing.TextField<T>(var);
		fields.add(field);
		return field;
	}

	@Override
	public void setButtonStyle(int style) {
		button_style = style;
	}

	@Override
	public void setButtonAlignment(int alignment) {
		button_alignment = alignment;
	}

	@Override
	public void setEnterMode(int mode) {
		enter_mode = mode;
	}

	/**
	 * Listener for enter key.
	 * @author casse
	 */
	private class EnterListener extends KeyAdapter {
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() != '\n')
				return;

			// transfer focus
			if(e.getComponent() == last)
				first.requestFocus();
			else
				e.getComponent().transferFocus();
			
			// submission
			if(enter_mode == ENTER_SUBMIT
			||(enter_mode == ENTER_NEXT_AND_SUBMIT && e.getComponent() == last)) 
				if(main_action != null && main_action.isEnabled())
					main_action.run();
		}	
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for(Label label: labels)
			label.dispose();
		labels.clear();
		component = null;
		first = null;
		last = null;
	}

	protected <T extends Field> T add(T field) {
		fields.add(field);
		addChild(field);
		return field;
	}

	@Override
	public void setButtonVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public CheckBox addCheckBox(Var<Boolean> var) {
		return add(new elf.swing.CheckBox(var));
	}

	@Override
	public <T> SubsetField<T> addSubsetField(CollectionVar<T> set) {
		return add(new elf.swing.SubsetField<T>(set));
	}

	@Override
	public <T> EnumField<T> addEnumField(EnumVar<T> var) {
		return add(new  elf.swing.EnumField<T>(var));
	}

	/**
	 * Label specialization with entity change support.
	 * @author casse
	 */
	private class Label extends JLabel implements Entity.EntityListener {
		private static final long serialVersionUID = 1L;
		private Field field;
		
		public Label(Field field, boolean right) {
			super("", right ? SwingConstants.RIGHT : SwingConstants.LEFT);
			this.field = field;
			field.getEntity().addListener(this);
			configure();
		}
		
		private void configure() {
			this.setText(field.getEntity().getLabel());
			elf.ui.Icon icon = field.getEntity().getIcon();
			if(icon != null)
				this.setIcon(view.getIcon(icon).get(Icon.NORMAL, Icon.TEXTUAL));
		}
		
		public void dispose() {
			field.getEntity().removeListener(this);
		}
		
		@Override
		public void onChange(Entity entity) {
			configure();
		}
		
	}

	@Override
	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	public Factory getFactory() {
		return factory;
	}

	@Override
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Override
	public elf.ui.Field addField(Var<?> var) throws NoFieldError {
		Factory.Maker maker = factory.get(var.get().getClass());
		if(maker == null)
			throw new NoFieldError("no field for " + var.getLabel());
		return add((Field)maker.make(this, var));
	}

	@Override
	public void addMainAction(Action action) {
		actions.add(action);
		main_action = action;
	}
}
