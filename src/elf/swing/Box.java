package elf.swing;

import javax.swing.JComponent;

/**
 * Box implementation in Swing.
 * @author casse
 */
public class Box extends Container implements elf.ui.Box {
	private int axis;
	private javax.swing.Box box;
	private int align = CENTER;

	@Override
	public void addFiller() {
		add(new Filler());
	}

	public Box(int axis) {
		this.axis = axis;
	}
	
	@Override
	public JComponent getComponent(UI ui) {
		if(box == null) {
			switch(axis) {
			
			case HORIZONTAL:
				box = javax.swing.Box.createHorizontalBox();
				switch(align) {
				case TOP:		box.setAlignmentY(0); break;
				case CENTER:	box.setAlignmentY(0.5f); break;
				case BOTTOM:	box.setAlignmentY(1); break;
				}
				for(Component component: getComponents()) {
					JComponent jc = component.getComponent(ui);
					jc.setAlignmentX(javax.swing.JComponent.LEFT_ALIGNMENT);
					box.add(jc);
				}
				break;
				
			case VERTICAL:
				box = javax.swing.Box.createVerticalBox();
				switch(align) {
				case LEFT:		box.setAlignmentX(0); break;
				case CENTER:	box.setAlignmentX(0.5f); break;
				case RIGHT:		box.setAlignmentX(1); break;
				}
				for(Component component: getComponents()) {
					JComponent jc = component.getComponent(ui);
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
		public JComponent getComponent(UI ui) {
			if(axis == HORIZONTAL)
				return new javax.swing.Box.Filler(ZERO, HFILL, HFILL);
			else
				return new javax.swing.Box.Filler(ZERO, VFILL, VFILL);
		}
		
	}

	@Override
	public void setAlign(int align) {
		this.align = align;
	}
}
