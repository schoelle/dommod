package de.fams.dommod;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import de.fams.dommod.Parser.ParsingException;
import de.fams.dommod.tasks.Task;
import de.fams.dommod.tasks.TaskRegistry;

public class Main {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Syntax: dommod modname.(dm|json) task [args ...]");
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
		if (dmFileName.toLowerCase().endsWith(".dm")) {
			dmFile = readDmFile(dmFileName);
		} else {
			dmFile = readJsonFile(dmFileName);
		}
		task.process(dmFile, arguments);
	}

	private static Command buildCommand(JsonNode node) {
		String name = node.get("name").textValue();
		JsonNode prefixNode = node.get("prefix");
		String prefix = prefixNode == null ? null : prefixNode.textValue();
		JsonNode commentNode = node.get("comment");
		String comment = commentNode == null ? null : commentNode.textValue();
		List<Argument> arguments = Lists.newArrayList();
		int argCount = 1;
		JsonNode numNode = node.get(String.format("arg%dnum", argCount));
		JsonNode strNode = node.get(String.format("arg%dstr", argCount));
		while (numNode != null || strNode != null) {
			if (numNode != null) {
				arguments.add(new Argument(Argument.Type.NUMBER, numNode.textValue()));
			} else {
				arguments.add(new Argument(Argument.Type.STRING, strNode.textValue()));
			}
			argCount++;
			numNode = node.get(String.format("arg%dnum", argCount));
			strNode = node.get(String.format("arg%dstr", argCount));
		}
		return new Command(1, prefix, name, arguments, comment); // we lose the line numbers
	}

	private static DmFile readJsonFile(String dmFileName) {
		File dmFile = new File(dmFileName);
		List<Command> commands = Lists.newArrayList();
		String lastComment = "";
		try {
			ArrayNode data = (ArrayNode) MAPPER.readTree(dmFile);
			for (JsonNode node: data) {
				ObjectNode oNode = (ObjectNode) node;
				if (oNode.has("tailComment")) {
					lastComment = oNode.get("tailComment").textValue();
					continue;
				}
				Command cmd = buildCommand(oNode);
				commands.add(cmd);
				if (oNode.has("content")) {
					ArrayNode content = (ArrayNode) oNode.get("content");
					for (JsonNode cNode: content) {
						Command ccmd = buildCommand(cNode);
						commands.add(ccmd);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return new DmFile(commands, lastComment);
	}

	private static DmFile readDmFile(String dmFileName) {
		try {
			Parser parser = new Parser(dmFileName);
			return parser.parse();
		} catch (ParsingException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.exit(1);
			return null;
		}
	}

}
