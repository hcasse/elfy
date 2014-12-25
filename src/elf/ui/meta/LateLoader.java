package elf.ui.meta;

import java.io.IOException;

import elf.ui.Monitor;

/**
 * Implement late-loading model: load of the entity
 * is only performed when needed. When the model is required,
 * the get() method is called. If the object is already loaded, it is
 * returned. Else, the abstract load() method is called to get the object.
 * If the load is successful, the obtained object is returned.
 * Else an error is displayed using the given monitor and the default object
 * is returned. This default value must be designed  to block following access
 * to the object.
 * @author casse
 */
public abstract class LateLoader<T, I> {
	public static final int
		INIT = 0,
		LOADED = 1,
		FAILED = 2;
	private Context<T> context;
	private T obj;
	private I id;
	private int state = INIT;
	
	/**
	 * Build a late loader.
	 * @param context	Current context.
	 * @param id		Object identifier.
	 */
	public LateLoader(Context<T> context, I id) {
		this.context = context;
		this.id = id;
	}
	
	/**
	 * Set the current value.
	 * @param obj	Set value.
	 */
	public void set(T obj) {
		this.obj = obj;
		state = LOADED;
	}
	
	/**
	 * Check for load.
	 */
	private void check() {
		if(state != INIT)
			return;
		try {
			set(load(id));
		} catch (IOException e) {
			obj = context.getDefault();
			context.getMonitor().error("cannot load " + id + ": " + e.getLocalizedMessage());
			state = FAILED;
		}		
	}
	
	/**
	 * Test if the load has been successful (possibly causing the load itself).
	 * @return	True if the load is successful, false else.
	 */
	public boolean isReady() {
		check();
		return state == LOADED;
	}
	
	/**
	 * Get the identifier of the object.
	 * @return	Object identifier.
	 */
	public I getID() {
		return id;
	}
	
	/**
	 * Called to load the object.
	 * @param id	Identifier of the object to load.
	 * @return		Loaded object.
	 */
	public abstract T load(I id) throws IOException;
	
	/**
	 * Get the late-loaded object.
	 * @return	Late-loaded object.
	 */
	public T get() {
		if(state == INIT)
			check();
		return obj;
	}
	
	/**
	 * Context allows to group some facilities for late-loading.
	 * @author casse
	 */
	public static class Context<T> {
		private Monitor mon = Monitor.NULL;
		private T def;
		
		public Context(T def) {
			this.def = def;
		}
		
		public Context(T def, Monitor mon) {
			this.def = def;
			this.mon = mon;
		}
		
		/**
		 * Get the context monitor.
		 * @return	Current monitor.
		 */
		public Monitor getMonitor() {
			return mon;
		}
		
		/**
		 * Get the default value.
		 * @return	Default value.
		 */
		public T getDefault() {
			return def;
		}
	}	
}
