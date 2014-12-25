package elf.ui;

import java.io.PrintStream;

/**
 * Monitor for textual file output.
 * @author casse
 */
public class TextMonitor implements Monitor {
	private PrintStream stream;
	private boolean succeeded = true;
	
	/**
	 * Monitor on standard output.
	 */
	public TextMonitor() {
		stream = System.err;
	}
	
	/**
	 * Monitor on the given print stream.
	 * @param stream	Stream to output to.
	 */
	public TextMonitor(PrintStream stream) {
		this.stream = stream;
	}
	
	@Override
	public void info(String message) {
		stream.println("INFO: " + message);
	}

	@Override
	public void warn(String message) {
		stream.println("WARNING: " + message);
	}

	@Override
	public void error(String message) {
		stream.println("ERROR: " + message);
		succeeded = false;
	}

	@Override
	public void panic(String message) {
		stream.println("FATAL: " + message);
		System.exit(1);
	}

	@Override
	public void begin(String title) {
		stream.println("\nSESSION: " + title);
		succeeded = true;
	}

	@Override
	public boolean end() {
		stream.println();
		return succeeded;
	}

}
