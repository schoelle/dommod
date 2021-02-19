package de.fams.dommod.tasks;

import de.fams.dommod.DmFile;

import java.util.List;

public interface Task {
	void process(DmFile mod, List<String> arguments);
	String description();
}
