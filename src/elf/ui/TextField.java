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
package elf.ui;

import java.lang.reflect.Type;

import elf.store.TextSerializer;
import elf.store.TextSerializer.Serializer;
import elf.ui.meta.SingleVar;
import elf.ui.meta.Var;

/**
 * Text field.
 * @author casse
 */
public abstract class TextField<T> implements SingleVar.Listener<T> {
	private SingleVar<T> var;
	private Serializer serial;
	
	/**
	 * Nice trick to get type of generic type argument.
	 * Thanks to http://www.artima.com/weblogs/viewpost.jsp?thread=208860.
	 * @return	Generic type.
	 */
	private Type getGenericTypeArgument() {
		return var.get().getClass();		
	}

	public TextField(T init) {
		set(new SingleVar<T>(init));
		serial = TextSerializer.get(getGenericTypeArgument());
	}
	
	public TextField(SingleVar<T> var) {
		set(var);
		serial = TextSerializer.get(getGenericTypeArgument());
	}
	
	/**
	 * Dispose the UI.
	 */
	public void dispose() {
		var.removeListener(this);
	}
	
	/**
	 * Get the current serializer.
	 * @return	Current serializer.
	 */
	public Serializer getSerializer() {
		return serial;
	}

	/**
	 * Set the serializer.
	 * @param serial	New serializer.
	 */
	public void setSerializer(Serializer serial) {
		this.serial = serial;
		updateUI();
	}
	
	/**
	 * Get the current variable.
	 * @return	Current variable.
	 */
	public SingleVar<T> getVar() {
		return var;
	}
	
	/**
	 * Set the current variable.
	 * @param var	New variable.
	 */
	public void set(SingleVar<T> var) {
		if(var != null)
			var.removeListener(this);
		this.var = var;
		var.addListener(this);
	}
	
	/**
	 * Update the user interface from the variable.
	 */
	protected abstract void updateUI();
	
	/**
	 * Update the variable from the user interface.
	 */
	protected abstract void updateVar();

	@Override
	public void change(Var<T> data) {
		updateUI();
	}
	
}
