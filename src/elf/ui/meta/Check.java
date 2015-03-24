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

import elf.ui.Monitor;

/**
 * A check implements predicate whose results
 * depends on a list of variable. According to the result of
 * the check method (that must be overload for use), 
 * it will display the error message or not.
 * @author casse
 *
 */
public abstract class Check extends AbstractListenable implements Listenable.Listener {
	private Monitor monitor;
	private Listenable[] vars;
	private boolean state;
	private String message;

	/**
	 * Build a check.
	 * @param monitor	Monitor to display message to.
	 * @param message	Message in case of check failure.
	 * @param vars		Variable whose check depends to.
	 */
	public Check(Monitor monitor, String message, Listenable... vars) {
		this.monitor = monitor;
		this.vars = vars;
		this.message = message;
		state = check();
		startListening();
	}
	
	/**
	 * Start this check listening its variables.
	 */
	public void startListening() {
		for(Listenable var: vars)
			var.add(this);
	}
	
	/**
	 * Start this check listening its variables.
	 */
	public void stopListening() {
		for(Listenable var: vars)
			var.remove(this);		
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
	
	@Override
	public void update(Listenable obs) {
		boolean new_state = check();
		if(new_state != state) {
			state = new_state;
			if(state)
				monitor.clear();
			else
				monitor.error(message);
			fireListenableChange();
		}
	}
	
}
