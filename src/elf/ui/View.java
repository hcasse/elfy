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
