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

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JComponent;

import elf.ui.I18N;
import elf.ui.meta.Action;

/**
 * Swing implementation of page pane.
 * @author casse
 */
public class PagePane extends Component implements elf.ui.PagePane {
	private final LinkedList<Page> stack = new LinkedList<Page>();
	private Page page;
	private javax.swing.Box pane;
	private Action back_action;
	private UI ui;
	
	@Override
	public Page addPage() {
		return new Page();
	}

	@Override
	public void set(elf.ui.PagePane.Page set_page) {
		Page page = (Page)set_page;
		if(pane != null) {
			if(this.page != null)
				this.page.uninstall(pane);
			page.install(ui, pane);
			pane.revalidate();
			pane.repaint();
		}
		this.page = page;
	}

	@Override
	public void push(elf.ui.PagePane.Page set_page) {
		Page page = (Page)set_page;
		if(this.page != null) {
			stack.push(this.page);
			if(back_action != null)
				back_action.update();
		}
		set(page);
	}

	@Override
	public void pop() {
		Page page = stack.pop();
		set(page);
		if(back_action != null)
			back_action.update();
	}

	@Override
	public JComponent getComponent(UI ui) {
		if(pane == null) {
			this.ui = ui;
			pane = javax.swing.Box.createHorizontalBox();
			pane.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			pane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			if(page != null)
				page.install(ui, pane);
		}
		return pane;
	}

	/**
	 * Swing implementation of a page.
	 * @author casse
	 */
	private static class Page extends elf.swing.Container implements elf.ui.PagePane.Page {
		Listener listener = NULL;
		
		public void install(UI ui, javax.swing.Box panel) {
			listener.onShow();
			for(Component component: getComponents())
				panel.add(component.getComponent(ui));
		}
		
		@Override
		public JComponent getComponent(UI ui) {
			return null;
		}
		
		public void uninstall(javax.swing.Box panel) {
			listener.onHide();
			panel.removeAll();
		}

		@Override
		public void setListener(Listener listener) {
			this.listener = listener;
		}
		
	}

	@Override
	public Action getBackAction() {
		if(back_action == null)
			back_action = new Action() {
				@Override public void run() { pop(); }
				@Override public String getLabel() { return I18N.STD.t("Back"); }
				@Override public elf.ui.Icon getIcon() { return elf.ui.Icon.BACK; }
				@Override public boolean isEnabled() { return !stack.isEmpty(); }
			};
		return back_action;
	}

}
