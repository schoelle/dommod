package de.fams.dommod;

import com.google.common.collect.Lists;
import de.fams.dommod.Argument.Type;

import java.util.List;
import java.util.Optional;

/**
 * Definition of an entity in a .dm file, normally started by '#selectTYPE' or '#newTYPE'
 * and finished by the '#end' statement.
 */
public class Definition {

	public final List<Command> commands;
	
	public Definition(List<Command> commands) {
		this.commands = commands;
	}
	
	public EntityType getType() {
		return StaticTables.REFTYPE_BY_NAME.get(commands.get(0).name);
	}
	
	public Optional<Integer> getId() {
		Command cmd = commands.get(0);
		if (cmd.arguments.isEmpty()) {
			return Optional.empty();
		}
		Argument arg = cmd.arguments.get(0);
		if (arg.type == Type.NUMBER) {
			return Optional.of(Integer.valueOf(arg.value));
		}
		return Optional.empty();
	}

	public Reference getSelfReference() {
		Command cmd = commands.get(0);
		if (cmd.arguments.isEmpty()) {
			return null;
		}
		Argument arg = cmd.arguments.get(0);
		if (arg.type != Type.NUMBER) {
			return null;
		}
		return new NumericReference(getType(), Integer.parseInt(arg.value));
	}

	public String getName() {
		for (Command cmd: commands) {
			if (cmd.name.equals("name")) {
				return cmd.arguments.get(0).value;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		Optional<Integer> id = getId();
		String name = getName();
		if (name == null) {
			name = "???";
		}
		if (id.isEmpty()) {
			return getType().word() + ": " + name.trim();
		}
		return getType().word() + "(" + id.get() + "): " + name.trim();
	}

	public List<ReferenceCommand> getReferences() {
		List<ReferenceCommand> result = Lists.newArrayList();
		for (Command cmd: commands) {
			ReferenceCommand ref = cmd.getReferenceCommand();
			if (ref != null) {
				result.add(ref);
			}
		}
		return result;
	}

}
