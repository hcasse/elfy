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
package elf.tool;

import elf.app.Application;
import elf.data.Version;
import elf.os.DirPath;
import elf.os.OS;
import elf.ui.ActionBar;
import elf.ui.Box;
import elf.ui.Form;
import elf.ui.StatusBar;
import elf.ui.TitleBar;
import elf.ui.View;
import elf.ui.meta.Action;
import elf.ui.meta.Var;

/**
 * Tool to help to manage translations.
 * @author casse
 */
public class TransHelp extends Application {

	// model
	private Var<DirPath> project = new Var<DirPath>(DirPath.getWorkingDirectory()) {
		@Override public String getLabel() { return "Project"; }
		@Override public String getHelp() { return "Select the top-level directory of the project to process."; }
	};
	private Var<String> lang = new Var<String>("") {
		@Override public String getLabel() { return "Language"; }
		@Override public String getHelp() { return "Select the language to process."; }
	};

	private Action process = new Action() {
		@Override public void run() { }
		@Override public String getLabel() { return "Process"; }
		@Override public String getHelp() { return "Launch the analysis of translation of the project."; }
	};

	// UI
	StatusBar status;

	public TransHelp() {
		super("elftrans", new Version(1, 0, 0));
	}

	public static void main(String[] args) {
		TransHelp app = new TransHelp();
		app.run(args);
	}

	@Override
	protected void proceed() {
		View view = OS.os.getUI().makeView(this);
		Box box = view.addBox(Box.VERTICAL);
		TitleBar bar = box.addTitleBar();
		bar.setTitle("Select the project to process:");
		bar.addMenu(process);
		bar.addMenu(this.getAboutAction(view));
		bar.addMenu(Action.QUIT);
		Form form = box.addForm();
		form.addField(project);
		form.addField(lang);
		ActionBar abar = box.addActionBar();
		abar.add(process);
		abar.add(Action.QUIT);
		abar.setAlignment(ActionBar.RIGHT);
		status = box.addStatusBar();
		status.info("Welcome in " + this.getLabel());
		view.show();
	}

}
