package de.fams.dommod;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.fams.dommod.Parser.ParsingException;
import de.fams.dommod.tasks.Task;
import de.fams.dommod.tasks.TaskRegistry;

public class Main {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Syntax: dommod modname.dm task [args ...]");
			System.out.println("Tasks:");
			List<String> taskNames = Lists.newArrayList(TaskRegistry.TASK_TABLE.keySet());
			Collections.sort(taskNames);
			for (String name: taskNames) {
				System.out.println(String.format("  %-14s %s", name, TaskRegistry.TASK_TABLE.get(name).description()));
			}
			System.exit(1);
			return;
		}
		String dmFileName = args[0];
		String taskName = args[1];
		Task task = TaskRegistry.TASK_TABLE.get(taskName);
		if (task == null) {
			System.out.println("ERROR: Unknown task " + taskName);
			System.exit(1);
			return;			
		}
		
		List<String> arguments  = Lists.newArrayList(args).subList(2, args.length);
		DmFile dmFile;
		try {
			Parser parser = new Parser(dmFileName);
			dmFile = parser.parse();
		} catch (ParsingException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.exit(1);
			return;
		}
		task.process(dmFile, arguments);
	}

}
