package de.fams.dommod;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Reference {

    private final EntityType entityType;

    public Reference(EntityType entityType) {
        this.entityType = entityType;
    }
    public EntityType getEntityType() {
        return entityType;
    }

    public abstract boolean matches(Definition def);
    public abstract boolean matches(InspectorData.Item item);
    public abstract boolean isBuiltIn();
    public abstract boolean isInvalid();
    public abstract boolean isById();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference that = (Reference) o;
        return o.toString().equalsIgnoreCase(toString());
    }

    public List<Definition> findIn(DmFile mod) {
        return mod.definitions.stream().filter(this::matches).collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return toString().toLowerCase().hashCode();
    }

}
