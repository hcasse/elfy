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
package elf.swing;

import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import elf.ui.I18N;
import elf.ui.Icon;
import elf.ui.IconManager;
import elf.ui.meta.Action;

/**
 * Swing implementation of page pane.
 * @author casse
 */
public class PagePane extends Component implements elf.ui.PagePane {
	private final LinkedList<Page> stack = new LinkedList<Page>();
	private Page page;
	private JPanel pane;
	private Action back_action;
	
	@Override
	public Page addPage() {
		return new Page();
	}

	@Override
	public void set(elf.ui.PagePane.Page set_page) {
		Page page = (Page)set_page;
		if(pane == null)
			return;
		if(this.page != null)
			page.uninstall(pane);
		this.page = page;
		page.install(pane);
		pane.revalidate();
		pane.repaint();
	}

	@Override
	public void push(elf.ui.PagePane.Page set_page) {
		Page page = (Page)set_page;
		if(this.page != null) {
			stack.push(this.page);
			//back.update();
		}
		set(page);
	}

	@Override
	public void pop() {
		Page page = stack.pop();
		set(page);
		//back.update();
	}

	@Override
	public JComponent getComponent() {
		if(pane == null) {
			pane = new JPanel();
			page.install(pane);
		}
		return pane;
	}

	/**
	 * Swing implementation of a page.
	 * @author casse
	 */
	private static class Page extends elf.swing.Container implements elf.ui.PagePane.Page {

		public void install(JPanel panel) {
			for(Component component: getComponents())
				panel.add(component.getComponent());
		}
		
		@Override
		public JComponent getComponent() {
			return null;
		}
		
		public void uninstall(JPanel panel) {
			for(Component component: getComponents())
				panel.remove(component.getComponent());			
		}
		
	}

	@Override
	public Action getBackAction() {
		if(back_action == null)
			back_action = new Action() {
				@Override public void run() { pop(); }
				@Override public String getLabel() { return I18N.STD.t("Back"); }
				@Override public Icon getIcon() { return IconManager.STD.get(IconManager.ICON_BACK); }
				@Override public boolean isEnabled() { return !stack.isEmpty(); }
			};
		return back_action;
	}

}
