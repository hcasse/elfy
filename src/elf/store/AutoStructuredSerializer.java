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
package elf.store;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Automatically serialize a datastructure to a structured store.
 * @author casse
 */
public class AutoStructuredSerializer {
	StructuredStore store;
	Object object;
	
	/**
	 * Test if the field is an instance accessible field.
	 * @param field		Field to test.
	 * @return			True if it is instance, false else.
	 */
	private static boolean isInstanceField(Field field) {
		return (field.getModifiers() & Modifier.PUBLIC) != 0
			&&	(field.getModifiers() & Modifier.STATIC) == 0;
	}
	
	/**
	 * Build an automatic serializer.
	 * @param store		Used store.
	 * @param object	Object to serialize.
	 */
	public AutoStructuredSerializer(StructuredStore store, Object object) {
		this.store = store;
		this.object = object;
	}
	
	/**
	 * Save the object.
	 */
	public void save() throws IOException {
		StructuredStore.Save save = store.save();
		saveFields(save, object);
		save.end();
	}
	
	/**
	 * Save the fields of the current object.
	 * @param save				Store to save to.
	 * @param object			Object to save.
	 * @throws IOException		In case of error.
	 */
	private void saveFields(StructuredStore.Save save, Object object) throws IOException {
		for(Field field: object.getClass().getDeclaredFields()) {
			if(isInstanceField(field)) {
				try {
					Object value = field.get(object);
					save.putField(field.getName());
					saveData(save, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Save a datum.
	 * @param save		Store to save to.
	 * @param object	Value to save.
	 */
	private void saveData(StructuredStore.Save save, Object object) throws IOException {
		if(store.supports(object.getClass()))
			save.put(object);
		else if(object instanceof Collection<?>) {
			save.putList();
			for(Object item: ((Collection<?>)object))
				saveData(save, item);
			save.end();
		}
		else {
			save.putStruct();
			saveFields(save, object);
			save.end();
		}
	}
	
	/**
	 * Load the object.
	 * @throws IOException
	 */
	public void load() throws IOException {
		StructuredStore.Load load = store.load();
		loadFields(load, object);
		load.end();
		
	}

	/**
	 * Load the fields of the current object.
	 * @param save				Store to load to.
	 * @param object			Object to load.
	 * @throws IOException		In case of error.
	 */
	private void loadFields(StructuredStore.Load load, Object object) throws IOException {
		for(Field field: object.getClass().getDeclaredFields())
			if(isInstanceField(field)) {
				try {
					if(load.getField(field.getName()))
						field.set(object, loadData(load, field.getGenericType()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * Load a datum.
	 * @param load		Store to load from.
	 * @param field		Target field.
	 * @return			Read value.
	 */
	@SuppressWarnings("unchecked")
	private Object loadData(StructuredStore.Load load, Type type) throws IOException {
		
		// basic class and arrays
		if(type instanceof Class) {
			Class<?> clazz = (Class<?>)type;
			if(store.supports(clazz))
				return load.get((Class<?>)type);
			else if(clazz.isArray()) {
				Class<?> item_type = clazz.getComponentType();
				int n = load.getList();
				Object array = Array.newInstance(item_type, n);
				for(int i = 0; i < n; i++)
					Array.set(array, i, loadData(load, item_type));
				load.end();
				return array;
			}
			else if(clazz.isEnum()) {
				
			}
		}
		
		// collections
		if(type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType)type;
			Type rtype = ptype.getRawType();
			if(rtype instanceof Class<?>) {
				Class<?> clazz = (Class<?>)rtype;
				if(Collection.class.isAssignableFrom(clazz)) {
					try {
						@SuppressWarnings("rawtypes")
						Collection coll = (Collection<?>)clazz.newInstance();
						int n = load.getList();
						for(int i = 0; i < n; i++)
							coll.add(loadData(load, ptype.getActualTypeArguments()[0]));
						load.end();
						return coll;
					} catch (InstantiationException e) {
						throw new IOException(e);
					} catch (IllegalAccessException e) {
						throw new IOException(e);
					}
				}
			}
		}
		
		// else this is a structured type
		if(type instanceof Class<?>) {
			Class<?> clazz = (Class<?>)type;
			try {
				Object r = clazz.newInstance();
				load.getClass();
				for(Field field: clazz.getDeclaredFields())
					if(field.isAccessible() && load.getField(field.getName()))
						field.set(r, load.get(field.getType()));
				load.end();
			} catch (InstantiationException e) {
				throw new IOException(e);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
		}
		
		// don't know how to proceed ?
		throw new IOException("unsupported type: " + type); 
		
		/*else if(Collection.class.isAssignableFrom(type)) {
			Collection<?> coll = (Collection<?>)type.newInstance();
			Class<?> item = (Class<?>)(()type.getGenericSuperclass()).getActualTypeArguments()[0];
			int n = load.getList();
			for(int i = 0; i < n; i++)
				;
		}
		else {
		}
			
			// get the object
			Object data = field.get(object);
			
			// fetch the object
			if(data instanceof Collection<?>) {
				Collection<?> coll = (Collection<?>)data;
				
				save.putList();
				for(Object item: ((Collection<?>)object))
					saveData(save, item);
				save.end();
			}
			else {
				save.putStruct();
				saveFields(save, object);
				save.end();
			}
		}*/
	}
	
}
