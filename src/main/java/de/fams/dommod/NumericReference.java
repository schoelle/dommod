package de.fams.dommod;

import java.util.Optional;

public class NumericReference extends Reference {

    private final int id;

    public NumericReference(EntityType entityType, int id) {
        super(entityType);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", getEntityType(), id);
    }

    @Override
    public boolean matches(Definition def) {
        EntityType defType = def.getType();
        if (defType != getEntityType()) {
            return false;
        }
        if (id <= -1000 && id >= -100000) {
            // Look for montag
            for (Command cmd : def.commands) {
                if (cmd.name.equals("montag")) {
                    Optional<Integer> numarg = cmd.getNumericArgument();
                    if (numarg.isEmpty()) {
                        continue;
                    }
                    int val = -numarg.get();
                    if (val == id) {
                        return true;
                    }
                }
            }
        }
        Optional<Integer> defId = def.getId();
        if (defId.isEmpty()) {
            return false;
        }
        return defId.get() == id;
    }

    @Override
    public boolean matches(InspectorData.Item item) {
        if (item.type != getEntityType()) {
            return false;
        }
        return id == item.id;
    }

    @Override
    public boolean isBuiltIn() {
        if (getEntityType() == EntityType.MONSTER) {
            if (id >= -18 && id <= -2) {
                return true; // monster type reference
            }
        }
        if (getEntityType() == EntityType.NATION) {
            if (id >= -2 && id <= -1) {
                return true; // random or province owner
            }
        }
        return id >= getEntityType().minId && id <= getEntityType().minModId;
    }

    @Override
    public boolean isInvalid() {
        if (getEntityType() == EntityType.MONSTER) {
            if (id >= -100000 && id <= -1000) {
                return false; // montag reference
            }
            if (id >= -18 && id <= -2) {
                return false; // monster type reference
            }
        }
        if (getEntityType() == EntityType.NATION) {
            if (id >= -2 && id <= -1) {
                return false; // random or province owner
            }
        }
        return id < getEntityType().minId || id > getEntityType().maxId;
    }

    @Override
    public boolean isById() {
        return true;
    }
}
