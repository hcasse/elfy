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

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import elf.ui.CheckBox;
import elf.ui.SubsetField;
import elf.ui.TextField;
import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.Var;

public class Form extends Component implements elf.ui.Form {
	private int style, button_style = Button.STYLE_ICON_TEXT, button_alignment = LEFT;
	private int enter_mode = ENTER_NEXT_AND_SUBMIT;
	private LinkedList<Action> actions = new LinkedList<Action>();
	private LinkedList<elf.swing.Field> fields = new LinkedList<elf.swing.Field>();
	private boolean visible = true;
	private JComponent component, first, last;
	
	public Form(int style, Action action) {
		this.style = style;
		actions.add(action);
	}

	/**
	 * Make a two-column form.
	 * @return	Built form.
	 */
	private JComponent makeTwoColumn() {
		
		// build the panel
		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(fields.size(), 2);
		panel.setLayout(layout);
	
		// build the fields
		EnterListener listener = null;
		if(enter_mode != ENTER_IGNORE)
			listener = new EnterListener();
		for(elf.swing.Field field: fields) {
			JLabel label = new JLabel(field.getEntity().getLabel());
			label.setAlignmentX(1);
			panel.add(label);
			last = field.getComponent();
			panel.add(last);
			if(listener != null)
				last.addKeyListener(listener);
			if(first == null)
				first = last;
		}
		return panel;
	}
	
	private JComponent makeVertical() {
		
		// build the box
		javax.swing.Box box = javax.swing.Box.createVerticalBox();
		
		// build the fields
		EnterListener listener = null;
		if(enter_mode != ENTER_IGNORE)
			listener = new EnterListener();
		for(elf.swing.Field field: fields) {
			JLabel label = new JLabel(field.getEntity().getLabel());
			box.add(label);
			last = field.getComponent();
			box.add(last);
			if(listener != null)
				last.addKeyListener(listener);
			if(first == null)
				first = last;
		}		
		return box;
	}
	
	@Override
	public void addAction(Action action) {
		actions.addLast(action);
	}

	@Override
	public JComponent getComponent() {
		if(component == null) {
			
			// build the form
			JComponent form;
			if(style == STYLE_VERTICAL)
				form = makeVertical();
			else
				form = makeTwoColumn();
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
				box.add(bar.getComponent());				
			}
		}
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
				if(actions.getFirst().isEnabled())
					actions.getFirst().run();
		}	
	}

	@Override
	public void dispose() {
		super.dispose();
		component = null;
		first = null;
		last = null;
	}

	@Override
	public CheckBox addCheckBox(Var<Boolean> var) {
		elf.swing.CheckBox cbox = new elf.swing.CheckBox(var);
		fields.add(cbox);
		return cbox;
	}

	@Override
	public void setButtonVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public <T> SubsetField<T> addSubsetField(CollectionVar<T> set) {
		elf.swing.SubsetField<T> field = new elf.swing.SubsetField<T>(set);
		fields.add(field);
		return field;
	}
	
}
