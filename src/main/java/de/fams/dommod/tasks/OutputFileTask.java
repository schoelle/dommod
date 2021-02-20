package de.fams.dommod.tasks;

import de.fams.dommod.DmFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public abstract class OutputFileTask implements Task {

    @Override
    public void process(DmFile mod, List<String> arguments) {
        BufferedWriter output;
        try {
            if (arguments.size() > 1) {
                System.out.println("command takes only zero or one argument (the output file name)");
                return;
            } if (arguments.isEmpty()) {
                output = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                output = new BufferedWriter(new PrintWriter(arguments.get(0)));
            }
            doOutput(mod, output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void doOutput(DmFile mod, BufferedWriter writer) throws IOException;

}
