package de.fams.dommod.tasks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fams.dommod.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Check extends OutputFileTask {

	@Override
	public String description() {
		return "Check the mod file for inconsistencies.";
	}
	
	public List<String> warnings = Lists.newArrayList();
	public List<String> errors = Lists.newArrayList();

	@Override
	public void doOutput(DmFile mod, BufferedWriter writer) throws IOException {
		check(mod);
		if (warnings.isEmpty() && errors.isEmpty()) {
			writer.write("Everything look ok.\n");
		} else {
			for (String s: warnings) {
				writer.write(String.format("  WARNING: %s\n", s));
			}
			for (String s: errors) {
				writer.write(String.format("  ERROR: %s\n", s));
			}
			writer.write(String.format("Total of %d errors and %d warnings found.\n", errors.size(), warnings.size()));
		}
	}

	public void check(DmFile mod) {
		errors.clear();
		warnings.clear();
		Map<String, Integer> idsUsed = Maps.newHashMap();
		for (Definition d: mod.definitions) {
			if (d.getId().isPresent()) {
				String id = d.getSelfReference().toString();
				if (idsUsed.containsKey(id)) {
					warnings.add(String.format("Duplicate definition of %s (line %d vs %d)", id,
							idsUsed.get(id), d.commands.get(0).line));
				} else {
					idsUsed.put(id, d.commands.get(0).line);
				}
			}	
		}
		for (Command cmd: mod.commands) {
			ReferenceCommand ref = cmd.getReferenceCommand();
			if (ref != null) {
				RefStatus status = ref.status();
				if (status.isBad()) {
					errors.add(status.getBadText()+ ": " + ref.toString());
				} else {
					if (status == RefStatus.EXTERNAL) {
						warnings.add("External reference detected: " + ref.toString());
					}
				}
			}
		}
	}
}
