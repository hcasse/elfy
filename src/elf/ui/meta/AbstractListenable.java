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

import java.util.LinkedList;

/**
 * Abstract implementation of a listener.
 * @author casse
 */
public class AbstractListenable extends AbstractEntity implements Subject {
	private LinkedList<Delegate> delegates = new LinkedList<Delegate>();

	@Override
	public void add(final Observer listener) {
		add(new Delegate(listener) {
			@Override public void update() { listener.update(); }
		});
	}

	@Override
	public void remove(Observer listener) {
		removeByID(listener);
	}
	
	protected void removeByID(Object id) {
		for(Delegate delegate: delegates)
			if(id == delegate.getID()) {
				delegates.remove(delegate);
				return;
			}		
	}

	/**
	 * Called to update all listeners.
	 */
	protected void fireChange() {
		for(Delegate delegate: delegates)
			delegate.update();
	}

	/**
	 * Add directly a listener using a wrapper.
	 * @param delegate	Adde listener.
	 */
	protected void add(Delegate delegate) {
		delegates.add(delegate);
	}

	/**
	 * Wrapper to embed complex listeners.
	 * @author casse
	 *
	 * @param <T>	Type of complex listener.
	 */
	protected static abstract class Delegate implements Observer {
		protected Object id;
		
		public Delegate(Object id) {
			this.id = id;
		}
		
		public Object getID() {
			return id;
		}
		
	}

}

