package de.fams.dommod.tasks;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.fams.dommod.DmFile;
import de.fams.dommod.IdSetParser;
import de.fams.dommod.NumericReference;
import de.fams.dommod.UsageSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Avoid implements Task {

    IdSetParser idParser = new IdSetParser();

    @Override
    public void process(DmFile mod, List<String> arguments) {
        if (arguments.size() != 2) {
            System.out.println("avoid takes two arguments, usage id input file name and mod output file name");
            return;
        }
        UsageSet avoidIds = idParser.parse(arguments.get(0));
        UsageSet usedIds = mod.getUsage();
        Map<NumericReference, NumericReference> renameMap = computeRenameMap(usedIds, avoidIds);
        for (Map.Entry<NumericReference, NumericReference> entry: renameMap.entrySet()) {
            System.out.println(String.format("Chaning ID from %s to %s", entry.getKey(), entry.getValue()));
            mod.rename(entry.getKey(), entry.getValue());
        }
        Map<Integer, Integer> montagMap = computeMontagMap(usedIds, avoidIds);
        for (Map.Entry<Integer, Integer> entry : montagMap.entrySet()) {
            System.out.println(String.format("Chaning monster tag %d to %d", entry.getKey(), entry.getValue()));
            mod.renameMonTag(entry.getKey(), entry.getValue());
        }
        Output output = new Output();
        output.process(mod, arguments.subList(1,2));
    }

    private Map<Integer, Integer> computeMontagMap(UsageSet usedIds, UsageSet avoidIds) {
        Map<Integer, Integer> result = Maps.newHashMap();
        Set<Integer> allIds = Sets.newHashSet(Sets.union(usedIds.getMontagUsage(), avoidIds.getMontagUsage()));
        Set<Integer> bothIds = Sets.intersection(usedIds.getMontagUsage(), avoidIds.getMontagUsage());
        int newId = 1000;
        for (int id : bothIds) {
            while (allIds.contains(newId)) newId++;
            allIds.add(newId);
            result.put(id, newId);
        }
        return result;
    }

    private Map<NumericReference, NumericReference> computeRenameMap(UsageSet usedIds, UsageSet avoidIds) {
        Map<NumericReference, NumericReference> result = Maps.newHashMap();
        UsageSet allIds = new UsageSet();
        usedIds.getReferences().forEach(allIds::add);
        avoidIds.getReferences().forEach(allIds::add);
        UsageSet bothIds = usedIds.intersected(avoidIds);
        for (NumericReference id : bothIds.getReferences()) {
            NumericReference newId = allIds.findUnused(id.getEntityType());
            allIds.add(newId);
            result.put(id, newId);
        }
        return result;
    }

    @Override
    public String description() {
        return null;
    }
}
