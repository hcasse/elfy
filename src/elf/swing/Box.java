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
			switch(direction) {
			case HORIZONTAL:
				box = javax.swing.Box.createHorizontalBox();
				for(Component component: getComponents()) {
					JComponent jc = component.getComponent();
					jc.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
					box.add(jc);
				}
				break;
			case VERTICAL:
				box = javax.swing.Box.createVerticalBox();
				for(Component component: getComponents()) {
					JComponent jc = component.getComponent();
					jc.setAlignmentY(javax.swing.JComponent.TOP_ALIGNMENT);
					box.add(jc);
				}
				break;
			default:
				assert(false);
			}
			
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
