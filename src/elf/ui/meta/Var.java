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

import elf.app.AutoConfiguration;

/**
 * Common interface to variables.
 * @author casse
 */
public class Var<T> extends AbstractListenable {
	private Accessor<T> accessor;

	/**
	 * Fast variable construction.
	 * @param value		Value of the variable.
	 * @return			Built variable.
	 */
	public static <T> Var<T> make(T value) {
		return new Var<T>(value);
	}
	
	/**
	 * Build an empty variable.
	 */
	public Var() {
		this.accessor = new Accessor.Store<T>();
	}
	
	/**
	 * Build a variable storing a single value.
	 * @param value		Stored value.
	 */
	public Var(T value) {
		this.accessor = new Accessor.Store<T>(value);
	}
	
	/**
	 * Build a variable from an accessor.
	 * @param accessor	Accessor to use.
	 */
	public Var(Accessor<T> accessor) {
		this.accessor = accessor;
		accessor.link(this);
	}

	/**
	 * Called when the variable is modified.
	 */
	public void fireChange() {
		fireListenableChange();
	}
	
	/**
	 * Change the accessor.
	 * @param accessor	New accessor.
	 */
	public void setAccessor(Accessor<T> accessor) {
		this.accessor.unlink();
		this.accessor = accessor;
		this.accessor.link(this);
		fireChange();
	}

	/**
	 * Get the value of the data.
	 * @return	Data value.
	 */
	public T get() {
		return accessor.get();
	}
	
	/**
	 * Set the value of the data.
	 * @param value		Set value.
	 */
	public void set(T value) {
		accessor.set(value);
		fireChange();
	}
	
	/**
	 * Add the given listener.
	 * @param listener	Listener to add.
	 */
	public void addListener(Listener<T> listener) {
		add(new ListenerSupport(listener));
	}
	
	/**
	 * Listen the current variable and trigger entity event on the given entity.
	 * @param entity	Entity to signal.
	 */
	public void listenForEntity(Entity entity) {
		add(new EntityPropagator<T>(entity));
	}
	
	/**
	 * Remove the given listener.
	 * @param listener	Removed listener.
	 */
	public void removeListener(Listener<T> listener) {
		remove(new ListenerSupport(listener));
	}
	
	/**
	 * Listener to record change in the data.
	 */
	public interface Listener<T> {
		
		/**
		 * Called each time the data has been changed.
		 * @param data	Changed data.
		 */
		public void change(Var<T> data);
	}
	
	/**
	 * Build a variable based on get/set accessor.
	 * @author casse
	 *
	 * @param <T>	Type of attribute.
	 * @param <U>	Type of object.
	 */
	public static class GetterSetter<T, U> extends Var<T> {
		
		public GetterSetter(U object, String name) {
			super(new Accessor.GetSet<T, U>(object, name));
		}

		public GetterSetter(Var<U> object, String name) {
			super(new Accessor.GetSet<T, U>(object, name));
		}
	}
	
	/**
	 * Build a variable based on an attribute accessor.
	 * @author casse
	 *
	 * @param <T>	Type of attribute.
	 * @param <T>	Type of object.
	 */
	public static class Attribute<T, U> extends Var<T> {
		
		public Attribute(U object, String name) {
			super(new Accessor.Attribute<T, U>(object, name));
		}
		
		public Attribute(Var<U> object, String name) {
			super(new Accessor.Attribute<T, U>(object, name));
		}
	}
	
	/**
	 * Build a variabled on an attribute in a configuration.
	 * @author casse
	 *
	 * @param <T>		Type of the attribute.
	 */
	public static class Config<T> extends Var<T> {
		
		public Config(AutoConfiguration config, String name) {
			super(new Accessor.Config<T>(config, name));
		}
		
	}
	
	/**
	 * Provide support for variable listener based on listenable listeners.
	 * @author casse
	 *
	 * @param <T>	Type of variable.
	 */
	public class ListenerSupport implements Listenable.Listener {
		Listener<T> listener;
		
		public ListenerSupport(Listener<T> listener) {
			this.listener = listener;
		}
		
		@Override
		public void update(Listenable obs) {
			listener.change(Var.this);
		}
		
	}
	
	/**
	 * Listener propagating change on variable to entity lookup.
	 * @author casse
	 *
	 * @param <T>	Type of variable value.
	 */
	public static class EntityPropagator<T> implements Listenable.Listener {
		Entity entity;
		
		public EntityPropagator(Entity entity) {
			this.entity = entity;
		}
		
		@Override
		public void update(Listenable obs) {
			entity.fireEntityChange();
		}
		
	}
}
