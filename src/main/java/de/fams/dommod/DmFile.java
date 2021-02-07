package de.fams.dommod;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class DmFile {
	
	public final List<Command> commands;
	public final String tailComment;
	public final List<Command> outsideCommands = Lists.newArrayList();

	private List<Definition> definitions = null;

	public DmFile(List<Command> commands, String tailComment) {
		this.commands = commands;
		this.tailComment = tailComment;
		buildDefinitions();
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
				definitions.add(new Definition(defCommands));
			}	
		}
	}
}
