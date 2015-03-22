/*
 * ElfCore library
 * Copyright (c) 2015 - Hugues Cassé <hugues.casse@laposte.net>
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

import java.util.Vector;

/**
 * Abstract implementation of a listener.
 * @author casse
 */
public class AbstractListenable extends AbstractEntity implements Listenable {
	private Vector<Listenable.Listener> listeners = new Vector<Listenable.Listener>();

	@Override
	public void add(Listenable.Listener listener) {
		listeners.add(listener);
	}

	@Override
	public void remove(Listenable.Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * Called to update all listeners.
	 */
	protected void fireListenableChange() {
		for(Listenable.Listener listener: listeners)
			listener.update(this);
	}
}

