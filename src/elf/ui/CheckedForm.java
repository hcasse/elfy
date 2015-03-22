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
package elf.ui;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import elf.ui.meta.AbstractListenable;
import elf.ui.meta.Action;
import elf.ui.meta.Listenable;
import elf.ui.meta.Var;

/**
 * Represents a collection of consistent data
 * that may be edited from a form.  
 * @author casse
 */
public class CheckedForm extends AbstractListenable implements Listenable.Listener {
	private Form form;
	private LinkedList<Var<?>> vars = new LinkedList<Var<?>>();
	private Vector<CheckedAction> checked_actions = new Vector<CheckedAction>();
	private TreeMap<Listenable, Field> map = new TreeMap<Listenable, Field>(); 
	private boolean valid = true;
	
	public CheckedForm(Form form, Var<?>... args) {
		
		// initialize the state
		this.form = form;
		for(Var<?> arg: args) 
			this.vars.add(arg);
		
		// build the field and link record to variables
		for(Var<?> var: vars) {
			Field field = form.addField(var);
			var.add(this);
			map.put(var, field);
		}
	}
	
	/**
	 * Add an action that is drived by the current record.
	 * @param action	Added action.
	 */
	public void addCheckedAction(CheckedAction action) {
		checked_actions.add(action);
		form.addAction(action);
		action.add(this);
	}

	/**
	 * This method is called each time a variable is modified
	 * to check consistency of values. If check return false,
	 * the form cannot be applied.
	 * 
	 * This method must be overridden to provide specific consistency
	 * rules. As a default, returns true.
	 * 
	 * @return	True if the current values are consistent, false else.
	 */
	protected boolean check() {
		return true;
	}
	
	/**
	 * Apply the given check.
	 * @param value		Value of the check (true for success, false else).
	 * @param message	Message to display in case of failed check.
	 * @param vars		Variable involved in the failed check.
	 * @return			Value itself (for composition purpose).
	 */
	protected boolean check(boolean value, String message, Var<?>... vars) {
		
		// reset all fields
		for(Field field: map.values())
			field.setValidity(true);

		// display message
		if(value)
			form.getMonitor().clear();
		else {
			form.getMonitor().error(message);
			for(Var<?> var: vars)
				map.get(var).setValidity(false);
		}
		
		// return value
		return value;
	}

	@Override
	public void update(Listenable obs) {
		
		// need for update?
		boolean old_valid = valid;
		valid = check();
		if(valid == old_valid)
			return;
		
		// update actions
		fireListenableChange();
	}
	
	/**
	 * Action that depends on a record.
	 * @author casse
	 */
	public static abstract class CheckedAction extends Action {
		private CheckedForm record;

		@Override
		public boolean isEnabled() {
			return record.valid;
		}
		
	}
}
