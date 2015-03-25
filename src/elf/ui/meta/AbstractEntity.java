/*
 * ElfCore library
 * Copyright (c) 2012 - Hugues Cassé <hugues.casse@laposte.net>
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

/**
 * Default implementation of the entity.
 * @author casse
 */
public class AbstractEntity implements Entity {
	private LinkedList<EntityListener> listeners = new LinkedList<EntityListener>();

	@Override
	public String getLabel() {
		return "<unnamed>";
	}
	
	@Override
	public String getHelp() {
		return null;
	}
	
	@Override
	public int getMnemonic() {
		return 0;
	}
	
	@Override
	public int getControl() {
		return 0;
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public void add(EntityListener listener) {
		listeners.add(listener);
	}

	@Override
	public void remove(EntityListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void fireEntityChange() {
		for(EntityListener listener: listeners)
			listener.onChange(this);
	}

}
