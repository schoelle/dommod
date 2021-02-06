package de.fams.dommod;

import java.util.List;

import com.google.common.collect.Lists;

import de.fams.dommod.Parser.ParsingException;

public class Main {

	public static void main(String[] args) {
		if (args.length < 2) {
			Print.error("Arguments: modname.dm command args ...");
			System.exit(1);
			return;
		}
		String dmFileName = args[0];
		String command = args[1];
		List<String> arguments  = Lists.newArrayList(args).subList(2, args.length);
		Print.info("DM File:%s", dmFileName);
		Print.info("Command: %s", command);
		Print.info("Arguments: %s", arguments);
		DmFile dmFile;
		try {
			Parser parser = new Parser(dmFileName);
			dmFile = parser.parse();
		} catch (ParsingException e) {
			Print.error("%s", e.getMessage());
			System.exit(1);
			return;
		}
		Print.info("%s", dmFile);
		for (Definition d: dmFile.definitions) {
			Print.info("%s", d);
			for (Command cmd : d.commands) {
				Reference r = cmd.reference();
				if (r != null) {
					Print.info("%s", r);
				}
			}
		}
	}

}
