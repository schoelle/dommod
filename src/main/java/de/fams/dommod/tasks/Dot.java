package de.fams.dommod.tasks;

import de.fams.dommod.Definition;
import de.fams.dommod.DmFile;
import de.fams.dommod.ReferenceCommand;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

public class Dot extends OutputFileTask {
	
	public String nodeName(Definition def) {
		Optional<Integer> id = def.getId();
		if (id.isEmpty()) {
			if (def.getName() == null) {
				return def.getType().toString() + ":???";
			} else {
				return def.getName().trim();
			}
		}
		return def.getType().toString() + ":" + id.get();
	}

	@Override
	public void doOutput(DmFile mod, BufferedWriter output) throws IOException {
		output.write("digraph modgraph {\n");
		output.write("  node [shape=record];\n");
		output.write("  overlap=false;\n");
		for (Definition def: mod.definitions) {
			String label;
			if (def.getId().isEmpty() || def.getName() == null) {
				label = nodeName(def);
			} else {
				label = nodeName(def) + "\\n" + def.getName();
			}
			output.write(String.format("  \"%s\" [label=\"%s\"];\n", nodeName(def), label));
		}
		for (Definition def: mod.definitions) {
			for (ReferenceCommand ref : def.getReferences()) {
				for (Definition target: ref.getTargets()) {
					if (target != def) {
						output.write(String.format("  \"%s\" -> \"%s\" [label=\"#%s\"];\n", nodeName(def), nodeName(target), ref.command.name));
					}
				}
			}
		}
		output.write("}\n");
	}
	
	@Override
	public String description() {
		return "Creates a .dot file to be used by Graphviz.";
	}

}
