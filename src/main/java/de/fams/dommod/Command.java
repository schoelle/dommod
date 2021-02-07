package de.fams.dommod;

import java.util.List;

import de.fams.dommod.Argument.Type;

/**
 * A single instruction (#keyword) in a .dm file
 */
public class Command {

	public int line;
	public String prefixComment;
	public String name;
	public List<Argument> arguments;
	public String lineComment;
	
	public Command(int line, String prefixComment, String name, List<Argument> arguments, String lineComment) {
		this.line = line;
		this.prefixComment = prefixComment;
		this.name = name;
		this.arguments = arguments;
		this.lineComment = lineComment;
	}
	
	public Reference reference() {
		RefType type = StaticTables.REFTYPE_BY_NAME.get(name);
		if (type == null) {
			return null;
		}
		if (arguments.isEmpty()) {
			return null;
		}
		Argument arg = arguments.get(0);
		if (arg.type == Type.NUMBER) {
			return new Reference(type, Integer.valueOf(arg.value), null);
		}
		return new Reference(type, null, arg.value);
	}
	
	@Override
	public String toString() {
		return String.format("Command(Line: %d, #%s, %s)", line, name, arguments);
	}
	
}
