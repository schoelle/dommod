package de.fams.dommod.tasks;

import de.fams.dommod.Definition;
import de.fams.dommod.DmFile;
import de.fams.dommod.EntityType;
import org.apache.commons.text.WordUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Info implements Task {

    @Override
    public void process(DmFile mod, List<String> arguments) {
        print("Mod Name", mod.getModName());
        print("Icon", mod.getIcon());
        print("Version", mod.getVersion());
        print("Dom Version", mod.getDomVersion());
        if (mod.getDescription() != null) {
            System.out.println("\nDescription:");
            System.out.println(WordUtils.wrap(mod.getDescription(), 80));
        }
        System.out.println("");
        print("Commands", Integer.toString(mod.commands.size()));
        print("All Definitions", Integer.toString(mod.definitions.size()));
        for (EntityType type : EntityType.values()) {
            List<Definition> defOfType = mod.definitions.stream().filter(d -> d.getType() == type).collect(Collectors.toList());
            if (!defOfType.isEmpty()) {
                print(WordUtils.capitalizeFully(type.toString()), Integer.toString(defOfType.size()));
            }
        }
    }

    private void print(String key, String value) {
        if (value == null) {
            value = "not specified";
        }
        System.out.println(String.format("%-16s: %s", key, value));
    }

    @Override
    public String description() {
        return "Print some information about the mod";
    }
}
