package de.fams.dommod.tasks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fams.dommod.*;

import java.util.List;
import java.util.Map;

public class Check implements Task {

	@Override
	public String description() {
		return "Check the mod file for inconsistencies.";
	}
	
	public List<String> warnings = Lists.newArrayList();
	public List<String> errors = Lists.newArrayList();

	@Override
	public void process(DmFile mod, List<String> arguments) {
		if (!arguments.isEmpty()) {
			System.out.println("check does not take any arguments");
			return;
		}
		check(mod);
		if (warnings.isEmpty() && errors.isEmpty()) {
			System.out.println("Everything look ok.");
		} else {
			for (String s: warnings) {
				System.out.println(String.format("  WARNING: %s", s));
			}
			for (String s: errors) {
				System.out.println(String.format("  ERROR: %s", s));
			}
			System.out.println(String.format("Total of %d errors and %d warnings found.", errors.size(), warnings.size()));
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
