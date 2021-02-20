package de.fams.dommod;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UsageSet {

    private final Map<EntityType, Set<Integer>> idUsage = Maps.newHashMap();
    private final Set<Integer> montagUsage = Sets.newHashSet();

    public void add(NumericReference ref) {
        Set<Integer> idSet = idUsage.computeIfAbsent(ref.entityType, x -> Sets.newHashSet());
        idSet.add(ref.getId());
    }

    public void addMontag(int id) {
        montagUsage.add(id);
    }

    public Map<EntityType, Set<Integer>> getIdUsage() {
        return idUsage;
    }

    public Set<NumericReference> getReferences() {
        Set<NumericReference> result = Sets.newHashSet();
        for (Map.Entry<EntityType, Set<Integer>> entry : idUsage.entrySet()) {
            result.addAll(entry.getValue().stream().map(s -> new NumericReference(entry.getKey(), s)).collect(Collectors.toList()));
        }
        return result;
    }

    public Set<Integer> getMontagUsage() {
        return montagUsage;
    }
}
