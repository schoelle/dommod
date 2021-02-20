package de.fams.dommod;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Optional;

/**
 * A single instruction (#keyword) in a .dm file
 */
public class Command {

	public int line;
	public String prefixComment;
	public String name;
	public List<Argument> arguments;
	public String lineComment;
	public DmFile dmFile = null;
	public Definition definition = null;

	public Command(int line, String prefixComment, String name, List<Argument> arguments, String lineComment) {
		this.line = line;
		this.prefixComment = prefixComment;
		this.name = name;
		this.arguments = arguments;
		this.lineComment = lineComment;
	}

	public void replaceBy(Command cmd) {
		cmd.setDefinition(definition);
		cmd.setDmFile(dmFile);
		dmFile.commands.set(dmFile.commands.indexOf(this), cmd);
		if (definition != null) {
			definition.commands.set(definition.commands.indexOf(this), cmd);
		}
	}

	public void setDmFile(DmFile dmFile) {
		Preconditions.checkState(this.dmFile == null);
		this.dmFile = dmFile;
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public ReferenceCommand getReferenceCommand() {
		Reference ref = getReference();
		if (ref == null) {
			return null;
		}
		return new ReferenceCommand(ref, dmFile, this);
	}

	public Reference getReference() {
		EntityType type = StaticTables.REFTYPE_BY_NAME.get(name);
		if (type == null) {
			return null;
		}
		if (arguments.isEmpty()) {
			return null;
		}
		Argument arg = arguments.get(0);
		if (arg.type == Argument.Type.NUMBER) {
			return new NumericReference(type, Integer.parseInt(arg.value));
		}
		return new StringReference(type, arg.value);
	}


	@Override
	public String toString() {
		return String.format("Command(Line: %d, #%s, %s)", line, name, arguments);
	}

	public boolean hasName(String name) {
		if (name == null) {
			return false;
		}
		return this.name.equalsIgnoreCase(name);
	}

	public Optional<Integer> getNumericArgument() {
		if (arguments.isEmpty()) {
			return Optional.empty();
		}
		Argument arg = arguments.get(0);
		if (arg.type == Argument.Type.STRING) {
			return Optional.empty();
		}
		try {
			return Optional.of(Integer.parseInt(arg.value));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
}
