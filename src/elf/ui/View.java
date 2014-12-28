package elf.ui;

import elf.ui.meta.Action;

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
}
