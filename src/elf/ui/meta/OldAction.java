package elf.ui.meta;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import elf.swing.Icon;

/**
 * Automatization of action management.
 * @author H. Cassé <casse@irit.fr>
 */
public abstract class OldAction implements ActionListener {
	
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
	 * Get an icon for the action.
	 * @return	Matching icon.
	 */
	public Icon getIcon() {
		return null;
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
	 * Make a button for the given action.
	 * @return	Button for the action.
	 */
	public JButton makeButton() {
		JButton b = new JButton();
		String l = getLabel();
		if(l != null)
			b.setText(l);
		Icon i = getIcon();
		if(i != null)
			b.setIcon(i.get(Icon.NORMAL, Icon.TEXTUAL));
		b.addActionListener(this);
		return b;
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
		
		/**
		 * Make a dependency to a button.
		 * @param button	Button to depend on.
		 */
		public void dependToButton(JButton button);
		
	}
	
	/**
	 * Dependency on a change event.
	 */
	/*public class ChangedDependency implements Dependency {
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
	
		@Override
		public void dependToButton(JButton button) {
			// TODO Auto-generated method stub
			
		}
	}*/
	
	/**
	 * Dependency on Data change.
	 * @param <T>	Type of data.
	 */
	public class DataDependency<T> implements Dependency {
		Var<T> data;
		
		public DataDependency(Var<T> data) { this.data = data; }
		
		public void dependToMenuItem(JMenuItem item) {
			data.addListener(new MenuItemListener<T>(item));
		}
		
		@Override
		public void dependToButton(JButton button) {
			data.addListener(new ButtonListener<T>(button));
		}
	}

	/**
	 * Listener handling menu items.
	 */
	private class MenuItemListener<T> implements /*ChangeListener,*/ Var.Listener<T> {
		JMenuItem item;
		public MenuItemListener(JMenuItem item) { this.item = item; } 
		//public void stateChanged(ChangeEvent e) { item.setEnabled(isEnabled()); }
		@Override public void change(Var<T> data) { item.setEnabled(isEnabled()); }
	}
	
	/**
	 * Listener for a button.
	 * @author casse
	 * @param <T>
	 */
	private class ButtonListener<T> implements Var.Listener<T> {
		JButton button;
		public ButtonListener(JButton button) { this.button = button; }
		@Override public void change(Var<T> data) { button.setEnabled(isEnabled()); }
	}
	
	public static class Label extends OldAction {
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
