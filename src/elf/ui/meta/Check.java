/*
 * Elfy library
 * Copyright (c) 2015 - Hugues Cassé <hugues.casse@laposte.net>
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
package elf.ui.meta;

import elf.ui.Component;
import elf.ui.Field;
import elf.ui.Monitor;

/**
 * A check implements predicate whose results
 * depends on a list of variable. According to the result of
 * the check method (that must be overload for use),
 * it will display the error message or not.
 * @author casse
 *
 */
public abstract class Check extends AbstractListenable implements Subject.Observer {
	private Component component;
	private Monitor monitor;
	private Field[] fields;
	private boolean state;
	private String message;

	/**
	 * Build a check on the given field or variable.
	 * @param component	Checked component.
	 * @param message	Message in case of check failure.
	 * @param fields	Fields and variable whose check depends to.
	 */
	public Check(Component component, String message, Field... fields) {
		this.component = component;
		this.fields = fields;
		this.message = message;
		state = checkAll();
		startListening();
	}

	/**
	 * Start this check listening its variables.
	 */
	public void startListening() {
		for(Field field: fields)
			field.getVar().add(this);
	}

	/**
	 * Start this check listening its variables.
	 */
	public void stopListening() {
		for(Field field: fields)
			field.getVar().remove(this);
	}

	/**
	 * Called to make the check.
	 * @return	True if check is successful, false else.
	 */
	protected abstract boolean check();

	/**
	 * Test if the check is successful or not.
	 * @return		True if successful, false else.
	 */
	public boolean isChecked() {
		return state;
	}

	/**
	 * Check fields and check together to determine
	 * the whole validity of the check.
	 */
	private boolean checkAll() {

		// check fields
		for(Field field: fields)
			if(!field.isValid())
				return false;

		// check the rule
		return check();
	}

	@Override
	public void update() {
		boolean new_state = checkAll();
		if(new_state != state) {
			state = new_state;

			// display check message
			if(monitor == null)
				monitor = component.getMonitor();
			if(!state)
				monitor.error(message);

			// update listener
			this.fireChange();

			// update fields
			for(Field field: fields)
				field.setValidity(state);
		}
	}

	/**
	 * Build a pseudo-field to box-in a single variable.
	 * @param var	Variable to box in.
	 * @return		Built pseudo-field.
	 */
	public static Field var(Var<?> var) {
		return new NullField(var);
	}

	/**
	 * Class allowing considering fields even with variables
	 * not associated with a field.
	 * @author casse
	 */
	public static class NullField implements Field {
		private Var<?> var;

		public NullField(Var<?> var) {
			this.var = var;
		}

		@Override
		public Entity getEntity() {
			return null;
		}

		@Override
		public boolean isReadOnly() {
			return false;
		}

		@Override
		public Var<?> getVar() {
			return var;
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public void setValidity(boolean validity) {
		}

		@Override
		public void gainFocus() {
		}

	}
}
