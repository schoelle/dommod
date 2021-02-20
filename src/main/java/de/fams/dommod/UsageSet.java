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

    public boolean has(NumericReference ref) {
        Set<Integer> ids = idUsage.get(ref.entityType);
        if (ids != null) {
            return ids.contains(ref.getId());
        }
        return false;
    }

    public void add(UsageSet other) {
        montagUsage.addAll(other.getMontagUsage());
        other.getReferences().stream().forEach(this::add);
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

    public UsageSet intersected(UsageSet other) {
        UsageSet result = new UsageSet();
        Set<Integer> montagSet = Sets.intersection(montagUsage, other.getMontagUsage());
        montagSet.stream().forEach(e -> result.addMontag(e));
        Set<NumericReference> refSet = Sets.intersection(getReferences(), other.getReferences());
        refSet.stream().forEach(r -> result.add(r));
        return result;
    }

    public NumericReference findUnused(EntityType type) {
        int id = type.minModId;
        Set<Integer> usedIds = idUsage.get(type);
        if (usedIds != null) {
            while (usedIds.contains(id))  {
                id++;
                if (id > type.maxId) {
                    throw new RuntimeException("Out of available IDs for type " + type);
                }
            }
        }
        return new NumericReference(type, id);
    }

    public Set<Integer> getMontagUsage() {
        return montagUsage;
    }
}
