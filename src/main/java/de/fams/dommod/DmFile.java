package de.fams.dommod;

import com.google.common.collect.Lists;

import java.sql.Ref;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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

	public UsageSet getUsage() {
		UsageSet usageSet = new UsageSet();
		for (Definition def : definitions) {
			NumericReference ref = def.getSelfReference();
			if (ref != null) {
				usageSet.add(ref);
			}
		}
		for (Command cmd : commands) {
			if (cmd.name.equalsIgnoreCase("montag")) {
				Optional<Integer> id = cmd.getNumericArgument();
				if (id.isPresent()) {
					usageSet.addMontag(id.get());
				}
			}
		}
		return usageSet;
	}

    public void rename(NumericReference from, NumericReference to) {
		for (Command cmd: commands) {
			Reference ref = cmd.getReference();
			if (ref != null && ref instanceof NumericReference) {
				NumericReference numRef = (NumericReference) ref;
				if (numRef.equals(from)) {
					cmd.arguments.set(0, new Argument(Argument.Type.NUMBER, Integer.toString(to.getId())));
				}
			}
		}
    }

    public void renameMonTag(int from, int to) {
		for (Command cmd: commands) {
			if (cmd.name.equalsIgnoreCase("montag")) {
				Optional<Integer> arg = cmd.getNumericArgument();
				if (arg.isPresent() && arg.get() == from) {
					cmd.arguments.set(0, new Argument(Argument.Type.NUMBER, Integer.toString(to)));
				}
			}
			Reference ref = cmd.getReference();
			if (ref != null && ref instanceof NumericReference) {
				NumericReference numRef = (NumericReference) ref;
				if (numRef.getEntityType() == EntityType.MONSTER && numRef.getId() == -from) {
					cmd.arguments.set(0, new Argument(Argument.Type.NUMBER, Integer.toString(-to)));
				}
			}
		}
	}
}
