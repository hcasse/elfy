package elf.ui.meta;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * A factory for different GUI items.
 * @author casse
 *
 */
public class Factory {

	private Factory() {
		
	}
	
	/**
	 * Build a menu bar.
	 * @param actions	Action in the bar (each action of the sub-array give
	 * 					name of the menu, a null represents a bar).
	 * @return			Built menu bar.
	 */
	public static JMenuBar makeMenuBar(OldAction[][] actions) {
		JMenuBar bar = new JMenuBar();
		for(int i = 0; i < actions.length; i++) {
			JMenu menu = actions[i][0].makeMenu();
			bar.add(menu);
			for(int j = 1; j < actions[i].length; j++) {
				if(actions[i][j] == null)
					menu.addSeparator();
				else
					menu.add(actions[i][j].makeMenuItem());
			}
		}
		return bar;
	}
}
