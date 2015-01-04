package elf.swing;

import javax.swing.JComponent;

/**
 * Box implementation in Swing.
 * @author casse
 */
public class Box extends Container implements elf.ui.Box {
	private int direction;
	private javax.swing.Box box;

	@Override
	public void addFiller() {
		add(new Filler());
	}

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

	private class Filler extends Component {

		@Override
		public JComponent getComponent() {
			if(direction == HORIZONTAL)
				return new javax.swing.Box.Filler(ZERO, HFILL, HFILL);
			else
				return new javax.swing.Box.Filler(ZERO, VFILL, VFILL);
		}
		
	}
}
