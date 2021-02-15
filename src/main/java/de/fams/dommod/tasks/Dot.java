package de.fams.dommod.tasks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import de.fams.dommod.Definition;
import de.fams.dommod.DmFile;
import de.fams.dommod.Reference;

public class Dot implements Task {
	
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
	
	public void writeGraphviz(DmFile mod, BufferedWriter output) throws IOException {
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
			for (Reference ref : def.getReferences()) {
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
	public void process(DmFile mod, List<String> arguments) {
		BufferedWriter output;
		try {
			if (arguments.size() > 1) {
				System.out.println("dot takes only zero or one argument (the output file name)");
				return;
			} if (arguments.isEmpty()) {
				output = new BufferedWriter(new OutputStreamWriter(System.out));
			} else {
				output = new BufferedWriter(new PrintWriter(arguments.get(0)));
			}
			writeGraphviz(mod, output);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String description() {
		return "Creates a .dot file to be used by Graphviz.";
	}

}
