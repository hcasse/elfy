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

import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import elf.ui.I18N;
import elf.ui.meta.Action;
import elf.ui.meta.Activable;
import elf.ui.meta.Var;
import elf.ui.meta.Var.ChangeListener;

/**
 * Swing implementation of page pane.
 * @author casse
 */
public class PagePane extends Parent implements elf.ui.PagePane {
	private final LinkedList<Page> stack = new LinkedList<Page>();
	private Page page;
	private javax.swing.JPanel pane;
	private Action back_action;
	private View view;
	
	@Override
	public Page addPage() {
		Page page = new Page();
		this.addChild(page);
		return page;
	}

	@Override
	public void set(elf.ui.PagePane.Page set_page) {
		Page page = (Page)set_page;
		if(pane != null) {
			if(this.page != null)
				this.page.uninstall(pane);
			page.install(view, pane);
			pane.validate();
			pane.repaint();
			page.takeFocus();
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
	public JComponent getComponent(View view) {
		if(pane == null) {
			this.view = view;
			//pane = javax.swing.Box.createHorizontalBox();
			pane = new JPanel();
			pane.setLayout(new GridLayout(1, 1));
			//pane.setPreferredSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			//pane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
			if(page != null)
				page.install(view, pane);
		}
		return pane;
	}

	/**
	 * Swing implementation of a page.
	 * @author casse
	 */
	private static class Page extends elf.swing.Container implements elf.ui.PagePane.Page {
		Listener listener = NULL;
		LinkedList<Activable> activables = new LinkedList<Activable>();
		boolean shown = false;
		
		public void install(View view, JPanel panel) {
			shown = true;
			listener.onShow();
			for(Activable activable: activables)
				activable.enable();
			for(Component component: getComponents())
				panel.add(component.getComponent(view));
			panel.doLayout();
		}
		
		@Override
		public JComponent getComponent(View view) {
			return null;
		}
		
		public void uninstall(JPanel panel) {
			shown = false;
			for(Activable activable: activables)
				activable.disable();
			listener.onHide();
			panel.removeAll();
		}

		@Override
		public void setListener(Listener listener) {
			this.listener = listener;
		}

		@Override
		public void add(Activable activable) {
			activables.add(activable);
			if(shown)
				activable.enable();
			else
				activable.disable();
		}

		@Override
		public void remove(Activable activable) {
			activables.remove(activable);
		}

		@Override
		public <T> void listenExtern(Var<T> var, ChangeListener<T> listener) {
			Var.DelayedListener<T> dlist = new Var.DelayedListener<T>(listener);
			var.add(dlist);
			add(dlist);
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

	@Override
	public void back() {
		pop();
	}
	
}
