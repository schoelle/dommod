package de.fams.dommod.tasks;

import de.fams.dommod.Argument;
import de.fams.dommod.Command;
import de.fams.dommod.DmFile;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Output the mod as read in
 */
public class Output extends OutputFileTask {

    @Override
    public void doOutput(DmFile mod, BufferedWriter output) throws IOException {
        for (Command cmd : mod.commands) {
            if (cmd.prefixComment != null) {
                output.write(cmd.prefixComment);
            }
            output.write("#");
            output.write(cmd.name);
            for (Argument arg: cmd.arguments) {
                output.write(" ");
                if (arg.type == Argument.Type.STRING) {
                    output.write("\"");
                }
                output.write(arg.value);
                if (arg.type == Argument.Type.STRING) {
                    output.write("\"");
                }
            }
            if (cmd.lineComment != null) {
                output.write(" " + cmd.lineComment);
            }
            output.write("\n");
        }
        if (mod.tailComment != null) {
            output.write(mod.tailComment);
        }
    }

    @Override
    public String description() {
        return "Output mod file in .dm format";
    }
}
