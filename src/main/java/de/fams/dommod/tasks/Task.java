package de.fams.dommod.tasks;

import java.util.List;

import de.fams.dommod.DmFile;

public interface Task {
	void process(DmFile mod, List<String> arguments);
	String description();
}
