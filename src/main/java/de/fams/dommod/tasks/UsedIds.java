package de.fams.dommod.tasks;

import de.fams.dommod.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class UsedIds extends OutputFileTask {

    @Override
    public void doOutput(DmFile mod, BufferedWriter writer) throws IOException {
        UsageSet usage = mod.getUsage();
        for (NumericReference ref : usage.getReferences()) {
            List<Definition> def = ref.findIn(mod);
            if (ref.isBuiltIn()) {
                continue;
            }
            if (def.size() == 1 && def.get(0).getName() != null) {
                writer.write(String.format("%s -- %s\n", ref, def.get(0).getName()));
            } else {
                writer.write(ref.toString() + "\n");
            }
        }
        for (int id : usage.getMontagUsage()) {
            writer.write(String.format("MONTAG(%d)\n", id));
        }
    }

    @Override
    public String description() {
        return "Output all the IDs used by a mod.";
    }
}
