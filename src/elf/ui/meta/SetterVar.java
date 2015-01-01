package elf.ui.meta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import elf.ui.Monitor;

/**
 * Variable with a setter method. Setter method name must be built as 
 * set<UpperCase attribute name> and take the good value as parameter.
 * It must also have an accessor method named get<UpperCase attribute name>.
 * @author casse
 */
public class SetterVar<T> extends Var<T> {
	private String name;
	private Object object;
	private Method setter, getter;
	
	/**
	 * Build a setter variable.
	 * @param object	Object it is applied to.
	 * @param name		Name of the field (suffixing the get and set methods).
	 */
	public SetterVar(Object object, String name) {
		this.object = object;
		this.name = name;
	}
	
	/**
	 * Get the current object.
	 * @return	Current object.
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * Set the current object.
	 * @param object	New object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
	public Method getGetter() {
		if(getter == null)
			try {
				getter = object.getClass().getDeclaredMethod("get" + name);
			} catch (NoSuchMethodException e) {
				Monitor.STD.error("cannot get method get" + name + ": " + e.getLocalizedMessage());
			} catch (SecurityException e) {
				Monitor.STD.error("cannot get method get" + name + ": " + e.getLocalizedMessage());
			}
		return getter;
	}
	
	public Method getSetter() {
		if(setter == null) {
			getGetter();
			if(getter == null)
				return null;
			try {
				setter = object.getClass().getDeclaredMethod("set" + name, getter.getReturnType());
			} catch (NoSuchMethodException e) {
				Monitor.STD.error("cannot get method set" + name + ": " + e.getLocalizedMessage());
			} catch (SecurityException e) {
				Monitor.STD.error("cannot get method set" + name + ": " + e.getLocalizedMessage());
			}			
		}
		return setter;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		getGetter();
		if(getter != null)
			try {
				return (T) getter.invoke(object);
			} catch (IllegalAccessException e) {
				Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
			} catch (IllegalArgumentException e) {
				Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
			} catch (InvocationTargetException e) {
				Monitor.STD.error("cannot call " + getter.getName() + ": " + e.getLocalizedMessage());
			}
		return null;
	}

	@Override
	public void set(T value) {
		getSetter();
		if(setter != null)
			try {
				setter.invoke(object, value);
			} catch (IllegalAccessException e) {
				Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
			} catch (IllegalArgumentException e) {
				Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
			} catch (InvocationTargetException e) {
				Monitor.STD.error("cannot call " + setter.getName() + ": " + e.getLocalizedMessage());
			}
	}

}
