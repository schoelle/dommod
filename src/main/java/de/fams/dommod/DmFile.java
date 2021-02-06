package de.fams.dommod;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class DmFile {
	
	public final List<Command> commands;
	public final String tailComment;
	public final List<Command> outsideCommands = Lists.newArrayList();
	public final List<Definition> definitions = Lists.newArrayList();
	
	public DmFile(List<Command> commands, String tailComment) {
		this.commands = commands;
		this.tailComment = tailComment;
		buildDefinitions();
	}

	public void buildDefinitions() {
		Iterator<Command> cmdIterator = commands.iterator();		
		while (cmdIterator.hasNext()) {
			Command cmd = cmdIterator.next();
			RefType startType = StaticTables.STARTCMD_FOR_NAME.get(cmd.name);
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
	
	@Override
	public String toString() {
		return String.format("DmFile(commands=%s,definitions=%s,outsideCommands=%s)", commands.size(), definitions.size(), outsideCommands.size());
	}
}
