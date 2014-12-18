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
import java.util.Stack;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParentNode;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.Text;
import nu.xom.ValidityException;

/**
 * Structured store supporting XML.
 * @author casse
 *
 */
public class XMLStructuredStore implements StructuredStore {
	private static final int
		IN_STRUCT = 0,
		IN_LIST = 1,
		IN_FIELD = 2;
	public static final String
		TOP_ELEM = "elf-store",
		ITEM_ELEM = "elf-item";
	Storage store;
	
	/**
	 * Build an XML structured store.
	 * @param store		Stream storage to load/save to.
	 */
	public XMLStructuredStore(Storage store) {
		this.store = store;
	}
	
	@Override
	public Save save() throws IOException {
		return new XMLSave();
	}

	@Override
	public Load load() throws IOException {
		return new XMLLoad();
	}

	/**
	 * Save part.
	 * @author casse
	 */
	private class XMLSave implements Save {
		int state = IN_STRUCT;
		Element top = new Element(TOP_ELEM);
		ParentNode current = top;
		Document doc = new Document(top);
		Stack<Integer> stack = new Stack<Integer>();
		
		/**
		 * Builder for an XML save.
		 */
		public XMLSave() {
			doc.setRootElement(top);
		}
		
		/**
		 * Push a new state.
		 * @param new_state		Pushed state.
		 * @param element		New current element.
		 */
		private void push(int new_state, Element element) {
			stack.push(state);
			state = new_state;
			current.appendChild(element);
			current = element;
		}
		
		/**
		 * Pop an old state.
		 */
		private void pop() {
			state = stack.pop();
			current = current.getParent();
		}
		
		@Override
		public void put(Object value) throws IOException {
			switch(state) {
			case IN_STRUCT:
				throw new StructuralError("cannot put raw value in structure");
			case IN_LIST:
				Element item = new Element(ITEM_ELEM);
				current.appendChild(item);
				item.appendChild(new Text(TextSerializer.serialize(value)));
				break;
			case IN_FIELD:
				current.appendChild(new Text(TextSerializer.serialize(value)));
				pop();
				break;
			}
		}

		@Override
		public void putList() throws IOException {
			switch(state) {
			case IN_STRUCT:
				throw new StructuralError("cannot put raw list in list");
			case IN_LIST:
				push(IN_LIST, new Element(ITEM_ELEM));
				break;
			case IN_FIELD:
				state = IN_LIST;
				break;
			}
		}

		@Override
		public void putStruct() throws IOException {
			switch(state) {
			case IN_STRUCT:
				throw new StructuralError("cannot put struct in struct");
			case IN_LIST:
				push(IN_STRUCT, new Element(ITEM_ELEM));
				break;
			case IN_FIELD:
				state = IN_STRUCT;
				break;
			}
		}

		@Override
		public void putField(String name) throws IOException {
			switch(state) {
			case IN_STRUCT:
				push(IN_FIELD, new Element(name));
				break;
			case IN_LIST:
				throw new StructuralError("cannot put field in list");
			case IN_FIELD:
				throw new StructuralError("cannot put field in field");
			}
		}

		@Override
		public void end() throws IOException {
			if(current == top) {
				Serializer serial = new Serializer(store.write());
				serial.write(doc);
				serial.flush();
			}
			else
				switch(state) {
				case IN_STRUCT:
				case IN_LIST:
					pop();
					break;
				case IN_FIELD:
					throw new StructuralError("ending inside an empty field");
				}
		}
	}
	
	private class XMLLoad implements Load {
		Document doc;
		LoadState cur = new LoadState();
		Stack<LoadState> stack = new Stack<LoadState>();
		
		public XMLLoad() throws IOException {
			Builder builder = new Builder();
			try {
				doc = builder.build(store.read());
				cur.elt = doc.getRootElement();
			} catch (ValidityException e) {
				throw new IOException(e);
			} catch (ParsingException e) {
				throw new IOException(e);
			}
		}
		
		private final void pop() {
			cur = stack.pop();
		}
		
		@Override
		public Object get(Class<?> type) throws IOException {
			Object r = null;
			switch(cur.state) {
			case IN_STRUCT:
				throw new IOException("cannot get value of a structure");
			case IN_FIELD:
				r = TextSerializer.unserialize(type, cur.elt.getValue().trim());
				pop();
				break;
			case IN_LIST:
				r = TextSerializer.unserialize(type, cur.elt.getValue().trim());
				if(cur.i + 1 < cur.elts.size())
					cur.elt = cur.elts.get(++cur.i);
				break;
			}
			return r;
		}

		@Override
		public int getList() throws IOException {
			switch(cur.state) {
			case IN_STRUCT:
				throw new IOException("cannot get list of a structure");
			default:
				Elements elts = cur.elt.getChildElements(ITEM_ELEM);
				int r = elts.size();
				stack.push(cur);
				cur = new LoadState();
				cur.state = IN_LIST;
				cur.elts = elts;
				cur.i = 0;
				if(r > 0)
					cur.elt = elts.get(0);
				return r;
			}
		}

		@Override
		public void getStruct() throws IOException {
			switch(cur.state) {
			case IN_STRUCT:
				throw new IOException("cannot get struct of a structure");
			case IN_LIST:
			case IN_FIELD:
				stack.push(cur);
				cur = new LoadState();
				cur.state = IN_STRUCT;
				cur.elt = stack.peek().elt;
				break;
			}
		}

		@Override
		public boolean getField(String name) throws IOException {
			switch(cur.state) {
			case IN_STRUCT:
				Element elt = cur.elt.getFirstChildElement(name);
				if(elt == null)
					return false;
				else {
					stack.push(cur);
					cur = new LoadState();
					cur.elt = elt;
					cur.state = IN_FIELD;
				}
				return true;
			default:
				throw new IOException("cannot get field from field or list");
			}
		}

		@Override
		public void end() throws IOException {
			if(!stack.isEmpty())
				cur = stack.pop();
			if(cur.state == IN_FIELD)
				pop();
		}
		
	}
	
	private static final class LoadState {
		int state;
		Element elt;
		Elements elts;
		int i;
	}

	@Override
	public boolean supports(Class<?> type) {
		return TextSerializer.get(type) != null;
	}

	@Override
	public boolean exists() {
		return store.exists();
	}
	
}
