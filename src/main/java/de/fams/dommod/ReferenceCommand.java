package de.fams.dommod;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class ReferenceCommand {

	public final Reference reference;
	public final Command command;
	public final DmFile dmFile;
	
	public ReferenceCommand(Reference reference, DmFile dmFile, Command command) {
		this.reference = reference;
		this.command = command;
		this.dmFile = dmFile;
	}
	
	@Override
	public String toString() {
		return reference.toString();
	}

	private List<Definition> lastMatch;

	public RefStatus status() {
		List<Definition> defs = dmFile.definitions;
		lastMatch = defs.stream().filter(d -> reference.matches(d)).collect(Collectors.toList());
		if (reference.isInvalid()) {
			return RefStatus.INVALID;
		}
		if (lastMatch.size() > 1 && !reference.isById()) {
			return RefStatus.DUPLICATE;
		}
		if (!lastMatch.isEmpty()) {
			return RefStatus.INTERNAL;
		}
		if (reference.isBuiltIn()) {
			return RefStatus.BUILT_IN;
		}
		if (reference.isById()) {
			return RefStatus.EXTERNAL;
		}
		return RefStatus.NOT_FOUND;
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
