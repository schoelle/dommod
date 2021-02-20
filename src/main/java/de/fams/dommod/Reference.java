package de.fams.dommod;

public abstract class Reference {

    public final EntityType entityType;

    public Reference(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public abstract boolean matches(Definition def);
    public abstract boolean matches(InspectorData.Item item);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference that = (Reference) o;
        return o.toString().equalsIgnoreCase(toString());
    }

    @Override
    public int hashCode() {
        return toString().toLowerCase().hashCode();
    }

    public abstract boolean isBuiltIn();
    public abstract boolean isInvalid();
    public abstract boolean isById();
}
