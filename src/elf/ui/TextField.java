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
package elf.ui;

import elf.ui.meta.SingleVar;
import elf.ui.meta.Var;

/**
 * Text field.
 * @author casse
 */
public interface TextField<T> extends Component, SingleVar.Listener<T>, Field {
	
	/**
	 * Get the current variable.
	 * @return	Current variable.
	 */
	Var<T> get();
	
	/**
	 * Change the current variable.
	 * @param var	New variable.
	 */
	void set(Var<T> var);
	
	/**
	 * Set the adapter.
	 * @param adapter	Set adapter.
	 */
	void setAdapter(StringAdapter<T> adapter);
	
	/**
	 * Set the field read-only.
	 * @param ro	Field read-only.
	 */
	void setReadOnly(boolean ro);
}
