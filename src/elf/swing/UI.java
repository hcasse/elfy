/*
 * Elfy library
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

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import elf.ui.View;
import elf.ui.meta.Entity;

/**
 * Swing UI implementation.
 * @author casse
 */
public class UI implements elf.ui.UI {
	private Hashtable<elf.ui.Icon, WeakReference<Icon> > icons = new Hashtable<elf.ui.Icon, WeakReference<Icon> >(); 
	private Hashtable<Task, TaskTimer> timers = new Hashtable<Task, TaskTimer>();
	private Icon broken;
	
	@Override
	public View makeView(Entity entity) {
		return new elf.swing.View(this, entity);
	}

	@Override
	public void start(Task task) {
		TaskTimer timer = timers.get(task);
		if(timer == null)
			timer = new TaskTimer(task);
		if(!task.isPeriodic())	// fix for one-shot timers
			timer.stop();	
		timer.start();
	}

	@Override
	public void stop(Task task) {
		TaskTimer timer = timers.get(task);
		if(timer != null)
			timer.stop();
	}
	
	private class TaskTimer implements ActionListener {
		private Timer timer;
		private Task task;
		
		public TaskTimer(Task task) {
			this.task = task;
		}
		
		public void start() {
			if(timer != null)
				return;
			timer = new Timer((int)task.getPeriod(), this);
			timer.setRepeats(task.isPeriodic());
			timers.put(task, this);
			timer.start();
		}
		
		public void stop() {
			if(timer == null)
				return;
			timer.stop();
			timers.remove(task);
			timer = null;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			task.run();
		}
		
	}

	/**
	 * Get the broken icon.
	 * @return	Icon for broken image.
	 */
	public Icon getBroken() {
		if(broken == null) {
			try {
				Image image = ImageIO.read(elf.ui.Icon.BROKEN.getURL());
				broken = new Icon.Image(image);			
			} catch (IOException e) {
				System.err.println("ERROR: no broken icon");
			}
			icons.put(elf.ui.Icon.BROKEN, new WeakReference<Icon>(broken));
		}
		return broken;
	}

	/**
	 * Get Swing icon for an UI icon.
	 * @param icon	Icon to get.
	 * @return		Swing icon.
	 */
	Icon getIcon(elf.ui.Icon icon) {
		if(icon == null)
			return null;
		Icon sicon = null;
		WeakReference<Icon> ref = icons.get(icon);
		if(ref != null)
			sicon = ref.get();
		if(sicon == null) {
			try {
				Image image = ImageIO.read(icon.getURL());
				sicon = new Icon.Image(image);
			} catch (MalformedURLException e) {
				sicon = getBroken();
			} catch (IOException e) {
				sicon = getBroken();
			}
			icons.put(icon, new WeakReference<Icon>(sicon));	
		}
		return sicon;
	}

	@Override
	public Color getColor(String rgba) {
		return new Color(java.awt.Color.decode(rgba));
	}

	@Override
	public Color getColor(int r, int g, int b) {
		return new Color(new java.awt.Color(r, g, b));
	}

	@Override
	public Color getColor(int r, int g, int b, int a) {
		return new Color(new java.awt.Color(r, g, b, a));
	}
	
	/**
	 * Color representation for SWING UI.
	 * @author casse
	 */
	public class Color implements elf.ui.UI.Color {
		java.awt.Color color;
		
		public Color(java.awt.Color color) {
			this.color = color;
		}
		
		/**
		 * Get the native color.
		 * @return	Native color.
		 */
		public java.awt.Color getColor() {
			return color;
		}
		
		@Override
		public String getRGBA() {
			return String.format("#%2x%2x%2x%2x", getRed(), getGreen(), getBlue(), getAlpha());
		}

		@Override
		public String getRGB() {
			return String.format("#%2x%2x%2x", getRed(), getGreen(), getBlue());
		}

		@Override
		public int getRed() {
			return color.getRed();
		}

		@Override
		public int getGreen() {
			return color.getGreen();
		}

		@Override
		public int getBlue() {
			return color.getBlue();
		}

		@Override
		public int getAlpha() {
			return color.getAlpha();
		}
		
	}
}
