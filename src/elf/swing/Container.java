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
package elf.swing;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JLabel;

import elf.ui.ActionBar;
import elf.ui.CheckBox;
import elf.ui.EnumField;
import elf.ui.Form;
import elf.ui.List;
import elf.ui.PagePane;
import elf.ui.ProgressBar;
import elf.ui.SplitPane;
import elf.ui.StatusBar;
import elf.ui.SubsetField;
import elf.ui.TextArea;
import elf.ui.TextField;
import elf.ui.TextInfo;
import elf.ui.TitleBar;
import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;
import elf.ui.meta.EnumVar;
import elf.ui.meta.Var;

/**
 * Swing implementation of container.
 * @author casse
 */
public abstract class Container extends Component implements elf.ui.Container {
	private LinkedList<Component> content = new LinkedList<Component>();

	/**
	 * Get the list of components.
	 * @return	Component list.
	 */
	protected Collection<Component> getComponents() {
		return content;
	}
	
	/**
	 * Add a component.
	 * @param component		Added component.
	 */
	protected void add(Component component) {
		content.add(component);
	}
	
	@Override
	public elf.ui.Button addButton(Action action, int style) {
		Button button = new Button(action, style); 
		add(button);
		return button;
	}

	@Override
	public void remove(elf.ui.Component component) {
		content.remove(component);
	}

	@Override
	public elf.ui.Box addBox(int direction) {
		Box box = new Box(direction);
		add(box);
		return box;
	}

	@Override
	public TitleBar addTitleBar() {
		elf.swing.TitleBar bar = new elf.swing.TitleBar();
		add(bar);
		return bar;
	}

	@Override
	public void dispose() {
		for(Component component: getComponents())
			component.dispose();
	}

	@Override
	public ActionBar addActionBar() {
		elf.swing.ActionBar bar = new elf.swing.ActionBar();
		add(bar);
		return bar;
	}

	@Override
	public <T> TextField<T> addTextField(T init) {
		elf.swing.TextField<T> field = new elf.swing.TextField<T>(init);
		add(field);
		return field;
	}

	@Override
	public <T> TextField<T> addTextField(Var<T> var) {
		elf.swing.TextField<T> field = new elf.swing.TextField<T>(var);
		add(field);
		return field;
	}

	@Override
	public <T> List<T> addList() {
		elf.swing.List<T> list = new elf.swing.List<T>();
		add(list);
		return list;
	}

	@Override
	public <T> List<T> addList(CollectionVar<T> collection) {
		elf.swing.List<T> list = new elf.swing.List<T>(collection);
		add(list);
		return list;
	}

	@Override
	public PagePane addPagePane() {
		elf.swing.PagePane pane = new elf.swing.PagePane();
		add(pane);
		return pane;
	}

	@Override
	public SplitPane addSplitPane(int axis) {
		elf.swing.SplitPane spane = new elf.swing.SplitPane(axis);
		add(spane);
		return spane;
	}

	@Override
	public Form addForm(int style, Action action) {
		elf.swing.Form form = new elf.swing.Form(style, action);
		add(form);
		return form;
	}

	@Override
	public CheckBox addCheckBox(Var<Boolean> var) {
		elf.swing.CheckBox cbox = new elf.swing.CheckBox(var);
		add(cbox);
		return cbox;
	}

	@Override
	public <T> SubsetField<T> addSubsetField(CollectionVar<T> set) {
		elf.swing.SubsetField<T> field = new elf.swing.SubsetField<T>(set);
		add(field);
		return field;
	}

	@Override
	public TextInfo addTextInfo(String format) {
		elf.swing.TextInfo info = new elf.swing.TextInfo(format);
		add(info);
		return info;
	}

	@Override
	public StatusBar addStatusBar() {
		elf.swing.StatusBar sbar = new elf.swing.StatusBar();
		add(sbar);
		return sbar;
	}

	@Override
	public ProgressBar addProgressBar(Var<Integer> value, int min, int max, int axis) {
		elf.swing.ProgressBar bar = new elf.swing.ProgressBar(value, Var.make(min), Var.make(max), axis);
		add(bar);
		return bar;
	}

	@Override
	public ProgressBar addProgressBar(Var<Integer> value, Var<Integer> min, Var<Integer> max, int axis) {
		elf.swing.ProgressBar bar = new elf.swing.ProgressBar(value, min, max, axis);
		add(bar);
		return bar;
	}

	@Override
	public TextArea addTextArea() {
		elf.swing.TextArea area = new elf.swing.TextArea();
		add(area);
		return area;
	}

	@Override
	public <T> EnumField<T> addEnumField(EnumVar<T> var) {
		elf.swing.EnumField<T> field = new  elf.swing.EnumField<T>(var);
		add(field);
		return field;
	}

	@Override
	public void add(elf.ui.Icon icon) {
		add(new Image(icon));
	}

	public static class Image extends Component {
		private elf.ui.Icon icon;
		private JLabel label;
		
		public Image(elf.ui.Icon icon) {
			this.icon = icon;
		}

		@Override
		public JComponent getComponent(View view) {
			if(label == null)
				label = new JLabel(view.getIcon(icon).get(Icon.NORMAL, Icon.ORIGINAL));
			return label;
		}

		@Override
		public void dispose() {
			label = null;
		}
		
	}
}
