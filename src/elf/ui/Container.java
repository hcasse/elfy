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
import elf.ui.meta.Var;

/**
 * A component that contains other components.
 * @author casse
 */
public interface Container extends Component {
	
	/**
	 * Add a button to the container.
	 * @param action	Action of the button.
	 * @param style		Button style (one of Button.TEXT, Button.ICON_TEXT, Button.ICON and Button.TOOL).
	 */
	Button addButton(Action action, int style);
	
	/**
	 * Add a box with given direction.
	 * @param direction		Direction of box (either HORIZONTAL or VERTICAL).
	 * @return
	 */
	Container addBox(int direction);
	
	/**
	 * Add a title bar.
	 * @return	Added title bar.
	 */
	TitleBar addTitleBar();
	
	/**
	 * Add an action bar.
	 * @return	Added action bar.
	 */
	ActionBar addActionBar();
	
	/**
	 * Add a text field editing the given type.
	 * @param init	Initial value.
	 * @return		Created text field.
	 */
	<T> TextField<T> addTextField(T init);
	
	/**
	 * Add a text field editing the given type.
	 * @param var	Variable to edit.
	 * @return		Created text field.
	 */
	<T> TextField<T> addTextField(Var<T> var);
	
	/**
	 * Add a list widget.
	 * @return	Added widget.
	 */
	<T> List<T> addList();
	
	/**
	 * Add a list widget.
	 * @param collection	Collection to display.
	 * @return				Added widget.
	 */
	<T> List<T> addList(CollectionVar<T> collection);
	
	/**
	 * Add a page pane.
	 * @return	Built page pane.
	 */
	PagePane addPagePane();
	
	/**
	 * Add a split pane.
	 * @param axis		One of Component.HORIZONTAL or Component.VERTICAL.
	 * @return			Split pane.
	 */
	SplitPane addSplitPane(int axis);
	
	/**
	 * Add a form.
	 * @param style		Style of form.
	 * @param action	Main action (called at validation).
	 * @return			Added form.
	 */
	Form addForm(int style, Action action);
	
	/**
	 * Remove a component from the container.
	 * @param component		Removed component.
	 */
	void remove(Component component);
	
}
