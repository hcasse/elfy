/*
 * Elfy library
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

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JDialog;

import elf.ui.I18N;
import elf.ui.Icon;
import elf.ui.Monitor;
import elf.ui.SelectionDialog;
import elf.ui.meta.Action;
import elf.ui.meta.ActionShield;
import elf.ui.meta.Entity;

/**
 * Dialog view.
 * @author casse
 */
public class Dialog extends Container implements elf.ui.GenericDialog {
	private Entity entity;
	private View parent;
	private JDialog dialog;
	private boolean result;
	private ActionBar tb = new ActionBar();
	
	public Dialog(View parent, Entity entity) {
		this.parent = parent;
		this.entity = entity;
		tb.setAlignment(RIGHT);
		tb.setStyle(Button.STYLE_ICON_TEXT);
		tb.setAxis(HORIZONTAL);
	}
	
	@Override
	public JComponent getComponent(View view) {
		return null;
	}

	@Override
	public void show() {
		if(dialog == null) {
			dialog = new JDialog(parent.getFrame(), entity.getLabel(), true);
			javax.swing.Box box = javax.swing.Box.createVerticalBox();
			dialog.add(box);
			for(Component component: getComponents())
				box.add(component.getComponent(parent));
			box.add(tb.getComponent(parent));
		}
		dialog.pack();
		dialog.setLocationRelativeTo(parent.getFrame());
		dialog.setVisible(true);
	}

	@Override
	public void hide() {
		if(dialog != null)
			dialog.setVisible(false);
	}

	@Override
	public void setCloseAction(Action action) {
	}

	@Override
	public Monitor getMonitor() {
		return parent.getMonitor();
	}

	@Override
	public boolean showConfirmDialog(String message, String title) {
		return parent.showConfirmDialog(message, title);
	}

	@Override
	public <T> SelectionDialog<T> makeSelectionDialog(String message, String title, Collection<T> collection) {
		return parent.makeSelectionDialog(message, title, collection);
	}

	@Override
	public elf.ui.GenericDialog makeDialog(Entity entity) {
		return parent.makeDialog(entity);
	}

	@Override
	public void dispose() {
		super.dispose();
		dialog = null;
	}

	@Override
	public Action add(Action action) {
		Action faction = new ActionShield(action) {
			@Override public void run() { result = true; super.run(); dialog.setVisible(false); }
		};
		tb.add(faction);
		return faction;
	}

	@Override
	public void addOk() {
		tb.add(new Action() {
			@Override public void run() { result = true; dialog.setVisible(false); }
			@Override public String getLabel() { return I18N.STD.t("Ok"); }
			@Override public String getHelp() { return I18N.STD.t("Close and validate the dialog action"); }
			@Override public Icon getIcon() { return Icon.OK; }
		});
	}
 
	@Override
	public void addCancel() {
		tb.add(new Action() {
			@Override public void run() { result = false; dialog.setVisible(false); }
			@Override public String getLabel() { return I18N.STD.t("Cancel"); }
			@Override public String getHelp() { return I18N.STD.t("Close but cancel the dialog action"); }
			@Override public Icon getIcon() { return Icon.CANCEL; }
		});
	}

	@Override
	public boolean run() {
		result = false;
		show();
		return result;
	}

	@Override
	public View getView() {
		return parent;
	}

	@Override
	public Action makeValidatedAction(Action action, Entity message) {
		return parent.makeValidatedAction(action, message);
	}

}
