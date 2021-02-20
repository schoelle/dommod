package de.fams.dommod.tasks;

import de.fams.dommod.Definition;
import de.fams.dommod.DmFile;
import de.fams.dommod.Reference;

import java.io.BufferedWriter;
import java.io.IOException;

public class UsedIds extends OutputFileTask {

    @Override
    public void doOutput(DmFile mod, BufferedWriter writer) throws IOException {
        for (Definition def : mod.definitions) {
            Reference ref = def.getSelfReference();
            if (ref != null) {
                writer.write(ref.toString() + "\n");
            }
        }
    }

    @Override
    public String description() {
        return "Output all the IDs used by a mod.";
    }
}
