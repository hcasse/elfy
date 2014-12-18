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

public abstract class Action {
	private LinkedList<Command> commands = new LinkedList<Command>();
	private LinkedList<Dependency> deps = new LinkedList<Dependency>();

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
	 * Get an icon for the action.
	 * @return	Matching icon.
	 */
	public Icon getIcon() { return null; }
	
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
	 * Add a dependency.
	 * @param dep	Added dependency.
	 */
	public void add(Dependency dep) {
		deps.add(dep);
	}
	
	/**
	 * Add a dependency to data.
	 * @param d
	 */
	public<T> void add(Data<T> d) {
		add(new DataDependency<T>(this, d));
	}
	
	/**
	 * Add a dependency to a collection of data.
	 * @param d	Added dependency.
	 */
	public<T> void add(DataCollection<T> d) {
		add(new DataCollectionDependency<T>(this, d));
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
		protected void update() {
			action.update();
		}
		
	}
	
	/**
	 * Dependency on the validity of a data.
	 * @author casse
	 *
	 * @param <T>	Type of data.
	 */
	public static class DataDependency<T> extends Dependency implements Data.Listener<T> {
		Data<T> data;
		
		public DataDependency(Action action, Data<T> data) {
			super(action);
			this.data = data;
		}

		@Override
		public void change(Data<T> data) {
			action.update();
		}

	};
	
	/**
	 * Dependency on a data collection.
	 * @author casse
	 *
	 * @param <T>	Type of data.
	 */
	public static class DataCollectionDependency<T> extends Dependency implements DataCollection.Listener<T> {
		DataCollection<T> coll;
		
		public DataCollectionDependency(Action action, DataCollection<T> coll) {
			super(action);
			this.coll = coll;
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
		
	};
}
