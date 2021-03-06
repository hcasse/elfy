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
package elf.ui.meta;

import java.util.LinkedList;

import elf.ui.Icon;
//import elf.swing.IconManager;
import elf.ui.I18N;

/**
 * An action in the UI.
 * @author casse
 */
public abstract class Action extends AbstractEntity {
	public static final Action NULL = new Action() {
		@Override public void run() { }
	};
	public static final Action QUIT = new Action() {
		@Override public void run() { System.exit(0); }
		@Override public String getLabel() { return I18N.STD.t("Quit"); }
		@Override public Icon getIcon() { return Icon.QUIT; }
	};
	private LinkedList<Command> commands = new LinkedList<Command>();

	/**
	 * Run the action.
	 */
	public abstract void run();
	
	/**
	 * Test if the action is enabled.
	 * @return	True for enabled, false else.
	 */
	public boolean isEnabled() {
		return true;
	}
	
	/**
	 * Add a command.
	 * @param command	Added command.
	 */
	public void add(Command command) {
		commands.add(command);
		command.enable(isEnabled());
	}
	
	/**
	 * Remove a command from the action.
	 * @param command	Removed command.
	 */
	public void remove(Command command) {
		commands.remove(command);
	}
	
	/**
	 * Add a dependency to data.
	 * @param d
	 */
	public<T> void add(Var<T> d) {
		new DataDependency<T>(this, d);
	}
	
	/**
	 * Add a dependency on a listenable object.
	 * @param listenable	Listenable to be dependent on.
	 */
	public void add(Subject listenable) {
		new ListenableDependency(this, listenable);
	}
	
	/**
	 * Add a dependency to a collection of data.
	 * @param d	Added dependency.
	 */
	public<T> void add(CollectionVar<T> d) {
		new DataCollectionDependency<T>(this, d);
	}
	
	/**
	 * Add dependency on several listenables.
	 * @param listenables	Listenables to depend on.
	 */
	public void dependOn(Subject... listenables) {
		for(Subject listenable: listenables)
			new ListenableDependency(this, listenable);
	}
	
	/**
	 * Update the commands.
	 */
	public void update() {
		boolean enabled = isEnabled();
		for(Command command: commands)
			command.enable(enabled);		
	}
	
	/**
	 * Command generated by the action.
	 * @author casse
	 */
	public interface Command {
		
		/**
		 * Enable or disable the command.
		 * @param enabled	True for enabled, false else.
		 */
		void enable(boolean enabled);
		
	}
	
	/**
	 * Base class for implementing dependencies.
	 * @author casse
	 */
	public static abstract class Dependency {
		Action action;
		
		public Dependency(Action action) {
			this.action = action;
		}
		
		/**
		 * Update the commands.
		 */
		public void update() {
			action.update();
		}
		
	}
	
	/**
	 * Dependendency on a listenable.
	 * @author casse
	 *
	 */
	public static class ListenableDependency extends Dependency implements Subject.Observer {

		public ListenableDependency(Action action, Subject listenable) {
			super(action);
			listenable.add(this);
		}

	}
	
	/**
	 * Dependency on the validity of a data.
	 * @author casse
	 *
	 * @param <T>	Type of data.
	 */
	public static class DataDependency<T> extends Dependency implements Var.ChangeListener<T> {
		Var<T> data;
		
		public DataDependency(Action action, Var<T> data) {
			super(action);
			this.data = data;
			data.add(this);
		}

		@Override
		public void onChange(Var<T> data) {
			action.update();
		}

	};
	
	/**
	 * Dependency on a data collection.
	 * @author casse
	 *
	 * @param <T>	Type of data.
	 */
	public static class DataCollectionDependency<T> extends Dependency implements CollectionVar.Listener<T> {
		CollectionVar<T> coll;
		
		public DataCollectionDependency(Action action, CollectionVar<T> coll) {
			super(action);
			this.coll = coll;
			coll.addListener(this);
		}

		@Override
		public void onAdd(T item) {
			action.update();
		}

		@Override
		public void onRemove(T item) {
			action.update();
		}

		@Override
		public void onClear() {
			action.update();
		}

		@Override
		public void onChange() {
			action.update();
		}
		
	};
}
