package de.fams.dommod;

/**
 * Argument to a command (#keyword) in a .dm file.
 * 
 * Most commands have 0 or 1 argument, a few have more than one. Commands referencing other definitions
 * have normally just one argument.
 */
public class Argument {

	public static enum Type {
		NUMBER,
		STRING
	}
	
	public final Type type;
	public final String value;
	
	public Argument(Type type, String value) {
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString() {
		if (type == Type.STRING) {
			return String.format("\"%s\"", value);			
		}
		return value;
	}
	
}
