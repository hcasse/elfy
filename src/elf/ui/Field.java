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

import elf.ui.meta.Entity;
import elf.ui.meta.Var;

/**
 * A field is a widget that may be involved in a form.
 * Mainly, it references an entity (whose label, icon, help may be extracted).
 * @author casse
 */
public interface Field {

	/**
	 * Get the variable.
	 * @return	Variable.
	 */
	Entity getEntity();

	/**
	 * Test if the field is read-only.
	 * @return	True if read-only, false else.
	 */
	boolean isReadOnly();

	/**
	 * Get the variable edited by the given field.
	 * @return	Field variable.
	 */
	Var<?> getVar();

	/**
	 * Test if the field contains valid value.
	 * @return		True if content is valid, false else.
	 */
	boolean isValid();

	/**
	 * Set the validity of the field.
	 * If set to false, display is highlighted to show error.
	 * @param validity	True for valid, false for invalid.
	 */
	void setValidity(boolean validity);

	/**
	 * Set the focus on the current field.
	 */
	void gainFocus();
}
