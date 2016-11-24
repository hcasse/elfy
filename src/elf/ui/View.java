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

import java.util.Collection;

import elf.ui.meta.Action;
import elf.ui.meta.Entity;

/**
 * A view is a facility letting the user interact graphically with the application.
 * Depending on the underlying OS and device, it may be a window in a GUI
 * or a page on an handled device.
 * @author casse
 */
public interface View extends Container {
	
	/**
	 * Open the view, i.e., make the view visible.
	 */
	public void show();
	
	/**
	 * Close the view.
	 */
	public void hide();
	
	/**
	 * Action to launch on close command.
	 * @param action	Close action.
	 */
	public void setCloseAction(Action action);
	
	/**
	 * Get the monitor associated with the view.
	 * @return	View monitor.
	 */
	public Monitor getMonitor();
	
	/**
	 * Open a validation dialog.
	 * @param message	Message of the dialog.
	 * @param title		Title of the dialog.
	 * @return			True if the user validate it, false else.
	 */
	public boolean showConfirmDialog(String message, String title);
	
	/**
	 * Prepare a dialog that displays a list of values and let the user
	 * choice one value (or cancel).
	 * @param message		Message for the user.
	 * @param title			Dialog title.
	 * @param collection	Collection of values.
	 * @return				Built selection dialog.
	 */
	public <T> SelectionDialog<T> makeSelectionDialog(String message, String title, Collection<T> collection);
	
	/**
	 * Build a modal dialog.
	 * @param entity	Entity to get label and icon.
	 * @return			Built dialog.
	 */
	public GenericDialog makeDialog(Entity entity);
	
	/**
	 * Make an action with validation: a dialog is open to let
	 * the user validate the action. It is used for undoeable action
	 * like final deletion.
	 * @param action	Action to validate.
	 * @param message	Message to display for the action (if null, it will be replaced by a standard message).
	 * @return			Meta-action supporting validation when activated. 
	 */
	public Action makeValidatedAction(Action action, Entity message);
	
}
