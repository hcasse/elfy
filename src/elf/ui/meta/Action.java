package elf.ui.meta;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Automatization of action management.
 * @author H. Cassé <casse@irit.fr>
 */
public abstract class Action implements ActionListener {
	
	/**
	 * Get the label of the action.
	 * @return	Action label.
	 */
	public abstract String getLabel();
	
	/**
	 * Get the help string for the action.
	 * @return	Help string.
	 */
	public String getHelp() {
		return null;
	}
	
	/**
	 * Get the mnemonic of the action.
	 * @return	Mnemonic (or 0 if no mnemonic).
	 */
	public int getMnemonic() { return 0; }
	
	/**
	 * Get the control-character of the action.
	 * @return	Control character.
	 */
	public int getControl() { return 0; }

	/**
	 * Test if the menu is enabled or not at initialization.
	 * @return	True if is enabled, false else.
	 */
	public boolean isEnabled() { return true; }

	/**
	 * Get the dependency list of the current action.
	 * @return	Dependency list.
	 */
	public Dependency[] getDependencies() {
		return new Dependency[0];
	}
	
	/**
	 * Run the action.
	 */
	public abstract void run();
	
	/**
	 * Build a menu item for the given action.
	 * @return	Made menu item.
	 */
	public JMenuItem makeMenuItem() {
		return makeMenuItem(0);
	}
	
	/**
	 * Prepare the component.
	 * @param component
	 */
	private void prepare(AbstractButton component) {
		if(getMnemonic() != 0)
			component.setMnemonic(this.getMnemonic());
		component.setEnabled(isEnabled());
		component.addActionListener(this);
		if(getHelp() != null)
			component.setToolTipText(getHelp());
	}
	
	/**
	 * Prepare a menu item.
	 * @param item	Menu item to prepare.
	 */
	private void prepareMenuItem(JMenuItem item) {
		prepare(item);
		if(getControl() != 0)
			item.setAccelerator(KeyStroke.getKeyStroke(getControl(), Event.CTRL_MASK));
		Dependency[] deps = this.getDependencies();
		for(int i = 0; i < deps.length; i++)
			deps[i].dependToMenuItem(item);		
	}
	
	/**
	 * Build a menu item (no action).
	 * @return	Built menu item.
	 */
	public JMenu makeMenu() {
		JMenu menu = new JMenu(getLabel());
		prepareMenuItem(menu);
		return menu;
	}
	
	/**
	 * Build a menu item for the given action.
	 * @param flags	Flags for activation some option (see WITH_ENABLER).
	 * @return	Made menu item.
	 */
	public JMenuItem makeMenuItem(int flags) {
		JMenuItem item = new JMenuItem(getLabel());
		prepareMenuItem(item);
		return item;
	}

	// implementation
	public void actionPerformed(ActionEvent e) {
		run();
	}
	
	/**
	 * Dependency of the current action with another SWING component.
	 */
	public interface Dependency {
		
		/**
		 * Called to build a dependency to a menu item.
		 * @param item	Dependent menu item.
		 */
		public void dependToMenuItem(JMenuItem item);
		
	}
	
	/**
	 * Dependency on a change event.
	 */
	public class ChangedDependency implements Dependency {
		JComponent target;
		public ChangedDependency(JComponent target) { this.target = target; }
		public void dependToMenuItem(JMenuItem item) {
			try {
				Method meth = target.getClass().getMethod("addChangeListener", ChangeListener.class);
				meth.invoke(target, new Object[] { new MenuItemListener<Integer>(item) });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Dependency on Data change.
	 * @param <T>	Type of data.
	 */
	public class DataDependency<T> implements Dependency {
		Data<T> data;
		
		public DataDependency(Data<T> data) { this.data = data; }
		public void dependToMenuItem(JMenuItem item) {
			data.addListener(new MenuItemListener<T>(item));
		}
	}

	/**
	 * Listener handling menu items.
	 */
	private class MenuItemListener<T> implements ChangeListener, Data.Listener<T> {
		JMenuItem item;
		public MenuItemListener(JMenuItem item) { this.item = item; } 
		public void stateChanged(ChangeEvent e) { item.setEnabled(isEnabled()); }
		public void change(Data<T> data) { item.setEnabled(isEnabled()); }
	}
	
	public static class Label extends Action {
		String label;
		
		public Label(String label) {
			this.label = label;
		}
		
		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public void run() {
		}
		
	}
}
