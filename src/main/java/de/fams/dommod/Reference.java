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
	
	public boolean matches(Definition def) {
		if (argument.type == Type.NUMBER) {
			int nr = Integer.valueOf(argument.value);
			return def.getType() == type && def.getId().isPresent() && nr == def.getId().get(); 
		} else {
			String name = argument.value.toLowerCase();
			return def.getType() == type && def.getName() != null && name.equals(def.getName().toLowerCase());
		}
	}
	
	public RefStatus status() {
		List<Definition> defs = dmFile.definitions;
		lastMatch = defs.stream().filter(d -> matches(d)).collect(Collectors.toList());
		if (argument.type == Type.NUMBER) {
			if (!lastMatch.isEmpty()) {
				return RefStatus.INTERNAL;
			}
			int nr = Integer.valueOf(argument.value);
			if (nr < type.minId && nr > type.maxId) {
				return RefStatus.INVALID;
			}
			if (nr < type.minModId) {
				return RefStatus.BUILT_IN;
			}
			return RefStatus.EXTERNAL;
		} else {
			if (lastMatch.isEmpty()) {
				if (InspectorData.isBuiltIn(type, argument.value)) {
					return RefStatus.BUILT_IN;
				}
				return RefStatus.NOT_FOUND;
			}
			if (lastMatch.size() > 1) {
				return RefStatus.DUPLICATE;
			}
			return RefStatus.INTERNAL;
		}
	}
	
	public List<Definition> getTargets() {
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
