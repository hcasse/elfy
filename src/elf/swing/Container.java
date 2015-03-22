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
import elf.ui.ErrorManager;
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
public abstract class Container extends Parent implements elf.ui.Container {
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
	 * @return				Current component (composition purpose).
	 */
	protected <T extends Component> T add(T component) {
		content.add(component);
		super.addChild(component);
		return component;
	}
	
	@Override
	public void remove(elf.ui.Component component) {
		content.remove(component);
	}

	@Override
	public void dispose() {
		for(Component component: getComponents())
			component.dispose();
	}

	@Override
	public elf.ui.Button addButton(Action action, int style) {
		return add(new Button(action, style)); 
	}

	@Override
	public elf.ui.Box addBox(int direction) {
		return add(new Box(direction));
	}

	@Override
	public TitleBar addTitleBar() {
		return add(new elf.swing.TitleBar());
	}

	@Override
	public ActionBar addActionBar() {
		return add(new elf.swing.ActionBar());
	}

	@Override
	public <T> TextField<T> addTextField(T init) {
		return add(new elf.swing.TextField<T>(init));
	}

	@Override
	public <T> TextField<T> addTextField(Var<T> var) {
		return add(new elf.swing.TextField<T>(var));
	}

	@Override
	public <T> List<T> addList() {
		return add(new elf.swing.List<T>());
	}

	@Override
	public <T> List<T> addList(CollectionVar<T> collection) {
		return add(new elf.swing.List<T>(collection));
	}

	@Override
	public PagePane addPagePane() {
		return add(new elf.swing.PagePane());
	}

	@Override
	public SplitPane addSplitPane(int axis) {
		return add(new elf.swing.SplitPane(axis));
	}

	@Override
	public Form addForm() {
		return add(new elf.swing.Form(getView().getFactory()));
	}

	@Override
	public CheckBox addCheckBox(Var<Boolean> var) {
		return add(new elf.swing.CheckBox(var));
	}

	@Override
	public <T> SubsetField<T> addSubsetField(CollectionVar<T> set) {
		return add(new elf.swing.SubsetField<T>(set));
	}

	@Override
	public TextInfo addTextInfo(String format) {
		return add(new elf.swing.TextInfo(format));
	}

	@Override
	public StatusBar addStatusBar() {
		return add(new elf.swing.StatusBar());
	}

	@Override
	public ProgressBar addProgressBar(Var<Integer> value, int min, int max, int axis) {
		return add(new elf.swing.ProgressBar(value, Var.make(min), Var.make(max), axis));
	}

	@Override
	public ProgressBar addProgressBar(Var<Integer> value, Var<Integer> min, Var<Integer> max, int axis) {
		return add(new elf.swing.ProgressBar(value, min, max, axis));
	}

	@Override
	public TextArea addTextArea() {
		return add(new elf.swing.TextArea());
	}

	@Override
	public <T> EnumField<T> addEnumField(EnumVar<T> var) {
		return add(new  elf.swing.EnumField<T>(var));
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

	@Override
	public ErrorManager addErrorManager() {
		return add(new elf.swing.ErrorManager());
	}
}
