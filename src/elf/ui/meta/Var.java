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
		if(value != get()) {
			accessor.set(value);
			fireChange();			
		}
	}
	
	/**
	 * Add a listener to be alerted about changes of value of the variable.
	 * @param listener	Listener to add.
	 */
	public void add(final ChangeListener<T> listener) {
		add(new Delegate(listener) {
			@Override public void update() { listener.onChange(Var.this); }
		});
	}
	
	/**
	 * Listen the current variable and trigger entity event on the given entity.
	 * @param entity	Entity to signal.
	 */
	public void observeForEntity(final Entity entity) {
		add(new Delegate(entity) {
			@Override public void update() { entity.fireEntityChange(); }
		});
	}
	
	/**
	 * Remove the given listener.
	 * @param listener	Removed listener.
	 */
	public void remove(ChangeListener<T> listener) {
		removeByID(listener);
	}
	
	/**
	 * Remove the given listener.
	 * @param listener	Removed listener.
	 */
	public void remove(EventListener listener) {
		removeByID(listener);
	}

	/**
	 * Listener to record change in the data.
	 */
	public interface ChangeListener<T> {
		
		/**
		 * This method is called each time the variable
		 * this listener is attached to is changed.
		 * @param var	Variable triggering the event.
		 */
		void onChange(Var<T> var);
		
	}
	
	/**
	 * Listener for a single change event (without reference to the
	 * changed variable).
	 * @author casse
	 */
	public interface EventListener {
		
		/**
		 * This method is called each time the listened variable
		 * is changed.
		 */
		void onChange();
		
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
	 * A listener that can be disabled (recording but ignoring the events) or enabled (propagating an event
	 * or triggering postponed event). To let it working, you have to overload actualChange() instead of
	 * onChange().
	 * @author casse
	 *
	 */
	public static class DelayedListener<T> implements ChangeListener<T>, Activable {
		private ChangeListener<T> listener;
		private boolean disabled = false;
		private boolean triggered = false;
		private Var<T> var;

		/**
		 * Build a delayed listener.
		 * @param listener	Added listener.
		 */
		public DelayedListener(ChangeListener<T> listener) {
			this.listener = listener;
		}
		
		/**
		 * Enable the immediate raise of events. 
		 */
		@Override
		public void enable() {
			if(disabled && triggered) {
				listener.onChange(var);
				triggered = false;
			}
			disabled = false;
		}
		
		/**
		 * Disable the immediate raise of events.
		 */
		@Override
		public void disable() {
			disabled = true;
		}

		@Override
		public void onChange(Var<T> var) {
			if(disabled) {
				triggered = true;
				this.var = var;
			}
			else
				listener.onChange(var);
		}		
	}

}
