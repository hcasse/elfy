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

import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.EnumVar;
import elf.ui.meta.Factory;
import elf.ui.meta.Var;

/**
 * Form let edit several fields together. The display
 * is arranged according to the selected style.
 * @author casse
 */
public interface Form extends Component {
	public static final int
		STYLE_TWO_COLUMN = 0,	/** Left column for labels, right one for fields */
		STYLE_VERTICAL = 1;		/** Vertical sequence of (label, field) */
	public static final int
		ENTER_IGNORE = 0,				/** Enter key is processed as other keys. */
		ENTER_SUBMIT = 1,				/** Enter key causes submission of the form. */
		ENTER_NEXT = 2,					/** Enter key causes focus to next field. */
		ENTER_NEXT_AND_SUBMIT = 3;		/** Enter key causes focus to next field and submission on last field. */

	/**
	 * Set the style of the form.
	 * @param style		Set style.
	 */
	void setStyle(int style);
	
	/**
	 * Add a text field.
	 * @param var	Used variable.
	 * @return		Created text field.
	 */
	<T> TextField<T> addTextField(Var<T> var);
	
	/**
	 * Build a checkbox.
	 * @param var	Variable of the checkbox.
	 * @return		Added checkbox.
	 */
	CheckBox addCheckBox(Var<Boolean> var);

	/**
	 * Add a subset field.
	 * @param set	Initial set.
	 * @return		Created subset field.
	 */
	<T> SubsetField<T> addSubsetField(CollectionVar<T> set);
	
	/**
	 * Add an enumeration field.
	 * @param var	Field variable.
	 * @return		Added field.
	 */
	<T> EnumField<T> addEnumField(EnumVar<T> var);
	
	/**
	 * Add non-nominal action.
	 * @param action	Added action.
	 */
	void addAction(Action action);
	
	/**
	 * Add an action that is considered as the main action, that is,
	 * it will be activated when the <enter> key is pressed.
	 * @param action	Added action.
	 */
	void addMainAction(Action action);
	
	/**
	 * Set the style of the buttons.
	 * @param style		Button style (one of Button.STYLE_xxx).
	 */
	void setButtonStyle(int style);
	
	/**
	 * Set the button alignment.
	 * @param alignment	One of Component.LEFT, Component.CENTER, Component.RIGHT or Component.SPREAD.
	 */
	void setButtonAlignment(int alignment);
	
	/**
	 * Set the mode of enter key.
	 * @param mode	Enter key mode (one of ENTER_xxx).
	 */
	void setEnterMode(int mode);
	
	/**
	 * Make the button visible or not.
	 * @param visible	True for visible, false for invisible.
	 */
	void setButtonVisible(boolean visible);
	
	/**
	 * Get the current factory.
	 * @return	Form factory.
	 */
	Factory getFactory();
	
	/**
	 * Set the form factory.
	 * @param factory	New factory.
	 */
	void setFactory(Factory factory);
	
	/**
	 * Add a field for the given variable.
	 * @param var				Variable to create variable with.
	 * @return					Created field.
	 * @throws NoFieldError		Thrown when no field exists for the given variable.
	 */
	Field addField(Var<?> var) throws NoFieldError;
	
	public static class NoFieldError extends Error {
		private static final long serialVersionUID = 7405394642587268219L;

		public NoFieldError(String msg) {
			super(msg);
		}
		
	}
}
