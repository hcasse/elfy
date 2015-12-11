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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import elf.os.Path;
import elf.ui.meta.Entity;
import elf.ui.meta.Var;

public class PathField<T extends Path> extends Field {
	private static Icon path_icon;
	private Var<T> path;
	private javax.swing.Box box;
	private JTextField field;
	private JButton button;

	public PathField(Var<T> path) {
		this.path = path;
	}

	@Override
	public Entity getEntity() {
		return path;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setValidity(boolean validity) {
		// TODO Auto-generated method stub

	}

	@Override
	public JComponent getComponent(View view) {
		if(box == null) {
			box = new javax.swing.Box(javax.swing.BoxLayout.X_AXIS);
			field = new JTextField(path.get().toString());
			field.setColumns(10);
			box.add(field);
			if(path_icon == null)
				path_icon = view.getIcon(elf.ui.Icon.STD.get("path"));
			button = new JButton(path_icon.get(Icon.NORMAL));
			box.add(button);
		}
		return box;
	}

	@Override
	public Var<?> getVar() {
		return path;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
