package elf.ui;

import java.io.IOException;

import elf.store.TextSerializer;
import elf.store.TextSerializer.Serializer;

/**
 * Adapter to convert from/to string.
 * A string adapter may also be used to filter in/out values entered as strings.
 * @author casse
 */
public interface StringAdapter<T> {
	
	/**
	 * Convert value to string.
	 * @param value		Value to convert.
	 * @return			String value.
	 */
	String toString(T value);
	
	/**
	 * Convert string to value.
	 * @param string			String to convert.
	 * @return					Converted value.
	 * @throws IOException		If there is an error.
	 */
	T fromString(String string) throws IOException;

	/**
	 * Adapter based on string serialization.
	 * @author casse
	 *
	 * @param <T>		Type of values.
	 */
	public static class SerializerAdapter<T> implements StringAdapter<T> {
		private Serializer serial;
		
		/**
		 * Because type erasure, there is no compatible way to get the type
		 * of the generic type T: so the type argument is required. 
		 * @param type	Type of T.
		 */
		public SerializerAdapter(Class<?> type) {
			serial = TextSerializer.get(type);
		}
		
		@Override
		public String toString(T value) {
			try {
				return serial.serialize(value);
			} catch (IOException e) {
				// shouldn't arise
				return "<failed>";
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public T fromString(String string) throws IOException {
			return (T)serial.unserialize(string);
		}
		
	}
}
