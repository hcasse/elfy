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

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeMap;

import elf.ui.Field;
import elf.ui.Form;
import elf.ui.StringAdapter;
import elf.ui.TextField;

/**
 * A factory for field in form.
 * @author casse
 *
 */
public class Factory {
	private static final ClassComparator cmp = new ClassComparator();
	private Factory parent;
	private TreeMap<Class<?>, Maker> map;
	public final static Factory DEF = new Factory();

	static {
		DEF.add(Boolean.class, new Maker() {
			@SuppressWarnings("unchecked")
			@Override public Field make(Form form, Var<?> var) { return form.addCheckBox((Var<Boolean>)var); }
		});
		DEF.add(String.class, new Maker() {
			@Override public Field make(Form form, Var<?> var) { return form.addTextField(var); }
		});
		DEF.add(Integer.class, new CheckMaker<Integer>() {
			@Override public String toString(Integer value) { return value.toString(); }
			@Override public Integer fromString(String string) throws IOException {
				try { return Integer.parseInt(string); }
				catch(NumberFormatException e) { throw new IOException(e); }
			}
		});
		DEF.add(Long.class, new CheckMaker<Long>() {
			@Override public String toString(Long value) { return value.toString(); }
			@Override public Long fromString(String string) throws IOException {
				try { return Long.parseLong(string); }
				catch(NumberFormatException e) { throw new IOException(e); }
			}
		});
		DEF.add(Float.class, new CheckMaker<Float>() {
			@Override public String toString(Float value) { return value.toString(); }
			@Override public Float fromString(String string) throws IOException {
				try { return Float.parseFloat(string); }
				catch(NumberFormatException e) { throw new IOException(e); }
			}
		});
		DEF.add(Double.class, new CheckMaker<Double>() {
			@Override public String toString(Double value) { return value.toString(); }
			@Override public Double fromString(String string) throws IOException {
				try { return Double.parseDouble(string); }
				catch(NumberFormatException e) { throw new IOException(e); }
			}
		});

	}

	/**
	 * Empty factory.
	 */
	public Factory() {
		parent = null;
		map = new TreeMap<Class<?>, Maker>(cmp);
	}

	/**
	 * Build a factory by cloning the given one.
	 * @param factory	Factory to clone.
	 */
	public Factory(Factory factory) {
		parent = factory;
		map = new TreeMap<Class<?>, Maker>(factory.map);
	}

	/**
	 * Add a maker for the given type of data.
	 * @param clz		Data type.
	 * @param maker		Associated maker.
	 */
	public void add(Class<?> clz, Maker maker) {
		map.put(clz, maker);
	}

	/**
	 * Look for a maker that match the best the given data.
	 * @param clz		Data type.
	 * @return			Associated maker or null.
	 */
	public Maker get(Class<?> clz) {
		while(clz != Object.class) {
			Maker maker = map.get(clz);
			if(maker == null && parent != null)
				maker = parent.get(clz);
			if(maker != null)
				return maker;
			clz = clz.getSuperclass();
		}
		return null;
	}

	/**
	 * Print the content of the factory.
	 */
	public void print() {
		for(Class<?> cls: map.keySet())
			System.out.println("maker for " + cls.getCanonicalName());
	}

	/**
	 * Maker for a type of data.
	 * @author casse
	 */
	public interface Maker {

		/**
		 * Add a component to the form for the given variable.
		 * @param form	Form to add to.
		 * @param var	Variable to create field for.
		 * @return		Created component.
		 */
		Field make(Form form, Var<?> var);
	}

	/**
	 * A text maker with a check function.
	 * @author casse
	 */
	public static abstract class CheckMaker<T> implements Maker, StringAdapter<T> {

		@Override public Field make(Form form, Var<?> var) {
			@SuppressWarnings("unchecked")
			TextField<T> field = form.addTextField((Var<T>)var);
			field.setAdapter(this);
			return field;
		}

	}

	private static class ClassComparator implements Comparator<Class<?> > {

		@Override
		public int compare(Class<?> c1, Class<?> c2) {
			return c1.getCanonicalName().compareTo(c2.getCanonicalName());
		}

	}
}
