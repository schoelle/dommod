package de.fams.dommod.tasks;

import de.fams.dommod.Argument;
import de.fams.dommod.Command;
import de.fams.dommod.DmFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Output the mod as read in
 */
public class Output implements Task {

    @Override
    public void process(DmFile mod, List<String> arguments) {
        BufferedWriter output;
        try {
            if (arguments.size() > 1) {
                System.out.println("output takes only zero or one argument (the output file name)");
                return;
            } if (arguments.isEmpty()) {
                output = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                output = new BufferedWriter(new PrintWriter(arguments.get(0)));
            }
            writeOutput(mod, output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeOutput(DmFile mod, BufferedWriter output) throws IOException {
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
