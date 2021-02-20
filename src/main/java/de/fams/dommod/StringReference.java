package de.fams.dommod;

public class StringReference extends Reference {

    public final String name;

    public StringReference(EntityType entityType, String name) {
        super(entityType);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s(\"%s\")", entityType, name);
    }

    @Override
    public boolean matches(Definition def) {
        EntityType defType = def.getType();
        if (defType != getEntityType()) {
            return false;
        }
        String defName = def.getName();
        if (defName == null) {
            return false;
        }
        return name.equalsIgnoreCase(defName);
    }

    @Override
    public boolean matches(InspectorData.Item item) {
        if (item.type != getEntityType()) {
            return false;
        }
        if (item.name == null) {
            return false;
        }
        return name.equalsIgnoreCase(item.name);
    }

    @Override
    public boolean isBuiltIn() {
        return InspectorData.getBuiltIn(entityType).stream().anyMatch(i -> matches(i));
    }

    @Override
    public boolean isInvalid() {
        return name.trim().length() != name.length();
    }

    @Override
    public boolean isById() {
        return false;
    }
}
