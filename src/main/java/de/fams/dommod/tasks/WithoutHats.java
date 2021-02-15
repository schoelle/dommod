package de.fams.dommod.tasks;

import de.fams.dommod.Definition;
import de.fams.dommod.DmFile;
import de.fams.dommod.EntityType;

import java.util.List;

public class WithoutHats implements Task {


    @Override
    public void process(DmFile mod, List<String> arguments) {
        for (Definition def: mod.definitions) {
            if (def.getType() != EntityType.MONSTER) {
                continue;
            }

        }
    }

    @Override
    public String description() {
        return "Lists all monsters that do not wear hats.";
    }
}
