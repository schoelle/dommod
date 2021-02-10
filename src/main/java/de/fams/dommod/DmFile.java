package de.fams.dommod;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class DmFile {
	
	public final List<Command> commands;
	public final String tailComment;
	public final List<Command> outsideCommands = Lists.newArrayList();

	private List<Definition> definitions = null;

	public DmFile(List<Command> commands, String tailComment) {
		this.commands = commands;
		this.tailComment = tailComment;
 	}

	public List<Definition> getDefinitions() {
		if (definitions == null) {
			buildDefinitions();
		}
		return definitions;
	}

	@Override
	public String toString() {
		return String.format("DmFile(commands=%s,definitions=%s,outsideCommands=%s)", commands.size(), definitions.size(), outsideCommands.size());
	}

	private void buildDefinitions() {
		Preconditions.checkState(definitions == null);
		definitions = Lists.newArrayList();
		Iterator<Command> cmdIterator = commands.iterator();		
		while (cmdIterator.hasNext()) {
			Command cmd = cmdIterator.next();
			EntityType startType = StaticTables.STARTCMD_FOR_NAME.get(cmd.name);
			if (startType == null) {
				outsideCommands.add(cmd);
			} else {
				List<Command> defCommands = Lists.newArrayList();
				defCommands.add(cmd);
				while(!cmd.name.equals("end")) {
					if (!cmdIterator.hasNext()) {
						throw new RuntimeException("#end missing");
					}
					cmd = cmdIterator.next();
					defCommands.add(cmd);
				}
				Definition def = new Definition(defCommands);
				definitions.add(def);
				for (Command c: defCommands) {
					c.setDefinition(def);
				}
			}	
		}
	}

	public List<Command> getGlobalCommands() {
		return commands.stream().filter(c -> c.definition == null).collect(Collectors.toList());
	}

	public String getModInfo(String name) {
		String lName = name.toLowerCase();
		Command cmd = commands.stream().filter(c -> c.definition == null && c.name.toLowerCase().equals(lName)).findAny().orElse(null);
		if (cmd == null || cmd.arguments.isEmpty()) {
			return null;
		}
		return cmd.arguments.get(0).value;
	}

	public String getModName() {
		return getModInfo("modname");
	}

	public String getDescription() {
		return getModInfo("description");
	}

	public String getIcon() {
		return getModInfo("icon");
	}

	public String getVersion() {
		return getModInfo("version");
	}

	public String getDomVersion() {
		return getModInfo("domversion");
	}

	public boolean oldNationsAreDisabled() {
		return commands.stream().anyMatch(c -> c.name.toLowerCase().equals("disableoldnations"));
	}


}
