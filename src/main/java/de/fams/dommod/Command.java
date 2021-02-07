package de.fams.dommod;

import java.util.List;

/**
 * A single instruction (#keyword) in a .dm file
 */
public class Command {

	public int line;
	public String prefixComment;
	public String name;
	public List<Argument> arguments;
	public String lineComment;
	public DmFile dmFile;
	
	public Command(int line, String prefixComment, String name, List<Argument> arguments, String lineComment) {
		this.line = line;
		this.prefixComment = prefixComment;
		this.name = name;
		this.arguments = arguments;
		this.lineComment = lineComment;
	}
	
	public void setDmFile(DmFile dmFile) {
		this.dmFile = dmFile;
	}
	
	public Reference reference() {
		EntityType type = StaticTables.REFTYPE_BY_NAME.get(name);
		if (type == null) {
			return null;
		}
		if (arguments.isEmpty()) {
			return null;
		}
		return new Reference(type, dmFile, this, arguments.get(0));
	}
	
	@Override
	public String toString() {
		return String.format("Command(Line: %d, #%s, %s)", line, name, arguments);
	}
	
}
