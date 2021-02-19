package de.fams.dommod;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DmFile {
	
	public final List<Command> commands;
	public final String tailComment;
	public final List<Command> outsideCommands = Lists.newArrayList();
	public  List<Definition> definitions = null;

	public DmFile(List<Command> commands, String tailComment) {
		this.commands = commands;
		this.tailComment = tailComment;
 	}

	@Override
	public String toString() {
		return String.format("DmFile(commands=%s,definitions=%s,outsideCommands=%s)", commands.size(), definitions.size(), outsideCommands.size());
	}

	public void rebuildDefinitions() {
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

	public Command getGlobalCommand(String name) {
		return commands.stream().filter(c -> c.definition == null && c.name.toLowerCase().equals(name.toLowerCase())).findAny().orElse(null);
	}


	public String getModInfo(String name) {
		Command cmd = getGlobalCommand(name);
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

	public void removeCommands(Collection<Command> cmds) {
		commands.removeAll(cmds);
		rebuildDefinitions();
	}

}
