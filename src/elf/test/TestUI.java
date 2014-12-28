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
package elf.test;

import java.util.Vector;

import elf.os.OS;
import elf.ui.ActionBar;
import elf.ui.Button;
import elf.ui.Component;
import elf.ui.Container;
import elf.ui.TitleBar;
import elf.ui.UI;
import elf.ui.View;
import elf.ui.meta.AbstractEntity;
import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;

/**
 * Test for UI.
 * @author casse
 */
public class TestUI {

	public static void main(String[] args) {
		UI ui = OS.os.getUI();
		View view = ui.makeView(new AbstractEntity() {
			@Override public String getLabel() { return "TestUI"; }
		});
		view.setCloseAction(Action.QUIT);
		Container vbox = view.addBox(Component.VERTICAL);
		
		TitleBar tbar = vbox.addTitleBar();
		tbar.addLeft(Action.QUIT);
		tbar.addMenu(Action.QUIT);
		tbar.setTitle("coucou");
		
		ActionBar abar = vbox.addActionBar();
		abar.add(Action.QUIT);
		abar.add(Action.QUIT);
		abar.setAlignment(Component.SPREAD);
		
		vbox.addTextField("coucou");
		
		vbox.addList(new CollectionVar<Integer>(new Vector<Integer>()));
		
		vbox.addButton(Action.QUIT, Button.STYLE_TEXT);
		view.show();
	}

}
