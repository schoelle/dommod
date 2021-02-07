package de.fams.dommod;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import de.fams.dommod.Argument.Type;

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
		if (id.isEmpty()) {
			return getType().toString() + ": " + getName();
		}
		return getType().toString() + "(" + id.get() + "): " + getName();
	}

	public List<Reference> getReferences() {
		List<Reference> result = Lists.newArrayList();
		for (Command cmd: commands) {
			Reference ref = cmd.reference();
			if (ref != null) {
				result.add(ref);
			}
		}
		return result;
	}
}
