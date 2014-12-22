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

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provide serialization as text.
 * @author casse
 */
public class TextSerializer {
	private static final Pattern COLOR_PAT = Pattern.compile("^#([0-9a-fA-F]{6})$");
	private static final Pattern bool_pat = Pattern.compile("^(false|no|off)|(true|yes|on)$");
	private static final Hashtable<Type, Serializer> map = new Hashtable<Type, Serializer>();
	static {
		
		map.put(Boolean.class, new StringSerializer() {
			@Override public String serialize(Object value) throws IOException {
				return (Boolean)value ? "on" : "off";
			}			
			@Override public Object unserialize(String text) throws IOException {
				Matcher m = bool_pat.matcher(text);
				if(!m.matches())
					throw new IOException("not a boolean: " + text);
				else
					return m.group(1) == null;
			}
		});
		map.put(boolean.class, map.get(Boolean.class));
		
		map.put(Character.class, new StreamSerializer() {
			@Override public Object unserialize(Reader reader) throws IOException {
				int r = reader.read();
				if(r == 0)
					throw new IOException("no more characters");
				else
					return (char)r;
			}
			@Override public void serialize(Writer writer, Object value) throws IOException {
				writer.write((Character)value);
			}
		});
		map.put(char.class, map.get(Character.class));

		map.put(Byte.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Byte.parseByte(text);
			}
		});
		map.put(byte.class, map.get(Byte.class));

		map.put(Short.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Short.parseShort(text);
			}
		});
		map.put(short.class, map.get(Short.class));

		map.put(Integer.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Integer.parseInt(text);
			}
		});
		map.put(int.class, map.get(Integer.class));

		map.put(Long.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Long.parseLong(text);
			}
		});
		map.put(long.class, map.get(Long.class));

		map.put(Float.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Float.parseFloat(text);
			}
		});
		map.put(float.class, map.get(Float.class));

		map.put(Double.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return Double.parseDouble(text);
			}
		});
		map.put(double.class, map.get(Double.class));
		
		map.put(String.class, new ToStringSerializer() {
			@Override public Object unserialize(String text) throws IOException {
				return text;
			}
		});
		
		map.put(Color.class, new ColorSerializer());
	}
	
	/**
	 * Add a serializer.
	 * @param type		Type the serializer is for.
	 * @param serial	Added serializer.
	 */
	public static void add(Class<?> type, Serializer serial) {
		map.put(type, serial);
	}
	
	/**
	 * Get serializer for the given type.
	 * @param type		Type serializer is looked for.
	 * @return			Found serializer or null.
	 */
	public static Serializer get(Type type) {
		return map.get(type);
	}
	
	/**
	 * Find the serializer for the given object.
	 * @param object
	 * @return
	 */
	public static Serializer get(Object object) {
		return get(object.getClass());
	}
	
	/**
	 * Serialize the given object on the given writer.
	 * @param writer		Writer to serialize to.
	 * @param object		Serialized object.
	 */
	public static void serialize(Writer writer, Object object) throws IOException {
		Serializer serial = get(object.getClass());
		if(serial == null)
			throw new IOException("can't serialize object of type " + object.getClass().getName());
		else
			serial.serialize(writer, object);
	}
	
	/**
	 * Serialize an object to string.
	 * @param object			Object to serialize.
	 * @return					String result.
	 * @throws IOException		Error in serialization.
	 */
	public static String serialize(Object object) throws IOException {
		Serializer serial = get(object.getClass());
		if(serial == null)
			throw new IOException("can't serialize object of type " + object.getClass().getName());
		else
			return serial.serialize(object);		
	}
	
	/**
	 * Unserialize from a string.
	 * @param type		Type of unserialized object.
	 * @param text		String to unserialize from.
	 * @return			Unserialized object.
	 */
	public static Object unserialize(Class<?> type, String text) throws IOException {
		Serializer serial = get(type);
		if(serial == null)
			throw new IOException("can't unserialize object of type " + type.getName());
		else
			return serial.unserialize(text);				
	}
	
	/**
	 * Unserialize from a string.
	 * @param type		Type of unserialized object.
	 * @param reader	Reader to unserialize from.
	 * @return			Unserialized object.
	 */
	public static Object unserialize(Class<?> type, Reader reader) throws IOException {
		Serializer serial = get(type);
		if(serial == null)
			throw new IOException("can't unserialize object of type " + type.getName());
		else
			return serial.unserialize(reader);				
	}
	
	/**
	 * Provide serialization service.
	 * @author casse
	 */
	public interface Serializer {
		void serialize(Writer writer, Object value) throws IOException;
		String serialize(Object value) throws IOException;
		Object unserialize(Reader reader) throws IOException;
		Object unserialize(String text) throws IOException;
	}
	
	/**
	 * Serializer based on streams (writer / reader).
	 * @author casse
	 */
	public abstract static class StreamSerializer implements Serializer {

		@Override
		public Object unserialize(String text) throws IOException {
			StringReader reader = new StringReader(text);
			return unserialize(reader);
		}

		@Override
		public String serialize(Object value) throws IOException {
			StringWriter writer = new StringWriter();
			serialize(writer, value);
			return writer.toString();
		}
				
	}
	
	/**
	 * Serialization based on string handling.
	 * @author casse
	 */
	public abstract static class StringSerializer implements Serializer {

		@Override
		public void serialize(Writer writer, Object value) throws IOException {
			writer.write(serialize(value));
		}

		@Override
		public Object unserialize(Reader reader) throws IOException {
			StringBuffer buf = new StringBuffer();
			int r = reader.read();
			while(r != 0) {
				buf.append((char)r);
				r = reader.read();
			}
			return serialize(buf.toString());
		}

	}
	
	/**
	 * String serializer using toString() for serialization.
	 * @author casse
	 *
	 */
	public abstract static class ToStringSerializer extends StringSerializer {

		@Override
		public String serialize(Object value) throws IOException {
			return value.toString();
		}
		
	}

	/**
	 * Color serializer.
	 * @author casse
	 */
	private static class ColorSerializer extends StringSerializer {

		@Override
		public String serialize(Object value) throws IOException {
			Color color = (Color)value;
			return String.format("#%06x", color.getRGB() & 0x00ffffff);
		}

		@Override
		public Object unserialize(String text) throws IOException {
			Matcher m = COLOR_PAT.matcher(text);
			if(!m.matches())
				throw new IOException("bad color value: " + text);
			return new Color(Integer.parseInt(m.group(1), 16));
		}
		
	}
}
