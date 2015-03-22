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

import java.util.LinkedList;


/**
 * Represents an action that is guarded by a list of checks.
 * @author casse
 */
public abstract class CheckedAction extends Action {
	private LinkedList<Check> checks = new LinkedList<Check>(); 
	
	/**
	 * Add a check to drive this action.
	 * @param check		Added check.
	 */
	public void add(Check check) {
		add((Listenable)check);
		checks.add(check);
	}

	@Override
	public boolean isEnabled() {
		for(Check check: checks)
			if(!check.isChecked())
				return false;
		return true;
	}
	
}
