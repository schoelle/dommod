package de.fams.dommod;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.fams.dommod.Argument.Type;

public class Reference {

	public final EntityType type;
	public final Argument argument;
	public final Command command;
	public final DmFile dmFile;
	
	public Reference(EntityType type, DmFile dmFile, Command command, Argument argument) {
		this.type = type;
		this.argument = argument;
		this.command = command;
		this.dmFile = dmFile;
	}
	
	@Override
	public String toString() {
		return type + "(" + argument.toString() + ")";
	}
	
	private List<Definition> lastMatch;
	
	public RefStatus status() {
		List<Definition> defs = dmFile.getDefinitions();
		if (argument.type == Type.NUMBER) {
			int nr = Integer.valueOf(argument.value);
			if (nr < type.minId && nr > type.maxId) {
				return RefStatus.INVALID;
			}
			if (nr < type.minModId) {
				return RefStatus.BUILT_IN;
			}
			lastMatch = defs.stream().filter(d -> d.getId().isPresent() && nr == d.getId().get()).collect(Collectors.toList());
			if (lastMatch.isEmpty()) {
				return RefStatus.EXTERNAL;
			}
			return RefStatus.INTERNAL;
		} else {
			String name = argument.value;
			lastMatch = defs.stream().filter(d -> name.equals(d.getName())).collect(Collectors.toList());
			if (lastMatch.isEmpty()) {
				return RefStatus.NOT_FOUND;
			}
			if (lastMatch.size() > 1) {
				return RefStatus.DUPLICATE;
			}
			return RefStatus.INTERNAL;
		}
	}
	
	public List<Definition> getTarget() {
		switch(status()) {
		case BUILT_IN:
		case NOT_FOUND:
		case INVALID:
		case EXTERNAL:
			return Lists.newArrayList();
		default:
			return lastMatch;
		}
	}
	
}
