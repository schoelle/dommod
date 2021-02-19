package de.fams.dommod.tasks;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import de.fams.dommod.*;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class SliceNation implements Task {

    DmFile mod;

    @Override
    public void process(DmFile mod, List<String> arguments) {
        this.mod = mod;
        if (arguments.size() != 2) {
            System.out.println("slicenation takes two arguments, the number of the nation and the name of the output file");
            return;
        }
        int nationId = Integer.valueOf(arguments.get(0));
        Definition nation = findNation(nationId);
        if (nation == null) {
            System.out.println("Unable to find specified nation: " + nationId);
            return;
        }
        List<Command> commandToRemove = Lists.newArrayList();
        for (Command cmd: mod.commands) {
            if (StaticTables.NATION_CMDS.contains(cmd.name) && !StaticTables.STARTCMD_FOR_NAME.containsKey(cmd.name)) {
                ReferenceCommand ref = cmd.getReferenceCommand();
                if (ref != null && !ref.getTargets().contains(nation)) {
                    commandToRemove.add(cmd);
                }
            }
        }
        mod.removeCommands(commandToRemove);
        nation = findNation(nationId);
        String nationName = nation.getName();
        System.out.println(String.format("Extracting nation %d: %s", nation.getId().get(), nationName));
        List<Definition> spells = findSpells(nationId, nationName);
        for (Definition spell: spells) {
            System.out.println(String.format("National %s", spell.toString()));
        }
        List<Definition> required = Lists.newArrayList();
        required.add(nation);
        required.addAll(spells);

        List<Definition> needed = dfs(required);
        List<Definition> notNeeded = mod.definitions.stream().filter(d -> !needed.contains(d)).collect(Collectors.toList());
        commandToRemove.clear();
        for (Definition def: notNeeded) {
            System.out.println("Removing: " + def.toString());
            commandToRemove.addAll(def.commands);
        }
        Command cmd = mod.getGlobalCommand("disableoldnations");
        if (cmd != null) {
            commandToRemove.add(cmd);
        }
        mod.removeCommands(commandToRemove);
        for (Definition n: mod.definitions) {
            System.out.println("Keeping: " + n.toString());
        }

        changeGlobalCommand("description", Argument.Type.STRING, "Extracted from mod " + mod.getModName());
        changeGlobalCommand("modname", Argument.Type.STRING, nationName);


        Output output = new Output();
        output.process(mod, arguments.subList(1, 2));
    }

    private List<Definition> dfs(List<Definition> required) {
        List<Definition> result = Lists.newArrayList();
        Queue<Definition> todo = Queues.newArrayDeque();
        todo.addAll(required);
        while (!todo.isEmpty()) {
            Definition def = todo.remove();
            result.add(def);
            for (Command cmd: def.commands) {
                ReferenceCommand ref = cmd.getReferenceCommand();
                if (ref != null) {
                    for (Definition target: ref.getTargets()) {
                        if (!result.contains(target)) {
                            todo.add(target);
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<Definition> findSpells(int nationId, String nationName) {
        return mod.definitions.stream().filter(d -> {
                    if (d.getType() != EntityType.SPELL) {
                        return false;
                    }
                    Command restricted = d.commands.stream().filter(c -> c.hasName("restricted")).findAny().orElse(null);
                    if (restricted == null || restricted.arguments.isEmpty()) {
                        return false;
                    }
                    Argument arg = restricted.arguments.get(0);
                    if (arg.type == Argument.Type.NUMBER) {
                        return Integer.valueOf(arg.value) == nationId;
                    } else {
                        return nameMatch(arg.value, nationName);
                    }
                }).collect(Collectors.toList());
    }

    private boolean nameMatch(String a, String b) {
        return a != null && b != null && a.toLowerCase().equals(b.toLowerCase());
    }

    private Definition findNation(int id) {
        for (Definition def : mod.definitions) {
            if (def.getType() == EntityType.NATION && def.getId().isPresent() && def.getId().get() == id) {
                return def;
            }
        }
        return null;
    }

    private void changeGlobalCommand(String name, Argument.Type type, String value) {
        Command origCmd = mod.getGlobalCommand("modname");
        Command newCmd = new Command(origCmd.line, null, origCmd.name, Lists.newArrayList(new Argument(type, value)), null);
        origCmd.replaceBy(newCmd);
    }

    @Override
    public String description() {
        return "Slices a nation from a mod into a new mod. Warning: removes all events.";
    }
}
