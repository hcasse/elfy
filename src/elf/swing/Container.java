package elf.swing;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JComponent;

import elf.ui.ActionBar;
import elf.ui.Form;
import elf.ui.List;
import elf.ui.PagePane;
import elf.ui.SplitPane;
import elf.ui.TextField;
import elf.ui.TitleBar;
import elf.ui.meta.Action;
import elf.ui.meta.CollectionVar;
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
	public elf.ui.Container addBox(int direction) {
		Box box = new Box(direction);
		add(box);
		return box;
	}

	/**
	 * Horizontal / vertical box.
	 * @author casse
	 */
	public static class Box extends Container {
		private int direction;
		private javax.swing.Box box;

		public Box(int direction) {
			this.direction = direction;
		}
		
		@Override
		public JComponent getComponent() {
			if(box == null) {
				
				// create the box
				switch(direction) {
				case HORIZONTAL:	box = javax.swing.Box.createHorizontalBox(); break;
				case VERTICAL:		box = javax.swing.Box.createVerticalBox(); break;
				default:			assert(false);
				}
				
				// add the components
				for(Component component: getComponents())
					box.add(component.getComponent());
			}
			return box;
		}

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
	
}
